package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private int transferId;
    private int fromUser;
    private int toUser;
    private BigDecimal teBucks;
    private String transferStatus;

     /*
transfer_id int NOT NULL DEFAULT nextval('seq_transfer_id'),
	from_user int NOT NULL,
	to_user int NOT NULL,
	TE_bucks int NOT NULL,
	transfer_status varchar DEFAULT 'APPROVED' NOT NULL,
 */

    public Transfer() { }

    public Transfer(int transferId, int fromUser, int toUser, BigDecimal teBucks, String transferStatus) {
        this.transferId = transferId;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.teBucks = teBucks;
        this.transferStatus = transferStatus;
    }

    public int getTransferId() {
        return transferId;
    }

    public int getFromUser() {
        return fromUser;
    }

    public int getToUser() {
        return toUser;
    }

    public BigDecimal getTeBucks() {
        return teBucks;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public void setFromUser(int fromUser) {
        this.fromUser = fromUser;
    }

    public void setToUser(int toUser) {
        this.toUser = toUser;
    }

    public void setTeBucks(BigDecimal teBucks) {
        this.teBucks = teBucks;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }



}
