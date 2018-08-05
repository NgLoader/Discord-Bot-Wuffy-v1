var http = require('http');
var url = require('url');
var figlet = require('./lib/node-figlet.js');

http.createServer(function (request, response) {
	try {
		var responseMessage = {"code": "400 Bad Request", "message": ""};

		switch(request.method) {
			case "GET":
				var parameters = url.parse(request.url, true).query;

				if(Object.keys(parameters).length > 0) {
					if(parameters.type != null) {
						switch(parameters.type) {
							case "make":
								let text = parameters.text;
								let font = parameters.font;
								let horizontalLayout = parameters.horizontalLayout;
								let verticalLayout = parameters.verticalLayout;

								if(text != null) {
									responseMessage.code = "200 OK";
									responseMessage.message = Buffer.from(figlet.textSync(text, {
										font: font != null ? font : 'Standard',
										horizontalLayout: horizontalLayout != null ? horizontalLayout : 'default',
										verticalLayout: verticalLayout != null ? verticalLayout : 'default'
									}), 'ascii').toString('base64');
								} else {
									responseMessage.message = "No Text";
								}
							break;

							case "list":
								responseMessage.code = "200 OK";
								responseMessage.message = figlet.fontsSync();
								break;

							default:
								break;
						}
					}
				}
				break;

			default:
				break;
		}

		response.writeHead(200, {'Content-Type': 'text/plain'});
		response.end(JSON.stringify(responseMessage));
	} catch(error) {
		if(response != null)
			response.end('{"code": "400 Bad Request", "message": ""}');
	}
}).listen(8009);
console.log('Server running at http://localhost:8009/');