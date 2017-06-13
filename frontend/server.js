var express = require('express');
var morgan = require('morgan')
var path = require('path');
var bodyParser = require('body-parser');
var app = express();

app.use(morgan('dev')); 
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));
app.use(express.static('/usr/src/app'));

/* GET home page. */
app.get('/', function(req, res, next) {
  //Path to your main file
  res.status(200).sendFile('./index.html'); 
});

app.listen(8080);
console.log("App listening on port 8080");