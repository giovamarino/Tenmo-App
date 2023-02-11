package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTests extends BaseDaoTests {
//bob account 1
//user account 2
    private static final Account ACCOUNT_1 = new Account(2001, 1001, new BigDecimal("100.00"));
    private static final Account ACCOUNT_2 = new Account(2002, 1002, new BigDecimal("500.00"));


    private static final User bob = new User(1001,"bob","$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2", "[Authority{name=ROLE_USER}]");
    private static final User user = new User(1002,"user", "$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEy","[Authority{name=ROLE_USER}]");


    private JdbcAccountDao sut;


    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void get_balance_returns_correct_balance(){
        BigDecimal test = sut.getBalance(bob.getId());
        Assert.assertEquals(ACCOUNT_1.getBalance(), test);
    }

    @Test
    public void get_account_id_works_correctly() {
        int test = sut.getAccountId(bob.getId());
        Assert.assertEquals(ACCOUNT_1.getAccountId(),test);

        int test1 = sut.getAccountId(user.getId());
        Assert.assertEquals(ACCOUNT_2.getAccountId(), test1);
    }

    @Test
    public void get_account_works_correctly(){
        Account account = sut.getAccount(ACCOUNT_1.getUserId());
        assertAccountsMatch(ACCOUNT_1,account);

        Account account2 = sut.getAccount(ACCOUNT_2.getUserId());
        assertAccountsMatch(ACCOUNT_2,account2);
    }

    private void assertAccountsMatch(Account expected, Account actual){
        Assert.assertEquals(expected.getAccountId(), actual.getAccountId());
        Assert.assertEquals(expected.getUserId(), actual.getUserId());
        Assert.assertEquals(expected.getBalance(), actual.getBalance());
    }
}
