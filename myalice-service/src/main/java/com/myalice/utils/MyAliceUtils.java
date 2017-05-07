package com.myalice.utils;

public class MyAliceUtils {

	public final static String EMPTY_STR = "";

	public final static String toString(Object vo) {
		if (vo == null) {
			return EMPTY_STR;
		}
		return vo.toString().trim();
	}

	public final static int toInt(Object vo) {
		if (vo == null) {
			return 0;
		}
		String voStr = toString(vo);

		try {
			return Integer.parseInt(voStr);
		} catch (Exception e) {
		}
		return 0;
	}
}
