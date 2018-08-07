package gr.ypes.qnationality.controller;

import gr.ypes.qnationality.dto.QuestionDTO;
import gr.ypes.qnationality.model.Difficulty;
import gr.ypes.qnationality.model.Question;
import gr.ypes.qnationality.model.QuestionCategory;
import gr.ypes.qnationality.service.IDifficultyService;
import gr.ypes.qnationality.service.IQuestionCategoryService;
import gr.ypes.qnationality.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@Controller
@Secured("ADMIN")
@RequestMapping("/admin/question")
public class QuestionController {

    @Autowired
    IQuestionService questionService;

    @Autowired
    IQuestionCategoryService questionCategoryService;

    @Autowired
    IDifficultyService difficultyService;

    @RequestMapping("/")
    public ModelAndView getQuestions(@RequestParam(value = "qcategory", required=false, defaultValue = "") String qcategory,
                                     @RequestParam(value = "qdifficulty", required=false, defaultValue = "0") String qdifficulty,
                                     ModelAndView modelAndView, Pageable pageable){
        Page<Question> categoryPage;
        int difficulty;
        try{
            difficulty = Integer.parseInt(qdifficulty);
        }catch (NumberFormatException e){
            difficulty = 0;
        }
        if(qcategory.equals("") && difficulty == 0) {
            categoryPage = questionService.findAll(pageable);
        }else if(difficulty == 0){
            categoryPage = questionService.findQuestionsByQuestionCategoryName(qcategory,pageable);
        }else if(qcategory.equals("")){
            categoryPage = questionService.findQuestionsByDifficultyLevelNumber(difficulty, pageable);
        }else{
            categoryPage = questionService.findQuestionsByQuestionCategoryNameAndDifficultyLevelNumber(qcategory,difficulty, pageable);
        }
        modelAndView.addObject("difficulties",difficultyService.findAll());
        modelAndView.addObject("questionCategories",questionCategoryService.findAll());
        modelAndView.addObject("questions", categoryPage.getContent());
        modelAndView.addObject("page",categoryPage);
        modelAndView.setViewName("admin/question/showQuestions");
        return modelAndView;
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ModelAndView showQuestion(@PathVariable("id") long id){
        ModelAndView modelAndView = new ModelAndView();
        Question question = questionService.getOne(id);
        if(question != null) {
            modelAndView.addObject("question", question);
            modelAndView.setViewName("admin/question/showQuestion");
            return modelAndView;
        }else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value="/edit/{id}", method = RequestMethod.GET)
    public ModelAndView editQuestion(@PathVariable("id") long id){
        ModelAndView modelAndView = new ModelAndView();
        Question question = questionService.getOne(id);
        if(question == null){
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.init(question);
        modelAndView.addObject("questionCategories",questionCategoryService.findAll());
        modelAndView.addObject("difficulties",difficultyService.findAll());
        modelAndView.addObject("question", questionDTO);
        modelAndView.setViewName("/admin/question/editQuestion");
        return modelAndView;
    }

    @RequestMapping(value="/edit/{id}", method = RequestMethod.POST)
    public ModelAndView editQuestion(@PathVariable("id") long id, @Valid @ModelAttribute("question") QuestionDTO questionDTO, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();
        Question editQuestion = questionService.getOne(id);
        if(editQuestion == null){
            throw new EntityNotFoundException();
        }

        //TODO - check if used

        Difficulty editDifficulty = difficultyService.getOne(questionDTO.getQuestionDifficulty());
        if(editDifficulty == null){
            bindingResult
                    .rejectValue("questionDifficulty", "error.question",
                            "The selected difficulty is invalid");
        }
        QuestionCategory editQuestionCategory = questionCategoryService.getOne(questionDTO.getQuestionCategory());
        if(editQuestionCategory == null){
            bindingResult
                    .rejectValue("questionCategory", "error.question",
                            "The selected question category is invalid");
        }
        Question checkQuestion = questionService.findQuestionByShortname(questionDTO.getShortname());
        if(checkQuestion != null){
            if(checkQuestion.getId() != editQuestion.getId()) {
                bindingResult
                        .rejectValue("shortname", "error.question",
                                "There is already a Question with the short name provided");
            }
        }
        if (bindingResult.hasErrors()){
            modelAndView.addObject("questionCategories",questionCategoryService.findAll());
            modelAndView.addObject("difficulties",difficultyService.findAll());
            modelAndView.setViewName("/admin/question/editQuestion");
        }
        else{
            editQuestion.setShortname(questionDTO.getShortname());
            editQuestion.setQuestiontext(questionDTO.getQuestiontext());
            editQuestion.setQuestionDifficulty(editDifficulty);
            editQuestion.setQuestionCategory(editQuestionCategory);
            questionService.save(editQuestion);
            modelAndView.setViewName("redirect:/admin/question/" + editQuestion.getId());
        }
        return modelAndView;
    }

    @RequestMapping(value="/new", method = RequestMethod.GET)
    public ModelAndView newQuestion(){
        ModelAndView modelAndView = new ModelAndView();
        QuestionDTO questionDTO = new QuestionDTO();
        modelAndView.addObject("questionCategories",questionCategoryService.findAll());
        modelAndView.addObject("difficulties",difficultyService.findAll());
        modelAndView.addObject("question", questionDTO);
        modelAndView.setViewName("admin/question/newQuestion");
        return modelAndView;
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public ModelAndView createNewQuestion(@Valid @ModelAttribute("question") QuestionDTO questionDTO, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        Question questionExists = questionService.findQuestionByShortname(questionDTO.getShortname());
        if (questionExists != null) {
            bindingResult
                    .rejectValue("shortname", "error.question",
                            "There is already a Question with the short name provided");
        }
        Difficulty editDifficulty = difficultyService.getOne(questionDTO.getQuestionDifficulty());
        if(editDifficulty == null){
            bindingResult
                    .rejectValue("questionDifficulty", "error.question",
                            "The selected difficulty is invalid");
        }
        QuestionCategory editQuestionCategory = questionCategoryService.getOne(questionDTO.getQuestionCategory());
        if(editQuestionCategory == null){
            bindingResult
                    .rejectValue("questionCategory", "error.question",
                            "The selected question category is invalid");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("questionCategories",questionCategoryService.findAll());
            modelAndView.addObject("difficulties",difficultyService.findAll());
            modelAndView.setViewName("admin/question/newQuestion");
        } else {
            Question question = new Question();
            question.setShortname(questionDTO.getShortname());
            question.setQuestiontext(questionDTO.getQuestiontext());
            question.setQuestionDifficulty(editDifficulty);
            question.setQuestionCategory(editQuestionCategory);
            questionService.save(question);
            modelAndView.addObject("questionCategories",questionCategoryService.findAll());
            modelAndView.addObject("difficulties",difficultyService.findAll());
            modelAndView.addObject("successMessageBox", "Question has been created successfully");
            modelAndView.addObject("question", new Question());
            modelAndView.setViewName("admin/question/newQuestion");

        }
        return modelAndView;
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.GET)
    public ModelAndView deleteQuestion(@PathVariable("id") long id){
        ModelAndView modelAndView = new ModelAndView();
        questionService.delete(id);
        modelAndView.setViewName("redirect:/admin/question/");
        return modelAndView;
    }

}
