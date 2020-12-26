//This file contains signup,login and logout routes for User.................
const route = require('express').Router();
const bcrypt = require('bcrypt');
const { auth_user } = require('../auth/jwt_auth_user.js');
const validator = require('validator');
const jwt = require('jsonwebtoken');
const dotenv = require('dotenv');
dotenv.config({ path: '.env' });
const { user, official, location, token } = require('../Database/modals.js');

const sgMail = require('@sendgrid/mail');
sgMail.setApiKey(process.env.key);

//desiging signup routes for user
route.post('/signup', async (req, res) => {
	const obj = req.body.object_user;
	try {
		const email = req.body.email;
		const password = req.body.password;
		const name = req.body.name;
		const phn_no = req.body.phn_no;
		//console.log(obj)
		//checking if any field is empty or not
		if (
			validator.isEmpty(email) ||
			validator.isEmpty(name) ||
			validator.isEmpty(password)
		) {
			obj.Error = 'Please enter all the fields';
			res.status(200).send(JSON.stringify(obj));
			return;
		}
		//checking whether email is valid
		if (validator.isEmail(email) === false) {
			obj.Error = 'Please enter valid email id';
			res.status(200).send(JSON.stringify(obj));
			return;
		}
		//checking if email is unique or not
		const data2 = await user.findOne({ email: email });
		if (data2 != null) {
			obj.Error = 'This Email is alreqady registered';
			res.status(200).send(JSON.stringify(obj));
			return;
		} else {
			//hashing the user password before stroing it to db
			const new_pass = await bcrypt.hash(password, 8);
			const new_user = new user({
				email: email,
				password: new_pass,
				name: name,
				phn_no: phn_no,
			});
			const saved_user = await new_user.save();
			obj.Email = saved_user.email;
			obj.Name = saved_user.name;
			obj.Requests = saved_user.points;
			obj.Message = 'User signed up successfully';
			obj.Phn_no = saved_user.phn_no;
			res.status(200).send(JSON.stringify(obj));
			//Sending mail to user for succesfull registration
			const user_email = saved_user.email;
			const msg = {
				to: user_email,
				from: process.env.EMAIL_ID, // Use the email address or domain you verified above
				subject: 'Thanks for registering',
				text: 'and easy to do anywhere, even with Node.js',
				html: `<p>Thanks ${saved_user.name} for registering with us.</p><br>
			<p> Please report the Potholes around your locality</p><br>
			`,
			};
			sgMail.send(msg).then(
				() => {},
				(error) => {
					console.error(error);

					if (error.response) {
						console.error(error.response.body);
					}
				}
			);
			return;
		}
	} catch (err) {
		console.log(err);
		obj.Error = 'something went wrong while signing up user';
		res.status(504).send(JSON.stringify(obj));
	}
});

//Desigining Login route for user
route.post('/login', async (req, res) => {
	const obj = req.body.object_user;
	try {
		const email = req.body.email;
		const password = req.body.password;
		//checking if any field is empty or not
		if (validator.isEmpty(email) || validator.isEmpty(password)) {
			obj.Error = 'Please enter all the fields';
			res.status(200).send(JSON.stringify(obj));
			return;
		}
		//checking whether email is valid
		if (validator.isEmail(email) == false) {
			obj.Error = 'Please enter valid email';
			res.status(200).send(JSON.stringify(obj));
			return;
		}
		//checking if user with given email exists or not
		const data2 = await user.findOne({ email: email });
		if (data2 == null) {
			obj.Error = 'Invalid Credentials';
			res.status(200).send(JSON.stringify(obj));
			return;
		} else {
			const hashed_pass = data2.password;
			const result = await bcrypt.compare(password, hashed_pass);
			if (result == true) {
				const gen_token = jwt.sign(email, process.env.auth_user_key);
				//if genrated token already exists
				const val2 = await token.findOne({ value: gen_token });
				if (val2 != null) {
					await token.deleteOne({ value: gen_token });
				}
				const save_token = new token({
					value: gen_token,
					owner: data2._id,
				});
				const vari = await save_token.save();
				(obj.Message = 'Logged in Successfully'),
					(obj.Token = gen_token),
					res.status(200).send(JSON.stringify(obj));
				return;
			} else {
				obj.Error = 'Invalid Credentials';
				res.status(200).send(JSON.stringify(obj));
			}
		}
	} catch (err) {
		console.log(err);
		obj.Error = 'Something went wrong while logging in user';
		res.status(500).send(JSON.stringify(obj));
	}
});

//Designing Routes for logout of user
route.post('/logout', async (req, res) => {
	const obj = req.body.object_user;
	try {
		await token.deleteOne({ value: req.body.token });
		obj.Message = 'Logged out user successfully';
		res.status(200).send(JSON.stringify(obj));
	} catch (err) {
		console.log(err);
		obj.Error = 'Something went wrong while Logging out user';
		res.status(200).send(JSON.stringify(obj));
	}
});

// Route for Forgot Password
route.post('/forgotpassword', async (req, res) => {
	const obj = req.body.object_user;
	try {
		const email = req.body.email;
		const data = await user.findOne({ email });
		console.log(data);
		if (!data) {
			obj.Error = 'The given Email does not exist';
			res.status(200).send(JSON.stringify(obj));
		} else {
			const gen_token = jwt.sign({ email }, process.env.auth_user_reset_key, {
				expiresIn: 300,
			});
			obj.Message = 'Email Sent';
			res.status(200).send(JSON.stringify(obj));
			const msg = {
				to: email,
				from: process.env.EMAIL_ID, // Use the email address or domain you verified above
				subject: 'Reset Password',
				text: 'and easy to do anywhere, even with Node.js',
				html: `<p>To reset password click <a href="https://bugslayerss.netlify.app/public/reset?token=${gen_token}">here</p></a><br>
				<p>The link will expire in 5 minutes</p> `,
			};
			sgMail.send(msg).then(
				() => {},
				(error) => {
					console.error(error);

					if (error.response) {
						console.error(error.response.body);
					}
				}
			);
		}
	} catch (err) {
		console.log(err);
		obj.Error = 'Something went wrong while Logging out user';
		res.status(500).send(JSON.stringify(obj));
	}
});

// Reset password route
route.post('/resetpassword', async (req, res) => {
	const obj = req.body.object_user;
	try {
		const newpass = req.body.newpass;
		const new_pass = await bcrypt.hash(newpass, 8);
		const token = req.body.token;
		var useremail = jwt.verify(token, process.env.auth_user_reset_key);
		const email = useremail.email;
		const data = await user.findOne({ email });
		if (data) {
			data.password = new_pass;
			const updated_user = await data.save();
			obj.Message = 'Password changed successfully';
			obj.Email = data.email;
			obj.Name = data.name;
			obj.Phn_no = data.phn_no;
			res.status(200).send(JSON.stringify(obj));
		} else {
			obj.Error = ' You are not Authorized';
			res.status(201).send(JSON.stringify(obj));
		}
	} catch (err) {
		console.log(err);
		obj.Error = 'Something went wrong';
		res.status(500).send(JSON.stringify(obj));
	}
});
module.exports = route;
