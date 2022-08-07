package com.woowahan.app.shop.error;


import com.woowahan.elasticsearch.shop.error.FailedElasticsearchActionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SearchExcpetionHandler {

    @ExceptionHandler(FailedElasticsearchActionException.class)
    protected ResponseEntity<String> handleSearchException(FailedElasticsearchActionException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<String> handleSearchException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
