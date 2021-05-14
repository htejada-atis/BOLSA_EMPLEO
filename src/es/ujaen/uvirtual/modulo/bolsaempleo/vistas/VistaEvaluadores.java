package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Evaluador;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/** Bean para la vista.
 * @author jlopez
 */
public class VistaEvaluadores extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Area> areas = new ArrayList<>();
	private BolsaEmpleoDataTable<Evaluador> dataTable;
	private Area area;
	private Evaluador evaluador;
	private String vista;
	
	public List<Area> getAreas() {
		return areas;
	}
	
	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}
	
	public BolsaEmpleoDataTable<Evaluador> getDatatableUsuarios() {
		return dataTable;
	}
	
	public void setDatatableUsuarios(BolsaEmpleoDataTable<Evaluador> dt) {
		this.dataTable = dt;
	}
	
	public Area getArea() {
		return area;
	}
	
	public void setArea(Area area) {
		this.area = area;
	}
	
	public Evaluador getEvaluador() {
		return evaluador;
	}
	
	public void setEvaluador(Evaluador evaluador) {
		this.evaluador = evaluador;
	}
	
	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}
	
}
