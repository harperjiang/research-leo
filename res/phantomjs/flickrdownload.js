var page = require('webpage').create();
var system = require('system');
var fs = require('fs');
var loop = 1;
var wait = 4000;
var interval = 50;

var date = system.args[1];

page.viewportSize = {
	width : 1920,
	height : 16000
};
var url = "http://www.flickr.com/signin";
var exploreUrl = "http://www.flickr.com/explore/";
function login() {
	console.log("Login page loaded");
	page.evaluate(function() {
		document.getElementById("username").value = 'harperjiang';
		document.getElementById('passwd').value = 'J1eninan';
		document.getElementById('.save').click();
	});
	console.log("Logged in.");
	window.setTimeout(explore_page, wait);
}

function explore_page() {
	exploreUrl += date;
	console.log(exploreUrl);
	page.open(exploreUrl);
	window.setTimeout(scroll_down, wait, 0);
}

function scroll_down(count) {
	if (count == 3) {
		window.setTimeout(extract_image, 2000);
	} else {
		page.evaluate(function() {
			window.document.body.scrollTop = document.body.scrollHeight;
		});
		window.setTimeout(scroll_down, 10000, count + 1);
	}
}

function extract_image() {
	console.log("Start downloading image...");
	var array = page
			.evaluate(function() {
				var imglist = document.querySelectorAll("img.pc_img");
				var urllist = new Array();
				var count = 0;
				for (var i = 0; i < imglist.length; i++) {
					if (imglist[i].width > 300
							&& imglist[i].src != 'http://l.yimg.com/g/images/spaceball.gif')
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
	phantom.exit();
}

page.open(url, login);
