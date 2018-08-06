package gr.ypes.qnationality.dto;

import gr.ypes.qnationality.model.Difficulty;
import gr.ypes.qnationality.model.QuestionCategory;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class QuestionCategoryAndDifficultySettingDTO {

    @Min(0)
    private int numOfQuestions;

    @NotEmpty
    private String difficultyLevel;

    @NotEmpty
    private int difficultyLevelNumber;

    @NotEmpty
    private String questionCategoryName;

    public int getNumOfQuestions() {
        return numOfQuestions;
    }

    public void setNumOfQuestions(int numOfQuestions) {
        this.numOfQuestions = numOfQuestions;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getDifficultyLevelNumber() {
        return difficultyLevelNumber;
    }

    public void setDifficultyLevelNumber(int difficultyLevelNumber) {
        this.difficultyLevelNumber = difficultyLevelNumber;
    }

    public String getQuestionCategoryName() {
        return questionCategoryName;
    }

    public void setQuestionCategoryName(String questionCategoryName) {
        this.questionCategoryName = questionCategoryName;
    }

    public void init(Difficulty difficulty, QuestionCategory questionCategory){
        this.difficultyLevel = difficulty.getLevel();
        this.difficultyLevelNumber = difficulty.getLevelNumber();
        this.questionCategoryName = questionCategory.getName();
        this.numOfQuestions = 0;
    }

    public void init(Difficulty difficulty, QuestionCategory questionCategory, int numOfQuestions){
        this.difficultyLevel = difficulty.getLevel();
        this.difficultyLevelNumber = difficulty.getLevelNumber();
        this.questionCategoryName = questionCategory.getName();
        this.numOfQuestions = numOfQuestions;
    }
}
