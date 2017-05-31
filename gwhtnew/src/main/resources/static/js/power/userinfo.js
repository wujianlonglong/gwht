var myId = 0;
$(function () {
    firstSearchData();

    $("#dialog_data_detail").dialog({
        autoOpen: false,
        height: 200,
        width: 800,
        modal: true,
        buttons: {
            确定: function () {
                submitUpdate("");
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

    $("#dialog_role_detail").dialog({
        autoOpen: false,
        height: 200,
        width: 600,
        modal: true,
        buttons: {
            确定: function () {
                updateUserRole();
                //  $(this).find("form")[0].reset();
                $(this).dialog("close");
            },
            取消: function () {
                //  $(this).find("form")[0].reset();
                $(this).dialog("close");
            }
        },
        close: function () {
            //$(this).find("form")[0].reset();
        }
    });


});

function firstSearchData() {
    var page = 1;
    var size = 20;
    var key = $("#ConditionInput").val();
    GetPageData(page, size, key);
}


function SearchData() {
    var page = 1;
    var size = $("#pagesize").val();
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
        url: "getUserInfo",
        async: false,
        type: "get",
        data: {key: key, page: pCur, size: pSize},
        success: function (data) {
            $("#tmp").html(data);
        }
    });
}

function updateData(nickName, department, id) {
    $('div[name=user_name]').hide();
    $('div[name=user_password]').hide();
    $('div[name=create_time]').hide();
    $('div[name=user_id]').hide();


    $('#user_nickname').val(nickName);
    $('#department').val(department);

    $("#dialog_data_detail").dialog({
        title: "修改角色",
        buttons: {
            确认: function () {
                updateSubmit(id)
                $(this).dialog("close");
                $(this).find("form")[0].reset();
            },
            取消: function () {
                $(this).dialog("close");
                $(this).find("form")[0].reset();
            }
        }
    });

    $("#dialog_data_detail").dialog("open");
}

function updateSubmit(userId) {
    if (userId == null || userId <= 0) {
        alert("用户Id为空！")
        return;
    }
    var nickname = $("#user_nickname").val();
    var department = $("#department").val();
    if (department == "") {
        alert("请输入部门");
        return;
    }
    var model = new Object();
    model.id = userId;
    model.nickName = nickname;
    model.department = department;

    $.ajax({
        url: "updateuser",
        type: "post",
        async: false,
        contentType: "application/json ;charset=utf-8",
        data: JSON.stringify(model),
        dataType: "json",
        success: function (data) {
            alert(data.msg);
            SearchData(1, 20);
        }

    });
}

function newData(obj) {
    $('div[name=user_name]').show();
    $('div[name=user_password]').show();
    $('div[name=create_time]').show();
    $('div[name=user_id]').show();

    $("#dialog_data_detail").dialog({
        title: "新增用户",
        buttons: {
            确认: function () {
                submitData("add");
                $(this).dialog("close");
                $(this).find("form")[0].reset();
            },
            取消: function () {
                $(this).dialog("close");
                $(this).find("form")[0].reset();
            }
        }
    });

    $("#dialog_data_detail").dialog("open");
}

function submitData(obj) {

    switch (obj) {
        case "add":
            var name = $("#user_name").val();
            if (name == "") {
                alert("请输入用户名");
                break;
            }
            var password = $("#user_password").val();
            if (password == "") {
                alert("请输入密码");
                break;
            }
            var nickname = $("#user_nickname").val();
            var department = $("#department").val();
            if (department == "") {
                alert("请输入部门");
                break;
            }
            var model = new Object();
            model.userName = name;
            model.passWord = password;
            model.nickName = nickname;
            model.department = department;

            $.ajax({
                url: "adduser",
                async: false,
                type: "post",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(model),
                dataType: "json",
                success: function (data) {
                    alert(data.msg);
                    SearchData(1, 20);
                }
            });

            break;
    }

    //$("#dialog_data_detail").dialog("open");
}

function RefreshUserInfo() {
    $.ajax({
        url: "redis/refreshUserInfo",
        type: "post",
        async: false,
        success: function (data) {
            alert(data.msg);
        }
    });
}

function getRole(id) {
    myId = id;
    $('#dialog_role_detail').html("");
    $.ajax({
        url: "getRole",
        type: "get",
        async: false,
        data: {userId: id},
        success: function (data) {

            if (data.success == false) {
                alert(data.msg);
            }
            else {
                if (data.data != null && data.data.length > 0) {
                    var text = "";
                    for (var i = 0; i < data.data.length; i++) {
                        if (data.data[i].chooseRole == 1)
                            text += "<input name='roles' type='checkbox' value='" + data.data[i].roleId + "' checked='checked' />" + data.data[i].roleName;
                        else
                            text += "<input  name='roles' type='checkbox' value='" + data.data[i].roleId + "'  />" + data.data[i].roleName;
                    }
                    $('#dialog_role_detail').append(text);
                    $("#dialog_role_detail").dialog("open");
                }

            }

        }
    });

}

function getAllRole() {
    $.ajax({
        url: "getAllRole",
        type: "get",
        async: false,
        success: function (data) {

        }
    });
}

function updateUserRole() {
    var roleArray = new Array;
    $('input[name=roles]:checked').each(function () {
        roleArray.push($(this).val());
    });
    $.ajax({
        url: "updateUserRole",
        type: "post",
        async: false,
        data: {userId: myId, roleList: roleArray},
        success: function (data) {

        }
    });

}

function initPassword(userid) {
    $.ajax({
        url: "initPassword",
        type: "post",
        async: false,
        data: {userId: userid},
        success: function (data) {
            if (data.success) {
                alert(data.msg + "初始化密码为：123456");
            } else {
                alert(data.msg);
            }
        }

    });
}


/**
 * Created by wujianlong on 2017/5/10.
 */
