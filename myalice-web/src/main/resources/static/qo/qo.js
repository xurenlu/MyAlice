var loadDataUrl = "/qo/listData";

$(function(){
	var page = $.getWellParam("page");
	var pageNum=1;
	if(page!=""){
		pageNum=page; 
	}
	
	loadDict( function(){
		btnSearch(pageNum);
	})
})

function btnSearch(pageNum){
	var questionContent=$("#questionContent").val();
	var id=$("#id").val();
	loadData(loadDataUrl,{pageNum:pageNum,id:id,questionContent:questionContent})
}

function loadData(url,param){
	$.get(url, param , function(data){
		showData($("#qoData"),data.list) ; 
		$('#pageToolbar').html(""); 
		$('#pageToolbar').Paging({current:data.pageNum,pagesize:data.pageSize,count:data.total,callback:function(page,size,count){
			console.log( '当前第 ' +page +'页,每页 '+size+'条,总页数：'+count+'页' ) ;
			loadData(loadDataUrl , {pageNum:page});   
		}});
	} , "json")
}

function loadDict(callback){
	$.ajax({url:"/pub/orderTypes" ,
				traditional:true,
				dataType:"json" ,
			data:{dtypes:["orderType","orderState"]}
	,success:function(json){
		//$("#questionTypeTd").attr("fun" , html);  
		for(v in json){
			$("#td_" + v).attr("fun" , json[v]); 
		}
		callback();
	} } )
}