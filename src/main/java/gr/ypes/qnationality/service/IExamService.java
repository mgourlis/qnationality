package gr.ypes.qnationality.service;

import gr.ypes.qnationality.model.Exam;
import gr.ypes.qnationality.model.ExamStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IExamService {
    public Exam getOne(long id);
    public Exam getOneByUser(long id, String email);
    public Exam findByUID(String uID);
    public Exam findLastByUser(String email);
    public List<Exam> findAll();
    public Page<Exam> findAll(Pageable pageable);
    public List<Exam> findExamsByUser(String email);
    public Page<Exam> findExamsByUser(String email, Pageable pageable);
    public List<Exam> findExamsByStatusAndUser(String status, String email);
    public Page<Exam> findExamsByStatusAndUser(String status, String email, Pageable pageable);
    public List<Exam> findAllByStatus(ExamStatus status);
    public Page<Exam> findAllByStatus(ExamStatus status, Pageable pageable);
    public String createExam(Exam exam, long examSettingId);
    public void validateExam(Exam exam, String email, boolean validationStatus, boolean finalValidation);
    public void delete(long id);

}
