package gr.ypes.qnationality.controller;

import gr.ypes.qnationality.dto.CreateExamDTO;
import gr.ypes.qnationality.model.Exam;
import gr.ypes.qnationality.model.ExamSetting;
import gr.ypes.qnationality.model.User;
import gr.ypes.qnationality.service.IExamService;
import gr.ypes.qnationality.service.IExamSettingService;
import gr.ypes.qnationality.service.IUserService;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.sql.DataSource;
import javax.validation.Valid;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@Secured("USER")
@RequestMapping("/user/exam")
public class UserExamController {

    @Autowired
    IExamService examService;

    @Autowired
    IExamSettingService examSettingService;

    @Autowired
    IUserService userService;

    @Autowired
    JRFileVirtualizer fv;

    @Autowired
    DataSource datasource;

    @Autowired
    JasperReport jasperReport;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value= "/new", method = RequestMethod.GET)
    public ModelAndView createExam(Authentication authentication){
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.findUserByEmail(authentication.getName());
        CreateExamDTO createExamDTO = new CreateExamDTO();
        createExamDTO.setForeas(user.getForeas());
        try {
            Exam exam = examService.findLastByUser(authentication.getName());
            modelAndView.addObject("examcreatedtime", exam.getCreatedAt().getTime());
            modelAndView.addObject("downloadtrue", "true");
        }catch (Exception e){
            modelAndView.addObject("downloadtrue", "false");
        }
        modelAndView.addObject("examSettings", examSettingService.findAllSortedAsc());
        modelAndView.addObject("exam",createExamDTO);
        modelAndView.setViewName("user/exam/createExam");
        return modelAndView;
    }

    @RequestMapping(value= "/new", method = RequestMethod.POST)
    public ModelAndView createExam(@Valid @ModelAttribute("exam") CreateExamDTO createExamDTO,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes,
                                   Authentication authentication){
        ModelAndView modelAndView = new ModelAndView();
        ExamSetting examSetting = examSettingService.getOne(createExamDTO.getExamSettingId());
        if(examSetting == null){
            bindingResult
                    .rejectValue("examSettingId", "errors.exam.user.examSettingId",
                            "Exam Setting does not exist");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("examSettings", examSettingService.findAllSortedAsc());
            modelAndView.setViewName("user/exam/createExam");
        } else {
            try {
                String messageParams[] = new String[1];
                messageParams[0] = examService.createExam(createExamDTO.getExam(examSetting),createExamDTO.getExamSettingId());
                modelAndView.addObject("examcreatedtime", (new Date()).getTime());
                modelAndView.addObject("successMessageBox",messageSource.getMessage("success.message.createexam", messageParams, LocaleContextHolder.getLocale())); //exam with uid: " + uid + " created successfully
                modelAndView.addObject("downloadtrue", "true");
            }catch (Exception e){
                modelAndView.addObject("examSettings", examSettingService.findAllSortedAsc());
                String messageParams[] = new String[1];
                modelAndView.addObject("errorMessageBox", messageSource.getMessage(e.getMessage(),messageParams, LocaleContextHolder.getLocale()));
                Exam lastExam =examService.findLastByUser(authentication.getName());
                if(lastExam != null) {
                    modelAndView.addObject("examcreatedtime", lastExam.getCreatedAt().getTime());
                    modelAndView.addObject("downloadtrue", "true");
                }
            }
            modelAndView.addObject("examSettings", examSettingService.findAllSortedAsc());
            modelAndView.setViewName("user/exam/createExam");
        }
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> getExamQuestionReport(Authentication authentication) {
        Map<String, Object> m = new HashMap<>();

        m.put(JRParameter.REPORT_VIRTUALIZER, fv);

        String email = authentication.getName();
        try {
            String exam_uid = examService.findLastByUser(email).getuID();
            m.put("exam_uid", exam_uid);
            String name = email + ".pdf";
            return generateReport(name, m);
        }
        catch(Exception e){
            return new ResponseEntity<InputStreamResource>(HttpStatus.NOT_FOUND);
        }
    }

    private ResponseEntity<InputStreamResource> generateReport(String name, Map<String, Object> params) {
        FileInputStream st = null;
        Connection cc = null;
        try {
            cc = datasource.getConnection();
            JasperPrint p = JasperFillManager.fillReport(jasperReport, params, cc);
            JRPdfExporter exporter = new JRPdfExporter();
            SimpleOutputStreamExporterOutput c = new SimpleOutputStreamExporterOutput(name);
            exporter.setExporterInput(new SimpleExporterInput(p));
            exporter.setExporterOutput(c);
            exporter.exportReport();

            st = new FileInputStream(name);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.valueOf("application/pdf"));
            responseHeaders.setContentDispositionFormData("attachment", name);
            responseHeaders.setContentLength(st.available());
            return new ResponseEntity<InputStreamResource>(new InputStreamResource(st), responseHeaders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fv.cleanup();
            if (cc != null)
                try {
                    cc.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }
}
