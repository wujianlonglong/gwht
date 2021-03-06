/**
 * Created by wujianlong on 2017/5/22.
 */
var editor = null;
$(function () {

    editor = CKEDITOR.replace("cke_companyNewContent", {
        filebrowserImageUploadUrl: 'imgUpload',
        filebrowserFlashUploadUrl: 'imgUpload',
        image_previewText: ' ',
        height: 400
    });

    $("#datepicker").datepicker({
        dateFormat: "yy-mm-dd",
        maxDate: "0d",
        changeMonth: true,
        changeYear: true
    });

});

function Back() {
    //window.history.back(-1);
    parent.RemoveDiv('0001' + $('#newId').val())
}


function addNews() {
    if (!confirm("确认提交？"))
        return;

    var model = new Object();
    model.newsId = $('#newId').val();
    model.newType = $('#newtype').val();
    if ($.trim($('#tb_companyNewsTitle').val()).length <=0) {
        alert("新闻标题不能为空！");
        return;
    }
    model.newsTitle = $('#tb_companyNewsTitle').val();
    model.isTop = $('#cb_isTop').is(':checked') ? '1' : '0';
    if ($.trim($('#datepicker').val()).length <=0) {
        alert("发布日期不能为空！");
    }
    if(!checkDate($('#datepicker').val())){
        alert("发布日期的时间格式不正确");
        return;
    }
    model.showTime = new Date($('#datepicker').val()).getTime();
    model.showSource = $('#tb_newSource').val();
    if ($.trim(editor.getData()).length <=0) {
        alert("新闻内容不能为空！");
        return;
    }
    model.newsContent = editor.getData();


    $.ajax({
        url: "subNews",
        async: false,
        type: "post",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(model),
        success: function (data) {
            alert(data.msg);
            if (data.success) {
                //window.location.href="newslist";
                parent.RemoveDiv('0001' + $('#newId').val())
            }
        }
    });
}

function checkDate(d){
    var ds=d.match(/\d+/g),ts=['getFullYear','getMonth','getDate'];
    var d=new Date(d.replace(/-/g,'/')),i=3;
    ds[1]--;
    while(i--)if( ds[i]*1!=d[ts[i]]()) return false;
    return true;
}
