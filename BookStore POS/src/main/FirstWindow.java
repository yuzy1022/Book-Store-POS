package main;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class FirstWindow extends JFrame implements ActionListener{
	String empNum;
	int branchNum = 1000;
	
	//생성자
    public FirstWindow(String num) {
    	empNum = num;
    	
        setTitle("첫 화면");
        setSize(480, 240);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JButton start = new JButton("영업시작");
        JButton end = new JButton("영업마감");
        JButton logout = new JButton("로그아웃");
        JButton payInfo = new JButton("결제정보");
        
        start.addActionListener(this);  //영업시작 버튼 이벤트 리스너 등록
        logout.addActionListener(this);  //로그아웃 버튼 이벤트 리스너 등록
        end.addActionListener(this);  //영업마감 버튼 이벤트 리스너 등록
        payInfo.addActionListener(this);  //결제정보 버튼 이벤트 리스너 등록
        
        Container ct = getContentPane();
        ct.setLayout(null);
        ct.add(logout);
        ct.add(end);
        ct.add(start);
        ct.add(payInfo);
        
        start.setBounds(20, 50, 90, 90);
        end.setBounds(130, 50, 90, 90);
        payInfo.setBounds(240, 50, 90, 90);
        logout.setBounds(350, 50, 90, 90);
        
    }//생성자 끝
    
    
    //이벤트 처리 메소드
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == "영업시작") {
        	new MainWindow(empNum, branchNum, this);
            setVisible(false);
        }
        else if(e.getActionCommand() == "로그아웃") {
        	new LoginWindow(); 
        	this.dispose();
        }
        else if(e.getActionCommand() == "결제정보") {
        	new PayInfoWindow(this);
        	setVisible(false);
        }
        else {  //업업종료 버튼 눌렀을 때
        	try {  
            SqlQuery sql = new SqlQuery();  
            sql.setSalesToDB(branchNum);  //DB에 매출정보 insert  
            sql.sqlClose();
            new SalesWindow(branchNum);  //매출정보 윈도우 띄우기
            this.dispose();  //firstWindow 닫기
            }
            catch (SQLException ex) {
            	JOptionPane.showMessageDialog(this, "DB연결에 실패 했습니다.", "DB연결 실패", JOptionPane.ERROR_MESSAGE);
              	return;
            }
        }
    }//이벤트 처리 메소드 끝
   

}//FirstWindow 클래스 끝