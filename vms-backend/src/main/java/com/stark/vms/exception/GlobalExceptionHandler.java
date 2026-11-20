package com.stark.vms.exception;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<Map<String, Object>> body(HttpStatus s, String msg, Map<String, String> fields) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("status", s.value());
        m.put("message", msg);
        if (fields != null) m.put("fieldErrors", fields);
        return ResponseEntity.status(s).body(m);
    }

    @ExceptionHandler(DuplicateVehicleException.class)
    public ResponseEntity<Map<String, Object>> duplicate(DuplicateVehicleException e) {
        return body(HttpStatus.CONFLICT, e.getMessage(), Map.of("vehicleNumber", e.getMessage()));
    }
    @ExceptionHandler(DataIntegrityViolationException.class)   // race condition on unique constraint
    public ResponseEntity<Map<String, Object>> integrity(DataIntegrityViolationException e) {
        String m = new DuplicateVehicleException().getMessage();
        return body(HttpStatus.CONFLICT, m, Map.of("vehicleNumber", m));
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> notFound(NotFoundException e) {
        return body(HttpStatus.NOT_FOUND, e.getMessage(), null);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> invalid(MethodArgumentNotValidException e) {
        Map<String, String> f = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(x -> f.putIfAbsent(x.getField(), x.getDefaultMessage()));
        return body(HttpStatus.BAD_REQUEST, "Validation failed", f);
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> status(ResponseStatusException e) {
        return body(HttpStatus.valueOf(e.getStatusCode().value()), e.getReason(), null);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> unreadable(HttpMessageNotReadableException e) {
        return body(HttpStatus.BAD_REQUEST, "Malformed request body (check field values, e.g. status)", null);
    }
}
