package view;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;

import controller.AccountController;
import controller.AccountHistoryController;
import controller.CustomerController;
import controller.OrderController;
import controller.ProductController;
import session.Session;
import session.SessionSet;
import vo.Account;
import vo.Customer;
import vo.Order;
import vo.Product;

public class MainView {
	private static AccountController accountController = AccountController.getInstance();
	private static AccountHistoryController accountHistoryController = AccountHistoryController.getInstance();
	private static CustomerController customerController = CustomerController.getInstance();
	private static ProductController productController = ProductController.getInstance();
	private static OrderController orderController = OrderController.getInstance();

	static Scanner sc = new Scanner(System.in);
	static Scanner sc2 = new Scanner(System.in);

	public static void printMenu() {
		while (true) {
			System.out.println("--�۾�����--");
			System.out.println("1.����,  2.�α���,  9.����");
			String menu = null;
			menu = sc.next();
			switch (menu) {
			case "1":
				register();
				break;
			case "2":
				login();
				break;
			case "9":
				return;
			}
		}
	}

	// �����ϱ�
	public static void register() {
		System.out.println("--�����ϱ�--");
		System.out.print("ID:");
		String id = sc.next();
		System.out.print("\n��й�ȣ:");
		String pwd = sc.next();
		System.out.print("\n�̸�:");
		String name = sc.next();
		System.out.print("\n����");
		int age = sc.nextInt();
		System.out.print("\n����[M/F]:");
		String gender = sc.next();
		System.out.print("\n��ȭ��ȣ: ");
		String phoneNo = sc.next();
		System.out.print("\n���� ��й�ȣ: ");
		String accountPwd = sc.next();

		Customer c = new Customer();
		c.setId(id);
		c.setPwd(pwd);
		c.setName(name);
		c.setAge(age);
		c.setGender(gender);
		c.setPhoneNo(phoneNo);
		c.setStatus(1);
		c.setJoinDt(new Date());
		customerController.register(c, accountPwd);
	}

