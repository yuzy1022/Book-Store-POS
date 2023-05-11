package main;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;


//확인 버튼을 눌렀을때 입력한 전화번호로 회원을 찾는 이벤트 클래스
class SearchMemBtnEvent implements ActionListener{ 
	MainWindow mainWin;
	MemberSearchWindow win;
	JLabel label;
	JTextField inputField;
	
	//생성자
	SearchMemBtnEvent(MainWindow mainWin, MemberSearchWindow win, JTextField inputField, JLabel label){  
		this.mainWin = mainWin;
		this.win = win;
		this.label = label;
		this.inputField = inputField;
	}//생성자 끝
	
	
	//이벤트 처리 메소드
	public void actionPerformed(ActionEvent e) {  
		try {
			String phone = inputField.getText();
			SqlQuery sql = new SqlQuery();
			Member mem = sql.searchMember(phone);  //전화번호로 DB를 검색해 회원정보를 Member 객체로 받아옴
			
			if(mem != null) {  //해당 번호의 회원이 있을 경우
				mainWin.setMemberInfo(mem); //MainWindow의 회원정보 Label을 업데이트
				sql.sqlClose(); //sql연결 종료
				win.dispose();  //회원조회 window 닫음
			}
			else {  //해당 번호로 회원을 찾지 못하면
				label.setText("회원이 아닙니다."); //label에 회원 아님 출력
				inputField.setText("");
			}
			
		}
		catch(SQLException ex) {  //sql연결 실패시 경고창 출력
			JOptionPane.showMessageDialog(win, "DB연결에 실패 했습니다.", "DB연결 실패", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}//이벤트 처리 메소드 끝
} //SearchBtnEvent 클래스 끝



//Window 창 띄우는 클래스
public class MemberSearchWindow extends JFrame implements ActionListener{  
	JTextField phoneNumberInput = new JTextField();
	String buttonName [] = {"확인", "취소"};
	JButton button [] = new JButton[2];
	JLabel label = new JLabel();
	MainWindow mainWin;
	JPanel buttonPanel = new JPanel();
	
	
	//메인 화면을 매개변수로 받는 생성자 
	MemberSearchWindow(MainWindow mainWin){ 
		this.mainWin = mainWin;
		setTitle("회원조회");  
		setVisible(true);  
		setSize(380, 150);
		Container c = getContentPane();
		Font font = new Font("Sans-serif", Font.BOLD, 15);  //폰트설정
		phoneNumberInput.setFont(font);  //전화번호 입력필드 폰트 설정
		label.setHorizontalAlignment(JLabel.CENTER);  //label 가운데 정렬
		label.setFont(font);  //label 폰트 설정
		
		buttonPanel.setLayout(new FlowLayout());  //버튼패널 플로우레이아웃 설정
		for (int i = 0; i < button.length; i++) {  //버튼 초기화, 버튼 패널에 버튼추가, 폰트설정
			button[i] = new JButton(buttonName[i]);
			button[i].setFont(font);
			buttonPanel.add(button[i]);
		}
		button[0].addActionListener(new SearchMemBtnEvent(mainWin, this, phoneNumberInput, label));  //확인 버튼 이벤트 리스너 등록
		button[1].addActionListener(this);   //취소버튼 이벤트 리스너 등록
		
		
		c.setLayout(null); //컨테이너 레이아웃 설정
		c.add(phoneNumberInput); phoneNumberInput.setBounds(10, 10, 200, 30);  //전화번호 입력필드 컨테이너에 add, 크기 위치 설정
		c.add(buttonPanel);  buttonPanel.setBounds(195, 5, 180, 40);  //버튼패널 컨테이너에 add, 크기 위치 설정
		c.add(label);  label.setBounds(10, 60, 350, 30);  //label 컨테이너에 add, 크기 위치 설정
		
		phoneNumberInput.addKeyListener(new KeyAdapter() {  //전화번호 자리수 제한
			public void keyTyped(KeyEvent k) {
				if(((JTextField)k.getSource()).getText().length() > 12 )
					k.consume();
			}
		});
	}
	
	
	
	//취소 버튼 눌렀을 때 이벤트 처리 메소드
	public void actionPerformed(ActionEvent e) {  
		this.dispose();
	}


}//MemberSearchWindow 클래스 끝