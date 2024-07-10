package ru.cft.template.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.cft.template.model.enums.InvoiceStatus;
import ru.cft.template.model.enums.InvoiceType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Table(name = "invoices")
@Entity
@Data
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    private User recipient;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private Long invoiceNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceType type;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "invoice_date")
    private LocalDateTime invoiceDateTime = LocalDateTime.now();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "pay_date")
    private LocalDateTime payDateTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "cancel")
    private LocalDateTime cancelDateTime;

    @Column
    private String comment;

    @ManyToOne
    @JoinColumn(name = "sender_wallet_id", referencedColumnName = "id")
    private Wallet senderWallet;

    @ManyToOne
    @JoinColumn(name = "receiver_wallet_id", referencedColumnName = "id")
    private Wallet receiverWallet;
}
