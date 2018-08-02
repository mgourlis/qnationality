package gr.ypes.qnationality.service;

import gr.ypes.qnationality.model.Difficulty;
import gr.ypes.qnationality.model.DifficultySetting;
import gr.ypes.qnationality.model.ExamSetting;
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

    @Override
    public ExamSetting getOne(long id) {
        return examSettingRepository.findExamSettingByIdAndDeleted(id,false);
    }

    @Override
    public List<ExamSetting> findAll() {
        return examSettingRepository.findExamSettingsByDeleted(false);
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
        Collections.sort(difficulties);
        List<DifficultySetting> difficultySettings = new ArrayList<>();
        for (Difficulty difficulty: difficulties) {
            DifficultySetting difficultySetting = new DifficultySetting();
            difficultySetting.setDifficulty(difficulty);
            difficultySetting.setPercentage(0);
            difficultySettings.add(difficultySetting);
        }
        examSetting.setDifficultySettings(difficultySettings);
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
                oldExamSetting.setNumOfQuestions(examSetting.getNumOfQuestions());
                oldExamSetting.setEnabled(examSetting.isEnabled());
                oldExamSetting.setQuestionCategories(examSetting.getQuestionCategories());
                oldExamSetting.setDifficultySettings(examSetting.getDifficultySettings());
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
            for (DifficultySetting difficultySetting : examSetting.getDifficultySettings()) {
                difficultySetting.setDeleted(true);
            }
            examSettingRepository.save(examSetting);
        }else{
            throw new EntityNotFoundException("Invalid Exam Setting");
        }
    }
}

