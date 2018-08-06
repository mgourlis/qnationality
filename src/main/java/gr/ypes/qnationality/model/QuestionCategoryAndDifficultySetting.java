package gr.ypes.qnationality.model;


import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Table(name = "questioncategoryanddifficultyseting")
@AttributeOverride(name = "id", column = @Column(name = "question_category_and_difficulty_setting_id",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class QuestionCategoryAndDifficultySetting extends BaseEntity {

    @Column(name = "number_of_questions")
    @Min(0)
    private int numOfQuestions;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH})
    @JoinColumn(name = "question_category_id", referencedColumnName = "question_category_id", nullable = false)
    private QuestionCategory questionCategory;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH})
    @JoinColumn(name = "difficulty_id", referencedColumnName = "difficulty_id", nullable = false)
    private Difficulty difficulty;

    public int getNumOfQuestions() {
        return numOfQuestions;
    }

    public void setNumOfQuestions(int numOfQuestions) {
        this.numOfQuestions = numOfQuestions;
    }

    public QuestionCategory getQuestionCategory() {
        return questionCategory;
    }

    public void setQuestionCategory(QuestionCategory questionCategory) {
        this.questionCategory = questionCategory;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionCategoryAndDifficultySetting that = (QuestionCategoryAndDifficultySetting) o;

        if (numOfQuestions != that.numOfQuestions) return false;
        if (!questionCategory.equals(that.questionCategory)) return false;
        return difficulty.equals(that.difficulty);
    }

    @Override
    public int hashCode() {
        int result = numOfQuestions;
        result = 31 * result + questionCategory.hashCode();
        result = 31 * result + difficulty.hashCode();
        return result;
    }
}
