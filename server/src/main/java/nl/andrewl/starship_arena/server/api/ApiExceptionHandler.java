package nl.andrewl.starship_arena.server.api;

import nl.andrewl.starship_arena.server.api.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ErrorResponse> handleRSE(ResponseStatusException e) {
		return new ResponseEntity<>(new ErrorResponse(e.getRawStatusCode(), e.getReason()), e.getStatus());
	}
}
