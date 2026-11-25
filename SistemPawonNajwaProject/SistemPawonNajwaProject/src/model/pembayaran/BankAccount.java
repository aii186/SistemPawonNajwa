/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.pembayaran;

/**
 *
 * @author USER
 */
public class BankAccount implements FinancialAccount {
    private String accountNumber;
    private double balance;
    private String securityCode;

    public BankAccount(String accountNumber, double balance, String securityCode) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            this.accountNumber = "";
        } else {
            this.accountNumber = accountNumber;
        }

        this.balance = balance < 0 ? 0 : balance;

        if (securityCode == null || securityCode.trim().isEmpty()) {
            this.securityCode = "";
        } else {
            this.securityCode = securityCode;
        }
    }

    @Override
    public boolean charge(double amount) {
        if (securityCode.length() == 3 && amount > 0 && balance >= amount) {
            this.balance -= amount;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean receive(double amount) {
        if (amount > 0) {
            this.balance += amount;
            return true;
        } else {
            return false;
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public String getSecurityCode() {
        return securityCode;
    }
}
