package org.acme.Model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "Logs")
public class Log extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "logSeq")
    @SequenceGenerator(name = "logSeq", sequenceName = "log_seq", allocationSize = 1)
    private Long id;

    private String level; // e.g., INFO, WARN, ERROR
    private String message;
    private String source; // e.g., class or module name
    private String username;
    private LocalDateTime timestamp;

    public Log() {}

    public Log(String level, String message, String source, String username, LocalDateTime timestamp) {
        this.level = level;
        this.message = message;
        this.source = source;
        this.username = username;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}