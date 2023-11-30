package org.wallet_service.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallet_service.AbstractServiceTest;
import org.wallet_service.PlayerTestData;
import org.wallet_service.entity.MoneyAccount;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class MoneyAccountServiceTest extends AbstractServiceTest {

    @Autowired
    private MoneyAccountService moneyAccountService;

    @Test
    void save() throws SQLException {
        MoneyAccount moneyAccount = moneyAccountService.save(PlayerTestData.ACCOUNT_1002_WITHOUT_ID);
        assertThat(moneyAccount).usingRecursiveComparison().isEqualTo(PlayerTestData.ACCOUNT_1002_WITH_ID);
        connection.createStatement().executeUpdate("DELETE FROM wallet.money_account WHERE id = 1002");
        assertThat(moneyAccountService.get(1002)).isNull();
    }

    @Test
    void get(){
        MoneyAccount moneyAccount = moneyAccountService.get(1001);
        assertThat(moneyAccount).isNotNull();
        assertThat(moneyAccount).usingRecursiveComparison().isEqualTo(PlayerTestData.ACCOUNT_1001_WITH_ID);
    }

    @Test
    void updateBalance(){
        moneyAccountService.updateBalance(PlayerTestData.CHANGED_BALANCE_ACCOUNT_1001_WITH_ID);
        assertThat(moneyAccountService.get(1001).getBalance()).isEqualTo("500.00");
        moneyAccountService.updateBalance(PlayerTestData.ACCOUNT_1001_WITH_ID);
    }
}
