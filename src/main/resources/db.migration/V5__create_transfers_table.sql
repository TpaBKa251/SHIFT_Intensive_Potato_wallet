CREATE TABLE transfers
(
    id                  UUID PRIMARY KEY,
    amount              BIGINT                      NOT NULL,
    transaction_date    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    type                VARCHAR(255)                NOT NULL,
    invoice_number      BIGINT,
    recipient_phone     BIGINT,
    recipient_id        uuid,
    sender_id           uuid,
    status              VARCHAR(255)                NOT NULL,
    sender_wallet_id    UUID,
    recipient_wallet_id UUID,
    FOREIGN KEY (sender_wallet_id) REFERENCES wallets (id),
    FOREIGN KEY (recipient_wallet_id) REFERENCES wallets (id),
    foreign key (recipient_id) references users (id),
    foreign key (sender_id) references users (id)
);