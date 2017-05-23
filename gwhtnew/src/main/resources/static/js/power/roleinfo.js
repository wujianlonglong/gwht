/**
 * Created by wujianlong on 2017/5/16.
 */
var subtype = "";
var myRoleId = 0;
$(function () {
    firstSearchData();

    $("#dialog_data_detail").dialog({
        autoOpen: false,
        height: 200,
        width: 800,
        modal: true,
        buttons: {
            确定: function () {
                DataSubmit();
                $(this).find("form")[0].reset();
                $(this).dialog("close");
            },
            取消: function () {
                $(this).find("form")[0].reset();
                $(this).dialog("close");
            }
        },
        close: function () {
            $(this).find("form")[0].reset();
        }
    });

    $('#dialog_menu_detail').dialog({
        autoOpen: false,
        height: 200,
        width: 800,
        modal: true,
        buttons: {
            确定: function () {
                roleMenuSubmit();
                // $(this).find("form")[0].reset();
                $(this).dialog("close");
            },
            取消: function () {
                //   $(this).find("form")[0].reset();
                $(this).dialog("close");
            }
        },
        close: function () {
           // $(this).find("form")[0].reset();
        }
    });

});


function firstSearchData() {
    var page = 1;
    var size = 20;
    var key = $("#ConditionInput").val();
    GetPageData(page, size, key);
}

function page(num) {
    var page = num;
    var size = $("#pagesize").val();
    var key = $("#ConditionInput").val();
    GetPageData(page, size, key);
}


function jump() {
    var page = $("#pagenum").val();
    var size = $("#pagesize").val();
    var key = $("#ConditionInput").val();
    GetPageData(page, size, key);
}


function GetPageData(pCur, pSize, key) {
    if (pCur == null || pCur == "" || pSize == null || pSize == "") {
        alert("分页的page或size为空！");
        return;
    }
    $.ajax({
        url: "/getRoleInfo",
        async: false,
        type: "get",
        data: {key: key, page: pCur, size: pSize},
        success: function (data) {
            $("#tmp").html(data);
        }
    });
}


function SearchData() {
    var page = 1;
    var size = $("#pagesize").val();
    var key = $("#ConditionInput").val();
    GetPageData(page, size, key);
}

function newData() {
    subtype = "add";
    $('div[name=role_id]').show();
    $('div[name=create_time]').show();
    $('#dialog_data_detail').dialog("open");
}


function updateData(roleName, showOder, comments, roleId) {
    subtype = "update";
    $('div[name=role_id]').hide();
    $('div[name=create_time]').hide();

    $('#role_name').val(roleName);
    $('#show_order').val(showOder);
    $('#comments').val(comments);

    myRoleId = roleId;

    $('#dialog_data_detail').dialog("open");
}

function DataSubmit() {
    var roleName = $('#role_name').val();
    if (roleName == null || roleName == "") {
        alert("角色名不能为空！");
        return;
    }
    var showOder = $('#show_order').val();
    if (showOder == null || showOder == "") {
        alert("排列序号不能为空!");
        return;
    }
    var comments = $('#comments').val();

    var model = new Object();
    model.roleName = roleName;
    model.showOder = showOder;
    model.comments = comments;
    switch (subtype) {
        case "add":
            $.ajax({
                url: "/addRole",
                type: "post",
                async: false,
                contentType: "application/json;charset=utf-8",
                data: JSON.stringify(model),
                success: function (data) {
                    alert(data.msg);
                    SearchData(1, 20);
                }
            });
            break;
        case "update":
            model.roleId = myRoleId;
            $.ajax({
                url: "/updateRole",
                type: "post",
                async: false,
                contentType: "application/json;charset=utf-8",
                data: JSON.stringify(model),
                success: function (data) {
                    alert(data.msg);
                    SearchData(1, 20);
                }
            });
            break;
    }

}


function RefreshRoleInfo() {
    $.ajax({
        url: "/redis/refreshRoleInfo",
        type: "post",
        async: false,
        success: function (data) {
            alert(data.msg);
        }
    });
}

function getMenu(roleId) {
    myRoleId = roleId;
    $('#dialog_menu_detail').html("");
    $.ajax({
        url: "/getMenu",
        type: "get",
        async: false,
        data: {roleId: roleId},
        success: function (data) {
            if (data.success == false) {
                alert(data.msg);
            }
            else {
                if (data.data != null && data.data.length > 0) {
                    var text = "";
                    for (var i = 0; i < data.data.length; i++) {
                        text +="<span>";
                        if (data.data[i].chooseMenu == 1)
                            text += "<input name='menus' type='checkbox' value='" + data.data[i].menuId + "' checked='checked' />" + data.data[i].menuName;
                        else
                            text += "<input  name='menus' type='checkbox' value='" + data.data[i].menuId + "'  />" + data.data[i].menuName;
                        if(data.data[i].childMenus!=null&&data.data[i].childMenus.length>0){
                            text+="<ul>";
                            for(var j=0;j<data.data[i].childMenus.length;j++){
                                text+="<li>";
                                if (data.data[i].childMenus[j].chooseMenu == 1)
                                    text += "<input name='menus' type='checkbox' value='" + data.data[i].childMenus[j].menuId + "' checked='checked' />" + data.data[i].childMenus[j].menuName;
                                else
                                    text += "<input  name='menus' type='checkbox' value='" + data.data[i].childMenus[j].menuId + "'  />" + data.data[i].childMenus[j].menuName;
                                text+="</li>";
                            }
                            text+="</ul>";
                        }
                        text +="</span>";

                    }
                    $('#dialog_menu_detail').append(text);
                    $("#dialog_menu_detail").dialog("open");
                }
            }
        }
    });

}

function roleMenuSubmit() {
var menus=new Array();
$('input[name=menus]:checked').each(function(){
    menus.push($(this).val());
});
$.ajax({
    url:"/updateRoleMenu",
    type:"post",
    async:false,
    data:{roleId:myRoleId,menuList:menus},
    success:function(){

    }
});
}


