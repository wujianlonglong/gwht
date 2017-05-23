// JavaScript Document
function CreateDiv(tabid, url, name) {
    ///如果当前tabid存在直接显示已经打开的tab
    if (document.getElementById("div_" + tabid) == null) {
        //创建iframe
        var box = document.createElement("iframe");
        box.id = "div_" + tabid;
        box.src = url;
        box.height = "100%";
        box.frameBorder = 0;
        box.width = "100%";
        if (document.getElementById("div_pannel")) {
            document.getElementById("div_pannel").appendChild(box);
        }

        //遍历并清除开始存在的tab当前效果并隐藏其显示的div
        var tablist = document.getElementById("div_tab").getElementsByTagName('li');
        var pannellist = document.getElementById("div_pannel").getElementsByTagName('iframe');
        if (tablist.length > 0) {
            //单行最多能够放11个，如果超过11个，将最前面的一个移除
            if (tablist.length == 7) {
                tablist[0].parentNode.removeChild(tablist[0]);
                pannellist[1].parentNode.removeChild(pannellist[1]);
            }
            for (i = 0; i < tablist.length; i++) {
                tablist[i].className = "";
            }
            for (i = 0; i <= tablist.length; i++)  //不论其他tab是否存在桌面始终会存在,所以ifrme要比取到的li数多一个
            {
                pannellist[i].style.display = "none";
            }
        }
        else {
            pannellist[0].style.display = "none";   //桌面
        }

        //创建li菜单
        var tab = document.createElement("li");
        tab.className = "crent";
        tab.id = tabid;
        var litxt = "<span><a href=\"javascript:;\" onclick=\"javascript:CreateDiv('" + tabid + "','" + url + "','" + name + "')\" title=" + name + " class=\"menua\">" + name + "</a><a onclick=\"RemoveDiv('" + tabid + "')\" class=\"win_close\" title=\"关闭当前窗口\"><a></span>";
        tab.innerHTML = litxt;
        document.getElementById("div_tab").appendChild(tab);
        document.getElementById('HomePage').className = "";    //桌面
    }
    else {
        var tablist = document.getElementById("div_tab").getElementsByTagName('li');
        var pannellist = document.getElementById("div_pannel").getElementsByTagName('iframe');
        //alert(tablist.length);
        for (i = 0; i < tablist.length; i++) {
            tablist[i].className = "";
            pannellist[i].style.display = "none"
        }
        document.getElementById('HomePage').className = "";     //桌面
        document.getElementById(tabid).className = 'crent';
        document.getElementById("div_" + tabid).style.display = 'block';
    }
}
function RemoveDiv(obj) {
    var ob = document.getElementById(obj);
    ob.parentNode.removeChild(ob);
    var obdiv = document.getElementById("div_" + obj);
    obdiv.parentNode.removeChild(obdiv);
    var tablist = document.getElementById("div_tab").getElementsByTagName('li');
    var pannellist = document.getElementById("div_pannel").getElementsByTagName('iframe');
    if (tablist.length > 0) {
        tablist[tablist.length - 1].className = 'crent';
        pannellist[tablist.length].style.display = 'block';
    }
    //桌面
    else {
        document.getElementById('HomePage').className = 'crent';
        pannellist[0].style.display = 'block';
    }
}


//左侧菜单结束
$(function () {
    $(".nav_left li a").click(function () {
        $(this).parent().parent().children().children("ul").slideUp(500);
        $(this).parent().children("ul").slideDown(500);

    });

    //模块尺寸
    $('#doc').css('height', $(window).height() - 70);
    $('#sidebar_l2').css('height', $(window).height() - 70);
    $('#sidebar_r').css('height', $(window).height() - 70);
    $('#div_pannel').css('height', $(window).height() - 103);

    $("#dialog_quick_changePwd").dialog({
        autoOpen: false,
        height: 220,
        width: 350,
        modal: true,//true 变灰 不能操作背景元素 false 可以
        buttons: {
            确定: function () {
                if (confirm('确定修改？')) {
                    var pwd1 = $("#ip_Password1").val();
                    var pwd2 = $("#ip_Password2").val();
                    var pwd3 = $("#ip_Password3").val();
                    if (pwd2 != pwd3) {
                        alert('两次重复的密码不一致！');
                    }
                    else {
                        $.post("DataProvider/Manager.ashx", { op: 'password', oldPwd: pwd1, newPwd: pwd2 }, function (json) {

                            alert(json.message);

                        }, "json");

                        $(this).dialog("close");
                        $(this).find("form")[0].reset();
                    }
                }
            },
            取消: function () {
                if (confirm('确定取消？')) {
                    $(this).dialog("close");
                    $(this).find("form")[0].reset();
                }
            }
        }
    });

})

//改变窗体大小时适应浏览器高度
$(window).resize(function () {
    //模块尺寸
    $('#doc').css('height', $(window).height() - 70);
    $('#sidebar_l2').css('height', $(window).height() - 70);
    $('#sidebar_r').css('height', $(window).height() - 70);
    $('#div_pannel').css('height', $(window).height() - 103);

});

//登出
function LogOut() {
    if (confirm('确定登出系统？')) {
        location.href = "loginout";
    }
}



//隐藏左侧菜单
function menuChange(obj, menu) {
    //alert(menu.style.display);
    if (menu.style.display == "" || menu.style.display == "table-cell") {
        menu.style.display = "none";
    } else {
        menu.style.display = "";
    }
}

//弹出层
function showdiv1() {

    document.getElementById("main_select").style.visibility = "visible";
}


function hidediv1() {

    document.getElementById("main_select").style.visibility = 'hidden';
}


//按钮切换
function Change(obj) {
    if (obj.className == "H_Tab2") {
        obj.className = "H_Tab1";
    }
    else {
        obj.className = "H_Tab2";
    }
}
$(function () {
    $('.up_arrow').click(function () {
        if ($('.top').is(':hidden')) {

            $('.top').show();
            $(this).removeClass('act');
            $('#doc').css('height', $(window).height() - 70);
            $('#sidebar_l2').css('height', $(window).height() - 70);
            $('#sidebar_r').css('height', $(window).height() - 70);
            $('#div_pannel').css('height', $(window).height() - 103);
        } else {
            $('.top').hide();
            $(this).addClass('act');
            $('#doc').css('height', $(window).height());
            $('#sidebar_l2').css('height', $(window).height());
            $('#sidebar_r').css('height', $(window).height());
            $('#div_pannel').css('height', $(window).height() - 33);

        }
    });

    $('.cz_button').hover(function () {
        $('#menu1').show()
    })
    $('.user').mouseleave(function () {
        $('#menu1').hide()
    });

});


//隐藏层
function MM_showHideLayers() { //v9.0
    var i, p, v, obj, args = MM_showHideLayers.arguments;
    for (i = 0; i < (args.length - 2); i += 3)
        with (document) if (getElementById && ((obj = getElementById(args[i])) != null)) {
            v = args[i + 2];
            if (obj.style) { obj = obj.style; v = (v == 'show') ? 'visible' : (v == 'hide') ? 'hidden' : v; }
            obj.visibility = v;
        }
}
























