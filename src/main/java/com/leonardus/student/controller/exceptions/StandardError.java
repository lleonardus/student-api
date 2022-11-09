package com.leonardus.student.controller.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class StandardError {
    private LocalDateTime timestamp;
    private Integer status;
    private String message;
    private String path;
}
