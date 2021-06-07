package es.ujaen.uvirtual.modulo.bolsaempleo.controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.vistas.VistaError;

/**
 * Clase controlador para mostrar errores .
 */
@WebServlet(
	name = "informacionadministrativa.bolsaempleo.descargaficheros",
	displayName = "bolsaempleo.descargaficheros",
	description = "Controlador para la descarga de ficheros de bolsa empleo", 
	urlPatterns = { 
			"/srv/es/informacionadministrativa/bolsaempleo/descargaficheros", 
			"/srv/en/informacionadministrativa/bolsaempleo/descargaficheros",
			"/pub/es/ajax/informacionadministrativa/bolsaempleo/descargaficheros",
			"/pub/en/ajax/informacionadministrativa/bolsaempleo/descargaficheros"
	})
public class ControladorDescargaFicherosBolsaEmpleo extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	/** do get.
	 * @param request  peticion
	 * @param response respuesta
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");

		VistaError bean = new VistaError();
		try {
			init(bean, request);
		} catch (Exception ex) {	
			
		}

		datos.getVistas().put(bean.getClass().getName(), bean);
		datos.getFicherosJSP().add(bean.getVista());
		datos.getFicherosJS().add("/js/jquery-1.latest.min.js");
		datos.getFicherosJS().add("/js/jquery-ui-1.10.4.min.js");
		datos.getFicherosJS().add("/js/bolsaempleo/utils.js");
		datos.getFicherosCSS().add("/css/intranet.css");
		datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
		datos.getFicherosCSS().add("/css/ujaen_bolsa_empleo.css");
	}

	/** do post.
	 * @param request  peticion
	 * @param response respuesta
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private void init(VistaError bean, HttpServletRequest request) {
		BolsaEmpleoUtils.readMensajeSession(bean, request);
	}
}
