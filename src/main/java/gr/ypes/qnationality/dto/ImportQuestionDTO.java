package gr.ypes.qnationality.dto;

public class ImportQuestionDTO {

    private String shortname;

    private String questiontext;

    private String questionCategory;

    private String questionDifficultyLevel;

    private Integer questionDifficultyLevelNumber;

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getQuestiontext() {
        return questiontext;
    }

    public void setQuestiontext(String questiontext) {
        this.questiontext = questiontext;
    }

    public String getQuestionCategory() {
        return questionCategory;
    }

    public void setQuestionCategory(String questionCategory) {
        this.questionCategory = questionCategory;
    }

    public String getQuestionDifficultyLevel() {
        return questionDifficultyLevel;
    }

    public void setQuestionDifficultyLevel(String questionDifficultyLevel) {
        this.questionDifficultyLevel = questionDifficultyLevel;
    }

    public Integer getQuestionDifficultyLevelNumber() {
        return questionDifficultyLevelNumber;
    }

    public void setQuestionDifficultyLevelNumber(Integer questionDifficultyLevelNumber) {
        this.questionDifficultyLevelNumber = questionDifficultyLevelNumber;
    }

    @Override
    public String toString() {
        return "'" + shortname + '\'' +
                ", '" + questiontext + '\'' +
                ", '" + questionCategory + '\'' +
                ", '" + questionDifficultyLevel + '\'' +
                ", " + questionDifficultyLevelNumber;
    }
}