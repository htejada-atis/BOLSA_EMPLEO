package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Area;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.TitulacionArea;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;

/** Bean para la vista.
 * @author jlopez
 */
public class VistaCandidatoTitulacionesArea extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private BolsaEmpleoDataTable<Bolsa> dataTableAreas;
	private BolsaEmpleoDataTable<TitulacionArea> dataTableTitulaciones;
	private Area area;
	private String vista;
	
	
	public BolsaEmpleoDataTable<Bolsa> getDatatableAreas() {
		return dataTableAreas;
	}
	
	public void setDatatableAreas(BolsaEmpleoDataTable<Bolsa> dt) {
		this.dataTableAreas = dt;
	}
	
	public BolsaEmpleoDataTable<TitulacionArea> getDatatableTitulaciones() {
		return dataTableTitulaciones;
	}
	
	public void setDatatableTitulaciones(BolsaEmpleoDataTable<TitulacionArea> dt) {
		this.dataTableTitulaciones = dt;
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
