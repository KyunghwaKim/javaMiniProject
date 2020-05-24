package controller;

import java.sql.Timestamp;
import java.util.List;

import exception.AddException;
import exception.NotFoundException;
import service.OrderService;
import view.FailView;
import view.SuccessView;
import vo.Order;

public class OrderController {
	private static OrderController controller = new OrderController();
	private OrderService service = OrderService.getInstance();
	
	private OrderController() {	}
	
	public void insert(int pd_no, String receivedId, int quantity, Timestamp dt, String id_check ) {
		
		
		try {
			service.insert( pd_no, receivedId, quantity, dt, id_check);
			SuccessView.printBuy("�ش� ��ǰ�� ���ŵǾ����ϴ�.");
		} catch (AddException e) {
			
			e.printStackTrace();
			FailView.errorMessage(e.getMessage());
		} catch (NotFoundException e) {
			
			//e.printStackTrace();
			FailView.errorMessage(e.getMessage());
			System.out.println("��ǰ�� ������ id�� ���� ������ ã�� ���߽��ϴ�.");
		}
		
		
	}
	
	public static OrderController getInstance() {
		return controller;
	}

	public void findByReceiveId(String id) {
		try {
			List<Object[]> list = service.findByReceiveId(id);
			SuccessView.printGiftChart(list);
		} catch (NotFoundException e) {
			e.printStackTrace();
			FailView.errorMessage(e.getMessage());
		}
	}

	public void findById(String id) {
		try {
			List<Order> list = service.findById(id);
			SuccessView.printPurchaseHistory(list);
		} catch (NotFoundException e) {
			e.printStackTrace();
			FailView.errorMessage(e.getMessage());
		}
	}
	
	public void findRefundById(String id) {
		try {
			List<Order> list = service.findById(id);
			SuccessView.printRefundPurchaseHistory(list);
		} catch (NotFoundException e) {
			e.printStackTrace();
			FailView.errorMessage(e.getMessage());
		}
	}

	public void findAll() {
		try {
			List<Order> list = service.findAll();
			SuccessView.printPurchaseHistory(list);
		} catch (NotFoundException e) {
			e.printStackTrace();
			FailView.errorMessage(e.getMessage());
		}
	}

	public void refund(Order order) {
		try {
			Order refundOrder = service.findRefundProduct(order);
			service.refund(refundOrder);
			SuccessView.printRefund("ȯ���� ���������� ó���Ǿ����ϴ�");
		} catch (NotFoundException e) {
			e.printStackTrace();
			FailView.errorMessage("ȯ���� ��ǰ�� ���ų����� �����ϴ�");
		} catch (AddException e1) {
			e1.printStackTrace();
			FailView.errorMessage("ȯ�ҿ� �����Ͽ����ϴ�");
		}
	}
}
