package com.myalice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.myalice.domain.QuestionOrder;
import com.myalice.mapping.QuestionOrderMapper;

@Service
public class QuestionOrderService {
	
	@Autowired
	protected QuestionOrderMapper questionOrderMapper ;
	
	public Page<QuestionOrder> list(int pageId){
		Page<QuestionOrder> startPage = PageHelper.startPage(pageId, 10);
		questionOrderMapper.query(); 
		return startPage ;
	}
}
