package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Noticia;
import es.ujaen.uvirtual.beans.vistas.Vista;

/** Bean para la vista.
 * @author jlopez
 */
public class VistaNoticias extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Noticia> noticias = new ArrayList<>();
	private Noticia noticia;
	private String vista;
	
	public List<Noticia> getNoticias() {
		return noticias;
	}
	
	public void setNoticias(List<Noticia> noticias) {
		this.noticias = noticias;
	}
	
	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}
	
	public Noticia getNoticia() {
		return noticia;
	}
	
	public void setNoticia(Noticia noticia) {
		this.noticia = noticia;
	}
	
}
