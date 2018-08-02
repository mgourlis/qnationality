package gr.ypes.qnationality.service;

import gr.ypes.qnationality.model.Difficulty;
import gr.ypes.qnationality.model.Question;
import gr.ypes.qnationality.model.QuestionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface IQuestionService {
    public Question getOne(long id);
    public List<Question> findAll();
    public Page<Question> findAll(Pageable pageable);
    public Question findQuestionByShortname(String name);
    public List<Question> findQuestionsByQuestionCategoryName(String questionCategoryName);
    public Page<Question> findQuestionsByQuestionCategoryName(String questionCategoryName, Pageable pageable);
    public int countQuestionsByQuestionCategoryName(String questionCategoryName);
    public List<Question> findQuestionsByDifficultyLevelNumber(int difficultyLevelNumber);
    public Page<Question> findQuestionsByDifficultyLevelNumber(int difficultyLevelNumber, Pageable pageable);
    public List<Question> findQuestionsByQuestionCategoryNameAndDifficultyLevelNumber(String questionCategoryName, int difficultyLevelNumber);
    public Page<Question> findQuestionsByQuestionCategoryNameAndDifficultyLevelNumber(String questionCategoryName, int difficultyLevelNumber, Pageable pageable);
    public int countQuestionsByQuestionCategoryNameAndDifficultyLevelNumber(String questionCategoryName, int difficultyLevelNumber);
    public Set<Question> getRandomQuestionsByCategoryAndDifficulty(QuestionCategory questionCategory , Difficulty difficulty, int size);
    public void save(Question question);
    public void delete(long id);
}
