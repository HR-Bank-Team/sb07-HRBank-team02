package com.codeit.hrbank.domain.file.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileStorage fileStorage;


    public ResponseEntity<Resource> downloadFileService(Long id) throws IOException {
       return  fileStorage.download(id);
    }

}
