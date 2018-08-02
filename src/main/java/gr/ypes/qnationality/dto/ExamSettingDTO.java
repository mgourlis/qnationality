package gr.ypes.qnationality.dto;

import gr.ypes.qnationality.model.Difficulty;
import gr.ypes.qnationality.model.DifficultySetting;
import gr.ypes.qnationality.model.ExamSetting;
import gr.ypes.qnationality.model.QuestionCategory;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class ExamSettingDTO {

    private long id;

    @NotEmpty
    private String name;

    @Min(1)
    private int numOfQuestions;

    private boolean enabled;

    @NotEmpty
    private List<Float> difficultySettings;

    @NotEmpty
    private List<Long> questionCategories;

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

    public int getNumOfQuestions() {
        return numOfQuestions;
    }

    public void setNumOfQuestions(int numOfQuestions) {
        this.numOfQuestions = numOfQuestions;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Float> getDifficultySettings() {
        return difficultySettings;
    }

    public void setDifficultySettings(List<Float> difficultySettings) {
        this.difficultySettings = difficultySettings;
    }

    public List<Long> getQuestionCategories() {
        return questionCategories;
    }

    public void setQuestionCategories(List<Long> questionCategories) {
        this.questionCategories = questionCategories;
    }

    public void init(ExamSetting examSetting) throws IllegalArgumentException{
        this.setId(examSetting.getId());
        this.setName(examSetting.getName());
        this.setNumOfQuestions(examSetting.getNumOfQuestions());
        this.setEnabled(examSetting.isEnabled());
        List<Long> questionCategories = new ArrayList<>();
        for (QuestionCategory questionCategory : examSetting.getQuestionCategories()) {
            questionCategories.add(questionCategory.getId());
        }
        this.setQuestionCategories(questionCategories);
        List<Float> difficultySettings = new ArrayList<>();
        for (DifficultySetting difficultySetting : examSetting.getDifficultySettings()) {
            difficultySettings.add(difficultySetting.getPercentage());
        }
        this.setDifficultySettings(difficultySettings);
    }

    public void init(List<Difficulty> difficulties){
        List<Float> difficultySettings = new ArrayList<>();
        for (Difficulty difficulty : difficulties){
            difficultySettings.add((float) 0);
        }
        this.setDifficultySettings(difficultySettings);
    }
}

