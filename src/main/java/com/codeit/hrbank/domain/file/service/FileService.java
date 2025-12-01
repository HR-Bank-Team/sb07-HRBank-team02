package com.codeit.hrbank.domain.file.service;

import com.codeit.hrbank.domain.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileStorage fileStorage;

    public String downloadFileService(Long id){
        return null;
    }

}
