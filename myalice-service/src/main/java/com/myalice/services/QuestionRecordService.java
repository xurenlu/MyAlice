package com.myalice.services;

import com.myalice.domain.QuestionRecord;
import com.myalice.mapping.QuestionRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionRecordService {

    @Autowired
    QuestionRecordMapper questionRecordMapper;

    public void insert(QuestionRecord record) {
        this.questionRecordMapper.insert(record);
    }

}
