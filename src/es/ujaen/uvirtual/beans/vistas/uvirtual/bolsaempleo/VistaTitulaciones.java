package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Titulacion;
import es.ujaen.uvirtual.beans.vistas.Vista;

/** Bean para la vista.
 * @author jlopez
 */
public class VistaTitulaciones extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Titulacion> titulaciones = new ArrayList<>();
	private Titulacion titulacion;
	private String vista;
	
	
	public List<Titulacion> getTitulaciones() {
		return titulaciones;
	}
	
	public void setTitulaciones(List<Titulacion> titulaciones) {
		this.titulaciones = titulaciones;
	}
	
	public Titulacion getTitulacion() {
		return titulacion;
	}
	
	public void setTitulacion(Titulacion titulacion) {
		this.titulacion = titulacion;
	}
	
	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}
	
}
