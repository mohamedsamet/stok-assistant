package com.yesmind.stok.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product implements Trackable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private UUID publicId = UUID.randomUUID();

    @Column(name = "name", columnDefinition = "text")
    private String name;
    private String provider;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Column(precision = 11, scale = 3)
    private BigDecimal initialValue;

    private Boolean deleted;

    @Enumerated(EnumType.STRING)
    private CreationStatus creationStatus;

    private LocalDateTime creationDate;

    @Column(name = "reference", columnDefinition = "text")
    private String reference;

    @Column(precision = 11, scale = 3)
    private BigDecimal quantity;

    @Column(precision = 11, scale = 3)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<StationTransformation> transformations = new HashSet<>();

    @Override
    public ActionType getActionType() {
        return ActionType.PRODUCT;
    }

    @Override
    public String getDescription() {
        return "Produit: " + name + " isDeleted: " + deleted + " quantity: " + quantity;
    }

    @Override
    public String getUser() {
        return "admin";
    }

    @Override
    public long getRefIncrement() {
        String[] parts = reference.split("T");
        return Long.parseLong(parts[parts.length - 1]);
    }
}
