package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component

public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, AccountDao accountDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountDao = accountDao;
    }

@Override
   public Transfer sendTransfer(int accountFrom, int accountTo, BigDecimal amount) {
    if (accountTo == accountFrom) {
        System.out.println("You can't send money to yourself!");
    }
    if (amount.compareTo(accountDao.getBalanceByAccountId(accountFrom)) <= 0) {
        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount)" +
                " Values(2, 2, ?, ?, ?);";
        jdbcTemplate.update(sql, accountFrom, accountTo, amount);
        accountDao.addToBalance(amount, accountTo);
        accountDao.subtractFromBalance(amount, accountFrom);
    }
    return null;
}


    @Override
    public List<Transfer> getAllTransfers(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfers.transfer_id, transfers.transfer_type_id, transfers.transfer_status_id, transfers.account_from, " +
                "transfers.account_to, transfers.amount FROM transfers " +
                "INNER JOIN accounts ON transfers.account_to = accounts.account_id OR transfers.account_from = accounts.account_id " +
                "INNER JOIN users ON accounts.user_id = users.user_id " +
                "WHERE (transfers.account_from = accounts.account_id OR transfers.account_to = accounts.account_id) " +
                "AND accounts.user_id = users.user_id " +
                "AND users.user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    @Override
    public int findAccountIdByAccountFrom(int accountFrom) {
        int accountId = 0;
        String sql = "SELECT account_id FROM accounts " +
                "INNER JOIN transfers ON accounts.account_id = transfers.account_from " +
                "WHERE transfers.account_from = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountFrom);
        while (result.next()) {
            accountId = result.getInt("account_id");
        }
        return accountId;
    }

    @Override
    public int findAccountIdByAccountTo(int accountTo) {
        int accountId = 0;
        String sql = "SELECT account_id FROM accounts " +
                "INNER JOIN transfers ON accounts.account_id = transfers.account_to " +
                "WHERE transfers.account_to = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountTo);
        while (result.next()) {
            accountId = result.getInt("account_id");
        }
        return accountId;
    }

    @Override
    public Transfer getTransferById(int transferId) {
        Transfer transfer = new Transfer();
        String sql = "SELECT * FROM transfers WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        while (results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }

    @Override
    public String getStatusDescFromStatusId(int transferStatusId) {
        String sql = "SELECT transfer_status_desc FROM transfer_statuses WHERE transfer_status_id = ?;";
        String statusTransferDesc = jdbcTemplate.queryForObject(sql, String.class, transferStatusId);
        return statusTransferDesc;
    }

    @Override
    public String getTypeDescFromTypeId(int transferTypeId) {
        String sql = "SELECT transfer_type_desc FROM transfer_types WHERE transfer_type_id = ?;";
        String typeTransferDesc = jdbcTemplate.queryForObject(sql, String.class, transferTypeId);
        return typeTransferDesc;
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setAccountFrom(rowSet.getInt("account_from"));
        transfer.setAccountTo(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        return transfer;
    }
}
