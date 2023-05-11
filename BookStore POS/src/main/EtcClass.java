package main;

//BookŬ����
class Book{  
	String isbn, name;
	int price;
	
	Book(String isbn, String name, int price){
		this.isbn = isbn;
		this.name = name;
		this.price = price;
	}
} //BookŬ���� ��


//MemberŬ����
class Member{ 
	String name, phone;
	int point;
	
	Member(String name, String phone, int point){
		this.name = name;
		this.phone = phone;
		this.point = point;
	}
}//MemberŬ���� ��


//Sales (����) Ŭ����
class Sales{
	String date;
	int branchNum, totalSales, cardSales, cashSales, pointSales;
	
	//������
	Sales(String date, int branchNum, int totalSales, int cardSales, int cashSales, int pointSales){
		this.date = date;
		this.branchNum = branchNum;
		this.totalSales = totalSales;
		this.cardSales = cardSales;
		this.cashSales = cashSales;
		this.pointSales = pointSales;
	}
}//Sales Ŭ���� ��


//�������� Ŭ����
class PayInfo{
	String date, phone;
	int totalPay, cardPay, cashPay, pointPay, addPoint, empNum;
	long payNum;
	
	//������
	PayInfo(String date, long payNum, int totalPay, int cardPay, int cashPay, int pointPay, int addPoint, String phone, int empNum){
		this.date = date;
		this.payNum = payNum;
		this.totalPay = totalPay;
		this.cardPay = cardPay;
		this.cashPay = cashPay;
		this.pointPay = pointPay;
		this.addPoint = addPoint;
		this.phone = phone;
		this.empNum = empNum;
	}
}//PayInfo Ŭ���� ��


