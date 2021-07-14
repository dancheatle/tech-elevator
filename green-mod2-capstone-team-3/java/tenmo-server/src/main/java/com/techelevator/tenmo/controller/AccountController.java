package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")

public class AccountController {

    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/account/{id}/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable int id) {
        BigDecimal balance = accountDao.getBalance(id);
        return balance;
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> listUsers() {
        List<User> users = userDao.findAll();
        return users;
    }

    @RequestMapping(path = "/account/{id}", method = RequestMethod.GET)
    public int getAccountIdByUserId(@PathVariable int id) {
        int accountId = accountDao.getAccountIdByUserId(id);
        return accountId;
    }

    @RequestMapping(path = "/account/{id}/username", method = RequestMethod.GET)
    public String getUsernameByAccountId(@PathVariable int id) {
        String username = accountDao.getUsernameByAccountId(id);
        return username;
    }
}
