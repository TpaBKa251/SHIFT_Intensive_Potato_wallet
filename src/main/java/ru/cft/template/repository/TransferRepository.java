package ru.cft.template.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cft.template.entity.Transfer;
import ru.cft.template.entity.Wallet;
import ru.cft.template.model.enums.TransferType;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, UUID> {
    List<Transfer> findAllByRecipientId(UUID recipientId);

    List<Transfer> findAllBySenderId(UUID senderId);

    List<Transfer> findBySenderWallet(Wallet senderWallet);

    List<Transfer> findAllByType(TransferType type);

    List<Transfer> findBySenderWalletAndType(Wallet senderWallet, TransferType type);

    List<Transfer> findByRecipientIdAndType(UUID recipientId, TransferType type);


    @Query("SELECT t FROM Transfer t WHERE t.senderWallet.id = :walletId AND t.type = :type ORDER BY t.transferDateTime DESC")
    List<Transfer> findLastTransactionsByTypeAndWalletId(TransferType type, UUID walletId, Pageable pageable);
}
