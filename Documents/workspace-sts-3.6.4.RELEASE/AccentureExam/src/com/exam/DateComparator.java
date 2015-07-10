package com.exam;

import java.util.Comparator;
import java.util.Date;

public class DateComparator implements Comparator<Record>{

	@Override
	public int compare(Record o1, Record o2) {
		Date date1 = o1.getEndDate();
		Date date2 = o2.getEndDate();

			return date1.compareTo(date2);
		}
}
