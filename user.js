function get_pending(done){
    $.post('https://bug-slayerss.herokuapp.com/check-official/info',{
        token:window.localStorage.token,
        id:window.localStorage.id
    },function(data){
        done(data)
    })
}
function user_load(text){
    return $(`
    <div class="col-7 mt-4">
            <div class="card"">
                <div class="card-horizontal">
                    <div class="img-square-wrapper p-5 ">
                        <img class="img-fluid" style="height: 9rem;" src="./by2.png" alt="Card image cap">
                    </div>
                    <div class="card-body ml-5 p-5 mt-2">
                        <h4 class="card-title">User</h4>
                        <p class="card-text"><b>Name:</b> ${text.Name}
                        <br> <b>Email:</b> ${text.Email}
                        <br> <b>Phone NO:</b> ${text.Phn_no}
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>`)
}
function load(text,i,flag){
    return $(`  <div class="col-sm-4">
    <div class= "card w-75 ml-5 mb-4 mt-5">
    <div class="card-body">
    <b><h5 class="card-title text-center"><i class="fas fa-exclamation-triangle"></i>  Request ${i+1}</h5></b><hr>
    <p class="card-text"><b>Address:</b> ${text.address}
     <br>   <b>Coord:</b> ${text.coordinates}
    <br>  <b>Resolved:</b> ${flag}
    </p>
  </div>
  </div>
  </div>`)
}
$(function(){
    let contain=$('#user')
    let msg=$('#msg')
    let res=$('#res')
    get_pending(function(data){
       msg.empty()
       contain.empty()
       //res.emtpy()
       msg.append(user_load(data))
       if(data.error!=null) window.location.replace('https://bugslayerss.netlify.app/public/login')
      else{ for(let i=0;i<data.Pending_request.length;i++){
        contain.append(load(data.Pending_request[i],i,false))
       }
       for(let i=0;i<data.Solved_request.length;i++){
        res.append(load(data.Solved_request[i],i,true))
       }
    }
})

    
})