package com.bakulinTasks.socks_bakulin.exception;

import org.apache.poi.EncryptedDocumentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ModelNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(ModelNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);}

    @ExceptionHandler(WrongDataException.class)
    public ResponseEntity<String> handleWrongDataException(WrongDataException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);}

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);}

    @ExceptionHandler(EncryptedDocumentException.class)
    public ResponseEntity<String> handleEncryptedDocumentException(EncryptedDocumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);}
}
