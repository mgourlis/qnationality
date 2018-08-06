package gr.ypes.qnationality.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Table(name = "examsettings")
@AttributeOverride(name = "id", column = @Column(name = "exam_setting_id",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class ExamSetting extends BaseEntity {

    @Column(name = "name")
    @NotEmpty(message = "*Please provide a name for the exam settings")
    private String name;

    @Column(name = "enabled")
    private Boolean enabled;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true)
    @JoinColumn(name="exam_setting_id", referencedColumnName="exam_setting_id", nullable = false)
    @NotEmpty
    private List<QuestionCategoryAndDifficultySetting> questionCategoryAndDifficultySettings;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfQuestions() {
        int numOfQuestions = 0;
        for (QuestionCategoryAndDifficultySetting qCADS : questionCategoryAndDifficultySettings) {
            numOfQuestions += qCADS.getNumOfQuestions();
        }
        return numOfQuestions;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<QuestionCategoryAndDifficultySetting> getQuestionCategoryAndDifficultySettings() {
        return questionCategoryAndDifficultySettings;
    }

    public void setQuestionCategoryAndDifficultySettings(List<QuestionCategoryAndDifficultySetting> questionCategoryAndDifficultySettings) {
        this.questionCategoryAndDifficultySettings = questionCategoryAndDifficultySettings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ExamSetting that = (ExamSetting) o;

        if (!name.equals(that.name)) return false;
        if (!enabled.equals(that.enabled)) return false;
        return questionCategoryAndDifficultySettings != null ? questionCategoryAndDifficultySettings.equals(that.questionCategoryAndDifficultySettings) : that.questionCategoryAndDifficultySettings == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + enabled.hashCode();
        result = 31 * result + (questionCategoryAndDifficultySettings != null ? questionCategoryAndDifficultySettings.hashCode() : 0);
        return result;
    }
}
