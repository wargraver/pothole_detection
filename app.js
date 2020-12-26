//Importing all packages
const express = require('express');
const dotenv = require('dotenv');
const mongoose = require('mongoose');
const morgan = require('morgan');

const app = express();
dotenv.config({ path: '.env' });

// middlewares
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(morgan('dev'));

// Importing all the modals form modals.js
const {
	user,	
	location,
	official,
	token,
	token_official,
} = require('./Database/modals.js');
const conn = async () => {
	try {
		//connecting to db
		const db = await mongoose.connect(process.env.DB_CONNECT, {
			useNewUrlParser: true,
			useUnifiedTopology: true,
			useFindAndModify: false,
		});
		console.log('connected to db');
	} catch (error) {
		console.log(error);
	}
};
//Calling Function to connect to mongodb
conn();

//Adding the user object to every Incoming request
app.use((req, res, next) => {
	req.body.object_user = {
		Error: "null",
		Email: "null",
		Name: "null",
		Token: "null",
		Requests: "null",
		Message: "null",
		Status: false,
		Pending_request:[],
		Solved_request:[],
		Phn_no:"null",
		Address:"null",
	};
	next();
});

// Adding the official object to every Incoming request
app.use((req, res, next) => {
	req.body.official_object = {
		error: null,
		msg: null,
		employee_id: null,
		email:null,
		name: null,
		token: null,
		issues_solved: null,
		issues_pending: null,
		status: null,
	};
	next();
});

//Resolving CORS errors
app.use((req, res, next) => {
	res.setHeader('Access-Control-Allow-Origin', '*');
	res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, PATCH');
	res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization');
	res.setHeader('Content-Type', 'application/json');
	next();
});

//routes
app.get('/hello', (req, res) => {
	res.status(200).send(JSON.stringify({ msg: 'hello' }));
});
app.use('/user', require('./Routes/user_auth.js'));
app.use('/official', require('./Routes/official_auth.js'));
app.use('/check-official', require('./Routes/official_status.js'));
app.use('/check-user', require('./Routes/user_status.js'));

//starting  server
const port = process.env.PORT || 3000;
app.listen(port, () => {
	console.log('server started');
});