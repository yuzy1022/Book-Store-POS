package main;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;


//Ȯ�� ��ư�� �������� �Է��� ��ȭ��ȣ�� ȸ���� ã�� �̺�Ʈ Ŭ����
class SearchMemBtnEvent implements ActionListener{ 
	MainWindow mainWin;
	MemberSearchWindow win;
	JLabel label;
	JTextField inputField;
	
	//������
	SearchMemBtnEvent(MainWindow mainWin, MemberSearchWindow win, JTextField inputField, JLabel label){  
		this.mainWin = mainWin;
		this.win = win;
		this.label = label;
		this.inputField = inputField;
	}//������ ��
	
	
	//�̺�Ʈ ó�� �޼ҵ�
	public void actionPerformed(ActionEvent e) {  
		try {
			String phone = inputField.getText();
			SqlQuery sql = new SqlQuery();
			Member mem = sql.searchMember(phone);  //��ȭ��ȣ�� DB�� �˻��� ȸ�������� Member ��ü�� �޾ƿ�
			
			if(mem != null) {  //�ش� ��ȣ�� ȸ���� ���� ���
				mainWin.setMemberInfo(mem); //MainWindow�� ȸ������ Label�� ������Ʈ
				sql.sqlClose(); //sql���� ����
				win.dispose();  //ȸ����ȸ window ����
			}
			else {  //�ش� ��ȣ�� ȸ���� ã�� ���ϸ�
				label.setText("ȸ���� �ƴմϴ�."); //label�� ȸ�� �ƴ� ���
				inputField.setText("");
			}
			
		}
		catch(SQLException ex) {  //sql���� ���н� ���â ���
			JOptionPane.showMessageDialog(win, "DB���ῡ ���� �߽��ϴ�.", "DB���� ����", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}//�̺�Ʈ ó�� �޼ҵ� ��
} //SearchBtnEvent Ŭ���� ��



//Window â ���� Ŭ����
public class MemberSearchWindow extends JFrame implements ActionListener{  
	JTextField phoneNumberInput = new JTextField();
	String buttonName [] = {"Ȯ��", "���"};
	JButton button [] = new JButton[2];
	JLabel label = new JLabel();
	MainWindow mainWin;
	JPanel buttonPanel = new JPanel();
	
	
	//���� ȭ���� �Ű������� �޴� ������ 
	MemberSearchWindow(MainWindow mainWin){ 
		this.mainWin = mainWin;
		setTitle("ȸ����ȸ");  
		setVisible(true);  
		setSize(380, 150);
		Container c = getContentPane();
		Font font = new Font("Sans-serif", Font.BOLD, 15);  //��Ʈ����
		phoneNumberInput.setFont(font);  //��ȭ��ȣ �Է��ʵ� ��Ʈ ����
		label.setHorizontalAlignment(JLabel.CENTER);  //label ��� ����
		label.setFont(font);  //label ��Ʈ ����
		
		buttonPanel.setLayout(new FlowLayout());  //��ư�г� �÷ο췹�̾ƿ� ����
		for (int i = 0; i < button.length; i++) {  //��ư �ʱ�ȭ, ��ư �гο� ��ư�߰�, ��Ʈ����
			button[i] = new JButton(buttonName[i]);
			button[i].setFont(font);
			buttonPanel.add(button[i]);
		}
		button[0].addActionListener(new SearchMemBtnEvent(mainWin, this, phoneNumberInput, label));  //Ȯ�� ��ư �̺�Ʈ ������ ���
		button[1].addActionListener(this);   //��ҹ�ư �̺�Ʈ ������ ���
		
		
		c.setLayout(null); //�����̳� ���̾ƿ� ����
		c.add(phoneNumberInput); phoneNumberInput.setBounds(10, 10, 200, 30);  //��ȭ��ȣ �Է��ʵ� �����̳ʿ� add, ũ�� ��ġ ����
		c.add(buttonPanel);  buttonPanel.setBounds(195, 5, 180, 40);  //��ư�г� �����̳ʿ� add, ũ�� ��ġ ����
		c.add(label);  label.setBounds(10, 60, 350, 30);  //label �����̳ʿ� add, ũ�� ��ġ ����
		
		phoneNumberInput.addKeyListener(new KeyAdapter() {  //��ȭ��ȣ �ڸ��� ����
			public void keyTyped(KeyEvent k) {
				if(((JTextField)k.getSource()).getText().length() > 12 )
					k.consume();
			}
		});
	}
	
	
	
	//��� ��ư ������ �� �̺�Ʈ ó�� �޼ҵ�
	public void actionPerformed(ActionEvent e) {  
		this.dispose();
	}


}//MemberSearchWindow Ŭ���� ��