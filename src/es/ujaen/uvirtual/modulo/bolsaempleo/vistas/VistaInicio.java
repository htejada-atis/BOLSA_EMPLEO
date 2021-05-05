package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Fichero;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Noticia;

/** Bean para la vista.
 * @author jlopez
 */
public class VistaInicio extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Noticia> noticias = new ArrayList<>();
	private List<Fichero> ficheros = new ArrayList<>();
	private Fichero fichero;
	private String vista;
	
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
	
	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}
	
}
