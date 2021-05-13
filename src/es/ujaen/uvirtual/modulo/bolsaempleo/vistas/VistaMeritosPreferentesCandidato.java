package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.List;

import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BloqueBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferente;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones 2021
 */
public class VistaMeritosPreferentesCandidato extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private String vista;
	private BolsaEmpleoDataTable<MeritoPreferente> dataTable;

	public String getVista() {
		return vista;
	}

	public void setVista(String vista) {
		this.vista = vista;
	}

	public BolsaEmpleoDataTable<MeritoPreferente> getDatatable() {
		return dataTable;
	}

	public void setDatatable(BolsaEmpleoDataTable<MeritoPreferente> dataTable2) {
		this.dataTable = dataTable2;
	}
}
