package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.table.*;
import java.sql.*;
import java.text.SimpleDateFormat;


//ȸ����ȸ ��ư�� ������ ����Ǵ� �̺�Ʈ Ŭ����
class MemSchBtnEvent implements ActionListener{  
	MainWindow win;
	
	//������
	MemSchBtnEvent(MainWindow win){  
		this.win = win;
	}//������ ��
	
	//�̺�Ʈ ó�� �޼ҵ� (ȸ����ȸ â ���)
	public void actionPerformed(ActionEvent e) {  
		win.clearMemberInfo();  //mainWindow ȸ������ �ʱ�ȭ
		new MemberSearchWindow(win);
	}//�̺�Ʈ ó�� �޼ҵ� ��
} //MemSchBtnEvent Ŭ���� ��



//���� ��ư�� ������ ����Ǵ� Ŭ����
class ReserveBtnEvent implements ActionListener{
	MainWindow win;
	
	//������
	ReserveBtnEvent(MainWindow win){
		this.win = win;
	}
	
	//�̺�Ʈ ó�� �޼ҵ�
	public void actionPerformed(ActionEvent e) {
		if(win.mem != null) {   //ȸ����ȸ�� �Ϸ�� ���� �ϰ��
			new ReservationManagementWindow(win);  //�������â ���
		}
		else { //ȸ����ȸ�� �ȵǾ� ���� ���
			JOptionPane.showMessageDialog(win, "ȸ����ȸ�� ���� ���ּ���.", "����", JOptionPane.ERROR_MESSAGE);  //���â ���
			return;
		}
	}//�̺�Ʈ ó�� �޼ҵ� ��
}//ReserveBtnEvent Ŭ���� ��




//�������� ��ư�� ������ ����Ǵ� �̺�Ʈ Ŭ����
class PaymethBtnEvent implements ActionListener{
	MainWindow mainWin;
	
	//������
	PaymethBtnEvent(MainWindow mainWin){
		this.mainWin = mainWin;
	}
	
	
	//�̺�Ʈ ó�� �޼ҵ�
	public void actionPerformed(ActionEvent e) {
		if(mainWin.table.getRowCount() == 0) {
			JOptionPane.showMessageDialog(mainWin, "������ ������ �����ϴ�.", "����", JOptionPane.ERROR_MESSAGE);
			return;
		}
		else new PaymentWindow(mainWin);
	}
	
}//PayBtnEvent Ŭ���� ��



//������ư�� ������ ����Ǵ� �̺�Ʈ Ŭ����
class PayBtnEvent implements ActionListener{
	MainWindow mainWin;
	
	PayBtnEvent(MainWindow mainWin){
		this.mainWin = mainWin;
	}
	
