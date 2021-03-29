package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Titulacion;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.utilidades.DataTable;

/** Bean para la vista.
 * @author jlopez
 */
public class VistaTitulacionesArea extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Area> areas = new ArrayList<>();
	private DataTable<Titulacion> dataTable;
	private Area area;
	private String vista;
	
	public List<Area> getAreas() {
		return areas;
	}
	
	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}
	
	public DataTable<Titulacion> getDatatableTitulaciones() {
		return dataTable;
	}
	
	public void setDatatableTitulaciones(DataTable<Titulacion> dataTable) {
		this.dataTable = dataTable;
	}
	
	public Area getArea() {
		return area;
	}
	
	public void setArea(Area area) {
		this.area = area;
	}
	
	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}
	
}
