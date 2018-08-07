package gr.ypes.qnationality.service;

import gr.ypes.qnationality.model.QuestionCategory;
import gr.ypes.qnationality.repository.QuestionCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service("QuestionCategoryService")
public class QuestionCategoryServiceImpl implements IQuestionCategoryService {

    @Autowired
    QuestionCategoryRepository questionCategoryRepository;

    @Override
    public QuestionCategory getOne(long id) {
        return questionCategoryRepository.findQuestionCategoryByIdAndDeleted(id,false);
    }

    @Override
    public List<QuestionCategory> findAll() {
        return questionCategoryRepository.findQuestionCategoriesByDeleted(false);
    }

    @Override
    public Page<QuestionCategory> findAll(Pageable pageable) {
        return questionCategoryRepository.findQuestionCategoriesByDeleted(false, pageable);
    }

    @Override
    public QuestionCategory findQuestionCategoryByName(String name) {
        return questionCategoryRepository.findQuestionCategoryByNameAndDeleted(name, false);
    }

    @Override
    public void save(QuestionCategory questionCategory) {
        if(questionCategory.getId() == null){
            if(questionCategoryRepository.findQuestionCategoryByNameAndDeleted(questionCategory.getName(),false) == null){
                questionCategoryRepository.save(questionCategory);
            }else{
                throw new EntityExistsException("Question Category already exists");
            }
        }else{
            QuestionCategory oldQuestionCategory = questionCategoryRepository.findQuestionCategoryByIdAndDeleted(questionCategory.getId(),false);
            if(oldQuestionCategory != null){
                QuestionCategory dublicateQCategory = questionCategoryRepository.findQuestionCategoryByNameAndDeleted(questionCategory.getName(),false);
                if(dublicateQCategory != null) {
                    if (dublicateQCategory.getId() != questionCategory.getId()) {
                            throw new EntityExistsException("Question Category already exists");
                    } else {
                        oldQuestionCategory.setName(questionCategory.getName());
                        questionCategoryRepository.save(oldQuestionCategory);
                    }
                }else{
                    oldQuestionCategory.setName(questionCategory.getName());
                    questionCategoryRepository.save(oldQuestionCategory);
                }
            }else{
                throw new EntityNotFoundException("Can't save Question Category. Invalid Question Category");
            }
        }

    }

    @Override
    public void delete(long id) {
        QuestionCategory questionCategory = questionCategoryRepository.findQuestionCategoryByIdAndDeleted(id,false);
        if(questionCategory != null){
            questionCategory.setDeleted(true);
            questionCategoryRepository.save(questionCategory);
        }else{
            throw new EntityNotFoundException("Invalid Question Category");
        }
    }
}
