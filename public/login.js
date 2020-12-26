function login(employee_id,password,done){
    $.post('https://bug-slayerss.herokuapp.com/official/login',{
        employee_id:employee_id,
        password:password
    },function(data){
        done(data)
    })
}

$(function(){
    let email=$('#employeeid')
    let pass=$('#inputPassword')
    console.log(email.val())
    $('#btn1').click(()=>{
        login(email.val(),pass.val(),function(data){
            console.log(data)
            console.log(data.token)
       if(data.token===null) { 
        let contain=$('#contain')
       contain.empty()
       contain.append($(`<p class="text-danger">Incorrect ID or Password</p>`))}
     else{
             window.localStorage.token=data.token
           window.location.replace('https://bugslayerss.netlify.app/dashboard')
        }
        })
    })
})