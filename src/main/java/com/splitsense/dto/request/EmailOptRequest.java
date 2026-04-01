package com.splitsense.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class EmailOptRequest
{
    private String email;
    private int otp;
    private LocalDateTime expiryTime;
    private boolean verified;
    private LocalDateTime createdOn;
}
