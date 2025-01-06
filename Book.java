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
		setTitle("���� ���� ���α׷�");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 400);
		c = getContentPane();

		tabPane = new JTabbedPane();

		tabloan = new LoanPanel();
		tabuser = new UserPanel();
		tabbook = new BookPanel();

		tabPane.addTab("����", tabloan);
		tabPane.addTab("�̿���", tabuser);
		tabPane.addTab("å", tabbook);

		c.add(tabPane);
		setVisible(true);
	}

	public static void main(String[] args) {
		new Book();
	}
}

class LoanPanel extends JPanel {
	String[] header = { "å�̸�", "ISBN", "��ȭ��ȣ", "������", "�ݳ���" };
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
		display(); // ȭ�� ���� �ҽ�
		startEvent(); // ȭ����۽� database���� ������������ �����´�.
		checkoutEvent(); // �����ư �̺�Ʈ �ҽ�
		checkinEvent(); // �ݳ���ư �̺�Ʈ �ҽ�
		searchEvent(); // ��ȸ��ư �̺�Ʈ �ҽ�
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
					return; // �ʵ忡 �Է¾��ϸ� ����
				
				LocalDateTime date = LocalDateTime.now(); // ���� ��¥
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // ��¥ ����
				makeConnection(); // �����ͺ��̽� ����
				String sql = "UPDATE library SET phone = \'010-0000-0000\' WHERE isbn = "+"'"+ISBN.getText()+"';";
				try {
					rs = stmt.executeQuery(sql); // ISBN��ġ�� ��ȭ��ȣ ����
					
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
					return; // �ʵ忡 �Է¾��ϸ� ����
				
				LocalDateTime date = LocalDateTime.now(); // ���� ��¥
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // ��¥ ����
				makeConnection(); // �����ͺ��̽� ����
				String sql = "SELECT * FROM library, member WHERE isbn = '"+ISBN.getText()+"'"; // ��ȸ�ϱ�
				try {
					rs = stmt.executeQuery(sql); // ��ȸ
					
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
		lphone = new JLabel("��ȭ��ȣ");
		ISBN = new JTextField();
		phone = new JTextField();

		checkout = new JButton("����");
		checkin = new JButton("�ݳ�");
		search = new JButton("��ȸ");

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
			return; // �ʵ忡 �Է¾��ϸ� ����
		
		LocalDateTime date = LocalDateTime.now(); // ���� ��¥
		LocalDateTime date2 = date.plusDays(14); // 14�� �� ��¥
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // ��¥ ����
		makeConnection(); // �����ͺ��̽� ����
		String sql = "UPDATE library SET phone = '"+phone.getText()+"', loanday = '"+date.format(formatter)+"', "
				+ "returnday = "+"'"+date2.format(formatter)+"'"+" WHERE isbn = "+"'"+ISBN.getText()+"';";
		String sql2 = "SELECT * FROM library, member WHERE isbn = '"+ISBN.getText()+"'"; // ��ȸ�ϱ�
		String[] index = new String[6];
		try {
			rs = stmt.executeQuery(sql); // ISBN��ġ�� ��ȭ��ȣ, ����, �ݳ��� ����
			rs = stmt.executeQuery(sql2); // ��ȸ
			while(rs.next()) { // ������ ���̺� �߰�
				index[0] = rs.getString("name"); // å�̸�
				index[1] = rs.getString("isbn");
				index[2] = rs.getString("mname");
				index[3] = rs.getString("phone");
				index[4] = rs.getString("loanday");
				index[5] = rs.getString("returnday");
				model.insertRow(0, index); // ���̺� �߰�	
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
			System.out.println("����̺� ���� ����");
			con = DriverManager.getConnection(url, id, password);
			stmt = con.createStatement();
			System.out.println("�����ͺ��̽� ���� ����");
		} catch (ClassNotFoundException e) {
			System.out.println("����̹��� ã�� �� �����ϴ�");
			e.getStackTrace();
		} catch (SQLException e) {
			System.out.println("���ῡ �����Ͽ����ϴ�");
			e.getStackTrace();
		}
	}
}

class UserPanel extends JPanel {
	String[] header = { "�̸�", "��ȭ��ȣ", "�ּ�", "�������" };
	String[][] contents = { { "ȫ�浿", "010-1234-1234", "�뿬��", "930412" }, { "������", "010-1222-1224", "�ϱ�", "121112" } };

	JPanel north, sub_north1, sub_north2, center;

	JLabel lname, lphone, laddress, lbirth;
	JTextField name, phone, address, birth;

	JButton signup, retouch, delete, search;

	JScrollPane scrollpane;
	DefaultTableModel model;
	JTable table;

	public UserPanel() {
		setLayout(new BorderLayout());

		lname = new JLabel("�̸�");
		lphone = new JLabel("��ȭ��ȣ");
		laddress = new JLabel("�ּ�");
		lbirth = new JLabel("�������");
		name = new JTextField();
		phone = new JTextField();
		address = new JTextField();
		birth = new JTextField();

		signup = new JButton("���");
		retouch = new JButton("����");
		delete = new JButton("����");
		search = new JButton("��ȸ");

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
		sub_north1.add(t4); // ����

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
				int isDelete = JOptionPane.showConfirmDialog(null, "�����Ͻðڽ��ϱ� ?");
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
				int[] index = table.getSelectedRows(); // ������ ROW�� ��ȯ
				if (index.length == 0) // ������ ���� ��, �ٷ� ����
					return;

				int message = JOptionPane.showConfirmDialog(null, "���� �����ϰڽ��ϱ�?", "���!", JOptionPane.YES_NO_OPTION,
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
			System.out.println("����̺� ���� ����");
			con = DriverManager.getConnection(url, id, password);
			stmt = con.createStatement();
			System.out.println("�����ͺ��̽� ���� ����");
		} catch (ClassNotFoundException e) {
			System.out.println("����̹��� ã�� �� �����ϴ�");
			e.getStackTrace();
		} catch (SQLException e) {
			System.out.println("���ῡ �����Ͽ����ϴ�");
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
	String[] header = { "å�̸�", "�з���ȣ", "���ǻ�", "����", "ISBN" };
	String[][] contents = { { "�� ��", "0.12", "����", "�ϻ��", "1231312124" },
			{ "UI ������ ������", "904.1", "����������", "�϶�� ������", "1414151251" } };

	JPanel north, sub_north1, sub_north2, center;

	JLabel lname, lkdc, lpublisher, lauthor, lisbn;
	JTextField name, kdc, publisher, author, isbn;

	JButton signup, retouch, delete, search;

	JScrollPane scrollpane;
	DefaultTableModel model;
	JTable table;

	public BookPanel() {
		setLayout(new BorderLayout());

		lname = new JLabel("å�̸�");
		lkdc = new JLabel("�з���ȣ");
		lpublisher = new JLabel("���ǻ�");
		lauthor = new JLabel("����");
		lisbn = new JLabel("ISBN");
		name = new JTextField();
		kdc = new JTextField();
		publisher = new JTextField();
		author = new JTextField();
		isbn = new JTextField();

		signup = new JButton("���");
		retouch = new JButton("����");
		delete = new JButton("����");
		search = new JButton("��ȸ");

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
				int isDelete = JOptionPane.showConfirmDialog(null, "�����Ͻðڽ��ϱ� ?");
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
				int[] index = table.getSelectedRows(); // ������ ROW�� ��ȯ
				if (index.length == 0) // ������ ���� ��, �ٷ� ����
					return;

				int message = JOptionPane.showConfirmDialog(null, "���� �����ϰڽ��ϱ�?", "���!", JOptionPane.YES_NO_OPTION,
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
			System.out.println("����̺� ���� ����");
			con = DriverManager.getConnection(url, id, password);
			stmt = con.createStatement();
			System.out.println("�����ͺ��̽� ���� ����");
		} catch (ClassNotFoundException e) {
			System.out.println("����̹��� ã�� �� �����ϴ�");
			e.getStackTrace();
		} catch (SQLException e) {
			System.out.println("���ῡ �����Ͽ����ϴ�");
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