package com.codeit.hrbank.domain.file.service;

import com.codeit.hrbank.domain.backup.dto.export.ExportEmployeeDto;
import com.codeit.hrbank.domain.file.entity.File;
import com.codeit.hrbank.domain.file.repository.FileRepository;
import com.codeit.hrbank.domain.file.util.CsvUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriUtils;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FileStorage {

    @Value("${hrbank.storage.local.root-path}")
    private String root;
    private String profilePath;
    private String backupErrorLogPath;
    private String backupFilePath;

    private final FileRepository fileRepository;
    private final CsvUtil csvUtil;

    @Transactional
    public Long saveCsv(String fileName, List<ExportEmployeeDto> exportEmployeeDtos) throws IOException {

        Path storagePath = Path.of(backupFilePath,fileName);
        if(Files.exists(storagePath)) return csvUtil.extentBackupCsv(storagePath,exportEmployeeDtos);
        return csvUtil.writeBackupCsv(storagePath,exportEmployeeDtos);
    }

    @Transactional
    public void saveProfile(Long id, byte[] bytes) throws IOException {
        Path storagePath = resolvePathFromId(id);
        Files.write(storagePath,bytes);
    }

    @Transactional(readOnly = true)
    public InputStream get(Long id) throws IOException {
        Path storagePath = resolvePathFromId(id);
        if(!Files.exists(storagePath)) return null;
        return new ByteArrayInputStream(Files.readAllBytes(storagePath));
    }

    @Transactional
    public ResponseEntity<Resource> download(Long id) throws IOException {
        File file = fileRepository.findById(id).orElseThrow();
        InputStream stream = get(id);
        Resource resource = new InputStreamResource(stream);
        String encodeFile = UriUtils.encode(file.getName(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header("Content-Disposition","attachment; filename= "+ encodeFile)
                .header("Content-Type",file.getType())
                .body(resource);
    }

    @Transactional
    public Long saveLog(String fileName, String errorMessage ) throws IOException {
        Path storagePath = Path.of(backupErrorLogPath,fileName);
        BufferedWriter bw = Files.newBufferedWriter(storagePath);
        bw.write(errorMessage);
        bw.flush();
        bw.close();
        return Files.size(storagePath);
    }

    @Transactional
    public void deleteById(Long id) throws IOException {
        Path storagePath = resolvePathFromId(id);
        if(Files.exists(storagePath)) Files.delete(storagePath);
    }


    @PostConstruct
    void init() throws IOException {

        profilePath = root+"/profile";
        backupErrorLogPath = root +"/log";
        backupFilePath = root +"/backup";

        Path tempPath = Path.of(root);
        Path logPath = Path.of(backupErrorLogPath);
        Path backupPath = Path.of(backupFilePath);
        Path imagePath = Path.of(profilePath);

        if(!Files.exists(tempPath))Files.createDirectories(tempPath);
        if(!Files.exists(logPath))Files.createDirectories(logPath);
        if(!Files.exists(imagePath))Files.createDirectories(imagePath);
        if(!Files.exists(backupPath))Files.createDirectories(backupPath);
    }


    private Path resolvePathFromId(Long id){
        File file = fileRepository.findById(id).orElseThrow();
        return switch (file.getType()) {
            case "text/csv" -> resolveBackupPath(id);
            case "text/plain" -> resolveErrorLogPath(id);
            default -> resolveProfilePath(id);
        };
    }

    private Path resolveProfilePath(Long id){
        String fileName = fileRepository.findById(id).orElseThrow().getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".")+1);
        String fileNameWithoutExtension = fileName.substring(0,fileName.lastIndexOf("."));
        fileName = fileNameWithoutExtension+"_" + id.toString() + "." + fileExtension;
        return Path.of(profilePath,fileName);
    }

    private Path resolveErrorLogPath(Long id){
        String fileName = fileRepository.findById(id).orElseThrow().getName();
        return Path.of(backupErrorLogPath,fileName);
    }

    private Path resolveBackupPath(Long id){
        String fileName = fileRepository.findById(id).orElseThrow().getName();
        return Path.of(backupFilePath,fileName);
    }

}
