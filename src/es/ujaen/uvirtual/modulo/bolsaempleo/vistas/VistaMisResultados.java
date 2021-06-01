package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones
 */
public class VistaMisResultados extends VistaBEP implements Serializable {
	private static final long serialVersionUID = 1L;
	private BolsaEmpleoDataTable<Area> dataTableAreas;
	private Area area;

	public BolsaEmpleoDataTable<Area> getDatatableAreas() {
		return dataTableAreas;
	}

	public void setDatatableAreas(BolsaEmpleoDataTable<Area> dt) {
		this.dataTableAreas = dt;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
}
