package com.abc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BankTest {
    private static final double DOUBLE_DELTA = 0.05;

    @Test
    public void customerSummary() {
        Bank bank = new Bank();
        Customer john = new Customer("John");
        john.openAccount(new Account(Account.AccountType.CHECKING));
        bank.addCustomer(john);

        assertEquals("Customer Summary\n - John (1 account)", bank.customerSummary());
    }

    @Test
    public void checkingAccount() {
        Bank bank = new Bank();
        Account checking = new Account(Account.AccountType.CHECKING);
        Customer bill = new Customer("Bill").openAccount(checking);
        bank.addCustomer(bill);

        checking.deposit(100.0);

        assertEquals(0.0, checking.interestEarned(), DOUBLE_DELTA);
    }

    @Test
    public void savings_account() {
        Bank bank = new Bank();
        Account savings = new Account(Account.AccountType.SAVINGS);
        bank.addCustomer(new Customer("Bill").openAccount(savings));

        savings.deposit(1500.0);

        assertEquals(0.19, savings.interestEarned(), DOUBLE_DELTA);
    }

    @Test
    public void maxi_savings_account() {
        Bank bank = new Bank();
        Account maxi_savings = new Account(Account.AccountType.MAXI_SAVINGS);
        bank.addCustomer(new Customer("Bill").openAccount(maxi_savings));

        maxi_savings.deposit(3000.0);
        maxi_savings.withdraw(100.0);

        assertEquals(14.30, maxi_savings.interestEarned(), DOUBLE_DELTA);
    }

}
