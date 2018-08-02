package gr.ypes.qnationality.service;

import gr.ypes.qnationality.model.QuestionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IQuestionCategoryService {
    public QuestionCategory getOne(long id);
    public List<QuestionCategory> findAll();
    public Page<QuestionCategory> findAll(Pageable pageable);
    public QuestionCategory findQuestionCategoryByName(String name);
    public void save(QuestionCategory questionCategory);
    public void delete(long id);

}

