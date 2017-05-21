var isSubmit = false ;
$( function(){
	loadOrderType(); 
	
	$("#btnSubmit").click(function(){
		if(isSubmit){return;}
		var questionType = $("#questionType").val();
		var question = $("#question").val();
		var answer = $("#answer").val();
		var id = $("#id").val();
		if("" ==  questionType){
			bootbox.alert("请选择问题类型"); 
			return ;
		}
		if("" == question){
			bootbox.alert("请输入问题"); 
			return ;
		}
		if("" == answer){
			bootbox.alert("请输入答案"); 
			return ;
		}
		isSubmit = true;
		$.mypost("/admin/question/add" , {questionType:questionType , question:question ,
			anwser:answer,"id":id } , function(json){
				isSubmit=false ; 
			if(json.suc){
				
				window.setTimeout(function(){
					window.location="/admin/question/listData.html";
				},1000)
				return ;
			}
			bootbox.alert(json.msg); 
		},"json")
	});
	
})

function loadOrderType(){
	$.mypost("/pub/orderType" , {type:"orderType"} , function(json){
		var html="<option value=''>请选择</option>"
		for(var i in json){
			html+="<option value='"+i+"'>"+json[i]+"</option>";
		}
		$("#questionType").html(html); 
		
		initUpdate() ;
	},"json")
}


function initUpdate(){ 
	var id = $.getParam("id") ;  
	
	if(null == id || "" == id){
		return ;
	}
	$.mypost("/admin/question/load" , {id:id} ,function(data){
		$("#questionType").val(data.questionType); 
		$("#question").val(data.title); 
		$("#answer").val(data.anwser);
		$("#id").val(data.question_id);
	},"json")
}