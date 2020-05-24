package controller;

import exception.NotFoundException;
import service.AccountHistoryService;
import view.FailView;
import view.SuccessView;

public class AccountHistoryController {
	private static AccountHistoryController controller = new AccountHistoryController();
	private AccountHistoryService accountHistoryService = AccountHistoryService.getInstance();
	
	private AccountHistoryController() {}
	
	public static AccountHistoryController getInstance() {
		return controller;
	}
	
	public void selectById(String id){
		try {
			SuccessView.printAccountHistoryList(accountHistoryService.selectById(id));
		} catch (NotFoundException e) {
			FailView.printErrorMessage(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			FailView.printErrorMessage(e.getMessage());
			e.printStackTrace();
		}
	}
//	public static void main(String[] args) {
//		AccountHistoryController ac = new AccountHistoryController();
//		ac.selectById("id1");
//		ac.selectById("id2");
//		ac.selectById("jhgyuy");
//	}

}
