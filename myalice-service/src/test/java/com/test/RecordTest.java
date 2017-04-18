package com.test;

import com.myalice.MyAliceSpringConfig;
import com.myalice.domain.QuestionRecord;
import com.myalice.services.QuestionRecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration( classes = MyAliceSpringConfig.class )
public class RecordTest {

    @Autowired
    QuestionRecordService questionRecordService;

    @Test
    public void insert(){
        QuestionRecord record = new QuestionRecord();
        record.setId("2");
        record.setCreateTime(new Date());
        record.setCommitUser("user");
        record.setContent("aaaa");
        record.setQuestionOrderId("1");
        record.setUsertype(new Byte("1"));
        questionRecordService.insert(record);
    }



}
