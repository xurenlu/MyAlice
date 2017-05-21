package com.myalice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myalice.domain.TalkRecord;
import com.myalice.mapping.TalkRecordMapper;
import com.myalice.utils.Tools;

@Service
public class TalkRecordService {

	@Autowired
	protected TalkRecordMapper talkRecordMapper;

	public void insert(TalkRecord talkRecord) {
		talkRecord.setId(Tools.uuid());
		talkRecord.setCreateTime(Tools.currentDate());
		talkRecordMapper.insert(talkRecord);
	}
}
