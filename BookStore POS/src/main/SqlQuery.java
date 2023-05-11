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
	
	SqlQuery() throws SQLException{  //������
		try {  //DB�� ����
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("����̹� �ε忡 �����߽��ϴ�.");
        }
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookpos?serverTimezone=UTC", "root", "0000");
        dbSt = con.createStatement();
	} //������ ��
	
	
	//�ش� ��¥�� ������ �� ���� ���� ���� ��ȣ +1�� ��ȯ�ϴ� �޼ҵ� 
	long searchPayNum(int branchNum) throws SQLException{  
		String date = Time.getDate();
		String query = "select payNum from consumer where date=\"" + date + "\" and payNum=(select max(payNum) from consumer) and consumer_branchNum=" + branchNum +";";
		ResultSet result = dbSt.executeQuery(query);
		if(result.next()) {   //�ش� ��¥�� ������ ���� 1�� �̻� ���� ���
			long payNum = result.getLong(1)+1;  //���� ���� ������ȣ�� +1 �� ���� ����
			return payNum;
		}
		else { //�ش� ��¥�� ������ ���� �ϳ��� ���� ��� ������ȣ 1 ���� (�ش� ��¥�� ù �����̹Ƿ�)
			long payNum = Long.parseLong(Time.getDateForPayNum() + branchNum + "0001");
			return payNum;
		} 
	} //searchPayNum �޼ҵ� ��
	
	
	//id, passwd�� �޾ƿͼ� �α����� �õ��ϴ� �޼ҵ�
	boolean login(String id, String passwd) throws SQLException{  
		String query = "SELECT * FROM employee  WHERE empNum='" + id + "' AND passwd='" + passwd + "';";
		ResultSet result = dbSt.executeQuery(query);
		if (result.next()) return true;  //�α����� �����ϸ� true ����
        else return false;  //�α��� ���н� false����
	}  //login�޼ҵ� ��
	
	
	//sql������ �����ϴ� �޼ҵ�
	void sqlClose() throws SQLException{  
		con.close();
		dbSt.close();
	}//sqlClose�޼ҵ� ��
	
	
	//����� �Է¹޾� ������ �̸��� �������ִ� �޼ҵ�
	String getEmpName(String empNum) throws SQLException{  
		String query = "select empName from employee where empNum=" + empNum + ";";
		ResultSet result = dbSt.executeQuery(query);
		result.next();  
		return result.getString(1);
	} //getEmpName �޼ҵ� ��
	
	
	//isbn�� �Է¹޾� å�� ã���ִ� �޼ҵ�
	Book searchBook(String searchISBN) throws SQLException{  
		String query = "select * from book where isbn=" + searchISBN + ";"; //������
		ResultSet result = dbSt.executeQuery(query);
		if(result.next()) {  //�ش� isbn�� å ������ ã���� ���
			String isbn, name;
			int price;
			isbn = result.getString(1); //ù��° ���� 1~3Į�� (isbn, ����, ����)�� ������ ����
			name = result.getString(2);
			price = result.getInt(3);
			return new Book(isbn, name, price); //Book Ÿ�� ��ü ����
		}
		else return null;  //ã�� �������� null ����
	}//searchBook �޼ҵ� ��
	
	
	//��ȭ��ȣ�� �Է¹޾� ȸ���� ã���ִ� �޼ҵ�
	Member searchMember(String phone) throws SQLException{  
		String query = "select * from member where phone=\"" + phone + "\";";  //������
		ResultSet result = dbSt.executeQuery(query);
		
		if(result.next()) {  //�ش� ��ȣ�� ȸ���� ������ ���
			String name = result.getString(2); 
			int point = result.getInt(3);
			return new Member(name, phone, point); //Member Ÿ�� ��ü ����
		}
		else return null;  //ã�� �������� null ����
	} //SearchMember �޼ҵ� ��
	
	
	//��ȭ��ȣ, ���̺��� �Է¹޾� ����� ������ ���̺� �߰����� �ִ� �޼ҵ�
	void searchReservation(DefaultTableModel model, String phone) throws SQLException{
		String query = "select * from reservation_book where reservation_book_phone='" + phone + "';";
		ResultSet result = dbSt.executeQuery(query);
		String isbn, name, quan, date;

		while(result.next()) {
			isbn = result.getString(2);  //������� isbn�� ������ ����
			date = result.getString(3);  //��¥�� ������ ����
			quan = result.getString(4);  //������ ������ ����
			SqlQuery sql = new SqlQuery();
			Book book = sql.searchBook(isbn);//isbn�� �Ű������� �ְ� å ��ü�� ���� �޴´�
			sql.sqlClose();
			name = book.name; //���� ����
			
			Vector<String> row = new Vector<String>();
			row.add(isbn);  //���Ϳ� isbn, ����, ����, ��¥�� �߰�
			row.add(name);
			row.add(quan);
			row.add(date);
			model.addRow(row);  //���ο� ���� ���̺� �߰��Ѵ�
		}
	}//SearchReservation �޼ҵ� ��
	
	
	//JTable�� �Է¹޾� DB�� reservation_book ���̺��� ���������ִ� �޼ҵ�
	void updateReservationTable(JTable table, String phone) throws SQLException{
		String query = "delete from reservation_book where reservation_book_phone='" + phone + "';";
		dbSt.executeUpdate(query);  //DB�� �ִ� phone ��ȣ�� ����� �����͸� ��� �����, insert, delete, update ���� ����� ���� �������� executeUpdate�� �����ؾ���
		int count = table.getRowCount();  //���̺� �ִ� ���� ���� count�� ����
		String isbn, quan, date;
		
		for(int i=0; i<count; i++) {  //���̺��� ���� ����ŭ �ݺ�
			isbn = table.getValueAt(i, table.getColumn("ISBN").getModelIndex()).toString();  //i��° ���� isbn, ����, ��¥�� ������ ������ ����
			quan = table.getValueAt(i, table.getColumn("����").getModelIndex()).toString();
			date = table.getValueAt(i, table.getColumn("��¥").getModelIndex()).toString();
			query = "insert into reservation_book values('" + phone + "', '" + isbn + "', '" + date + "', '" + quan + "');"; 
			dbSt.executeUpdate(query); //DB�� �������� ������ �߰�
		}
	}//UpdateReservationTable �޼ҵ� ��
	
	
	//å�� �������� �� �����ϴ� �޼ҵ�
	void payBook(MainWindow mainWin) throws SQLException{
		String query;  //������ ������ String
		SqlQuery sql = new SqlQuery();
		long payNum = sql.searchPayNum(mainWin.branchNum); //������ȣ �޾ƿ�
		int addPoint = (int)(mainWin.totalPay*0.01); //����Ʈ ������
		int cashPay = mainWin.totalPay - mainWin.cardPay - mainWin.pointPay;  //���� ����
		sql.sqlClose();
		query = "insert into consumer values(" + payNum+ ", '" + Time.getDate() + "', " + mainWin.posNum + ", " + mainWin.branchNum + ");"; 
		dbSt.executeUpdate(query); //consumer ���̺� ������ ����
		for(int i=0; i < mainWin.table.getRowCount(); i++) {
			String isbn = mainWin.table.getValueAt(i, mainWin.table.getColumn("ISBN").getModelIndex()).toString();
			String quan = mainWin.table.getValueAt(i, mainWin.table.getColumn("����").getModelIndex()).toString();
			
			query = "insert into consumer_buy values('" + isbn + "', " + payNum + ", '" + Time.getDate() + "', " + quan + ", " + mainWin.branchNum + ");";
			dbSt.executeUpdate(query); //cousumer_buy ���̺� ������ ����
		}
		if(mainWin.mem == null) {  //ȸ���� �ƴ� ���
			query = "insert into consumer_pay values(" + payNum + ", " + mainWin.branchNum + ", '" + Time.getDate() + "', " + mainWin.totalPay + ", null, " + mainWin.cardPay + ", " + cashPay + ", " + mainWin.pointPay + ", null, " + mainWin.empNum + ");";
			dbSt.executeUpdate(query);
		}
		else {  //ȸ�� �ϰ��
			int point = mainWin.mem.point + addPoint - mainWin.pointPay;  //���� ����Ʈ + ���� ����Ʈ - ����� ����Ʈ
			
			//consumer_pay ���̺� ������ ����
			query = "insert into consumer_pay values(" + payNum + ", " + mainWin.branchNum + ", '" + Time.getDate() + "', " + mainWin.totalPay + ", " + addPoint + ", " + mainWin.cardPay + ", " + cashPay + ", " + mainWin.pointPay + " , '" + mainWin.mem.phone + "', " + mainWin.empNum + ");";
			dbSt.executeUpdate(query);
			query = "update member set point=" + point + " where phone='" + mainWin.mem.phone + "';";  //DB���� ȸ���� ����Ʈ�� ������Ʈ
			dbSt.executeUpdate(query);
			
			if(mainWin.buyResBook) { //ȸ���̸鼭 ����� ������ �������� ���
				query = "delete from reservation_book where reservation_book_phone='" + mainWin.mem.phone + "';";  //DB���� �ش� ȸ���� ���� ������ ��� ����
				dbSt.executeUpdate(query);
			}
		}
		
		TableWorkHelper.deleteAllRow(mainWin.model);  //���� ������ ���̺��� ��� ��� ����
		TableWorkHelper.updateTotalPrice(mainWin);  //�� �ݾ� Label������Ʈ 
		mainWin.clearPayment(); //�������� �ʱ�ȭ
		mainWin.clearMemberInfo(); //ȸ������ �ʱ�ȭ
	}//payBook �޼ҵ� ��
	
	
	//���� ��ȣ�� �Է¹޾� ���� ���� �������ִ� �޼ҵ�
	String getBranchName(int branchNum) throws SQLException{
		String query = "select branchName from branch where branchNum=" + branchNum + ";";
		ResultSet result = dbSt.executeQuery(query);
		
		if(result.next()) {  //�����̸��� ã���� ���� �̸� ����
			return result.getString(1);
		}
		else return null;  //��ã���� null ����
	} //getBranchName �޼ҵ� ��
	
	
	//�������� ������ �� DB�� ���� ���� insert ���ִ� �޼ҵ�
	void setSalesToDB(int branchNum) throws SQLException{
		int [] pay = new int[4];  //�� �ݾ�, ī�� �ݾ�, ���� �ݾ�, ����Ʈ �ݾ��� ������ int �迭
		String [] s = {"totalPay", "cardPay", "cashPay", "pointPay"}; //�� �ݾ�, ī�� �ݾ�, ���� �ݾ�, ����Ʈ �ݾ� String �迭
		String date = Time.getDate(); //�ð� �޾ƿ� 
		String query; //������ ������ String 
		
		query = "select * from sales where date='" + date + "' and sales_branchNum=" + branchNum + ";";
		ResultSet result = dbSt.executeQuery(query);
		if(result.next()) { //�ش� ��¥�� ���� ���������� �̹� DB�� ��ϵǾ� ������ 
			JOptionPane.showMessageDialog(null, date + " �� ���� ���� ������ �̹� ��ϵǾ� �ֽ��ϴ�.", "����", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		for(int i=0; i < s.length; i++) {//��� ��¥�� ���� �� �ݾ�, ī�� �ݾ�, ���� �ݾ�, ����Ʈ �ݾ��� ���� ���� �� pay �迭�� ����
			query = "select sum(" + s[i] + ") from consumer_pay where date='" + date + "' " + "and consumer_pay_branchNum=" + branchNum + ";";
			result = dbSt.executeQuery(query);
				
			if(result.next()) pay[i] = result.getInt(1);  //���� ������ �迭�� ����
			else pay[i] = 0;  //���� ������ 0 ����
		}
		//sales (����) ���̺� ������ ����
		query = "insert into sales values(" + branchNum + ", '" + date + "', " + pay[0] + ", " + pay[1] + ", " + pay[2] + ", " + pay[3] + ");";
		dbSt.executeUpdate(query);
	}//setSalesToDB �޼ҵ� ��
	
	
	//��� ���� ������ �޾ƿ� Sales �迭�� �������ִ� �޼ҵ�
	Sales[] getSales() throws SQLException{
		String date, query;  //��¥, ������ ������ String
		int branchNum, totalSales, cardSales, cashSales, pointSales, count; //������ȣ, �� ����, ī�����, ���ݸ���, ����Ʈ����, ī��Ʈ ����
		Sales [] sales; //Sales Ÿ�� �迭
		
		//sales ���̺� ����� �������� ������ �޾ƿ� count�� ����
		query = "select count(*) from sales;";  
		ResultSet result = dbSt.executeQuery(query);
		result.next();
		count = result.getInt(1);
		sales = new Sales[count];  //count ������ Sales �迭�� �ʱ�ȭ
		
		//sales ���̺� �ִ� �����͸� ���� �����ͼ� sales �迭�� ����
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
		
		return sales;  //sales �迭 ����
	}//getSales �޼ҵ� ��
	
	
	//DB���� ���������� �˻��Ͽ� PayInfo �迭�� �������ִ� �޼ҵ�
	PayInfo[] getPayInfo(String value, String kind) throws SQLException{
		String date, phone, query;
		int totalPay, cardPay, cashPay, pointPay, addPoint, empNum, count;
		long payNum;
		PayInfo [] payInfo;
		
		//��¥, �޴�����ȣ, ��� �� �ϳ��� DB�� �˻��Ͽ� Ʃ���� ������ �޾ƿ� count�� ����
		if(kind.equals("��¥")) query = "select count(*) from consumer_pay where date='" + value + "';";
		else if(kind.equals("�޴��� ��ȣ")) query = "select count(*) from consumer_pay where consumer_pay_phone='" + value + "';";
		else if(kind.equals("���")) query = "select count(*) from consumer_pay where consumer_pay_empNum=" + value + ";";
		else query = "select count(*) from consumer_pay where consumer_pay_payNum=" + value + ";";
		ResultSet result = dbSt.executeQuery(query);
		result.next();
		count = result.getInt(1);
		payInfo = new PayInfo[count]; //�޾ƿ� count�� payInfo �迭 �ʱ�ȭ
		
		
		//DB���� �����͸� �޾ƿ� payInfo �迭�� ����
		if(kind.equals("��¥")) query = "select * from consumer_pay where date='" + value + "';";
		else if(kind.equals("�޴��� ��ȣ")) query = "select * from consumer_pay where consumer_pay_phone='" + value + "';";
		else if(kind.equals("���")) query = "select * from consumer_pay where consumer_pay_empNum=" + value + ";";
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
	}//getPayInfo �޼ҵ� ��
	
}//SqlQueryŬ���� ��
