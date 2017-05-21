package com.myalice.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.myalice.domain.TalkRecord;
import com.myalice.services.TalkRecordService;

@RestController
@RequestMapping("/admin/talkrecord")
public class AdminTalkRecordCtrl {
	
	@Autowired
	protected TalkRecordService talkRecordService ;
	
	@RequestMapping("data")
	public PageInfo<TalkRecord> data(Integer pageId,TalkRecord record){
		pageId = null == pageId ? 1 : pageId ;
		Page<TalkRecord> list = talkRecordService.list(pageId, record) ;
		return new PageInfo<>(list) ; 
	}
}
