function logout(done){
    $.post('https://bug-slayerss.herokuapp.com/official/logout',{
        token:window.localStorage.token
    },function(data){
        done(data)
    })
}

$(function(){
    $('#btn2').click(()=>{
        logout(function(data){
             window.localStorage.token=undefined
           window.location.replace('https://bugslayerss.netlify.app/public/login')
        })
    })
})