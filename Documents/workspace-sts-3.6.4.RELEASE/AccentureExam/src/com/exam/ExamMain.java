package com.exam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExamMain {

	
	public static void main(String[] args) {

		Database_1 db = new Database_1();
		
		int dataSize = db.loadData("input_1.csv");
		System.out.println("Toplam kayit sayisi:" + dataSize);
		Record[] recordsName = db.findByName("Kancer", "Gok");
		Record[] recordsDepartment = db.findByDepartment("Engineer");
		Record[] recordsTelNum = db.findByTelNum("+90 534 9627846");
		
		// findEndDate fonksiyonu icin alinacak olan input larin String formatindan Date formatina 
		// donusturulmesi gerekmektedir.
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateInString1 = "2015-06-09";
		String dateInString2 = "2015-06-15";
		Date date1 = null;
		Date date2 = null;
		
		try {
			
			date1 = formatter.parse(dateInString1);
			date2 = formatter.parse(dateInString2);
			
	 
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Record[] recordsEndDate = db.findEndDate(date1,date2);
		
		db.printAllElement(recordsName);
		System.out.println("---------------------");
		db.printAllElement(recordsDepartment);
		System.out.println("---------------------");
		db.printAllElement(recordsTelNum);
		System.out.println("---------------------");
		db.printAllElement(recordsEndDate);
		
	}

}
