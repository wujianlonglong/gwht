/**
 * Created by wujianlong on 2017/5/22.
 */
var editor = null;
$(function () {

    editor = CKEDITOR.replace("cke_companyNewContent", {
        filebrowserImageUploadUrl: '/imgUpload',
        filebrowserFlashUploadUrl:'/imgUpload',
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
    window.history.back(-1)
}


function addNews() {
    if (!confirm("确认提交？"))
        return;

    var model = new Object();
    model.newsId = $('#newId').val();
    model.newType = $('#newtype').val();
    model.newsTitle = $('#tb_companyNewsTitle').val();
    model.isTop = $('#cb_isTop').is(':checked') ? '1' : '0';
    model.showTime = new Date($('#datepicker').val()).getTime();
    model.showSource = $('#tb_newSource').val();
    model.newsContent = editor.getData();

    $.ajax({
        url: "/subNews",
        async: false,
        type: "post",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(model),
        success: function (data) {
            alert(data.msg);
            if (data.success) {
               window.location.href="newslist";
            }
        }
    });
}
