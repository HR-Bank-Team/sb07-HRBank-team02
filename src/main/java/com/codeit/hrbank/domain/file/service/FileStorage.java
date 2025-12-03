package com.codeit.hrbank.domain.file.service;

import com.codeit.hrbank.domain.backup.dto.export.ExportEmployeeDto;
import com.codeit.hrbank.domain.file.entity.File;
import com.codeit.hrbank.domain.file.repository.FileRepository;
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
import java.text.MessageFormat;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FileStorage {

    @Value("${hrbank.storage.local.root-path}")
    private String root;

    private final FileRepository fileRepository;

    @Transactional
    public Long saveCsv(String fileName, List<ExportEmployeeDto> exportEmployeeDtos) throws IOException {
        Path storagePath = Path.of(root,fileName);
        BufferedWriter bw = Files.newBufferedWriter(storagePath);
        bw.write("ID,employeeNumber,name,email,department,position,hiredate,status");
        bw.newLine();
        for(ExportEmployeeDto dto : exportEmployeeDtos){
            String line = MessageFormat.format("{0},{1},{2},{3},{4},{5},{6},{7}",
                    dto.id(),
                    dto.employeeNumber(),
                    dto.name(),
                    dto.email(),
                    dto.departmentName(),
                    dto.position(),
                    dto.hireDate(),
                    dto.status()
                    );
            bw.write(line);
            bw.newLine();
        }
        bw.flush();
        bw.close();

        return Files.size(storagePath);
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


    @PostConstruct
    void init() throws IOException {
        Path tempPath = Path.of(root);
        if(!Files.exists(tempPath))Files.createDirectories(tempPath);
    }


    private Path resolvePathFromId(Long id){
        String fileName = fileRepository.findById(id).orElseThrow().getName();
        return Path.of(root,fileName);
    }

}
