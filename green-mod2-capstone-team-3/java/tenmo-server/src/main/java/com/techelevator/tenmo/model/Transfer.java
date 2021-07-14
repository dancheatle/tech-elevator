package com.techelevator.tenmo.model;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

import java.math.BigDecimal;

public class Transfer {

    @Min(value = 0)
    private int transferId;

    @Min(value = 0)
    private int transferTypeId;

    @Min(value = 0)
    private int transferStatusId;

    @Min(value = 0)
    private int accountFrom;

    @Min(value = 0)
    private int accountTo;

    @DecimalMin(value = "0.00")
    private BigDecimal amount;

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
