package gr.ypes.qnationality.service;

import gr.ypes.qnationality.model.Difficulty;
import gr.ypes.qnationality.repository.DifficultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service("DifficultyService")
public class DifficultyServiceImpl implements IDifficultyService{

    @Autowired
    DifficultyRepository difficultyRepository;

    @Override
    public Difficulty getOne(long id) {
        return difficultyRepository.findDifficultyByIdAndDeleted(id,false);
    }

    @Override
    public List<Difficulty> findAll() {
        return difficultyRepository.findAll();
    }

    @Override
    public Page<Difficulty> findAll(Pageable pageable) {
        return difficultyRepository.findDifficultiesByDeleted(false, pageable);
    }

    @Override
    public Difficulty findByLevel(String level) {
        return difficultyRepository.findDifficultyByLevelAndDeleted(level, false);
    }

    @Override
    public Difficulty findByLevelNumber(int levelNumber) {
        return difficultyRepository.findDifficultyByLevelNumberAndDeleted(levelNumber, false);
    }

    @Override
    public void save(Difficulty difficulty) {
        if(difficulty.getId() == null){
            if(difficultyRepository.findDifficultyByLevelNumberAndDeleted(difficulty.getLevelNumber(),false) == null){
                difficultyRepository.save(difficulty);
            }else{
                throw new EntityExistsException("Difficulty already exists");
            }
        }else{
            Difficulty oldDifficulty = difficultyRepository.findDifficultyByIdAndDeleted(difficulty.getId(),false);
            if(oldDifficulty != null){
                oldDifficulty.setLevel(difficulty.getLevel());
                oldDifficulty.setLevelNumber(difficulty.getLevelNumber());
                difficultyRepository.save(oldDifficulty);
            }else{
                throw new EntityNotFoundException("Can't save difficulty. Invalid difficulty");
            }
        }
    }

    @Override
    public void delete(Long id) {
        Difficulty difficulty = difficultyRepository.findDifficultyByIdAndDeleted(id,false);
        if(difficulty != null){
            difficulty.setDeleted(true);
            difficultyRepository.save(difficulty);
        }else{
            throw new EntityNotFoundException("Invalid Difficulty");
        }
    }
}
