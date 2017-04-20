package com.test;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.myalice.MyAliceSpringConfig;
import com.myalice.domain.QuestionOrder;
import com.myalice.services.QuestionOrderService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration( classes = MyAliceSpringConfig.class )
public class QuestionOrderTest {

    @Autowired
    QuestionOrderService questionOrderService;
    @Test
    public void insert(){
    	QuestionOrder questionOrder = new QuestionOrder();
    	questionOrder.setQuestionContent("我想提一个问题");
    	questionOrder	.setQuestionSummary("都行");
    	questionOrder.setQuestionType((byte)0);
    	questionOrder.setState((byte)0);
    	questionOrderService.insert(questionOrder, Arrays.asList("aa") ); ;
    }



}
