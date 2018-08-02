package gr.ypes.qnationality.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;

@Entity
@Table(name = "difficulty")
@AttributeOverride(name = "id", column = @Column(name = "difficulty_id",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Difficulty extends BaseEntity implements Comparable<Difficulty> {

    @Column(name = "level")
    @NotEmpty(message = "*Please provide a level for the difficulty")
    private String level;

    @Column(name = "levelnum")
    @Min(1)
    private int levelNumber;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Difficulty)) return false;
        if (!super.equals(o)) return false;

        Difficulty that = (Difficulty) o;

        return getLevelNumber() == that.getLevelNumber();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getLevelNumber();
        return result;
    }

    @Override
    public int compareTo(Difficulty o) {
        if(this.levelNumber > o.levelNumber){
            return 1;
        }else if(this.levelNumber < o.levelNumber){
            return -1;
        }else{
            return 0;
        }
    }
}
