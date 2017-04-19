package com.test;

import com.alibaba.fastjson.JSON;
import com.myalice.MyAliceSpringConfig;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@ContextConfiguration( classes = MyAliceSpringConfig.class )
public class ESTest {
	@Value("${cluster-nodes}")
	private String cluster_nodes;
	@Value("${elasticsearch}")
	private String elasticsearch;
	@Value("${cluster-name}")
	private String cluster_name;
	private Settings settings = null;
	private TransportClient client = null;
	static final String index = "alice-1";
	static final String type = "alice";
	static final String id = "1";
	private LogModel log = new LogModel();
	@Before
	public void before() throws UnknownHostException {
		//设置集群名称
		settings = Settings.builder().put(cluster_name, elasticsearch).build();
		//创建client
		client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(cluster_nodes), 9300));
	}


	@Test
	public void es(){
		System.out.println(JSON.toJSONString(new LogModel()));
		/**
		 * 创建:CREATED
		 * 修改:OK
		 * 查询:{"catIds":[471481195,59759181,225731217,1246770709,842294147],"desc":"中文f997d154-420a-4808-a37f-cebbaa9c3994","host":"10.0.0.1","id":6771264498184729386,"subId":1953865356,"systemName":"oa"}
		 * 删除:OK
		 */
		//在这里创建我们要索引的对象     必须为对象单独指定ID
		IndexResponse create = client.prepareIndex(index, type,id).setSource(JSON.toJSONString(new LogModel())).execute().actionGet();
		System.out.println("创建:"+create.status());//如果存在 status == OK 否则  == CREATE
		UpdateResponse update = client.prepareUpdate(index,type,id).setDoc(JSON.toJSONString(new LogModel())).execute().actionGet();
		System.out.println("修改:"+update.status());//如果存在 status == OK 否则  == ERROR
		GetResponse response = client.prepareGet(index, type, id).execute().actionGet();
		System.out.println("查询:"+response.getSourceAsString());
		DeleteResponse remove = client.prepareDelete(index,type,id).execute().actionGet();
		System.out.println("删除:"+remove.status());//如果删除成功 status == OK
	}

	@After
	public  void after(){
		client.close();//关闭client
	}

}
/**
 * 瞎编的一个模型，跟日志基本没有关系
 * @author donlian
 */
class LogModel {
	//主ID
	private long id;
	//次ID
	private int subId;
	private String systemName;
	private String host;
	//日志描述
	private String desc;
	private List<Integer> catIds;
	public LogModel(){
		Random random = new Random();
		this.id = Math.abs(random.nextLong());
		int subId = Math.abs(random.nextInt());
		this.subId = subId;
		List<Integer> list = new ArrayList<Integer>(5);
		for(int i=0;i<5;i++){
			list.add(Math.abs(random.nextInt()));
		}
		this.catIds = list;
		this.systemName = subId%1 == 0?"oa":"cms";
		this.host = subId%1 == 0?"10.0.0.1":"10.2.0.1";
		this.desc = "中文" + UUID.randomUUID().toString();
	}
	public LogModel(long id,int subId,String sysName,String host,String desc,List<Integer> catIds){
		this.id = id;
		this.subId = subId;
		this.systemName = sysName;
		this.host = host;
		this.desc = desc;
		this.catIds = catIds;
	}

	public long getId() {
		return id;
	}

	public int getSubId() {
		return subId;
	}

	public String getSystemName() {
		return systemName;
	}

	public String getHost() {
		return host;
	}

	public String getDesc() {
		return desc;
	}

	public List<Integer> getCatIds() {
		return catIds;
	}
}