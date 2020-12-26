// this file contains routes for viewing and resolving issues
const route = require('express').Router();
const dotenv = require('dotenv');
dotenv.config({ path: '.env' });
//const sendgrid=require('nodemailer-sendgrid-transport')
const sgMail = require('@sendgrid/mail');
sgMail.setApiKey(process.env.key);

const { auth_official } = require('../auth/jwt_auth_official');
const {
	user,
	official,
	location,
	token,
	token_official,
} = require('../Database/modals.js');

//route to fetch status of the request
route.post('/status/:id', auth_official, async (req, res, next) => {
	const official_object = req.body.official_object;
	try {
		const req_id = req.params.id;
		const req_status = await location.findOne({ _id: req_id });
		if (req_status === null || req_status == undefined) {
			official_object.error = 'Requested issue not available';
			res.send(404).send(JSON.stringify(official_object));
			return;
		}
		official_object.status = req_status.status;
		res.status(200).send(JSON.stringify(official_object));
		return;
	} catch (err) {
		console.log(err);
		official_object.error =
			'Something went wrong while fetching request status';
		res.status(500).send(JSON.stringify(official_object));
		return;
	}
});

//Route for resolving request
route.post('/resolve/:id', auth_official, async (req, res) => {
	const req_id = req.params.id;
	const official_object = req.body.official_object;
	try {
		const req_status = await location
			.findOne({ _id: req_id })
			.populate('reported_by');
		if (req_status === null || req_status === undefined) {
			official_object.error = 'Requested issue not available';
			res.status(404).send(JSON.stringify(official_object));
			return;
		}
		if (req_status.status === true) {
			official_object.msg = 'This issue is already resolved';
			res.status(200).send(JSON.stringify(official_object));
			return;
		}
		req_status.status = true;
		const saved_status = await req_status.save();
		req.body.official.issues_solved.push(req_id);
		const updated_official = await req.body.official.save();
		official_object.msg = 'Issue is resolved';
		official_object.status = true;
		res.status(200).send(JSON.stringify(official_object));
		console.log(process.env.key);
		// sending mail to the user who created the issue
		const user_email = req_status.reported_by.email;
		console.log(req_status);
		const msg = {
			to: user_email,
			from: process.env.EMAIL_ID, // Use the email address or domain you verified above
			subject: 'Issue Status',
			text: 'and easy to do anywhere, even with Node.js',
			html: `<p>The issue reported by you has been resolved</p><br>
			<p> Details:</p><br>
			<p>Id: ${req_status._id}<br>
			<p>Location: ${req_status.coordinates}`,
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
	} catch (err) {
		console.log(err);
		official_object.error = 'Something went wrong';
		res.status(500).send(JSON.stringify(official_object));
	}
});

// get all pending issues
route.post('/pending', auth_official, async (req, res) => {
	const official_object = req.body.official_object;
	try {
		let city = req.body.official.city.replace(/\s+/g, ' ').trim().toLowerCase();
		const request_array = await location.find({ status: false, address: city });
		official_object.issues_pending = request_array;
		res.status(200).send(JSON.stringify(official_object));
	} catch (err) {
		console.log(err);
		official_object.error = 'Something went wrong';
		res.status(500).send(JSON.stringify(official_object));
	}
});

// check all resolved issues
route.post('/resolved', auth_official, async (req, res) => {
	const official_object = req.body.official_object;
	try {
		const request_array = await location.find({ status: true });
		official_object.issues_solved = request_array;
		res.status(200).send(JSON.stringify(official_object));
	} catch (err) {
		console.log(err);
		official_object.error = 'Something went wrong';
		res.status(500).send(JSON.stringify(official_object));
	}
});

//Route for getting user info
route.post('/info', auth_official, async (req, res, next) => {
	const obj = req.body.object_user;
	try {
		const id = req.body.id;
		const data = await user.findOne({ _id: id });
		console.log(data);
		obj.Email = data.email;
		obj.Name = data.name;
		obj.Phn_no = data.phn_no;
		const request_array = await location.find({
			reported_by: data._id,
			status: false,
		});
		obj.Pending_request = request_array;
		const resolved_array = await location.find({
			reported_by: data._id,
			status: true,
		});
		obj.Solved_request = resolved_array;
		res.status(200).send(JSON.stringify(obj));
	} catch (err) {
		console.log(err);
		user_object.error = 'Something went wrong';
		res.status(500).send(JSON.stringify(obj));
	}
});

module.exports = route;
