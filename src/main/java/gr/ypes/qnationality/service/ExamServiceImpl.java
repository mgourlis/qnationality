package gr.ypes.qnationality.service;

import gr.ypes.qnationality.model.*;
import gr.ypes.qnationality.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service("ExamService")
public class ExamServiceImpl implements IExamService {

    @Autowired
    ExamRepository examRepository;

    @Autowired
    IExamSettingService examSettingService;

    @Autowired
    IQuestionService questionService;

    @Override
    public Exam getOne(long id) {
        return examRepository.findExamByIdAndDeleted(id,false);
    }

    @Override
    public Exam getOneByUser(long id, String email) {
        return examRepository.findExamByIdAndCreatedByAndDeleted(id,email,false);
    }

    @Override
    public Exam findByUID(String uID) {
        return examRepository.findExamByUIDAndDeleted(uID,false);
    }

    @Override
    public Exam findLastByUser(String email) {
        return examRepository.findFirstByCreatedByAndDeletedOrderByCreatedAtDesc(email,false);
    }

    @Override
    public List<Exam> findAll() {
        return examRepository.findExamsByDeleted(false);
    }

    @Override
    public Page<Exam> findAll(Pageable pageable) {
        return examRepository.findExamsByDeleted(false,pageable);
    }

    @Override
    public List<Exam> findExamsByUser(String email) {
        return examRepository.findExamsByCreatedByAndDeletedOrderByCreatedAtDesc(email,false);
    }

    @Override
    public Page<Exam> findExamsByUser(String email, Pageable pageable) {
        return examRepository.findExamsByCreatedByAndDeletedOrderByCreatedAtDesc(email,false,pageable);
    }

    @Override
    public List<Exam> findExamsByStatusAndUser(String status, String email) {
        try {
            ExamStatus examStatus = ExamStatus.valueOf(status);
            return examRepository.findExamsByStatusAndCreatedByAndDeletedOrderByCreatedAtDesc(examStatus, email,false);
        } catch (IllegalArgumentException e){
            return examRepository.findExamsByCreatedByAndDeletedOrderByCreatedAtDesc(email, false);
        }
    }

    @Override
    public Page<Exam> findExamsByStatusAndUser(String status, String email, Pageable pageable) {
        try {
            ExamStatus examStatus = ExamStatus.valueOf(status);
            return examRepository.findExamsByStatusAndCreatedByAndDeletedOrderByCreatedAtDesc(examStatus, email,false, pageable);
        } catch (IllegalArgumentException e){
            return examRepository.findExamsByCreatedByAndDeletedOrderByCreatedAtDesc(email,false, pageable);
        }
    }

    @Override
    public List<Exam> findAllByStatus(ExamStatus status) {
        return examRepository.findExamsByStatusAndDeleted(status,false);
    }

    @Override
    public Page<Exam> findAllByStatus(ExamStatus status, Pageable pageable) {
        return examRepository.findExamsByStatusAndDeleted(status,false, pageable);
    }

    @Override
    public String createExam(Exam exam, long examSettingId) throws IllegalArgumentException,EntityNotFoundException {
        ExamSetting examSetting = examSettingService.getOne(examSettingId);
        if(examSetting != null) {
            if(!canGenerateQuestions(examSetting)){
                throw new IllegalArgumentException("Can not generate exam with this Exam Setting.");
            }
            exam.setStatus(ExamStatus.PENDING);
            exam.setuID(Long.toString(Calendar.getInstance(TimeZone.getTimeZone("Europe/Athens")).getTimeInMillis()));
            exam.setExamSetting(examSetting);
            exam.setExamQuestions(generateQuestions(examSetting));
            examRepository.save(exam);
            return exam.getuID();
        }else {
            throw new EntityNotFoundException("Exam Setting does not exist.");
        }
    }

    @Override
    public void validateExam(Exam exam, String email, boolean validationStatus, boolean finalValidation) {
        if(getOne(exam.getId()) != null) {
            if (exam.getStatus().equals(ExamStatus.PENDING)) {
                if (finalValidation) {
                    exam.setValidationUser(email);
                    exam.setValidatedDate(new Date());
                    exam.setValidationStatus(booleanToValidationStatus(validationStatus));
                    exam.setStatus(ExamStatus.VALIDATED);
                }
                examRepository.save(exam);
            } else {
                throw new IllegalArgumentException("Can not set status to validated");
            }
        }else{
            throw new EntityNotFoundException("Exam does not exists");
        }
    }

    @Override
    public void delete(long id) {
        Exam exam = examRepository.findExamByIdAndDeleted(id,false);
        if(exam != null){
            exam.setDeleted(true);
            for (ExamQuestion examQuestion : exam.getExamQuestions()) {
                examQuestion.setDeleted(true);
            }
            examRepository.save(exam);
        }else{
            throw new EntityNotFoundException("Invalid Exam");
        }
    }

    private int countExamQuestions(List<Set<ExamQuestion>> examQuestionsList){
        int count = 0;
        for (Set<ExamQuestion> examQuestions :examQuestionsList) {
            count += examQuestions.size();
        }
        return count;
    }

    private boolean canGenerateQuestions(ExamSetting examSetting){
        List<QuestionCategory> questionCategories = examSetting.getQuestionCategories();
        List<DifficultySetting> difficultySettings = examSetting.getDifficultySettings();
        int questionsSize = questionCategories.size();
        if(questionCategories.isEmpty())
            return false;
        if(difficultySettings.isEmpty())
            return false;
        for (QuestionCategory category : questionCategories) {
            int questionCategorySize = ((int) examSetting.getNumOfQuestions() / questionsSize) + 1;
            for (DifficultySetting difficultySetting: difficultySettings) {
                int questionsCategoryDifficultySize = (int)((questionCategorySize * difficultySetting.getPercentage())/100.0);
                int qnum = questionService.countQuestionsByQuestionCategoryNameAndDifficultyLevelNumber(category.getName(),difficultySetting.getDifficulty().getLevelNumber());
                if(questionsCategoryDifficultySize > qnum){
                    return false;
                }
            }
        }
        return true;
    }

    private List<ExamQuestion> generateQuestions(ExamSetting examSetting){
        List<QuestionCategory> questionCategories = examSetting.getQuestionCategories();
        List<DifficultySetting> difficultySettings = examSetting.getDifficultySettings();
        if(!questionCategories.isEmpty()) {
            List<Set<ExamQuestion>> examQuestionsList = new ArrayList<>();
            int questionsSize = questionCategories.size();
            for (QuestionCategory questionCategory : questionCategories) {
                Set<ExamQuestion> categoryQuestions = new LinkedHashSet<>();
                int questionCategorySize = ((int) examSetting.getNumOfQuestions() / questionsSize) + 1;
                for (DifficultySetting difficultySetting : difficultySettings) {
                    int questionsCategoryDifficultySize = (int) ((questionCategorySize * difficultySetting.getPercentage()) / 100.0);
                    Set<Question> randQuestions = questionService.getRandomQuestionsByCategoryAndDifficulty
                            (questionCategory, difficultySetting.getDifficulty(), questionsCategoryDifficultySize);
                    for (Question question : randQuestions) {
                        ExamQuestion examQuestion = new ExamQuestion();
                        examQuestion.setSortNumber(categoryQuestions.size());
                        examQuestion.setQuestion(question);
                        categoryQuestions.add(examQuestion);
                    }
                }
                examQuestionsList.add(categoryQuestions);
            }
            int categoryIndex = questionCategories.size() - 1;
            while ((examSetting.getNumOfQuestions() - countExamQuestions(examQuestionsList)) < 0) {
                if (categoryIndex < 0)
                    categoryIndex = questionCategories.size() - 1;
                examQuestionsList.get(categoryIndex).remove(examQuestionsList.get(categoryIndex).toArray()[examQuestionsList.get(categoryIndex).size() - 1]);
            }
            int count = 1;
            List<ExamQuestion> allExamQuestions = new ArrayList<>();
            for (Set<ExamQuestion> examQuestions : examQuestionsList) {
                for (ExamQuestion examQuestion : examQuestions) {
                    examQuestion.setSortNumber(count);
                    allExamQuestions.add(examQuestion);
                    count++;
                }
            }
            Collections.sort(allExamQuestions);
            return allExamQuestions;
        }else{
            throw new IllegalArgumentException("Failed two generate questions with this Exam Setting");
        }
    }

    private ValidationStatus booleanToValidationStatus(boolean b){
        return b ? ValidationStatus.CORRECT : ValidationStatus.FALSE;
    }
}

