package es.ujaen.uvirtual.modulo.autoregistrado.beans.vista;

import java.io.Serializable;

import es.ujaen.uvirtual.beans.vistas.Vista;

/** vista de usuario externo.
 *
 */
public class VistaUsuarioAutoregistrado extends Vista implements Serializable {

	private static final long serialVersionUID = 1L;
	private String vista;
	private String correo;
	private String clave;
	private String codigoTemporal;
	private String idSolicitud;
	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getCodigoTemporal() {
		return codigoTemporal;
	}

	public void setCodigoTemporal(String codigoTemporal) {
		this.codigoTemporal = codigoTemporal;
	}

	public String getIdSolicitud() {
		return idSolicitud;
	}

	public void setIdSolicitud(String idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
}
