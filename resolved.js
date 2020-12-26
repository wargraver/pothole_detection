function get_resolved(done){
    $.post('https://bug-slayerss.herokuapp.com/check-official/resolved',{
        token:window.localStorage.token
    },function(data){
        done(data)
    })
}
function load(text,temp,i){
    // console.log(text.coordinates);
    return $(` <div class="col-sm-4">
    <div class= "card w-75 ml-5 mb-4 mt-5">
    <div class="card-body">
    <b><h5 class="card-title text-center"> <i class="fas fa-check-double"></i>  Request ${i+1}</h5></b><hr>
    <p class="card-text"><b>Pincode:</b> ${text.address}
     <br>   <b>Coord:</b> ${text.coordinates}
    <br>
     <div class="ml-2">
     <span class="user"><button value=${text.reported_by} class="btn btn-dark">View User</button></button> </span> 
     <span class="map">
        <button value=${temp} class="btn btn-dark ml-5">View in map</button>
    </span>
    <div>
    </p>
  </div>
  </div>
  </div>`)
}

$(function(){
    let contain=$('#dashboard')
    get_resolved(function(data){
       contain.empty()
       if(data.error!=null) window.location.replace('https://bugslayerss.netlify.app/public/login')
      else{ for(let i=0;i<data.issues_solved.length;i++){
        // data.issues_solved[i].coordinates = data.issues_solved[i].coordinates;
        // console.log(data.issues_solved[i].coordinates.split())
        let temp=''
        for(let j=0;j<data.issues_solved[i].coordinates.length;j++){
            if(data.issues_solved[i].coordinates[j]===' '){
                temp+=',';
            }
            else temp+=data.issues_solved[i].coordinates[j]
        }
        console.log(temp)
        console.log(typeof data.issues_solved[i].coordinates);
        contain.append(load(data.issues_solved[i],temp,i))
       }
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