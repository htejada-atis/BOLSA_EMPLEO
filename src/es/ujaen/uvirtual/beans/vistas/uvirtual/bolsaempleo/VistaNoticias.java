package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Noticia;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.utilidades.DataTable;

/** Bean para la vista.
 * @author jlopez
 */
public class VistaNoticias extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private DataTable<Noticia> dataTable;
	private Noticia noticia;
	private String vista;
	
	public DataTable<Noticia> getDatatableNoticias() {
		return dataTable;
	}
	
	public void setDatatableNoticias(DataTable<Noticia> dt) {
		this.dataTable = dt;
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
