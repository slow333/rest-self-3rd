package com.magic.system.exception;

import com.magic.system.Result;
import com.magic.system.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class HandleRestControllerAdvice {

  @ExceptionHandler(ObjectNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Result handleObjectNotFound(ObjectNotFoundException ex){
    return new Result(false, StatusCode.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public Result handleUsernameNotFound(Exception ex){
    return new Result(false, StatusCode.UNAUTHORIZED,
            "The username or password is invalid.", ex.getMessage());
  }

  @ExceptionHandler(InvalidBearerTokenException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public Result handleInvalidBearerToken(InvalidBearerTokenException ex){
    return new Result(false, StatusCode.UNAUTHORIZED,
            "The token is invalid.", ex.getMessage());
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public Result handleAccessDenied(AccessDeniedException ex){
    return new Result(false, StatusCode.UNAUTHORIZED,
            "No Permission.", ex.getMessage());
  }

  @ExceptionHandler(InsufficientAuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  Result handleInsufficientAuthentication(InsufficientAuthenticationException ex){
    return new Result(true, StatusCode.UNAUTHORIZED, "Login credentials are missing", ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Result handleMethodArgNotValidException(MethodArgumentNotValidException ex) {
    List<ObjectError> errors = ex.getBindingResult().getAllErrors();
    Map<String, String> map = new HashMap<>(errors.size());
    errors.forEach(e -> {
            String key = ((FieldError) e).getField();
            String value = e.getDefaultMessage();
            map.put(key, value);
    });
    return new Result(false, StatusCode.BAD_REQUEST,
            "Provided arguments invalid.", map);
  }

  @ExceptionHandler(PasswordChangeIllegalArgException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Result handlePasswordChangeIllegalArgumentException(PasswordChangeIllegalArgException ex){
    return new Result(false, StatusCode.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Result handleException(Exception ex){
    return new Result(false, StatusCode.INTERNAL_SERVER_ERROR, ex.getMessage());
  }
  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Result handleRuntimeException(RuntimeException ex){
    return new Result(false, StatusCode.INTERNAL_SERVER_ERROR, ex.getMessage());
  }
}
