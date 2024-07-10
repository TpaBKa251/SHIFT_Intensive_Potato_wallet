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
    @Query("select i from Invoice i where i.invoiceHolder.Id = i.sender.Id and i.invoiceHolder.Id = :senderId")
    List<Invoice> findBySenderId(UUID senderId);

    @Query("select i from Invoice i where i.invoiceHolder.Id = i.recipient.Id and i.invoiceHolder.Id = :recipientId")
    List<Invoice> findByRecipientId(UUID recipientId);

    List<Invoice> findByInvoiceHolder(User invoiceHolder);

    @Query("select i from Invoice i where i.id = :invoiceId and i.invoiceHolder.Id = :invoiceHolderId")
    Invoice findByIdAndInvoiceHolderId(UUID invoiceId, UUID invoiceHolderId);

    @Query("select i from Invoice i where i.invoiceHolder.Id = :invoiceHolderId and i.invoiceNumber = :number")
    Invoice finByInvoiceHolderIdAndInvoiceNumber(UUID invoiceHolderId, Long number);

    @Query("select i from Invoice i where i.invoiceHolder.Id = :invoiceHolderId and i.type = 'INCOMING' and i.status = 'UNPAID' order by i.invoiceDateTime desc")
    List<Invoice> findLastInvoiceByInvoiceHolderId(UUID invoiceHolderId);
}
