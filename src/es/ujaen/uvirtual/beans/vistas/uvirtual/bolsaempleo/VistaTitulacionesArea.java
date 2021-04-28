package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Titulacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.TitulacionArea;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;

/** Bean para la vista.
 * @author jlopez
 */
public class VistaTitulacionesArea extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Area> areas = new ArrayList<>();
	private BolsaEmpleoDataTable<Titulacion> dataTableTitulaciones;
	private BolsaEmpleoDataTable<TitulacionArea> dataTableTitulacionesArea;
	private Area area;
	private String vista;
	
	public List<Area> getAreas() {
		return areas;
	}
	
	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}
	
	public BolsaEmpleoDataTable<Titulacion> getDatatableTitulaciones() {
		return dataTableTitulaciones;
	}
	
	public void setDatatableTitulaciones(BolsaEmpleoDataTable<Titulacion> dt) {
		this.dataTableTitulaciones = dt;
	}
	
	public BolsaEmpleoDataTable<TitulacionArea> getDatatableTitulacionesArea() {
		return dataTableTitulacionesArea;
	}
	
	public void setDatatableTitulacionesArea(BolsaEmpleoDataTable<TitulacionArea> dt) {
		this.dataTableTitulacionesArea = dt;
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
