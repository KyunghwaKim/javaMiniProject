package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import connection.SQLConnection;
import exception.AddException;
import exception.ModifyException;
import exception.NotFoundException;
import vo.Account;
import vo.AccountHistory;
import vo.PurchaseHistory;

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
			throw new AddException("계좌 거래내역 등록에 실패하였습니다.");
		} finally {
			SQLConnection.close(con, pstmt, null);
		}

	}

	/**
	 * 물품구매금액을 계좌에서 출금,입금한다
	 * 
	 * @param con
	 * @param order 상품구매내역
	 * @param date  출금일자
	 * @throws AddException
	 */
	public void insertAccHis(Connection con, PurchaseHistory order, String date) throws AddException {
		PreparedStatement pstmt = null;
		try {
			int balance = order.getAccount().getBalance();
			int type = 1;
			int quantity = order.getQuantity();
			if (order.getStatus() == 1) { // 구매
				balance -= order.getProduct().getPrice() * quantity;
				type = 2;
			} else if (order.getStatus() == 2) { // 환불
				balance += order.getProduct().getPrice() * quantity;
			}
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
			aDao.update(con, account); // 계좌금액 업데이트
		} catch (Exception e) {
			e.printStackTrace();
			throw new AddException(e.getMessage());
		} finally {
			SQLConnection.close(null, pstmt, null);
		}
	}

	public void refundAccHis(Connection con, PurchaseHistory order, String date) throws AddException {
		PreparedStatement pstmt = null;
		try {
			int balance = order.getAccount().getBalance();
			int type = 4;
			int quantity = order.getQuantity();

			balance = accountDAO.selectById(order.getAccount().getCustomer().getId()).getBalance()
					+ order.getProduct().getPrice() * quantity;

			String insertAccHisSQL = "INSERT INTO account_history(user_id, acc_his_dt, type, money, acc_balance) VALUES(?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(insertAccHisSQL);

			String id = order.getAccount().getCustomer().getId();
			pstmt.setString(1, id);
			pstmt.setTimestamp(2, order.getDate());
			pstmt.setInt(3, type);
			pstmt.setInt(4, order.getProduct().getPrice() * quantity);

			pstmt.setInt(5, balance);
			pstmt.executeUpdate();

			AccountDAO aDao = new AccountDAO();
			Account account = order.getAccount();
			account.setBalance(balance);
			aDao.update(con, account); // 계좌금액 업데이트
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
			throw new NotFoundException("계좌 거래 목록을 찾을 수 없습니다.");
		}

	}

	/**
	 * 아이디로 계좌 내역 찾기
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
			throw new NotFoundException("계좌 거래 목록을 찾을 수 없습니다.");
		} finally {
			SQLConnection.close(con, pstmt, rs);
		}

	}

	public void refund(Connection con, PurchaseHistory order) throws ModifyException {
		PreparedStatement pstmt = null;
		String sql = "UPDATE account_history SET type = 3 WHERE user_id=? AND acc_his_dt=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, order.getAccount().getCustomer().getId());
			pstmt.setTimestamp(2, order.getDate());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ModifyException("환불 작업에 실패하였습니다.");
		} finally {
			SQLConnection.close(null, pstmt, null);
		}

	}

}
