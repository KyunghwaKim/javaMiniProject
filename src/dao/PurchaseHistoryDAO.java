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
import vo.Customer;
import vo.PurchaseHistory;
import vo.Product;

public class PurchaseHistoryDAO {
	/**
	 * ��ǰ���ų����߰�, �������, �Ǹż���, �����ܾ��� �����Ѵ�
	 * 
	 * @param order ��ǰ���ų���
	 * @throws AddException
	 */
	public void insert(PurchaseHistory order) throws AddException {
		Connection con = null;
		try {
			con = SQLConnection.getConnection();
			con.setAutoCommit(false);

			Timestamp date = order.getDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSSSSS");
			String dt = sdf.format(date);

			insertOrder(con, order, dt); // PURCHASE_HISTORY���̺� �ֹ��߰�

			AccountHistoryDAO aDao = new AccountHistoryDAO();
			aDao.insertAccHis(con, order, dt); // ACCOUNT_HISTORY���̺� �ֹ��ݾ� ���
			// Ʈ����� �Ϸ�
			con.commit();
		} catch (Exception e) {
			if (con != null) {
				try {
					con.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new AddException(e1.getMessage());
				}
			}
			e.printStackTrace();
			throw new AddException(e.getMessage());
		} finally {
			SQLConnection.close(con, null, null);
		}
	}

	public void insert(Connection con, PurchaseHistory order) throws AddException {
		// ���̺� : PURCHASE_HISTORY, ACCOUNT_HISTORY
		// Ʈ����� ����, Connection�� autoCommit�� ����
		try {
			Timestamp date = order.getDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSSSSS");
			String dt = sdf.format(date);

			insertOrder(con, order, dt); // PURCHASE_HISTORY���̺� �ֹ��߰�

			AccountHistoryDAO aDao = new AccountHistoryDAO();
			aDao.insertAccHis(con, order, dt); // ACCOUNT_HISTORY���̺� �ֹ��ݾ� ���
		} catch (Exception e) {
			if (con != null) {
				try {
					con.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new AddException(e1.getMessage());
				}
			}
			e.printStackTrace();
			throw new AddException(e.getMessage());
		}
	}

	public void refundInsert(Connection con, PurchaseHistory order) throws AddException {
		// ���̺� : PURCHASE_HISTORY, ACCOUNT_HISTORY
		// Ʈ����� ����, Connection�� autoCommit�� ����
		try {
			Timestamp date = order.getDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSSSSS");
			String dt = sdf.format(date);

			insertOrder(con, order, dt); // PURCHASE_HISTORY���̺� �ֹ��߰�
		} catch (Exception e) {
			if (con != null) {
				try {
					con.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new AddException(e1.getMessage());
				}
			}
			e.printStackTrace();
			throw new AddException(e.getMessage());
		}
	}

	/**
	 * ��ǰ���ų����� �߰��ϰ� �Ǹż����� �÷��ش�
	 * 
	 * @param con
	 * @param order ��ǰ���ų���
	 * @param date  ���ų�¥
	 * @throws AddException
	 */
	private void insertOrder(Connection con, PurchaseHistory order, String date) throws AddException {
		PreparedStatement pstmt = null;
		try {
			String insertPurHisSQL = "INSERT INTO purchase_history(user_id, purchase_dt, pd_no, recipient, quantity, ph_status) VALUES(?, ?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(insertPurHisSQL);
			pstmt.setString(1, order.getAccount().getCustomer().getId());
			pstmt.setString(2, date);
			int productNo = order.getProduct().getNo();
			pstmt.setInt(3, productNo);
			pstmt.setString(4, order.getReceiveId().getId());

			int quantity = order.getQuantity();
			pstmt.setInt(5, quantity);

			int status = order.getStatus();
			pstmt.setInt(6, status);
			pstmt.executeUpdate();

			ProductDAO pDao = new ProductDAO();
			int sold = pDao.selectById(con, productNo);
			int updateSold = 0;

			if (status == 1)
				updateSold = sold + quantity;
			else if (status == 2)
				updateSold = sold - quantity;

			Product product = new Product();
			product.setSold(updateSold); // �Ǹż��� ������Ʈ
			product.setNo(productNo);
			pDao.update(product);

		} catch (Exception e) {
			e.printStackTrace();
			throw new AddException(e.getMessage());
		} finally {
			SQLConnection.close(null, pstmt, null);
		}
	}

	/**
	 * Customer�� id�� ���ų����� ã�´�
	 * 
	 * @param id customer�� id
	 * @return id�� �ش��ϴ� customer�� ���ų��� List
	 * @throws NotFoundException
	 */
	public List<PurchaseHistory> selectById(String id) throws NotFoundException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<PurchaseHistory> list = new ArrayList<>();
		PurchaseHistory order;

