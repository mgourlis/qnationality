package gr.ypes.qnationality.controller;

import gr.ypes.qnationality.dto.QuestionCategoryDTO;
import gr.ypes.qnationality.model.Question;
import gr.ypes.qnationality.model.QuestionCategory;
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
@RequestMapping("/admin/question-category")
public class QuestionCategoryController {

    @Autowired
    IQuestionCategoryService questionCategoryService;

    @Autowired
    IQuestionService questionService;

    @RequestMapping("/")
    public ModelAndView getQuestionCategories(ModelAndView modelAndView, Pageable pageable){
        modelAndView = new ModelAndView();
        Page<QuestionCategory> categoryPage = questionCategoryService.findAll(pageable);
        modelAndView.addObject("questionCategories", categoryPage.getContent());
        modelAndView.addObject("page",categoryPage);
        modelAndView.setViewName("admin/category/showCategories");
        return modelAndView;
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ModelAndView showQuestionCategory(@PathVariable("id") long id){
        ModelAndView modelAndView = new ModelAndView();
        QuestionCategory questionCategory = questionCategoryService.getOne(id);
        if(questionCategory != null) {
            modelAndView.addObject("questionCategory", questionCategory);
            modelAndView.setViewName("admin/category/showCategory");
            return modelAndView;
        }else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value="/edit/{id}", method = RequestMethod.GET)
    public ModelAndView editQuestionCategory(@PathVariable("id") long id){
        ModelAndView modelAndView = new ModelAndView();
        QuestionCategory questionCategory = questionCategoryService.getOne(id);
        if(questionCategory == null){
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        QuestionCategoryDTO questionCategoryDTO = new QuestionCategoryDTO();
        questionCategoryDTO.init(questionCategory);
        modelAndView.addObject("questionCategory", questionCategoryDTO);
        modelAndView.setViewName("/admin/category/editCategory");
        return modelAndView;
    }

    @RequestMapping(value="/edit/{id}", method = RequestMethod.POST)
    public ModelAndView editQuestionCategory(@PathVariable("id") long id, @Valid @ModelAttribute("questionCategory") QuestionCategoryDTO questionCategoryDTO, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();
        QuestionCategory editQuestionCategory = questionCategoryService.getOne(id);
        if(editQuestionCategory == null){
            throw new EntityNotFoundException();
        }
        if(questionCategoryService.findQuestionCategoryByName(questionCategoryDTO.getName()) != null){
            bindingResult
                    .rejectValue("name", "error.questionCategory",
                            "There is already a Question Category with the name provided");
        }
        if (bindingResult.hasErrors()){
            modelAndView.setViewName("/admin/category/editCategory");
        }
        else{
            editQuestionCategory.setName(questionCategoryDTO.getName());
            questionCategoryService.save(editQuestionCategory);
            modelAndView.setViewName("redirect:/admin/question-category/" + editQuestionCategory.getId());
        }
        return modelAndView;
    }

    @RequestMapping(value="/new", method = RequestMethod.GET)
    public ModelAndView newQuestionCategory(){
        ModelAndView modelAndView = new ModelAndView();
        QuestionCategoryDTO questionCategoryDTO = new QuestionCategoryDTO();
        modelAndView.addObject("questionCategory", questionCategoryDTO);
        modelAndView.setViewName("admin/category/newCategory");
        return modelAndView;
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public ModelAndView createNewQuestionCategory(@Valid @ModelAttribute("questionCategory") QuestionCategoryDTO questionCategoryDTO, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        QuestionCategory questionCategoryExists = questionCategoryService.findQuestionCategoryByName(questionCategoryDTO.getName());
        if (questionCategoryExists != null) {
            bindingResult
                    .rejectValue("name", "error.questionCategory",
                            "There is already a Question Category with the name provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("admin/category/newCategory");
        } else {
            QuestionCategory questionCategory = new QuestionCategory();
            questionCategory.setName(questionCategoryDTO.getName());
            questionCategoryService.save(questionCategory);
            modelAndView.addObject("successMessageBox", "Question Category has been created successfully");
            modelAndView.addObject("questionCategory", new QuestionCategory());
            modelAndView.setViewName("admin/category/newCategory");

        }
        return modelAndView;
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.GET)
    public ModelAndView deleteQuestionCategoty(@PathVariable("id") long id) {
        ModelAndView modelAndView = new ModelAndView();
        QuestionCategory questionCategory = questionCategoryService.getOne(id);

        if (questionCategory != null){
            List<Question> questionList = questionService.findQuestionsByQuestionCategoryName(questionCategory.getName());
            if(!questionList.isEmpty()){
                modelAndView.addObject("errorMessageBox","Questions exists in this Question category. Please delete the questions first");
                modelAndView.setViewName("admin/category/showCategories");
            }
        }else{
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }

        questionCategoryService.delete(id);
        modelAndView.setViewName("redirect:/admin/question-category/");
        return modelAndView;
    }
}
