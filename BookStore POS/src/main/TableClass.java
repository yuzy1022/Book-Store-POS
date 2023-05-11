package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

//��� ��ư�� ���� ���̺��� �׸��� �����ϴ� �̺�Ʈ Ŭ����
class BookCancelEvent implements ActionListener{  
	JTable table;
	DefaultTableModel model;
	MainWindow mainWin=null;
	
	//MainWindow���� ����ϴ� ������
	//BookCancelEvent(JTable table, DefaultTableModel model, JLabel totalPrice){
	BookCancelEvent(MainWindow mainWin){
		this.table = mainWin.table;
		this.model = mainWin.model;
		this.mainWin = mainWin;
	} //������ ��
	
	
	//������� â���� ����ϴ� ������
	//BookCancelEvent(JTable table, DefaultTableModel model){
	BookCancelEvent(ReservationManagementWindow resWin){
		this.table = resWin.table;
		this.model = resWin.model;
	} //������ ��
	
	
	//�̺�Ʈ ó�� �޼ҵ�
	public void actionPerformed(ActionEvent e) {
		int selectIndex[] = table.getSelectedRows();  //���õ� ���� �ε������� �迭�� ������
		if(selectIndex.length == 0) {
			JOptionPane.showMessageDialog(null, "������ ����� ������ �ּ���", "����", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		int count=0;  
		for (int i=0; i<selectIndex.length; i++) { //���õ� ����� ���̺��� ������
			model.removeRow(selectIndex[i]-count++);  //������ ������ ī��Ʈ�� 1�����Ͽ� �ε������� �A�� (������ ������ ���� 1���� �پ��Ƿ� ���� �ε����� �ٲ�)
		}
		if(mainWin != null)TableWorkHelper.updateTotalPrice(mainWin);  //mainWindow�� �� ���� �ֽ�ȭ
	}//�̺�Ʈó�� �޼ҵ� ��
}  //BookCancelEvent Ŭ���� ��



//���̺� �۾��� �����ִ� Ŭ����
class TableWorkHelper{ 
	
	//���̺� �ش� isbn�� ���� �̹� �����ϴ��� Ȯ�� ���ִ� �޼ҵ�
	static int isBookInTable(DefaultTableModel model, String isbn) {  
		for(int i=0; i<model.getRowCount(); i++) { //getRowCount() : ��ü �� ����
			if(isbn.equals(model.getValueAt(i, 0).toString())) {  //�ش� isbn�� Į���� ������ ���    i:��, 0:isbnĮ��  
				return i;  //�ش� �� ��ȣ�� ����
			}
			else continue;
		}
		return -1;  //�ش� isbn�� ���̺� ������ -1 ����
	}//isBookInTable �޼ҵ� ��
	
	//����ȭ���� �� �����ݾ��� ������Ʈ ���ִ� �޼ҵ�
	static void updateTotalPrice(MainWindow win) {  
		int totalPrice=0;
		
		for(int i=0; i<win.model.getRowCount(); i++) {  //���̺��� ��� ���� ������ ���Ѵ�
			totalPrice += Integer.parseInt(win.table.getValueAt(i, win.table.getColumn("�� ����").getModelIndex()).toString());
		}
		win.totalPrice.setText(Integer.toString(totalPrice) + " ��"); //���� ������ JLabel�� ����
		win.totalPay = totalPrice;
	} //updateTotalPrice �޼ҵ� ��
	
	
	//���̺��� ����� ��� �����ϴ� �޼ҵ�
	static void deleteAllRow(DefaultTableModel model) {
		int count = model.getRowCount();
		
		for(int i=0; i < count; i++) {
			model.removeRow(0);
		}
		
	}//deleteAllRow �޼ҵ� ��
	
	
}//TableWorkHelper Ŭ���� ��