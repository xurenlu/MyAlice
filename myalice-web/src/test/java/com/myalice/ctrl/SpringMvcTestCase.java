package com.myalice.ctrl;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.myalice.App;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(webEnvironment=WebEnvironment.MOCK)
@ContextConfiguration( classes = App.class )
@AutoConfigureMockMvc
public class SpringMvcTestCase {
	
	@Autowired
	protected MockMvc mockMvc;
	
	
	@Test
	public void test()throws Exception{
		MvcResult andReturn = mockMvc.perform(MockMvcRequestBuilders.get("/list")).andReturn() ;
		int status = andReturn.getResponse().getStatus() ;
		System.out.println("----");
		System.out.println( "response status:" + status );
		System.out.println( "response status content:" + andReturn.getResponse().getContentAsString() );
		System.out.println("----");
	}
	
	
	@Test
	public void testInsertUser()throws Exception{
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<String, String>();
		valueMap.put("username", Arrays.asList("hpgary1")); 
		valueMap.put("password", Arrays.asList("123456")); 
		valueMap.put("password1", Arrays.asList("123456"));
		valueMap.put("email", Arrays.asList("garhp@qq.com"));
		valueMap.put("name", Arrays.asList("garhp@qq.com"));
		MvcResult andReturn = mockMvc.perform(MockMvcRequestBuilders.post("/admin/user/insert").params(valueMap)).andReturn() ;
		System.out.println("-------------");
		System.out.println(andReturn.getResponse().getContentAsString());
		System.out.println("-------------");
	}
	
	@Test
	public void testUpdateUser()throws Exception{
		MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<String, String>();
		valueMap.put("username", Arrays.asList("hpgary1")); 
		valueMap.put("password", Arrays.asList("123457")); 
		valueMap.put("password1", Arrays.asList("123457"));
		valueMap.put("email", Arrays.asList("garhp@qq.com"));
		valueMap.put("id", Arrays.asList("2dea00a36aa64d6d9c36cc3e3ef3f770"));
		valueMap.put("name", Arrays.asList("garhp@qq.com"));
		MvcResult andReturn = mockMvc.perform(MockMvcRequestBuilders.post("/admin/user/update").params(valueMap)).andReturn() ;
		System.out.println("-------------");
		System.out.println(andReturn.getResponse().getContentAsString());
		System.out.println("-------------");
	}
	
	
}
