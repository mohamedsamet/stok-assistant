package com.yesmind.stok.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @Setter
    private UUID publicId = UUID.randomUUID();

    private UUID actionPublicId;

    @Enumerated(EnumType.STRING)
    private ActionType type;

    private String operator;
    private LocalDateTime dateTime;

    private String description;
}
