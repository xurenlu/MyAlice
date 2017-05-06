package com.myalice.utils;

public class MyAliceStringUtils {
	
	public final static String EMPTY_STR = "" ;
	
	public final static String toString(Object vo){
		if(vo == null){
			return EMPTY_STR ;
		}
		return vo.toString().trim();
	}
}
