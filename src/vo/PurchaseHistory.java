package vo;

import java.sql.Timestamp;

public class PurchaseHistory {
	private Account account;
	private Timestamp date;
	private Product product;
	private Customer receiveId;
	private int quantity;
	private int status;

	public PurchaseHistory() {
		super();
	}

	public PurchaseHistory(Account account, Timestamp date, Product product, Customer receiveId, int quantity, int status) {
		super();
		this.account = account;
		this.date = date;
		this.product = product;
		this.receiveId = receiveId;
		this.quantity = quantity;
		this.status = status;
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Customer getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(Customer receiveId) {
		this.receiveId = receiveId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "account=" + account + ", date=" + date + ", product=" + product + ", receiveId=" + receiveId
				+ ", quantity=" + quantity + ", status=" + status;
	}

}
