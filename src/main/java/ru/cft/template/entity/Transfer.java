package ru.cft.template.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.cft.template.model.TransferStatus;
import ru.cft.template.model.TransferType;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@Table(name = "transfers")
@Entity
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false, name = "transaction_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime transferDateTime = LocalDateTime.now();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransferType type;

    @Column
    private Long recipientPhone;

    @Column
    private UUID recipientId;

    @Column(name = "sender_id")
    private UUID senderId;

    @Column
    private Long invoiceNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    @ManyToOne
    @JoinColumn(name = "sender_wallet_id", referencedColumnName = "id")
    private Wallet senderWallet;

    @ManyToOne
    @JoinColumn(name = "recipient_wallet_id", referencedColumnName = "id")
    private Wallet recipientWallet;


}
