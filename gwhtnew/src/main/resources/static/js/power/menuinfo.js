/**
 * Created by wujianlong on 2017/5/17.
 */
/*
 *   zTree 设置
 */
var chooseMenu = new Object();
var childmenu = new Object();
var subtype = "";
var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "pId"
        }
    },
    callback: {
        onClick: getList
    }
};

/*
 *   封装zTree属性类
 */
function zNode(id, pId, name, isParent, open) {
    this.id = id;
    this.pId = pId;
    this.name = name;
    this.isParent = isParent;
    this.open = open;
}

/*
 *   初始化
 */
$(function () {

    getTree();

    $("#moddialog").dialog({
        autoOpen: false,
        modal: true,
        width: 500,
        buttons: {
            确定: function () {
                //提交请求
                if (subtype == "add") {
                    submitData();
                }
                else if (subtype == "update") {
                    updateData();
                }
                //ClearDialog();
                //$(this).dialog("close");
            },
            取消: function () {
                ClearDialog();
                $(this).dialog("close");
            }
        },
        close: function () {
            ClearDialog();
        }
    });


    //删除
    $("#deleteDialog").dialog({
        autoOpen: false,
        modal: true,
        buttons: {
            确认: function () {
                //提交删除
                submitData("delete");
                $(this).dialog("close");
            },
            取消: function () {
                $(this).dialog("close");
            }
        }
    });


});

/*
 *   获得树
 */
function getTree() {
    getMenuSelect();
    var objArray = new Array(); //对象数组
    var rootobj = new zNode(0, -1, "root", true, true);
    objArray.push(rootobj);
    $.post("getMenuTree", {rootParentId: 0}, function (json) {
        if (json.success) {
            for (var i = 0; i < json.data.length; i++) {
                if (json.data[i].menuId.length != 0) {
                    var pid = json.data[i].menuId;
                    for (var j = 0; j < json.data[i].childMenus.length; j++) {
                        var obj = new zNode(json.data[i].childMenus[j].menuId, pid, json.data[i].childMenus[j].menuName, false, false);
                        objArray.push(obj);
                    }
                }
                var obj = new zNode(json.data[i].menuId, 0, json.data[i].menuName, true, false);
                objArray.push(obj);
            }
            $.fn.zTree.init($("#tree"), setting, objArray);
        } else {
            alert(json.Message);
        }
    }, "json");
}


//获取父级菜单下拉框
function getMenuSelect() {
    $.post("getMenuTree", {rootParentId: 0}, function (json) {
        var text = "<option value='0'>root</option>";
        for (var i = 0; i < json.data.length; i++) {
            text = text + "<option value='" + json.data[i].menuId + "'>" + json.data[i].menuName + "</option>";
        }
        $("#f_menu_id").empty().append(text);
    }, "json");
}

function newMenu() {
    subtype = "add";
    $("#menu_id").attr("disabled", false);
    $("#f_menu_id").attr("disabled", false);
    $("#is_show").prop("checked", true);
    $("#moddialog").dialog("open");
}


function submitData() {
    var menuId = $("#menu_id").val();
    if (menuId == "") {
        alert("菜单编号不能为空！");
        return;
    }
    var menuName = $("#menu_name").val();
    if (menuName == "") {
        alert("菜单名称不能为空！");
        return;
    }
    var parentMenuId = $("#f_menu_id").val();
    var showOrder = $("#show_order").val();
    if (showOrder == "") {
        alert("显示序列不能为空！");
        return;
    }
    var menuUrl = $("#menu_url").val();
    var comments = $("#comments").val();
    var status = 0;
    if ($('#is_show').is(':checked')) {
        status = 1;
    }

    var model = new Object();
    model.menuId = menuId;
    model.menuName = menuName;
    model.parentMenuId = parentMenuId;
    model.showOrder = showOrder;
    model.menuUrl = menuUrl;
    model.comments = comments;
    model.status = status;

    $.ajax({
        url: "addMenu",
        type: "post",
        async: false,
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(model),
        success: function (data) {
            alert(data.msg);
            if (data.success) {
                getTree();
                getPaAndChilMenu(parentMenuId);
                ClearDialog();
                $("#moddialog").dialog("close");
            }
        }
    });

}

