package controller;

import java.sql.Timestamp;
import java.util.List;

import exception.AddException;
import exception.NotFoundException;
import service.PurchaseHistoryService;
import view.FailView;
import view.SuccessView;
import vo.PurchaseHistory;

public class PurchaseHistoryController {
	private static PurchaseHistoryController controller = new PurchaseHistoryController();
	private PurchaseHistoryService service = PurchaseHistoryService.getInstance();

	private PurchaseHistoryController() {
	}

	public void insert(int pd_no, String receivedId, int quantity, Timestamp dt, String id_check) {

		try {
			service.insert(pd_no, receivedId, quantity, dt, id_check);
			SuccessView.printBuy("�ش� ��ǰ�� ���ŵǾ����ϴ�.");
		} catch (AddException e) {
			FailView.errorMessage("�ܾ��� �����մϴ�");
		} catch (NotFoundException e) {
			System.out.println("��ǰ�� ������ id�� ���� ������ ã�� ���߽��ϴ�.");
		}

	}

	public static PurchaseHistoryController getInstance() {
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
			List<PurchaseHistory> list = service.findById(id);
			SuccessView.printPurchaseHistory(list);
		} catch (NotFoundException e) {
			e.printStackTrace();
			FailView.errorMessage(e.getMessage());
		}
	}

	public void findRefundById(String id) {
		try {
			List<PurchaseHistory> list = service.findById(id);
			SuccessView.printRefundPurchaseHistory(list);
		} catch (NotFoundException e) {
			e.printStackTrace();
			FailView.errorMessage(e.getMessage());
		}
	}

	public void findAll() {
		try {
			List<PurchaseHistory> list = service.findAll();
			SuccessView.printPurchaseHistory(list);
		} catch (NotFoundException e) {
			e.printStackTrace();
			FailView.errorMessage(e.getMessage());
		}
	}

	public void refund(PurchaseHistory order) {
		try {
			PurchaseHistory refundOrder = service.findRefundProduct(order);
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

	public void findByDate(String id, Timestamp startTime, Timestamp endTime) {
		try {
			List<PurchaseHistory> list = service.findByDate(id, startTime, endTime);
			SuccessView.printPurchaseHistoryByDate(list, startTime, endTime);
		} catch (NotFoundException e) {
			e.printStackTrace();
			FailView.errorMessage(e.getMessage());
		}
	}
}
