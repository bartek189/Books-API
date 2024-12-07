package x.none.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import x.none.model.dto.JobIdResponseDto;
import x.none.service.BooksImportService;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookController {


    private final BooksImportService service;

    @PostMapping("/import")
    public ResponseEntity<JobIdResponseDto> load(@RequestParam("file") MultipartFile file) throws RuntimeException, IOException {
        File fileTemp = service.getFile(file);
        JobIdResponseDto id = service.getMessage(fileTemp);
        return new ResponseEntity<JobIdResponseDto>(id, HttpStatus.OK);
    }

}


