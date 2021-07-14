package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")

public class TransferController {

    private TransferDao transferDao;
    private UserDao userDao;

    public TransferController(TransferDao transferDao, UserDao userDao){
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/transfers/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int id) {
        Transfer transfer = transferDao.getTransferById(id);
        return transfer;
    }

    @RequestMapping(path = "/transfers", method = RequestMethod.POST)
    public void sendTransfer(@Valid @RequestBody Transfer transfer){
        int accountFrom = transfer.getAccountFrom();
        int accountTo = transfer.getAccountTo();
        BigDecimal amount = transfer.getAmount();
        transferDao.sendTransfer(accountFrom, accountTo, amount);
    }

    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(Principal principal) {
        int userId = userDao.findIdByUsername(principal.getName());
        List<Transfer> transfersList = transferDao.getAllTransfers(userId);
        return transfersList;
    }

    @RequestMapping(path = "/transfers/status/{id}", method = RequestMethod.GET)
    public String getStatusDescFromStatusId(@PathVariable int id) {
        String statusDesc = transferDao.getStatusDescFromStatusId(id);
        return statusDesc;
    }

    @RequestMapping(path = "/transfers/type/{id}", method = RequestMethod.GET)
    public String getTypeDescFromTypeId(@PathVariable int id) {
        String typeDesc = transferDao.getTypeDescFromTypeId(id);
        return typeDesc;
    }

}