package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Fichero;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Noticia;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones
 */
public class VistaInicio extends VistaBEP implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Noticia> noticias = new ArrayList<>();
	private List<Fichero> ficheros = new ArrayList<>();
	private Fichero fichero;
	private boolean anonimo;

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
