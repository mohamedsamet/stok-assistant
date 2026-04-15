package com.yesmind.stok.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Client implements Trackable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private UUID publicId = UUID.randomUUID();

    private String name;

    private Boolean deleted;

    @Enumerated(EnumType.STRING)
    private CreationStatus creationStatus;

    private LocalDateTime creationDate;

    private String email;

    private String address;
    private String tel;

    private String reference;

    private String fiscalId;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Invoice> invoices = new ArrayList<>();

    @Override
    public ActionType getActionType() {
        return ActionType.CLIENT;
    }

    @Override
    public String getDescription() {
        return "Client: " + name + " isDeleted: " + deleted;
    }

    @Override
    public String getUser() {
        return "admin";
    }

    @Override
    public long getRefIncrement() {
        String[] parts = reference.split("E");
        return Long.parseLong(parts[parts.length - 1]);
    }
}
