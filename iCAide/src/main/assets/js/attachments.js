/**
 * Created by guange on 16/1/30.
 */

$(function(){

    window.play_pause = function (i) {
        var audio = document.getElementById('music_'+i);
        if (audio != null) {
            if (audio.paused) {
                audio.play();
            } else {
                audio.pause();//暂停
            }
        }
    }

    $(".menu_chart").click(function(){
        $(this).css({"color":"#ef8c3a","border-bottom":"0px","background":"#fff"});
        $(this).siblings().css({"color":"#000","border-bottom":"1px solid #ddd","background":"#f5f5f5"});
        $(".chart").show();
        $(".feedback").hide();
        $(".remark").hide();
    });
    $(".menu_feedback").click(function(){
        $(this).css({"color":"#ef8c3a","border-bottom":"0px","background":"#fff"});
        $(this).siblings().css({"color":"#000","border-bottom":"1px solid #ddd","background":"#f5f5f5"});
        $(".chart").hide();
        $(".feedback").show();
        $(".remark").hide();
    });
    $(".menu_remark").click(function(){
        $(this).css({"color":"#ef8c3a","border-bottom":"0px","background":"#fff"});
        $(this).siblings().css({"color":"#000","border-bottom":"1px solid #ddd","background":"#f5f5f5"});
        $(".chart").hide();
        $(".feedback").hide();
        $(".remark").show();
    });

    $(".small_img").click(function(){
        var height = $(this).height();
        if(height == 58){
            $(this).css({"max-width":"640px","width":"90%","height":"200px"});
        }else{
            $(this).css({"max-width":"640px","width":"24%","height":"58px"});
        }

    })
});