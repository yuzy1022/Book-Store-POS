package main;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;


//현금 탭
class MoneyPanel extends JPanel implements ActionListener {
	PaymentWindow win;
	MainWindow mainWin;
	JTextField pay = new JTextField();
	
    public MoneyPanel(PaymentWindow win, MainWindow mainWin) {
    	this.win = win;
    	this.mainWin = mainWin;
        Font Bold = new Font("", Font.BOLD, 20);
        JButton ok = new JButton("확인");
        JButton cancel = new JButton("취소");
        JLabel payLabel = new JLabel("결제금액");
        JButton balance = new JButton("잔액");
        setLayout(null);
        
        add(ok);  ok.setBounds(140, 110, 100, 40);
        add(cancel);  cancel.setBounds(260, 110, 100, 40);
        add(pay);  pay.setBounds(126, 50, 210, 30);
        add(payLabel);  payLabel.setBounds(20, 45, 100, 40); 
        add(balance);  balance.setBounds(20, 110, 100, 40);  
        
        ok.addActionListener(this);
        cancel.addActionListener(this);
        balance.addActionListener(this);
        
        ok.setFont(Bold);
        cancel.setFont(Bold);
        ok.setFont(Bold);
        payLabel.setFont(Bold);
        balance.setFont(Bold);
        pay.setFont(Bold);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("확인")) {
        	if(pay.getText().equals("")) { //금액을 입력하지 않았으면
        		JOptionPane.showMessageDialog(this, "금액을 입력 해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
        		return;
			}
            mainWin.cashPay = Integer.parseInt(pay.getText());   //메인 윈도우의 현금 결제액에 결제할 금액 저장
            mainWin.updatePayInfo(); //결제정보 업데이트
            win.dispose(); //결제창 닫기
        } 
        else if (e.getActionCommand().equals("취소")) win.dispose();
        else if(e.getActionCommand().equals("잔액")) {
        	int balance = mainWin.totalPay - mainWin.cardPay - mainWin.pointPay;
        	pay.setText(Integer.toString(balance));
        }
    }
}//현금 탭 끝


//포인트 탭
class PointPanel extends JPanel implements ActionListener {
	PaymentWindow win;
	MainWindow mainWin;
	JTextField pay = new JTextField();
	
    public PointPanel(PaymentWindow win, MainWindow mainWin) {
    	this.win = win;
    	this.mainWin = mainWin;
        Font Bold = new Font("", Font.BOLD, 20);
        JButton ok = new JButton("확인");
        JButton cancel = new JButton("취소");
        JButton balance = new JButton("잔액");
        JLabel payLabel = new JLabel("결제금액");
        setLayout(null);
        
        add(cancel);  cancel.setBounds(260, 110, 100, 40);
        add(ok);  ok.setBounds(140, 110, 100, 40);
        add(pay);  pay.setBounds(126, 50, 210, 30);
        add(payLabel);  payLabel.setBounds(20, 45, 100, 40);
        add(balance);  balance.setBounds(20, 110, 100, 40);  
        
        ok.addActionListener(this);
        cancel.addActionListener(this);
        balance.addActionListener(this);
        
        ok.setFont(Bold);
        cancel.setFont(Bold);
        ok.setFont(Bold);
        payLabel.setFont(Bold);
        balance.setFont(Bold);
        pay.setFont(Bold);
    }
    
    //이벤트 처리 메소드
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("확인")) {
        	int pointPay, totalPrice;
        	if(pay.getText().equals("")) { //금액을 입력하지 않았으면
        		JOptionPane.showMessageDialog(this, "금액을 입력 해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
        		return;
			}
        	else {  //입력 했으면
        		pointPay = Integer.parseInt(pay.getText()); //포인트로 결제할 금액 저장
            	totalPrice = mainWin.totalPay;  //받아야 될 금액 저장
        	}
        	
        	if(mainWin.mem == null) {  //회원조회가 되지 않았으면
        		JOptionPane.showMessageDialog(this, "회원조회를 먼저 해주세요", "오류", JOptionPane.ERROR_MESSAGE);
        		return;
    		}
        	else if(mainWin.mem.point < pointPay) {  //결제할 포인트보다 가진 포인트가 적다면
        		JOptionPane.showMessageDialog(this, "포인트가 부족합니다.", "오류", JOptionPane.ERROR_MESSAGE);
        		return;
        	}
        	else if(pointPay <= totalPrice) {  //받아야 될 금액보다 포인트 결제 금액이 적거나 같을 경우
            	mainWin.pointPay = pointPay; //메인 윈도우의 포인트 결제금액에 저장
            	mainWin.updatePayInfo();  //결제정보 업데이트
            	win.dispose();  //결제창 닫기
            }
            else { //받아야 될 금액보다 포인트 결제 금액이 많을 경우
            	JOptionPane.showMessageDialog(this, "포인트로 총 금액보다 많은 액수를 지불할 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
    			return;
            }
        } 
        else if (e.getActionCommand().equals("취소")) win.dispose();
        else if(e.getActionCommand().equals("잔액")) {
        	int balance = mainWin.totalPay - mainWin.cardPay - mainWin.cashPay;
        	pay.setText(Integer.toString(balance));
        }
    }//이벤트 처리 메소드 끝
}//포인트 탭 끝


