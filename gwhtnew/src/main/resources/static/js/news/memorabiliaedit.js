/**
 * Created by wujianlong on 2017/5/23.
 */
var editor = null;
$(function () {

    editor = CKEDITOR.replace("cke_companyNewContent", {
        filebrowserImageUploadUrl: 'imgUpload',
        filebrowserFlashUploadUrl:'imgUpload',
        image_previewText:' ',
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
   // window.history.back(-1);
    parent.RemoveDiv('0002'+$('#memroId').val())
}


function addmemor() {
    if (!confirm("确认提交？"))
        return;

    var model = new Object();
    model.id = $('#memroId').val();
    if ($.trim($('#datepicker').val()).length <=0) {
        alert("发布日期不能为空！");
    }
    if(!checkDate($('#datepicker').val())){
        alert("发布日期的时间格式不正确");
        return;
    }
    model.happenDate = new Date($('#datepicker').val()).getTime();

    if ($.trim(editor.getData()).length <=0) {
        alert("新闻内容不能为空！");
        return;
    }
    model.content = editor.getData();

    $.ajax({
        url: "subMemor",
        async: false,
        type: "post",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(model),
        success: function (data) {
            alert(data.msg);
            if (data.success) {
                //window.location.href="memorabilialist";
                parent.RemoveDiv('0002'+$('#memroId').val())
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
