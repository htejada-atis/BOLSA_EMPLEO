package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.List;

import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BloqueBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferente;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones 2021
 */
public class VistaMeritosPreferentesCandidato extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private String vista;
	private BolsaEmpleoDataTable<MeritoPreferenteUsuario> dataTable;
	private List<MeritoPreferente> meritosPreferentes;

	public String getVista() {
		return vista;
	}

	public void setVista(String vista) {
		this.vista = vista;
	}

	public BolsaEmpleoDataTable<MeritoPreferenteUsuario> getDatatable() {
		return dataTable;
	}

	public void setDatatable(BolsaEmpleoDataTable<MeritoPreferenteUsuario> dataTable2) {
		this.dataTable = dataTable2;
	}
	
	public void setMeritosPreferente(List<MeritoPreferente> pmeritosPreferentes) {
		this.meritosPreferentes = pmeritosPreferentes;		
	}
	
	public List<MeritoPreferente> getMeritosPreferente() {
		return this.meritosPreferentes;
	}
}
