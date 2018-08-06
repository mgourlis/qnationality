package gr.ypes.qnationality.dto;

import gr.ypes.qnationality.model.Difficulty;
import gr.ypes.qnationality.model.ExamSetting;
import gr.ypes.qnationality.model.QuestionCategory;
import gr.ypes.qnationality.model.QuestionCategoryAndDifficultySetting;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class ExamSettingDTO {

    private long id;

    @NotEmpty
    private String name;

    private boolean enabled;

    @NotEmpty
    private List<QuestionCategoryAndDifficultySettingDTO> questionCategoryAndDifficultySettings ;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<QuestionCategoryAndDifficultySettingDTO> getQuestionCategoryAndDifficultySettings() {
        return questionCategoryAndDifficultySettings;
    }

    public void setQuestionCategoryAndDifficultySettings(List<QuestionCategoryAndDifficultySettingDTO> questionCategoryAndDifficultySettings) {
        this.questionCategoryAndDifficultySettings = questionCategoryAndDifficultySettings;
    }

    public void init(ExamSetting examSetting) throws IllegalArgumentException{
        this.setId(examSetting.getId());
        this.setName(examSetting.getName());
        this.setEnabled(examSetting.isEnabled());
        List<QuestionCategoryAndDifficultySettingDTO> questionCategoryAndDifficultySettings = new ArrayList<>();
        for (QuestionCategoryAndDifficultySetting questionCategoryAndDifficultySetting : examSetting.getQuestionCategoryAndDifficultySettings()) {
            QuestionCategoryAndDifficultySettingDTO qCADDTO = new QuestionCategoryAndDifficultySettingDTO();
            qCADDTO.init(questionCategoryAndDifficultySetting.getDifficulty(),questionCategoryAndDifficultySetting.getQuestionCategory(),questionCategoryAndDifficultySetting.getNumOfQuestions());
            questionCategoryAndDifficultySettings.add(qCADDTO);
        }
        this.setQuestionCategoryAndDifficultySettings(questionCategoryAndDifficultySettings);
    }

    public void init(List<QuestionCategory> questionCategories, List<Difficulty> difficulties){
        List<QuestionCategoryAndDifficultySettingDTO> questionCategoryAndDifficultySettings = new ArrayList<>();
        for (QuestionCategory questionCategory : questionCategories){
            for(Difficulty difficulty : difficulties) {
                QuestionCategoryAndDifficultySettingDTO qCADDTO = new QuestionCategoryAndDifficultySettingDTO();
                qCADDTO.init(difficulty,questionCategory);
                questionCategoryAndDifficultySettings.add(qCADDTO);
            }
        }
        this.setQuestionCategoryAndDifficultySettings(questionCategoryAndDifficultySettings);
    }
}

