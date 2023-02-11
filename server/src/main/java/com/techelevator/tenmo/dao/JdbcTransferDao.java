package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.security.TransferExceptions;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean depositToAccount (BigDecimal money, int userId) {
        String sql = "UPDATE account SET balance = balance + ? WHERE user_id = ?;";
        int count = jdbcTemplate.update(sql, money, userId);
        if (count <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deposit Failed");
        }
        return true;
    }

    @Override
    public boolean withdrawFromAccount (BigDecimal money, int userId) {
        String sql = "UPDATE account SET balance = balance - ? WHERE BALANCE >= ? AND user_id = ?;";
        int count = jdbcTemplate.update(sql, money, money, userId);
        if (count <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Withdraw Failed");
        }
        return true;
    }

    public Transfer createTransfer(int fromUser, int toUser, BigDecimal teBucks) {
        String sql = "INSERT INTO transfer (from_user, to_user, te_bucks) values(?,?,?) RETURNING transfer_id";
        Integer newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, fromUser, toUser, teBucks );
        Transfer transfer = getTransferById(newTransferId);

        return transfer;
    }

    // 6.
    public List<Transfer> getAllTransfers(int userId) {
        List<Transfer> list = new ArrayList<>();
        Transfer transferObj = new Transfer();
        String sql = "SELECT * FROM transfer WHERE from_user = ? OR to_user = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while(results.next()) {
            transferObj = mapRowToTransfer(results);
            list.add(transferObj);
        }
        return list;
    }

//    7.
    public Transfer getTransferById (int transferId, int accountId) {
        String sql = "SELECT * FROM transfer WHERE transfer_id = ? AND (to_user = ? OR from_user = ?);";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId, accountId, accountId);
        Transfer transfer = null;
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }

    public Transfer getTransferById (int transferId) {
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        Transfer transfer = null;
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }


    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setFromUser(rs.getInt("from_user"));
        transfer.setToUser(rs.getInt("to_user"));
        transfer.setTeBucks(rs.getBigDecimal("te_bucks"));
        transfer.setTransferStatus(rs.getString("transfer_status"));

        return transfer;
    }
}
