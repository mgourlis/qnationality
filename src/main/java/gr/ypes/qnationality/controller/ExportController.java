package gr.ypes.qnationality.controller;

import gr.ypes.qnationality.service.IImportExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

@Controller
@Secured("ADMIN")
@RequestMapping("/admin/export")
public class ExportController {

    @Autowired
    IImportExportService importExportService;

    @GetMapping("/questions")
    @ResponseBody
    public ResponseEntity<Resource> exportQuestions() {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream utf8OutputStreamWriter = new ObjectOutputStream(byteArrayOutputStream);
            byteArrayOutputStream.reset();
            utf8OutputStreamWriter.writeObject(importExportService.exportQuestions());

            Resource file = new ByteArrayResource(byteArrayOutputStream.toByteArray());
            utf8OutputStreamWriter.close();
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"export.csv\"").body(file);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}