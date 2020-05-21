package controller;

import exception.ModifyException;
import exception.NotFoundException;
import service.AccountService;
import view.FailView;
import view.SuccessView;

public class AccountController {
	private static AccountController controller = new AccountController();
	private AccountService service = AccountService.getInstance();

	private AccountController() {
	}

	public static AccountController getInstance() {
		return controller;
	}

	public void checkBalanceById(String id) {
		try {
			int balance = service.checkBalanceById(id);
			SuccessView.printBalance(balance);
		} catch (NotFoundException e) {
			FailView.printErrorMessage(e.getMessage());
			e.printStackTrace();
		}
	}

	public void deposit(String id, int money) {
		try {
			service.deposit(id, money);
			SuccessView.printDeposit();
		} catch (ModifyException e) {
			e.printStackTrace();
			FailView.errorMessage(e.getMessage());
		}
	}

	public boolean checkAccountPwd(String sessionId, String accountPwd) {
		try {
			service.checkAccountPwd(sessionId, accountPwd);
			return true;
		} catch (NotFoundException e) {
			FailView.errorMessage(e.getMessage());
			return false;
		}

	}
}
