package org.acme.DTOs;

import java.time.LocalDateTime;

public class BasicEventDTO {
    private Long id;
    private String title;
    private String description;
    private Long etypeId;
    private LocalDateTime eventDateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getEtypeId() {
        return etypeId;
    }

    public void setEtypeId(Long etypeId) {
        this.etypeId = etypeId;
    }

    public LocalDateTime getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(LocalDateTime eventDateTime) {
        this.eventDateTime = eventDateTime;
    }
}
