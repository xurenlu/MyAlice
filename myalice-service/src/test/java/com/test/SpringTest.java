package com.test;

import com.myalice.MyAliceSpringConfig;
import com.myalice.services.MyAliceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@ContextConfiguration( classes = MyAliceSpringConfig.class )
public class SpringTest {

	@Autowired
	MyAliceService aliceService;
	//@Autowired EslService eslService;

	@Test
	public void test(){
		System.out.println("======>>>>>"+aliceService.list() ) ;
	}


	
}
