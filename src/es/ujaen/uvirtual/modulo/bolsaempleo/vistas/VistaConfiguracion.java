package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones.
 */
public class VistaConfiguracion extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private String version;
	private String vista;
	private UsuarioBolsaEmpleo usuarioLogeado;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getVista() {
		return vista;
	}

	public void setVista(String vista) {
		this.vista = vista;
	}
	
	public UsuarioBolsaEmpleo getUsuarioLogeado() {
		return this.usuarioLogeado;
	}
	
	public void setUsuarioLogeado(UsuarioBolsaEmpleo usuario) {
		this.usuarioLogeado = usuario;
	}
}
