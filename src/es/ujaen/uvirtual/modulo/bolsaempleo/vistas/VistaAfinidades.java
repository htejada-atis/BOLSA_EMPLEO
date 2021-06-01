package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Afinidad;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones
 */
public class VistaAfinidades extends VistaBEP implements Serializable {
	private static final long serialVersionUID = 1L;
	private BolsaEmpleoDataTable<Afinidad> dataTable;
	private Afinidad afinidad;

	public BolsaEmpleoDataTable<Afinidad> getDatatableAfinidades() {
		return dataTable;
	}

	public void setDatatableAfinidades(BolsaEmpleoDataTable<Afinidad> dt) {
		this.dataTable = dt;
	}

	public Afinidad getAfinidad() {
		return afinidad;
	}

	public void setAfinidad(Afinidad afinidad) {
		this.afinidad = afinidad;
	}
}
