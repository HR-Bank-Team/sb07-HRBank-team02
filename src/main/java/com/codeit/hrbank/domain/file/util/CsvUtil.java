package com.codeit.hrbank.domain.file.util;

import com.codeit.hrbank.domain.backup.dto.export.ExportEmployeeDto;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Component
public class CsvUtil {

    public Long writeBackupCsv(Path filePath, List<ExportEmployeeDto> exportEmployeeDtos) throws IOException {

        OutputStream os = Files.newOutputStream(filePath, StandardOpenOption.CREATE);
        BufferedOutputStream bos = new BufferedOutputStream(os);

        bos.write(0xEF);
        bos.write(0xBB);
        bos.write(0xBF);

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(bos, StandardCharsets.UTF_8);
        CSVWriter csvWriter = new CSVWriter(outputStreamWriter);

        String[] header = {"ID", "사원번호", "이름", "이메일", "부서", "직책", "채용일", "상태"};
        csvWriter.writeNext(header);

        // 데이터 작성
        for (ExportEmployeeDto dto : exportEmployeeDtos) {
            String[] line = {
                    String.valueOf(dto.id()),
                    dto.employeeNumber(),
                    dto.name(),
                    dto.email(),
                    dto.departmentName(),
                    dto.position(),
                    dto.hireDate().toString(),          // LocalDate -> String
                    dto.status().name()                // enum -> String
            };
            csvWriter.writeNext(line);
        }
        csvWriter.flush();
        csvWriter.close();

        return Files.size(filePath);
    }

    public Long extentBackupCsv(Path filePath, List<ExportEmployeeDto> exportEmployeeDtos) throws IOException {

        OutputStream os = Files.newOutputStream(filePath, StandardOpenOption.APPEND);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(bos, StandardCharsets.UTF_8);
        CSVWriter csvWriter = new CSVWriter(outputStreamWriter);

        // 데이터 작성
        for (ExportEmployeeDto dto : exportEmployeeDtos) {
            String[] line = {
                    String.valueOf(dto.id()),
                    dto.employeeNumber(),
                    dto.name(),
                    dto.email(),
                    dto.departmentName(),
                    dto.position(),
                    dto.hireDate().toString(),          // LocalDate -> String
                    dto.status().name()                // enum -> String
            };
            csvWriter.writeNext(line);
        }
        csvWriter.flush();
        csvWriter.close();

        return Files.size(filePath);
    }
}

