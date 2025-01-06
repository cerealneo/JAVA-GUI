package chapter;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.function.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Book extends JFrame {
	Container c;
	JTabbedPane tabPane;
	LoanPanel tabloan;
	UserPanel tabuser;
	BookPanel tabbook;

	public Book() {
		setTitle("도서 관리 프로그램");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 400);
		c = getContentPane();

		tabPane = new JTabbedPane();

		tabloan = new LoanPanel();
		tabuser = new UserPanel();
		tabbook = new BookPanel();

		tabPane.addTab("대출", tabloan);
		tabPane.addTab("이용자", tabuser);
		tabPane.addTab("책", tabbook);

		c.add(tabPane);
		setVisible(true);
	}

	public static void main(String[] args) {
		new Book();
	}
}

class LoanPanel extends JPanel {
	String[] header = { "책이름", "ISBN", "전화번호", "대출일", "반납일" };
	JPanel north, sub_north1, sub_north2, center;

	JLabel lphone, lISBN;
	JTextField phone, ISBN;

	JButton checkout, checkin, search;

	JScrollPane scrollpane;
	DefaultTableModel model;
	JTable table;

	Connection con;
	Statement stmt;
	ResultSet rs;
	
	public LoanPanel() {
		display(); // 화면 관련 소스
		startEvent(); // 화면시작시 database에서 대출정보들을 가져온다.
		checkoutEvent(); // 대출버튼 이벤트 소스
		checkinEvent(); // 반납버튼 이벤트 소스
		searchEvent(); // 조회버튼 이벤트 소스
	}

	void startEvent() {
		makeConnection();
		String sql = "SELECT * FROM library, member WHERE library.phone = member.phone";
		try {
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String[] index = new String[5];
				index[0] = rs.getString("name");
				index[1] = rs.getString("isbn");
				index[2] = rs.getString("phone");
				index[3] = rs.getString("loanday");
				index[4] = rs.getString("returnday");
				model.insertRow(0, index);
			}
		} catch (SQLException e) {
			System.out.println("START: SQL Error");
		} finally {
			try {
				rs.close();
				stmt.close();
				con.close();
			} catch (Exception e) {
			}
		}
	}

	void checkoutEvent() {
		checkout.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10)
					checkoutAction();
			}
		});

		checkout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				checkoutAction();
			}
		});
	}

	void checkinEvent() {
		checkin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (ISBN.getText().equals("") || phone.getText().equals(""))
					return; // 필드에 입력안하면 리턴
				
				LocalDateTime date = LocalDateTime.now(); // 오늘 날짜
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 날짜 포맷
				makeConnection(); // 데이터베이스 연결
				String sql = "UPDATE library SET phone = \'010-0000-0000\' WHERE isbn = "+"'"+ISBN.getText()+"';";
				try {
					rs = stmt.executeQuery(sql); // ISBN위치의 전화번호 변경
					
					repaint();
				} catch (SQLException e1) {
					System.out.println("CHECKIN: SQL Error");
				} finally {
					try {
						rs.close();
						stmt.close();
						con.close();
					} catch (Exception e1) {
					}
				}
			}
		});
	}

	void searchEvent() {
		search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (ISBN.getText().equals("") || phone.getText().equals(""))
					return; // 필드에 입력안하면 리턴
				
				LocalDateTime date = LocalDateTime.now(); // 오늘 날짜
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 날짜 포맷
				makeConnection(); // 데이터베이스 연결
				String sql = "SELECT * FROM library, member WHERE isbn = '"+ISBN.getText()+"'"; // 조회하기
				try {
					rs = stmt.executeQuery(sql); // 조회
					
				} catch (SQLException e1) {
					System.out.println("CHECKIN: SQL Error");
				} finally {
					try {
						rs.close();
						stmt.close();
						con.close();
					} catch (Exception e1) {
					}
				}
			}
		});
	}

	void display() {
		setLayout(new BorderLayout());

		lISBN = new JLabel("ISBN");
		lphone = new JLabel("전화번호");
		ISBN = new JTextField();
		phone = new JTextField();

		checkout = new JButton("대출");
		checkin = new JButton("반납");
		search = new JButton("조회");

		model = new DefaultTableModel(null , header);
		table = new JTable(model);
		scrollpane = new JScrollPane(table);
		scrollpane.setPreferredSize(new Dimension(650, 200));

		north = new JPanel();
		sub_north1 = new JPanel();
		sub_north2 = new JPanel();
		center = new JPanel();
		north.setLayout(new BorderLayout());
		sub_north1.setLayout(new GridLayout(2, 1));
		sub_north1.add(lphone).setForeground(Color.BLUE);
		sub_north1.add(phone);
		sub_north1.add(lISBN).setForeground(Color.BLUE);
		sub_north1.add(ISBN);

		sub_north2.add(checkout);
		sub_north2.add(checkin);
		sub_north2.add(search);

		north.add(sub_north1, "North");
		north.add(sub_north2, "Center");
		center.add(scrollpane);

		add(north, "North");
		add(center, "Center");
	}

	void checkoutAction() {
		if (ISBN.getText().equals("") || phone.getText().equals(""))
			return; // 필드에 입력안하면 리턴
		
		LocalDateTime date = LocalDateTime.now(); // 오늘 날짜
		LocalDateTime date2 = date.plusDays(14); // 14일 후 날짜
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 날짜 포맷
		makeConnection(); // 데이터베이스 연결
		String sql = "UPDATE library SET phone = '"+phone.getText()+"', loanday = '"+date.format(formatter)+"', "
				+ "returnday = "+"'"+date2.format(formatter)+"'"+" WHERE isbn = "+"'"+ISBN.getText()+"';";
		String sql2 = "SELECT * FROM library, member WHERE isbn = '"+ISBN.getText()+"'"; // 조회하기
		String[] index = new String[6];
		try {
			rs = stmt.executeQuery(sql); // ISBN위치의 전화번호, 대출, 반납일 변경
			rs = stmt.executeQuery(sql2); // 조회
			while(rs.next()) { // 있으면 테이블에 추가
				index[0] = rs.getString("name"); // 책이름
				index[1] = rs.getString("isbn");
				index[2] = rs.getString("mname");
				index[3] = rs.getString("phone");
				index[4] = rs.getString("loanday");
				index[5] = rs.getString("returnday");
				model.insertRow(0, index); // 테이블에 추가	
			}
		} catch (SQLException e) {
			System.out.println("CHECKOUT: SQL Error");
		} finally {
			try {
				rs.close();
				stmt.close();
				con.close();
			} catch (Exception e) {
			}
		}
	}

	void makeConnection() {
		String url = "jdbc:mysql://db.ecys.kr:3306/dpsdy2?autoReconnect=true";
		String id = "dpsdy1";
		String password = "lms4929!";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("드라이브 적재 성공");
			con = DriverManager.getConnection(url, id, password);
			stmt = con.createStatement();
			System.out.println("데이터베이스 연결 성공");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버를 찾을 수 없습니다");
			e.getStackTrace();
		} catch (SQLException e) {
			System.out.println("연결에 실패하였습니다");
			e.getStackTrace();
		}
	}
}

