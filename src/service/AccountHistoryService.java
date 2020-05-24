package service;

import java.util.List;

import dao.AccountHistoryDAO;
import exception.NotFoundException;
import vo.AccountHistory;

public class AccountHistoryService {
	private static AccountHistoryService service = new AccountHistoryService();
	private AccountHistoryDAO dao;

	private AccountHistoryService() {
		dao = new AccountHistoryDAO();
	}
	public static AccountHistoryService getInstance() {
		return service;
	}
	
	public List<AccountHistory> selectById(String id) throws Exception {
		List<AccountHistory> list = dao.selectById(id);
		return list;
	}
}
