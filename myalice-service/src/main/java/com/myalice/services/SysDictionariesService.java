package com.myalice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myalice.domain.SysDictionaries;
import com.myalice.mapping.SysDictionariesMapper;

@Service
public class SysDictionariesService {

	@Autowired
	protected SysDictionariesMapper dictionariesMapper;

	public List<SysDictionaries> selectForDType(String dtype) {
		return dictionariesMapper.selectForDType(dtype);
	}
	
	
	public List<SysDictionaries> selectForDTypes(String[]dtype) {
		return dictionariesMapper.selectForDTypes(dtype);
	}
}
