/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.pembayaran;

/**
 *
 * @author USER
 */
public class EWalletAccount implements FinancialAccount {
    private String phoneNumber;
    private double balance;

    public EWalletAccount(String phoneNumber, double balance) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            this.phoneNumber = "";
        } else {
            this.phoneNumber = phoneNumber;
        }

        this.balance = balance < 0 ? 0 : balance;
    }

    @Override
    public boolean charge(double amount) {
        if (phoneNumber.length() == 10 && amount > 0 && balance >= amount) {
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public double getBalance() {
        return balance;
    }
}
