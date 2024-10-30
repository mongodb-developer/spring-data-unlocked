package com.mongodb;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "transactions")
@Data

public class Transaction {
    @Id
    private String id;
    private String transactionType;
    private Double amount;
    private String currency;
    private String status;
    private String description;
    private LocalDateTime createdAt;
    private AccountDetails accountDetails;

    public record AccountDetails(Originator originator, Beneficiary beneficiary) {}
    public record Originator(String accountNumber, String name, String bank) {}
    public record Beneficiary(String accountNumber, String name, String bank) {}

}

