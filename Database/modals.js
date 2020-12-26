const mongoose = require('mongoose');
const user = mongoose.model('User', {
	email: {
		type: String,
		required: true,
		unique: true,
	},
	name: {
		type: String,
	},
	password: {
		type: String,
	},
	points: [
		{
			type: mongoose.Schema.Types.ObjectId,
			ref: 'Location',
		},
	],
	phn_no:{
		type:String
	}
});
const official = mongoose.model('Official', {
	employee_id: {
		type: String,
		required: true,
		unique:true
	},
	name: {
		type: String,
	},
	password: {
		type: String,
	},
	city: {
		type: String
	},
	issues_solved: [
		{
			type: mongoose.Schema.Types.ObjectId,
			ref: 'Location',
		}
	]
});
const token = mongoose.model('Token', {
	value: {
		type: String,
		unique: true,
	},
	owner: {
		type: mongoose.Schema.Types.ObjectId,
		ref: 'User',
	},
});
const token_official = mongoose.model('Token_Official', {
	value: {
		type: String,
		unique: true,
	},
	owner: {
		type: mongoose.Schema.Types.ObjectId,
		ref: 'Official',
	},
});
const location = mongoose.model('Location', {
	coordinates: {
		type: String,
		required: true,
		unique: true,
	},
	address:{
		type: String,
	},
	status:{
		type:Boolean,
	},
	reported_by: {
		type: mongoose.Schema.Types.ObjectId,
		ref: 'User',
	},
});
module.exports = { user, location, token, official, token_official };
