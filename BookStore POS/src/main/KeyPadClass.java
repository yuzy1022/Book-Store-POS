package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

//텍스트 필드 초기화 하는 이벤트 클래스
class ClearJTextField implements ActionListener{ 
	JTextField bookSearch;
	
	//생성자
	ClearJTextField(JTextField s){
		bookSearch = s;
	}//생성자 끝
	
	//이벤트 처리 메소드 (텍스트 필드 초기화함)
	public void actionPerformed(ActionEvent e) {
		bookSearch.setText("");
	}//이벤트 처리 메소드 끝
}//ClearTextField 클래스 끝



//numberKey의 숫자 버튼 클릭 시 액션 이벤트
class numberKeyEvent implements ActionListener{ 
	JTextField bookSearch; String n;
	
	//생성자
	numberKeyEvent(JTextField s, String n){
		bookSearch = s; this.n =n;
	}//생성자 끝
	
	//이벤트 처리 메소드 (숫자 버튼 클릭하면 텍스트 필드에 값 입력)
	public void actionPerformed(ActionEvent e) {
		String isbn = bookSearch.getText();
		bookSearch.setText(isbn + n);
	}//이벤트 처리 메소드 끝
}//numberKeyEvent 클래스 끝



//키패드의 확인버튼 클릭시 책을 검색하는 이벤트 클래스
class numberAccessEvent implements ActionListener{  
	JTextField bookSearch;
	DefaultTableModel model;
	MainWindow mainWin = null;
	JTable table;
	
	//MainWindow에서 사용할 생성자
	numberAccessEvent(MainWindow mainWin){
		this.bookSearch = mainWin.bookSearch;
		this.model = mainWin.model;
		this.table = mainWin.table;
		this.mainWin = mainWin;
	}  //MainWindow에서 사용할 생성자 끝
	
	
	//예약관리 화면에서 사용할 생성자 
	numberAccessEvent(ReservationManagementWindow resWin){ 
		this.bookSearch = resWin.bookSearch;
		this.model = resWin.model;
		this.table = resWin.table;
	}  //예약관리 화면에서 사용할 생성자 끝
	
	
	//이벤트 처리 메소드
	public void actionPerformed(ActionEvent e) {  
		String isbn = bookSearch.getText(); //입력필드의 isbn을 가져와 저장
		bookSearch.setText("");  //isbn 입력필드 비우기
		if(isbn.equals("")) {
			JOptionPane.showMessageDialog(null, "ISBN을 입력해주세요.", "오류", JOptionPane.ERROR_MESSAGE); //isbn을 입력하지 않았을 경우 경고창 띄움
			return;
		} 
		try {
			SqlQuery sql = new SqlQuery(); 
			Book book = sql.searchBook(isbn);  //입력된 isbn으로 책을 찾아서 Book객체로 리턴 받음
			
			if(book != null) { //입력된 isbn의 책정보를 DB에서 찾았을 경우
				int bookRow = TableWorkHelper.isBookInTable(model, book.isbn); //해당 isbn의 칼럼이 이미 테이블에 있는지 검사하여 행의 인덱스를 bookRow에 저장
				
				if(bookRow >= 0) {  //테이블에 해당 isbn의 행이 있을 경우
					int quan = Integer.parseInt(model.getValueAt(bookRow, table.getColumn("수량").getModelIndex()).toString())+1; //해당 책의 수량을  1더한 값을 quan에 저장
					int quanCol = table.getColumn("수량").getModelIndex(); //수량 칼럼의 인덱스를 quanIndex에 저장
					model.setValueAt(quan, bookRow, quanCol); //1더한 수량을 테이블에 적용한다
					
					
					if(mainWin != null) {
						model.setValueAt(quan*book.price, bookRow, table.getColumn("총 가격").getModelIndex()); //MainWindow일 경우 총 가격을 테이블에 적용한다
						TableWorkHelper.updateTotalPrice(mainWin);  //결제정보의 총 금액을 업데이트
					}
					else { //예약 관리창일 경우
						int dateCol = table.getColumn("날짜").getModelIndex();  //날짜 칼럼의 인덱스를 dateCol에 저장
						model.setValueAt(Time.getDate(), bookRow, dateCol);  //행의 날짜 칼럼을 오늘 날짜로 바꿈
					}
				}
				else {   //테이블에 해당 isbn인 책이 없을 경우
					Vector<String> row = new Vector<String>();
					row.add(book.isbn);  //벡터에 isbn 추가
					row.add(book.name);  //책 이름
					row.add("1");  //수량
					
					if(mainWin != null) {  //Mainwindow일 경우
						row.add(Integer.toString(book.price));  //가격
						row.add(Integer.toString(book.price));  //총 가격
						model.addRow(row);  //새로운 행을 테이블에 추가한다
						TableWorkHelper.updateTotalPrice(mainWin);  //결제정보의 총 금액을 업데이트
					}
					else {  //예약 관리창이면
						row.add(Time.getDate());  //오늘 날짜
						model.addRow(row);  //새로운 행을 테이블에 추가한다
					}
				}
				sql.sqlClose();  //sql연결 종료
			} 
			else JOptionPane.showMessageDialog(null, "해당 ISBN의 책 정보를 찾지 못했습니다.", "데이터를 찾을 수 없음", JOptionPane.ERROR_MESSAGE); //입력된 isbn으로 책정보를 찾지 못했을 경우 경고창 띄움
		}
		catch(SQLException ex) { //DB연결 실패 예외처리
			JOptionPane.showMessageDialog(null, "DB연결에 실패 했습니다.", "DB연결 실패", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}//이벤트 처리 메소드 끝
}//numberAccessEvent 클래스 끝
