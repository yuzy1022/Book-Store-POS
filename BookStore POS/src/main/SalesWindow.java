package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.SQLException;
import java.util.Vector;

//매출정보 윈도우
public class SalesWindow extends JFrame implements ActionListener{
	DefaultTableModel model;
	JTable table;
	String salesTableHeader [] = {"날짜", "총 매출 금액", "카드 매출 금액", "현금 매출 금액", "포인트 매출 금액"};
	Vector <String> tableHeader = new Vector<String>();  //테이블 헤더에 쓸 벡터
	Vector <Vector<String>> tableContents = new Vector <Vector <String>>();  //테이블 컨텐츠에 쓸 벡터 
	Sales [] sales;
	
	//생성자
	SalesWindow(int branchNum){  //지점 번호 받아옴
		setVisible(true);
		setTitle("매출정보");
		setSize(670, 630);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String branchName;
		
		//테이블
		for (int i=0; i<salesTableHeader.length; i++) {  //테이블 헤더 만들기
			tableHeader.add(salesTableHeader[i]);
		}
		model = new DefaultTableModel(tableContents, tableHeader); //테이블 컨텐츠와 헤더로 defaultTableModel만들기
		table = new JTable(model); //만든 model을 JTable에 넣어 테이블 생성
		table.getColumn("날짜").setPreferredWidth(100);  //칼럼 크기 설정
		table.getColumn("총 매출 금액").setPreferredWidth(140);
		table.getColumn("카드 매출 금액").setPreferredWidth(130);
		table.getColumn("현금 매출 금액").setPreferredWidth(130);
		table.getColumn("포인트 매출 금액").setPreferredWidth(130);
		JScrollPane tableScroll = new JScrollPane(table);  //테이블에 스클롤팬 추가
		
		 //DB에 연결해서 지점 이름과 매출 정보 받아옴
		try {
			SqlQuery sql = new SqlQuery();
			branchName = sql.getBranchName(branchNum);  //지점이름 받아옴
			sales = sql.getSales(); //매출 정보 받아옴
			sql.sqlClose();
		}
		catch(SQLException e) {
			JOptionPane.showMessageDialog(this, "DB연결에 실패 했습니다.", "DB연결 실패", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//테이블에 매출 정보 데이터 추가
		for(int i=sales.length-1; i >= 0; i--) {
			Vector<String> row = new Vector<String>();
			row.add(sales[i].date);
			row.add(Integer.toString(sales[i].totalSales));
			row.add(Integer.toString(sales[i].cardSales));
			row.add(Integer.toString(sales[i].cashSales));
			row.add(Integer.toString(sales[i].pointSales));
			model.addRow(row);
		}
		
		
		//지점이름 Label, 종료 버튼
		Font font = new Font("Arial Rounded MT 굵게", Font.BOLD, 20); //폰트
		JLabel branchNameLabel = new JLabel(branchName + " 매출 정보"); //지점 이름으로 Label 만듬
		JButton btn = new JButton("종료"); //종료 버튼
		branchNameLabel.setFont(font);  //지점 이름 label 폰트 설정
		btn.setFont(font);  //종료 버튼 폰트 설정
		btn.addActionListener(this);  //종료 버튼에 이벤트 리스너 등록
		
		Container c = getContentPane(); //컨테이너 생성
		c.setLayout(null);  //컨테이너 레이아웃 설정
		
		c.add(tableScroll); tableScroll.setBounds(13,100,630,480);  //컨테이너에 테이블 추가, 크기, 위치 설정 
		c.add(branchNameLabel);  branchNameLabel.setBounds(13, 0, 550, 100); //컨테이너에 지점이름 Label 추가, 크기, 위치 설정
		c.add(btn);  btn.setBounds(560, 20, 80, 60); //컨테이너에 종료버튼 추가, 크기, 위치 설정
		
	}//생성자 끝
	
	//종료버튼 이벤트 처리 메소드
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}
}//SalesWindow 클래스 끝
