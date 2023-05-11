package main;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginWindow extends JFrame {

    Container ct = null;

    JTextField loginid = null; // 로그인 아이디

    JPasswordField password = null; // 로그인 패스워드

    public LoginWindow() {
        ct = getContentPane();
        loginid = new JTextField(20); // 로그인 아이디
        password = new JPasswordField(20); // 로그인 패스워드
        JLabel id = new JLabel("사원번호 :");
        JLabel pwd = new JLabel("비밀번호 :");
        JButton a = new JButton("로그인");
        EnterActionListener enter = new EnterActionListener(this);
        a.addActionListener(enter);
        password.addActionListener(enter);

        ct.setLayout(null);
        loginid.setBounds(130, 40, 150, 30);
        ct.add(loginid);
        password.setBounds(130, 80, 150, 30);
        ct.add(password);
        id.setBounds(20, 40, 70, 30);
        ct.add(id);
        pwd.setBounds(20, 80, 70, 30);
        ct.add(pwd);
        a.setBounds(300, 40, 80, 70);
        ct.add(a);
        setTitle("로그인");
        setSize(410, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginWindow();
    }

    class EnterActionListener implements ActionListener  { //로그인 버튼 클릭 이벤트 클래스
    	LoginWindow login;
    	
    	//생성자
    	EnterActionListener(LoginWindow login){
    		this.login = login;
    	}
    	
        public void actionPerformed(ActionEvent ae) {  //이벤트 처리 메소드
        	String id = loginid.getText();
        	String passwd = password.getText();
        	try {
        		SqlQuery sql = new SqlQuery();
            	
                if (sql.login(id, passwd)) { //로그인 성공
                    new FirstWindow(id);
                    login.dispose();
                } 
                else JOptionPane.showMessageDialog(ct, "사원번호와 비밀번호를 다시 입력하세요", "로그인 실패", JOptionPane.ERROR_MESSAGE, null);  //로그인 실패
                sql.sqlClose();  //sql연결 종료
        	}
        	catch(SQLException e) { JOptionPane.showMessageDialog(ct, "DB연결에 실패 했습니다.", "DB연결 실패", JOptionPane.ERROR_MESSAGE, null); }  //DB연결실패
        	
        }  //actionPerformed 메소드 끝
    } //EnterActionListener 클래스 끝
} //LoginWindow 클래스 끝