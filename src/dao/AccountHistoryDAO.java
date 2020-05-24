package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import connection.SQLConnection;
import exception.AddException;
import exception.NotFoundException;
import vo.Account;
import vo.AccountHistory;
import vo.Order;

public class AccountHistoryDAO {
	CustomerDAO customerDAO = new CustomerDAO();
	AccountDAO accountDAO = new AccountDAO();

	public void insert(AccountHistory accountHistory) throws AddException {
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = SQLConnection.getConnection();
			String sql = "INSERT INTO account_history(user_id, acc_his_dt, type, money, acc_balance) VALUES (?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, accountHistory.getAccount().getCustomer().getId());
			pstmt.setString(2, new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSSSSS").format(accountHistory.getDate()));
			pstmt.setInt(3, accountHistory.getType());
			pstmt.setInt(4, accountHistory.getMoney());
			pstmt.setInt(5, accountHistory.getBalanceChanged());
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AddException("���� �ŷ����� ��Ͽ� �����Ͽ����ϴ�.");
		} finally {
			SQLConnection.close(con, pstmt, null);
		}

	}

	/**
	 * ��ǰ���űݾ��� ���¿��� ���,�Ա��Ѵ�
	 * 
	 * @param con
	 * @param order ��ǰ���ų���
	 * @param date  �������
	 * @throws AddException
	 */
	public void insertAccHis(Connection con, Order order, String date) throws AddException {
		PreparedStatement pstmt = null;
		try {
			int balance = order.getAccount().getBalance();
			System.out.println(balance);
			int type = 1;
			int quantity = order.getQuantity();
			if (order.getStatus() == 1) { // ����
				balance -= order.getProduct().getPrice() * quantity;
				type = 2;
			} else if (order.getStatus() == 2) { // ȯ��
				balance += order.getProduct().getPrice() * quantity;
			}

			System.out.println(balance);
			String insertAccHisSQL = "INSERT INTO account_history(user_id, acc_his_dt, type, money, acc_balance) VALUES(?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(insertAccHisSQL);

			String id = order.getAccount().getCustomer().getId();
			pstmt.setString(1, id);
			pstmt.setString(2, date);
			pstmt.setInt(3, type);
			pstmt.setInt(4, order.getProduct().getPrice() * quantity);

			pstmt.setInt(5, balance);
			pstmt.executeUpdate();

			AccountDAO aDao = new AccountDAO();
			Account account = order.getAccount();
			account.setBalance(balance);
			aDao.update(con, account); // ���±ݾ� ������Ʈ
		} catch (Exception e) {
			e.printStackTrace();
			throw new AddException(e.getMessage());
		} finally {
			SQLConnection.close(null, pstmt, null);
		}
	}

	public List<AccountHistory> selectAll() throws NotFoundException {
		List<AccountHistory> list = new ArrayList<>();
		Connection con;
		try {
			con = SQLConnection.getConnection();
			String sql = "SELECT * FROM account_history";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String id = rs.getString("user_id");
				Account account = accountDAO.selectById(id);
				Timestamp date = rs.getTimestamp("acc_his_dt");
				int type = rs.getInt("type");
				int money = rs.getInt("money");
				int balance = rs.getInt("acc_balance");

				AccountHistory ah = new AccountHistory(account, date, type, money, balance);
				list.add(ah);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new NotFoundException("���� �ŷ� ����� ã�� �� �����ϴ�.");
		}

	}

	/**
	 * ���̵�� ���� ���� ã��
	 * 
	 * @param id
	 * @return List<AccountHistory>
	 * @throws NotFoundException
	 */
	public List<AccountHistory> selectById(String id) throws NotFoundException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			con = SQLConnection.getConnection();
			String sql = "SELECT * FROM account_history WHERE user_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			List<AccountHistory> list = new ArrayList<>();
			Account account = accountDAO.selectById(id);
			while (rs.next()) {
				Timestamp date = rs.getTimestamp("acc_his_dt");
				int type = rs.getInt("type");
				int money = rs.getInt("money");
				int balance = rs.getInt("acc_balance");

				AccountHistory ah = new AccountHistory(account, date, type, money, balance);

				list.add(ah);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new NotFoundException("���� �ŷ� ����� ã�� �� �����ϴ�.");
		} finally {
			SQLConnection.close(con, pstmt, rs);
		}

	}

}
