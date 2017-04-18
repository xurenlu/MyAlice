package com.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.myalice.MyAliceSpringConfig;
import com.myalice.services.QuestionOrderService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@ContextConfiguration( classes = MyAliceSpringConfig.class )
public class SpringTest {
	
	@Autowired
	QuestionOrderService questionOrderService;

	@Test
	public void test(){
		System.out.println("======>>>>>"+questionOrderService.list(1) ) ;
	}
	
}
