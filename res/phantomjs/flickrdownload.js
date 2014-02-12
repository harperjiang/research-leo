var page = require('webpage').create();
var system = require('system');
var fs = require('fs');
page.viewportSize = {
	width : 1920,
	height : 16000
};
var url = "http://www.flickr.com/signin";

function login() {
	console.log("Login page loaded");
	page.evaluate(function() {
		document.getElementById("username").value = 'harperjiang';
		document.getElementById('passwd').value = 'J1eninan';
		document.getElementById('.save').click();
	});
	window.setTimeout(function() {
		extract_image(page);
		phantom.exit(0);
	}, 5000);
}

function extract_image(pg) {
	console.log("Logged in, start downloading image...");
	var array = pg
			.evaluate(function() {
				var imglist = document.querySelectorAll("img.defer");
				var urllist = new Array();
				var count = 0;
				for (var i = 0; i < imglist.length; i++) {
					if (imglist[i].width > 500
							&& imglist[i].src != 'http://l.yimg.com/g/images/spaceout.gif')
						urllist[count++] = imglist[i].src;
				}
				return urllist;
			});
	var stream = fs.open('urlfile', 'wa');
	for (i in array) {
		stream.writeLine(array[i]);
	}
	stream.close();
	console.log("Image url downloaded");
}

page.open(url, login);
