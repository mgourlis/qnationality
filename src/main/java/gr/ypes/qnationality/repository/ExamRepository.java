package gr.ypes.qnationality.repository;

import gr.ypes.qnationality.model.Exam;
import gr.ypes.qnationality.model.ExamStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ExamRepository")
public interface ExamRepository extends JpaRepository<Exam,Long> {
    public Exam findExamByIdAndDeleted(long id, boolean deleted);

    public Exam findExamByIdAndCreatedByAndDeleted(long id, String email, boolean deleted);

    public Exam findExamByUIDAndDeleted(String uID, boolean deleted);

    public Exam findFirstByCreatedByAndDeletedOrderByCreatedAtDesc(String email, boolean deleted);

    public List<Exam> findExamsByDeleted(boolean deleted);

    public Page<Exam> findExamsByDeleted(boolean deleted, Pageable pageable);

    public List<Exam> findExamsByCreatedByAndDeletedOrderByCreatedAtDesc(String createdBy, boolean deleted);

    public Page<Exam> findExamsByCreatedByAndDeletedOrderByCreatedAtDesc(String createdBy, boolean deleted, Pageable pageable);

    public List<Exam> findExamsByStatusAndCreatedByAndDeletedOrderByCreatedAtDesc(ExamStatus status, String email, boolean deleted);

    public Page<Exam> findExamsByStatusAndCreatedByAndDeletedOrderByCreatedAtDesc(ExamStatus status, String email, boolean deleted, Pageable pageable);

    public List<Exam> findExamsByStatusAndDeleted(ExamStatus status, boolean deleted);

    public Page<Exam> findExamsByStatusAndDeleted(ExamStatus status, boolean deleted, Pageable pageable);
}
