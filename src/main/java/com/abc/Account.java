package com.abc;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Account {

	public enum AccountType {
		CHECKING, SAVINGS, MAXI_SAVINGS
	};

	private final AccountType accountType;
	public List<Transaction> transactions;
	private LocalDate acctOpenDate = null;
	private LocalDate withdrawalDate = null;

	public LocalDate getAcctOpenDate() {
		return acctOpenDate;
	}

	public LocalDate getWithdrawalDate() {
		return withdrawalDate;
	}

	public void setWithdrawalDate(LocalDate withdrawalDate) {
		this.withdrawalDate = withdrawalDate;
	}

	// minusDays(36) was added to show interest rate calculations
	// In production, acctOpenDate date will be set to LocalDate.now()
	public Account(AccountType accountType) {
		this.accountType = accountType;
		this.acctOpenDate = LocalDate.now().minusDays(36);
		this.transactions = new ArrayList<Transaction>();
	}

	public void deposit(double amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("amount must be greater than zero");
		} else {
			transactions.add(new Transaction(amount));
		}
	}

	// minusDays(12) was added to show interest rate calculations
	// In production, withdrawalDate will be set to LocalDate.now()
	public void withdraw(double amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("amount must be greater than zero");
		} else {
			transactions.add(new Transaction(-amount));
			setWithdrawalDate(LocalDate.now().minusDays(12));
		}
	}

	/*
	 * @param numOfDays - number of days for which account was held
	 * 
	 * @param daysSinceLastWithdrawal - days since last withdrawal for
	 * maxi-savings
	 */
	public double interestEarned() {
		long numOfDays = Duration.between(getAcctOpenDate().atTime(0, 0), LocalDate.now().atTime(0, 0)).toDays();
		if (getWithdrawalDate() == null)
			setWithdrawalDate(getAcctOpenDate());
		long daysSinceLastWithdrawal = Duration.between(getWithdrawalDate().atTime(0, 0), LocalDate.now().atTime(0, 0))
				.toDays();
        double amount = sumTransactions();
		switch (accountType) {
		case CHECKING:
			return amount * 0.001 * numOfDays / 365;
		case SAVINGS:
			if (amount > 1000)
				return (1000 * .001 + (amount - 1000) * .002) * numOfDays / 365;
			else
				return (amount * .001 * numOfDays / 365);
		case MAXI_SAVINGS:
			if (daysSinceLastWithdrawal > 10)
				return amount * 0.05 * numOfDays / 365;
			else
				return amount * 0.001 * numOfDays / 365;
		default:
			return amount * 0.001;
		}
    }

	public double transfer(Account to, double amount) {

		if (sumTransactions() >= amount) {
			this.withdraw(amount);
			to.deposit(amount);
		}
		return sumTransactions();
	}

	public double sumTransactions() {
		return checkIfTransactionsExist(true);
	}

	private double checkIfTransactionsExist(boolean checkAll) {
		double amount = 0.0;
		for (Transaction t : transactions)
			amount += t.amount;
		return amount;
	}

	public AccountType getAccountType() {
		return accountType;
	}

}
