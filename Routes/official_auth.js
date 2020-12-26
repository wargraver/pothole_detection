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
