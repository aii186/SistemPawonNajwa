/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.pembayaran;

/**
 *
 * @author USER
 */
public interface FinancialAccount {
    boolean charge(double amount);
    boolean receive(double amount);
}
