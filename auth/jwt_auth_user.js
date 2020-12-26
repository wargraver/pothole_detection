//This will use a middleware to perform authentication of user using jsonweb token...................
const dotenv = require('dotenv');
dotenv.config({ path: '.env' });
const { user, official, location, token } = require('../Database/modals');
const jwt = require('jsonwebtoken');

//auth_user mioddleware will be uses to authenticate user's request
const auth_user = async (req, res, next) => {
	const obj=req.body.object_user
	try {
		//checking if token is valid
		var given = req.body.token;
		if (given === null || given === undefined) {
			obj.Error='Please Login First'
			res.status(200).send(JSON.stringify(obj));
			return;
		}
		//Checking if this token exists on DB or not
		const data = await token.findOne({ value: given });
		//console.log(data)
		if (data === null || data === undefined) {
			obj.Error='Invalid token'
			res.status(200).send(JSON.stringify(obj));
			return;
		}

		const user_stored = await user.findOne({ _id: data.owner });
		//getting email from which token was genrated
		var useremail = jwt.verify(
			given,
			process.env.auth_user_key
		);

		if (user_stored.email != useremail) {
			obj.Error='Invalid token'
			res.status(200).send(JSON.stringify(obj));
			return;
		}
		req.body.user = user_stored;
		next();
	} catch (err) {
		console.log(err);
		obj.Error='Something went wrong in auth user middleware'
		res.status(500).send(JSON.stringify(obj));
		return;
	}
};

//Exporitng the middleware
module.exports = {
	auth_user,
};