//카드 탭
class CardPanel extends JPanel implements ActionListener {
	PaymentWindow win;
	MainWindow mainWin;
	JTextField pay = new JTextField();
	JTextField [] cardNum = new JTextField[4];
	
	//생성자
    public CardPanel(PaymentWindow win, MainWindow mainWin) {
    	this.win = win;
    	this.mainWin = mainWin;
        Font Bold = new Font("", Font.BOLD, 20);
        JPanel card = new JPanel();
        JButton ok = new JButton("확인");
        JButton cancel = new JButton("취소");
        JButton balance = new JButton("잔액");
        JLabel cardLabel = new JLabel("카드번호");
        JLabel payLabel = new JLabel("결제금액");
        
        
        for(int i=0; i<cardNum.length; i++) {
        	cardNum[i] = new JTextField(4);
        	card.add(cardNum[i]);
        	cardNum[i].addKeyListener(new KeyAdapter() {  //카드번호 자리수 제한
    			public void keyTyped(KeyEvent k) {
    				if(((JTextField)k.getSource()).getText().length() > 3 )
    					k.consume();
    			}
    		});
        }
        
        setLayout(null);

        ok.addActionListener(this);
        cancel.addActionListener(this);
        balance.addActionListener(this);
        
        
        add(ok);  ok.setBounds(140, 110, 100, 40);  ok.setFont(Bold);
        add(cancel);  cancel.setBounds(260, 110, 100, 40);  cancel.setFont(Bold);
        add(balance);  balance.setBounds(20, 110, 100, 40);  balance.setFont(Bold);
        add(card);  card.setBounds(106, 30, 250, 30);  card.setFont(Bold);
        add(pay);  pay.setBounds(126, 70, 210, 30);  pay.setFont(Bold);
        add(cardLabel);  cardLabel.setBounds(20, 25, 100, 40);  cardLabel.setFont(Bold);
        add(payLabel);  payLabel.setBounds(20, 65, 100, 40);  payLabel.setFont(Bold);
    }

    //이벤트 처리 메소드
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("확인")) {
        	for(int i=0; i<cardNum.length; i++) {  //카드번호 4자리씩 다 입력했는지 확인
        		if(cardNum[i].getText().length() < 4) {
        			JOptionPane.showMessageDialog(this, "카드 번호를 정확하게 입력해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
        			return;
        		}
        	}
        	if(pay.getText().equals("")) { //금액을 입력하지 않았으면
        		JOptionPane.showMessageDialog(this, "금액을 입력 해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
        		return;
			}
        	
        	int cardPay = Integer.parseInt(pay.getText()); //카드로 결제할 금액 저장
        	int totalPrice = mainWin.totalPay;  //받아야 될 금액 저장
        	
        	if(cardPay <= totalPrice) {  //받아야 될 금액보다 카드 결제 금액이 적거나 같을 경우
            	mainWin.cardPay = cardPay; //메인 윈도우의 카드 결제금액에 저장
            	mainWin.updatePayInfo();  //결제정보 업데이트
            	win.dispose();  //결제창 닫기
            }
            else { //받아야 될 금액보다 카드 결제 금액이 많을 경우
            	JOptionPane.showMessageDialog(this, "카드로 총 금액보다 많은 액수를 지불할 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
    			return;
            }
        } 
        else if (e.getActionCommand().equals("취소")) win.dispose();
        else if(e.getActionCommand().equals("잔액")) {
        	int balance = mainWin.totalPay - mainWin.cashPay - mainWin.pointPay;
        	pay.setText(Integer.toString(balance));
        }
        
    }//이벤트 처리 메소드 끝
}//카드 탭 끝


//윈도우 띄우는 클래스
public class PaymentWindow extends JFrame {
	MainWindow mainWin;
	
    public PaymentWindow(MainWindow mainWin) {
    	this.mainWin = mainWin;
        setLocationRelativeTo(null);
        setTitle("결제 수단");
        setSize(400, 250);
        setVisible(true);
        JTabbedPane pay = new JTabbedPane();
        CardPanel card = new CardPanel(this, mainWin);
        PointPanel point = new PointPanel(this, mainWin);
        MoneyPanel money = new MoneyPanel(this, mainWin);
        pay.addTab("카드", card);
        pay.addTab("포인트", point);
        pay.addTab("현금", money);
        Container ct = getContentPane();
        ct.add(pay);
    }

}//PaymentWindow 클래스 끝
