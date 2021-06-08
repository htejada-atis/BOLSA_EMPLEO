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
	private String descripcionModulo;
	private String paginaRedireccion;
	private String captchaPublica;
	private boolean mostrarCaptcha;
	private String idModulo;
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

	public String getPaginaRedireccion() {
		return paginaRedireccion;
	}

	public void setPaginaRedireccion(String paginaRedireccion) {
		this.paginaRedireccion = paginaRedireccion;
	}

	public String getIdModulo() {
		return idModulo;
	}

	public void setIdModulo(String idModulo) {
		this.idModulo = idModulo;
	}

	public String getDescripcionModulo() {
		return descripcionModulo;
	}

	public void setDescripcionModulo(String descripcionModulo) {
		this.descripcionModulo = descripcionModulo;
	}

	public String getCaptchaPublica() {
		return captchaPublica;
	}

	public void setCaptchaPublica(String captchaPublica) {
		this.captchaPublica = captchaPublica;
	}

	public boolean isMostrarCaptcha() {
		return mostrarCaptcha;
	}

	public void setMostrarCaptcha(boolean mostrarCaptcha) {
		this.mostrarCaptcha = mostrarCaptcha;
	}
}
