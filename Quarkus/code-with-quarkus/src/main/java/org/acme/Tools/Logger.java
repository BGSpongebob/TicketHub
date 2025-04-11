package org.acme.Tools;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.Model.Log;
import org.acme.Repositories.LogRepository;

import java.time.LocalDateTime;

@ApplicationScoped
public class Logger {
    @Inject
    LogRepository logRepository;

    @Transactional
    public void log(String level, String message, String source, String username) {
        Log log = new Log(level, message, source, username, LocalDateTime.now());
        logRepository.persist(log);
    }

    public void info(String message, String source, String username) {
        log("INFO", message, source, username);
    }

    public void warn(String message, String source, String username) {
        log("WARN", message, source, username);
    }

    public void error(String message, String source, String username) {
        log("ERROR", message, source, username);
    }
}
