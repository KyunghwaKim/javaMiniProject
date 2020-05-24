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
import vo.Customer;
import vo.Order;
import vo.Product;

public class OrderDAO {
	/**
	 * 상품구매내역추가, 계좌출금, 판매수량, 계좌잔액을 조정한다
	 * @param order	상품구매내역
	 * @throws AddException
	 */
	public void insert(Order order) throws AddException {
		Connection con = null;
		// 테이블 : PURCHASE_HISTORY, ACCOUNT_HISTORY
		// 트랜잭션 시작, Connection의 autoCommit을 해제
		try {
			con = SQLConnection.getConnection();
			con.setAutoCommit(false);
			
			Timestamp date = order.getDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSSSSS");
			String dt = sdf.format(date);
			
			insertOrder(con, order, dt); // PURCHASE_HISTORY테이블 주문추가
			
			AccountHistoryDAO aDao = new AccountHistoryDAO();
			aDao.insertAccHis(con, order, dt); // ACCOUNT_HISTORY테이블 주문금액 출금
			// 트랜잭션 완료
			con.commit();
		} catch (Exception e) {
			if(con != null) {
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

	/**
	 * 상품구매내역을 추가하고 판매수량을 올려준다
	 * @param con
	 * @param order 상품구매내역
	 * @param date 구매날짜
	 * @throws AddException
	 */
	private void insertOrder(Connection con, Order order, String date) throws AddException {
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
			
			if(status == 1)
				updateSold = sold + quantity;
			else if(status == 2)
				updateSold = sold - quantity;
			
			Product product = new Product();
			product.setSold(updateSold);	//판매수량 업데이트
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
	 * Customer의 id로 구매내역을 찾는다
	 * @param id customer의 id
	 * @return id에 해당하는 customer의 구매내역 List
	 * @throws NotFoundException
	 */
	public List<Order> selectById(String id) throws NotFoundException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Order> list = new ArrayList<>();
		Order order;
		
		try {
			con = SQLConnection.getConnection();
			String selectByIdSQL = "SELECT * FROM purchase_history WHERE user_id=? ORDER BY purchase_dt DESC";
			pstmt = con.prepareStatement(selectByIdSQL);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				order = new Order();
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
	 * 관리자가 모든 Customer의 구매내역을 확인한다
	 * @return 모든 Customer의 구매내역
	 * @throws NotFoundException
	 */
	public List<Order> selectAll() throws NotFoundException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<Order> list = new ArrayList<>();
		Order order;
		
		try {
			con = SQLConnection.getConnection();
			String selectByIdSQL = "SELECT * FROM purchase_history ORDER BY purchase_dt DESC";
			stmt = con.createStatement();
			rs = stmt.executeQuery(selectByIdSQL);
			while(rs.next()) {
				order = new Order();
				
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
				
				list.add(order);
			}
			return list;
		}catch (Exception e) {
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
					+ " JOIN product p ON ph.pd_no = p.pd_no"
					+ " WHERE recipient=?"
					+ " ORDER BY purchase_dt DESC";
			pstmt = con.prepareStatement(selectByReceiveIdSQL);
			pstmt.setString(1, receiveId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String customerName = rs.getString("name");
				String productName = rs.getString("pd_name");
				int quantity = rs.getInt("quantity");
				int ph_status = rs.getInt("ph_status");
				Object [] s = {customerName, productName, quantity, ph_status};
				
				list.add(s);
			}
			return list;
		} catch (Exception e) {
			throw new NotFoundException("선물받은 내역이 없습니다");
		} finally {
			SQLConnection.close(con, pstmt, rs);
		}
	}
	
	public Order selectByIdAndPdNoAndQuantity(Order order) throws NotFoundException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = SQLConnection.getConnection();
			String selectSQL = "SELECT * FROM purchase_history WHERE user_id=? AND pd_no=? AND quantity=? ORDER BY purchase_dt DESC";
			pstmt = con.prepareStatement(selectSQL);
			pstmt.setString(1, order.getAccount().getCustomer().getId());
			pstmt.setInt(2, order.getProduct().getNo());
			pstmt.setInt(3, order.getQuantity());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {	//제일 최신 구매내역
				Customer customer = new Customer();
				customer.setId(rs.getString("recipient"));
				order.setReceiveId(customer);
			} else {
				throw new NotFoundException("해당 구매내역이 없습니다");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new NotFoundException();
		} finally {
			SQLConnection.close(con, pstmt, rs);
		}
		return order;
	}
	
	public static void main(String[] args) {
//
//		try {
//			Customer c = new Customer("id2", "p2", "김경화", 16, "F", "010-2345-5679",
//					new SimpleDateFormat("yy/MM/dd").parse("19/01/20"), 1);
//			Customer reC = new Customer("id5", "p5", "김연주", 19, "F", "010-5678-5681",
//					new SimpleDateFormat("yy/MM/dd").parse("15/12/20"), 1);
//
//			Account a = new Account(c, 5050, "000002");
//			Product p = new Product(1, 4100, "스타벅스 아메리카노", 6, 1);
//
//			Date date = new Date();
//			Timestamp dt = new Timestamp(date.getTime());
//			
//			Order o = new Order(a, dt, p, reC, 1);
//
//			insert(o);
//			System.out.println("인서트성공");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		try {
//			List<String []> list = selectByReceiveId("id6");
//			for(int i=0; i<list.size(); i++) {
//				String customerName = list.get(i)[0];
//				String productName = list.get(i)[1];
//				System.out.println("보낸사람:" + customerName + ", 받은선물:" + productName);
//			}
//		} catch (NotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
