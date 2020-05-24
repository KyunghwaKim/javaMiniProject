package controller;

import java.util.List;

import exception.AddException;
import exception.ModifyException;
import exception.NotFoundException;
import service.ProductService;
import view.FailView;
import view.SuccessView;
import vo.Customer;
import vo.Product;

public class ProductController {
	private ProductService productService = ProductService.getInstance();
	private static ProductController productController = new ProductController();

	private ProductController() {
	}

	public static ProductController getInstance() {

		return productController;
	}
	
	   public void findByStatus(int status){
		      
	         try {
	            List<Product> list = productService.findByStatus(status);
	            SuccessView.printByStatus(list);
	         } catch (NotFoundException e) {
	            
	            e.printStackTrace();
	            FailView.printErrorMessage(e.getMessage());
	         }
	      
	   }

	public void findByName(String pd_name) {

		try {
			List<Product> list = productService.findByName(pd_name);
			SuccessView.printByName(list);
		} catch (NotFoundException e) {

			e.printStackTrace();
			FailView.errorMessage(e.getMessage());
		}

	}

	public void findBySold() {

		try {
			List<Product> list = productService.findBySold();
			SuccessView.printBySold(list);

		} catch (NotFoundException e) {

			e.printStackTrace();
			FailView.errorMessage(e.getMessage());
		}

	}

	public void findBySold_Cus() {

		try {
			List<Product> list = productService.findBySold_Cus();
			SuccessView.printBySold(list);
		} catch (NotFoundException e) {

			e.printStackTrace();
			FailView.errorMessage(e.getMessage());
		}
	}

	public void findBySold_Cus_Asc() {

		try {
			List<Product> list = productService.findBySold_Cus();
			SuccessView.printBySoldAsc(list);
		} catch (NotFoundException e) {

			e.printStackTrace();
			FailView.errorMessage(e.getMessage());
		}
	}

	public void findBySold_Cus_Desc() {
		try {
			List<Product> list = productService.findBySold_Cus();
			SuccessView.printBySoldDesc(list);
		} catch (NotFoundException e) {

			e.printStackTrace();
			FailView.errorMessage(e.getMessage());
		}

	}
	
	public void productUpdate(Product p) {
		try {
			productService.update(p);
			SuccessView.printProudctUpdate("상품 정보 변경에 성공하였습니다.");
		} catch (ModifyException e) {
			e.printStackTrace();
			FailView.printErrorMessage(e.getMessage());
		}
	}
	
	public void productAdd(Product ap) {
		try {
			productService.productAdd(ap);
			SuccessView.printProductAdd("상품등록이 되었습니다.");
		}catch(AddException e) {
			e.printStackTrace();
			FailView.printErrorMessage(e.getMessage());
		}
		
		
		
	}
	public static void main(String[] args) {
		productController.findByName("스");
	}
	
//	public static void main(String[] args) {
//		productController.findByName("스");
//	}
}