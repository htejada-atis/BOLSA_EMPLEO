/**
 * 
 */
function enviarFormularioArray(campoDeFormulario, campo, valor) {
	for (var i=0;i<campo.length;i++) {
		//por si no existe opcion
		if (document.getElementById(campo[i]).type.substring(0, 6)=="select") {
			 if (valor[i]!= null && $("#"+campo[i]+" option[value='"+valor[i]+"']").length == 0){
				 $("#"+campo[i]).append($('<option>').html(valor[i]).attr('value', valor[i]));
				 $("#"+campo[i]).prop('disabled', false);
			 }
		} 
		document.getElementById(campo[i]).value=valor[i];
	}
	campoDeFormulario.form.submit();
}
