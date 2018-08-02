package gr.ypes.qnationality.repository;

import gr.ypes.qnationality.model.QuestionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("QuestionCategoryRepository")
public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory,Long> {
    public Page<QuestionCategory> findQuestionCategoriesByDeleted(Boolean deleted, Pageable pageable);
    public List<QuestionCategory> findQuestionCategoriesByDeleted(Boolean deleted);
    public QuestionCategory findQuestionCategoryByIdAndDeleted(long id, boolean deleted);
    public QuestionCategory findQuestionCategoryByNameAndDeleted(String name, boolean deleted);
}
