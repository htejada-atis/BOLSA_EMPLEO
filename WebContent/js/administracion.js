/*
 *
 * Javascript para el módulo de administración
 * 
 * Versión 1.0.3
 * 
 * @author: julopez
 *
 */
function menuCambiar(campoDeFormulario, nombreAccion) {
	var v = document.getElementById("accionFormulario");
	v.value = nombreAccion;
	bloquearItemsMainContent();
	campoDeFormulario.form.submit();
}

function menuActivarDesactivarRolAcceso(campoDeFormulario, nombreAccion, codigoRol) {
	var v = document.getElementById("accionFormulario");
	v.value = nombreAccion;
	var v1 = document.getElementById("campoRol");
	v1.value = codigoRol;
	bloquearItemsMainContent();
	campoDeFormulario.form.submit();
}

function menuAgregarRolAcceso(campoDeFormulario, nombreAccion) {
	var v = document.getElementById("accionFormulario");
	v.value = nombreAccion;
	var v1 = document.getElementById("campoRol");
	var v2 = document.getElementById("idAgregarRolAcceso");
	v1.value = v2.value;
	bloquearItemsMainContent();
	campoDeFormulario.form.submit();
}

function menuEliminarRolAcceso(campoDeFormulario, nombreAccion, codigoRol) {
	var v = document.getElementById("accionFormulario");
	v.value = nombreAccion;
	var v1 = document.getElementById("campoRol");
	v1.value = codigoRol;
	bloquearItemsMainContent();
	campoDeFormulario.form.submit();	
}

function menuAgregarRolAdministrador(campoDeFormulario, nombreAccion) {
	var v = document.getElementById("accionFormulario");
	v.value = nombreAccion;
	var v1 = document.getElementById("campoRol");
	var v2 = document.getElementById("idAgregarRolAdministrador");
	v1.value = v2.value;
	bloquearItemsMainContent();
	campoDeFormulario.form.submit();
}

function menuEliminarRolAdministrador(campoDeFormulario, nombreAccion, codigoRol) {
	var v = document.getElementById("accionFormulario");
	v.value = nombreAccion;
	var v1 = document.getElementById("campoRol");
	v1.value = codigoRol;
	bloquearItemsMainContent();
	campoDeFormulario.form.submit();	
}

function menuAgregarIdioma(campoDeFormulario, nombreAccion) {
	var v = document.getElementById("accionFormulario");
	v.value = nombreAccion;
	var v1  = document.getElementById("campoIdioma");
	var v2  = document.getElementById("textoIdioma");
	var v1v = document.getElementById("idIdioma_select");
	var v2v = document.getElementById("idIdioma_texto");
	v1.value = v1v.value;
	v2.value = v2v.value;
	bloquearItemsMainContent();
	campoDeFormulario.form.submit();		
}

function menuEliminarIdioma(campoDeFormulario, nombreAccion, codigoIdioma) {
	var v = document.getElementById("accionFormulario");
	v.value = nombreAccion;
	var v1  = document.getElementById("campoIdioma");
	v1.value = codigoIdioma;	
	bloquearItemsMainContent();
	campoDeFormulario.form.submit();		
}

function menuCambiarIdioma(campoDeFormulario, nombreAccion, codigoIdioma) {
	var v = document.getElementById("accionFormulario");
	v.value = nombreAccion;
	var v1  = document.getElementById("campoIdioma");
	var v2  = document.getElementById("textoIdioma");
	var v2v = document.getElementById("idIdioma_"+codigoIdioma);
	v1.value = codigoIdioma;
	v2.value = v2v.value;
	bloquearItemsMainContent();
	campoDeFormulario.form.submit();		
}

function menuAgregarSubred(campoDeFormulario, nombreAccion) {
	var v = document.getElementById("accionFormulario");
	v.value = nombreAccion;
	var v1  = document.getElementById("campoRed");
	var v2  = document.getElementById("textoRed");
	var v1v = document.getElementById("idSubred_red");
	var v2v = document.getElementById("idSubred_desc");
	v1.value = v1v.value;
	v2.value = v2v.value;
	bloquearItemsMainContent();
	campoDeFormulario.form.submit();	
}

function menuEliminarSubred(campoDeFormulario, nombreAccion, codigoRed) {
	var v = document.getElementById("accionFormulario");
	v.value = nombreAccion;
	var v1  = document.getElementById("campoRed");
	v1.value = codigoRed;	
	bloquearItemsMainContent();
	campoDeFormulario.form.submit();		
}

function menuCambiarSubred(campoDeFormulario, nombreAccion, codigoRed) {
	var v = document.getElementById("accionFormulario");
	v.value = nombreAccion;
	var v1  = document.getElementById("campoRed");
	v1.value = codigoRed;
	bloquearItemsMainContent();
	campoDeFormulario.form.submit();		
}


function rolAgregar(campoDeFormulario, nombreAccion) {
	var v = document.getElementById("accionFormulario");
	v.value = nombreAccion;
	var v1 = document.getElementById("campoRol");
	var v2 = document.getElementById("campoDesc");
	var v1v = document.getElementById("idAgregarRol");
	var v2v = document.getElementById("idAgregarDesc");
	v1.value = v1v.value;
	v2.value = v2v.value;
	bloquearItemsMainContent();
	campoDeFormulario.form.submit();
}

function rolEliminar(campoDeFormulario, nombreAccion, codigoRol) {
	var v = document.getElementById("accionFormulario");
	v.value = nombreAccion;
	var v1 = document.getElementById("campoRol");
	v1.value = codigoRol;
	bloquearItemsMainContent();
	campoDeFormulario.form.submit();	
}


function idiomaAgregar(campoDeFormulario, nombreAccion) {
	var v = document.getElementById("accionFormulario");
	v.value = nombreAccion;
	var v1 = document.getElementById("campoIdioma");
	var v2 = document.getElementById("campoDesc");
	var v1v = document.getElementById("idAgregarIdioma");
	var v2v = document.getElementById("idAgregarDesc");
	v1.value = v1v.value;
	v2.value = v2v.value;
	bloquearItemsMainContent();
	campoDeFormulario.form.submit();
}

function idiomaEliminar(campoDeFormulario, nombreAccion, codigoIdioma) {
	var v = document.getElementById("accionFormulario");
	v.value = nombreAccion;
	var v1 = document.getElementById("campoIdioma");
	v1.value = codigoIdioma;
	bloquearItemsMainContent();
	campoDeFormulario.form.submit();	
}

function memcacheCambiar(campoDeFormulario, nombreAccion) {
	var v = document.getElementById("accionFormulario");
	v.value = nombreAccion;
	bloquearItemsMainContent();
	campoDeFormulario.form.submit();	
}

function memcacheAccionClave(campoClave, clave, accion) {
	var a = document.getElementById("accionFormulario");
	var v = document.getElementById("valorFormulario");
	v.value = clave;
	a.value = accion;
	bloquearItemsMainContent();
	a.form.submit();	
}
function cambiarAPaginaClave(boton, pagina) {
	var v = document.getElementById("pagina");
	v.value = pagina;
	bloquearItemsMainContent();
	boton.form.submit();	
}
function logCambiarNivel(campoDeFormulario, nombreAccion, clave) {
	var v = document.getElementById("accionFormulario");
	v.value = nombreAccion;
	var v1 = document.getElementById("campoLogger");
	v1.value = clave;
	var v2 = document.getElementById("campoNivel");
	var v2v = document.getElementById("nivel." + clave );
	v2.value = v2v.value;
	bloquearItemsMainContent();
	campoDeFormulario.form.submit();	
}
