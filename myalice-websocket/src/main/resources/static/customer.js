var socket = null;
var supporterSessionId = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    socket = new SockJS('http://localhost:8080/customer');
    socket.onopen = function() {
        console.log('open');
        setConnected(true);
    };
    socket.onmessage = function(e) {
        console.log('message', e.data);
        var content = $.parseJSON(e.data);
        if (content) {
        	if (content.type=="customer_talk") {
        		showOther(content.content.talkContent);
        	} else if (content.type=="customer_assign") {
        		supporterSessionId = content.content.sessionId;
        		showOther(content.content.userName + " as your with.");
        	}
        }
        
    };
    socket.onclose = function() {
    	supporterSessionId = null;
    	console.log('close');
    };
}

function disconnect() {
	socket.close();
    setConnected(false);
}

function sendName() {
	socket.send(createMessage());
	showSelf($('#name').val());
}

function createMessage() {
	return JSON.stringify({'type':'supporter_talk', 'content':{'sessionId':$('#customerSelect').val(), 'talkContent': $('#name').val()}})
}

function showOther(message) {
    $("#greetings").append("<tr><td><font color='red'>" + message + "</font></td></tr>");
}

function showSelf(message) {
    $("#greetings").append("<tr><td><font color='blue'>" + message + "</font></td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});

