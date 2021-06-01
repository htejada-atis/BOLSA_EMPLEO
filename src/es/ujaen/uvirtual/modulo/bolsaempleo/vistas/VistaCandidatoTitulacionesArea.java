package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionArea;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones 2021
 */
public class VistaCandidatoTitulacionesArea extends VistaBEP implements Serializable {
	private static final long serialVersionUID = 1L;
	private BolsaEmpleoDataTable<Bolsa> dataTableAreas;
	private BolsaEmpleoDataTable<TitulacionArea> dataTableTitulaciones;
	private Bolsa area;

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

	public Bolsa getArea() {
		return area;
	}

	public void setArea(Bolsa area) {
		this.area = area;
	}
}
