package com.drewfilkins.spring_boot_mvc.controller;

import java.time.LocalDateTime;

public class ServerErrorDto {

    private String message;
    private String detailedMessage;
    private LocalDateTime dateTime;

    public ServerErrorDto(String message, String detailedMessage, LocalDateTime dateTime) {
        this.message = message;
        this.detailedMessage = detailedMessage;
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }

    public void setDetailedMessage(String detailedMessage) {
        this.detailedMessage = detailedMessage;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
