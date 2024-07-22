CREATE TABLE transfers
(
    id                  UUID PRIMARY KEY,
    amount              BIGINT                      NOT NULL,
    transaction_date    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    type                VARCHAR(255)                NOT NULL,
    invoice_id          uuid,
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

CREATE TABLE invoices
(
    id                 UUID PRIMARY KEY,
    invoice_holder     uuid         not null,
    sender_id          uuid,
    recipient_id       uuid,
    invoice_id         bigint       not null,
    amount             BIGINT       NOT NULL,
    status             VARCHAR(255) NOT NULL,
    type               varchar(255) not null,
    invoice_date       timestamp    NOT NULL,
    pay_date           TIMESTAMP,
    cancel             timestamp,
    comment            TEXT,
    sender_wallet_id   UUID,
    receiver_wallet_id UUID,
    FOREIGN KEY (sender_wallet_id) REFERENCES wallets (id),
    FOREIGN KEY (receiver_wallet_id) REFERENCES wallets (id),
    foreign key (sender_id) references users (id),
    foreign key (recipient_id) references users (id),
    foreign key (invoice_holder) references users (id)
);

create sequence invoices_number_seq
start 1
increment 1
owned by invoices.invoice_id