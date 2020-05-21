package vo;

public class Account {
	private Customer customer;
	private int balance;
	private String accountPwd;

	public Account() {
	}

	public Account(Customer customer, int balance, String accountPwd) {
		super();
		this.customer = customer;
		this.balance = balance;
		this.accountPwd = accountPwd;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public String getAccountPwd() {
		return accountPwd;
	}

	public void setAccountPwd(String accountPwd) {
		this.accountPwd = accountPwd;
	}

	@Override
	public String toString() {
		return "customer=" + customer + ", balance=" + balance + ", accountPwd=" + accountPwd;
	}

}
