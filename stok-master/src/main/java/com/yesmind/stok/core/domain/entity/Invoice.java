package com.yesmind.stok.core.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Invoice implements Trackable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private UUID publicId = UUID.randomUUID();

    @Enumerated(EnumType.STRING)
    private CreationStatus creationStatus;

    private LocalDateTime creationDate;

    private String reference;

    private Long referenceInvoice;
    private Long referenceBl;

    @Column(precision = 11, scale = 3)
    private BigDecimal totalBrut;

    @Column(precision = 11, scale = 3)
    private BigDecimal totalTva;

    private Float timbre;

    @Column(precision = 11, scale = 3)
    private BigDecimal totalNet;

    private String priceAsText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="client_id")
    private Client client;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<ProductInvoice> productInvoices = new ArrayList<>();

    private Boolean closed;
    private Boolean isBl;

    @Override
    public ActionType getActionType() {
        return ActionType.INVOICE;
    }

    @Override
    public String getDescription() {
        return "Invoice: " + reference;
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
