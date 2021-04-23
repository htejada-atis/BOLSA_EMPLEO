package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area;
import es.ujaen.uvirtual.beans.vistas.Vista;

/** Bean para la vista.
 * @author fcampos
 */
public class VistaValidarNoAfines extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private String vista;
	private List<Area> areas = new ArrayList<>();
	
	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}
	
	public List<Area> getAreas() {
		return areas;
	}
	
	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}
}
