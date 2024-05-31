package id.ac.ui.cs.advprog.authentication.exceptions;

import org.springframework.http.ProblemDetail;

public interface ExceptionFactory {
    ProblemDetail createProblemDetail(Exception exception);
}