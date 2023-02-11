package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BigDecimal getBalance(int id) {
        String sql = "SELECT balance FROM account WHERE user_id = ?;";
        BigDecimal balance = new BigDecimal(0);
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,id);
        while(results.next()) {
            balance = results.getBigDecimal("balance");
        }
        return balance;
    }

    public Account getAccount(int userId) {
        String sql = "SELECT * FROM account JOIN tenmo_user on account.user_id = tenmo_user.user_id WHERE tenmo_user.user_id = ? ";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        Account account = null;
        if(results.next()) {
            account = mapRowToAccount(results);
        }
        if(account == null){
            throw new NullPointerException("Account " + userId + " was not found.");
        }
        return account;
    }

    public int getAccountId (int userId) {
        String sql = "Select account_id from account WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        int account = 0;
        if(results.next()){
           account = results.getInt("account_id");
        }
        return account;
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));

        return account;
    }
}
