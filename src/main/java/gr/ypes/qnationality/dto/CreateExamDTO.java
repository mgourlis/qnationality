package gr.ypes.qnationality.dto;

import gr.ypes.qnationality.model.Exam;
import gr.ypes.qnationality.model.ExamSetting;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CreateExamDTO {

    private long examSettingId;

    @NotEmpty
    private String foreas;

    @Size(max = 512)
    private String languageExemptionNotes;

    public long getExamSettingId() {
        return examSettingId;
    }

    public void setExamSettingId(long examSettingId) {
        this.examSettingId = examSettingId;
    }

    public String getForeas() {
        return foreas;
    }

    public void setForeas(String foreas) {
        this.foreas = foreas;
    }

    public void init(Exam exam){
        if(exam.getExamSetting() != null)
            this.setExamSettingId(exam.getExamSetting().getId());
        this.setForeas(exam.getForeas());
    }

    public Exam getExam(ExamSetting examSetting){
        Exam exam = new Exam();
        exam.setExamSetting(examSetting);
        exam.setForeas(this.getForeas().trim());
        return exam;
    }
}
