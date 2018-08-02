package gr.ypes.qnationality.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "exam")
@AttributeOverride(name = "id", column = @Column(name = "exam_id",
        nullable = false, columnDefinition = "BIGINT UNSIGNED"))
public class Exam extends BaseEntity {

    @Column(name = "uid", unique = true)
    @NotEmpty
    private String uID;

    @Column(name = "foreas")
    @NotEmpty
    private String foreas;

    @Column(name = "validated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validatedDate;

    @Column(name = "validation_user")
    private String validationUser;

    @Column(name = "validation_status", length = 15)
    @Enumerated(EnumType.STRING)
    private ValidationStatus validationStatus;

    @Column(name = "status", length = 15)
    @Enumerated(EnumType.STRING)
    private ExamStatus status;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH})
    @JoinColumn(name = "exam_setting_id", referencedColumnName = "exam_setting_id", nullable = false)
    private ExamSetting examSetting;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true)
    @JoinColumn(name = "exam_id", referencedColumnName = "exam_id", nullable = false)
    private List<ExamQuestion> examQuestions;

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getForeas() {
        return foreas;
    }

    public void setForeas(String foreas) {
        this.foreas = foreas;
    }

    public Date getValidatedDate() {
        return validatedDate;
    }

    public void setValidatedDate(Date validatedDate) {
        this.validatedDate = validatedDate;
    }

    public String getValidationUser() {
        return validationUser;
    }

    public void setValidationUser(String validationUser) {
        this.validationUser = validationUser;
    }

    public ValidationStatus getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(ValidationStatus validationStatus) {
        this.validationStatus = validationStatus;
    }

    public ExamStatus getStatus() {
        return status;
    }

    public void setStatus(ExamStatus status) {
        this.status = status;
    }

    public ExamSetting getExamSetting() {
        return examSetting;
    }

    public void setExamSetting(ExamSetting examSetting) {
        this.examSetting = examSetting;
    }

    public List<ExamQuestion> getExamQuestions() {
        return examQuestions;
    }

    public void setExamQuestions(List<ExamQuestion> examQuestions) {
        this.examQuestions = examQuestions;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exam)) return false;
        if (!super.equals(o)) return false;

        Exam exam = (Exam) o;

        return uID.equals(exam.uID);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + uID.hashCode();
        return result;
    }
}
