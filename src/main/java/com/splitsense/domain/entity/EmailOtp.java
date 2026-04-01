package com.splitsense.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "email_otp")
public class EmailOtp
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private int otp;

    @Column(nullable = false)
    private LocalDateTime expiryTime;

    private boolean verified;

    private LocalDateTime createdOn;
}
