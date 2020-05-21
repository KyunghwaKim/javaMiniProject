package view;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

import vo.AccountHistory;
import vo.Customer;
import vo.PurchaseHistory;
import vo.Product;

public class SuccessView {
	public static void printBuy(String message) {
		System.out.println(message);

	}

	public static void printBalance(int balance) {
		System.out.println("------잔액 조회-----");
		System.out.println("현재 잔액은 " + balance + "원 입니다.");
	}

	public static void printAccountHistoryList(List<AccountHistory> list) {
		System.out.println("-----입출금내역조회-----");
		for (AccountHistory ah : list) {
			String id = ah.getAccount().getCustomer().getId();
			String date = new SimpleDateFormat("yy-MM-dd").format(ah.getDate());
			String type = null;
			if (ah.getType() == 1) {
				type = "충전";
			} else if (ah.getType() == 2) {
				type = "구입";
			} else if (ah.getType() == 3) {
				type = "환불처리완료";
			} else if (ah.getType() == 4) {
				type = "환불";
			}
			String message = "id: " + id + ", 거래일자: " + date + ", " + type + ": " + ah.getMoney() + ", 잔액: "
					+ ah.getBalanceChanged();
			System.out.println(message);
		}
	}

	public static void printAccountHistoryInputList(List<AccountHistory> list) {
		for (AccountHistory ah : list) {
			if (ah.getType() == 1) {
				String id = ah.getAccount().getCustomer().getId();
				String date = new SimpleDateFormat("yy-MM-dd").format(ah.getDate());

				String message = "id: " + id + ", 거래일자: " + date + ": " + ah.getMoney() + ", 잔액: "
						+ ah.getBalanceChanged();
				System.out.println(message);
			}
		}
	}

	public static void printAccountHistoryOutputList(List<AccountHistory> list) {
		for (AccountHistory ah : list) {
			if (ah.getType() == 2) {
				String id = ah.getAccount().getCustomer().getId();
				String date = new SimpleDateFormat("yy-MM-dd").format(ah.getDate());

				String message = "id: " + id + ", 거래일자: " + date + ": " + ah.getMoney() + ", 잔액: "
						+ ah.getBalanceChanged();
				System.out.println(message);
			}
		}
	}

	public static void printByStatus(List<Product> list) {
		for (Product p : list) {

			System.out.print("상품번호:" + p.getNo() + ", 상품명:" + p.getName() + ", 가격:" + p.getPrice() + ", 판매량"
					+ p.getSold() + ", 상태:");
			switch (p.getStatus()) {
			case 1:
				System.out.println("판매중");
				break;
			case 2:
				System.out.println("판매중지중");
				break;
			}
		}

	}

	/**
	 * 로그인 결과를 출력한다
	 * 
	 * @param msg
	 */
	public static void printLogin(String msg) {
		System.out.println(msg);

	}

	/**
	 * 가입 결과를 출력한다
	 * 
	 * @param msg
	 */
	public static void printRegister(String msg) {
		System.out.println(msg);

	}

	/**
	 * 고객1명을 출력한다
	 * 
	 * @param c 고객
	 */
	public static void printCustomer(Customer c) {
		System.out.print("ID : " + c.getId());
		System.out.print(", 이름 : " + c.getName());
		System.out.print(", 나이 : " + c.getAge());
		System.out.print(", 성별은 ");
		switch (c.getGender()) {
		case "M":
			System.out.print("남");
			break;
		case "F":
			System.out.print("여");
			break;
		default:
			System.out.print("없음");
		}
		System.out.print(", 전화번호 : " + c.getPhoneNo());
		System.out.print(", 가입일자 : " + c.getJoinDt());
		System.out.print(", 회원상태 : ");
		switch (c.getStatus()) {
		case 2:
			System.out.print("탈퇴상태");
			break;
		case 1:
			System.out.print("가입상태");
			break;
		default:
			System.out.print("비상정회원입니다.");
		}
		System.out.println();
	}

	/**
	 * 고객들 출력한다
	 * 
	 * @param list 고객들
	 */
	public static void printCustomers(List<Customer> list) {
		for (Customer c : list) {
			printCustomer(c);
		}
	}

	public static void printModify(String msg) {
		System.out.println(msg);
	}

	/**
	 * 이름에 맞는 상품 리스트 출력
	 * 
	 * @param list
	 */
	public static void printByName(List<Product> list) {
		for (Product p : list) {
			// System.out.println("실행");

			System.out.print("상품번호:" + p.getNo() + ", 이름: " + p.getName() + ", 가격: " + p.getPrice() + ", 판매량: "
					+ p.getSold() + ", 상태: ");

			switch (p.getStatus()) {
			case 1:
				System.out.println("판매중");
				break;
			case 2:
				System.out.println("판매중지중");
				break;
			}
			;

		}

	}

	/**
	 * 판매량 (인기순)에 따른 리스트 출력
	 * 
	 * @param list
	 */
	public static void printBySold(List<Product> list) {
		System.out.println("----전체상품조회-----");
		for (Product p : list) {
			System.out.print("상품번호:" + p.getNo() + ", 이름: " + p.getName() + ", 가격: " + p.getPrice() + ", 판매량: "
					+ p.getSold() + " 상태: ");
			switch (p.getStatus()) {
			case 1:
				System.out.println("판매중");
				break;
			case 2:
				System.out.println("판매중지중");
				break;
			}

		}

	}

