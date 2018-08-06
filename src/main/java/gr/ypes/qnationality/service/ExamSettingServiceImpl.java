package gr.ypes.qnationality.service;

import gr.ypes.qnationality.model.*;
import gr.ypes.qnationality.repository.ExamSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("ExamSettingService")
public class ExamSettingServiceImpl implements IExamSettingService {

    @Autowired
    ExamSettingRepository examSettingRepository;

    @Autowired
    IDifficultyService difficultyService;

    @Autowired
    IQuestionCategoryService questionCategoryService;

    @Override
    public ExamSetting getOne(long id) {
        return examSettingRepository.findExamSettingByIdAndDeleted(id,false);
    }

    @Override
    public List<ExamSetting> findAll() {
        return examSettingRepository.findExamSettingsByDeleted(false);
    }

    @Override
    public List<ExamSetting> findAllSortedAsc() {
        return examSettingRepository.findExamSettingsByDeletedOrderByNameAsc(false);
    }

    @Override
    public Page<ExamSetting> findAll(Pageable pageable) {
        return examSettingRepository.findExamSettingsByDeleted(false, pageable);
    }

    @Override
    public ExamSetting findExamSettingByName(String name) {
        return examSettingRepository.findExamSettingByNameAndDeleted(name,false);
    }

    @Override
    public List<ExamSetting> findExamSettingsByEnabled(boolean enabled) {
        return examSettingRepository.findExamSettingsByEnabledAndDeleted(enabled,false);
    }

    @Override
    public Page<ExamSetting> findExamSettingsByEnabled(boolean enabled, Pageable pageable) {
        return examSettingRepository.findExamSettingsByEnabledAndDeleted(enabled,false, pageable);
    }

    @Override
    public ExamSetting createExamSetting() {
        ExamSetting examSetting = new ExamSetting();
        List<Difficulty> difficulties = difficultyService.findAll();
        List<QuestionCategory> questionCategories = questionCategoryService.findAll();
        Collections.sort(difficulties);
        Collections.sort(questionCategories);
        List<QuestionCategoryAndDifficultySetting> questionCategoryAndDifficultySettings = new ArrayList<>();
        for (QuestionCategory questionCategory: questionCategories) {
            for(Difficulty difficulty : difficulties) {
                QuestionCategoryAndDifficultySetting questionCategoryAndDifficultySetting = new QuestionCategoryAndDifficultySetting();
                questionCategoryAndDifficultySetting.setNumOfQuestions(0);
                questionCategoryAndDifficultySetting.setDifficulty(difficulty);
                questionCategoryAndDifficultySetting.setQuestionCategory(questionCategory);
                questionCategoryAndDifficultySettings.add(questionCategoryAndDifficultySetting);
            }
        }
        examSetting.setQuestionCategoryAndDifficultySettings(questionCategoryAndDifficultySettings);
        return examSetting;
    }

    @Override
    public void save(ExamSetting examSetting) {
        if(examSetting.getId() == null){
            if(examSettingRepository.findExamSettingByNameAndDeleted(examSetting.getName(),false) == null){
                examSettingRepository.save(examSetting);
            }else{
                throw new EntityExistsException("Exam Setting already exists");
            }
        }else{
            ExamSetting oldExamSetting = examSettingRepository.findExamSettingByIdAndDeleted(examSetting.getId(),false);
            if(oldExamSetting != null){
                oldExamSetting.setName(examSetting.getName());
                oldExamSetting.setEnabled(examSetting.isEnabled());
                oldExamSetting.setQuestionCategoryAndDifficultySettings(examSetting.getQuestionCategoryAndDifficultySettings());
                examSettingRepository.save(oldExamSetting);
            }else{
                throw new EntityNotFoundException("Can't save Exam Setting. Invalid Exam Setting");
            }
        }
    }

    @Override
    public void delete(long id) {
        ExamSetting examSetting = examSettingRepository.findExamSettingByIdAndDeleted(id,false);
        if(examSetting != null){
            examSetting.setDeleted(true);
            for (QuestionCategoryAndDifficultySetting questionCategoryAndDifficultySetting : examSetting.getQuestionCategoryAndDifficultySettings()) {
                questionCategoryAndDifficultySetting.setDeleted(true);
            }
            examSettingRepository.save(examSetting);
        }else{
            throw new EntityNotFoundException("Invalid Exam Setting");
        }
    }
}

