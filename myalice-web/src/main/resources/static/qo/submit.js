$( function(){
	loadOrderType();
	$("#btnSubmit").click( function(){
		var formData = new FormData($('#subForm')[0]); 
		var token = Cookies.get("XSRF-TOKEN");
		
		var questionType = $("#questionType").val();
		if(questionType==""){
			bootbox.alert("请选择问题分类");
			return;
		}
		var questionContent = $("#questionContent").val();
		if(questionContent==""){
			bootbox.alert("请输入问题描述");
			return;
		}
		
		$.ajax({
			"url":"/qo/upload" ,
			enctype: 'multipart/form-data',
			type: 'POST',
			headers: {
		    	"X-XSRF-TOKEN":token 
		    },
		    data: formData ,
		    cache: false ,
		    dataType:"json" ,
	        contentType: false ,
	        processData: false ,
	        xhr: function() { 
	            myXhr = $.ajaxSettings.xhr();
	            if(myXhr.upload){
	                myXhr.upload.addEventListener('progress',function(e){
	                	if(e.lengthComputable){
	                		//$('progress').attr({value:e.loaded,max:e.total});
	                		console.log( "loaded:" + e.loaded + ", total:" + e.total ) ; 
	                	}
	                }, false);
	            }
	            return myXhr;
	        },
	        success:function(responseText){
	        	if(!responseText.suc){
	        		bootbox.alert(responseText.msg); 
	        	}else{
	        		bootbox.alert(responseText.msg);
	        	}
	        },
	        error:function(e){
	        	alert( "xxx" + e  );
	        }
		})
	});
} )


function loadOrderType(){
	$.mypost("/pub/orderType" , {type:"orderType"} , function(json){
		var html="<option value=''>请选择</option>"
		for(var i in json){
			html+="<option value='"+i+"'>"+json[i]+"</option>";
		}
		$("#questionType").html(html); 
	},"json")
}