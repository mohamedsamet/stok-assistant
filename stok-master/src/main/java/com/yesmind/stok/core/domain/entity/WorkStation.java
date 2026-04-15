package com.yesmind.stok.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WorkStation implements Trackable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @Setter
    private UUID publicId = UUID.randomUUID();

    private String machine;
    private String name;
    private String operator;
    private LocalDate date;
    private Integer duration;
    private LocalDateTime creationDate;
    private String reference;

    @Enumerated(EnumType.STRING)
    private CreationStatus creationStatus;

    @OneToMany(mappedBy = "workStation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Setter
    @Builder.Default
    private List<StationTransformation> transformations = new ArrayList<>();

    @Setter
    private Boolean closed;

    @Setter
    private Boolean deleted;

    public boolean isClosed() {
        return Boolean.TRUE.equals(closed);
    }

    @Override
    public ActionType getActionType() {
        return ActionType.WORK_STATION;
    }

    @Override
    public String getDescription() {
        return "Station: machine: " + machine + " Operator: " + operator + " name: " + name + " closed: " + closed;
    }

    @Override
    public String getUser() {
        return "";
    }

    @Override
    public long getRefIncrement() {
        String[] parts = reference.split("T");
        return Long.parseLong(parts[parts.length - 1]);
    }

}
