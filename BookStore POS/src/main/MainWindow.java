package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.table.*;
import java.sql.*;
import java.text.SimpleDateFormat;


//회원조회 버튼을 누르면 실행되는 이벤트 클래스
class MemSchBtnEvent implements ActionListener{  
	MainWindow win;
	
	//생성자
	MemSchBtnEvent(MainWindow win){  
		this.win = win;
	}//생성자 끝
	
	//이벤트 처리 메소드 (회원조회 창 띄움)
	public void actionPerformed(ActionEvent e) {  
		win.clearMemberInfo();  //mainWindow 회원정보 초기화
		new MemberSearchWindow(win);
	}//이벤트 처리 메소드 끝
} //MemSchBtnEvent 클래스 끝



//예약 버튼을 누르면 실행되는 클래스
class ReserveBtnEvent implements ActionListener{
	MainWindow win;
	
	//생성자
	ReserveBtnEvent(MainWindow win){
		this.win = win;
	}
	
	//이벤트 처리 메소드
	public void actionPerformed(ActionEvent e) {
		if(win.mem != null) {   //회원조회가 완료된 상태 일경우
			new ReservationManagementWindow(win);  //예약관리창 띄움
		}
		else { //회원조회가 안되어 있을 경우
			JOptionPane.showMessageDialog(win, "회원조회를 먼저 해주세요.", "오류", JOptionPane.ERROR_MESSAGE);  //경고창 띄움
			return;
		}
	}//이벤트 처리 메소드 끝
}//ReserveBtnEvent 클래스 끝




//결제수단 버튼을 누르면 실행되는 이벤트 클래스
class PaymethBtnEvent implements ActionListener{
	MainWindow mainWin;
	
	//생성자
	PaymethBtnEvent(MainWindow mainWin){
		this.mainWin = mainWin;
	}
	
	
	//이벤트 처리 메소드
	public void actionPerformed(ActionEvent e) {
		if(mainWin.table.getRowCount() == 0) {
			JOptionPane.showMessageDialog(mainWin, "구매할 도서가 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
			return;
		}
		else new PaymentWindow(mainWin);
	}
	
}//PayBtnEvent 클래스 끝



//결제버튼을 누르면 실행되는 이벤트 클래스
class PayBtnEvent implements ActionListener{
	MainWindow mainWin;
	
	PayBtnEvent(MainWindow mainWin){
		this.mainWin = mainWin;
	}
	
	//이벤트 처리 클래스
	public void actionPerformed(ActionEvent e) {
		if(mainWin.table.getRowCount() == 0) {
			JOptionPane.showMessageDialog(mainWin, "구매할 도서가 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(mainWin.totalPay < mainWin.cardPay + mainWin.pointPay && mainWin.totalPay != 0) {  //카드와 포인트를 합한 금액이 총 금액보다 많으면
			JOptionPane.showMessageDialog(mainWin, "카드와 포인트를 합한 금액이 총 금액보다 많습니다.", "오류", JOptionPane.ERROR_MESSAGE);
			return;
		}
		else if(mainWin.totalPay > mainWin.cardPay + mainWin.pointPay + mainWin.cashPay) { //결제금액이 모자라면
			JOptionPane.showMessageDialog(mainWin, "결제 금액이 부족합니다.", "오류", JOptionPane.ERROR_MESSAGE);
			return;
		}
		else {
			try {
				SqlQuery sql = new SqlQuery();
				sql.payBook(mainWin);
				sql.sqlClose();
				JOptionPane.showMessageDialog(mainWin, "결제가 완료되었습니다.");  //완료창
			}
			catch(SQLException ex) {
				JOptionPane.showMessageDialog(mainWin, "DB연결에 실패 했습니다.", "DB연결 실패", JOptionPane.ERROR_MESSAGE);
				System.out.println(ex.getMessage());
				return;
			}
		}
	}
}//PayBtnEvent 클래스 끝



//홈버튼 눌렀을때 실행되는 클래스
class HomeBtnEvent implements ActionListener{
	FirstWindow firstWin;
	MainWindow mainWin;
	
