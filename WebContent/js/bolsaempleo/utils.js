/**
 * Datatable
 * @copyright ATISoluciones 2021
 */

function getProp( object, keys, defaultVal ){
	keys = Array.isArray( keys )? keys : keys.split('.');
	object = object[keys[0]];
	if( object && keys.length > 1 ){
		return getProp( object, keys.slice(1) );
	}
	return object === undefined? defaultVal : object;
}

function setProp( object, keys, val ){
	keys = Array.isArray( keys )? keys : keys.split('.');
	if( keys.length > 1 ){
		object[keys[0]] = object[keys[0]] || {};
		return setProp( object[keys[0]], keys.slice(1), val );
	}
	object[keys[0]] = val;
}

function removeValueArray(array, val) {
	array = Array.isArray(array) ? array : [array];
	console.log(array);
	const index = array.indexOf(val);
	if (index > -1) {
		array.splice(index, 1);
	}
	
	console.log(array);
}

function formatearFecha(fecha) {
	var sin_hora = fecha.split(" ")[0];
	var sin_guiones = sin_hora.split("-");
	return sin_guiones[2] + "/" + sin_guiones[1] + "/" +sin_guiones[0];
}

function getErrorResponse(response) {
	try {
		var contentType = response.getResponseHeader('content-type').split(';')[0];		
		if (contentType == 'application/json') {					
			if (!response.responseText) {
				return getProp(response, 'status', '999') + ": respuesta vacia";
			}
			
			var error = Atis.json2Object(response.responseText);
			return getProp(error, 'descripcion', 'Sin definir');
		} else {
			return getProp(response, 'status', '999') + ": " + getProp(response, 'statusText', 'Sin definir');
		}     
	} catch (err) { 
		console.error("Error procesando error", response, err);
	}
}

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

function sendAjax(url, params, success, error) {
	$.ajax({
		type: 'GET',
		url: url,
		contentType: "application/json",
		dataType: "json",
		data: params,
		success: success,
		error: error
	});
}

function alertDialog(title, message) {
	$('<div class="atisDialog"></div>')
		.appendTo('body')
		.html('<div><h6>' + message + '</h6></div>')
		.dialog({
			modal: true,
			title: title,
			zIndex: 10000,
			autoOpen: true,
			width: 'auto',
			resizable: false,
			buttons: {
				Ok: function() {
					$(this).dialog("close");
				}
			},
			close: function(event, ui) {
				$(this).remove();
			}
	});
}

function confirmDialog(title, message, buttons) {
	$('<div></div>').appendTo('body')
	    	.html('<div><h6>' + message + '</h6></div>')
	    	.dialog({
		      modal: true,
		      title: title,
		      zIndex: 10000,
		      autoOpen: true,
		      width: 'auto',
		      resizable: false,
		      buttons: buttons,
		      close: function(event, ui) {
		        $(this).remove();
		      }
		});
}

function object2Json(object) { 
	return JSON.stringify(object);
}

function json2Object(json) {
	return JSON.parse(json);
}

window.Atis = $.extend(window.Atis ? window.Atis : {}, {
	"getProp": getProp,
	"setProp": setProp,
	"alertDialog": alertDialog,
	"confirmDialog": confirmDialog,
	"getErrorResponse": getErrorResponse,
	"isFunction": isFunction,
	"formatearFecha": formatearFecha,
	"sendForm": sendForm,
	"object2Json": object2Json,
	"json2Object": json2Object,
	"sendAjax": sendAjax,
	"removeValueArray": removeValueArray
});