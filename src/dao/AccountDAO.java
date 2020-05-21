package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import connection.SQLConnection;
import exception.AddException;
import exception.NotFoundException;
import vo.Account;
import vo.Customer;

public class AccountDAO {

	public Account selectById(String id) throws NotFoundException {
		CustomerDAO customerDAO = new CustomerDAO();
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			con = SQLConnection.getConnection();
			String sql = "SELECT * FROM account WHERE user_id=?";
			pstmt = con.prepareCall(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			Customer customer = customerDAO.selectById(id);
			rs.next();
			int balance = rs.getInt("balance");
			String accountPwd = rs.getString("account_pwd");

			return new Account(customer, balance, accountPwd);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NotFoundException("해당하는 아이디가 없습니다.");
		} finally {
			SQLConnection.close(con, pstmt, rs);
		}
	}

	public void update(Connection con, Account account) throws AddException {
		Statement stmt = null;
		try {
			Customer customer = account.getCustomer();
			String updateSQL1 = "UPDATE account SET ";
			String updateSQL2 = " WHERE user_id='" + customer.getId() + "'";
			boolean flag = false;
			if (account.getBalance() != 0) {
				updateSQL1 += "balance='" + account.getBalance() + "'";
				flag = true;
			}
			if (account.getAccountPwd() != null && !account.getAccountPwd().equals("")) {
				if (flag) {
					updateSQL1 += ",";
				}
				updateSQL1 += "account_pwd='" + account.getAccountPwd() + "'";
				flag = true;
			}
			if (flag) {
				stmt = con.createStatement();
				stmt.executeUpdate(updateSQL1 + updateSQL2);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AddException(e.getMessage());
		} finally {
			SQLConnection.close(null, stmt, null);
		}
	}
}
