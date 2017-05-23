/**
 * Created by wujianlong on 2017/5/4.
 */
function validate()
{
    var result=true;
    var username=$("input[name='username']").val();
    var password=$("inputp[name='password']").val();
    if(username==""||password==""){
        alert("用户名或密码不能为空！");
        result= false;
    }
    return result;
}