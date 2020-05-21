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
		System.out.println("------�ܾ� ��ȸ-----");
		System.out.println("���� �ܾ��� " + balance + "�� �Դϴ�.");
	}

	public static void printAccountHistoryList(List<AccountHistory> list) {
		System.out.println("-----����ݳ�����ȸ-----");
		for (AccountHistory ah : list) {
			String id = ah.getAccount().getCustomer().getId();
			String date = new SimpleDateFormat("yy-MM-dd").format(ah.getDate());
			String type = null;
			if (ah.getType() == 1) {
				type = "����";
			} else if (ah.getType() == 2) {
				type = "����";
			} else if (ah.getType() == 3) {
				type = "ȯ��ó���Ϸ�";
			} else if (ah.getType() == 4) {
				type = "ȯ��";
			}
			String message = "id: " + id + ", �ŷ�����: " + date + ", " + type + ": " + ah.getMoney() + ", �ܾ�: "
					+ ah.getBalanceChanged();
			System.out.println(message);
		}
	}

	public static void printAccountHistoryInputList(List<AccountHistory> list) {
		for (AccountHistory ah : list) {
			if (ah.getType() == 1) {
				String id = ah.getAccount().getCustomer().getId();
				String date = new SimpleDateFormat("yy-MM-dd").format(ah.getDate());

				String message = "id: " + id + ", �ŷ�����: " + date + ": " + ah.getMoney() + ", �ܾ�: "
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

				String message = "id: " + id + ", �ŷ�����: " + date + ": " + ah.getMoney() + ", �ܾ�: "
						+ ah.getBalanceChanged();
				System.out.println(message);
			}
		}
	}

	public static void printByStatus(List<Product> list) {
		for (Product p : list) {

			System.out.print("��ǰ��ȣ:" + p.getNo() + ", ��ǰ��:" + p.getName() + ", ����:" + p.getPrice() + ", �Ǹŷ�"
					+ p.getSold() + ", ����:");
			switch (p.getStatus()) {
			case 1:
				System.out.println("�Ǹ���");
				break;
			case 2:
				System.out.println("�Ǹ�������");
				break;
			}
		}

	}

	/**
	 * �α��� ����� ����Ѵ�
	 * 
	 * @param msg
	 */
	public static void printLogin(String msg) {
		System.out.println(msg);

	}

	/**
	 * ���� ����� ����Ѵ�
	 * 
	 * @param msg
	 */
	public static void printRegister(String msg) {
		System.out.println(msg);

	}

	/**
	 * ��1���� ����Ѵ�
	 * 
	 * @param c ��
	 */
	public static void printCustomer(Customer c) {
		System.out.print("ID : " + c.getId());
		System.out.print(", �̸� : " + c.getName());
		System.out.print(", ���� : " + c.getAge());
		System.out.print(", ������ ");
		switch (c.getGender()) {
		case "M":
			System.out.print("��");
			break;
		case "F":
			System.out.print("��");
			break;
		default:
			System.out.print("����");
		}
		System.out.print(", ��ȭ��ȣ : " + c.getPhoneNo());
		System.out.print(", �������� : " + c.getJoinDt());
		System.out.print(", ȸ������ : ");
		switch (c.getStatus()) {
		case 2:
			System.out.print("Ż�����");
			break;
		case 1:
			System.out.print("���Ի���");
			break;
		default:
			System.out.print("�����ȸ���Դϴ�.");
		}
		System.out.println();
	}

	/**
	 * ���� ����Ѵ�
	 * 
	 * @param list ����
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
	 * �̸��� �´� ��ǰ ����Ʈ ���
	 * 
	 * @param list
	 */
	public static void printByName(List<Product> list) {
		for (Product p : list) {
			// System.out.println("����");

			System.out.print("��ǰ��ȣ:" + p.getNo() + ", �̸�: " + p.getName() + ", ����: " + p.getPrice() + ", �Ǹŷ�: "
					+ p.getSold() + ", ����: ");

			switch (p.getStatus()) {
			case 1:
				System.out.println("�Ǹ���");
				break;
			case 2:
				System.out.println("�Ǹ�������");
				break;
			}
			;

		}

	}

	/**
	 * �Ǹŷ� (�α��)�� ���� ����Ʈ ���
	 * 
	 * @param list
	 */
	public static void printBySold(List<Product> list) {
		System.out.println("----��ü��ǰ��ȸ-----");
		for (Product p : list) {
			System.out.print("��ǰ��ȣ:" + p.getNo() + ", �̸�: " + p.getName() + ", ����: " + p.getPrice() + ", �Ǹŷ�: "
					+ p.getSold() + " ����: ");
			switch (p.getStatus()) {
			case 1:
				System.out.println("�Ǹ���");
				break;
			case 2:
				System.out.println("�Ǹ�������");
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
				status = ", ȯ�ҿϷ�";
			System.out.println("�������:" + customerName + ", ��������:" + productName + ", ����:" + quantity + status);
		}
	}

	public static void printPurchaseHistory(List<PurchaseHistory> list) {
		System.out.println("-----���ų�����ȸ-----");
		for (PurchaseHistory order : list) {
			int status = order.getStatus();
			if (true) {
				Product product = order.getProduct();
				Customer customer = order.getReceiveId();
				String statusS = "";
				if (status == 1) {
					statusS = "����";
				} else if (status == 2) {
					statusS = "ȯ��ó���Ϸ�";
				} else if (status == 3) {
					statusS = "ȯ��";
				}
				System.out.println("��ǰ��ȣ:" + product.getNo() + ", ���Ű���:" + order.getQuantity() + ", ����������Id:"
						+ customer.getId() + ", ��������:" + new SimpleDateFormat("yyyy-MM-dd").format(order.getDate())
						+ ", ����:" + statusS);
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
					statusS = "����";
				} else if (status == 2) {
					statusS = "ȯ��ó���Ϸ�";
				} else if (status == 3) {
					statusS = "ȯ��";
				}
				System.out.println("��ǰ��ȣ:" + product.getNo() + ", ���Ű���:" + order.getQuantity() + ", ����������Id:"
						+ customer.getId() + ", ��������:" + new SimpleDateFormat("yyyy-MM-dd").format(order.getDate())
						+ ", ����:" + statusS);
			}
		}
	}

	/**
	 * ���ݼ��� ���� ����Ʈ ��� ��������, ��������
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
		System.out.println("������ ���������� �迭�մϴ�");
		for (Product p : list) {
			System.out.print(
					"��ǰ��ȣ :" + p.getNo() + " ���� : " + p.getPrice() + " �̸� : " + p.getName() + " �Ǹŷ� : " + p.getSold());
			switch (p.getStatus()) {
			case 1:
				System.out.println(" �Ǹ���");
				break;
			case 2:
				System.out.println("�Ǹ�������");
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
		System.out.println("������ ���������� �迭�մϴ�");
		for (Product p : list) {
			System.out.print(
					"��ǰ��ȣ :" + p.getNo() + " ���� : " + p.getPrice() + " �̸� : " + p.getName() + " �Ǹŷ� : " + p.getSold());
			switch (p.getStatus()) {
			case 1:
				System.out.println(" �Ǹ���");
				break;
			case 2:
				System.out.println("�Ǹ�������");
				break;
			}
		}
	}

	public static void printDeposit() {
		System.out.println("������ �Ϸ� �Ǿ����ϴ�.");
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
		System.out.println("----------" + startStr + "���� " + endStr + "������ ���ų���-----------");
		for (PurchaseHistory order : list) {
			int status = order.getStatus();
			if (true) {
				Product product = order.getProduct();
				Customer customer = order.getReceiveId();
				String statusS = "";
				if (status == 1) {
					statusS = "����";
				} else if (status == 2) {
					statusS = "ȯ��";
				} else if (status == 3) {
					statusS = "ȯ��ó���Ϸ�";
				}
				System.out.println("��ǰ��ȣ:" + product.getNo() + ", ���Ű���:" + order.getQuantity() + ", ����������Id:"
						+ customer.getId() + ", ��������:" + new SimpleDateFormat("yyyy-MM-dd").format(order.getDate())
						+ ", ����:" + statusS);
			}
		}
	}
}
