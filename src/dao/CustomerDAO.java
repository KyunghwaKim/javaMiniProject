package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import connection.SQLConnection;
import exception.AddException;
import exception.ModifyException;
import exception.NotFoundException;
import vo.Customer;

public class CustomerDAO {
	public void insert(Customer c, String accountPwd) throws AddException {
		// 1)DB����
		Connection con = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtAccount = null;
		try {
			con = SQLConnection.getConnection();
			con.setAutoCommit(false);
			// 2)SQL�۽�
			String insertSQL = "INSERT INTO customer(id, pwd, name, age, gender, phone_no )" + " VALUES (?,?,?,?,?,?)";

			pstmt = con.prepareStatement(insertSQL);
			pstmt.setString(1, c.getId());
			pstmt.setString(2, c.getPwd());
			pstmt.setString(3, c.getName());
			pstmt.setInt(4, c.getAge());
			pstmt.setString(5, c.getGender());
			pstmt.setString(6, c.getPhoneNo());
			pstmt.executeUpdate();

			String accountInsertSQL = "INSERT INTO account(user_id, balance, account_pwd) VALUES (?, ?, ?) ";

			pstmtAccount = con.prepareStatement(accountInsertSQL);
			pstmtAccount.setString(1, c.getId());
			pstmtAccount.setInt(2, 0);
			pstmtAccount.setString(3, accountPwd);
			pstmtAccount.executeUpdate();

			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			if (e.getErrorCode() == 1) { // PK�ߺ�
				throw new AddException("�̹� �����ϴ� ID�Դϴ�.");
			}
			throw new AddException(e.getMessage());
		} catch (Exception e) {
			// e.printStackTrace();
			throw new AddException(e.getMessage());
		} finally {
			// 3)���н� �ѹ�
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AddException("���Կ� �����Ͽ����ϴ�.");
			}
			// )DB��������
			SQLConnection.close(con, pstmt, null);
		}
	}

	public List<Customer> selectAll() throws NotFoundException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Customer> list = new ArrayList<>();
		try {
			con = SQLConnection.getConnection();
			String selectAllSQL = "SELECT * FROM customer ORDER BY id";
			pstmt = con.prepareStatement(selectAllSQL);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String id = rs.getString("id");
				String pwd = rs.getString("pwd");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String gender = rs.getString("gender");
				String phoneNo = rs.getString("phone_no");
				java.sql.Date joinDt = rs.getDate("join_dt");
				int status = rs.getInt("status");
				list.add(new Customer(id, pwd, name, age, gender, phoneNo, joinDt, status));
			}
			if (list.size() == 0) {
				throw new NotFoundException("���� �����ϴ�");
			}
			return list;
		} catch (Exception e) {
			// e.printStackTrace();
			throw new NotFoundException("��ü�˻� ����:" + e.getMessage());
		} finally {
			SQLConnection.close(con, pstmt, rs);
		}

	}

	public Customer selectById(String id) throws NotFoundException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = SQLConnection.getConnection();
			// 2)SQL�۽�
			String selectByIdSQL = "SELECT * FROM customer WHERE id=?";
			pstmt = con.prepareStatement(selectByIdSQL);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				String pwd = rs.getString("pwd");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String gender = rs.getString("gender");
				String phoneNo = rs.getString("phone_No");
				java.sql.Date joinDt = rs.getDate("join_Dt");
				int status = rs.getInt("status");
				return new Customer(id, pwd, name, age, gender, phoneNo, joinDt, status);
			} else {
				throw new NotFoundException("ID�� �ش��ϴ� ���� �����ϴ�");
			}
		} catch (Exception e) {
			throw new NotFoundException("ID�� �˻� ����:" + e.getMessage());
		} finally {
			// 3)DB��������
			SQLConnection.close(con, pstmt, rs);
		}
	}

	public void update(Customer c) throws ModifyException {
		Connection con = null;
		try {
			con = SQLConnection.getConnection();
			update(con, c);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ModifyException(e.getMessage());
		}

	}

	public void update(Connection con, Customer c) throws ModifyException {
		System.out.println(c);
		Statement stmt = null;
		try {
			con = SQLConnection.getConnection();
			String updateSQL1 = "UPDATE customer SET ";
			String updateSQL2 = " WHERE id='" + c.getId() + "'";
			boolean flag = false;
			String test = null;
			System.out.println(test + "1");
			System.out.println(c.getPwd() + "1");
			if (c.getPwd() != null && !c.getPwd().equals("")) {
				updateSQL1 += "pwd='" + c.getPwd() + "'";
				flag = true;
			}
			if (c.getName() != null && !c.getName().equals("")) {
				if (flag) {
					updateSQL1 += ",";
				}
				updateSQL1 += "name='" + c.getName() + "'";
				flag = true;
			}
			if (c.getAge() != 0) {
				if (flag) {
					updateSQL1 += ",";
				}
				updateSQL1 += "age='" + c.getAge() + "'";
				flag = true;
			}

			if (c.getGender() != null && !c.getGender().equals("")) {
				if (flag) {
					updateSQL1 += ",";
				}
				updateSQL1 += "gender='" + c.getGender() + "'";
				flag = true;
			}
			if (c.getPhoneNo() != null && !c.getPhoneNo().equals("")) {
				if (flag) {
					updateSQL1 += ",";
				}
				updateSQL1 += "phone_No='" + c.getPhoneNo() + "'";
				flag = true;
			}

			if (c.getJoinDt() != null && !c.getJoinDt().equals("")) {
				if (flag) {
					updateSQL1 += ",";
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
				String dt = sdf.format(c.getJoinDt());// ��¥->���ڿ�
				updateSQL1 += "join_Dt='" + dt + "'";
				flag = true;
			}

			if (c.getStatus() != 0) {
				if (flag) {
					updateSQL1 += ",";
				}
				updateSQL1 += "status=" + c.getStatus();
				flag = true;
			}
			System.out.println(updateSQL1 + updateSQL2);
			if (flag) {
				stmt = con.createStatement();
				System.out.println(updateSQL1 + updateSQL2);
				stmt.executeUpdate(updateSQL1 + updateSQL2);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ModifyException(e.getMessage());
		} finally {
			SQLConnection.close(null, stmt, null);
		}

	}

	/**
	 * �̸��� �ش�ܾ ������ ������ �˻��Ѵ�
	 * 
	 * @param word �ܾ�
	 * @return
	 * @throws NotFoundException
	 */
	public List<Customer> selectByName(String word) throws NotFoundException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Customer> list = new ArrayList<>();
		try {
			con = SQLConnection.getConnection();
			String selectByNameSQL = "SELECT * FROM customer WHERE name LIKE ?";
			pstmt = con.prepareStatement(selectByNameSQL);
			pstmt.setString(1, "%" + word + "%");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(new Customer(rs.getString("id"), rs.getString("pwd"), rs.getString("name"), rs.getInt("age"),
						rs.getString("gender"), rs.getString("phone_no"), rs.getDate("join_dt"), rs.getInt("status")));
			}
			if (list.size() == 0) {
				throw new NotFoundException(word + "�� �ش��ϴ� �̸��� �����ϴ�");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			throw new NotFoundException("�̸����� �˻�����:" + e.getMessage());
		} finally {
			SQLConnection.close(con, pstmt, rs);
		}
		return list;
	}

}
