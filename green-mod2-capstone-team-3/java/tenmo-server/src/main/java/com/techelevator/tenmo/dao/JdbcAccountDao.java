package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalance(int userId) {
        String sql = "SELECT balance FROM accounts WHERE user_id = ?;";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
        return balance;
    }

    @Override
    public BigDecimal getBalanceByAccountId(int accountId) {
        System.out.println("Account id: " + accountId);
        String sql = "SELECT balance FROM accounts WHERE account_id = ?;";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
        if (balance != null) {
            return balance;
        } else {
            return null;
        }
    }

    @Override

    public List<Account> listAllUsers() {
        List<Account> account = new ArrayList<>();
        String sql = "SELECT user_id, username FROM users;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            account.add(mapRowToAccount(results));
        }
        return account;
    }

    @Override
    public void subtractFromBalance(BigDecimal amount, int accountId) {
        String sql = "UPDATE accounts SET balance = balance - ?  WHERE account_id = ?;";
        try {
            jdbcTemplate.update(sql, amount, accountId);
        } catch (ResourceAccessException re) {
        }
    }

    @Override
    public void addToBalance(BigDecimal amount, int accountId) {
        String sql = "UPDATE accounts SET balance = balance + ?  WHERE account_id = ?;";
        try {
            jdbcTemplate.update(sql, amount, accountId);
        } catch (ResourceAccessException re) {
        }
    }

    @Override
    public int getAccountIdByUserId(int userId) {
        String sql = "SELECT account_id FROM accounts WHERE user_id = ?;";
        Integer accountId = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return accountId;
    }

    @Override
    public String getUsernameByAccountId(int accountId) {
        String username = "";
        String sql = "SELECT username FROM users " +
                "INNER JOIN accounts ON users.user_id = accounts.user_id " +
                "WHERE accounts.account_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);
        while (result.next()) {
            username = result.getString("username");
        }
        return username;
    }

    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }
}