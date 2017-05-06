package com.myalice.mapping;

import java.util.List;
import java.util.Map;

import com.myalice.domain.ElasticsearchData;

public interface IElasticsearch {
	
	public boolean add(Map<String, Object> data);

	public boolean adds(List<Map<String, Object>> datas);

	public boolean remove(String index);

	public boolean removes(String... index);
	
	public void query(ElasticsearchData searchData);
}
