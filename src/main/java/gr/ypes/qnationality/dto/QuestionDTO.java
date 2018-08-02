package gr.ypes.qnationality.dto;

import gr.ypes.qnationality.model.Question;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class QuestionDTO {

    private long id;

    @NotEmpty
    private String shortname;

    @Size(min = 1, max = 1000)
    private String questiontext;

    private long questionCategory;

    private long questionDifficulty;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public long getQuestionCategory() {
        return questionCategory;
    }

    public void setQuestionCategory(long questionCategory) {
        this.questionCategory = questionCategory;
    }

    public long getQuestionDifficulty() {
        return questionDifficulty;
    }

    public void setQuestionDifficulty(long questionDifficulty) {
        this.questionDifficulty = questionDifficulty;
    }

    public void init(Question question) throws IllegalArgumentException{
        this.setId(question.getId());
        this.setShortname(question.getShortname());
        this.setQuestiontext(question.getQuestiontext());
        if(question.getQuestionDifficulty() != null)
            this.setQuestionDifficulty(question.getQuestionDifficulty().getId());
        if(question.getQuestionCategory() != null)
            this.setQuestionCategory(question.getQuestionCategory().getId());
    }
}

