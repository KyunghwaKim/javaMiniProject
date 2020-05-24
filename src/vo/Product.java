package vo;

public class Product {
	private int no;
	private int price;
	private String name;
	private int sold;
	private int status;
	
	public Product() {}

	public Product(int no, int price, String name, int sold, int status) {
		super();
		this.no = no;
		this.price = price;
		this.name = name;
		this.sold = sold;
		this.status = status;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSold() {
		return sold;
	}

	public void setSold(int sold) {
		this.sold = sold;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "no=" + no + ", price=" + price + ", name=" + name + ", sold=" + sold + ", status=" + status;
	}
	
}