/*
 *  清空Dialog内数据
 */
function ClearDialog() {
    $("#menu_id").val("");
    $("#menu_name").val("");
    $("#is_show").prop("checked", true);
    $("#show_order").val("");
    $("#menu_url").val("");
    $("#create_time").val("");
    $("#comments").val("");
    $("#f_menu_id").val(0);
}

/*
 *   获得菜单细节
 */
function getList(e, treeId, treeNode) {
    var menuId = treeNode.id;
    //  var pmenuId = treeNode.pId
    getPaAndChilMenu(menuId);
}

function getPaAndChilMenu(menuId) {
    $.ajax({
        url: "getPaAndChilMenu",
        type: "get",
        async: false,
        data: {menuId: menuId},
        success: function (data) {
            if (data.success) {
                chooseMenu = data.data;
                if (data.data != null) {
                    $("#ip_Menu_Id_Dis").val(data.data.menuId); //填充菜单ID
                    $("#ip_Menu_Name_Dis").val(data.data.menuName); //菜单Name
                    $("#ip_Menu_Url_Dis").val(data.data.menuUrl); //菜单链接
                    var isvalid = data.data.status; //是否正常
                    if (isvalid == 0) {
                        $("#cb_Is_Valid_Dis").prop("checked", false);
                    } else if (isvalid == 1) {
                        $("#cb_Is_Valid_Dis").prop("checked", true);
                    }
                }
                //子菜单
                if (data.data.childMenus != null && data.data.childMenus.length > 0) {
                    var text = "<tr class='e'><td>序号</td><td>菜单ID</td><td>菜单名称</td><td>排序</td><td>菜单链接</td><td class='right'>操作</td></tr>";
                    childmenu = data.data.childMenus;
                    for (var i = 0; i < childmenu.length; i++) {
                        if (i % 2 == 0) {
                            text = text + "<tr>";
                        } else {
                            text = text + "<tr class='e'>";
                        }
                        text = text + "<td>" + (i + 1) + "</td><td>" + childmenu[i].menuId + "</td><td>" + childmenu[i].menuName + "</td><td>" + childmenu[i].showOrder + "</td><td>" + childmenu[i].menuUrl + "</td>";
                        text = text + "<td class='right'><input type='button' onclick='updateMenu("+i+");' value='修改菜单项' /><input type='button'   onclick='deleteMenu(" + childmenu[i].menuId + "," + childmenu[i].parentMenuId + ");' value='删除菜单项' /></td></tr>"
                    }
                    $("#subMenuList").empty().append(text);
                } else {
                    $("#subMenuList").empty();
                }
            }
            else {
                alert(data.msg);
            }
        }

    });
}

function RefreshMenuInfo() {
    $.ajax({
        url: "redis/refreshMenuInfo",
        type: "post",
        async: false,
        success: function (data) {
            alert(data.msg);
        }
    });

}

function updatefather() {
    subtype = "update";
    if ($("#ip_Menu_Id_Dis").val() == 0) {
        alert("root菜单无法修改！");
        return;
    }
    $("#menu_id").attr("disabled", "disabled");

    $("#menu_id").val(chooseMenu.menuId);
    $("#menu_name").val(chooseMenu.menuName);
    if (chooseMenu.status == 0) {
        $("#is_show").prop("checked", false);
    }
    else {
        $("#is_show").prop("checked", true);
    }
    $("#show_order").val(chooseMenu.showOrder);
    $("#menu_url").val(chooseMenu.menuUrl);
    $("#create_time").val(timeStamp2String(chooseMenu.createTime));
    $("#comments").val(chooseMenu.comments);
    $("#f_menu_id").val(chooseMenu.parentMenuId);

    if (chooseMenu.parentMenuId == 0) {
        $("#f_menu_id").attr("disabled", "disabled");
    } else {
        $("#f_menu_id").attr("disabled", false);
    }

    $("#moddialog").dialog("open");
}

function updateData() {
    var menuId = $("#menu_id").val();
    if (menuId == "") {
        alert("菜单编号不能为空！");
        return;
    }
    var menuName = $("#menu_name").val();
    if (menuName == "") {
        alert("菜单名称不能为空！");
        return;
    }
    var parentMenuId = $("#f_menu_id").val();
    var showOrder = $("#show_order").val();
    if (showOrder == "") {
        alert("显示序列不能为空！");
        return;
    }
    var menuUrl = $("#menu_url").val();
    var comments = $("#comments").val();
    var status = 0;
    if ($('#is_show').is(':checked')) {
        status = 1;
    }
    var createTime = $("#create_time").val();

    var model = new Object();
    model.menuId = menuId;
    model.menuName = menuName;
    model.parentMenuId = parentMenuId;
    model.showOrder = showOrder;
    model.menuUrl = menuUrl;
    model.comments = comments;
    model.status = status;
    model.createTime = new Date(createTime).getTime();

    $.ajax({
        url: "updateMenu",
        type: "post",
        async: false,
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(model),
        success: function (data) {
            alert(data.msg);
            if (data.success) {
                getTree();
                getPaAndChilMenu(menuId);
                ClearDialog();
                $("#moddialog").dialog("close");
            }
        }
    });

}


function timeStamp2String(time) {
    var datetime = new Date();
    datetime.setTime(time);
    return datetime.Format("yyyy-MM-dd hh:mm:ss.S");
    // var year = datetime.getFullYear();
    // var month = datetime.getMonth() + 1;
    // var date = datetime.getDate();
    // var hour = datetime.getHours();
    // var minute = datetime.getMinutes();
    // var second = datetime.getSeconds();
    // var mseconds = datetime.getMilliseconds();
    // return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second+"."+mseconds;
}


Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
}


function newSonMenu() {
    if (chooseMenu.menuId != 0 && chooseMenu.parentMenuId != 0) {
        alert("该菜单已经为子菜单，无法再添加子菜单！");
        return;
    }

    subtype = "add";
    $("#menu_id").attr("disabled", false);
    $("#f_menu_id").val(chooseMenu.menuId);
    $("#f_menu_id").attr("disabled", true);
    $("#is_show").prop("checked", true);
    $("#moddialog").dialog("open");
}


function deleteMenu(menuId, parentMenuId) {
    if (!confirm("确认删除" + menuId + "菜单？"))
        return;
    $.ajax({
        url: "deleteMenu",
        type: "post",
        async: false,
        data: {menuId: menuId},
        success: function (data) {
            alert(data.msg);
            getTree();
            getPaAndChilMenu(parentMenuId);
        }

    });
}

function updateMenu(i){
    var menuInfo=childmenu[i];
    subtype = "update";
    $("#menu_id").attr("disabled", "disabled");

    $("#menu_id").val(menuInfo.menuId);
    $("#menu_name").val(menuInfo.menuName);
    if (menuInfo.status == 0) {
        $("#is_show").prop("checked", false);
    }
    else {
        $("#is_show").prop("checked", true);
    }
    $("#show_order").val(menuInfo.showOrder);
    $("#menu_url").val(menuInfo.menuUrl);
    $("#create_time").val(timeStamp2String(menuInfo.createTime));
    $("#comments").val(menuInfo.comments);
    $("#f_menu_id").val(menuInfo.parentMenuId);

    if (menuInfo.parentMenuId == 0) {
        $("#f_menu_id").attr("disabled", "disabled");
    } else {
        $("#f_menu_id").attr("disabled", false);
    }

    $("#moddialog").dialog("open");
}