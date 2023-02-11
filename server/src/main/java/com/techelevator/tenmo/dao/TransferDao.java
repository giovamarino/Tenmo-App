package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.security.TransferExceptions;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.List;

public interface TransferDao {

    boolean depositToAccount (BigDecimal money, int userId) throws TransferExceptions;

    boolean withdrawFromAccount (BigDecimal money, int userId) throws TransferExceptions;

    Transfer createTransfer(int fromUser, int toUser, BigDecimal teBucks) throws TransferExceptions;

    List<Transfer> getAllTransfers(int userId);

    Transfer getTransferById (int transferId, int accountId);

    Transfer getTransferById (int transferId);
}
