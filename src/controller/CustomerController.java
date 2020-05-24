package controller;

import java.util.List;

import exception.AddException;
import exception.ModifyException;
import exception.NotFoundException;
import service.CustomerService;
import session.Session;
import session.SessionSet;
import view.FailView;
import view.SuccessView;
import vo.Customer;

public class CustomerController {
	private static CustomerController controller = new CustomerController();
	private CustomerService service = CustomerService.getInstance();
	private CustomerController() {}
	public static CustomerController getInstance() {
		return controller;
	}
	public void login(String id, String pwd) {		
		try {
			service.login(id, pwd);
			SuccessView.printLogin("�α��μ���!");
			//�α��μ����� �������� �ý��ۿ� ���
		} catch (NotFoundException e) {
			FailView.errorMessage("�α��ν���!");
		}
	}
	public void register(Customer c, String AccountPwd) {
		try {
			service.register(c, AccountPwd );
			SuccessView.printRegister("���Լ���!");
		} catch (AddException e) {
			FailView.errorMessage(e.getMessage());
		}
	}
	public void findById(String id) {
		try {
			Customer c = service.findById(id);
			SuccessView.printCustomer(c);
		} catch (NotFoundException e) {
			FailView.errorMessage(e.getMessage());
		}
		
	}
	
	public void findAll() {
		try {
			List<Customer> list = service.findAll();
			SuccessView.printCustomers(list);
		} catch (NotFoundException e) {
			FailView.errorMessage(e.getMessage());
		}
	}
	
	public void logout(String id) {
		SessionSet ss = SessionSet.getInstance();
		Session session = ss.get(id);
		ss.remove(session);		
	}
	public void modify(Customer c, String accountPwd) {
		try {
			
			
			System.out.println(c.getPwd());
			
			
			service.modify(c, accountPwd);
			SuccessView.printModify("��������!");
		}catch(ModifyException e) {
			FailView.errorMessage(e.getMessage());
		}
	}
	public void withdraw(String id) {
		try {
			service.withdraw(id);
			SuccessView.printModify("Ż�𼺰�!");
		}catch(ModifyException e) {
			FailView.errorMessage(e.getMessage());
		}
	}
	
	public void findByName(String word) {
		try {
			List<Customer> list = service.findByName(word);
			SuccessView.printCustomers(list);
		} catch (NotFoundException e) {
			FailView.errorMessage(e.getMessage());
		}
		
	}
	
	
}



