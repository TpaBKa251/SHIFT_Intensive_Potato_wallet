CREATE TABLE invoices
(
    id                 UUID PRIMARY KEY,
    sender_id          uuid,
    recipient_id       uuid,
    amount             BIGINT       NOT NULL,
    invoice_number     BIGINT       NOT NULL,
    status             VARCHAR(255) NOT NULL,
    type               varchar(255) not null,
    invoice_date       DATE         NOT NULL,
    pay_date date,
    cancel date,
    comment            TEXT,
    sender_wallet_id   UUID,
    receiver_wallet_id UUID,
    FOREIGN KEY (sender_wallet_id) REFERENCES wallets (id),
    FOREIGN KEY (receiver_wallet_id) REFERENCES wallets (id),
    foreign key (sender_id) references users (id),
    foreign key (recipient_id) references users(id)
);