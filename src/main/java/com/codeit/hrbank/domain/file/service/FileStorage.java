package com.codeit.hrbank.domain.file.service;

import com.codeit.hrbank.domain.backup.dto.export.ExportEmployeeDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;

@Component
public class FileStorage {

    @Value("${hrbank.storage.local.root-path}")
    private String root;

    public Long put(Long id,byte[] bytes) throws IOException {
        Path storagePath = resolvePath(id);
        Files.write(storagePath,bytes);
        return id;
    }

    public void putCsv(String fileName, List<ExportEmployeeDto> exportEmployeeDtos) throws IOException {
        BufferedWriter bw = Files.newBufferedWriter(Path.of(root,fileName));
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
    }

    public InputStream get(Long id){
        return null;
    }

    @PostConstruct
    void init() throws IOException {
        Path tempPath = Path.of(root);
        if(!Files.exists(tempPath))Files.createDirectories(tempPath);
    }


    private Path resolvePath(Long id){
        Path temp = Path.of(root);
        return temp.resolve(id.toString());
    }



}
