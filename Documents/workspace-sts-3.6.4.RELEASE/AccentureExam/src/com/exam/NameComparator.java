package com.exam;

import java.util.Comparator;

public class NameComparator implements Comparator<Record>{

	@Override
	public int compare(Record o1, Record o2) {
		String lastName1 = o1.getLastName();
		String lastName2 = o2.getLastName();
		
		if(lastName1.compareTo(lastName2)==0){
			String firstName1 = o1.getFirstName();
			String firstName2 = o2.getFirstName();
			
			return firstName1.compareTo(firstName2);
		}
		else{
			return lastName1.compareTo(lastName2);
		}
	}

}
