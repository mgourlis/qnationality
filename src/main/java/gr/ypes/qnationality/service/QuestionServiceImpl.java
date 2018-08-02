package gr.ypes.qnationality.service;

import gr.ypes.qnationality.model.Difficulty;
import gr.ypes.qnationality.model.Question;
import gr.ypes.qnationality.model.QuestionCategory;
import gr.ypes.qnationality.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service("QuestionService")
public class QuestionServiceImpl implements IQuestionService {

    @Autowired
    QuestionRepository questionRepository;

    @Override
    public Question getOne(long id) {
        return questionRepository.findQuestionByIdAndDeleted(id,false);
    }

    @Override
    public List<Question> findAll() {
        return questionRepository.findQuestionsByDeleted(false);
    }

    @Override
    public Page<Question> findAll(Pageable pageable) {
        return questionRepository.findQuestionsByDeleted(false, pageable);
    }

    @Override
    public Question findQuestionByShortname(String name) {
        return questionRepository.findQuestionByShortnameAndDeleted(name, false);
    }

    @Override
    public List<Question> findQuestionsByQuestionCategoryName(String questionCategoryName) {
        return questionRepository.findQuestionsByQuestionCategory_NameAndDeleted(questionCategoryName,false);
    }

    @Override
    public Page<Question> findQuestionsByQuestionCategoryName(String questionCategoryName, Pageable pageable) {
        return questionRepository.findQuestionsByQuestionCategory_NameAndDeleted(questionCategoryName,false, pageable);
    }

    @Override
    public int countQuestionsByQuestionCategoryName(String questionCategoryName) {
        return questionRepository.countQuestionsByQuestionCategory_NameAndDeleted(questionCategoryName,false);
    }

    @Override
    public List<Question> findQuestionsByDifficultyLevelNumber(int difficultyLevelNumber) {
        return questionRepository.findQuestionsByQuestionDifficulty_LevelNumberAndDeleted(difficultyLevelNumber,false);
    }

    @Override
    public Page<Question> findQuestionsByDifficultyLevelNumber(int difficultyLevelNumber, Pageable pageable) {
        return questionRepository.findQuestionsByQuestionDifficulty_LevelNumberAndDeleted(difficultyLevelNumber,false, pageable);
    }

    @Override
    public List<Question> findQuestionsByQuestionCategoryNameAndDifficultyLevelNumber(String questionCategoryName, int difficultyLevelNumber) {
        return questionRepository.findQuestionsByQuestionCategory_NameAndQuestionDifficulty_LevelNumberAndDeleted(questionCategoryName, difficultyLevelNumber,false);
    }

    @Override
    public Page<Question> findQuestionsByQuestionCategoryNameAndDifficultyLevelNumber(String questionCategoryName, int difficultyLevelNumber, Pageable pageable) {
        return questionRepository.findQuestionsByQuestionCategory_NameAndQuestionDifficulty_LevelNumberAndDeleted(questionCategoryName, difficultyLevelNumber,false, pageable);
    }

    @Override
    public int countQuestionsByQuestionCategoryNameAndDifficultyLevelNumber(String questionCategoryName, int difficultyLevelNumber) {
        return questionRepository.countQuestionsByQuestionCategory_NameAndQuestionDifficulty_LevelNumberAndDeleted(questionCategoryName, difficultyLevelNumber,false);
    }

    @Override
    public Set<Question> getRandomQuestionsByCategoryAndDifficulty(QuestionCategory questionCategory, Difficulty difficulty, int size) {
        List<Question> questions =findQuestionsByQuestionCategoryNameAndDifficultyLevelNumber(questionCategory.getName(),difficulty.getLevelNumber());
        Set<Question> randomQuestions = new LinkedHashSet<>();
        while(randomQuestions.size() < size) {
            int index = (int) (Math.random() * questions.size());
            randomQuestions.add(questions.get(index));
        }
        return randomQuestions;
    }

    @Override
    public void save(Question question) {
        if(question.getId() == null){
            if(questionRepository.findQuestionByShortnameAndDeleted(question.getShortname(),false) == null){
                questionRepository.save(question);
            }else{
                throw new EntityExistsException("Question already exists");
            }
        }else{
            Question oldQuestion = questionRepository.findQuestionByIdAndDeleted(question.getId(),false);
            if(oldQuestion != null){
                oldQuestion.setShortname(question.getShortname());
                oldQuestion.setQuestiontext(question.getQuestiontext());
                oldQuestion.setQuestionCategory(question.getQuestionCategory());
                oldQuestion.setQuestionDifficulty(question.getQuestionDifficulty());
                questionRepository.save(oldQuestion);
            }else{
                throw new EntityNotFoundException("Can't save Question. Invalid Question");
            }
        }
    }

    @Override
    public void delete(long id) {
        Question question = questionRepository.findQuestionByIdAndDeleted(id,false);
        if(question != null){
            question.setDeleted(true);
            questionRepository.save(question);
        }else{
            throw new EntityNotFoundException("Invalid Question");
        }
    }

}

