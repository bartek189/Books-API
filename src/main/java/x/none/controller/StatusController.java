package x.none.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import x.none.model.dto.StatusResponseDto;
import x.none.service.StatusService;

@RestController
@RequestMapping("/api/v1/imports")
@RequiredArgsConstructor
public class StatusController {

    private final StatusService service;


    @GetMapping("/{id}")
    public ResponseEntity<StatusResponseDto> getStatus(@PathVariable long id) {
        StatusResponseDto s = service.getStatus(id);
        return ResponseEntity.status(HttpStatus.OK).body(s);

    }
}