class UserPanel extends JPanel {
	String[] header = { "이름", "전화번호", "주소", "생년월일" };
	String[][] contents = { { "홍길동", "010-1234-1234", "대연동", "930412" }, { "일지매", "010-1222-1224", "북구", "121112" } };

	JPanel north, sub_north1, sub_north2, center;

	JLabel lname, lphone, laddress, lbirth;
	JTextField name, phone, address, birth;

	JButton signup, retouch, delete, search;

	JScrollPane scrollpane;
	DefaultTableModel model;
	JTable table;

	public UserPanel() {
		setLayout(new BorderLayout());

		lname = new JLabel("이름");
		lphone = new JLabel("전화번호");
		laddress = new JLabel("주소");
		lbirth = new JLabel("생년월일");
		name = new JTextField();
		phone = new JTextField();
		address = new JTextField();
		birth = new JTextField();

		signup = new JButton("등록");
		retouch = new JButton("수정");
		delete = new JButton("삭제");
		search = new JButton("조회");

		model = new DefaultTableModel(contents, header);
		table = new JTable(model);
		scrollpane = new JScrollPane(table);
		scrollpane.setPreferredSize(new Dimension(650, 200));

		north = new JPanel();
		sub_north1 = new JPanel();
		sub_north2 = new JPanel();
		center = new JPanel();
		north.setLayout(new BorderLayout());
		sub_north1.setLayout(new GridLayout(3, 4));
		sub_north1.add(lname);
		sub_north1.add(name);
		sub_north1.add(lphone);
		sub_north1.add(phone);
		sub_north1.add(laddress);
		sub_north1.add(address);
		sub_north1.add(lbirth);
		sub_north1.add(birth);

		JLabel t1 = new JLabel();
		JLabel t2 = new JLabel();
		JLabel t3 = new JLabel();
		JLabel t4 = new JLabel();
		sub_north1.add(t1);
		sub_north1.add(t2);
		sub_north1.add(t3);
		sub_north1.add(t4); // 띄우기

		sub_north2.add(signup);
		sub_north2.add(retouch);
		sub_north2.add(delete);
		sub_north2.add(search);

		north.add(sub_north1, "North");
		north.add(sub_north2, "Center");
		center.add(scrollpane);

		add(north, "North");
		add(center, "Center");

		signup.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10)
					checkoutAction();
			}
		});

		signup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				makeConnection();
				try {
					String s = "";
					s = "INSERT INTO member (phone,name,address,birthday) values ";
					s += "('" + phone.getText() + "','" + name.getText() + "','" + address.getText() + "','"
							+ birth.getText() + "')";
					System.out.println(s);
					stmt.executeUpdate(s);

				} catch (SQLException sqle) {
					System.out.println(sqle.getMessage());
				}
				disConnection();
				checkoutAction();
			}
		});

		retouch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				makeConnection();
				try {
					String s = "";
					s = "UPDATE dpsdy2.member SET name='" + name.getText() + "'" + ",address='" + address.getText()
							+ "',birthday='" + birth.getText() + "' WHERE phone='" + phone.getText() + "'";
					System.out.println(s);
					stmt.executeUpdate(s);

				} catch (SQLException sqle) {
					System.out.println("isExist: SQL Error");
				}
				disConnection();
			}
		});

		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int isDelete = JOptionPane.showConfirmDialog(null, "삭제하시겠습니까 ?");
				System.out.println("DELETE: " + isDelete);
				if (isDelete == 0) {
					makeConnection();
					String sql = "";
					sql = "DELETE FROM member where phone='" + phone.getText() + "'";
					System.out.println(sql);
					try {
						stmt.executeUpdate(sql);
					} catch (SQLException sqle) {
						System.out.println("isExist: DELETE SQL Error");
					}
					disConnection();
				}
				int[] index = table.getSelectedRows(); // 선택한 ROW를 반환
				if (index.length == 0) // 선택이 없을 때, 바로 리턴
					return;

				int message = JOptionPane.showConfirmDialog(null, "정말 삭제하겠습니까?", "경고!", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);
				if (message == JOptionPane.YES_OPTION) {
					for (int i = index.length - 1; i >= 0; i--)
						model.removeRow(index[i]);
				}
			}
		});

		search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				makeConnection();
				String sql = "";
				sql = "SELECT * FROM dpsdy2.member";
				try {
					rs = stmt.executeQuery(sql);
					while (rs.next()) {
						String[] m = { rs.getString("phone"), rs.getString("name"), rs.getString("address"),
								rs.getString("birthday"), rs.getString("overdue") };
						for (int i = 0; i < m.length; i++)
							System.out.print(m[i]);
						System.out.println();
					}
				} catch (SQLException sqle) {
					System.out.println("getData: SQL Error");
				}
				disConnection();
			}
		});
	}

	void checkoutAction() {
		if (name.getText().equals("") || phone.getText().equals("") || address.getText().equals("")
				|| birth.getText().equals(""))
			return;
		String[] index = { name.getText(), phone.getText(), address.getText(), birth.getText() };
		model.insertRow(0, index);

		name.setText("");
		phone.setText("");
		address.setText("");
		birth.setText("");
		name.requestFocus();
	}

	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;

	public Connection makeConnection() {
		String url = "jdbc:mysql://db.ecys.kr:3306/dpsdy2?autoReconnect=true";
		String id = "dpsdy1";
		String password = "lms4929!";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("드라이브 적재 성공");
			con = DriverManager.getConnection(url, id, password);
			stmt = con.createStatement();
			System.out.println("데이터베이스 연결 성공");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버를 찾을 수 없습니다");
			e.getStackTrace();
		} catch (SQLException e) {
			System.out.println("연결에 실패하였습니다");
			e.getStackTrace();
		}
		return con;

	}

	public void disConnection() {
		try {
			 rs.close();
			stmt.close();
			con.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}

class BookPanel extends JPanel {
	String[] header = { "책이름", "분류번호", "출판사", "저자", "ISBN" };
	String[][] contents = { { "시 밤", "0.12", "예담", "하상욱", "1231312124" },
			{ "UI 디자인 교과서", "904.1", "유엑스리뷰", "하라다 히데시", "1414151251" } };

	JPanel north, sub_north1, sub_north2, center;

	JLabel lname, lkdc, lpublisher, lauthor, lisbn;
	JTextField name, kdc, publisher, author, isbn;

	JButton signup, retouch, delete, search;

	JScrollPane scrollpane;
	DefaultTableModel model;
	JTable table;

	public BookPanel() {
		setLayout(new BorderLayout());

		lname = new JLabel("책이름");
		lkdc = new JLabel("분류번호");
		lpublisher = new JLabel("출판사");
		lauthor = new JLabel("저자");
		lisbn = new JLabel("ISBN");
		name = new JTextField();
		kdc = new JTextField();
		publisher = new JTextField();
		author = new JTextField();
		isbn = new JTextField();

		signup = new JButton("등록");
		retouch = new JButton("수정");
		delete = new JButton("삭제");
		search = new JButton("조회");

		model = new DefaultTableModel(contents, header);
		table = new JTable(model);
		scrollpane = new JScrollPane(table);
		scrollpane.setPreferredSize(new Dimension(650, 200));

		north = new JPanel();
		sub_north1 = new JPanel();
		sub_north2 = new JPanel();
		center = new JPanel();
		north.setLayout(new BorderLayout());
		sub_north1.setLayout(new GridLayout(3, 4));
		sub_north1.add(lname);
		sub_north1.add(name);
		sub_north1.add(lkdc);
		sub_north1.add(kdc);
		sub_north1.add(lpublisher);
		sub_north1.add(publisher);
		sub_north1.add(lauthor);
		sub_north1.add(author);
		sub_north1.add(lisbn);
		sub_north1.add(isbn);

		sub_north2.add(signup);
		sub_north2.add(retouch);
		sub_north2.add(delete);
		sub_north2.add(search);

		north.add(sub_north1, "North");
		north.add(sub_north2, "Center");
		center.add(scrollpane);

		add(north, "North");
		add(center, "Center");

		signup.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == 10)
					checkoutAction();
			}
		});

		signup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				makeConnection();
				try {
					String s = "";
					s = "INSERT INTO book (name,number,publisher,writer,loan,isbn) values ";
					s += "('" + name.getText() + "','" + kdc.getText() + "','" + publisher.getText() + "','"
							+ author.getText() + "','null','" + isbn.getText() + "')";
					System.out.println(s);
					stmt.executeUpdate(s);

				} catch (SQLException sqle) {
					System.out.println(sqle.getMessage());
				}
				disConnection();
				checkoutAction();
			}
		});

		retouch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				makeConnection();
				try {
					String s = "";
					s = "UPDATE dpsdy2.book SET name='" + name.getText() + "'" + ",number='" + kdc.getText()
							+ "',publisher='" + publisher.getText() + "',writer='" + author.getText()
							+ "',loan='null' WHERE isbn='" + isbn.getText() + "'";
					System.out.println(s);
					stmt.executeUpdate(s);

				} catch (SQLException sqle) {
					System.out.println("isExist: SQL Error");
				}
				disConnection();
			}
		});

		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int isDelete = JOptionPane.showConfirmDialog(null, "삭제하시겠습니까 ?");
				System.out.println("DELETE: " + isDelete);
				if (isDelete == 0) {
					makeConnection();
					String sql = "";
					sql = "DELETE FROM book where isbn='" + isbn.getText() + "'";
					System.out.println(sql);
					try {
						stmt.executeUpdate(sql);
					} catch (SQLException sqle) {
						System.out.println("isExist: DELETE SQL Error");
					}
					disConnection();
				}
				int[] index = table.getSelectedRows(); // 선택한 ROW를 반환
				if (index.length == 0) // 선택이 없을 때, 바로 리턴
					return;

				int message = JOptionPane.showConfirmDialog(null, "정말 삭제하겠습니까?", "경고!", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);
				if (message == JOptionPane.YES_OPTION) {
					for (int i = index.length - 1; i >= 0; i--)
						model.removeRow(index[i]);
				}
			}
		});

		search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				makeConnection();
				String sql = "";
				sql = "SELECT * FROM dpsdy2.book";
				try {
					rs = stmt.executeQuery(sql);
					while (rs.next()) {
						String[] m = { rs.getString("name"), rs.getString("number"), rs.getString("publisher"),
								rs.getString("writer"), rs.getString("isbn") };
						for (int i = 0; i < m.length; i++)
							System.out.print(m[i]);
						System.out.println();
					}
				} catch (SQLException sqle) {
					System.out.println("getData: SQL Error");
				}
				disConnection();
			}
		});
	}

	void checkoutAction() {
		if (name.getText().equals("") || kdc.getText().equals("") || publisher.getText().equals("")
				|| author.getText().equals("") || isbn.getText().equals(""))
			return;
		String[] index = { name.getText(), kdc.getText(), publisher.getText(), author.getText(), isbn.getText() };
		model.insertRow(0, index);

		name.setText("");
		kdc.setText("");
		publisher.setText("");
		author.setText("");
		isbn.setText("");
		name.requestFocus();
	}

	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;

	public Connection makeConnection() {
		String url = "jdbc:mysql://db.ecys.kr:3306/dpsdy2?autoReconnect=true";
		String id = "dpsdy1";
		String password = "lms4929!";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("드라이브 적재 성공");
			con = DriverManager.getConnection(url, id, password);
			stmt = con.createStatement();
			System.out.println("데이터베이스 연결 성공");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버를 찾을 수 없습니다");
			e.getStackTrace();
		} catch (SQLException e) {
			System.out.println("연결에 실패하였습니다");
			e.getStackTrace();
		}
		return con;

	}

	public void disConnection() {
		try {
			 rs.close();
			stmt.close();
			con.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}