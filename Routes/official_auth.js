// login/signup route for officals
const dotenv = require('dotenv');
dotenv.config({ path: '.env' });
const route = require('express').Router();
const bcrypt = require('bcrypt');
const validator = require('validator');
const jwt = require('jsonwebtoken');

const { official, token_official } = require('../Database/modals.js');

// signup route
route.post('/signup', async (req, res) => {
	const official_object = req.body.official_object;
	try {
		const { employee_id, name, password, city } = req.body;
		// checking if any field is empty
		if (
			validator.isEmpty(employee_id) ||
			validator.isEmpty(name) ||
			validator.isEmpty(password) ||
			validator.isEmpty(city)
		) {
			official_object.error = 'Please fill all fields';
			res.status(200).send(JSON.stringify(official_object));
			return;
		}
		// checking if the employee_id is already present in db or not
		const found = await official.findOne({ employee_id: employee_id });
		if (found != null) {
			official_object.error = 'User already exists';
			res.status(200).send(JSON.stringify(official_object));
			return;
		} else {
			//hashing the password and storing it in db
			const hashPass = await bcrypt.hash(password, 8);
			const newOfficial = new official({
				employee_id: req.body.employee_id,
				name: name,
                password: hashPass,
                city
			});
			const savedOfficial = await newOfficial.save();
			official_object.employee_id = savedOfficial.employee_id;
			official_object.name = savedOfficial.name;
			official_object.issues_solved = savedOfficial.issues_solved;
			official_object.msg = 'Signed up successfully';
			res.status(200).send(JSON.stringify(official_object));
			return;
		}
	} catch (err) {
		console.log(err);
		official_object.error = 'Something went wrong while signing up Official';
		res.status(504).send(JSON.stringify(official_object));
		return;
	}
});


// login route
route.post('/login', async (req, res) => {
	const official_object = req.body.official_object;
	try {
		const { employee_id, password } = req.body;
		//checking if the field is empty or not
		if (validator.isEmpty(employee_id) || validator.isEmpty(password)) {
			official_object.error = 'Please enter all fields';
			res.status(200).send(JSON.stringify(official_object));
			return;
		}

		// checking if the id exists in the db or not
		const found = await official.findOne({ employee_id });
		if (found == null) {
			official_object.error = 'Invalid Credentials';
			res.status(200).send(JSON.stringify(official_object));
			return;
		}

		//checking if the password is correct
		const hashPass = found.password;
		const equal = await bcrypt.compare(password, hashPass);
		console.log(equal);
		if (equal === false) {
			official_object.error = 'Invalid Credentials';
			res.status(200).send(JSON.stringify(official_object));
			return;
		}

		// generating token using jwt
		const gen_token = jwt.sign(employee_id, process.env.auth_official_key);

		// checking if the token already exists in db
		const val = await token_official.findOne({ value: gen_token });
		if (val != null) {
			await token_official.deleteOne({ value: gen_token });
		}
		const save_token = new token_official({
			value: gen_token,
			owner: found.id,
		});
		const vari = await save_token.save();
		official_object.msg = 'Logged in successfully';
		official_object.token = gen_token;
		official_object.employee_id = found.employee_id;
		official_object.name = found.name;
		official_object.issues_solved = found.issues_solved;
		res.status(200).send(JSON.stringify(official_object));
	} catch (err) {
		console.log(err);
		official_object.error = 'Something went wrong while logging in';
		res.status(500).send(JSON.stringify(official_object));
	}
});

// Logout route
route.post('/logout', async (req, res) => {
	const official_object = req.body.official_object;
	try {
		await token_official.deleteOne({ value: req.body.token });
		official_object.msg = 'Logged out user successfully';
		res.status(200).send(JSON.stringify(official_object));
	} catch (err) {
		console.log(err);
		official_object.error = 'Something went wrong while Logging out user';
		res.status(500).send(official_object);
	}
});

// exporting route
module.exports = route;