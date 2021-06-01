package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones
 */
public class VistaMiembrosComision extends VistaBEP implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Area> areas = new ArrayList<>();

	public List<Area> getAreas() {
		return areas;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}
}
