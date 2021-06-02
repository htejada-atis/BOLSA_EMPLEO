package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Fichero;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Noticia;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones
 */
public class VistaInicio extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Noticia> noticias = new ArrayList<>();
	private List<Fichero> ficheros = new ArrayList<>();
	private Fichero fichero;
	private boolean anonimo;
	private String vista;
	private UsuarioBolsaEmpleo usuarioLogeado;
	
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

	public List<Noticia> getNoticias() {
		return noticias;
	}

	public void setNoticias(List<Noticia> noticias) {
		this.noticias = noticias;
	}

	public List<Fichero> getFicheros() {
		return ficheros;
	}

	public void setFicheros(List<Fichero> ficheros) {
		this.ficheros = ficheros;
	}

	public Fichero getFichero() {
		return fichero;
	}

	public void setFichero(Fichero fich) {
		this.fichero = fich;
	}

	public boolean getAnonimo() {
		return anonimo;
	}

	public void setAnonimo(boolean anonimo) {
		this.anonimo = anonimo;
	}
}
