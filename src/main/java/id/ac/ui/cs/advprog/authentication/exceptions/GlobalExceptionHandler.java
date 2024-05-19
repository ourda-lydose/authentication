package id.ac.ui.cs.advprog.authentication.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ExceptionFactory exceptionFactory;

    @Autowired
    public GlobalExceptionHandler(ExceptionFactory exceptionFactory) {
        this.exceptionFactory = exceptionFactory;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception exception) {
        // TODO send this stack trace to an observability tool
        exception.printStackTrace();
        return exceptionFactory.createProblemDetail(exception);
    }
}