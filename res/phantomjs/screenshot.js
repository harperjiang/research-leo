var page = require('webpage').create();
page.viewportSize = {
	width : 1024,
	height : 768
};
var system = require('system');
var url = system.args[1];
var file = system.args[2];
page.open(url, function() {
	page.render(file);
	phantom.exit();
});
