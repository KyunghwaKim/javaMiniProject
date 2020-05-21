package vo;

import java.util.Date;

public class Customer {
	private String id;
	private String pwd;
	private String name;
	private int age;
	private String gender;
	private String phoneNo;
	private Date joinDt;
	private int status;

	public Customer() {
	}

	public Customer(String id, String pwd, String name, int age, String gender, String phoneNo, Date joinDt,
			int status) {
		super();
		this.id = id;
		this.pwd = pwd;
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.phoneNo = phoneNo;
		this.joinDt = joinDt;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public Date getJoinDt() {
		return joinDt;
	}

	public void setJoinDt(Date joinDt) {
		this.joinDt = joinDt;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "id=" + id + ", pwd=" + pwd + ", name=" + name + ", age=" + age + ", gender=" + gender + ", phoneNo="
				+ phoneNo + ", joinDt=" + joinDt + ", status=" + status;
	}
}
