package gr.ypes.qnationality.repository;

import gr.ypes.qnationality.model.ExamSetting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ExamSettingRepository")
public interface ExamSettingRepository extends JpaRepository<ExamSetting,Long> {
    public ExamSetting findExamSettingByIdAndDeleted(long id, boolean deleted);
    public ExamSetting findExamSettingByNameAndDeleted(String name, boolean deleted);
    public List<ExamSetting> findExamSettingsByDeleted(boolean deleted);
    public Page<ExamSetting> findExamSettingsByDeleted(boolean deleted, Pageable pageable);
    public List<ExamSetting> findExamSettingsByEnabledAndDeleted(boolean enabled, boolean deleted);
    public Page<ExamSetting> findExamSettingsByEnabledAndDeleted(boolean enabled, boolean deleted, Pageable pageable);
}
