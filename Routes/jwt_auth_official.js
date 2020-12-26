// Middleware for authentication of OFFICIAL using JWT
const dotenv = require('dotenv');
dotenv.config({ path: '.env' });
const {
	user,
	official,
	location,
	token_official,
} = require('../Database/modals.js');
const jwt = require('jsonwebtoken');

// Middleware- auth_official
const auth_official = async (req, res, next) => {
	const official_object = req.body.official_object;
	try {
		const body_token = req.body.token;
		if (body_token === null || body_token === undefined) {
			official_object.error = 'Unauthorised Access'
			res.status(200).send(JSON.stringify(official_object));
			return;
		}

		// checking if the token exists in DB
		const found = await token_official.findOne({ value: body_token });
		if (found === null || found === undefined) {
			official_object.error = 'Unauthorised Access'
			res.status(200).send(JSON.stringify(official_object));
			return;
		}

		//finding id of the official in DB
		const official_stored = await official.findOne({ _id: found.owner });
		//getting employee_id from which token is generated
		var employeeID = jwt.verify(
			body_token,
			process.env.auth_official_key
        );
		if (official_stored.employee_id != employeeID) {
			official_object.error = 'Unauthorised Access'
			res.status(200).send(JSON.stringify(official_object));
			return;
		}
        req.body.official = official_stored;
        next();
	} catch (err) {
		console.log(err);
		official_object.error = 'Something went wrong while authenticating official'
		res.status(500).send(JSON.stringify(official_object));
	}
};

// exporting middleware
module.exports ={ auth_official}
