package gr.ypes.qnationality.controller;

import gr.ypes.qnationality.dto.ExamSettingDTO;
import gr.ypes.qnationality.dto.QuestionCategoryAndDifficultySettingDTO;
import gr.ypes.qnationality.model.Difficulty;
import gr.ypes.qnationality.model.ExamSetting;
import gr.ypes.qnationality.model.QuestionCategory;
import gr.ypes.qnationality.model.QuestionCategoryAndDifficultySetting;
import gr.ypes.qnationality.service.IDifficultyService;
import gr.ypes.qnationality.service.IExamSettingService;
import gr.ypes.qnationality.service.IQuestionCategoryService;
import gr.ypes.qnationality.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Controller
@Secured("ADMIN")
@RequestMapping("/admin/exam-setting")
public class ExamSettingController {

    @Autowired
    IExamSettingService examSettingService;

    @Autowired
    IQuestionCategoryService questionCategoryService;

    @Autowired
    IDifficultyService difficultyService;

    @Autowired
    IQuestionService questionService;

    @RequestMapping("/")
    public ModelAndView getExamSettings(ModelAndView modelAndView, Pageable pageable){
        Page<ExamSetting> examSettingPage = examSettingService.findAll(pageable);
        modelAndView.addObject("examSettings", examSettingPage.getContent());
        modelAndView.addObject("page",examSettingPage);
        modelAndView.setViewName("admin/examsetting/showExamSettings");
        return modelAndView;
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ModelAndView showExamSetting(@PathVariable("id") long id){
        ModelAndView modelAndView = new ModelAndView();
        ExamSetting examSetting = examSettingService.getOne(id);
        if(examSetting != null){
            modelAndView.addObject("examSetting", examSetting);
            modelAndView.setViewName("admin/examsetting/showExamSetting");
            return modelAndView;
        }else{
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value="/edit/{id}", method = RequestMethod.GET)
    public ModelAndView editExamSetting(@PathVariable("id") long id){
        ModelAndView modelAndView = new ModelAndView();
        ExamSetting examSetting = examSettingService.getOne(id);
        ExamSettingDTO examSettingDTO = new ExamSettingDTO();
        examSettingDTO.init(examSetting);
        if(examSetting == null){
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        modelAndView.addObject("examSetting", examSettingDTO);
        modelAndView.setViewName("/admin/examsetting/editExamSetting");
        return modelAndView;
    }

    @RequestMapping(value="/edit/{id}", method = RequestMethod.POST)
    public ModelAndView editExamSetting(@PathVariable("id") long id, @Valid @ModelAttribute("examSettingDTO") ExamSettingDTO examSettingDTO, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();
        ExamSetting editExamSetting = examSettingService.getOne(id);
        boolean checkFlag = false;
        String errorMessageBox = "";
        if(editExamSetting == null){
            throw new EntityNotFoundException();
        }

        //TODO - check if used

        ExamSetting checkName = examSettingService.findExamSettingByName(editExamSetting.getName());
        if(checkName != null){
            if(checkName.getId() != editExamSetting.getId()) {
                bindingResult
                        .rejectValue("name", "error.examSetting",
                                "There is already an Exam Setting with the name provided");
            }
        }
        float difficultySum = 0;
        int index=0;
        List<QuestionCategoryAndDifficultySetting> inputQuestionCategoryAndDifficultySettings = editExamSetting.getQuestionCategoryAndDifficultySettings();
        for (QuestionCategoryAndDifficultySettingDTO qCADDTO : examSettingDTO.getQuestionCategoryAndDifficultySettings()) {
            int maxQuestions = questionService.countQuestionsByQuestionCategoryNameAndDifficultyLevelNumber(qCADDTO.getQuestionCategoryName(),qCADDTO.getDifficultyLevelNumber());
            if(qCADDTO.getNumOfQuestions() < 0 || qCADDTO.getNumOfQuestions() > maxQuestions){
                bindingResult
                        .rejectValue("QuestionCategoryAndDifficultySettings[" + index + "]", "error.examSetting.difficultySettings",
                                "Error! the question number must be between Min: 0 and Max:" + maxQuestions);
            }
            try {
                inputQuestionCategoryAndDifficultySettings.get(index).setNumOfQuestions(examSettingDTO.getQuestionCategoryAndDifficultySettings().get(index).getNumOfQuestions());
            }catch (IndexOutOfBoundsException e){
                errorMessageBox += "Something went wrong, please retry.\n";
                checkFlag = true;
            }
            index++;
        }
        if (bindingResult.hasErrors() || checkFlag) {
            if (!errorMessageBox.equals("")){
                modelAndView.addObject("errorMessageBox", errorMessageBox);
            }
            modelAndView.addObject("difficulties", difficultyService.findAll());
            modelAndView.addObject("questionCategories",questionCategoryService.findAll());
            modelAndView.setViewName("/admin/examsetting/editExamSetting");
        }
        else{
            editExamSetting.setName(examSettingDTO.getName());
            editExamSetting.setEnabled(examSettingDTO.isEnabled());
            editExamSetting.setQuestionCategoryAndDifficultySettings(inputQuestionCategoryAndDifficultySettings);
            examSettingService.save(editExamSetting);
            modelAndView.setViewName("redirect:/admin/exam-setting/" + editExamSetting.getId());
        }
        return modelAndView;
    }

    @RequestMapping(value="/new", method = RequestMethod.GET)
    public ModelAndView newExamSetting(){
        ModelAndView modelAndView = new ModelAndView();
        ExamSettingDTO examSettingDTO = new ExamSettingDTO();
        List<Difficulty> difficulties = difficultyService.findAll();
        List<QuestionCategory> questionCategories = questionCategoryService.findAll();
        examSettingDTO.init(questionCategories, difficulties);
        modelAndView.addObject("examSetting", examSettingDTO);
        modelAndView.setViewName("admin/examsetting/newExamSetting");
        return modelAndView;
    }

    @RequestMapping(value="/new", method = RequestMethod.POST)
    public ModelAndView newExamSetting(@Valid @ModelAttribute("examSetting") ExamSettingDTO examSettingDTO, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();
        ExamSetting newExamSetting = examSettingService.createExamSetting();
        boolean checkFlag = false;
        String errorMessageBox = "";
        ExamSetting checkName = examSettingService.findExamSettingByName(examSettingDTO.getName());
        if(checkName != null){
            bindingResult
                    .rejectValue("name", "error.examSetting",
                            "There is already an Exam Setting with the name provided");
        }
        int index = 0;
        List<QuestionCategoryAndDifficultySetting> inputQuestionCategoryAndDifficultySettings = newExamSetting.getQuestionCategoryAndDifficultySettings();
        for (QuestionCategoryAndDifficultySettingDTO qCADDTO : examSettingDTO.getQuestionCategoryAndDifficultySettings()) {
            int maxQuestions = questionService.countQuestionsByQuestionCategoryNameAndDifficultyLevelNumber(qCADDTO.getQuestionCategoryName(),qCADDTO.getDifficultyLevelNumber());
            if(qCADDTO.getNumOfQuestions() < 0 || qCADDTO.getNumOfQuestions() > maxQuestions){
                bindingResult
                        .rejectValue("QuestionCategoryAndDifficultySettings[" + index + "]", "error.examSetting.difficultySettings",
                                "Error! the question number must be between Min: 0 and Max:" + maxQuestions);
            }
            try {
                inputQuestionCategoryAndDifficultySettings.get(index).setNumOfQuestions(examSettingDTO.getQuestionCategoryAndDifficultySettings().get(index).getNumOfQuestions());
            }catch (IndexOutOfBoundsException e){
                errorMessageBox += "Something went wrong, please retry.\n";
                checkFlag = true;
            }
            index++;
        }
        if (bindingResult.hasErrors() || checkFlag) {
            if (!errorMessageBox.equals("")){
                modelAndView.addObject("errorMessageBox", errorMessageBox);
            }
            modelAndView.setViewName("/admin/examsetting/newExamSetting");
        }
        else{
            newExamSetting.setName(examSettingDTO.getName());
            newExamSetting.setEnabled(examSettingDTO.isEnabled());
            newExamSetting.setQuestionCategoryAndDifficultySettings(inputQuestionCategoryAndDifficultySettings);
            examSettingService.save(newExamSetting);

            ExamSettingDTO newExamSettingDTO = new ExamSettingDTO();
            List<Difficulty> difficulties = difficultyService.findAll();
            List<QuestionCategory> questionCategories = questionCategoryService.findAll();
            newExamSettingDTO.init(questionCategories, difficulties);
            modelAndView.addObject("examSetting", newExamSettingDTO);
            modelAndView.addObject("successMessageBox", "Exam Setting was created successfully");
            modelAndView.setViewName("admin/examsetting/newExamSetting");
        }
        return modelAndView;
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.GET)
    public ModelAndView deleteQuestion(@PathVariable("id") long id){
        ModelAndView modelAndView = new ModelAndView();
        examSettingService.delete(id);
        modelAndView.setViewName("redirect:/admin/exam-setting/");
        return modelAndView;
    }


}