	//�̺�Ʈ ó�� Ŭ����
	public void actionPerformed(ActionEvent e) {
		if(mainWin.table.getRowCount() == 0) {
			JOptionPane.showMessageDialog(mainWin, "������ ������ �����ϴ�.", "����", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(mainWin.totalPay < mainWin.cardPay + mainWin.pointPay && mainWin.totalPay != 0) {  //ī��� ����Ʈ�� ���� �ݾ��� �� �ݾ׺��� ������
			JOptionPane.showMessageDialog(mainWin, "ī��� ����Ʈ�� ���� �ݾ��� �� �ݾ׺��� �����ϴ�.", "����", JOptionPane.ERROR_MESSAGE);
			return;
		}
		else if(mainWin.totalPay > mainWin.cardPay + mainWin.pointPay + mainWin.cashPay) { //�����ݾ��� ���ڶ��
			JOptionPane.showMessageDialog(mainWin, "���� �ݾ��� �����մϴ�.", "����", JOptionPane.ERROR_MESSAGE);
			return;
		}
		else {
			try {
				SqlQuery sql = new SqlQuery();
				sql.payBook(mainWin);
				sql.sqlClose();
				JOptionPane.showMessageDialog(mainWin, "������ �Ϸ�Ǿ����ϴ�.");  //�Ϸ�â
			}
			catch(SQLException ex) {
				JOptionPane.showMessageDialog(mainWin, "DB���ῡ ���� �߽��ϴ�.", "DB���� ����", JOptionPane.ERROR_MESSAGE);
				System.out.println(ex.getMessage());
				return;
			}
		}
	}
}//PayBtnEvent Ŭ���� ��



//Ȩ��ư �������� ����Ǵ� Ŭ����
class HomeBtnEvent implements ActionListener{
	FirstWindow firstWin;
	MainWindow mainWin;
	
	//������
	HomeBtnEvent(FirstWindow firstWin, MainWindow mainWin){
		this.firstWin = firstWin;
		this.mainWin = mainWin;
	}
	
	//�̺�Ʈ ó�� �޼ҵ�
	public void actionPerformed(ActionEvent e) {
		firstWin.setVisible(true);  //ù ȭ�� ���̰� ��
		mainWin.dispose(); //���� ������ ����
	}
	
}//HomeBtnEvent Ŭ���� ��



//�ð� ������
class Time implements Runnable{ 
	private JLabel timeLabel, payNumLabel;
	int branchNum;
	
	//��¥�� String���� ���� ���ִ� static �޼ҵ�
	static String getDate() {
		long miliseconds = System.currentTimeMillis();
        Date date = new Date(miliseconds);
        return date.toString();
	}//getDate �޼ҵ� ��
	
	//��¥�� �ð��� String���� �������ִ� static �޼ҵ�
	static String getDateForPayNum() {
		//SimpleDateFormat date = new SimpleDateFormat("YYMMDD");
		String s = Time.getDate();
		String date;
		date = s.substring(2,4);
		date += s.substring(5, 7);
		date += s.substring(8);
		return date.toString();
	}//getDateTime �޼ҵ� ��
	
	//������
	public Time(JLabel timeLabel, JLabel payNumLabel, int branchNum) {
		this.timeLabel = timeLabel;
		this.payNumLabel = payNumLabel;
		this.branchNum = branchNum;
	}//������ ��
	
	//run�޼ҵ� (������������ �ð��� ������ȣ�� 1�� ���� �ֽ�ȭ ��)
	public void run() {   
		while(true) {
			int year, month, day, hour, minute, second;
			Calendar cal = Calendar.getInstance();
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH)+1;
			day = cal.get(Calendar.DATE);
			hour = cal.get(Calendar.HOUR_OF_DAY);
			minute = cal.get(Calendar.MINUTE);
			second = cal.get(Calendar.SECOND);
			
			String timeD="", timeH = "", timeM= "", timeS="";
			if(day<10) {
				timeD="0"+day+"�� ";
			}else timeD=day+"�� ";
			if(hour<10) {
				timeH ="0"+hour+"�� ";
			}else timeH =hour+"�� ";
			
			if(minute<10) {
				timeM="0"+minute+"�� ";
			}else timeM=minute+"�� ";
			
			if(second<10) {
				timeS="0"+second+"�� ";
			}else timeS=second+"�� ";
		
			timeLabel.setText(year + "�� "+month+"�� "+timeD +timeH + timeM+ timeS);
			
			try {  //1�ʸ��� ������ȣ�� �ð� �ֽ�ȭ
				SqlQuery sql = new SqlQuery();
				long payNum = sql.searchPayNum(branchNum);
				payNumLabel.setText("������ȣ : " + payNum);
				sql.sqlClose();
				Thread.sleep(1000);
			}
			catch(SQLException e) { //DB���� ���� ����ó��
				JOptionPane.showMessageDialog(null, "DB���ῡ ���� �߽��ϴ�.", "DB���� ����", JOptionPane.ERROR_MESSAGE);
				return;
			}
			catch(Exception e) {}
		}  //while�� ��
	} //run �޼ҵ� ��
}  //�ð� ������ ��



//MainWindow â ��� Ŭ����
public class MainWindow extends JFrame{  
	Container ct;
	Vector <String> tableHeader = new Vector<String>();
	String headerString [] = {"ISBN", "���� ��", "����", "����", "�� ����"};
	Vector <Vector<String>> tableContents = new Vector <Vector <String>>(); 
	JLabel totalPrice = new JLabel("0 ��");   
	JLabel cardPayment = new JLabel("0 ��");
	JLabel pointPayment = new JLabel("0 ��");
	JLabel cashPayment = new JLabel("0 ��");
	JLabel totalPayment = new JLabel("0 ��");
	JLabel change = new JLabel("0 ��");
	JLabel memberInfoLabels [] = new JLabel[3];  //���� ���� ���̺� �迭
	JTextField bookSearch = new JTextField(); //isbn �˻� �ʵ�
	String empNum, empName;  //���, �����̸�
	JTable table;  //J���̺�
	DefaultTableModel model;  //JTable�� ������ table��
	Member mem = null;  
	int totalPay, cardPay, pointPay, cashPay;  //�����ؾ��� �ݾ�, ī�� ���ұݾ�, ����Ʈ ���ұݾ�, ���� ���ұݾ�
	int posNum = 1, branchNum;
	boolean buyResBook = false;
	FirstWindow firstWin;
	
	
	//mainwindow ������
	public MainWindow(String empNum, int branchNum, FirstWindow firstWin) {  
		setTitle("��������");
		setSize(1300,820);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		this.empNum = empNum;  //firstWindow���� �Ű������� �޾ƿ� ����� ����
		this.firstWin = firstWin;
		this.branchNum = branchNum;
		try {
			SqlQuery sql = new SqlQuery(); 
			empName = sql.getEmpName(empNum); //���� �̸��� �޾ƿͼ� empName�� ����
		}
		catch(SQLException e) {
			JOptionPane.showMessageDialog(this, "DB���ῡ ���� �߽��ϴ�.", "DB���� ����", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		
        
		ct = getContentPane();
		ct.setLayout(null);
		
		//��Ʈ ����
		Font searchF = new Font("Sans-serif", Font.BOLD, 45); //�����˻� TEXTFIELD �۾� ũ�� ����
		Font headF = new Font("Sans-Serif", Font.BOLD, 24); //headPane ��Ʈ ����
		Font numF = new Font("", Font.BOLD, 40); //���� Ű�е� ��Ʈ ����
		
		//���� ȭ�� head
		JPanel headPane = new JPanel(); 
		Color hPc = new Color(1,46,103); //headPane Color
		headPane.setBackground(hPc);
		
		JLabel employeeName = new JLabel(empName); //���� ȭ�� ��� �̸�
		JLabel employeeNum = new JLabel(empNum); //���� ȭ�� ��� ��ȣ
		JLabel payNum = new JLabel(); //���� ȭ�� ���� ��ȣ
		ImageIcon logo = new ImageIcon("images/logoS.jpg"); 
		JLabel logoImage = new JLabel(logo); //���� ȭ�� �ΰ�
		JButton homeBtn = new JButton("Ȩ"); //Ȩ ��ư 
		homeBtn.addActionListener(new HomeBtnEvent(firstWin, this)); //Ȩ��ư �̺�Ʈ ������ ���
		JLabel nowTime = new JLabel(); //���� ȭ�� �ð�
		Thread clock = new Thread(new Time(nowTime, payNum, branchNum));  //�ð�Ŭ���� �ʱ�ȭ
		clock.start();//�ð� start
		bookSearch.setFont(searchF); //isbn �˻� �ʵ� ��Ʈ ����
		
		//headPane Component ����
		headPane.setLayout(null); 
		headPane.add(employeeName); headPane.add(employeeNum);
		employeeName.setBounds(60,0,150,38); employeeName.setForeground(Color.white); employeeName.setFont(headF);
		employeeNum.setBounds(0,0,60,38); employeeNum.setForeground(Color.white); employeeNum.setFont(headF);
		headPane.add(payNum); headPane.add(logoImage);
		payNum.setBounds(210,0,270,38); payNum.setForeground(Color.white); payNum.setFont(new Font("Sans-Serif", Font.BOLD, 20));
		logoImage.setBounds(480,0,300,40);
		headPane.add(nowTime); headPane.add(homeBtn); //head�� �߰�
		nowTime.setBounds(810,0,700,38); nowTime.setForeground(Color.white); nowTime.setFont(headF);
		homeBtn.setBounds(1220,0,60,40); homeBtn.setFont(headF); //headPane ����	
		
		//J���̺�
		for (int i=0; i<headerString.length; i++) {
			tableHeader.add(headerString[i]);
		}
		model = new DefaultTableModel(tableContents, tableHeader);
		table = new JTable(model);
		table.getColumn("����").setPreferredWidth(100);
		table.getColumn("����").setPreferredWidth(100);
		table.getColumn("ISBN").setPreferredWidth(100);
		table.getColumn("���� ��").setPreferredWidth(400);
		table.getColumn("�� ����").setPreferredWidth(100);
		JScrollPane tableScroll = new JScrollPane(table);
		tableScroll.setPreferredSize(new Dimension(800,360));
		
		
		//���ų��� (purchaseHistory)
		JPanel purchaPane = new JPanel();
		purchaPane.setLayout(new BorderLayout());
		purchaPane.add(tableScroll, BorderLayout.WEST);
		
		
		//�������� ����(payHistory)
		JLabel totalPriceText = new JLabel("�� �ݾ�");   totalPriceText.setHorizontalAlignment(JLabel.CENTER);   totalPriceText.setFont(new Font("Sans-serif", Font.BOLD, 20));
		JLabel cardPaymentText = new JLabel("ī�� ��� �ݾ�");   cardPaymentText.setHorizontalAlignment(JLabel.CENTER);   cardPaymentText.setFont(new Font("Sans-serif", Font.BOLD, 20));
		JLabel pointPaymentText = new JLabel("����Ʈ ��� �ݾ�");   pointPaymentText.setHorizontalAlignment(JLabel.CENTER);   pointPaymentText.setFont(new Font("Sans-serif", Font.BOLD, 20));
		JLabel cashPaymentText = new JLabel("���� ��� �ݾ�");   cashPaymentText.setHorizontalAlignment(JLabel.CENTER);   cashPaymentText.setFont(new Font("Sans-serif", Font.BOLD, 20));
		JLabel totalPaymentText = new JLabel("���� �ݾ�");   totalPaymentText.setHorizontalAlignment(JLabel.CENTER);   totalPaymentText.setFont(new Font("Sans-serif", Font.BOLD, 20));
		JLabel changeText = new JLabel("�Ž�����");   changeText.setHorizontalAlignment(JLabel.CENTER);   changeText.setFont(new Font("Sans-serif", Font.BOLD, 20));
		totalPrice.setFont(new Font("Sans-serif", Font.BOLD, 25));
		cardPayment.setFont(new Font("Sans-serif", Font.BOLD, 25));
		pointPayment.setFont(new Font("Sans-serif", Font.BOLD, 25));
		cashPayment.setFont(new Font("Sans-serif", Font.BOLD, 25));
		totalPayment.setFont(new Font("Sans-serif", Font.BOLD, 25));
		change.setFont(new Font("Sans-serif", Font.BOLD, 25));
		JPanel paymentPanel = new JPanel();
		paymentPanel.setLayout(new GridLayout(6, 2, 100, 0));
		paymentPanel.add(totalPriceText);  paymentPanel.add(totalPrice);
		paymentPanel.add(cardPaymentText);   paymentPanel.add(cardPayment);
		paymentPanel.add(pointPaymentText);   paymentPanel.add(pointPayment);
		paymentPanel.add(cashPaymentText);   paymentPanel.add(cashPayment);
		paymentPanel.add(totalPaymentText);   paymentPanel.add(totalPayment);
		paymentPanel.add(changeText);   paymentPanel.add(change);
		
		
			
		//ȸ������ (member)
		JPanel memberPane = new JPanel();  //ȸ������ Label�� ��Ƴ��� �г�
		memberPane.setLayout(new BorderLayout());
		
		for(int i=0; i<memberInfoLabels.length; i++) {  //ȸ������ ���̺�� �ʱ�ȭ, ��Ʈ����, add
			memberInfoLabels[i] = new JLabel("");
			memberInfoLabels[i].setFont(new Font("Arial Rounded MT ����", Font.BOLD, 30));
		}
		memberPane.add(memberInfoLabels[0], BorderLayout.NORTH);
		memberPane.add(memberInfoLabels[1], BorderLayout.WEST);
		memberPane.add(memberInfoLabels[2], BorderLayout.SOUTH);
		
		JButton memberSearchButton = new JButton("<HTML>ȸ��<BR>��ȸ</HTML>");  //ȸ����ȸ ��ư
		memberSearchButton.setPreferredSize(new Dimension(120,120));
		memberSearchButton.setFont(headF);
		memberSearchButton.addActionListener(new MemSchBtnEvent(this));  //ȸ����ȸ��ư �̺�Ʈ ������ ���
		
		
		//��ϻ���, ����, ��������, ȸ������ �ʱ�ȭ, �������� �ʱ�ȭ, ����  ��ư (buttons)
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new GridLayout(2, 3, 0, 0));
		String [] btnsName = {"��� ����", "����", "��������", "ȸ������ �ʱ�ȭ", "�������� �ʱ�ȭ", "����"};
		JButton [] btns = new JButton[6];
		for(int i=0; i<btnsName.length; i++) {
			btns[i] = new JButton(btnsName[i]);
			buttonPane.add(btns[i]);
			btns[i].setFont(headF);
		}
		
		btns[0].addActionListener(new BookCancelEvent(this));  //��ϻ��� ��ư �̺�Ʈ ������ ���
		btns[1].addActionListener(new ReserveBtnEvent(this)); //���� ��ư �̺�Ʈ ������ ���
		btns[2].addActionListener(new PaymethBtnEvent(this)); //���� ���� ��ư �̺�Ʈ ������ ���
		btns[3].addActionListener( new ActionListener() {public void actionPerformed(ActionEvent e) { clearMemberInfo(); }} );  //ȸ������ �ʱ�ȭ ��ư �̺�Ʈ ������ ���
		btns[4].addActionListener( new ActionListener() {public void actionPerformed(ActionEvent e) { clearPayment(); }} );  //�������� �ʱ�ȭ ��ư �̺�Ʈ ������ ���
		btns[5].addActionListener(new PayBtnEvent(this)); //���� ��ư�� �̺�Ʈ ������ ���
		
				
		//���� Ű�е� (numberKey)
		JPanel numKeyPane = new JPanel();
		JButton[] num = new JButton[12]; //���� Ű�е� �迭�� ����
		String[] nums = {"1","2","3","4","5","6","7","8","9","���","0","Ȯ��"}; //String ����
		
		for(int i = 0; i <= 11; i++) {
			num[i] = new JButton(nums[i]);
			if(i!=9&&i!=11)
				num[i].addActionListener(new numberKeyEvent(bookSearch, nums[i]));
			else if(i==9)
				num[i].addActionListener(new ClearJTextField(bookSearch)); //��ҹ�ư action event
			else if(i==11)
				num[i].addActionListener(new numberAccessEvent(this)); //Ȯ�ι�ư �̺�Ʈ ������ ���
				;
			
			num[i].setFont(numF);
			num[i].setPreferredSize(new Dimension(143,90));
			numKeyPane.add(num[i]); //numKeyPane�� ���� Ű�е� �ø���, ��ư ũ�� ����
		} //Ű�е� ����
		
		
		//ct�� �ø�
		ct.add(headPane); headPane.setBounds(0,0,1300,40); //headPane
		ct.add(purchaPane); purchaPane.setBounds(0,40,800,360); //���ų���
		ct.add(memberPane); memberPane.setBounds(0,400,660,130); //ȸ������
		ct.add(memberSearchButton); memberSearchButton.setBounds(668, 402, 130, 130);  //ȸ����ȸ ��ư
		ct.add(buttonPane); buttonPane.setBounds(0,535,800,245); //���� ��� ���� ��ư
		ct.add(paymentPanel); paymentPanel.setBounds(800,40,480,300); //payPane
		ct.add(bookSearch); bookSearch.setBounds(800,345,485,50); //bookSearch TextField
		ct.add(numKeyPane); numKeyPane.setBounds(800,400,485,380); //numberKeyPane
		
	}//MainWindow ������ ��
	
	
	//ȸ�� ���� Label�� �ؽ�Ʈ�� �������ִ� �޼ҵ�
	public void setMemberInfo(Member mem) {  //Member�� ��ü�� �Ű����� ����
		this.mem = mem;
		memberInfoLabels[0].setText("�̸� : " + mem.name);
		memberInfoLabels[1].setText("��ȭ��ȣ : " + mem.phone);
		memberInfoLabels[2].setText("����Ʈ : " + mem.point + "��");
	}//setMemberInfo �޼ҵ� ��

	
	//ȸ�������� �ʱ�ȭ ���ִ� �޼ҵ�
	public void clearMemberInfo() {
		mem = null;
		buyResBook = false;
		for(int i=0; i<memberInfoLabels.length; i++) {
			memberInfoLabels[i].setText("");
		}
	}//clearMemberInfo �޼ҵ� ��
	
	
	//���������� �ʱ�ȭ ���ִ� �޼ҵ�
	public void clearPayment() {
		cardPay=0; 
		pointPay=0; 
		cashPay=0;
		updatePayInfo();
	}
	
	
	//���� ���� Label�� ������Ʈ ���ִ� �޼ҵ�
	public void updatePayInfo() {
		int total = cardPay + cashPay + pointPay;
		cardPayment.setText(Integer.toString(cardPay) + " ��");
		cashPayment.setText(Integer.toString(cashPay) + " ��");
		pointPayment.setText(Integer.toString(pointPay) + " ��");
		totalPayment.setText(Integer.toString(total) + " ��");
		if(totalPay < total) { //�����ؾߵ� �ݾ׺��� ���� ���� ���� ���
			change.setText(Integer.toString(total - totalPay) + " ��");
		}
		else change.setText("0 ��");
	}
	


}  //MainWindow Ŭ���� ��


