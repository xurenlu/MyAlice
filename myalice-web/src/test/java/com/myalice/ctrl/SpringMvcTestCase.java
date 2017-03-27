package com.myalice.ctrl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.myalice.App;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
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
}
