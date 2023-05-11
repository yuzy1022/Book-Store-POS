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

//�ؽ�Ʈ �ʵ� �ʱ�ȭ �ϴ� �̺�Ʈ Ŭ����
class ClearJTextField implements ActionListener{ 
	JTextField bookSearch;
	
	//������
	ClearJTextField(JTextField s){
		bookSearch = s;
	}//������ ��
	
	//�̺�Ʈ ó�� �޼ҵ� (�ؽ�Ʈ �ʵ� �ʱ�ȭ��)
	public void actionPerformed(ActionEvent e) {
		bookSearch.setText("");
	}//�̺�Ʈ ó�� �޼ҵ� ��
}//ClearTextField Ŭ���� ��



//numberKey�� ���� ��ư Ŭ�� �� �׼� �̺�Ʈ
class numberKeyEvent implements ActionListener{ 
	JTextField bookSearch; String n;
	
	//������
	numberKeyEvent(JTextField s, String n){
		bookSearch = s; this.n =n;
	}//������ ��
	
	//�̺�Ʈ ó�� �޼ҵ� (���� ��ư Ŭ���ϸ� �ؽ�Ʈ �ʵ忡 �� �Է�)
	public void actionPerformed(ActionEvent e) {
		String isbn = bookSearch.getText();
		bookSearch.setText(isbn + n);
	}//�̺�Ʈ ó�� �޼ҵ� ��
}//numberKeyEvent Ŭ���� ��



//Ű�е��� Ȯ�ι�ư Ŭ���� å�� �˻��ϴ� �̺�Ʈ Ŭ����
class numberAccessEvent implements ActionListener{  
	JTextField bookSearch;
	DefaultTableModel model;
	MainWindow mainWin = null;
	JTable table;
	
	//MainWindow���� ����� ������
	numberAccessEvent(MainWindow mainWin){
		this.bookSearch = mainWin.bookSearch;
		this.model = mainWin.model;
		this.table = mainWin.table;
		this.mainWin = mainWin;
	}  //MainWindow���� ����� ������ ��
	
	
	//������� ȭ�鿡�� ����� ������ 
	numberAccessEvent(ReservationManagementWindow resWin){ 
		this.bookSearch = resWin.bookSearch;
		this.model = resWin.model;
		this.table = resWin.table;
	}  //������� ȭ�鿡�� ����� ������ ��
	
	
	//�̺�Ʈ ó�� �޼ҵ�
	public void actionPerformed(ActionEvent e) {  
		String isbn = bookSearch.getText(); //�Է��ʵ��� isbn�� ������ ����
		bookSearch.setText("");  //isbn �Է��ʵ� ����
		if(isbn.equals("")) {
			JOptionPane.showMessageDialog(null, "ISBN�� �Է����ּ���.", "����", JOptionPane.ERROR_MESSAGE); //isbn�� �Է����� �ʾ��� ��� ���â ���
			return;
		} 
		try {
			SqlQuery sql = new SqlQuery(); 
			Book book = sql.searchBook(isbn);  //�Էµ� isbn���� å�� ã�Ƽ� Book��ü�� ���� ����
			
			if(book != null) { //�Էµ� isbn�� å������ DB���� ã���� ���
				int bookRow = TableWorkHelper.isBookInTable(model, book.isbn); //�ش� isbn�� Į���� �̹� ���̺� �ִ��� �˻��Ͽ� ���� �ε����� bookRow�� ����
				
				if(bookRow >= 0) {  //���̺� �ش� isbn�� ���� ���� ���
					int quan = Integer.parseInt(model.getValueAt(bookRow, table.getColumn("����").getModelIndex()).toString())+1; //�ش� å�� ������  1���� ���� quan�� ����
					int quanCol = table.getColumn("����").getModelIndex(); //���� Į���� �ε����� quanIndex�� ����
					model.setValueAt(quan, bookRow, quanCol); //1���� ������ ���̺� �����Ѵ�
					
					
					if(mainWin != null) {
						model.setValueAt(quan*book.price, bookRow, table.getColumn("�� ����").getModelIndex()); //MainWindow�� ��� �� ������ ���̺� �����Ѵ�
						TableWorkHelper.updateTotalPrice(mainWin);  //���������� �� �ݾ��� ������Ʈ
					}
					else { //���� ����â�� ���
						int dateCol = table.getColumn("��¥").getModelIndex();  //��¥ Į���� �ε����� dateCol�� ����
						model.setValueAt(Time.getDate(), bookRow, dateCol);  //���� ��¥ Į���� ���� ��¥�� �ٲ�
					}
				}
				else {   //���̺� �ش� isbn�� å�� ���� ���
					Vector<String> row = new Vector<String>();
					row.add(book.isbn);  //���Ϳ� isbn �߰�
					row.add(book.name);  //å �̸�
					row.add("1");  //����
					
					if(mainWin != null) {  //Mainwindow�� ���
						row.add(Integer.toString(book.price));  //����
						row.add(Integer.toString(book.price));  //�� ����
						model.addRow(row);  //���ο� ���� ���̺� �߰��Ѵ�
						TableWorkHelper.updateTotalPrice(mainWin);  //���������� �� �ݾ��� ������Ʈ
					}
					else {  //���� ����â�̸�
						row.add(Time.getDate());  //���� ��¥
						model.addRow(row);  //���ο� ���� ���̺� �߰��Ѵ�
					}
				}
				sql.sqlClose();  //sql���� ����
			} 
			else JOptionPane.showMessageDialog(null, "�ش� ISBN�� å ������ ã�� ���߽��ϴ�.", "�����͸� ã�� �� ����", JOptionPane.ERROR_MESSAGE); //�Էµ� isbn���� å������ ã�� ������ ��� ���â ���
		}
		catch(SQLException ex) { //DB���� ���� ����ó��
			JOptionPane.showMessageDialog(null, "DB���ῡ ���� �߽��ϴ�.", "DB���� ����", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}//�̺�Ʈ ó�� �޼ҵ� ��
}//numberAccessEvent Ŭ���� ��
