const mongoose = require('mongoose');
const dotenv = require('dotenv');

dotenv.config({ path: '.env' });
// const {username,password,dbname}=require('./varaibles.js')
let db;
const conn = async () => {
	try {
		db = await mongoose.connect(process.env.DB_CONNECT,);
		console.log('connected to db', db);
	} catch (error) {
		console.log(error);
	}
};
conn();
module.exports = { db };
