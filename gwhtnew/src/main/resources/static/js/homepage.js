/**
 * Created by wujianlong on 2017/5/10.
 */
//修改登录密码
function ChangeLoginPassword() {
    var pwd1 = $("#ip_Password1").val();
    var pwd2 = $("#ip_Password2").val();
    var pwd3 = $("#ip_Password3").val();
    if (pwd1 == "" || pwd2 == "" || pwd3 == "") {
        alert("原密码、新密码、重复新密码不能为空！");
        return;
    }

    if (confirm('确定修改登录密码？')) {

        if (pwd2 != pwd3) {
            alert('两次重复的密码不一致！');
            return;
        }
        $.ajax({
            url: "/changePassword",
            type: "post",
            async: false,
            data: {pwd1: pwd1, pwd2: pwd2, pwd3: pwd3},
            success: function (data) {
                alert(data.msg);
            }
        });
    }


}