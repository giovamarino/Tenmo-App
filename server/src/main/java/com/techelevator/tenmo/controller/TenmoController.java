package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.security.TransferExceptions;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TenmoController {

    TransferDao transferDao;
    AccountDao accountDao;
    UserDao userDao;

    public TenmoController(AccountDao accountDao, UserDao userDao, TransferDao transferDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(path="/mybalance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal) {
        User sender = userDao.findByUsername(principal.getName());
        return accountDao.getBalance(sender.getId());
    }

    @RequestMapping(path="/users", method = RequestMethod.GET)
    public List<User> findAll() {
        return userDao.findAll();
    }

    @RequestMapping(path="/transfer", method=RequestMethod.POST)
    public Transfer transferMoney(Principal principal, @RequestBody Transfer transfer) {
        User sender = userDao.findByUsername(principal.getName());
        User receiver = userDao.findByUserId(transfer.getToUser());

        // doesn't allow sending money to yourself
        if (sender.getUsername().equals(receiver.getUsername())) {
            throw new IllegalArgumentException("Can't send money to yourself");
        } else if (transfer.getTeBucks().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ArithmeticException("Not enough money");
        }

        Account senderAccount = accountDao.getAccount(sender.getId());
        Account receiverAccount = accountDao.getAccount(receiver.getId());

        transferDao.withdrawFromAccount(transfer.getTeBucks(), sender.getId());
        transferDao.depositToAccount(transfer.getTeBucks(), receiver.getId());
        return transferDao.createTransfer(senderAccount.getAccountId(), receiverAccount.getAccountId(), transfer.getTeBucks());
    }

    // see transfers I have sent or received
    @RequestMapping(path="/mytransfers", method = RequestMethod.GET)
    public List<Transfer> myTransfers (Principal principal) {
        User sender = userDao.findByUsername(principal.getName());
        return transferDao.getAllTransfers(accountDao.getAccountId(sender.getId()));
    }

    /*
    7. include path with id
    As an authenticated user of the system, I need to be able to retrieve the details of any transfer based upon the transfer ID.
     */
    @RequestMapping(path="/transfer/{id}", method=RequestMethod.GET)
    public Transfer viewTransferById (@PathVariable int id, Principal principal) {
        User sender = userDao.findByUsername(principal.getName());
        int senderAccountId = accountDao.getAccountId(sender.getId());
        Transfer transfer = transferDao.getTransferById(id, senderAccountId);

        if(transfer == null){
            throw new TransferExceptions("Transfer Not Found");
        }
        return transfer;
    }
}
