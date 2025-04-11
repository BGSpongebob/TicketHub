package org.acme.Tools.Validation;

import jakarta.enterprise.context.ApplicationScoped;
import org.postgresql.util.PSQLException;

@ApplicationScoped
public class DuplicateValidator {

    public void validateDuplicate(Exception e) throws DuplicateException {
        // Walk the exception chain to find the root cause
        Throwable rootCause = e;
        while (rootCause.getCause() != null && rootCause != rootCause.getCause()) {
            rootCause = rootCause.getCause();
        }

        // Check for PostgreSQL unique constraint violation
        if (rootCause instanceof PSQLException psqlException) {
            String message = psqlException.getMessage().toLowerCase();
            if (message.contains("phone")) {
                throw new DuplicateException("This phone number has already been registered!");
            } else if (message.contains("email")) {
                throw new DuplicateException("This email has already been registeredQ");
            } else if (message.contains("username")) {
                throw new DuplicateException("This username has already been registered!");
            }
        }
        // If not a recognized duplicate issue, rethrow as a generic runtime exception
        throw new RuntimeException("Unexpected error while validating duplicate: " + rootCause.getMessage(), rootCause);
    }
}