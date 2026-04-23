package order.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import common.exception.ApiError;
import common.exception.GlobalExceptionHandler;
import order.entity.OrderStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class OrderExceptionHandler {

    private final GlobalExceptionHandler globalExceptionHandler;

    public OrderExceptionHandler(GlobalExceptionHandler globalExceptionHandler) {
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadableMessage(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {
        String message = "Invalid JSON or wrong data type.";
        List<String> details = List.of();

        if (exception.getCause() instanceof InvalidFormatException invalidFormatException
                && invalidFormatException.getTargetType() == OrderStatus.class) {
            details = List.of("status: Invalid value. Allowed values: "
                    + String.join(", ", Arrays.stream(OrderStatus.values()).map(Enum::name).toList())
                    + ".");
        }

        return ResponseEntity.badRequest().body(globalExceptionHandler.buildError(
                HttpStatus.BAD_REQUEST,
                message,
                request.getRequestURI(),
                details
        ));
    }
}
