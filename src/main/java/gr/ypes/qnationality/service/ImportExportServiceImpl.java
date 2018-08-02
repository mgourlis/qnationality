package gr.ypes.qnationality.service;

import gr.ypes.qnationality.dto.ImportQuestionDTO;
import gr.ypes.qnationality.model.Difficulty;
import gr.ypes.qnationality.model.Question;
import gr.ypes.qnationality.model.QuestionCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service("ImportExportService")
public class ImportExportServiceImpl implements IImportExportService {

    @Autowired
    IQuestionCategoryService questionCategoryService;

    @Autowired
    IDifficultyService difficultyService;

    @Autowired
    IQuestionService questionService;

    @Override
    public String importQuestions(List<ImportQuestionDTO> importQuestions) {
        String errors = "";
        Set<QuestionCategory> categories = new LinkedHashSet<>();
        Set<Difficulty> difficulties = new LinkedHashSet<>();
        Set<Question> questions = new LinkedHashSet<>();
        Boolean dublicateFlag = false;
        Boolean emptyValueFlag = false;
        for (ImportQuestionDTO importQuestion : importQuestions) {
            QuestionCategory questionCategory = new QuestionCategory();
            Difficulty difficulty = new Difficulty();
            Boolean flag = true;
            if(!importQuestion.getQuestionCategory().equals("")){
                questionCategory.setName(importQuestion.getQuestionCategory());
                QuestionCategory dbCategory = questionCategoryService.findQuestionCategoryByName(importQuestion.getQuestionCategory());
                if(dbCategory == null)
                    categories.add(questionCategory);
            }else{
                flag = false;
            }
            if(importQuestion.getQuestionDifficultyLevelNumber() > 0 && !importQuestion.getQuestionDifficultyLevel().equals("")){
                difficulty.setLevel(importQuestion.getQuestionDifficultyLevel());
                difficulty.setLevelNumber(importQuestion.getQuestionDifficultyLevelNumber());
                Difficulty dbDifficulty = difficultyService.findByLevelNumber(importQuestion.getQuestionDifficultyLevelNumber());
                if(dbDifficulty == null)
                    difficulties.add(difficulty);
            }else{
                flag = false;
            }
            if(flag && !importQuestion.getShortname().equals("") && !importQuestion.getQuestiontext().equals("")){
                if(questionService.findQuestionByShortname(importQuestion.getShortname()) == null) {
                    Question question = new Question();
                    question.setShortname(importQuestion.getShortname());
                    question.setQuestiontext(importQuestion.getQuestiontext());
                    question.setQuestionCategory(questionCategory);
                    question.setQuestionDifficulty(difficulty);
                    questions.add(question);
                }else{
                    dublicateFlag = true;
                }
            }else{
                emptyValueFlag = true;
            }
        }
        if(dublicateFlag)
            errors = "Some questions already exists in the database.\n Non duplicate questions imported!\n";
        if(!emptyValueFlag){
            for (QuestionCategory category : categories) {
                questionCategoryService.save(category);
            }
            for (Difficulty difficulty : difficulties) {
                difficultyService.save(difficulty);
            }
            for (Question question : questions) {
                QuestionCategory dbCategory = questionCategoryService.findQuestionCategoryByName(question.getQuestionCategory().getName());
                Difficulty dbDifficulty = difficultyService.findByLevelNumber(question.getQuestionDifficulty().getLevelNumber());
                question.setQuestionCategory(dbCategory);
                question.setQuestionDifficulty(dbDifficulty);
                questionService.save(question);
            }
        }else{
            errors = "Found entries with empty values.\n";
        }
        return errors;

    }

    @Override
    public String exportQuestions() {
        List<Question> questions = questionService.findAll();
        String csvFormatString = "\"Short name\",\"Question text\",\"Question Category\",\"Diffilculty\",\"Difficulty percentage\"\n";
        for (Question question : questions) {
            csvFormatString += "\"" + question.getShortname() + "\",\"" +
                    question.getQuestiontext() + "\",\"" +
                    question.getQuestionCategory().getName() + "\",\"" +
                    question.getQuestionDifficulty().getLevel() + "\",\"" +
                    question.getQuestionDifficulty().getLevelNumber() + "\"\n";
        }
        return csvFormatString;
    }
}
