function signup(name,id,pass,city_val,done){
    $.post('https://bug-slayerss.herokuapp.com/official/signup',{
        name:name,
        employee_id:id,
        password:pass,
        city:city_val
    },function(data){
           done(data)
    })
}

$(function(){
let id=$('#employeeid')
let name=$('#name')
let pass=$('#inputPassword')
let city=$('#city')
$('#btn1').click(()=>{
  if(name.val()==='' || pass.val()===''  || id.val()==='' || city.val()===''){
      window.alert("Please enter all the fields")
  }
  else{
      signup(name.val(),id.val(),pass.val(),city.val(),function(data){
            console.log(data)
            if(data.error!=null){ 
                let contain=$('#contain')
                contain.empty()
                contain.append($(`<p class="text-danger">This User Already exists</p>`))
            }
            else{
                window.location.replace('https://bugslayerss.netlify.app/public/login')
            }
      })
  }
})
})