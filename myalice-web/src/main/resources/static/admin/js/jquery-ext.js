$.mypost = function(url , param , callfun , dataType) {
	
	var token = Cookies.get("XSRF-TOKEN");
	$.ajax({
	    accepts: {
	    	"XSRF-TOKEN":token
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

$.getParam = function(paramName){ 
    var result ="";
    var url = window.location.href.toString(); 
    if(!url.length) return result;
    var parts = url.split(/\?|\&/);
    for(var i=0, len=parts.length; i<len; i++) {
        var tokens = parts[i].split("=");
        if(tokens[0]==paramName)
        	{
        	return decodeURIComponent(tokens[1]);
        	}
        
    }
    return result;
}
