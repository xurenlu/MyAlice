$(function(){
	var q = new Qo();
	q.init( );
})

var Qo = function(){}

Qo.prototype = {
	loadRecord: function(){
		var id = $.getParam("id");
		$.mypost("/qo/queryOrder" , {id:id} , function(json){
			/*显示工单信息*/
			var html= "<h4 class=\"media-heading\" >" + json.user.username + "：" + json.questionOrder.questionContent
			+ "</h4>" + json.questionOrder.createTime; 
			$("#qo_content").html( html ); 
			
			/*显示工单进度*/
			var state = json.questionOrder.state ; 
			var isHave = false ;
			var lis = $("#flow").find("*[key]"); 
			for(var x=0;x<lis.length;x++){
				var li = lis[x];
				var key = $(li).attr("key");
				$(li).addClass("active"); 
				if(key == state){
					isHave=true;
					break ;
				} 
			}
			if(!isHave){$(lis).removeClass("active");}
			
			/*显示工单咨询记录*/
			var qo_record_html = $("#qo_record").html()
			var records = json.records; 
			var recordHtml = "" ;
			for(var x=0;x<records.length;x++){
				var newHtml = qo_record_html;
				var usertype = records[x].usertype; 
				if(usertype==1){
					newHtml = newHtml.replace("\{commitUser\}","<span>系统管理员&nbsp;&nbsp;</span>"+records[x].commitUser);
				}else{
					newHtml = newHtml.replace("\{commitUser\}",records[x].commitUser);
				}
				newHtml = newHtml.replace("\{content\}",records[x].content);
				newHtml = newHtml.replace("\{createTime\}",records[x].createTime);
				recordHtml+=newHtml;
			}
			$("#qo_data").html( recordHtml );
		},"json")
	},init:function(){
		var _this = this ;
		$.mypost("/pub/orderType" , {type:"orderState"} , function(json){
			var html="";
			for(key in json){
				html += "<li key='" + key + "'><a>" + json[key] + "</a></li>" ;
			}
			$("#flow").html(html); 
			_this.loadRecord();
		},"json"); 
	}
}