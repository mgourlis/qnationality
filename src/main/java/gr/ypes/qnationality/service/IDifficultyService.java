package gr.ypes.qnationality.service;

import gr.ypes.qnationality.model.Difficulty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDifficultyService {
    public Difficulty getOne(long id);
    public List<Difficulty> findAll();
    public Page<Difficulty> findAll(Pageable pageable);
    public  Difficulty findByLevel(String level);
    public  Difficulty findByLevelNumber(int levelNumber);
    public void save(Difficulty difficulty);
    public void delete(Long id);

}
