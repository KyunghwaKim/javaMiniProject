package service;

import java.sql.Connection;
import java.util.List;

import connection.SQLConnection;
import dao.AccountDAO;
import dao.CustomerDAO;
import exception.AddException;
import exception.ModifyException;
import exception.NotFoundException;
import session.Session;
import session.SessionSet;
import vo.Account;
import vo.Customer;

public class CustomerService {
	private static CustomerService service = new CustomerService();
	private CustomerDAO dao;
	private AccountDAO aDao;

	private CustomerService() {
		dao = new CustomerDAO();
		aDao = new AccountDAO();
	}

	public static CustomerService getInstance() {
		return service;
	}

	/**
	 * 아이디,비번에 해당고객이 존재하면 로그인 성공, 없으면 로그인 실패
	 * 
	 * @param id
	 * @param pwd
	 * @throws NotFoundException
	 */
	public void login(String id, String pwd) throws NotFoundException {
		Customer c = dao.selectById(id);

		if (!c.getPwd().equals(pwd)) {
			throw new NotFoundException("로그인실패");
		} else {
			SessionSet ss = SessionSet.getInstance();
			Session session = new Session(id);
			String status = Integer.toString(c.getStatus());
			session.setAttribute("status", status);
			ss.add(session);
		}
	}

	public Customer findById(String id) throws NotFoundException {
		return dao.selectById(id);
	}

	public List<Customer> findAll() throws NotFoundException {
		return dao.selectAll();
	}

	public void register(Customer c, String AccountPwd) throws AddException {
		dao.insert(c, AccountPwd);
	}

	public void modify(Customer c, String accountPwd) throws ModifyException {
		Connection con;

		try {
			con = SQLConnection.getConnection();
			con.setAutoCommit(false);
			Account account = aDao.selectById(c.getId());
			account.setAccountPwd(accountPwd);
			dao.update(con, c);
			aDao.update(con, account);
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ModifyException(e.getMessage());
		}

	}

	public void withdraw(String id) throws ModifyException {
		Customer c = new Customer();
		c.setId(id);
		c.setStatus(3); // 탈퇴용 status값: 3
		dao.update(c);
	}

	public void CancelWithdraw(String id) throws ModifyException {
		Customer c = new Customer();
		c.setId(id);
		c.setStatus(1); // 탈퇴취소 status값: 1
		dao.update(c);
	}

	public List<Customer> findByName(String word) throws NotFoundException {
		return dao.selectByName(word);
	}

}
