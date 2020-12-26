//This file contains routes by which user can view/submit his request for potholes............

const route = require('express').Router();
const {auth_user}=require('../auth/jwt_auth_user.js')
const { user, official, location, token } = require('../Database/modals.js');

//route to fetch status of a request whose id in db is id
route.post('/status/:id',auth_user,async (req,res,next)=>{
    const obj=req.body.object_user
    try{
        const req_id=req.params.id 
        const req_status=await location.findOne({_id:req_id})
        obj.Status=req_status.status
        res.status(200).send(JSON.stringify(obj)) 
        return
    }
    catch(err){
      console.log(err)
      obj.Error="Something went wrong while fetching request status"
      res.status(500).send(JSON.stringify(obj)) 
      return
    }
})

//Route for getting user info
route.post('/info',auth_user,async (req,res,next)=>{
    const obj=req.body.object_user
    try{
        obj.Email=req.body.user.email
        obj.Name=req.body.user.name
        obj.Phn_no=req.body.user.phn_no
        const request_array=await location.find({reported_by:req.body.user._id,status:false})
        obj.Pending_request=request_array
        const resolved_array=await location.find({reported_by:req.body.user._id,status:true})
        obj.Solved_request=resolved_array
        res.status(200).send(JSON.stringify(obj))
    }
    catch(err){
        console.log(err)
        obj.Error="Something went wrong while fetching request status"
        res.status(500).send(JSON.stringify(obj)) 
        return
    }
})

//Route for posting a request
route.post('/create',auth_user,async (req,res,next)=>{
    const obj=req.body.object_user
    try{
        //Checking wheter request is vaid or not
       if(req.body.coordinates===null || req.body.coordinates===undefined){
           obj.Error="Please enter the Location to submit a request"
           res.status(200).send(JSON.stringify(obj))
           return
       }
       else{
           //Saving the new request's location to Db and creating realtin between user and complain
           const check= await location.find({coordinates:req.body.coordinates})
           if(check.length===1){
               obj.Error="Location already reported"
               res.status(200).send(JSON.stringify(obj))
           return
           }
           const len=req.body.address.length-2
           const pincode=req.body.address.substring(len-5,len+1)
           console.log(pincode)
           const request=new location({
               coordinates:req.body.coordinates,
               address:pincode,
               reported_by:req.body.user._id,
               status:false
           })
           const saved_loc=await request.save()
           const id=saved_loc._id
           req.body.user.points.push(id)
           const updated_user=await req.body.user.save()
           obj.Message="Report has been registered"
           //Sending the JSON object back in return
           res.status(200).send(JSON.stringify(obj))
           return
       }
    }
    catch(err){
        //Error handling
        console.log(err)
        obj.Error="Something went wrong while Posting the request"
        res.send(500).send(JSON.stringify(obj)) 
        return
    }
})
 
//Route for showing all pending the requests made my user
route.post('/pending',auth_user,async (req,res,next)=>{
    const obj=req.body.object_user
    try{
        //From Location document retriveing requests which have not been resolved but reportes by user 
       const request_array=await location.find({reported_by:req.body.user._id,status:false})
        obj.Pending_request=request_array
        const resolved_array=await location.find({reported_by:req.body.user._id,status:true})
        obj.Solved_request=resolved_array
        res.status(200).send(JSON.stringify(obj))
        return
    }
    catch(error){
        obj.Error="Something went wrong while fetching users request list"
        console.log(error)
        res.status(500).send(JSON.stringify(obj))
        return
    }
})

//Route for showing all requests which hRepoave been resolved
route.post('/resolved',auth_user,async (req,res,next)=>{
    const obj=req.body.object_user
    try{
        const resolved_array=await location.find({reported_by:req.body.user._id,status:true})
        obj.Solved_request=resolved_array
        res.status(200).send(JSON.stringify(obj))
        return
    }
    catch(err){
        obj.Error='Something went wrong while fetching resolved request list'
        console.log(err)
        res.status(500).send(JSON.stringify(obj))
        return
    }
})

//exproting the route to app.js

module.exports =route
