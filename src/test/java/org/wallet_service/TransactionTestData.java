package org.wallet_service;

import org.wallet_service.dto.TransactionTO;
import org.wallet_service.entity.Operation;
import org.wallet_service.entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TransactionTestData {
    public static final UUID ID1 = UUID.fromString("694bc4c1-2987-4bd3-a71b-5809ef6686c1");
    public static final UUID ID2 = UUID.fromString("6d4c78d7-b8b0-4e3a-a488-46508fe91e20");
    public static final UUID ID3 = UUID.fromString("bae26c79-e40b-495d-87b0-24255a9d383a");
    public static final UUID ID4 = UUID.fromString("acb79fd6-88a4-455f-ac10-394ad7c0f336");
    public static final UUID ID5 = UUID.fromString("711c12eb-9f98-417d-af8f-57f902d3000e");

    public static TransactionTO TRANSACTION_TO_1 = new TransactionTO(ID1, Operation.CREDIT, new BigDecimal("1000.00"), "transaction #1", 1001);
    public static TransactionTO TRANSACTION_TO_2 = new TransactionTO(ID2, Operation.DEBIT, new BigDecimal("699.99"), "transaction #2", 1001);
    public static TransactionTO TRANSACTION_TO_3 = new TransactionTO(ID3, Operation.DEBIT, new BigDecimal("300.01"), "transaction #3", 1001);
    public static TransactionTO TRANSACTION_TO_4 = new TransactionTO(ID4, Operation.DEBIT, new BigDecimal("100.00"), "transaction #4", 1001);
    public static TransactionTO TRANSACTION_TO_5 = new TransactionTO(ID5, Operation.CREDIT, new BigDecimal("444.44"), "transaction #5", 1001);

    public static TransactionTO TRANSACTION_WITHOUT_ID = new TransactionTO(null, Operation.CREDIT, new BigDecimal("444.44"), "transaction #5", 1001);

    public static final Transaction TRANSACTION_1 = new Transaction(ID1, LocalDateTime.of(2023, Month.OCTOBER, 9, 17, 11, 50),
            "transaction #1", Operation.CREDIT, new BigDecimal("1000.00"), 1001, true);
    public static final Transaction TRANSACTION_2 = new Transaction(ID2, LocalDateTime.of(2023, Month.OCTOBER, 9, 17, 12, 5),
            "transaction #2", Operation.DEBIT, new BigDecimal("699.99"), 1001, true);
    public static final Transaction TRANSACTION_3 = new Transaction(ID3, LocalDateTime.of(2023, Month.OCTOBER, 9, 18, 30, 10),
            "transaction #3", Operation.DEBIT, new BigDecimal("300.01"), 1001, false);
    public static final Transaction TRANSACTION_4 = new Transaction(ID4, LocalDateTime.of(2023, Month.OCTOBER, 9, 18, 40, 10),
            "transaction #4", Operation.DEBIT, new BigDecimal("100.00"), 1001, false);
    public static final Transaction TRANSACTION_5 = new Transaction(ID5, LocalDateTime.of(2023, Month.OCTOBER, 9, 18, 50, 10),
            "transaction #5", Operation.CREDIT, new BigDecimal("444.44"), 1001, false);

//    public static final Transaction NOT_PROCESSED_TRANSACTION_1 = new Transaction(ID1, LocalDateTime.of(2023, Month.OCTOBER, 9, 17, 11, 50),
//            "transaction #1", Operation.CREDIT, new BigDecimal("1000.00"), 1001, false);
//    public static final Transaction NOT_PROCESSED_TRANSACTION_2 = new Transaction(ID2, LocalDateTime.of(2023, Month.OCTOBER, 9, 17, 12, 5),
//            "transaction #2", Operation.DEBIT, new BigDecimal("699.99"), 1001, false);

    public static final List<Transaction> TRANSACTIONS = new ArrayList<>();
    public static final List<TransactionTO> TRANSACTIONS_TOS = new ArrayList<>();
    public static final List<TransactionTO> NOT_PROCESSED_TRANSACTIONS_TOS = new ArrayList<>();
    public static final List<Transaction> NOT_PROCESSED_TRANSACTIONS = new ArrayList<>();
    public static final List<UUID> TRANSACTIONS_IDS = new ArrayList<>();
    public static final List<UUID> NOT_PROCESSED_TRANSACTIONS_IDS = new ArrayList<>();
//    public static final List<Transaction> FULL_NOT_PROCESSED_TRANSACTIONS = new ArrayList<>();

    static {
        Collections.addAll(TRANSACTIONS_TOS, TRANSACTION_TO_1, TRANSACTION_TO_2, TRANSACTION_TO_3, TRANSACTION_TO_4, TRANSACTION_TO_5);
        Collections.addAll(NOT_PROCESSED_TRANSACTIONS_TOS, TRANSACTION_TO_3, TRANSACTION_TO_4, TRANSACTION_TO_5);
        Collections.addAll(TRANSACTIONS, TRANSACTION_1, TRANSACTION_2, TRANSACTION_3, TRANSACTION_4, TRANSACTION_5);
        Collections.addAll(NOT_PROCESSED_TRANSACTIONS, TRANSACTION_3, TRANSACTION_4, TRANSACTION_5);
        Collections.addAll(TRANSACTIONS_IDS, ID1, ID2, ID3, ID4, ID5);
        Collections.addAll(NOT_PROCESSED_TRANSACTIONS_IDS, ID3, ID4, ID5);
//        Collections.addAll(FULL_NOT_PROCESSED_TRANSACTIONS, NOT_PROCESSED_TRANSACTION_1, NOT_PROCESSED_TRANSACTION_2, TRANSACTION_3, TRANSACTION_4, TRANSACTION_5);
    }
}
