package vo;

import java.sql.Timestamp;

public class AccountHistory {
	private Account account;
	private Timestamp date;
	private int type;
	private int money;
	private int balanceChanged;
	
	public AccountHistory() {
		super();
		
	}
	
			
	public AccountHistory(Account account, Timestamp date, int type, int money, int balanceChanged) {
		super();
		this.account = account;
		this.date = date;
		this.type = type;
		this.money = money;
		this.balanceChanged = balanceChanged;
	}
	
	
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getBalanceChanged() {
		return balanceChanged;
	}
	public void setBalanceChanged(int balanceChanged) {
		this.balanceChanged = balanceChanged;
	}


	@Override
	public String toString() {
		return "account=" + account + ", date=" + date + ", type=" + type + ", money=" + money
				+ ", balanceChanged=" + balanceChanged;
	}
	
	
}
