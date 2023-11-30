package org.wallet_service;

import org.wallet_service.dto.request.TransactionRequestDTO;
import org.wallet_service.entity.OperationType;
import org.wallet_service.entity.Transaction;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.wallet_service.AbstractServiceTest.*;

public class TransactionTestData {
    public static final UUID ID1 = UUID.fromString("694bc4c1-2987-4bd3-a71b-5809ef6686c1");
    public static final UUID ID2 = UUID.fromString("6d4c78d7-b8b0-4e3a-a488-46508fe91e20");
    public static final UUID ID3 = UUID.fromString("bae26c79-e40b-495d-87b0-24255a9d383a");
    public static final UUID ID4 = UUID.fromString("acb79fd6-88a4-455f-ac10-394ad7c0f336");
    public static final UUID ID5 = UUID.fromString("711c12eb-9f98-417d-af8f-57f902d3000e");

    public static TransactionRequestDTO TRANSACTION_DTO_1 = new TransactionRequestDTO(ID1.toString(), "CREDIT", "1000.00", "transaction #1", TOKEN);
    public static TransactionRequestDTO TRANSACTION_DTO_2 = new TransactionRequestDTO(ID2.toString(), "DEBIT", "699.99", "transaction #2", TOKEN);
    public static TransactionRequestDTO TRANSACTION_DTO_3 = new TransactionRequestDTO(ID3.toString(), "DEBIT", "300.01", "transaction #3", TOKEN);
    public static TransactionRequestDTO TRANSACTION_DTO_4 = new TransactionRequestDTO(ID4.toString(), "DEBIT", "100.00", "transaction #4", TOKEN);
    public static TransactionRequestDTO TRANSACTION_DTO_5 = new TransactionRequestDTO(ID5.toString(), "CREDIT", "444.44", "transaction #5", TOKEN);

    public static TransactionRequestDTO TRANSACTION_TO_WITH_EXPIRED_TOKEN = new TransactionRequestDTO(ID3.toString(), "DEBIT", "300.01", "transaction #3", EXPIRED_TOKEN);
    public static TransactionRequestDTO TRANSACTION_TO_WITH_OTHER_TOKEN = new TransactionRequestDTO(ID3.toString(), "DEBIT", "300.01", "transaction #3", OTHER_TOKEN);
    public static TransactionRequestDTO TRANSACTION_DTO_3_EMPTY_OPERATION = new TransactionRequestDTO(ID3.toString(), "", "300.01", "transaction #3", TOKEN);

    public static final Transaction TRANSACTION_1 = new Transaction(ID1, Timestamp.valueOf(LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 14, 10)),
            "transaction #1", OperationType.CREDIT, new BigDecimal("1000.00"), 1001, true);
    public static final Transaction TRANSACTION_2 = new Transaction(ID2, Timestamp.valueOf(LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 14, 20)),
            "transaction #2", OperationType.DEBIT, new BigDecimal("699.99"), 1001, true);
    public static final Transaction TRANSACTION_3 = new Transaction(ID3, Timestamp.valueOf(LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 14, 30)),
            "transaction #3", OperationType.DEBIT, new BigDecimal("300.01"), 1001, false);
    public static final Transaction TRANSACTION_4 = new Transaction(ID4, Timestamp.valueOf(LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 14, 40)),
            "transaction #4", OperationType.DEBIT, new BigDecimal("100.00"), 1001, false);
    public static final Transaction TRANSACTION_5 = new Transaction(ID5, Timestamp.valueOf(LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 14, 50)),
            "transaction #5", OperationType.CREDIT, new BigDecimal("444.44"), 1001, false);

    public static final List<Transaction> TRANSACTIONS = new ArrayList<>();
    public static final List<TransactionRequestDTO> TRANSACTIONS_TOS = new ArrayList<>();
    public static final List<TransactionRequestDTO> NOT_PROCESSED_TRANSACTIONS_TOS = new ArrayList<>();
    public static final List<Transaction> NOT_PROCESSED_TRANSACTIONS = new ArrayList<>();
    public static final List<UUID> TRANSACTIONS_IDS = new ArrayList<>();
    public static final List<UUID> NOT_PROCESSED_TRANSACTIONS_IDS = new ArrayList<>();

    static {
        Collections.addAll(TRANSACTIONS_TOS, TRANSACTION_DTO_3, TRANSACTION_DTO_4, TRANSACTION_DTO_5);
        Collections.addAll(NOT_PROCESSED_TRANSACTIONS_TOS, TRANSACTION_DTO_3, TRANSACTION_DTO_4, TRANSACTION_DTO_5);
        Collections.addAll(TRANSACTIONS, TRANSACTION_1, TRANSACTION_2, TRANSACTION_3, TRANSACTION_4, TRANSACTION_5);
        Collections.addAll(NOT_PROCESSED_TRANSACTIONS, TRANSACTION_3, TRANSACTION_4, TRANSACTION_5);
        Collections.addAll(TRANSACTIONS_IDS, ID3, ID4, ID5);
        Collections.addAll(NOT_PROCESSED_TRANSACTIONS_IDS, ID3, ID4, ID5);
    }
}
