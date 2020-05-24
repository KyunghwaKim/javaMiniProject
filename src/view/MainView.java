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
			System.out.println("--작업구분--");
			System.out.println("1.가입,  2.로그인,  9.종료");
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

	// 가입하기
	public static void register() {
		System.out.println("--가입하기--");
		System.out.print("ID:");
		String id = sc.next();
		System.out.print("\n비밀번호:");
		String pwd = sc.next();
		System.out.print("\n이름:");
		String name = sc.next();
		System.out.print("\n나이");
		int age = sc.nextInt();
		System.out.print("\n성별[M/F]:");
		String gender = sc.next();
		System.out.print("\n전화번호: ");
		String phoneNo = sc.next();
		System.out.print("\n계좌 비밀번호: ");
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

	// 로그인
	public static void login() {
		System.out.println("--로그인--");
		System.out.print("ID: ");
		String id = sc.next();
		System.out.print("비밀번호:");
		String pwd = sc.next();

		SessionSet ss = SessionSet.getInstance();
		Session addSession = new Session(id);
		customerController.login(id, pwd);
		Session session = ss.get(id);

		String menu = null;
		// 로그인 성공
		while (session != null) {

			// 관리자
			if (session.getAttribute("status").equals("2")) {
//          if (true) {
				System.out.println("--관리자 메뉴--");
				System.out.println("1.회원조회, 2.상품조회, 3.거래내역조회, 4.상품등록, 5.상품정보변경, 9.로그아웃");
				menu = sc.next();
				switch (menu) {
				case "1":// 1.회원조회
					do {

						System.out.println("--회원 조회 메뉴--");
						System.out.println("1.전체조회, 2.특정회원조회(id), 9.뒤로가기");
						menu = sc.next();
						switch (menu) {
						case "1":// 1)전체조회
							customerController.findAll();
							break;
						case "2":// 2)특정회원조회(id)
							System.out.println("검색할 대상의 아이디를 입력해 주십시오: ");
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
				case "2":// 2.상품조회
					do {
						System.out.println("--상품 조회 메뉴--");
						System.out.println("1.인기순 2.판매유무, 9.뒤로가기");
						menu = sc.next();
						switch (menu) {
						case "1":// 1)인기순
							productController.findBySold();
							break;
						case "2":// 2)판매유무
							do {
								System.out.println("1.판매중, 2.판매중지, 3.전체상품, 9.뒤로가기");
								menu = sc.next();
								switch (menu) {
								case "1":// ①판매중
									productController.findByStatus(1);

									break;
								case "2":// ②판매중지
									productController.findByStatus(2);
									break;
								case "3":// ③전체상품
									productController.findBySold();
									break;
								case "9":// ⑨뒤로가기
									break;
								}
							} while (!menu.equals("9"));
							menu = "";
							break;
						case "9":// 9)뒤로가기
							break;
						}
					} while (!menu.equals("9"));
					menu = "";
					break;
				case "3":// 3.거래내역조회
					do {
						System.out.println("--거래내역 조회 메뉴--");
						System.out.println("1.특정회원조회, 2.전체조회, 9.뒤로가기");
						menu = sc.next();
						switch (menu) {
						case "1":// 1)특정회원조회
							System.out.print("찾을 회원의 아이디:");
							String searchId = sc.next();
							orderController.findById(searchId);
							break;
						case "2":// 2)전체조회
							orderController.findAll();
							break;
						case "9":// 9)뒤로가기
							break;
						}
					} while (!menu.equals("9"));
					menu = "";
					break;
				case "4":// 4.상품등록
					System.out.println("--상품등록하기--");
					System.out.print("No:");
					int no = Integer.parseInt(sc.next());
					System.out.print("가격:");
					int price = Integer.parseInt(sc.next());
					System.out.print("상품이름(공백 없이 입력해 주세요.):");
					String name = sc.next();
					System.out.print("판매여부(판매상품:1, 판매중지상품:2):");
					int Status = Integer.parseInt(sc.next());

					Product pa = new Product(no, price, name, 0, Status);
					productController.productAdd(pa);
					break;
				case "5":// 5.상품정보변경
					System.out.print("제품번호:");

					int pdNo = Integer.parseInt(sc.next());
					System.out.print("가격:");
					int pdPrice = Integer.parseInt(sc.next());
					System.out.print("제품이름:");
					String pdName = sc.next();
					System.out.print("판매량:");
					int pdSold = Integer.parseInt(sc.next());
					System.out.print("판매상태(1.판매중 2.판매종료):");
					int pdStatus = Integer.parseInt(sc.next());

					Product p = new Product(pdNo, pdPrice, pdName, pdSold, pdStatus);
					productController.productUpdate(p);
					break;
				case "9":// 9.로그아웃
					customerController.logout(id);
					session = ss.get(id);
					break;
				}

			} else if ((session.getAttribute("status").equals("1"))) {
//         } else if (true) {
				///////////////// 사용자 모드////////////////////
				if (!"admin".equals(id)) {
					System.out.println("--" + id + "님 사용자메뉴--");
					System.out.println("1.계좌조회, 2.상품조회, 3.선물받은내역, 4.잔액충전, 5.환불, 6.회원정보변경, 7.탈퇴, 9.로그아웃");
					menu = sc.next();
					switch (menu) {
					case "1":// 1.계좌조회
						do {
							System.out.println("1.잔액조회, 2.입출금내역조회, 9.뒤로가기");
							menu = sc.next();
							switch (menu) {
							case "1":// 1)잔액조회
								accountController.checkBalanceById(id);
								break;
							case "2":// 2)입축금내역조회
								accountHistoryController.selectById(id);
								break;
							case "9":// 9)뒤로가기
								break;
							}
						} while (!menu.equals("9"));
						menu = "";
						break;
					case "2":// 2.상품조회
						do {
							System.out.println("1.상품명검색, 2.전체조회(인기순), 9.뒤로가기");
							menu = sc.next();
							switch (menu) {
							case "1":// 1)상품명검색
								System.out.println("검색할 상품명을 입력해 주십시오.");
								String pdName = sc.next();
								System.out.println();
								productController.findByName(pdName);
								System.out.println("1.구매, 9.뒤로가기");
								menu = sc.next();
								switch (menu) {
								case "1":// ①구매
									purchase(session);
									break;
								case "9":// ⑨뒤로가기
									break;
								}
								break;
							case "2":// 2)전체조회(인기순)
								do {
									productController.findBySold_Cus();
									System.out.println("1.가격오름차순, 2.가격내림차순, 9.뒤로가기");
									menu = sc.next();
									switch (menu) {
									case "1":// ①가격오름차순
										// 가격오름차순//
										productController.findBySold_Cus_Asc();
										System.out.println("1.구매, 9.뒤로가기");
										menu = sc.next();
										switch (menu) {
										case "1":// (1)구매
											purchase(session);
											break;
										case "9":// (9)뒤로가기
										}

										break;
									case "2":// ②가격내림차순
										// 가격내림차순//
										productController.findBySold_Cus_Desc();
										System.out.println("1.구매, 9.뒤로가기");
										menu = sc.next();
										switch (menu) {
										case "1":// (1)구매
											purchase(session);
											break;
										case "9":// (9)뒤로가기
										}
										break;
									}
								} while (!menu.equals("9"));
								menu = "";
								break;

							case "9":// 9)뒤로가기
								break;
							}
						} while (!menu.equals("9"));
						menu = "";
						break;

					case "3":// 3.선물받은내역
						// 선물받은내역//
						System.out.println("--" + id + "님의 선물함--");
						orderController.findByReceiveId(id);
						break;
					case "4":// 4.잔액충전
						// 잔액충전//
						System.out.println("==========잔액충전==========");
						System.out.println("충전하실 금액을 입력해 주십시오.");
						int money = sc.nextInt();
						accountController.deposit(id, money);
						break;
					case "5":// 5.환불
						do {
							System.out.println("1.상품거래내역조회, 2.환불상품내역, 9.뒤로가기");
							menu = sc.next();
							switch (menu) {
							case "1":// 1)상품거래내역조회
								// 상품거래내역//
								System.out.println("--" + id + "님의 구매목록--");
								orderController.findById(id);
								System.out.println("1.환불, 2.뒤로가기");
								switch (sc.next()) {
								case "1":
									System.out.println("환불하실 상품의 상품번호를 입력해 주십시오.");
									int pd_no = sc.nextInt();
									System.out.println("환불하실 개수를 입력해 주십시오.");
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
							case "9":// 9)뒤로가기
								break;
							}
						} while (!menu.equals("9"));
						menu = "";
						break;
					case "6":// 6.회원정보변경
						System.out.println("--회원 정보 수정하기--");
						System.out.print("비밀번호:");
						String modifyPwd = sc2.next();
						System.out.print("이름:");
						String modifyName = sc.next();
						System.out.print("나이:");
						int modifyAge = sc.nextInt();

						System.out.print("성별[M/F]:");
						String modifyGender = sc.next();
						System.out.print("전화번호: ");
						String modifyPhoneNo = sc2.next();
						System.out.println("계좌비밀번호:");
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
					case "7":// 7.탈퇴
						customerController.withdraw(id);
						return;
					case "9":// 9.로그아웃
						customerController.logout(id);
						session = ss.get(id);
						return;
					}
				}

			} else {
				///////// 탈퇴자메뉴////////////
				System.out.println("아직 탈퇴자 메뉴 없음 ㅅㄱ");
				return;
			}

		}

	}

	// 회원정보변경(수정하기)
	public static void modify(String id) {
	}

	public static void main(String[] args) {
		printMenu();
	}

	public static void purchase(Session session) {
		System.out.println("구매할 상품번호를 입력해 주십시오");
		int pd_no = sc.nextInt();
		System.out.println("수량을 입력해주십시오");
		int quantity = sc.nextInt();
		System.out.println("선물하실 분의 ID를 입력해주세요");
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