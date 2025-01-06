package test2;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Movie2 extends JFrame {
	private static final long serialVersionUID = -1680450831406994493L;
	
	JPanel selec;
	
	Container c = getContentPane();
	Font gainFont = new Font("���� ���", Font.PLAIN, 11);
	Font lostFont = new Font("���� ���", Font.ITALIC, 11);
	int window_width = 800;
	
	// ��ü �г�
	JPanel p_search, p_screen, p_foot;
	JPanel subp_foot_data, subp_foot_button;
	JPanel subp_data_info, subp_data_actor, subp_data_story;
	JPanel subp_info_info;
	// ��ü �г� end

	Connection con = null;
	Statement stmt = null;
	ResultSet rs=null;
	
	// search �г�
	JTextField tf_search;
	// search �г� end

	// screen �г�
	JPanel subp_screen;
	JScrollPane sp_screen;
	ArrayList<JPanel> subp_screen_movie;
	// screen �г� end

	// foot �г�
	// data �г�, info �г�
	JLabel l_info_title;
	JLabel l_info_name, l_info_age;
	JTextField tf_info_name, tf_info_age;
	JLabel l_info_date, l_info_genre;
	JTextField tf_info_date, tf_info_genre;
	JLabel l_info_country, l_info_time;
	JTextField tf_info_country, tf_info_time;
	// info �г� end

	// actor �г�
	JLabel l_actor_title;
	JScrollPane sp_actor_info;
	JTextArea ta_actor_info;
	// actor �г� end

	// story �г�
	JLabel l_story_title;
	JScrollPane sp_story_info;
	JTextArea ta_story_info;
	// story �г� end, data �г� end

	// button �г�
	JButton b_foot_create, b_foot_retouch, b_foot_delete;
	// button �г� end
	// foot �г� end

	public Movie2() {
		setTitle("��ȭ ���� �ý���");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 400);
		setLayout(new BorderLayout());

		p_search = new JPanel();
		p_search.setLayout(new BorderLayout());
		tf_search = new JTextField();
		tf_search.setToolTipText("�˻�");

		p_screen = new JPanel();
		p_screen.setPreferredSize(new Dimension(300, 900));
		p_screen.setLayout(new FlowLayout());
		p_screen.setBorder(new LineBorder(Color.gray, 1, false));
		sp_screen = new JScrollPane();
		sp_screen.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // ���� ��ũ�� ������
		sp_screen.getVerticalScrollBar().setUnitIncrement(16); // ��ũ�� �ӵ�
		subp_screen_movie = new ArrayList<JPanel>();
		
		p_foot = new JPanel();
		p_foot.setLayout(new BorderLayout());
		
		subp_foot_data = new JPanel();
		subp_foot_data.setLayout(new BorderLayout());
		
		///////////////////////////////////
		subp_data_info = new JPanel();
		subp_data_info.setLayout(new BorderLayout());
		
		l_info_title = new JLabel("����");
		l_info_title.setFont(gainFont);
		l_info_title.setHorizontalAlignment(JTextField.CENTER);
		subp_info_info = new JPanel();
		subp_info_info.setLayout(new GridLayout(3, 2));
		l_info_name = new JLabel("�̸�");
		l_info_name.setFont(gainFont);
		l_info_name.setHorizontalAlignment(JTextField.CENTER);
		l_info_age = new JLabel("����(���)");
		l_info_age.setFont(gainFont);
		l_info_age.setHorizontalAlignment(JTextField.CENTER);
		l_info_date = new JLabel("��ó⵵");
		l_info_date.setFont(gainFont);
		l_info_date.setHorizontalAlignment(JTextField.CENTER);
		l_info_genre = new JLabel("�з�(�帣)");
		l_info_genre.setFont(gainFont);
		l_info_genre.setHorizontalAlignment(JTextField.CENTER);
		l_info_country = new JLabel("����");
		l_info_country.setFont(gainFont);
		l_info_country.setHorizontalAlignment(JTextField.CENTER);
		l_info_time = new JLabel("����Ÿ��");
		l_info_time.setFont(gainFont);
		l_info_time.setHorizontalAlignment(JTextField.CENTER);
		tf_info_name = new JTextField(10);
		tf_info_name.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		tf_info_age = new JTextField(10);
		tf_info_age.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		tf_info_date = new JTextField(10);
		tf_info_date.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		tf_info_genre = new JTextField(10);
		tf_info_genre.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		tf_info_country = new JTextField(10);
		tf_info_country.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		tf_info_time = new JTextField(10);
		tf_info_time.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		
		subp_data_info.setPreferredSize(new Dimension(300, 100));

		///////////////////////////////////////
		subp_data_actor = new JPanel();
		subp_data_actor.setLayout(new BorderLayout());
		l_actor_title = new JLabel("����/���");
		l_actor_title.setFont(gainFont);
		l_actor_title.setHorizontalAlignment(JTextField.CENTER);
		ta_actor_info = new JTextArea();
		ta_actor_info.setLineWrap(true);
		sp_actor_info = new JScrollPane(ta_actor_info);
		sp_actor_info.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // ���� ��ũ�� ������
		
		//////////////////////////////////////
		subp_data_story = new JPanel();
		subp_data_story.setLayout(new BorderLayout());
		l_story_title = new JLabel("�ٰŸ�");
		l_story_title.setFont(gainFont);
		l_story_title.setHorizontalAlignment(JTextField.CENTER);
		ta_story_info = new JTextArea();
		ta_story_info.setLineWrap(true);
		sp_story_info = new JScrollPane(ta_story_info);
		sp_story_info.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // ���� ��ũ�� ������
		
		subp_data_story.setPreferredSize(new Dimension(300, 100));
		
		//////////////////////////////////////
		subp_foot_button = new JPanel();
		subp_foot_button.setLayout(new BorderLayout());
		b_foot_create = new JButton("����");
		b_foot_create.setPreferredSize(new Dimension(70, 33));
		b_foot_create.setFont(gainFont);
		b_foot_retouch = new JButton("����");
		b_foot_retouch.setFont(gainFont);
		b_foot_delete = new JButton("����");
		b_foot_delete.setPreferredSize(new Dimension(70, 33));
		b_foot_delete.setFont(gainFont);
		
		//////////////////////////////////////
		
		getCustomer();
		

		// ȭ�� ũ�� ������
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				window_width = c.getWidth();
				float W_story = (window_width-300-70)*0.7f;
				subp_data_info.setPreferredSize(new Dimension(300, 100));
				subp_data_story.setPreferredSize(new Dimension((int)W_story, 100));
				Resize_screen();
			}
		});
		
		// �˻� â
		tf_search.addFocusListener(new FocusListener() {
			String searchHint = "�˻�";
			@Override
			public void focusLost(FocusEvent e) {
				// ��Ŀ���� �Ҿ��� ��,
				if(tf_search.getText().equals("")) {
					tf_search.setText(searchHint);
					tf_search.setFont(lostFont);
					tf_search.setForeground(Color.GRAY);
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// ��Ŀ���� ����� ��,
				if(tf_search.getText().equals(searchHint)) {
					tf_search.setText("");
					tf_search.setFont(lostFont);
					tf_search.setForeground(Color.GRAY);
				}
			}
		});
		
		tf_search.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				// ���� Ű�� ������ ��,
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					System.out.println(tf_search.getText());
					String Search_data = tf_search.getText(); // �Է� ��
					p_screen.removeAll(); // �����͵��� ��� �����.
					Resize_screen(); // ȭ���� ����� �� ũ�� ������
					c.repaint(); // �����͵��� �����, �ٽ� �׸���.
					searchCustomer();
					if(tf_search.getText().equals("")) {
						for(int i=0; i<subp_screen_movie.size(); i++)
							p_screen.add(subp_screen_movie.get(i));
					}
					c.validate();
				}
			}
		});
		
		tf_info_name.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				if(!tf_info_name.getText().equals("")) {
					tf_info_name.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});
		
		// ���� ��ư
		b_foot_create.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(tf_info_name.getText().equals("")) {
					tf_info_name.setBorder(new LineBorder(Color.RED));
					tf_info_name.requestFocus(true);
					JOptionPane.showMessageDialog(null, "�̸��� �Է����ּ���.");
				}
				else {
					Add_movie(tf_info_name.getText());
					addMovie();
					tf_info_name.setText("");
					tf_info_name.requestFocus(true);
					p_screen.add(subp_screen_movie.get(subp_screen_movie.size()-1));
					c.validate();
					
				}
			}
		});
		
		// ���� ��ư
		b_foot_retouch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					makeConnection();
					String s="";
					s="UPDATE movie SET age=";
					s+="'"+tf_info_age.getText()+"',date='"+tf_info_date.getText()+"',genre="
							+ "'"+tf_info_genre.getText()+"',country='"+tf_info_country.getText()+"',runningtime='"+tf_info_time.getText()
							+"',people='"+ta_actor_info.getText()+"',story='"+ta_story_info.getText()+"' WHERE name = '"+tf_info_name.getText()+"'";
					System.out.println(s);
					stmt.executeUpdate(s);

				}catch(SQLException sqle){
					System.out.println(sqle.getMessage());
				}
				disConnection();
				
			}
		});
		
		// ���� ��ư
		b_foot_delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				subp_screen_movie.remove(selec);
				p_screen.remove(selec);
				Resize_screen();
				c.repaint();	
				try {
					makeConnection();
					String s="";
					s="DELETE FROM movie WHERE name='"+tf_info_name.getText()+"';";
					System.out.println(s);
					stmt.executeUpdate(s);
					
				}catch(SQLException sqle){
					System.out.println(sqle.getMessage());
				}
				disConnection();
			}
		});
		
		Add();
		setVisible(true);
	}
	
	void Resize_screen() {
		int SIZE = subp_screen_movie.size(); // ��ü ī�� ��
		int ROW = window_width/101 + 1; // ���ٿ� �ִ� ī�差
		int HEIGHT = (SIZE/ROW + 1) * 196; // ���� ��
		p_screen.setPreferredSize(new Dimension(300, HEIGHT));
	}
	
	public void getCustomer(){
		makeConnection();
		String sql="";
		sql="SELECT * FROM movie";
		try{
			rs=stmt.executeQuery(sql);
			while(rs.next()){
				Add_movie(rs.getString("name"));
			}
		}catch(SQLException sqle){System.out.println("getData: SQL Error");}
		disConnection();
	}
	
	public void searchCustomer(){
		tf_search.requestFocus();
	
		makeConnection();
		String sql="";
		sql="SELECT * FROM movie WHERE name='"+tf_search.getText()+"'";
		System.out.println(sql);
		try{
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				searchPanel(rs.getString("name"));
				tf_info_name.setText(rs.getString("name"));
                tf_info_age.setText(rs.getString("age"));
                tf_info_date.setText(rs.getString("date"));
                tf_info_genre.setText(rs.getString("genre"));
                tf_info_country.setText(rs.getString("country"));
                tf_info_time.setText(rs.getString("runningtime"));
                ta_actor_info.setText(rs.getString("people"));
                ta_story_info.setText(rs.getString("story"));
			}
		}catch(SQLException sqle){System.out.println("isExist: SQL Error");}
		disConnection();
	}

	public void searchPanel(String TITLE) {
		int num = -1;
		for(int i=0; i<subp_screen_movie.size(); i++) {
			Component tt[] = subp_screen_movie.get(i).getComponents();
			JPanel t2 = (JPanel)tt[1];
			System.out.println(t2.getComponent(0));
			JLabel t3 = (JLabel)t2.getComponent(0);
			System.out.println("TITLE: "+TITLE);
			System.out.println("t3: "+t3.getText());
			String aa = TITLE;
			if(aa.equals(t3.getText())) {
				System.out.println("num: "+num);
				num = i;
			}
		}
		
		if(num >= 0)
			p_screen.add(subp_screen_movie.get(num));

	}
	
	public Connection makeConnection(){
	      String url="jdbc:mysql://db.ecys.kr:3306/dpsdy1?autoReconnect=true";
	      String id="dpsdy1";
	      String password="lms4929!";
	      try{
	         Class.forName("com.mysql.cj.jdbc.Driver");
	         System.out.println("����̺� ���� ����");
	         con=DriverManager.getConnection(url, id, password);
	         stmt=con.createStatement();
	         System.out.println("�����ͺ��̽� ���� ����");
	      }catch(ClassNotFoundException e){
	         System.out.println("����̹��� ã�� �� �����ϴ�");
	         e.getStackTrace();
	      }catch(SQLException e){
	         System.out.println("���ῡ �����Ͽ����ϴ�");         
	         e.getStackTrace();
	      }
	      return con;
	   }

	void Add_movie(String TITLE) {
		
		JPanel NAME = new JPanel();
		NAME.setPreferredSize(new Dimension(100, 30));
		NAME.setLayout(new FlowLayout());
		JLabel NAME_TITLE = new JLabel(TITLE);
		NAME.add(NAME_TITLE);
		
		JPanel P = new JPanel();
		P.setPreferredSize(new Dimension(100, 160));
		P.setLayout(new BorderLayout());
		
		JPanel IMG = new JPanel();
		IMG.setPreferredSize(new Dimension(100, 130));
		IMG.setLayout(null);
		IMG.setBackground(Color.LIGHT_GRAY);
		JLabel L_IMG = new JLabel("<html>�̹����� ������<br/>��.</html>");
		L_IMG.setHorizontalAlignment(JTextField.CENTER);
		L_IMG.setBounds(0, 0, 100, 130);
		L_IMG.setToolTipText("���� Ŭ������ �̹��� ����");
		IMG.add(L_IMG);
		
		JFileChooser chooser;
		chooser = new JFileChooser();
		L_IMG.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseExited(MouseEvent e) {
				L_IMG.setBorder(null);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				L_IMG.setBorder(new LineBorder(Color.BLUE));
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// ���� Ŭ�� �Ͽ��� ��
				if(e.getButton() == MouseEvent.BUTTON1) {
					selec = P;
					//DB�κ�
						try {
							makeConnection();
							String s="";
							s="SELECT * FROM movie WHERE name='"+TITLE+"';";
							rs = stmt.executeQuery(s);
							while(rs.next()) {
								tf_info_name.setText(rs.getString("name"));
								tf_info_age.setText(rs.getString("age"));
								tf_info_date.setText(rs.getString("date"));
								tf_info_genre.setText(rs.getString("genre"));
								tf_info_country.setText(rs.getString("country"));
								tf_info_time.setText(rs.getString("runningtime"));
								ta_actor_info.setText(rs.getString("people"));
								ta_story_info.setText(rs.getString("story"));
							}
							
						}catch(SQLException sqle){
							System.out.println(sqle.getMessage());
						}
						disConnection();
				}
				
				// ������ Ŭ�� �Ͽ��� ��
				if(e.getButton() == MouseEvent.BUTTON3) {
					FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
					chooser.setFileFilter(filter);
					
					int ret = chooser.showOpenDialog(null);
					if(ret != JFileChooser.APPROVE_OPTION) {
						JOptionPane.showMessageDialog(null, "������ �������� �ʾҽ��ϴ�", "���", JOptionPane.WARNING_MESSAGE);
						return;
					}
					ImageIcon icon = new ImageIcon(chooser.getSelectedFile().getPath());
					Image img = icon.getImage();
					Image changeImg = img.getScaledInstance(100, 130, Image.SCALE_SMOOTH);	
					L_IMG.setHorizontalAlignment(JTextField.LEFT);
					L_IMG.setIcon(new ImageIcon(changeImg));
				}
			}
		});
		P.add(IMG, "Center");
		P.add(NAME, "South");
		
		subp_screen_movie.add(P);
		Resize_screen();
	}
	
	public void addMovie(){
		try {
			makeConnection();
			String s="";
			s="INSERT INTO movie (name,age,date,genre,country,runningtime,people,story) VALUES ";
			s+="('"+tf_info_name.getText()+"','"+tf_info_age.getText()+"','"+tf_info_date.getText()+"',"
					+ "'"+tf_info_genre.getText()+"','"+tf_info_country.getText()+"',"+tf_info_time.getText()+","
							+ "'"+ta_actor_info.getText()+"','"+ta_story_info.getText()+"')";
			System.out.println(s);
			stmt.executeUpdate(s);

		}catch(SQLException sqle){
			System.out.println(sqle.getMessage());
		}
		disConnection();
	}

	
	public void disConnection() {
		try{
			rs.close();
			stmt.close();
			con.close();
		}catch(SQLException e){System.out.println(e.getMessage());}
	}
	
	public String fromMySQL(String str){
		try{
			if (str != null)
				return new String(str.getBytes("8859_1"),"KSC5601");
			else
				return null;
		} catch (Exception e) {e.printStackTrace();return null;}
	}
	
	void Add() {
		p_search.add(tf_search);
		
		for(int i=0; i<subp_screen_movie.size(); i++)
			p_screen.add(subp_screen_movie.get(i));
		
		sp_screen.setViewportView(p_screen);
		
		subp_info_info.add(l_info_name);
		subp_info_info.add(tf_info_name);
		subp_info_info.add(l_info_age);
		subp_info_info.add(tf_info_age);
		subp_info_info.add(l_info_date);
		subp_info_info.add(tf_info_date);
		subp_info_info.add(l_info_genre);
		subp_info_info.add(tf_info_genre);
		subp_info_info.add(l_info_country);
		subp_info_info.add(tf_info_country);
		subp_info_info.add(l_info_time);
		subp_info_info.add(tf_info_time);
		
		subp_data_info.add(l_info_title, "North");
		subp_data_info.add(subp_info_info, "Center");
		
		subp_data_actor.add(l_actor_title, "North");
		subp_data_actor.add(sp_actor_info, "Center");
		
		subp_data_story.add(l_story_title, "North");
		subp_data_story.add(sp_story_info, "Center");

		subp_foot_data.add(subp_data_info, "West");
		subp_foot_data.add(subp_data_actor, "Center");
		subp_foot_data.add(subp_data_story, "East");
		
		subp_foot_button.add(b_foot_create, "North");
		subp_foot_button.add(b_foot_retouch, "Center");
		subp_foot_button.add(b_foot_delete, "South");
		
		p_foot.add(subp_foot_data, "Center");
		p_foot.add(subp_foot_button, "East");
		
		c.add(p_search, "North");
		c.add(sp_screen, "Center");
		c.add(p_foot, "South");
	}
	
	public static void main(String[] args) {
		new Movie2();
	}
}