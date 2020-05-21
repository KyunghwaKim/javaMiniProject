package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import connection.SQLConnection;
import dao.AccountDAO;
import dao.AccountHistoryDAO;
import dao.CustomerDAO;
import dao.PurchaseHistoryDAO;
import dao.ProductDAO;
import exception.AddException;
import exception.NotFoundException;
import vo.Account;
import vo.PurchaseHistory;

public class PurchaseHistoryService {
	private static PurchaseHistoryService service = new PurchaseHistoryService();
	private PurchaseHistoryDAO dao;
	private CustomerDAO cDao;
	private AccountDAO aDao;
	private ProductDAO pDao;
	private AccountHistoryDAO ahDao;

	private PurchaseHistoryService() {
		dao = new PurchaseHistoryDAO();
		cDao = new CustomerDAO();
		aDao = new AccountDAO();
		pDao = new ProductDAO();
		ahDao = new AccountHistoryDAO();
	}

	/**
	 * 구매
	 * 
	 * @param pd_no
	 * @param receiveId
	 * @param quantity
	 * @param dt
	 * @param id_check
	 * @throws AddException
	 * @throws NotFoundException
	 */
	public void insert(int pd_no, String receiveId, int quantity, Timestamp dt, String id_check)
			throws AddException, NotFoundException {

		PurchaseHistory order = new PurchaseHistory();
		Account account = new Account();

		account.setCustomer(cDao.selectById(id_check));
		account.setBalance(aDao.selectById(id_check).getBalance());
		account.setAccountPwd(aDao.selectById(id_check).getAccountPwd());
		order.setAccount(account);
		order.setDate(dt);

		order.setProduct(pDao.selectByNo(pd_no));
		order.setReceiveId(cDao.selectById(receiveId));
		order.setQuantity(quantity);

		order.setStatus(1);

		dao.insert(order);

	}

	public static PurchaseHistoryService getInstance() {
		return service;
	}

	public List<Object[]> findByReceiveId(String id) throws NotFoundException {
		return dao.selectByReceiveId(id);
	}

	public List<PurchaseHistory> findById(String id) throws NotFoundException {
		return dao.selectById(id);
	}

	public List<PurchaseHistory> findAll() throws NotFoundException {
		return dao.selectAll();
	}

	public PurchaseHistory findRefundProduct(PurchaseHistory order) throws NotFoundException {
		PurchaseHistory findOrder = dao.selectByIdAndPdNoAndQuantity(order);

		findOrder.setStatus(2);

		Account account = aDao.selectById(order.getAccount().getCustomer().getId());
		findOrder.setAccount(account);

		int price = pDao.selectById(findOrder.getProduct().getNo());
		findOrder.getProduct().setPrice(price);
		return findOrder;
	}

	public void refund(PurchaseHistory order) throws AddException {
		Connection con = null;

		try {
			if (order.getStatus() == 3)
				throw new AddException("이미 환불되었습니다.");

			con = SQLConnection.getConnection();
			con.setAutoCommit(false);

			PurchaseHistory oldOrder = dao.selectByIdAndPdNoAndQuantity(order);
			dao.refund(con, oldOrder);
			ahDao.refund(con, oldOrder);

			order.setDate(new Timestamp(System.currentTimeMillis()));
			dao.refundInsert(con, order);
			ahDao.refundAccHis(con, order, new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSSSSS").format(order.getDate()));
			con.commit();
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new AddException("환불에 실패하였습니다.");
		} finally {
			SQLConnection.close(con, null, null);
		}

	}

	public List<PurchaseHistory> findByDate(String id, Timestamp startTime, Timestamp endTime) throws NotFoundException {
		List<PurchaseHistory> list = dao.selectByDate(id, startTime, endTime);
		return list;
	}
}
