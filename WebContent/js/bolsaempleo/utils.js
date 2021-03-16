function formatearFecha(fecha) {
	var sin_hora = fecha.split(" ")[0];
	var sin_guiones = sin_hora.split("-");
	return sin_guiones[2] + "/" + sin_guiones[1] + "/" +sin_guiones[0];
}