	// �α���
	public static void login() {
		System.out.println("--�α���--");
		System.out.print("ID: ");
		String id = sc.next();
		System.out.print("��й�ȣ:");
		String pwd = sc.next();

		SessionSet ss = SessionSet.getInstance();
		Session addSession = new Session(id);
		customerController.login(id, pwd);
		Session session = ss.get(id);

		String menu = null;
		// �α��� ����
		while (session != null) {

			// ������
			if (session.getAttribute("status").equals("2")) {
//          if (true) {
				System.out.println("--������ �޴�--");
				System.out.println("1.ȸ����ȸ, 2.��ǰ��ȸ, 3.�ŷ�������ȸ, 4.��ǰ���, 5.��ǰ��������, 9.�α׾ƿ�");
				menu = sc.next();
				switch (menu) {
				case "1":// 1.ȸ����ȸ
					do {

						System.out.println("--ȸ�� ��ȸ �޴�--");
						System.out.println("1.��ü��ȸ, 2.Ư��ȸ����ȸ(id), 9.�ڷΰ���");
						menu = sc.next();
						switch (menu) {
						case "1":// 1)��ü��ȸ
							customerController.findAll();
							break;
						case "2":// 2)Ư��ȸ����ȸ(id)
							System.out.println("�˻��� ����� ���̵� �Է��� �ֽʽÿ�: ");
							String targetId = sc.next();
							System.out.println();
							customerController.findById(targetId);
							break;
						case "9":
							break;
						}
					} while (!menu.equals("9"));
					menu = "";
					break;
				case "2":// 2.��ǰ��ȸ
					do {
						System.out.println("--��ǰ ��ȸ �޴�--");
						System.out.println("1.�α�� 2.�Ǹ�����, 9.�ڷΰ���");
						menu = sc.next();
						switch (menu) {
						case "1":// 1)�α��
							productController.findBySold();
							break;
						case "2":// 2)�Ǹ�����
							do {
								System.out.println("1.�Ǹ���, 2.�Ǹ�����, 3.��ü��ǰ, 9.�ڷΰ���");
								menu = sc.next();
								switch (menu) {
								case "1":// ���Ǹ���
									productController.findByStatus(1);

									break;
								case "2":// ���Ǹ�����
									productController.findByStatus(2);
									break;
								case "3":// ����ü��ǰ
									productController.findBySold();
									break;
								case "9":// ��ڷΰ���
									break;
								}
							} while (!menu.equals("9"));
							menu = "";
							break;
						case "9":// 9)�ڷΰ���
							break;
						}
					} while (!menu.equals("9"));
					menu = "";
					break;
				case "3":// 3.�ŷ�������ȸ
					do {
						System.out.println("--�ŷ����� ��ȸ �޴�--");
						System.out.println("1.Ư��ȸ����ȸ, 2.��ü��ȸ, 9.�ڷΰ���");
						menu = sc.next();
						switch (menu) {
						case "1":// 1)Ư��ȸ����ȸ
							System.out.print("ã�� ȸ���� ���̵�:");
							String searchId = sc.next();
							orderController.findById(searchId);
							break;
						case "2":// 2)��ü��ȸ
							orderController.findAll();
							break;
						case "9":// 9)�ڷΰ���
							break;
						}
					} while (!menu.equals("9"));
					menu = "";
					break;
				case "4":// 4.��ǰ���
					System.out.println("--��ǰ����ϱ�--");
					System.out.print("No:");
					int no = Integer.parseInt(sc.next());
					System.out.print("����:");
					int price = Integer.parseInt(sc.next());
					System.out.print("��ǰ�̸�(���� ���� �Է��� �ּ���.):");
					String name = sc.next();
					System.out.print("�Ǹſ���(�ǸŻ�ǰ:1, �Ǹ�������ǰ:2):");
					int Status = Integer.parseInt(sc.next());

					Product pa = new Product(no, price, name, 0, Status);
					productController.productAdd(pa);
					break;
				case "5":// 5.��ǰ��������
					System.out.print("��ǰ��ȣ:");

					int pdNo = Integer.parseInt(sc.next());
					System.out.print("����:");
					int pdPrice = Integer.parseInt(sc.next());
					System.out.print("��ǰ�̸�:");
					String pdName = sc.next();
					System.out.print("�Ǹŷ�:");
					int pdSold = Integer.parseInt(sc.next());
					System.out.print("�ǸŻ���(1.�Ǹ��� 2.�Ǹ�����):");
					int pdStatus = Integer.parseInt(sc.next());

					Product p = new Product(pdNo, pdPrice, pdName, pdSold, pdStatus);
					productController.productUpdate(p);
					break;
				case "9":// 9.�α׾ƿ�
					customerController.logout(id);
					session = ss.get(id);
					break;
				}

			} else if ((session.getAttribute("status").equals("1"))) {
//         } else if (true) {
				///////////////// ����� ���////////////////////
				if (!"admin".equals(id)) {
					System.out.println("--" + id + "�� ����ڸ޴�--");
					System.out.println("1.������ȸ, 2.��ǰ��ȸ, 3.������������, 4.�ܾ�����, 5.ȯ��, 6.ȸ����������, 7.Ż��, 9.�α׾ƿ�");
					menu = sc.next();
					switch (menu) {
					case "1":// 1.������ȸ
						do {
							System.out.println("1.�ܾ���ȸ, 2.����ݳ�����ȸ, 9.�ڷΰ���");
							menu = sc.next();
							switch (menu) {
							case "1":// 1)�ܾ���ȸ
								accountController.checkBalanceById(id);
								break;
							case "2":// 2)����ݳ�����ȸ
								accountHistoryController.selectById(id);
								break;
							case "9":// 9)�ڷΰ���
								break;
							}
						} while (!menu.equals("9"));
						menu = "";
						break;
					case "2":// 2.��ǰ��ȸ
						do {
							System.out.println("1.��ǰ��˻�, 2.��ü��ȸ(�α��), 9.�ڷΰ���");
							menu = sc.next();
							switch (menu) {
							case "1":// 1)��ǰ��˻�
								System.out.println("�˻��� ��ǰ���� �Է��� �ֽʽÿ�.");
								String pdName = sc.next();
								System.out.println();
								productController.findByName(pdName);
								System.out.println("1.����, 9.�ڷΰ���");
								menu = sc.next();
								switch (menu) {
								case "1":// �籸��
									purchase(session);
									break;
								case "9":// ��ڷΰ���
									break;
								}
								break;
							case "2":// 2)��ü��ȸ(�α��)
								do {
									productController.findBySold_Cus();
									System.out.println("1.���ݿ�������, 2.���ݳ�������, 9.�ڷΰ���");
									menu = sc.next();
									switch (menu) {
									case "1":// �簡�ݿ�������
										// ���ݿ�������//
										productController.findBySold_Cus_Asc();
										System.out.println("1.����, 9.�ڷΰ���");
										menu = sc.next();
										switch (menu) {
										case "1":// (1)����
											purchase(session);
											break;
										case "9":// (9)�ڷΰ���
										}

										break;
									case "2":// �谡�ݳ�������
										// ���ݳ�������//
										productController.findBySold_Cus_Desc();
										System.out.println("1.����, 9.�ڷΰ���");
										menu = sc.next();
										switch (menu) {
										case "1":// (1)����
											purchase(session);
											break;
										case "9":// (9)�ڷΰ���
										}
										break;
									}
								} while (!menu.equals("9"));
								menu = "";
								break;

							case "9":// 9)�ڷΰ���
								break;
							}
						} while (!menu.equals("9"));
						menu = "";
						break;

					case "3":// 3.������������
						// ������������//
						System.out.println("--" + id + "���� ������--");
						orderController.findByReceiveId(id);
						break;
					case "4":// 4.�ܾ�����
						// �ܾ�����//
						System.out.println("==========�ܾ�����==========");
						System.out.println("�����Ͻ� �ݾ��� �Է��� �ֽʽÿ�.");
						int money = sc.nextInt();
						accountController.deposit(id, money);
						break;
					case "5":// 5.ȯ��
						do {
							System.out.println("1.��ǰ�ŷ�������ȸ, 2.ȯ�һ�ǰ����, 9.�ڷΰ���");
							menu = sc.next();
							switch (menu) {
							case "1":// 1)��ǰ�ŷ�������ȸ
								// ��ǰ�ŷ�����//
								System.out.println("--" + id + "���� ���Ÿ��--");
								orderController.findById(id);
								System.out.println("1.ȯ��, 2.�ڷΰ���");
								switch (sc.next()) {
								case "1":
									System.out.println("ȯ���Ͻ� ��ǰ�� ��ǰ��ȣ�� �Է��� �ֽʽÿ�.");
									int pd_no = sc.nextInt();
									System.out.println("ȯ���Ͻ� ������ �Է��� �ֽʽÿ�.");
									int quantity = sc.nextInt();
									Customer customer = new Customer();
									customer.setId(id);
									Account account = new Account();
									account.setCustomer(customer);
									Order order = new Order();
									order.setAccount(account);

									Product product = new Product();
									product.setNo(pd_no);
									order.setProduct(product);

									order.setQuantity(quantity);
									orderController.refund(order);
									break;
								case "9":
									break;
								}
								break;
							case "2":
								orderController.findRefundById(id);
								break;
							case "9":// 9)�ڷΰ���
								break;
							}
						} while (!menu.equals("9"));
						menu = "";
						break;
					case "6":// 6.ȸ����������
						System.out.println("--ȸ�� ���� �����ϱ�--");
						System.out.print("��й�ȣ:");
						String modifyPwd = sc2.next();
						System.out.print("�̸�:");
						String modifyName = sc.next();
						System.out.print("����:");
						int modifyAge = sc.nextInt();

						System.out.print("����[M/F]:");
						String modifyGender = sc.next();
						System.out.print("��ȭ��ȣ: ");
						String modifyPhoneNo = sc2.next();
						System.out.println("���º�й�ȣ:");
						String modifyAccountPwd = sc.next();
						System.out.println();

						Customer c = new Customer();
						c.setId(id);
						c.setPwd(modifyPwd);
						c.setName(modifyName);
						c.setAge(modifyAge);
						c.setGender(modifyGender);
						c.setPhoneNo(modifyPhoneNo);
						c.setStatus(1);
						c.setJoinDt(new Date());

						System.out.println(c.getPwd());

						customerController.modify(c, modifyAccountPwd);
						break;
					case "7":// 7.Ż��
						customerController.withdraw(id);
						return;
					case "9":// 9.�α׾ƿ�
						customerController.logout(id);
						session = ss.get(id);
						return;
					}
				}

			} else {
				///////// Ż���ڸ޴�////////////
				System.out.println("���� Ż���� �޴� ���� ����");
				return;
			}

		}

	}

	// ȸ����������(�����ϱ�)
	public static void modify(String id) {
	}

	public static void main(String[] args) {
		printMenu();
	}

	public static void purchase(Session session) {
		System.out.println("������ ��ǰ��ȣ�� �Է��� �ֽʽÿ�");
		int pd_no = sc.nextInt();
		System.out.println("������ �Է����ֽʽÿ�");
		int quantity = sc.nextInt();
		System.out.println("�����Ͻ� ���� ID�� �Է����ּ���");
		String receivedId = sc.next();
		String id_check = session.getSessionId();

		Date date = new Date();
		Timestamp dt = new Timestamp(date.getTime());
		orderController.insert(pd_no, receivedId, quantity, dt, id_check);
	}

	public static void printByStatus(int status) {

		;
	}

}