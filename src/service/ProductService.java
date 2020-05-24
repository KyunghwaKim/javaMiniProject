package service;

import java.util.List;

import dao.ProductDAO;
import exception.AddException;
import exception.ModifyException;
import exception.NotFoundException;
import vo.Customer;
import vo.Product;

public class ProductService {
	private static ProductService productService = new ProductService();

	private ProductDAO productDAO;

	private ProductService() {
		productDAO = new ProductDAO();
	}

	public static ProductService getInstance() {
		return productService;
	}
	
	public List<Product> findByStatus(int status) throws NotFoundException {
		      return productDAO.selectByStatus(status);
		   }

	public List<Product> findByName(String pd_name) throws NotFoundException {
		return productDAO.selectByName(pd_name);
	}

	public List<Product> findBySold() throws NotFoundException {
		return productDAO.selectBySold();
	}

	public List<Product> findByStatus() throws NotFoundException {
		return productDAO.selectBySold();
	}

	public List<Product> findBySold_Cus() throws NotFoundException {
		return productDAO.selectBySold_Customer();
	}

	public void update(Product p) throws ModifyException {
		try {
			productDAO.update(p);
		} catch (AddException e) {
			e.printStackTrace();
			throw new ModifyException(e.getMessage());
		}
	}
	
	public void productAdd(Product ap) throws AddException{
		productDAO.insert(ap);
	}
	

}
