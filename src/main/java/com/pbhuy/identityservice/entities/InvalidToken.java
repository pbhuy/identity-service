package com.pbhuy.identityservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "invalid_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvalidToken {
    @Id
    private String id;
    private Date expiryTime;
}
