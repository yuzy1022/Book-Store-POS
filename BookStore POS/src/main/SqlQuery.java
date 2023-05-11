package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class SqlQuery {
	Connection con;
	Statement dbSt;
	
	SqlQuery() throws SQLException{  //생성자
		try {  //DB에 연결
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("드라이버 로드에 실패했습니다.");
        }
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookpos?serverTimezone=UTC", "root", "0000");
        dbSt = con.createStatement();
	} //생성자 끝
	
	
	//해당 날짜의 결제건 중 가장 높은 결제 번호 +1을 반환하는 메소드 
	long searchPayNum(int branchNum) throws SQLException{  
		String date = Time.getDate();
		String query = "select payNum from consumer where date=\"" + date + "\" and payNum=(select max(payNum) from consumer) and consumer_branchNum=" + branchNum +";";
		ResultSet result = dbSt.executeQuery(query);
		if(result.next()) {   //해당 날짜에 결제된 건이 1개 이상 있을 경우
			long payNum = result.getLong(1)+1;  //가장 높은 결제번호에 +1 한 값을 리턴
			return payNum;
		}
		else { //해당 날짜에 결제된 건이 하나도 없을 경우 결제번호 1 리턴 (해당 날짜의 첫 결제이므로)
			long payNum = Long.parseLong(Time.getDateForPayNum() + branchNum + "0001");
			return payNum;
		} 
	} //searchPayNum 메소드 끝
	
	
	//id, passwd를 받아와서 로그인을 시도하는 메소드
	boolean login(String id, String passwd) throws SQLException{  
		String query = "SELECT * FROM employee  WHERE empNum='" + id + "' AND passwd='" + passwd + "';";
		ResultSet result = dbSt.executeQuery(query);
		if (result.next()) return true;  //로그인이 성공하면 true 리턴
        else return false;  //로그인 실패시 false리턴
	}  //login메소드 끝
	
	
	//sql연결을 종료하는 메소드
	void sqlClose() throws SQLException{  
		con.close();
		dbSt.close();
	}//sqlClose메소드 끝
	
	
	//사번을 입력받아 직원의 이름을 리턴해주는 메소드
	String getEmpName(String empNum) throws SQLException{  
		String query = "select empName from employee where empNum=" + empNum + ";";
		ResultSet result = dbSt.executeQuery(query);
		result.next();  
		return result.getString(1);
	} //getEmpName 메소드 끝
	
	
	//isbn을 입력받아 책을 찾아주는 메소드
	Book searchBook(String searchISBN) throws SQLException{  
		String query = "select * from book where isbn=" + searchISBN + ";"; //쿼리문
		ResultSet result = dbSt.executeQuery(query);
		if(result.next()) {  //해당 isbn의 책 정보를 찾았을 경우
			String isbn, name;
			int price;
			isbn = result.getString(1); //첫번째 행의 1~3칼럼 (isbn, 제목, 가격)을 가져와 저장
			name = result.getString(2);
			price = result.getInt(3);
			return new Book(isbn, name, price); //Book 타입 객체 리턴
		}
		else return null;  //찾지 못했으면 null 리턴
	}//searchBook 메소드 끝
	
	
	//전화번호를 입력받아 회원을 찾아주는 메소드
	Member searchMember(String phone) throws SQLException{  
		String query = "select * from member where phone=\"" + phone + "\";";  //쿼리문
		ResultSet result = dbSt.executeQuery(query);
		
		if(result.next()) {  //해당 번호의 회원이 존재할 경우
			String name = result.getString(2); 
			int point = result.getInt(3);
			return new Member(name, phone, point); //Member 타입 객체 리턴
		}
		else return null;  //찾지 못했으면 null 리턴
	} //SearchMember 메소드 끝
	
	
	//전화번호, 테이블을 입력받아 예약된 도서를 테이블에 추가시켜 주는 메소드
	void searchReservation(DefaultTableModel model, String phone) throws SQLException{
		String query = "select * from reservation_book where reservation_book_phone='" + phone + "';";
		ResultSet result = dbSt.executeQuery(query);
		String isbn, name, quan, date;

		while(result.next()) {
			isbn = result.getString(2);  //결과에서 isbn을 가져와 저장
			date = result.getString(3);  //날짜를 가져와 저장
			quan = result.getString(4);  //수량을 가져와 저장
			SqlQuery sql = new SqlQuery();
			Book book = sql.searchBook(isbn);//isbn을 매개변수로 주고 책 객체로 리턴 받는다
			sql.sqlClose();
			name = book.name; //제목 저장
			
			Vector<String> row = new Vector<String>();
			row.add(isbn);  //벡터에 isbn, 제목, 수량, 날짜를 추가
			row.add(name);
			row.add(quan);
			row.add(date);
			model.addRow(row);  //새로운 행을 테이블에 추가한다
		}
	}//SearchReservation 메소드 끝
	
	
	//JTable을 입력받아 DB의 reservation_book 테이블을 수정시켜주는 메소드
	void updateReservationTable(JTable table, String phone) throws SQLException{
		String query = "delete from reservation_book where reservation_book_phone='" + phone + "';";
		dbSt.executeUpdate(query);  //DB에 있는 phone 번호로 예약된 데이터를 모두 지운다, insert, delete, update 같은 결과값 없는 쿼리문은 executeUpdate로 실행해야함
		int count = table.getRowCount();  //테이블에 있는 행의 수를 count에 저장
		String isbn, quan, date;
		
		for(int i=0; i<count; i++) {  //테이블의 행의 수만큼 반복
			isbn = table.getValueAt(i, table.getColumn("ISBN").getModelIndex()).toString();  //i번째 행의 isbn, 수량, 날짜를 가져와 변수에 저장
			quan = table.getValueAt(i, table.getColumn("수량").getModelIndex()).toString();
			date = table.getValueAt(i, table.getColumn("날짜").getModelIndex()).toString();
			query = "insert into reservation_book values('" + phone + "', '" + isbn + "', '" + date + "', '" + quan + "');"; 
			dbSt.executeUpdate(query); //DB에 예약정보 데이터 추가
		}
	}//UpdateReservationTable 메소드 끝
	
	
	//책을 결제했을 때 실행하는 메소드
	void payBook(MainWindow mainWin) throws SQLException{
		String query;  //쿼리문 저장할 String
		SqlQuery sql = new SqlQuery();
		long payNum = sql.searchPayNum(mainWin.branchNum); //결제번호 받아옴
		int addPoint = (int)(mainWin.totalPay*0.01); //포인트 적립액
		int cashPay = mainWin.totalPay - mainWin.cardPay - mainWin.pointPay;  //현금 사용액
		sql.sqlClose();
		query = "insert into consumer values(" + payNum+ ", '" + Time.getDate() + "', " + mainWin.posNum + ", " + mainWin.branchNum + ");"; 
		dbSt.executeUpdate(query); //consumer 테이블에 데이터 삽입
		for(int i=0; i < mainWin.table.getRowCount(); i++) {
			String isbn = mainWin.table.getValueAt(i, mainWin.table.getColumn("ISBN").getModelIndex()).toString();
			String quan = mainWin.table.getValueAt(i, mainWin.table.getColumn("수량").getModelIndex()).toString();
			
			query = "insert into consumer_buy values('" + isbn + "', " + payNum + ", '" + Time.getDate() + "', " + quan + ", " + mainWin.branchNum + ");";
			dbSt.executeUpdate(query); //cousumer_buy 테이블에 데이터 삽입
		}
		if(mainWin.mem == null) {  //회원이 아닐 경우
			query = "insert into consumer_pay values(" + payNum + ", " + mainWin.branchNum + ", '" + Time.getDate() + "', " + mainWin.totalPay + ", null, " + mainWin.cardPay + ", " + cashPay + ", " + mainWin.pointPay + ", null, " + mainWin.empNum + ");";
			dbSt.executeUpdate(query);
		}
		else {  //회원 일경우
			int point = mainWin.mem.point + addPoint - mainWin.pointPay;  //가진 포인트 + 적립 포인트 - 사용한 포인트
			
			//consumer_pay 테이블에 데이터 삽입
			query = "insert into consumer_pay values(" + payNum + ", " + mainWin.branchNum + ", '" + Time.getDate() + "', " + mainWin.totalPay + ", " + addPoint + ", " + mainWin.cardPay + ", " + cashPay + ", " + mainWin.pointPay + " , '" + mainWin.mem.phone + "', " + mainWin.empNum + ");";
			dbSt.executeUpdate(query);
			query = "update member set point=" + point + " where phone='" + mainWin.mem.phone + "';";  //DB에서 회원의 포인트를 업데이트
			dbSt.executeUpdate(query);
			
			if(mainWin.buyResBook) { //회원이면서 예약된 도서를 구매했을 경우
				query = "delete from reservation_book where reservation_book_phone='" + mainWin.mem.phone + "';";  //DB에서 해당 회원의 예약 도서를 모두 삭제
				dbSt.executeUpdate(query);
			}
		}
		
		TableWorkHelper.deleteAllRow(mainWin.model);  //메인 윈도우 테이블의 목록 모두 삭제
		TableWorkHelper.updateTotalPrice(mainWin);  //총 금액 Label업데이트 
		mainWin.clearPayment(); //결제정보 초기화
		mainWin.clearMemberInfo(); //회원정보 초기화
	}//payBook 메소드 끝
	
	
	//지점 번호를 입력받아 지점 명을 리턴해주는 메소드
	String getBranchName(int branchNum) throws SQLException{
		String query = "select branchName from branch where branchNum=" + branchNum + ";";
		ResultSet result = dbSt.executeQuery(query);
		
		if(result.next()) {  //지점이름을 찾으면 지점 이름 리턴
			return result.getString(1);
		}
		else return null;  //못찾으면 null 리턴
	} //getBranchName 메소드 끝
	
	
	//영업마감 눌렀을 때 DB에 매출 정보 insert 해주는 메소드
	void setSalesToDB(int branchNum) throws SQLException{
		int [] pay = new int[4];  //총 금액, 카드 금액, 현금 금액, 포인트 금액을 저장할 int 배열
		String [] s = {"totalPay", "cardPay", "cashPay", "pointPay"}; //총 금액, 카드 금액, 현금 금액, 포인트 금액 String 배열
		String date = Time.getDate(); //시간 받아옴 
		String query; //쿼리문 저장할 String 
		
		query = "select * from sales where date='" + date + "' and sales_branchNum=" + branchNum + ";";
		ResultSet result = dbSt.executeQuery(query);
		if(result.next()) { //해당 날짜에 대한 매출정보가 이미 DB에 등록되어 있으면 
			JOptionPane.showMessageDialog(null, date + " 에 대한 매출 정보가 이미 등록되어 있습니다.", "오류", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		for(int i=0; i < s.length; i++) {//헤당 날짜에 대한 총 금액, 카드 금액, 현금 금액, 포인트 금액을 각각 더한 뒤 pay 배열에 저장
			query = "select sum(" + s[i] + ") from consumer_pay where date='" + date + "' " + "and consumer_pay_branchNum=" + branchNum + ";";
			result = dbSt.executeQuery(query);
				
			if(result.next()) pay[i] = result.getInt(1);  //값이 있으면 배열에 저장
			else pay[i] = 0;  //값이 없으면 0 저장
		}
		//sales (매출) 테이블에 데이터 삽입
		query = "insert into sales values(" + branchNum + ", '" + date + "', " + pay[0] + ", " + pay[1] + ", " + pay[2] + ", " + pay[3] + ");";
		dbSt.executeUpdate(query);
	}//setSalesToDB 메소드 끝
	
	
	//모든 매출 정보를 받아와 Sales 배열로 리턴해주는 메소드
	Sales[] getSales() throws SQLException{
		String date, query;  //날짜, 쿼리문 저장할 String
		int branchNum, totalSales, cardSales, cashSales, pointSales, count; //지점번호, 총 매출, 카드매출, 현금매출, 포인트매출, 카운트 변수
		Sales [] sales; //Sales 타입 배열
		
		//sales 테이블에 저장된 데이터의 갯수를 받아와 count에 저장
		query = "select count(*) from sales;";  
		ResultSet result = dbSt.executeQuery(query);
		result.next();
		count = result.getInt(1);
		sales = new Sales[count];  //count 값으로 Sales 배열을 초기화
		
		//sales 테이블에 있는 데이터를 전부 가져와서 sales 배열에 넣음
		query = "select * from sales;";  
		result = dbSt.executeQuery(query);
		for(int i=0; i < count; i++) {
			result.next();
			branchNum = result.getInt(1);
			date = result.getString(2);
			totalSales = result.getInt(3);
			cardSales = result.getInt(4);
			cashSales = result.getInt(5);
			pointSales = result.getInt(6);
			sales[i] = new Sales(date, branchNum, totalSales, cardSales, cashSales, pointSales);
		}
		
		return sales;  //sales 배열 리턴
	}//getSales 메소드 끝
	
	
	//DB에서 결제정보를 검색하여 PayInfo 배열로 리턴해주는 메소드
	PayInfo[] getPayInfo(String value, String kind) throws SQLException{
		String date, phone, query;
		int totalPay, cardPay, cashPay, pointPay, addPoint, empNum, count;
		long payNum;
		PayInfo [] payInfo;
		
		//날짜, 휴대폰번호, 사번 중 하나로 DB를 검색하여 튜플의 갯수를 받아와 count에 저장
		if(kind.equals("날짜")) query = "select count(*) from consumer_pay where date='" + value + "';";
		else if(kind.equals("휴대폰 번호")) query = "select count(*) from consumer_pay where consumer_pay_phone='" + value + "';";
		else if(kind.equals("사번")) query = "select count(*) from consumer_pay where consumer_pay_empNum=" + value + ";";
		else query = "select count(*) from consumer_pay where consumer_pay_payNum=" + value + ";";
		ResultSet result = dbSt.executeQuery(query);
		result.next();
		count = result.getInt(1);
		payInfo = new PayInfo[count]; //받아온 count로 payInfo 배열 초기화
		
		
		//DB에서 데이터를 받아와 payInfo 배열에 저장
		if(kind.equals("날짜")) query = "select * from consumer_pay where date='" + value + "';";
		else if(kind.equals("휴대폰 번호")) query = "select * from consumer_pay where consumer_pay_phone='" + value + "';";
		else if(kind.equals("사번")) query = "select * from consumer_pay where consumer_pay_empNum=" + value + ";";
		else query = "select * from consumer_pay where consumer_pay_payNum=" + value + ";";
		ResultSet result2 = dbSt.executeQuery(query);
		for(int i=0; i < count; i++) {
			result2.next();
			date = result2.getString(3);
			payNum = result2.getLong(1);
			totalPay = result2.getInt(4);
			cardPay = result2.getInt(6);
			cashPay = result2.getInt(7);
			pointPay = result2.getInt(8);
			addPoint = result2.getInt(5);
			phone = result2.getString(9);
			empNum = result2.getInt(10);
			payInfo[i] = new PayInfo(date, payNum, totalPay, cardPay, cashPay, pointPay, addPoint, phone, empNum);
		}
		return payInfo;
	}//getPayInfo 메소드 끝
	
}//SqlQuery클래스 끝
