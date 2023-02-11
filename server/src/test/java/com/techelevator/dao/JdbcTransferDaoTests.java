package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

public class JdbcTransferDaoTests extends BaseDaoTests {

    private static final Transfer TRANSFER_1 = new Transfer(3001, 2001, 2002, new BigDecimal("50.00"), "APPROVED");
    private static final Transfer TRANSFER_2 = new Transfer(3002, 2002, 2001, new BigDecimal("150.00"), "APPROVED");
    private static final Transfer TRANSFER_3 = new Transfer(3003, 2001, 2002, new BigDecimal("250.00"), "APPROVED");
    private static final Transfer TRANSFER_4 = new Transfer(3004, 2001, 2002, new BigDecimal("50.00"), "APPROVED");

    private JdbcTransferDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void deposit_to_account_works_correctly () {
        Boolean test = sut.depositToAccount(new BigDecimal("250"), 1002);
        Assert.assertTrue(test);
  }

    @Test
    public void withdraw_from_account_works_correctly () {
        boolean test = sut.withdrawFromAccount( new BigDecimal("250"), 1002);
        Assert.assertTrue(test);
    }

    @Test
    public void create_transfer_works_correctly () {
        Transfer test = sut.createTransfer(2001, 2002, new BigDecimal("50.00"));
        assertTransfersMatch(TRANSFER_4, test);
    }

    @Test
    public void get_all_transfers_works_correctly() {
        List<Transfer> test = sut.getAllTransfers(2001);
        Assert.assertEquals(3, test.size());
        assertTransfersMatch(TRANSFER_1, test.get(0));
        assertTransfersMatch(TRANSFER_2, test.get(1));
        assertTransfersMatch(TRANSFER_3, test.get(2));
    }

    @Test
    public void get_transfer_by_id_works_correctly() {
        Transfer test = sut.getTransferById(3001);
        assertTransfersMatch(TRANSFER_1, test);

        Transfer test1 = sut.getTransferById(3001, 2001);
        assertTransfersMatch(TRANSFER_1, test1);
    }

    private void assertTransfersMatch(Transfer expected, Transfer actual) {
        Assert.assertEquals(expected.getTransferId(), actual.getTransferId());
        Assert.assertEquals(expected.getFromUser(), actual.getFromUser());
        Assert.assertEquals(expected.getToUser(), actual.getToUser());
        Assert.assertEquals(expected.getTeBucks(), actual.getTeBucks());
        Assert.assertEquals(expected.getTransferStatus(), actual.getTransferStatus());
    }
}
