package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones 2021
 */
public class VistaConvocatorias extends VistaBEP implements Serializable {
	private static final long serialVersionUID = 1L;
	private BolsaEmpleoDataTable<Convocatoria> dataTable;
	private Convocatoria convocatoria;

	public BolsaEmpleoDataTable<Convocatoria> getDatatableConvocatorias() {
		return dataTable;
	}

	public void setDatatableConvocatorias(BolsaEmpleoDataTable<Convocatoria> dt) {
		this.dataTable = dt;
	}

	public Convocatoria getConvocatoria() {
		return convocatoria;
	}

	public void setConvocatoria(Convocatoria pconvocatoria) {
		this.convocatoria = pconvocatoria;
	}
}
