package service;

import java.sql.Connection;
import java.sql.Timestamp;

import connection.SQLConnection;
import dao.AccountDAO;
import dao.AccountHistoryDAO;
import exception.ModifyException;
import exception.NotFoundException;
import vo.Account;
import vo.AccountHistory;

public class AccountService {
	private static AccountService service = new AccountService();
	private AccountHistoryDAO aHDao;
	private AccountDAO dao;

	private AccountService() {
		dao = new AccountDAO();
		aHDao = new AccountHistoryDAO();
	}

	public static AccountService getInstance() {
		return service;
	}

	/**
	 * 아이디로 계좌 현재 잔액 조회
	 * 
	 * @param id
	 * @return int balance
	 * @throws NotFoundException
	 */
	public int checkBalanceById(String id) throws NotFoundException {
		Account account = null;
		account = dao.selectById(id);
		return account.getBalance();
	}

	/**
	 * 잔액 충전
	 * 
	 * @param id
	 * @param money
	 * @throws ModifyException
	 */
	public void deposit(String id, int money) throws ModifyException {
		Connection con = null;
		try {
			con = SQLConnection.getConnection();
			con.setAutoCommit(false);
			Account account = dao.selectById(id);
			Timestamp currTime = new Timestamp(System.currentTimeMillis());
			AccountHistory accountHistory = new AccountHistory(account, currTime, 1, money,
					account.getBalance() + money);
			account.setBalance(account.getBalance() + money);
			dao.update(con, account);
			aHDao.insert(accountHistory);
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ModifyException();
		} finally {
			SQLConnection.close(con, null, null);
		}

	}

}
