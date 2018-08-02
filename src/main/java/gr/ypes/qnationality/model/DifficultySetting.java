package gr.ypes.qnationality.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "difficultysettings")
@AttributeOverride(name = "id", column = @Column(name = "difficulty_setting_id",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class DifficultySetting extends BaseEntity {

    @Column(name = "percentage")
    @NotNull(message = "*Please set the percentage for this question difficulty")
    private float percentage;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="difficulty_id",referencedColumnName="difficulty_id", nullable = false)
    private Difficulty difficulty;

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
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
        if (!(o instanceof DifficultySetting)) return false;
        if (!super.equals(o)) return false;

        DifficultySetting that = (DifficultySetting) o;

        if (Float.compare(that.getPercentage(), getPercentage()) != 0) return false;
        return getDifficulty() != null ? getDifficulty().equals(that.getDifficulty()) : that.getDifficulty() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getPercentage() != +0.0f ? Float.floatToIntBits(getPercentage()) : 0);
        result = 31 * result + (getDifficulty() != null ? getDifficulty().hashCode() : 0);
        return result;
    }
}
