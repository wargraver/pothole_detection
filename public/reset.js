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
    var token = urlParams.get('token')
    console.log(token)
    $('#btn1').click(()=>{
        login(token,password.val(),function(data){
            console.log(data)
            //console.log(data.token)
       if(data.Error!="null") { 
        let contain=$('#contain')
       contain.empty()
       contain.append($(`<p class="text-danger">Invalid link</p>`))}
     else{
        contain.empty()
        contain.append($(`<p class="text-danger">Password Chnaged <br>Please login again using app</p>`))
             //window.localStorage.token=data.token
           //window.location.replace('https://bugslayerss.netlify.app/dashboard')
        }
        })
    })
})