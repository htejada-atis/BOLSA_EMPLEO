package es.ujaen.uvirtual.beans;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Bean con los datos de un acceso a Universidad Virtual (usuario, url, parámetros, etc.).
 * 20121009 - julopez - creación inicial
 *
 * @author julopez
 *
 */
@SuppressWarnings("java:S1319")
public class Acceso implements Serializable {
	private static final long serialVersionUID = -4100912737763762212L;
	protected String servidor = null;
	protected String url = null;
	protected String ip = null;
	protected String usuario = null;
	protected String sesion = null;
	protected long tiempo = 0;
	protected String comentarios = null;
	protected String parametros = null;
	private ArrayList<Exception> excepciones = null;
	protected boolean registrado = false;
	protected boolean registrar = true;

	public String getServidor() {
		return servidor;
	}

	public void setServidor(String servidor) {
		this.servidor = servidor;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public long getTiempo() {
		return tiempo;
	}

	public void setTiempo(long tiempo) {
		this.tiempo = tiempo;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public String getParametros() {
		return parametros;
	}

	public void setParametros(String parametros) {
		this.parametros = parametros;
	}

	public ArrayList<Exception> getExcepciones() {
		return excepciones;
	}

	/** devuelve excepciones como cadena.
	 * @return excepciones como una cadena de caracteres
	 */
	public String getExcepcionesComoCadena() {
		final StringBuilder resultado = new StringBuilder();
		final String newline = System.getProperty("line.separator");


		for (Exception excepcion : excepciones) {
			resultado.append(newline);
			resultado.append("---------------------------------------------------------------------------------");
			resultado.append(excepcion.toString());

			// añadir cada elemento de la lista de errores
			for (StackTraceElement elemento : excepcion.getStackTrace()) {
				resultado.append(elemento);
				resultado.append(newline);
			}
		}
		return resultado.toString();
		
	}
	
	public void setExcepciones(ArrayList<Exception> excepciones) {
		this.excepciones = excepciones;
	}

	public boolean isRegistrado() {
		return registrado;
	}

	public void setRegistrado(boolean registrado) {
		this.registrado = registrado;
	}

	public String getSesion() {
		return sesion;
	}

	public void setSesion(String sesion) {
		this.sesion = sesion;
	}

	public boolean isRegistrar() {
		return registrar;
	}

	public void setRegistrar(boolean registrar) {
		this.registrar = registrar;
	}
	
}
