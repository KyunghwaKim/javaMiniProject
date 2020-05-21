package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connection.SQLConnection;
import exception.AddException;
import exception.NotFoundException;
import vo.Product;

public class ProductDAO {
	public List<Product> selectByStatus(int status) throws NotFoundException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Product> list = new ArrayList<Product>();
		String selectByPriceSQL = "SELECT * FROM product WHERE pd_status = ? ORDER BY sold DESC";
		try {
			con = SQLConnection.getConnection();

			pstmt = con.prepareStatement(selectByPriceSQL);
			pstmt.setInt(1, status);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				list.add(new Product(rs.getInt("pd_no"), rs.getInt("pd_price"), rs.getString("pd_name"),
						rs.getInt("sold"), rs.getInt("pd_status"))

				);

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new NotFoundException("목록을 불러오는데 실패하였습니다.");
		} finally {
			SQLConnection.close(con, pstmt, rs);
		}
		return list;

	}

	/**
	 * 상품명에 해당하는 상품정보 출력
	 * 
	 * @param pd_no
	 * @return
	 * @throws NotFoundException
	 */
	public Product selectByNo(int pd_no) throws NotFoundException {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Product product = null;

		try {

			String selectByNoSQL = "SELECT * FROM product WHERE pd_no = ?";
			con = SQLConnection.getConnection();
			pstmt = con.prepareStatement(selectByNoSQL);
			pstmt.setInt(1, pd_no);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				product = new Product(rs.getInt("pd_no"), rs.getInt("pd_price"), rs.getString("pd_name"),
						rs.getInt("sold"), rs.getInt("pd_status"));

			}
			return product;
		} catch (Exception e) {

			e.printStackTrace();
			throw new NotFoundException("상품을 찾을 수 없습니다.");
		} finally {
			SQLConnection.close(con, pstmt, rs);
		}

	}

	public void insert(Product p) throws AddException {
		Connection con = null;
		PreparedStatement pstmt = null;
		String insertSQL = "INSERT INTO product(pd_no, pd_price, pd_name, sold, pd_status) VALUES (?, ?, ?, ?, ? )";

		try {
			con = SQLConnection.getConnection();
			pstmt = con.prepareStatement(insertSQL);

			pstmt.setInt(1, p.getNo());
			pstmt.setInt(2, p.getPrice());
			pstmt.setString(3, p.getName());
			pstmt.setInt(4, p.getSold());
			pstmt.setInt(5, p.getStatus());
			pstmt.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
			throw new AddException(e.getMessage());
		} finally {
			SQLConnection.close(con, pstmt, null);
		}

	}

	public void update(Product p) throws AddException {
		Connection con = null;
		try {
			con = SQLConnection.getConnection();
			update(con, p);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AddException(e.getMessage());
		} finally {
			SQLConnection.close(con, null, null);
		}
	}

	public void update(Connection con, Product p) throws AddException {
		Statement stmt = null;
		try {
			String updateSQL1 = "UPDATE product SET ";
			String updateSQL2 = " WHERE pd_no ='" + p.getNo() + "'";

			boolean flag = false;

			if (p.getPrice() != 0) {
				updateSQL1 += "pd_Price='" + p.getPrice() + "'";
				flag = true;
			}

			if (p.getName() != null) {
				if (flag) {
					updateSQL1 += ",";
				}
				updateSQL1 += "pd_Name='" + p.getName() + "'";
				flag = true;
			}

			if (p.getSold() != 0) {
				if (flag) {
					updateSQL1 += ",";
				}
				updateSQL1 += "Sold='" + p.getSold() + "'";
				flag = true;
			}

			if (p.getStatus() != 0) {
				if (flag) {
					updateSQL1 += ",";
				}
				updateSQL1 += "pd_status='" + p.getStatus() + "'";
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

	/**
	 * 물품수량을 알아낸다
	 * 
	 * @param con
	 * @param no  물품번호
	 * @return 물품의 수량
	 * @throws AddException
	 */
	public int selectById(Connection con, int no) throws AddException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String selectByIdSQL = "SELECT sold FROM product WHERE pd_no=?";
			pstmt = con.prepareStatement(selectByIdSQL);
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt("sold");
			} else {
				throw new AddException();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AddException(e.getMessage());
		} finally {
			SQLConnection.close(null, pstmt, rs);
		}
	}

	public int selectById(int no) throws NotFoundException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = SQLConnection.getConnection();
			String selectById = "SELECT pd_price FROM product WHERE pd_no=?";
			pstmt = con.prepareStatement(selectById);
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt("pd_price");
			} else {
				throw new NotFoundException("해당 상품을 찾지 못했습니다");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new NotFoundException();
		} finally {
			SQLConnection.close(con, pstmt, rs);
		}
	}

	public List<Product> selectByPriceDesc() {

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<Product> list = new ArrayList<Product>();

		String selectByPriceSQL = "SELECT * FROM product ORDER BY pd_price DESC";

		try {
			con = SQLConnection.getConnection();

			stmt = con.createStatement();
			rs = stmt.executeQuery(selectByPriceSQL);

			while (rs.next()) {
				list.add(new Product(rs.getInt("pd_no"), rs.getInt("pd_price"), rs.getString("pd_name"),
						rs.getInt("sold"), rs.getInt("pd_status"))

				);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			SQLConnection.close(con, stmt, rs);
		}
		return list;

	}

	public List<Product> selectByPriceAsc() {

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<Product> list = new ArrayList<Product>();

		String selectByPriceSQL = "SELECT * FROM product ORDER BY pd_price ASC";

		try {
			con = SQLConnection.getConnection();

			stmt = con.createStatement();
			rs = stmt.executeQuery(selectByPriceSQL);

			while (rs.next()) {
				list.add(new Product(rs.getInt("pd_no"), rs.getInt("pd_price"), rs.getString("pd_name"),
						rs.getInt("sold"), rs.getInt("pd_status"))

				);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			SQLConnection.close(con, stmt, rs);
		}
		return list;

	}

	public List<Product> selectBySold() {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<Product> list = new ArrayList<Product>();
		String selectByPriceSQL = "SELECT * FROM product ORDER BY sold DESC";
		try {
			con = SQLConnection.getConnection();

			stmt = con.createStatement();
			rs = stmt.executeQuery(selectByPriceSQL);

			while (rs.next()) {
				list.add(new Product(rs.getInt("pd_no"), rs.getInt("pd_price"), rs.getString("pd_name"),
						rs.getInt("sold"), rs.getInt("pd_status"))

				);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			SQLConnection.close(con, stmt, rs);
		}
		return list;

	}

	public List<Product> selectByName(String pd_name) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Product> list = new ArrayList<Product>();
		String selectByNameSQL = "SELECT * FROM product WHERE pd_name like ? ";
		try {
			con = SQLConnection.getConnection();

			pstmt = con.prepareStatement(selectByNameSQL);
			pstmt.setString(1, "%" + pd_name + "%");
			rs = pstmt.executeQuery();

			while (rs.next()) {
				list.add(new Product(rs.getInt("pd_no"), rs.getInt("pd_price"), rs.getString("pd_name"),
						rs.getInt("sold"), rs.getInt("pd_status"))

				);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			SQLConnection.close(con, pstmt, rs);
		}
		return list;

	}

	public List<Product> selectBySold_Customer() {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<Product> list = new ArrayList<Product>();
		String selectByPriceSQL = "SELECT * FROM product WHERE pd_status = 1 ORDER BY sold DESC";
		try {
			con = SQLConnection.getConnection();

			stmt = con.createStatement();
			rs = stmt.executeQuery(selectByPriceSQL);

			while (rs.next()) {
				list.add(new Product(rs.getInt("pd_no"), rs.getInt("pd_price"), rs.getString("pd_name"),
						rs.getInt("sold"), rs.getInt("pd_status"))

				);

			}
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			SQLConnection.close(con, stmt, rs);
		}
		return list;

	}

}
