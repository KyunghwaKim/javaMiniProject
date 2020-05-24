package service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import dao.AccountDAO;
import dao.CustomerDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import exception.AddException;
import exception.NotFoundException;
import vo.Account;
import vo.Customer;
import vo.Order;

public class OrderService {
	private static OrderService service = new OrderService();
	private OrderDAO dao;
	private CustomerDAO cDao;
	private AccountDAO aDao;
	private ProductDAO pDao;
	
	private OrderService() {
		dao = new OrderDAO();
		cDao = new CustomerDAO();
		aDao = new AccountDAO();
		pDao = new ProductDAO();
	}
	
	/**
	 * ±¸¸Å 
	 * @param pd_no
	 * @param receiveId
	 * @param quantity
	 * @param dt
	 * @param id_check
	 * @throws AddException
	 * @throws NotFoundException
	 */
	public void insert(int pd_no, String receiveId, int quantity, Timestamp dt, String id_check) throws AddException, NotFoundException{
			
		
		Order order = new Order();
		Account account = new Account();
		Customer customer = new Customer();
		
		
		System.out.println(id_check);
		
		account.setCustomer(cDao.selectById(id_check));
		account.setBalance(aDao.selectById(id_check).getBalance());
		account.setAccountPwd(aDao.selectById(id_check).getAccountPwd());
		//System.out.println(account);
		order.setAccount(account);
		order.setDate(dt);
		
		//System.out.println(productDao.selectByNo(1));
		order.setProduct(pDao.selectByNo(pd_no));
		order.setReceiveId(cDao.selectById(receiveId));
		order.setQuantity(quantity);
		
		order.setStatus(1);
		
		dao.insert(order);
			
	}
	
	public static OrderService getInstance(){
		return service;
	}

	public List<Object[]> findByReceiveId(String id) throws NotFoundException {
		return dao.selectByReceiveId(id);
	}
	
	public List<Order> findById(String id) throws NotFoundException {
		return dao.selectById(id);
	}

	public List<Order> findAll() throws NotFoundException {
		return dao.selectAll();
	}
	
	public Order findRefundProduct(Order order) throws NotFoundException {
		Order findOrder = dao.selectByIdAndPdNoAndQuantity(order);

		findOrder.setStatus(2);
		
		Date date = new Date();
		Timestamp dt = new Timestamp(date.getTime());
		findOrder.setDate(dt);
		
		Account account = aDao.selectById(order.getAccount().getCustomer().getId());
		findOrder.setAccount(account);
		
		int price = pDao.selectById(findOrder.getProduct().getNo());
		findOrder.getProduct().setPrice(price);
		return findOrder;
	}

	public void refund(Order order) throws AddException {
		dao.insert(order);
	}
}