	//생성자
	HomeBtnEvent(FirstWindow firstWin, MainWindow mainWin){
		this.firstWin = firstWin;
		this.mainWin = mainWin;
	}
	
	//이벤트 처리 메소드
	public void actionPerformed(ActionEvent e) {
		firstWin.setVisible(true);  //첫 화면 보이게 함
		mainWin.dispose(); //메인 윈도우 닫음
	}
	
}//HomeBtnEvent 클래스 끝



//시계 스레드
class Time implements Runnable{ 
	private JLabel timeLabel, payNumLabel;
	int branchNum;
	
	//날짜를 String으로 리턴 해주는 static 메소드
	static String getDate() {
		long miliseconds = System.currentTimeMillis();
        Date date = new Date(miliseconds);
        return date.toString();
	}//getDate 메소드 끝
	
	//날짜와 시간을 String으로 리턴해주는 static 메소드
	static String getDateForPayNum() {
		//SimpleDateFormat date = new SimpleDateFormat("YYMMDD");
		String s = Time.getDate();
		String date;
		date = s.substring(2,4);
		date += s.substring(5, 7);
		date += s.substring(8);
		return date.toString();
	}//getDateTime 메소드 끝
	
	//생성자
	public Time(JLabel timeLabel, JLabel payNumLabel, int branchNum) {
		this.timeLabel = timeLabel;
		this.payNumLabel = payNumLabel;
		this.branchNum = branchNum;
	}//생성자 끝
	
	//run메소드 (메인윈도우의 시간과 결제번호를 1초 마다 최신화 함)
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
				timeD="0"+day+"일 ";
			}else timeD=day+"일 ";
			if(hour<10) {
				timeH ="0"+hour+"시 ";
			}else timeH =hour+"시 ";
			
			if(minute<10) {
				timeM="0"+minute+"분 ";
			}else timeM=minute+"분 ";
			
			if(second<10) {
				timeS="0"+second+"초 ";
			}else timeS=second+"초 ";
		
			timeLabel.setText(year + "년 "+month+"월 "+timeD +timeH + timeM+ timeS);
			
			try {  //1초마다 결제번호와 시간 최신화
				SqlQuery sql = new SqlQuery();
				long payNum = sql.searchPayNum(branchNum);
				payNumLabel.setText("결제번호 : " + payNum);
				sql.sqlClose();
				Thread.sleep(1000);
			}
			catch(SQLException e) { //DB연결 실패 예외처리
				JOptionPane.showMessageDialog(null, "DB연결에 실패 했습니다.", "DB연결 실패", JOptionPane.ERROR_MESSAGE);
				return;
			}
			catch(Exception e) {}
		}  //while문 끝
	} //run 메소드 끝
}  //시계 스레드 끝



//MainWindow 창 띄울 클래스
public class MainWindow extends JFrame{  
	Container ct;
	Vector <String> tableHeader = new Vector<String>();
	String headerString [] = {"ISBN", "도서 명", "수량", "가격", "총 가격"};
	Vector <Vector<String>> tableContents = new Vector <Vector <String>>(); 
	JLabel totalPrice = new JLabel("0 원");   
	JLabel cardPayment = new JLabel("0 원");
	JLabel pointPayment = new JLabel("0 원");
	JLabel cashPayment = new JLabel("0 원");
	JLabel totalPayment = new JLabel("0 원");
	JLabel change = new JLabel("0 원");
	JLabel memberInfoLabels [] = new JLabel[3];  //직원 정보 레이블 배열
	JTextField bookSearch = new JTextField(); //isbn 검색 필드
	String empNum, empName;  //사번, 직원이름
	JTable table;  //J테이블
	DefaultTableModel model;  //JTable에 적용할 table모델
	Member mem = null;  
	int totalPay, cardPay, pointPay, cashPay;  //지불해야할 금액, 카드 지불금액, 포인트 지불금액, 현금 지불금액
	int posNum = 1, branchNum;
	boolean buyResBook = false;
	FirstWindow firstWin;
	
	
	//mainwindow 생성자
	public MainWindow(String empNum, int branchNum, FirstWindow firstWin) {  
		setTitle("명지문고");
		setSize(1300,820);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		this.empNum = empNum;  //firstWindow에서 매개변수로 받아온 사번을 저장
		this.firstWin = firstWin;
		this.branchNum = branchNum;
		try {
			SqlQuery sql = new SqlQuery(); 
			empName = sql.getEmpName(empNum); //직원 이름을 받아와서 empName에 저장
		}
		catch(SQLException e) {
			JOptionPane.showMessageDialog(this, "DB연결에 실패 했습니다.", "DB연결 실패", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		
        
		ct = getContentPane();
		ct.setLayout(null);
		
		//폰트 설정
		Font searchF = new Font("Sans-serif", Font.BOLD, 45); //도서검색 TEXTFIELD 글씨 크기 설정
		Font headF = new Font("Sans-Serif", Font.BOLD, 24); //headPane 폰트 설정
		Font numF = new Font("", Font.BOLD, 40); //숫자 키패드 폰트 설정
		
		//메인 화면 head
		JPanel headPane = new JPanel(); 
		Color hPc = new Color(1,46,103); //headPane Color
		headPane.setBackground(hPc);
		
		JLabel employeeName = new JLabel(empName); //메인 화면 사원 이름
		JLabel employeeNum = new JLabel(empNum); //메인 화면 사원 번호
		JLabel payNum = new JLabel(); //메인 화면 결제 번호
		ImageIcon logo = new ImageIcon("images/logoS.jpg"); 
		JLabel logoImage = new JLabel(logo); //메인 화면 로고
		JButton homeBtn = new JButton("홈"); //홈 버튼 
		homeBtn.addActionListener(new HomeBtnEvent(firstWin, this)); //홈버튼 이벤트 리스너 등록
		JLabel nowTime = new JLabel(); //메인 화면 시간
		Thread clock = new Thread(new Time(nowTime, payNum, branchNum));  //시계클래스 초기화
		clock.start();//시계 start
		bookSearch.setFont(searchF); //isbn 검색 필드 폰트 설정
		
		//headPane Component 설정
		headPane.setLayout(null); 
		headPane.add(employeeName); headPane.add(employeeNum);
		employeeName.setBounds(60,0,150,38); employeeName.setForeground(Color.white); employeeName.setFont(headF);
		employeeNum.setBounds(0,0,60,38); employeeNum.setForeground(Color.white); employeeNum.setFont(headF);
		headPane.add(payNum); headPane.add(logoImage);
		payNum.setBounds(210,0,270,38); payNum.setForeground(Color.white); payNum.setFont(new Font("Sans-Serif", Font.BOLD, 20));
		logoImage.setBounds(480,0,300,40);
		headPane.add(nowTime); headPane.add(homeBtn); //head에 추가
		nowTime.setBounds(810,0,700,38); nowTime.setForeground(Color.white); nowTime.setFont(headF);
		homeBtn.setBounds(1220,0,60,40); homeBtn.setFont(headF); //headPane 설정	
		
		//J테이블
		for (int i=0; i<headerString.length; i++) {
			tableHeader.add(headerString[i]);
		}
		model = new DefaultTableModel(tableContents, tableHeader);
		table = new JTable(model);
		table.getColumn("수량").setPreferredWidth(100);
		table.getColumn("가격").setPreferredWidth(100);
		table.getColumn("ISBN").setPreferredWidth(100);
		table.getColumn("도서 명").setPreferredWidth(400);
		table.getColumn("총 가격").setPreferredWidth(100);
		JScrollPane tableScroll = new JScrollPane(table);
		tableScroll.setPreferredSize(new Dimension(800,360));
		
		
		//구매내역 (purchaseHistory)
		JPanel purchaPane = new JPanel();
		purchaPane.setLayout(new BorderLayout());
		purchaPane.add(tableScroll, BorderLayout.WEST);
		
		
		//결제내역 설정(payHistory)
		JLabel totalPriceText = new JLabel("총 금액");   totalPriceText.setHorizontalAlignment(JLabel.CENTER);   totalPriceText.setFont(new Font("Sans-serif", Font.BOLD, 20));
		JLabel cardPaymentText = new JLabel("카드 사용 금액");   cardPaymentText.setHorizontalAlignment(JLabel.CENTER);   cardPaymentText.setFont(new Font("Sans-serif", Font.BOLD, 20));
		JLabel pointPaymentText = new JLabel("포인트 사용 금액");   pointPaymentText.setHorizontalAlignment(JLabel.CENTER);   pointPaymentText.setFont(new Font("Sans-serif", Font.BOLD, 20));
		JLabel cashPaymentText = new JLabel("현금 사용 금액");   cashPaymentText.setHorizontalAlignment(JLabel.CENTER);   cashPaymentText.setFont(new Font("Sans-serif", Font.BOLD, 20));
		JLabel totalPaymentText = new JLabel("받은 금액");   totalPaymentText.setHorizontalAlignment(JLabel.CENTER);   totalPaymentText.setFont(new Font("Sans-serif", Font.BOLD, 20));
		JLabel changeText = new JLabel("거스름돈");   changeText.setHorizontalAlignment(JLabel.CENTER);   changeText.setFont(new Font("Sans-serif", Font.BOLD, 20));
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
		
		
			
		//회원정보 (member)
		JPanel memberPane = new JPanel();  //회원정보 Label을 담아놓을 패널
		memberPane.setLayout(new BorderLayout());
		
		for(int i=0; i<memberInfoLabels.length; i++) {  //회원정보 레이블들 초기화, 폰트설정, add
			memberInfoLabels[i] = new JLabel("");
			memberInfoLabels[i].setFont(new Font("Arial Rounded MT 굵게", Font.BOLD, 30));
		}
		memberPane.add(memberInfoLabels[0], BorderLayout.NORTH);
		memberPane.add(memberInfoLabels[1], BorderLayout.WEST);
		memberPane.add(memberInfoLabels[2], BorderLayout.SOUTH);
		
		JButton memberSearchButton = new JButton("<HTML>회원<BR>조회</HTML>");  //회원조회 버튼
		memberSearchButton.setPreferredSize(new Dimension(120,120));
		memberSearchButton.setFont(headF);
		memberSearchButton.addActionListener(new MemSchBtnEvent(this));  //회원조회버튼 이벤트 리스너 등록
		
		
		//목록삭제, 예약, 결제수단, 회원정보 초기화, 결제정보 초기화, 결제  버튼 (buttons)
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new GridLayout(2, 3, 0, 0));
		String [] btnsName = {"목록 삭제", "예약", "결제수단", "회원정보 초기화", "결제정보 초기화", "결제"};
		JButton [] btns = new JButton[6];
		for(int i=0; i<btnsName.length; i++) {
			btns[i] = new JButton(btnsName[i]);
			buttonPane.add(btns[i]);
			btns[i].setFont(headF);
		}
		
		btns[0].addActionListener(new BookCancelEvent(this));  //목록삭제 버튼 이벤트 리스너 등록
		btns[1].addActionListener(new ReserveBtnEvent(this)); //예약 버튼 이벤트 리스너 등록
		btns[2].addActionListener(new PaymethBtnEvent(this)); //결제 수단 버튼 이벤트 리스너 등록
		btns[3].addActionListener( new ActionListener() {public void actionPerformed(ActionEvent e) { clearMemberInfo(); }} );  //회원정보 초기화 버튼 이벤트 리스너 등록
		btns[4].addActionListener( new ActionListener() {public void actionPerformed(ActionEvent e) { clearPayment(); }} );  //결제정보 초기화 버튼 이벤트 리스너 등록
		btns[5].addActionListener(new PayBtnEvent(this)); //결제 버튼에 이벤트 리스너 등록
		
				
		//숫자 키패드 (numberKey)
		JPanel numKeyPane = new JPanel();
		JButton[] num = new JButton[12]; //숫자 키패드 배열로 선언
		String[] nums = {"1","2","3","4","5","6","7","8","9","취소","0","확인"}; //String 형태
		
		for(int i = 0; i <= 11; i++) {
			num[i] = new JButton(nums[i]);
			if(i!=9&&i!=11)
				num[i].addActionListener(new numberKeyEvent(bookSearch, nums[i]));
			else if(i==9)
				num[i].addActionListener(new ClearJTextField(bookSearch)); //취소버튼 action event
			else if(i==11)
				num[i].addActionListener(new numberAccessEvent(this)); //확인버튼 이벤트 리스너 등록
				;
			
			num[i].setFont(numF);
			num[i].setPreferredSize(new Dimension(143,90));
			numKeyPane.add(num[i]); //numKeyPane에 숫자 키패드 올리고, 버튼 크기 설정
		} //키패드 지정
		
		
		//ct에 올림
		ct.add(headPane); headPane.setBounds(0,0,1300,40); //headPane
		ct.add(purchaPane); purchaPane.setBounds(0,40,800,360); //구매내역
		ct.add(memberPane); memberPane.setBounds(0,400,660,130); //회원정보
		ct.add(memberSearchButton); memberSearchButton.setBounds(668, 402, 130, 130);  //회원조회 버튼
		ct.add(buttonPane); buttonPane.setBounds(0,535,800,245); //결제 취소 예약 버튼
		ct.add(paymentPanel); paymentPanel.setBounds(800,40,480,300); //payPane
		ct.add(bookSearch); bookSearch.setBounds(800,345,485,50); //bookSearch TextField
		ct.add(numKeyPane); numKeyPane.setBounds(800,400,485,380); //numberKeyPane
		
	}//MainWindow 생성자 끝
	
	
	//회원 정보 Label의 텍스트를 설정해주는 메소드
	public void setMemberInfo(Member mem) {  //Member형 객체로 매개변수 받음
		this.mem = mem;
		memberInfoLabels[0].setText("이름 : " + mem.name);
		memberInfoLabels[1].setText("전화번호 : " + mem.phone);
		memberInfoLabels[2].setText("포인트 : " + mem.point + "점");
	}//setMemberInfo 메소드 끝

	
	//회원정보를 초기화 해주는 메소드
	public void clearMemberInfo() {
		mem = null;
		buyResBook = false;
		for(int i=0; i<memberInfoLabels.length; i++) {
			memberInfoLabels[i].setText("");
		}
	}//clearMemberInfo 메소드 끝
	
	
	//결제정보를 초기화 해주는 메소드
	public void clearPayment() {
		cardPay=0; 
		pointPay=0; 
		cashPay=0;
		updatePayInfo();
	}
	
	
	//결제 정보 Label을 업데이트 해주는 메소드
	public void updatePayInfo() {
		int total = cardPay + cashPay + pointPay;
		cardPayment.setText(Integer.toString(cardPay) + " 원");
		cashPayment.setText(Integer.toString(cashPay) + " 원");
		pointPayment.setText(Integer.toString(pointPay) + " 원");
		totalPayment.setText(Integer.toString(total) + " 원");
		if(totalPay < total) { //결제해야될 금액보다 받은 돈이 많을 경우
			change.setText(Integer.toString(total - totalPay) + " 원");
		}
		else change.setText("0 원");
	}
	


}  //MainWindow 클래스 끝


