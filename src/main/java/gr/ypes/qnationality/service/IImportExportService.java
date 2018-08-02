package gr.ypes.qnationality.service;

import gr.ypes.qnationality.dto.ImportQuestionDTO;

import java.util.List;

public interface IImportExportService {

    public String importQuestions(List<ImportQuestionDTO> importQuestions);
    public String exportQuestions();

}
