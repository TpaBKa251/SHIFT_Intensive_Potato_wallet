package ru.cft.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cft.template.entity.Invoice;
import ru.cft.template.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    List<Invoice> findBySenderId(UUID senderId);

    List<Invoice> findByRecipientId(UUID recipientId);

    List<Invoice> findByInvoiceHolder(User invoiceHolder);

    @Query("select i from Invoice i where i.invoiceHolder.Id = :invoiceHolderId and i.invoiceNumber = :number")
    Invoice finByInvoiceHolderIdAndInvoiceNumber(UUID invoiceHolderId, Long number);
}
