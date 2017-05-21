
$( function(){
	loadOrderType(); 
	
	$("#btnSubmit").click(function(){
		
		var questionType = $("#questionType").val();
		var question = $("#question").val();
		var answer = $("#answer").val();
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
		
		$.mypost("/admin/question/add" , {questionType:questionType , question:question ,
			anwser:answer} , function(json){
			if(json.suc){
				window.location="/admin/question/listData.html";
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
	if(null == id){
		return ;
	}
	$.mypost("/admin/question/load" , {id:id} ,function(data){
		$("#questionType").val(data.questionType); 
		$("#question").val(data.title); 
		$("#answer").val(data.anwser);
		$("#id").val(data.question_id);
	},"json")
}