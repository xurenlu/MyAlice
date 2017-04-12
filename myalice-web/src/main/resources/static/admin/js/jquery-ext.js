$.mypost = function(url , param , callfun , dataType) {
	
	var token = Cookies.get("myalice-token");
	$.ajax({
	    accepts: {
	    	myalice-token:token
	    },
	    dataType:dataType,
	    type:"POST",
	    success: function(data) {
	    	callfun( data );
	    },error: function(XMLHttpRequest, textStatus, errorThrown){
	    	bootbox.alert(textStatus);
	    }
	});
	
}