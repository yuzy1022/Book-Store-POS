package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

//취소 버튼을 눌러 테이블의 항목을 삭제하는 이벤트 클래스
class BookCancelEvent implements ActionListener{  
	JTable table;
	DefaultTableModel model;
	MainWindow mainWin=null;
	
	//MainWindow에서 사용하는 생성자
	//BookCancelEvent(JTable table, DefaultTableModel model, JLabel totalPrice){
	BookCancelEvent(MainWindow mainWin){
		this.table = mainWin.table;
		this.model = mainWin.model;
		this.mainWin = mainWin;
	} //생성자 끝
	
	
	//예약관리 창에서 사용하는 생성자
	//BookCancelEvent(JTable table, DefaultTableModel model){
	BookCancelEvent(ReservationManagementWindow resWin){
		this.table = resWin.table;
		this.model = resWin.model;
	} //생성자 끝
	
	
	//이벤트 처리 메소드
	public void actionPerformed(ActionEvent e) {
		int selectIndex[] = table.getSelectedRows();  //선택된 행의 인덱스들을 배열로 가져옴
		if(selectIndex.length == 0) {
			JOptionPane.showMessageDialog(null, "삭제할 목록을 선택해 주세요", "오류", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		int count=0;  
		for (int i=0; i<selectIndex.length; i++) { //선택된 행들을 테이블에서 삭제함
			model.removeRow(selectIndex[i]-count++);  //삭제될 때마다 카운트를 1증가하여 인덱스에서 뺸다 (삭제할 때마다 행이 1개씩 줄어드므로 행의 인덱스도 바뀜)
		}
		if(mainWin != null)TableWorkHelper.updateTotalPrice(mainWin);  //mainWindow면 총 가격 최신화
	}//이벤트처리 메소드 끝
}  //BookCancelEvent 클래스 끝



//테이블 작업을 도와주는 클래스
class TableWorkHelper{ 
	
	//테이블에 해당 isbn의 행이 이미 존재하는지 확인 해주는 메소드
	static int isBookInTable(DefaultTableModel model, String isbn) {  
		for(int i=0; i<model.getRowCount(); i++) { //getRowCount() : 전체 행 갯수
			if(isbn.equals(model.getValueAt(i, 0).toString())) {  //해당 isbn의 칼럼이 존재할 경우    i:행, 0:isbn칼럼  
				return i;  //해당 행 번호를 리턴
			}
			else continue;
		}
		return -1;  //해당 isbn이 테이블에 없으면 -1 리턴
	}//isBookInTable 메소드 끝
	
	//메인화면의 총 결제금액을 업데이트 해주는 메소드
	static void updateTotalPrice(MainWindow win) {  
		int totalPrice=0;
		
		for(int i=0; i<win.model.getRowCount(); i++) {  //테이블의 모든 행의 가격을 더한다
			totalPrice += Integer.parseInt(win.table.getValueAt(i, win.table.getColumn("총 가격").getModelIndex()).toString());
		}
		win.totalPrice.setText(Integer.toString(totalPrice) + " 원"); //더한 가격을 JLabel에 적용
		win.totalPay = totalPrice;
	} //updateTotalPrice 메소드 끝
	
	
	//테이블의 행들을 모두 삭제하는 메소드
	static void deleteAllRow(DefaultTableModel model) {
		int count = model.getRowCount();
		
		for(int i=0; i < count; i++) {
			model.removeRow(0);
		}
		
	}//deleteAllRow 메소드 끝
	
	
}//TableWorkHelper 클래스 끝