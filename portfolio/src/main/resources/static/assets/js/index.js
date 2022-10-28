
//Button Event Start =========================================================================================

//nav click event
$('.navbar-nav .nav-item .nav-link').click(function(e){
	var anchor = $(this);
	
	$('html, body').stop().animate({
	    scrollTop: $(anchor.attr('href')).offset().top - 30
	}, 1500);
	
	e.preventDefault();
	
});
//nav click event(mobile)
$('.navbar-collapse.show').click(function(e){
	e.preventDefault();
	if( $(e.target).is('a') && $(e.target).attr('class') != 'dropdown-toggle' ) {
	    $(this).collapse('hide');
	}
});

$('#btn_email').click(function(){
	window.scrollTo({top:$("#contact").offset().top , behavior:'smooth'});
});

$('.go-top').click(function(e){     
	e.preventDefault();  
	$('html, body').stop().animate({scrollTop: 0}, 500);
});

$('#btn_phone').hover(function(){       
	$(this).text('010  -  9577  -  6852');    
	}, function() {       
	 $(this).text('목소리가 궁금하다면,');    
});

$('#btn_email').hover(function(){       
	$(this).text('plain64@naver.com');    
	}, function() {       
	$(this).text('긴 이야기가 필요하다면,');    
});

//Button Event End=========================================================================================


//email send
function sendEmail() {
	
	var form1 = $("#contactForm").serialize();
	      
    $.ajax({
        url: "/sendMail",
        data: form1,
        type:"POST",
    }).done(function (fragment) {
	
		if(fragment){
			$("#contactForm").find("[name=email]").val('');
			$("#contactForm").find("[name=message]").val('');
			
			$("#btn_sendEmail").find('span').text('완료');
			$("#btn_sendEmail").removeClass('three');
			$("#btn_sendEmail").addClass('banner-btn');
			
			setTimeout(() => {	
				$("#btn_sendEmail").find('span').text('');
				$("#btn_sendEmail").removeClass('banner-btn');
				$("#btn_sendEmail").addClass('three');}, 5000);
			
		}
    });
    
    return false;
}


var introduce_txt = '항상 즐겁고 재미있는 개발을 하고싶은 <span>웹 / 안드로이드 개발자</span> 입니다.<br>부천에서 거주하고 있으며, 자전거를 타고 출퇴근하는 것을 좋아합니다.<br>저에 대한 자세한 정보가 궁금하다면 아래로 스크롤 해주세요.';

//window resize
function resize(){
	
	var windowWidth = $( window ).width();
	
	if(windowWidth < 992) { 
		$("#home").find("h1").html("<span>개발자</span><br>장연지");
		$("#btn_email").css("margin-left","0px");
		$("#introduce_txt").html(introduce_txt.replaceAll("<br>",""));
	} else {
		$("#home").find("h1").html("<span>개발자</span> 장연지");
		$("#btn_email").css("margin-left","15px");
		$("#introduce_txt").html(introduce_txt);
	}
}

//window resize event
$( window ).resize(function() {   
	resize();
});


$(document).ready(function(){
	
	//loader time out
	setTimeout(() => {$(".loader").fadeOut();}, 1000);
	
	//window resize
	resize();
	
	//mouse effect event
	$('a').each(function(index, item){
		
		$(item).addClass('magic-hover');
		$(item).addClass('magic-hover__square');
		
	});
	
	options = {
		"cursorOuter": "circle-basic",
		"hoverEffect": "circle-move",
		"hoverItemMove": false,
		"defaultCursor": false,
		"outerWidth": 30,
		"outerHeight": 30
	  };
	  
	magicMouse(options);
	//mouse effect event end

	
});

