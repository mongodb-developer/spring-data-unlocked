package com.mongodb.domain.service;

import com.mongodb.domain.model.Transaction;
import com.mongodb.domain.model.Transaction.AccountDetails;
import com.mongodb.domain.model.Transaction.Beneficiary;
import com.mongodb.domain.model.Transaction.Originator;
import com.mongodb.resources.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setup() {
        transactionRepository.deleteAll();
    }

    @Test
    void shouldSaveTransaction() {
        Transaction transaction = createSampleTransaction("CREDIT", "USD", "PENDING");

        Transaction saved = transactionService.save(transaction);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCurrency()).isEqualTo("USD");
    }

    @Test
    void shouldFindAllWithPagination() {
        transactionService.save(createSampleTransaction("DEBIT", "USD", "COMPLETED"));
        var result = transactionService.findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")));

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void shouldSearchByTypeAndCurrency() {
        transactionService.save(createSampleTransaction("CREDIT", "EUR", "PENDING"));

        List<Transaction> found = transactionService.searchTransactions("CREDIT", "EUR", null);

        assertThat(found).hasSize(1);
    }

    @Test
    void shouldSearchByStatus() {
        transactionService.save(createSampleTransaction("DEBIT", "BRL", "ERROR"));

        List<Transaction> found = transactionService.searchTransactions(null, null, "ERROR");

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getStatus()).isEqualTo("ERROR");
    }

    @Test
    void shouldUpdateTransaction() {
        Transaction transaction = transactionService.save(createSampleTransaction("CREDIT", "USD", "PENDING"));

        transaction.setStatus("COMPLETED");
        Transaction updated = transactionService.update(transaction);

        assertThat(updated.getStatus()).isEqualTo("COMPLETED");
    }

    @Test
    void shouldDeleteTransaction() {
        Transaction transaction = transactionService.save(createSampleTransaction("CREDIT", "USD", "PENDING"));

        transactionService.delete(transaction.getId());

        assertThat(transactionRepository.existsById(transaction.getId())).isFalse();
    }

    @Test
    void shouldThrowWhenDeletingNonexistentTransaction() {
        assertThatThrownBy(() -> transactionService.delete("nonexistent-id"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Transaction not found");
    }

    private Transaction createSampleTransaction(String type, String currency, String status) {
        return new Transaction(
                "123",
                type,
                500.0,
                currency,
                status,
                "Sample",
                new AccountDetails(
                        new Originator("111", "Ricardo", "Bank A"),
                        new Beneficiary("222", "Maria", "Bank B")
                ),
                "123",
                LocalDateTime.now());

    }
}
