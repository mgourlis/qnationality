package gr.ypes.qnationality.dto;

import gr.ypes.qnationality.model.Difficulty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class DifficultyDTO {

    private long id;

    @NotEmpty
    private String level;

    @Min(1)
    private int levelNumber;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public void init(Difficulty difficulty) throws IllegalArgumentException{
        this.setId(difficulty.getId());
        this.setLevel(difficulty.getLevel());
        this.setLevelNumber(difficulty.getLevelNumber());
    }
}

