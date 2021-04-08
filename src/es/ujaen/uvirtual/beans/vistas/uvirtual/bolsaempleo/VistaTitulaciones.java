package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Titulacion;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.utilidades.DataTable;

/** Bean para la vista.
 * @author jlopez
 */
public class VistaTitulaciones extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private DataTable<Titulacion> dataTable;
	private Titulacion titulacion;
	private String vista;
	
	
	public DataTable<Titulacion> getDatatableTitulaciones() {
		return dataTable;
	}
	
	public void setDatatableTitulaciones(DataTable<Titulacion> dt) {
		this.dataTable = dt;
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
