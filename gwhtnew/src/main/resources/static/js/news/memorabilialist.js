/**
 * Created by wujianlong on 2017/5/23.
 */
$(function(){
    firstSearchData();


})


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



function SearchData() {
    var page = 1;
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
        url: "getMemorabiliaList",
        async: false,
        type: "get",
        data: {key: key, page: pCur, size: pSize},
        success: function (data) {
            $("#tmp").html(data);
        }
    });
}

function newData(id) {
    //window.location.href = "memorabiliaedit?id=" + id;
    if (id == 0)
        parent.CreateDiv('0002' + id, 'memorabiliaedit?id=' + id, '大事记新增');
    else
        parent.CreateDiv('0002' + id, 'memorabiliaedit?id=' + id, '大事记编辑'+id);
}

function updateStatus(id,status){
    if(status==1)
        status=-1;
    else
        status=1;

    $.ajax({
        url:"updateMemorStatus",
        type:"post",
        async:false,
        data:{id:id,status:status},
        success:function(data){
            alert(data.msg);
            if(data.success){
                jump();
            }
        }
    });

}


function RefreshMemor(){
    $.ajax({
        url:"refreshMemor",
        type:"post",
        success:function(data){
            alert(data.msg);
        }
    });

}