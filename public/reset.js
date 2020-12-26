function getParameterByName(name, url = window.location.href) {
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

function login(token,password,done){
    $.post('https://bug-slayerss.herokuapp.com/user/resetpassword',{
       newpass:password, 
       token:token
    },function(data){
        done(data)
    })
}

$(function(){
    let password=$('#employeeid')
    var foo = getParameterByName('token');
    console.log(foo)
    $('#btn1').click(()=>{
        login(foo,password.val(),function(data){
            console.log(data)
            //console.log(data.token)
       if(data.Error!="null") { 
        let contain=$('#contain')
       //contain.empty()
       contain.append($(`<p class="text-danger">Invalid link</p>`))}
     else{
       // contain.empty()
        contain.append($(`<p class="text-danger">Password Chnaged</p>`))
             //window.localStorage.token=data.token
           window.alert("Please Login Using App")
           wondow.close()
        }
        })
    })
})