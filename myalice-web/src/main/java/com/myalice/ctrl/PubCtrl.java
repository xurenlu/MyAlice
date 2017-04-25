package com.myalice.ctrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myalice.domain.SysDictionaries;
import com.myalice.services.SysDictionariesService;

@RequestMapping("/pub")
@RestController
public class PubCtrl {
	protected static Logger logger = org.slf4j.LoggerFactory.getLogger("ctrl") ; 

	@Autowired
	protected SysDictionariesService dictionariesService;

	@RequestMapping("/orderType")
	public Map<Integer, String> list(String type) {
		if(StringUtils.isEmpty(type)){
			type="orderType";
		}
		List<SysDictionaries> selectForDType = dictionariesService.selectForDType(type); 
		if (null != selectForDType) {
			Map<Integer, String> collect = selectForDType.parallelStream().collect(HashMap<Integer, String>::new,
					(map, item) -> {
						map.put(item.getTindex(), item.getTname());
					}, (m1, m2) -> {
						m1.putAll(m2);
					});
			return collect;
		}
		return null;
	}

	@RequestMapping("/orderTypes")
	public Map<String, String> list(String[] dtypes) {
		Map<String, String> returnResult = new HashMap<>();
		try {
			
		
		List<SysDictionaries> selectForDType = dictionariesService.selectForDTypes(dtypes);
		if (null != selectForDType) {
			ObjectMapper mapper = new ObjectMapper();  
			Map<String, Map<Integer, String>> collect = selectForDType.stream().collect(HashMap<String, Map<Integer, String>>::new,
			(map, item) -> {
				String dtype = item.getDtype() ;
				Map<Integer, String> result = map.get(dtype);
				result=null==result?new HashMap<>():result;
				result.put(item.getTindex(), item.getTname()) ;
				map.put(dtype, result) ;
			}, (m1, m2) -> {
				m1.putAll(m2);
			});
			
			for(Map.Entry<String, Map<Integer, String>> entry:collect.entrySet()){
				returnResult.put(entry.getKey(), mapper.writeValueAsString(entry.getValue())) ; 
			}
		}
		} catch (Exception e) {
			logger.error("/pub/orderTypes resion:" + e.getMessage() , e);
		}
		return returnResult;
	}

}
