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
			
			var error = $.parseJSON(response.responseText);
	    	return getProp(error, 'error', 'Sin definir');
		} else {
			console.log(response);
			return getProp(response, 'status', '999') + ": " + getProp(response, 'statusText', 'Sin definir');
		}			
	} catch (err) {	
		console.error("Error procesando error", response, err);
	}
}

window.Atis = {
	"formatearFecha": formatearFecha,
	"getErrorResponse": getErrorResponse,
	"getProp": getProp,
	"setProp": setProp
};