function logout(done){
    $.post('https://bug-slayers.herokuapp.com/official/logout',{
        token:window.localStorage.token
    },function(data){
        done(data)
    })
}

$(function(){
    $('#btn2').click(()=>{
        logout(function(data){
             window.localStorage.token=undefined
           window.location.replace('https://bugslayers.netlify.app/public/login')
        })
    })
})