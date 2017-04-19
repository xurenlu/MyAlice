package com.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.myalice.MyAliceSpringConfig;
import com.myalice.domain.Users;
import com.myalice.services.UsersService;

@RunWith(SpringRunner.class)	
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@ContextConfiguration( classes = MyAliceSpringConfig.class )
public class SpringTest {
	
	@Autowired
	UsersService usersService;
	
	@Test
	public void test(){
		Users users = new Users();
		Page<Users> searchUsers = usersService.searchUsers(1, users) ; 
		System.out.println(JSON.toJSON(searchUsers.getResult()));
	}
	
}