		try {
			con = SQLConnection.getConnection();
			String selectByIdSQL = "SELECT * FROM purchase_history WHERE user_id=? ORDER BY purchase_dt DESC";
			pstmt = con.prepareStatement(selectByIdSQL);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				order = new PurchaseHistory();
				order.setDate(rs.getTimestamp("purchase_dt"));

				Product product = new Product();
				product.setNo(rs.getInt("pd_no"));
				order.setProduct(product);

				Customer customer = new Customer();
				customer.setId(rs.getString("recipient"));
				order.setReceiveId(customer);
				order.setQuantity(rs.getInt("quantity"));

				order.setStatus(rs.getInt("ph_status"));

				list.add(order);
			}
			return list;
		} catch (Exception e) {
			throw new NotFoundException(e.getMessage());
		} finally {
			SQLConnection.close(con, pstmt, rs);
		}
	}

	/**
	 * �����ڰ� ��� Customer�� ���ų����� Ȯ���Ѵ�
	 * 
	 * @return ��� Customer�� ���ų���
	 * @throws NotFoundException
	 */
	public List<PurchaseHistory> selectAll() throws NotFoundException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<PurchaseHistory> list = new ArrayList<>();
		PurchaseHistory order;

		try {
			con = SQLConnection.getConnection();
			String selectByIdSQL = "SELECT * FROM purchase_history ORDER BY purchase_dt DESC";
			stmt = con.createStatement();
			rs = stmt.executeQuery(selectByIdSQL);
			while (rs.next()) {
				order = new PurchaseHistory();

				Account account = new Account();
				Customer customer = new Customer();
				customer.setId(rs.getString("user_id"));
				account.setCustomer(customer);
				order.setAccount(account);

				order.setDate(rs.getTimestamp("purchase_dt"));

				Product product = new Product();
				product.setNo(rs.getInt("pd_no"));
				order.setProduct(product);

				Customer receiveCustomer = new Customer();
				receiveCustomer.setId(rs.getString("recipient"));
				order.setReceiveId(receiveCustomer);
				order.setQuantity(rs.getInt("quantity"));
				order.setStatus(rs.getInt("ph_status"));

				list.add(order);
			}
			return list;
		} catch (Exception e) {
			throw new NotFoundException(e.getMessage());
		} finally {
			SQLConnection.close(con, stmt, rs);
		}
	}

	public List<Object[]> selectByReceiveId(String receiveId) throws NotFoundException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Object[]> list = new ArrayList<>();

		try {
			con = SQLConnection.getConnection();
			String selectByReceiveIdSQL = "SELECT name, pd_name, quantity, ph_status"
					+ " FROM purchase_history ph JOIN customer c ON ph.user_id = c.id"
					+ " JOIN product p ON ph.pd_no = p.pd_no" + " WHERE recipient=?" + " ORDER BY purchase_dt DESC";
			pstmt = con.prepareStatement(selectByReceiveIdSQL);
			pstmt.setString(1, receiveId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String customerName = rs.getString("name");
				String productName = rs.getString("pd_name");
				int quantity = rs.getInt("quantity");
				int ph_status = rs.getInt("ph_status");
				Object[] s = { customerName, productName, quantity, ph_status };

				list.add(s);
			}
			return list;
		} catch (Exception e) {
			throw new NotFoundException("�������� ������ �����ϴ�");
		} finally {
			SQLConnection.close(con, pstmt, rs);
		}
	}

	public PurchaseHistory selectByIdAndPdNoAndQuantity(PurchaseHistory order) throws NotFoundException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = SQLConnection.getConnection();
			String selectSQL = "SELECT * FROM purchase_history WHERE user_id=? AND pd_no=? AND quantity=? AND ph_status <> 3 ORDER BY purchase_dt DESC";
			pstmt = con.prepareStatement(selectSQL);
			pstmt.setString(1, order.getAccount().getCustomer().getId());
			pstmt.setInt(2, order.getProduct().getNo());
			pstmt.setInt(3, order.getQuantity());
			rs = pstmt.executeQuery();

			if (rs.next()) { // ���� �ֽ� ���ų���
				Customer customer = new Customer();
				customer.setId(rs.getString("recipient"));
				order.setDate(rs.getTimestamp("purchase_dt"));
				order.setReceiveId(customer);
			} else {
				throw new NotFoundException("�ش� ���ų����� �����ϴ�");
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new NotFoundException();
		} finally {
			SQLConnection.close(con, pstmt, rs);
		}
		return order;
	}

	public List<PurchaseHistory> selectByDate(String id, Timestamp startTime, Timestamp endTime) throws NotFoundException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<PurchaseHistory> list = new ArrayList<>();

		try {
			con = SQLConnection.getConnection();
			String sql = "Select * FROM purchase_history WHERE user_id=? AND purchase_dt BETWEEN ? and ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setTimestamp(2, startTime);
			pstmt.setTimestamp(3, endTime);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				PurchaseHistory order = new PurchaseHistory();

				Account account = new Account();
				Customer customer = new Customer();
				customer.setId(rs.getString("user_id"));
				account.setCustomer(customer);
				order.setAccount(account);

				order.setDate(rs.getTimestamp("purchase_dt"));

				Product product = new Product();
				product.setNo(rs.getInt("pd_no"));
				order.setProduct(product);

				Customer receiveCustomer = new Customer();
				receiveCustomer.setId(rs.getString("recipient"));
				order.setReceiveId(receiveCustomer);
				order.setQuantity(rs.getInt("quantity"));
				order.setStatus(rs.getInt("ph_status"));
				list.add(order);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new NotFoundException("����� ã�� �� �����ϴ�.");
		} finally {
			SQLConnection.close(con, pstmt, rs);
		}
	}

	public void refund(Connection con, PurchaseHistory order) throws ModifyException {
		PreparedStatement pstmt = null;
		String sql = "UPDATE purchase_history SET ph_status = 3 WHERE user_id=? AND pd_no=? AND quantity=? AND recipient=? AND ph_status <> 3 AND ph_status <> 2";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, order.getAccount().getCustomer().getId());
			pstmt.setInt(2, order.getProduct().getNo());
			pstmt.setInt(3, order.getQuantity());
			pstmt.setString(4, order.getReceiveId().getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ModifyException("ȯ�� �۾��� �����Ͽ����ϴ�.");
		} finally {
			SQLConnection.close(null, pstmt, null);
		}

	}

}
