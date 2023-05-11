package main;

//Book클래스
class Book{  
	String isbn, name;
	int price;
	
	Book(String isbn, String name, int price){
		this.isbn = isbn;
		this.name = name;
		this.price = price;
	}
} //Book클래스 끝


//Member클래스
class Member{ 
	String name, phone;
	int point;
	
	Member(String name, String phone, int point){
		this.name = name;
		this.phone = phone;
		this.point = point;
	}
}//Member클래스 끝


//Sales (매출) 클래스
class Sales{
	String date;
	int branchNum, totalSales, cardSales, cashSales, pointSales;
	
	//생성자
	Sales(String date, int branchNum, int totalSales, int cardSales, int cashSales, int pointSales){
		this.date = date;
		this.branchNum = branchNum;
		this.totalSales = totalSales;
		this.cardSales = cardSales;
		this.cashSales = cashSales;
		this.pointSales = pointSales;
	}
}//Sales 클래스 끝


//결제정보 클래스
class PayInfo{
	String date, phone;
	int totalPay, cardPay, cashPay, pointPay, addPoint, empNum;
	long payNum;
	
	//생성자
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
}//PayInfo 클래스 끝


