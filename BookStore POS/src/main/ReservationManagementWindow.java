package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;


//변경 버튼을 눌렀을 때 실행되는 이벤트 클래스
class ChgRsvBtnEvent implements ActionListener{
	JTable table;
	String phone;
	ReservationManagementWindow win;
	
	ChgRsvBtnEvent(JTable table, String phone, ReservationManagementWindow win){
		this.table = table;
		this.phone = phone;
		this.win = win;
	}
	
	//이벤트 처리 메소드
	public void actionPerformed(ActionEvent e) {
		try {
			SqlQuery sql = new SqlQuery();
			sql.updateReservationTable(table, phone);  //DB의 예약도서 테이블 업데이트
			JOptionPane.showMessageDialog(win, "변경이 완료되었습니다.");  //완료창
			sql.sqlClose();
			win.dispose(); //변경 완료 후 예약관리창 닫음
		}
		catch(SQLException ex) {  //sql에러 경고창
			JOptionPane.showMessageDialog(win, "DB연결에 실패 했습니다.", "DB연결 실패", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}//이벤트 처리 메소드 끝
}//ChgRsvBtnEvent 클래스 끝


//window 띄우는 클래스
public class ReservationManagementWindow extends JFrame implements ActionListener{
	String [] buttonName = {"구매", "변경", "삭제", "취소"};  //버튼이름 String배열
	Vector <String> tableHeader = new Vector<String>();
	String [] headerString = {"ISBN", "책 제목", "수량", "날짜"};  //테이블 헤더 String배열
	Vector <Vector<String>> tableContents = new Vector <Vector <String>>(); 
	JButton [] button = new JButton[4];  //버튼 배열
	JTextField bookSearch;  //ISBN입력필드
	MainWindow win;
	JTable table;
	DefaultTableModel model;
	
	//생성자
	ReservationManagementWindow(MainWindow win){  
		this.win = win;
		setVisible(true);
		setTitle("예약 관리");
		setSize(1000, 630);
		JLabel nameLabel = new JLabel(win.mem.name + "님 예약 정보");  //생성할때 Member형 객체를 받아와서 label을 만듬
		nameLabel.setFont(new Font("Arial Rounded MT 굵게", Font.BOLD, 30));  //레이블 폰트설정
		JPanel topButtonPanel = new JPanel();  //버튼 4개 등록할 패널
		topButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 18, 0));  //버튼 패널 레이아웃 설정 좌에서 우로 정렬, 가로 간격 18
		

		//테이블
		for (int i=0; i<headerString.length; i++) {  //테이블 헤더 만들기
			tableHeader.add(headerString[i]);
		}
		model = new DefaultTableModel(tableContents, tableHeader); //테이블 컨텐츠와 헤더로 defaultTableModel만들기
		table = new JTable(model); //만든 model을 JTable에 넣어 테이블 생성
		table.getColumn("ISBN").setPreferredWidth(100);  //헤더 가로크기 설정
		table.getColumn("책 제목").setPreferredWidth(310);
		table.getColumn("수량").setPreferredWidth(100);
		table.getColumn("날짜").setPreferredWidth(100);
		JScrollPane tableScroll = new JScrollPane(table);  //테이블에 스클롤팬 추가
		
		
		//숫자 키패드 (numberKey)
		bookSearch = new JTextField();  //ISBN 입력필드 초기화
		bookSearch.setFont(new Font("Sans-serif", Font.BOLD, 25)); //ISBN 입력필드 폰트 설정
		JPanel numKeyPane = new JPanel();  //키패드 넣을 패널
		numKeyPane.setLayout(new GridLayout(4,3));  //패널 레이아웃 설정 (그리드 레이아웃 4행 3열)
		JButton[] num = new JButton[12]; //숫자 키패드 배열로 선언
		String[] nums = {"1","2","3","4","5","6","7","8","9","취소","0","확인"}; //String 형태
		//리스너 객체
				
		for(int i = 0; i <= 11; i++) {  //버튼 초기화, 이벤스리스너 등록
			num[i] = new JButton(nums[i]);
			if(i!=9&&i!=11)
				num[i].addActionListener(new numberKeyEvent(bookSearch, nums[i])); //숫자키 이벤트 리스너 등록
			else if(i==9)
				num[i].addActionListener(new ClearJTextField(bookSearch)); //취소버튼 action event
			else if(i==11)
				num[i].addActionListener(new numberAccessEvent(this)); //확인버튼 action event 
				;
					
			num[i].setFont(new Font("", Font.BOLD, 25));  //버튼 폰트 설정
			num[i].setPreferredSize(new Dimension(100,58));  //버튼 크기 설정
			numKeyPane.add(num[i]); //numKeyPane에 숫자 키패드 올림
		} //키패드 설정 끝
		
		//버튼 설정
		for(int i = 0; i < buttonName.length; i++) {  //버튼 초기화, 패널에 add, 크기, 폰트 설정
			button[i] = new JButton(buttonName[i]);
			topButtonPanel.add(button[i]);
			button[i].setFont(new Font("", Font.BOLD, 12));
			button[i].setPreferredSize(new Dimension(60, 40));
		}
		button[0].addActionListener(this); //구매 버튼에 이벤트 리스너 등록
		button[1].addActionListener(new ChgRsvBtnEvent(table, win.mem.phone, this)); //변경 버튼에 이벤트 리스너 등록
		button[2].addActionListener(new BookCancelEvent(this));  //삭제 버튼에 이벤트 리스너 등록
		button[3].addActionListener(this);  //취소 버튼에 이벤트 리스너 등록
		
		
		Container c = getContentPane(); //컨테이너 생성
		c.setLayout(null);  //컨테이너 레이아웃 설정
		c.add(topButtonPanel);  topButtonPanel.setBounds(655, 20, 500, 50);  //컨테이너에 버튼패널 추가, 크기, 위치 설정 
		c.add(tableScroll); tableScroll.setBounds(30,80,610,480);  //컨테이너에 테이블 추가, 크기, 위치 설정 
		c.add(bookSearch); bookSearch.setBounds(670, 80,300,50);  //컨테이너에 ISBN입력필드 추가, 크기, 위치 설정 
		c.add(numKeyPane); numKeyPane.setBounds(670, 160,300,400);  //컨테이너에 키패드 팬 추가, 크기, 위치 설정 
		c.add(nameLabel);  nameLabel.setBounds(30, 0, 500, 80);   //컨테이너에 이름 추가, 크기, 위치 설정 
		
		try {
			SqlQuery sql = new SqlQuery();
			sql.searchReservation(model, win.mem.phone);  //
			sql.sqlClose();
		}
		catch(SQLException e) {
			JOptionPane.showMessageDialog(this, "DB연결에 실패 했습니다.", "DB연결 실패", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
	}//생성자 끝
	
	
	//취소, 구매 버튼 눌렀을 때 작동하는 이벤트 처리 메소드
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("취소")) this.dispose(); //취소버튼 누르면 창 닫기
		else {  //구매버튼 누르면 Mainwindow로 테이블에 예약된 도서들 추가
			if(table.getRowCount() == 0) {
				JOptionPane.showMessageDialog(this, "구매할 도서가 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			int count = win.model.getRowCount();  //메인화면 테이블 행 갯수
			TableWorkHelper.deleteAllRow(win.model); //메인화면 테이블의 도서목록을 모두 삭제
			
			try {
				for(int i=0; i<table.getRowCount(); i++) { //예약관리창의 도서목록을 메인화면의 테이블로 입력
					SqlQuery sql = new SqlQuery();
					Book book = sql.searchBook(table.getValueAt(i, table.getColumn("ISBN").getModelIndex()).toString());  //예약관리창 도서의 isbn으로 책 정보를 받아서 book에 저장
					//수량, 가격을 변수에 저장
					String quan=table.getValueAt(i, table.getColumn("수량").getModelIndex()).toString(), price=Integer.toString(book.price);
					Vector<String> row = new Vector<String>();
					row.add(book.isbn);  //벡터에 isbn, 제목, 수량, 가격, 총가격 순으로 add
					row.add(book.name);
					row.add(quan);
					row.add(price);
					row.add(Integer.toString(Integer.parseInt(price) * Integer.parseInt(quan)));  //해당 책의 총 가격
					win.model.addRow(row); //메인화면 테이블에 행 추가
					win.buyResBook = true;
					sql.sqlClose();
				}
				TableWorkHelper.updateTotalPrice(win);  //메인윈도우의 총 결제금액 업데이트
				this.dispose(); //예약관리 화면 닫기
			}
			catch(SQLException ex) { //sql에러 예외처리
				JOptionPane.showMessageDialog(this, "DB연결에 실패 했습니다.", "DB연결 실패", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}//구매버튼 누를때 실행할 코드 끝
	}//이벤트 처리 메소드 끝
	
}//ReservationManagementWindow 클래스 끝
