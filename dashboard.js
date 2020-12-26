function get_pending(done){
    $.post('https://bug-slayerss.herokuapp.com/check-official/pending',{
        token:window.localStorage.token
    },function(data){
        done(data)
    })
}
function load(text,temp,i){
    return $(` <div class="col-sm-4">
    <div class= "card w-75 ml-5 mb-4 mt-5">
    <div class="card-body">
    <b><h5 class="card-title text-center"><i class="fas fa-exclamation-triangle"></i>  Request ${i+1}</h5></b><hr>
    <p class="card-text"><b>Pincode:</b> ${text.address}
     <br>   <b>Coord:</b> ${text.coordinates}
    <br>
     <div>
     <span class="user ml-2"><button value=${text.reported_by} class="btn btn-dark">User</button></button> </span> 
     <span class="map ml-1">
         <button value=${temp} class="btn btn-dark">Map View</button>
     </span>
     <span class="child ml-1">
    <button value=${text._id} class="btn btn-dark">Resolve</button>
    </span>
    <div>
    </p>
  </div>
  </div>
  </div>`)
}
function resolve(req_id,done){
    $.post(`https://bug-slayerss.herokuapp.com/check-official/resolve/`+req_id,{
      token:window.localStorage.token
    },function(data){
        done(data)
    })
}
$(function(){
    let contain=$('#dashboard')
    get_pending(function(data){
       contain.empty()
       if(data.error!=null) window.location.replace('https://bugslayerss.netlify.app/public/login')
      else{ for(let i=0;i<data.issues_pending.length;i++){
        let temp=''
        for(let j=0;j<data.issues_pending[i].coordinates.length;j++){
            if(data.issues_pending[i].coordinates[j]===' '){
                temp+=',';
            }
            else temp+=data.issues_pending[i].coordinates[j]
        }
        contain.append(load(data.issues_pending[i],temp,i))
       }
       $('.child').on('click',function(clicked){
        let id=($(clicked)[0].target.value).toString()
           resolve(id,function(data){
            let temp=$('#msg')
           temp.empty()
           temp.append($(`<p class="text-success"> Request Resolved and email to user has been sent!!!</p>`))
           window.location.replace('https://bugslayerss.netlify.app/dashboard')
       })
    })
    $('.user').on('click',function(clicked){
        let id2=($(clicked)[0].target.value).toString()
        console.log(id2)
        window.localStorage.id=id2
        //window.open('https://bugslayers.netlify.app/user')
        window.location.replace('https://bugslayerss.netlify.app/user')
    })
    $('.map').on('click',function(clicked){
        let coor=($(clicked)[0].target.value).toString()
        // console.log(coor)
        // console.log(coordinates)
        url = "https://www.google.com.sa/maps/search/"+ coor;
        window.open(url, '_blank');
    })
    }
})

    
})