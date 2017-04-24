package com.myalice.ctrl;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myalice.domain.SysDictionaries;

public class Test {
	
	
	public static void main(String[] args) throws Exception {
		ObjectMapper mapper = new ObjectMapper();  
		SysDictionaries df = new SysDictionaries();
		df.setCreatetime(new Date());
		df.setDtype("中国");
		df.setTindex(1);
		df.setTname("提欧"); 
		df.setDtype("zz");
		df.setId("zzz");
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) ;
		String writeValueAsString = mapper.writeValueAsString(df) ;
		
		System.out.println(writeValueAsString);
	}
}
