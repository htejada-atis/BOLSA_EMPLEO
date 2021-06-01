package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.List;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferente;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteOpcion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones 2021
 */
public class VistaMeritosPreferentesCandidato extends VistaBEP implements Serializable {
	private static final long serialVersionUID = 1L;
	private BolsaEmpleoDataTable<MeritoPreferenteUsuario> dataTable;
	private List<MeritoPreferente> meritosPreferentes;
	private List<MeritoPreferenteOpcion> opcionesMeritoPreferente;
	private String codigoPadreMeritoPreferente;

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

	public void setCodigoPadreMeritoPreferente(String codigo) {
		this.codigoPadreMeritoPreferente = codigo;
	}

	public String getCodigoPadreMeritoPreferente() {
		return this.codigoPadreMeritoPreferente;
	}

	public void setOpcionesMerito(List<MeritoPreferenteOpcion> opciones) {
		this.opcionesMeritoPreferente = opciones;
	}

	private List<MeritoPreferenteOpcion> getOpcionesMerito() {
		return this.opcionesMeritoPreferente;
	}
}
