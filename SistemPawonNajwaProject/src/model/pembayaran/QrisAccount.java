/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.pembayaran;

/**
 *
 * @author USER
 */
public class QrisAccount implements FinancialAccount {
    private String merchantId;
    private String qrisCode;
    private double balance;
    private boolean active;

    public QrisAccount(String merchantId, String qrisCode, double balance, boolean active) {
        if (merchantId == null || merchantId.trim().isEmpty()) {
            this.merchantId = "";
        } else {
            this.merchantId = merchantId.trim();
        }

        if (qrisCode == null || qrisCode.trim().isEmpty()) {
            this.qrisCode = "";
        } else {
            this.qrisCode = qrisCode.trim();
        }

        this.balance = balance < 0 ? 0 : balance;
        this.active = active;
    }

    @Override
    public boolean charge(double amount) {
        if (active && !merchantId.isEmpty() && !qrisCode.isEmpty() && amount > 0 && balance >= amount) {
            this.balance -= amount;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean receive(double amount) {
        if (active && !merchantId.isEmpty() && !qrisCode.isEmpty() && amount > 0) {
            this.balance += amount;
            return true;
        } else {
            return false;
        }
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getQrisCode() {
        return qrisCode;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
