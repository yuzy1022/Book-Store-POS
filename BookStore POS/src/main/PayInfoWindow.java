package main;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;
import java.sql.*;
import java.util.*;



//검색 버튼 눌렀을 때 실행되는 이벤트 클래스
class SearchBtnEvent implements ActionListener{
	PayInfoWindow win;
	
	SearchBtnEvent(PayInfoWindow win){
		this.win = win;
	}
	
	//이벤트 처리 메소드
	public void actionPerformed(ActionEvent e) {
		String value = win.jtf.getText();
		String kind = win.comboStat;
		win.jtf.setText("");  //텍스트 필드 초기화
		
		if(value.equals("")) {  //텍스트 필드에 아무것도 입력하지 않았으면
			JOptionPane.showMessageDialog(win, "검색어를 입력해 주세요", "오류", JOptionPane.ERROR_MESSAGE);
			return;
		}
		else {  //텍스트 필드에 입력 했다면
			try {
				SqlQuery sql = new SqlQuery();
				PayInfo [] payInfo = sql.getPayInfo(value, kind);  //DB에서 데이터를 가져와 payInfo 배열에 저장

				if(payInfo.length == 0) { //검색된 데이터가 없으면
					JOptionPane.showMessageDialog(win, "해당 정보로 검색된 데이터가 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
					return;
				}
				TableWorkHelper.deleteAllRow(win.model);  //테이블 초기화
				
				//테이블에 데이터 삽입
				for(int i=0; i < payInfo.length; i++) {
					Vector<String> row = new Vector<String>();
					row.add(payInfo[i].date);
					row.add(Long.toString(payInfo[i].payNum));
					row.add(Integer.toString(payInfo[i].totalPay));
					row.add(Integer.toString(payInfo[i].cardPay));
					row.add(Integer.toString(payInfo[i].cashPay));
					row.add(Integer.toString(payInfo[i].pointPay));
					row.add(Integer.toString(payInfo[i].addPoint));
					row.add(payInfo[i].phone);
					row.add(Integer.toString(payInfo[i].empNum));
					win.model.addRow(row);
				}
				sql.sqlClose();
			}
			catch(SQLException ex) {
				JOptionPane.showMessageDialog(win, "DB연결에 실패 했습니다.", "DB연결 실패", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}//이벤트 처리 메소드 끝
}//SearchBtnEvent 클래스 끝



//결제정보 윈도우
public class PayInfoWindow extends JFrame implements ActionListener{
	DefaultTableModel model;
	JTable table;
	String salesTableHeader [] = {"날짜", "결제 번호", "총 결제 금액", "카드 결제 금액", "현금 결제 금액", "포인트 결제 금액", "적립금", "휴대폰 번호", "사번"};
	String comboString [] = {"날짜", "결제번호", "휴대폰 번호", "사번"};
	String branchName;
	Vector <String> tableHeader = new Vector<String>();  //테이블 헤더에 쓸 벡터
	Vector <Vector<String>> tableContents = new Vector <Vector <String>>();  //테이블 컨텐츠에 쓸 벡터 
	JTextField jtf;
	JComboBox combo;
	String comboStat="날짜";
	JButton homeBtn;
	FirstWindow firstWin;
	
	//생성자
	PayInfoWindow(FirstWindow firstWin){ //메인윈도우 받아옴
		setVisible(true);
		setTitle("결제정보");
		setSize(1040, 700);
		this.firstWin = firstWin;
		
		
		//테이블
		for (int i=0; i<salesTableHeader.length; i++) {  //테이블 헤더 만들기
			tableHeader.add(salesTableHeader[i]);
		}
		model = new DefaultTableModel(tableContents, tableHeader); //테이블 컨텐츠와 헤더로 defaultTableModel만들기
		table = new JTable(model); //만든 model을 JTable에 넣어 테이블 생성
		table.getColumn("날짜").setPreferredWidth(80);  //칼럼 크기 설정
		table.getColumn("결제 번호").setPreferredWidth(160);
		table.getColumn("총 결제 금액").setPreferredWidth(100);
		table.getColumn("카드 결제 금액").setPreferredWidth(100);
		table.getColumn("현금 결제 금액").setPreferredWidth(100);
		table.getColumn("포인트 결제 금액").setPreferredWidth(100);
		table.getColumn("적립금").setPreferredWidth(80);
		table.getColumn("휴대폰 번호").setPreferredWidth(200);
		table.getColumn("사번").setPreferredWidth(80);
		JScrollPane tableScroll = new JScrollPane(table);  //테이블에 스클롤팬 추가
		
		
		//DB연결해서 지점 이름 받아옴
		try {
			SqlQuery sql = new SqlQuery();
			branchName = sql.getBranchName(firstWin.branchNum);  //지점이름 받아옴
			sql.sqlClose();
		}
		catch(SQLException e) {
			JOptionPane.showMessageDialog(this, "DB연결에 실패 했습니다.", "DB연결 실패", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		
		//버튼, 텍스트필드, 콤보박스, 지점이름 Label 생성
		JButton searchBtn = new JButton("검색");
		homeBtn = new JButton("홈");
		jtf = new JTextField(20);
		JLabel branchNameLabel = new JLabel(branchName);
		combo = new JComboBox(comboString);  //콤보박스 생성
		combo.setSelectedIndex(0);  //콤보박스 기본값 0으로 설정
		
		
		//이벤트 리스너 등록
		combo.addActionListener(this);  //콤보박스 이벤트 리스너 등록
		jtf.addKeyListener(new KeyAdapter() {  //텍스트 필드 문자수 제한 이벤트 리스너 등록
			public void keyTyped(KeyEvent k) {
				int length = ((JTextField)k.getSource()).getText().length();  //텍스트 필드의 문자 갯수를 가져옴
				
				//텍스트 필드 길이 제한
				if(length > 12 && combo.getSelectedItem().equals("휴대폰 번호") ) k.consume();
				else if(length > 9 && combo.getSelectedItem().equals("날짜")) k.consume();
				else if(length > 3 && combo.getSelectedItem().equals("사번")) k.consume();
				else if(length > 13 && combo.getSelectedItem().equals("결제번호")) k.consume();
			}
		});
		searchBtn.addActionListener(new SearchBtnEvent(this));  //검색 버튼에 이벤트 리스너 등록
		homeBtn.addActionListener(this); //홈버튼에 이벤트 리스너 등록

		
		//폰트설정
		Font font = new Font("Arial Rounded MT 굵게", Font.BOLD, 15); //폰트
		searchBtn.setFont(font);
		homeBtn.setFont(font);
		jtf.setFont(font);
		combo.setFont(font);
		branchNameLabel.setFont(new Font("Arial Rounded MT 굵게", Font.BOLD, 20));
		
		
		Container c = getContentPane(); //컨테이너 생성
		c.setLayout(null);  //컨테이너 레이아웃 설정
		c.add(tableScroll); tableScroll.setBounds(12, 108, 1000, 540);  //컨테이너에 테이블 추가, 크기, 위치 설정 
		c.add(branchNameLabel);  branchNameLabel.setBounds(12, 0, 500, 108); //컨테이너에 지점 Lable 추가, 크기, 위치 설정 
		c.add(combo); combo.setBounds(512, 42, 110, 25); //컨테이너에 콤보박스 추가, 크기, 위치 설정  
		c.add(jtf);  jtf.setBounds(632, 41, 150, 28); //컨테이너에 텍스트필드 추가, 크기, 위치 설정 
		c.add(searchBtn);  searchBtn.setBounds(792, 34, 70, 40); //컨테이너에 검색 버튼 추가, 크기, 위치 설정 
		c.add(homeBtn);  homeBtn.setBounds(872, 34, 70, 40); //컨테이너에 홈 버튼 추가, 크기, 위치 설정 
		
	}//생성자 끝
	
	
	//콤보박스, 홈버튼 이벤트 처리 메소드
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == combo) { //콤보박스 선택 이벤트면
			jtf.setText("");  //텍스트 필드 비우기
			comboStat = combo.getSelectedItem().toString();  //comboStat 값 업데이트
		}
		else if(e.getSource() == homeBtn) { //홈버튼 클릭하면
			firstWin.setVisible(true); //첫화면 띄우기
			this.dispose();  //결제정보창 닫기
		}
	}//콤보박스 취소버튼 이벤트 처리 메소드 끝

}//PayInfoWindow 클래스 끝
