package net.jordimp.casino.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(PlayerNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handlePlayerNotFound(PlayerNotFoundException e) {
		return new ErrorResponse("PlayerNotFound", e.getMessage());
	}

	@ExceptionHandler({InsufficientBalanceException.class, SessionExpiredException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleDomainError(RuntimeException e) {
		return new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleIllegalArgument(IllegalArgumentException e) {
		return new ErrorResponse("BadRequest", e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleGenericException(Exception e) {
		return new ErrorResponse("ServerError", "An error occurred: " + e.getMessage());
	}
}
