package main;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;
import java.sql.*;
import java.util.*;



//�˻� ��ư ������ �� ����Ǵ� �̺�Ʈ Ŭ����
class SearchBtnEvent implements ActionListener{
	PayInfoWindow win;
	
	SearchBtnEvent(PayInfoWindow win){
		this.win = win;
	}
	
	//�̺�Ʈ ó�� �޼ҵ�
	public void actionPerformed(ActionEvent e) {
		String value = win.jtf.getText();
		String kind = win.comboStat;
		win.jtf.setText("");  //�ؽ�Ʈ �ʵ� �ʱ�ȭ
		
		if(value.equals("")) {  //�ؽ�Ʈ �ʵ忡 �ƹ��͵� �Է����� �ʾ�����
			JOptionPane.showMessageDialog(win, "�˻�� �Է��� �ּ���", "����", JOptionPane.ERROR_MESSAGE);
			return;
		}
		else {  //�ؽ�Ʈ �ʵ忡 �Է� �ߴٸ�
			try {
				SqlQuery sql = new SqlQuery();
				PayInfo [] payInfo = sql.getPayInfo(value, kind);  //DB���� �����͸� ������ payInfo �迭�� ����

				if(payInfo.length == 0) { //�˻��� �����Ͱ� ������
					JOptionPane.showMessageDialog(win, "�ش� ������ �˻��� �����Ͱ� �����ϴ�.", "����", JOptionPane.ERROR_MESSAGE);
					return;
				}
				TableWorkHelper.deleteAllRow(win.model);  //���̺� �ʱ�ȭ
				
				//���̺� ������ ����
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
				JOptionPane.showMessageDialog(win, "DB���ῡ ���� �߽��ϴ�.", "DB���� ����", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}//�̺�Ʈ ó�� �޼ҵ� ��
}//SearchBtnEvent Ŭ���� ��



//�������� ������
public class PayInfoWindow extends JFrame implements ActionListener{
	DefaultTableModel model;
	JTable table;
	String salesTableHeader [] = {"��¥", "���� ��ȣ", "�� ���� �ݾ�", "ī�� ���� �ݾ�", "���� ���� �ݾ�", "����Ʈ ���� �ݾ�", "������", "�޴��� ��ȣ", "���"};
	String comboString [] = {"��¥", "������ȣ", "�޴��� ��ȣ", "���"};
	String branchName;
	Vector <String> tableHeader = new Vector<String>();  //���̺� ����� �� ����
	Vector <Vector<String>> tableContents = new Vector <Vector <String>>();  //���̺� �������� �� ���� 
	JTextField jtf;
	JComboBox combo;
	String comboStat="��¥";
	JButton homeBtn;
	FirstWindow firstWin;
	
	//������
	PayInfoWindow(FirstWindow firstWin){ //���������� �޾ƿ�
		setVisible(true);
		setTitle("��������");
		setSize(1040, 700);
		this.firstWin = firstWin;
		
		
		//���̺�
		for (int i=0; i<salesTableHeader.length; i++) {  //���̺� ��� �����
			tableHeader.add(salesTableHeader[i]);
		}
		model = new DefaultTableModel(tableContents, tableHeader); //���̺� �������� ����� defaultTableModel�����
		table = new JTable(model); //���� model�� JTable�� �־� ���̺� ����
		table.getColumn("��¥").setPreferredWidth(80);  //Į�� ũ�� ����
		table.getColumn("���� ��ȣ").setPreferredWidth(160);
		table.getColumn("�� ���� �ݾ�").setPreferredWidth(100);
		table.getColumn("ī�� ���� �ݾ�").setPreferredWidth(100);
		table.getColumn("���� ���� �ݾ�").setPreferredWidth(100);
		table.getColumn("����Ʈ ���� �ݾ�").setPreferredWidth(100);
		table.getColumn("������").setPreferredWidth(80);
		table.getColumn("�޴��� ��ȣ").setPreferredWidth(200);
		table.getColumn("���").setPreferredWidth(80);
		JScrollPane tableScroll = new JScrollPane(table);  //���̺� ��Ŭ���� �߰�
		
		
		//DB�����ؼ� ���� �̸� �޾ƿ�
		try {
			SqlQuery sql = new SqlQuery();
			branchName = sql.getBranchName(firstWin.branchNum);  //�����̸� �޾ƿ�
			sql.sqlClose();
		}
		catch(SQLException e) {
			JOptionPane.showMessageDialog(this, "DB���ῡ ���� �߽��ϴ�.", "DB���� ����", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		
		//��ư, �ؽ�Ʈ�ʵ�, �޺��ڽ�, �����̸� Label ����
		JButton searchBtn = new JButton("�˻�");
		homeBtn = new JButton("Ȩ");
		jtf = new JTextField(20);
		JLabel branchNameLabel = new JLabel(branchName);
		combo = new JComboBox(comboString);  //�޺��ڽ� ����
		combo.setSelectedIndex(0);  //�޺��ڽ� �⺻�� 0���� ����
		
		
		//�̺�Ʈ ������ ���
		combo.addActionListener(this);  //�޺��ڽ� �̺�Ʈ ������ ���
		jtf.addKeyListener(new KeyAdapter() {  //�ؽ�Ʈ �ʵ� ���ڼ� ���� �̺�Ʈ ������ ���
			public void keyTyped(KeyEvent k) {
				int length = ((JTextField)k.getSource()).getText().length();  //�ؽ�Ʈ �ʵ��� ���� ������ ������
				
				//�ؽ�Ʈ �ʵ� ���� ����
				if(length > 12 && combo.getSelectedItem().equals("�޴��� ��ȣ") ) k.consume();
				else if(length > 9 && combo.getSelectedItem().equals("��¥")) k.consume();
				else if(length > 3 && combo.getSelectedItem().equals("���")) k.consume();
				else if(length > 13 && combo.getSelectedItem().equals("������ȣ")) k.consume();
			}
		});
		searchBtn.addActionListener(new SearchBtnEvent(this));  //�˻� ��ư�� �̺�Ʈ ������ ���
		homeBtn.addActionListener(this); //Ȩ��ư�� �̺�Ʈ ������ ���

		
		//��Ʈ����
		Font font = new Font("Arial Rounded MT ����", Font.BOLD, 15); //��Ʈ
		searchBtn.setFont(font);
		homeBtn.setFont(font);
		jtf.setFont(font);
		combo.setFont(font);
		branchNameLabel.setFont(new Font("Arial Rounded MT ����", Font.BOLD, 20));
		
		
		Container c = getContentPane(); //�����̳� ����
		c.setLayout(null);  //�����̳� ���̾ƿ� ����
		c.add(tableScroll); tableScroll.setBounds(12, 108, 1000, 540);  //�����̳ʿ� ���̺� �߰�, ũ��, ��ġ ���� 
		c.add(branchNameLabel);  branchNameLabel.setBounds(12, 0, 500, 108); //�����̳ʿ� ���� Lable �߰�, ũ��, ��ġ ���� 
		c.add(combo); combo.setBounds(512, 42, 110, 25); //�����̳ʿ� �޺��ڽ� �߰�, ũ��, ��ġ ����  
		c.add(jtf);  jtf.setBounds(632, 41, 150, 28); //�����̳ʿ� �ؽ�Ʈ�ʵ� �߰�, ũ��, ��ġ ���� 
		c.add(searchBtn);  searchBtn.setBounds(792, 34, 70, 40); //�����̳ʿ� �˻� ��ư �߰�, ũ��, ��ġ ���� 
		c.add(homeBtn);  homeBtn.setBounds(872, 34, 70, 40); //�����̳ʿ� Ȩ ��ư �߰�, ũ��, ��ġ ���� 
		
	}//������ ��
	
	
	//�޺��ڽ�, Ȩ��ư �̺�Ʈ ó�� �޼ҵ�
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == combo) { //�޺��ڽ� ���� �̺�Ʈ��
			jtf.setText("");  //�ؽ�Ʈ �ʵ� ����
			comboStat = combo.getSelectedItem().toString();  //comboStat �� ������Ʈ
		}
		else if(e.getSource() == homeBtn) { //Ȩ��ư Ŭ���ϸ�
			firstWin.setVisible(true); //ùȭ�� ����
			this.dispose();  //��������â �ݱ�
		}
	}//�޺��ڽ� ��ҹ�ư �̺�Ʈ ó�� �޼ҵ� ��

}//PayInfoWindow Ŭ���� ��
