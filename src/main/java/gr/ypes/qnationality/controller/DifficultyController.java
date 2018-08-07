package gr.ypes.qnationality.controller;

import gr.ypes.qnationality.dto.DifficultyDTO;
import gr.ypes.qnationality.model.Difficulty;
import gr.ypes.qnationality.model.Question;
import gr.ypes.qnationality.service.IDifficultyService;
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
@RequestMapping("/admin/difficulty")
public class DifficultyController {

    @Autowired
    IDifficultyService difficultyService;

    @Autowired
    IQuestionService questionService;

    @RequestMapping(value="/", method = RequestMethod.GET)
    public ModelAndView getDifficulties(ModelAndView modelAndView, Pageable pageable){
        Page<Difficulty> difficultiesPage = difficultyService.findAll(pageable);
        modelAndView.addObject("difficulties", difficultiesPage.getContent());
        modelAndView.addObject("page",difficultiesPage);
        modelAndView.setViewName("admin/difficulty/showDifficulties");
        return modelAndView;
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ModelAndView showDifficulty(@PathVariable("id") long id){
        ModelAndView modelAndView = new ModelAndView();
        Difficulty difficulty = difficultyService.getOne(id);
        if(difficulty != null){
            modelAndView.addObject("difficulty", difficulty);
            modelAndView.setViewName("admin/difficulty/showDifficulty");
            return modelAndView;
        }else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value="/edit/{id}", method = RequestMethod.GET)
    public ModelAndView editDifficulty(@PathVariable("id") long id){
        ModelAndView modelAndView = new ModelAndView();
        Difficulty difficulty = difficultyService.getOne(id);
        if(difficulty == null){
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        DifficultyDTO difficultyDTO = new DifficultyDTO();
        difficultyDTO.init(difficulty);
        modelAndView.addObject("difficulty", difficultyDTO);
        modelAndView.setViewName("/admin/difficulty/editDifficulty");
        return modelAndView;
    }

    @RequestMapping(value="/edit/{id}", method = RequestMethod.POST)
    public ModelAndView editDifficulty(@PathVariable("id") long id, @Valid @ModelAttribute("difficulty") DifficultyDTO difficultyDTO, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();
        Difficulty editdifficulty = difficultyService.getOne(id);
        if(editdifficulty == null){
            throw new EntityNotFoundException();
        }

        //TODO - check if used

        else if (bindingResult.hasErrors()){
            modelAndView.setViewName("/admin/difficulty/editDifficulty");
        }
        else{
            editdifficulty.setLevel(difficultyDTO.getLevel());
            editdifficulty.setLevelNumber(difficultyDTO.getLevelNumber());
            difficultyService.save(editdifficulty);
            modelAndView.setViewName("redirect:/admin/difficulty/" + editdifficulty.getId());
        }
        return modelAndView;
    }

    @RequestMapping(value="/new", method = RequestMethod.GET)
    public ModelAndView newDifficulty(){
        ModelAndView modelAndView = new ModelAndView();
        DifficultyDTO difficultyDTO = new DifficultyDTO();
        modelAndView.addObject("difficulty", difficultyDTO);
        modelAndView.setViewName("admin/difficulty/newDifficulty");
        return modelAndView;
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public ModelAndView createNewDifficulty(@Valid @ModelAttribute("difficulty") DifficultyDTO difficultyDTO, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        Difficulty difficultyExists = difficultyService.findByLevelNumber(difficultyDTO.getLevelNumber());
        if (difficultyExists != null) {
            bindingResult
                    .rejectValue("levelNumber", "error.difficulty",
                            "There is already a difficulty with the level number provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("admin/difficulty/newDifficulty");
        } else {
            Difficulty difficulty = new Difficulty();
            difficulty.setLevel(difficultyDTO.getLevel());
            difficulty.setLevelNumber(difficultyDTO.getLevelNumber());
            difficultyService.save(difficulty);
            modelAndView.addObject("successMessageBox", "Difficulty has been created successfully");
            modelAndView.addObject("difficulty", new DifficultyDTO());
            modelAndView.setViewName("admin/difficulty/newDifficulty");

        }
        return modelAndView;
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.GET)
    public ModelAndView deleteDifficulty(@PathVariable("id") long id){
        ModelAndView modelAndView = new ModelAndView();
        Difficulty difficulty = difficultyService.getOne(id);

        if (difficulty != null){
            List<Question> questionList = questionService.findQuestionsByDifficultyLevelNumber(difficulty.getLevelNumber());
            if(!questionList.isEmpty()){
                modelAndView.addObject("errorMessageBox","Questions exists with this Difficulty. Please delete the questions first");
                modelAndView.setViewName("admin/category/showDifficulties");
            }
        }else{
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }

        //TODO - Check difficulty settings before delete

        difficultyService.delete(id);
        modelAndView.setViewName("redirect:/admin/difficulty/");
        return modelAndView;
    }
}
