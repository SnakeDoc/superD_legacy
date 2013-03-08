package com.snakedoc.superd;

import com.vanomaly.jutils.SQLiteUtils;
import com.vanomaly.jutils.ReadFileToString;

public class Schema {
	public static void main (String[] args) {
		Schema s = new Schema();
		s.getSchema();
	}
	public String getSchema() {
		ReadFileToString readFile = new ReadFileToString();
		System.out.println(cleanString(readFile.readFromFile("dbSchema.sql"), "\\n"));
		return "";
	}
	public String cleanString(String input, String stripChars) {
		String output = "";
		String[] temp = input.split(stripChars);
		for (int i = 0; i < temp.length; i++) {
			output += temp[i];
		}
		return output;
	}
}
