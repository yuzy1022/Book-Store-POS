package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.SQLException;
import java.util.Vector;

//�������� ������
public class SalesWindow extends JFrame implements ActionListener{
	DefaultTableModel model;
	JTable table;
	String salesTableHeader [] = {"��¥", "�� ���� �ݾ�", "ī�� ���� �ݾ�", "���� ���� �ݾ�", "����Ʈ ���� �ݾ�"};
	Vector <String> tableHeader = new Vector<String>();  //���̺� ����� �� ����
	Vector <Vector<String>> tableContents = new Vector <Vector <String>>();  //���̺� �������� �� ���� 
	Sales [] sales;
	
	//������
	SalesWindow(int branchNum){  //���� ��ȣ �޾ƿ�
		setVisible(true);
		setTitle("��������");
		setSize(670, 630);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String branchName;
		
		//���̺�
		for (int i=0; i<salesTableHeader.length; i++) {  //���̺� ��� �����
			tableHeader.add(salesTableHeader[i]);
		}
		model = new DefaultTableModel(tableContents, tableHeader); //���̺� �������� ����� defaultTableModel�����
		table = new JTable(model); //���� model�� JTable�� �־� ���̺� ����
		table.getColumn("��¥").setPreferredWidth(100);  //Į�� ũ�� ����
		table.getColumn("�� ���� �ݾ�").setPreferredWidth(140);
		table.getColumn("ī�� ���� �ݾ�").setPreferredWidth(130);
		table.getColumn("���� ���� �ݾ�").setPreferredWidth(130);
		table.getColumn("����Ʈ ���� �ݾ�").setPreferredWidth(130);
		JScrollPane tableScroll = new JScrollPane(table);  //���̺� ��Ŭ���� �߰�
		
		 //DB�� �����ؼ� ���� �̸��� ���� ���� �޾ƿ�
		try {
			SqlQuery sql = new SqlQuery();
			branchName = sql.getBranchName(branchNum);  //�����̸� �޾ƿ�
			sales = sql.getSales(); //���� ���� �޾ƿ�
			sql.sqlClose();
		}
		catch(SQLException e) {
			JOptionPane.showMessageDialog(this, "DB���ῡ ���� �߽��ϴ�.", "DB���� ����", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//���̺� ���� ���� ������ �߰�
		for(int i=sales.length-1; i >= 0; i--) {
			Vector<String> row = new Vector<String>();
			row.add(sales[i].date);
			row.add(Integer.toString(sales[i].totalSales));
			row.add(Integer.toString(sales[i].cardSales));
			row.add(Integer.toString(sales[i].cashSales));
			row.add(Integer.toString(sales[i].pointSales));
			model.addRow(row);
		}
		
		
		//�����̸� Label, ���� ��ư
		Font font = new Font("Arial Rounded MT ����", Font.BOLD, 20); //��Ʈ
		JLabel branchNameLabel = new JLabel(branchName + " ���� ����"); //���� �̸����� Label ����
		JButton btn = new JButton("����"); //���� ��ư
		branchNameLabel.setFont(font);  //���� �̸� label ��Ʈ ����
		btn.setFont(font);  //���� ��ư ��Ʈ ����
		btn.addActionListener(this);  //���� ��ư�� �̺�Ʈ ������ ���
		
		Container c = getContentPane(); //�����̳� ����
		c.setLayout(null);  //�����̳� ���̾ƿ� ����
		
		c.add(tableScroll); tableScroll.setBounds(13,100,630,480);  //�����̳ʿ� ���̺� �߰�, ũ��, ��ġ ���� 
		c.add(branchNameLabel);  branchNameLabel.setBounds(13, 0, 550, 100); //�����̳ʿ� �����̸� Label �߰�, ũ��, ��ġ ����
		c.add(btn);  btn.setBounds(560, 20, 80, 60); //�����̳ʿ� �����ư �߰�, ũ��, ��ġ ����
		
	}//������ ��
	
	//�����ư �̺�Ʈ ó�� �޼ҵ�
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}
}//SalesWindow Ŭ���� ��
