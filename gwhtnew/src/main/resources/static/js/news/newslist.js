/**
 * Created by wujianlong on 2017/5/21.
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
        url: "/getNewsList",
        async: false,
        type: "get",
        data: {key: key, page: pCur, size: pSize},
        success: function (data) {
            $("#tmp").html(data);
        }
    });
}

function newData(newsId) {
    window.location.href = "newsedit?newsId=" + newsId;
}

function updateStatus(newsId,status){
    if(status=='0')
        status='9';
    else
        status='0';

    $.ajax({
        url:"/updateStatus",
        type:"post",
        async:false,
        data:{newsId:newsId,status:status},
        success:function(data){
            alert(data.msg);
            if(data.success){
                jump();
            }
        }
    });

}