function isFunction(functionToCheck) {
 return functionToCheck && {}.toString.call(functionToCheck) === '[object Function]';
}
  
function sendForm(url, params) {
	var form = document.createElement("form");
  	form.method = "POST";
	form.action = url;
	document.body.appendChild(form);
	
	for(key in params) {
		var element = document.createElement("input");
		element.type = "hidden";
		element.name = key;
		element.value = params[key];
		form.appendChild(element);
	}
  	
	form.submit();
}
  