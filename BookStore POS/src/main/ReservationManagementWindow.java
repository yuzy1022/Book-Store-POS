package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;


//���� ��ư�� ������ �� ����Ǵ� �̺�Ʈ Ŭ����
class ChgRsvBtnEvent implements ActionListener{
	JTable table;
	String phone;
	ReservationManagementWindow win;
	
	ChgRsvBtnEvent(JTable table, String phone, ReservationManagementWindow win){
		this.table = table;
		this.phone = phone;
		this.win = win;
	}
	
	//�̺�Ʈ ó�� �޼ҵ�
	public void actionPerformed(ActionEvent e) {
		try {
			SqlQuery sql = new SqlQuery();
			sql.updateReservationTable(table, phone);  //DB�� ���൵�� ���̺� ������Ʈ
			JOptionPane.showMessageDialog(win, "������ �Ϸ�Ǿ����ϴ�.");  //�Ϸ�â
			sql.sqlClose();
			win.dispose(); //���� �Ϸ� �� �������â ����
		}
		catch(SQLException ex) {  //sql���� ���â
			JOptionPane.showMessageDialog(win, "DB���ῡ ���� �߽��ϴ�.", "DB���� ����", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}//�̺�Ʈ ó�� �޼ҵ� ��
}//ChgRsvBtnEvent Ŭ���� ��


//window ���� Ŭ����
public class ReservationManagementWindow extends JFrame implements ActionListener{
	String [] buttonName = {"����", "����", "����", "���"};  //��ư�̸� String�迭
	Vector <String> tableHeader = new Vector<String>();
	String [] headerString = {"ISBN", "å ����", "����", "��¥"};  //���̺� ��� String�迭
	Vector <Vector<String>> tableContents = new Vector <Vector <String>>(); 
	JButton [] button = new JButton[4];  //��ư �迭
	JTextField bookSearch;  //ISBN�Է��ʵ�
	MainWindow win;
	JTable table;
	DefaultTableModel model;
	
	//������
	ReservationManagementWindow(MainWindow win){  
		this.win = win;
		setVisible(true);
		setTitle("���� ����");
		setSize(1000, 630);
		JLabel nameLabel = new JLabel(win.mem.name + "�� ���� ����");  //�����Ҷ� Member�� ��ü�� �޾ƿͼ� label�� ����
		nameLabel.setFont(new Font("Arial Rounded MT ����", Font.BOLD, 30));  //���̺� ��Ʈ����
		JPanel topButtonPanel = new JPanel();  //��ư 4�� ����� �г�
		topButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 18, 0));  //��ư �г� ���̾ƿ� ���� �¿��� ��� ����, ���� ���� 18
		

		//���̺�
		for (int i=0; i<headerString.length; i++) {  //���̺� ��� �����
			tableHeader.add(headerString[i]);
		}
		model = new DefaultTableModel(tableContents, tableHeader); //���̺� �������� ����� defaultTableModel�����
		table = new JTable(model); //���� model�� JTable�� �־� ���̺� ����
		table.getColumn("ISBN").setPreferredWidth(100);  //��� ����ũ�� ����
		table.getColumn("å ����").setPreferredWidth(310);
		table.getColumn("����").setPreferredWidth(100);
		table.getColumn("��¥").setPreferredWidth(100);
		JScrollPane tableScroll = new JScrollPane(table);  //���̺� ��Ŭ���� �߰�
		
		
		//���� Ű�е� (numberKey)
		bookSearch = new JTextField();  //ISBN �Է��ʵ� �ʱ�ȭ
		bookSearch.setFont(new Font("Sans-serif", Font.BOLD, 25)); //ISBN �Է��ʵ� ��Ʈ ����
		JPanel numKeyPane = new JPanel();  //Ű�е� ���� �г�
		numKeyPane.setLayout(new GridLayout(4,3));  //�г� ���̾ƿ� ���� (�׸��� ���̾ƿ� 4�� 3��)
		JButton[] num = new JButton[12]; //���� Ű�е� �迭�� ����
		String[] nums = {"1","2","3","4","5","6","7","8","9","���","0","Ȯ��"}; //String ����
		//������ ��ü
				
		for(int i = 0; i <= 11; i++) {  //��ư �ʱ�ȭ, �̺��������� ���
			num[i] = new JButton(nums[i]);
			if(i!=9&&i!=11)
				num[i].addActionListener(new numberKeyEvent(bookSearch, nums[i])); //����Ű �̺�Ʈ ������ ���
			else if(i==9)
				num[i].addActionListener(new ClearJTextField(bookSearch)); //��ҹ�ư action event
			else if(i==11)
				num[i].addActionListener(new numberAccessEvent(this)); //Ȯ�ι�ư action event 
				;
					
			num[i].setFont(new Font("", Font.BOLD, 25));  //��ư ��Ʈ ����
			num[i].setPreferredSize(new Dimension(100,58));  //��ư ũ�� ����
			numKeyPane.add(num[i]); //numKeyPane�� ���� Ű�е� �ø�
		} //Ű�е� ���� ��
		
		//��ư ����
		for(int i = 0; i < buttonName.length; i++) {  //��ư �ʱ�ȭ, �гο� add, ũ��, ��Ʈ ����
			button[i] = new JButton(buttonName[i]);
			topButtonPanel.add(button[i]);
			button[i].setFont(new Font("", Font.BOLD, 12));
			button[i].setPreferredSize(new Dimension(60, 40));
		}
		button[0].addActionListener(this); //���� ��ư�� �̺�Ʈ ������ ���
		button[1].addActionListener(new ChgRsvBtnEvent(table, win.mem.phone, this)); //���� ��ư�� �̺�Ʈ ������ ���
		button[2].addActionListener(new BookCancelEvent(this));  //���� ��ư�� �̺�Ʈ ������ ���
		button[3].addActionListener(this);  //��� ��ư�� �̺�Ʈ ������ ���
		
		
		Container c = getContentPane(); //�����̳� ����
		c.setLayout(null);  //�����̳� ���̾ƿ� ����
		c.add(topButtonPanel);  topButtonPanel.setBounds(655, 20, 500, 50);  //�����̳ʿ� ��ư�г� �߰�, ũ��, ��ġ ���� 
		c.add(tableScroll); tableScroll.setBounds(30,80,610,480);  //�����̳ʿ� ���̺� �߰�, ũ��, ��ġ ���� 
		c.add(bookSearch); bookSearch.setBounds(670, 80,300,50);  //�����̳ʿ� ISBN�Է��ʵ� �߰�, ũ��, ��ġ ���� 
		c.add(numKeyPane); numKeyPane.setBounds(670, 160,300,400);  //�����̳ʿ� Ű�е� �� �߰�, ũ��, ��ġ ���� 
		c.add(nameLabel);  nameLabel.setBounds(30, 0, 500, 80);   //�����̳ʿ� �̸� �߰�, ũ��, ��ġ ���� 
		
		try {
			SqlQuery sql = new SqlQuery();
			sql.searchReservation(model, win.mem.phone);  //
			sql.sqlClose();
		}
		catch(SQLException e) {
			JOptionPane.showMessageDialog(this, "DB���ῡ ���� �߽��ϴ�.", "DB���� ����", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
	}//������ ��
	
	
	//���, ���� ��ư ������ �� �۵��ϴ� �̺�Ʈ ó�� �޼ҵ�
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("���")) this.dispose(); //��ҹ�ư ������ â �ݱ�
		else {  //���Ź�ư ������ Mainwindow�� ���̺� ����� ������ �߰�
			if(table.getRowCount() == 0) {
				JOptionPane.showMessageDialog(this, "������ ������ �����ϴ�.", "����", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			int count = win.model.getRowCount();  //����ȭ�� ���̺� �� ����
			TableWorkHelper.deleteAllRow(win.model); //����ȭ�� ���̺��� ��������� ��� ����
			
			try {
				for(int i=0; i<table.getRowCount(); i++) { //�������â�� ��������� ����ȭ���� ���̺�� �Է�
					SqlQuery sql = new SqlQuery();
					Book book = sql.searchBook(table.getValueAt(i, table.getColumn("ISBN").getModelIndex()).toString());  //�������â ������ isbn���� å ������ �޾Ƽ� book�� ����
					//����, ������ ������ ����
					String quan=table.getValueAt(i, table.getColumn("����").getModelIndex()).toString(), price=Integer.toString(book.price);
					Vector<String> row = new Vector<String>();
					row.add(book.isbn);  //���Ϳ� isbn, ����, ����, ����, �Ѱ��� ������ add
					row.add(book.name);
					row.add(quan);
					row.add(price);
					row.add(Integer.toString(Integer.parseInt(price) * Integer.parseInt(quan)));  //�ش� å�� �� ����
					win.model.addRow(row); //����ȭ�� ���̺� �� �߰�
					win.buyResBook = true;
					sql.sqlClose();
				}
				TableWorkHelper.updateTotalPrice(win);  //������������ �� �����ݾ� ������Ʈ
				this.dispose(); //������� ȭ�� �ݱ�
			}
			catch(SQLException ex) { //sql���� ����ó��
				JOptionPane.showMessageDialog(this, "DB���ῡ ���� �߽��ϴ�.", "DB���� ����", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}//���Ź�ư ������ ������ �ڵ� ��
	}//�̺�Ʈ ó�� �޼ҵ� ��
	
}//ReservationManagementWindow Ŭ���� ��
