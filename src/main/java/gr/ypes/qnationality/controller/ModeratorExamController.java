package gr.ypes.qnationality.controller;

import gr.ypes.qnationality.model.Exam;
import gr.ypes.qnationality.service.IExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Secured("MODERATOR")
@RequestMapping("/moderator/exam")
public class ModeratorExamController {

    @Autowired
    IExamService examService;

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public ModelAndView showExam(@RequestParam(name = "uid", required = false, defaultValue = "") String uid){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("moderator/exam/showExam");
        if (uid.equals("")){
            return modelAndView;
        }else{
            Exam exam = examService.findByUID(uid);
            if(exam == null){
                modelAndView.addObject("errorMessageBox","Exam with uid: " + uid + " does not exist or has been deleted.");
            }else{
                modelAndView.addObject("exam", exam);
            }
        }
        return modelAndView;
    }

    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    public ModelAndView validateExam(@RequestParam(name = "uid") String uid,
                                     @RequestParam(name = "validationstatus") String validationStatus,
                                     Authentication authentication){
        ModelAndView modelAndView = new ModelAndView();
        Exam exam = examService.findByUID(uid);

        if(exam != null){
            try{
                examService.validateExam(exam,authentication.getName(), validationStatus.equals("true"), true);
                modelAndView.addObject("exam", exam);
                modelAndView.addObject("successMessageBox","exam with uid: " + uid + " validated successfully");
            }catch (Exception e){
                modelAndView.addObject("errorMessageBox", "Error: " + e.getMessage());
            }
        }
        modelAndView.setViewName("moderator/exam/showExam");
        return modelAndView;

    }

}
