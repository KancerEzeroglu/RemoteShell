package com.exam;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Database_1 {

	List<Record> records = new ArrayList<Record>();
	
	 HashMap<String, ArrayList<Integer>> firstNameIndex = new HashMap<String, ArrayList<Integer>>();
	 HashMap<String, ArrayList<Integer>> departmentIndex = new HashMap<String, ArrayList<Integer>>();
	 HashMap<String, ArrayList<Integer>> telNumIndex = new HashMap<String, ArrayList<Integer>>();
	 HashMap<String, ArrayList<Integer>> endDateIndex = new HashMap<String, ArrayList<Integer>>();
	
	
	public void printAllElement(ArrayList<Record> records) {
		for (int i = 0; i < records.size(); i++) {
			System.out.println(records.get(i).getFirstName());
		}
	}

	public void printAllElement(Record[] records) {
		for (int i = 0; i < records.length; i++) {
			System.out.println(records[i]);
		}
	}

	public int loadData(String fileName) {
		// Open the file
		FileInputStream fstream;
		BufferedReader br = null;

		try {
			fstream = new FileInputStream(fileName);
			br = new BufferedReader(new InputStreamReader(fstream));

			String strLine;

			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				String[] tokens = strLine.split(";");
				// validate metodunu cagir. insert etmeden kontrol et.

				if (validate(tokens)) {
					Record r = new Record();
					r.setUserName(tokens[0]);
					r.setFirstName(tokens[1]);
					r.setLastName(tokens[2]);
					r.setDepartment(tokens[3]);
					r.setTelNum(tokens[5]);

					// convert string to date format
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					String dateInString = tokens[4];
					Date date = null;
					try {
						date = df.parse(dateInString);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					r.setEndDate(date);

					records.add(r);
				} else {
					System.out.println("Kayit hatalidir.");
				}

			}
			// record degerleri endDate'e gore sort edildi.
		Collections.sort(records,new DateComparator());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		updatedIndexes(records);
		return records.size();
	}

	private void updatedIndexes(List<Record> records2) {
		
		//findByName fonksiyonu icin calisan for dongusu
		for (int a=0;a<records.size();a++){
			String key = records.get(a).getFirstName()+"-"+records.get(a).getLastName();
			ArrayList<Integer> uNameIndexes = firstNameIndex.get(key);
			if(uNameIndexes==null){
				firstNameIndex.put(key,new ArrayList<Integer>());
				firstNameIndex.get(key).add(a);
			}
			else{
				firstNameIndex.get(key).add(a);
			}
		}
		for(int b=0;b<records.size();b++){
			String key = records.get(b).getDepartment();
			ArrayList<Integer> departIndexes = departmentIndex.get(key);
			if(departIndexes==null){
				departmentIndex.put(key, new ArrayList<Integer>());
				departmentIndex.get(key).add(b);
			}
			else{
				departmentIndex.get(key).add(b);
			}
		}
		for(int c=0;c<records.size();c++){
			String key = records.get(c).getTelNum();
			ArrayList<Integer> telIndexes = telNumIndex.get(key);
			if(telIndexes==null){
				telNumIndex.put(key, new ArrayList<Integer>());
				telNumIndex.get(key).add(c);
			}
			else{
				telNumIndex.get(key).add(c);
			}
		}
	}

	// Kontrollerin yapildigi fonksiyon
	private boolean validate(String[] tokens) {

		boolean flag = false;
		int nameLen = tokens[0].length();

		flag = tokens[0].matches("\\A\\p{ASCII}*\\z");
		if (!flag) {
			System.out.println(tokens[0]
					+ " User name ASCII karakter icermektedir.");
			return false;
		}
		if (nameLen < 6 || nameLen > 10) {
			System.out.println(tokens[0]
					+ " User name karater uzunlugu uymuyor.");
			return false;
		}

		if (!tokens[1].matches("[a-zA-Z]+") || !tokens[2].matches("[a-zA-Z]+")) {
			System.out.println(tokens[1] + " " + tokens[2]
					+ " first name ve last name karakter icermiyor.");
			return false;
		}
		// final byte[] utf8Bytes = string.getBytes("UTF-8");
		byte[] utf8FirstName = null;
		byte[] utf8LastName = null;
		try {
			utf8FirstName = tokens[1].getBytes("UTF-8");
			utf8LastName = tokens[2].getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (utf8FirstName.length > 30 || utf8LastName.length > 30) {
			System.out.println(tokens[1] + " " + tokens[2]
					+ " first name ve last name 30 byte'dan fazla");
			return false;
		}
		if (!isAsciiPrintable(tokens[3])) {
			System.out.println("printable degil");
			return false;
		}
		// depart alani icin 20 byte ve UTF-8 olma kontrolu yapildi
		byte[] utf8Depart = null;

		try {
			utf8Depart = tokens[3].getBytes("UTF-8");

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (utf8Depart.length > 20) {
			System.out.println("byte 20'den buyuk depart");
			return false;
		}
		if(!isSuitableTel(tokens[5])){
			System.out.println(tokens[0]+" "+tokens[1]+" "+tokens[2]+" "+"Telefon numarasi uygun degildir");
			return false;
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dateInString = tokens[4];
		Date date = null;
		try {
			date = df.parse(dateInString);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	// departman alanı için printable kontrolu yapildi
	public boolean isAsciiPrintable(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (isAsciiPrintable(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	public boolean isAsciiPrintable(char ch) {
		return ch >= 32 && ch < 127;
	}

	// telefon numarasi kontrol
	public boolean isSuitableTel(String tel) {
		
		int telSize = tel.length();
		
		if (tel == null || telSize > 25) {
			return false;
		}
		
		// tel numarasindan bosluklari kaldir
		String telNumNew = tel.replaceAll("\\s+", "");
		if (telNumNew.length() == 13 && telNumNew.charAt(0) == '+') {
			for (int i = 1; i < telNumNew.length(); i++) {
				if (!Character.isDigit(telNumNew.charAt(i))) {
					return false;
				}
			}
		}else{
			return false;
		}
		return true;
	}

	public Record[] findByName(String firstname, String lastname) {

		ArrayList<Record> matchRecords = new ArrayList<Record>();
		
		String key = firstname+"-"+lastname;
		ArrayList<Integer> indexes = firstNameIndex.get(key);
		if(indexes != null){
		for (int s =0;s<indexes.size();s++){
			matchRecords.add(records.get(indexes.get(s)));	
			}
		}
		Record[] recordArr = new Record[matchRecords.size()];
		recordArr = matchRecords.toArray(recordArr);
		Arrays.sort(recordArr,new DateComparator());
		return recordArr;
	}
	
	public Record[] findByDepartment(String department){
		
		ArrayList<Record> matchRecordsDepart = new ArrayList<Record>();
		String key = department;
		ArrayList<Integer> indexes = departmentIndex.get(key);
		if(indexes != null){
		for(int g=0;g<indexes.size();g++){
			matchRecordsDepart.add(records.get(indexes.get(g)));
		}
		}
		Record[] recordDepartArr = new Record[matchRecordsDepart.size()];
		recordDepartArr = matchRecordsDepart.toArray(recordDepartArr);
		Arrays.sort(recordDepartArr,new NameComparator());
		return recordDepartArr;
	}
	
	public Record[] findByTelNum(String telNum){
		
		ArrayList<Record> matchRecordsTel = new ArrayList<Record>();
		String key = telNum;
		ArrayList<Integer> indexes = telNumIndex.get(key);
		if(indexes != null){
		for(int d=0;d<indexes.size();d++){
			matchRecordsTel.add(records.get(indexes.get(d)));
		}
		}
		Record[] recordTelArr = new Record[matchRecordsTel.size()];
		recordTelArr = matchRecordsTel.toArray(recordTelArr);
		return recordTelArr;
	}
	
	public Record[] findEndDate( Date intervalStart, Date IntervalEnd) {
		ArrayList<Record> matchRecordsEndDate = new ArrayList<Record>();
		
		for(int i = 0; i<records.size();i++){
			if(records.get(i).getEndDate().after(intervalStart) && records.get(i).getEndDate().before(IntervalEnd)){
				matchRecordsEndDate.add(records.get(i));
			}
			if(records.get(i).getEndDate().after(IntervalEnd)){
				break;
			}
		}
		Record[] recordEnd = new Record[matchRecordsEndDate.size()];
		recordEnd = matchRecordsEndDate.toArray(recordEnd);
		return recordEnd;
	}
}