	public static void printGiftChart(List<Object[]> list) {
		for (int i = 0; i < list.size(); i++) {
			Object customerName = list.get(i)[0];
			Object productName = list.get(i)[1];
			Object quantity = list.get(i)[2];
			Integer ph_status = (Integer) list.get(i)[3];
			String status = "";
			if (ph_status == 3)
				status = ", 환불완료";
			System.out.println("보낸사람:" + customerName + ", 받은선물:" + productName + ", 수량:" + quantity + status);
		}
	}

	public static void printPurchaseHistory(List<PurchaseHistory> list) {
		System.out.println("-----구매내역조회-----");
		for (PurchaseHistory order : list) {
			int status = order.getStatus();
			if (true) {
				Product product = order.getProduct();
				Customer customer = order.getReceiveId();
				String statusS = "";
				if (status == 1) {
					statusS = "구매";
				} else if (status == 2) {
					statusS = "환불처리완료";
				} else if (status == 3) {
					statusS = "환불";
				}
				System.out.println("상품번호:" + product.getNo() + ", 구매개수:" + order.getQuantity() + ", 선물수신자Id:"
						+ customer.getId() + ", 구매일자:" + new SimpleDateFormat("yyyy-MM-dd").format(order.getDate())
						+ ", 상태:" + statusS);
			}
		}
	}

	public static void printRefundPurchaseHistory(List<PurchaseHistory> list) {
		for (PurchaseHistory order : list) {
			int status = order.getStatus();
			if (status == 2) {
				Product product = order.getProduct();
				Customer customer = order.getReceiveId();
				String statusS = "";
				if (status == 1) {
					statusS = "구매";
				} else if (status == 2) {
					statusS = "환불처리완료";
				} else if (status == 3) {
					statusS = "환불";
				}
				System.out.println("상품번호:" + product.getNo() + ", 구매개수:" + order.getQuantity() + ", 선물수신자Id:"
						+ customer.getId() + ", 구매일자:" + new SimpleDateFormat("yyyy-MM-dd").format(order.getDate())
						+ ", 상태:" + statusS);
			}
		}
	}

	/**
	 * 가격순에 따른 리스트 출력 내림차순, 오름차순
	 * 
	 * @param list
	 */
	public static void printBySoldDesc(List<Product> list) {
		list.sort(new Comparator<Product>() {
			@Override
			public int compare(Product o1, Product o2) {
				if (o1.getPrice() < o2.getPrice())
					return 1;
				else if (o1.getPrice() > o2.getPrice())
					return -1;

				else
					return 0;
			}

		});
		System.out.println("가격이 높은순으로 배열합니다");
		for (Product p : list) {
			System.out.print(
					"상품번호 :" + p.getNo() + " 가격 : " + p.getPrice() + " 이름 : " + p.getName() + " 판매량 : " + p.getSold());
			switch (p.getStatus()) {
			case 1:
				System.out.println(" 판매중");
				break;
			case 2:
				System.out.println("판매중지중");
				break;
			}
		}
	}

	public static void printBySoldAsc(List<Product> list) {
		list.sort(new Comparator<Product>() {

			@Override
			public int compare(Product o1, Product o2) {
				if (o1.getPrice() > o2.getPrice())
					return 1;
				else if (o1.getPrice() < o2.getPrice())
					return -1;

				else
					return 0;
			}

		});
		System.out.println("가격이 낮은순으로 배열합니다");
		for (Product p : list) {
			System.out.print(
					"상품번호 :" + p.getNo() + " 가격 : " + p.getPrice() + " 이름 : " + p.getName() + " 판매량 : " + p.getSold());
			switch (p.getStatus()) {
			case 1:
				System.out.println(" 판매중");
				break;
			case 2:
				System.out.println("판매중지중");
				break;
			}
		}
	}

	public static void printDeposit() {
		System.out.println("충전이 완료 되었습니다.");
	}

	public static void printProudctUpdate(String msg) {
		System.out.println(msg);
	}

	public static void printProductAdd(String msg) {
		System.out.println(msg);

	}

	public static void printRefund(String msg) {
		System.out.println(msg);
	}

	public static void printPurchaseHistoryByDate(List<PurchaseHistory> list, Timestamp startTime, Timestamp endTime) {
		String startStr = new SimpleDateFormat("yyyy-MM-dd").format(startTime);
		String endStr = new SimpleDateFormat("yyyy-MM-dd").format(endTime);
		System.out.println("----------" + startStr + "부터 " + endStr + "까지의 구매내역-----------");
		for (PurchaseHistory order : list) {
			int status = order.getStatus();
			if (true) {
				Product product = order.getProduct();
				Customer customer = order.getReceiveId();
				String statusS = "";
				if (status == 1) {
					statusS = "구매";
				} else if (status == 2) {
					statusS = "환불";
				} else if (status == 3) {
					statusS = "환불처리완료";
				}
				System.out.println("상품번호:" + product.getNo() + ", 구매개수:" + order.getQuantity() + ", 선물수신자Id:"
						+ customer.getId() + ", 구매일자:" + new SimpleDateFormat("yyyy-MM-dd").format(order.getDate())
						+ ", 상태:" + statusS);
			}
		}
	}
}
