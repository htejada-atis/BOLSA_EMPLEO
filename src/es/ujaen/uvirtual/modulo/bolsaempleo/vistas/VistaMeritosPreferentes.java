package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.List;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BloqueBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferente;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteOpcion;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones 2021
 */
public class VistaMeritosPreferentes extends VistaBEP implements Serializable {
	private static final long serialVersionUID = 1L;
	private BolsaEmpleoDataTable<MeritoPreferente> dataTable;
	private BolsaEmpleoDataTable<MeritoPreferenteOpcion> dataTableOpciones;
	private MeritoPreferente merito;
	private List<ItemBaremacion> itemsBaremacion;
	private List<ApartadoBaremacion> apartadoBaremacion;
	private List<BloqueBaremacion> bloqueBaremacion;

	public MeritoPreferente getMeritoPreferente() {
		return merito;
	}

	public void setMeritoPreferente(MeritoPreferente pmerito) {
		this.merito = pmerito;
	}

	public List<ItemBaremacion> getItemsBaremacion() {
		return this.itemsBaremacion;
	}

	public void setItemsBaremacion(List<ItemBaremacion> itemsBaremacion) {
		this.itemsBaremacion = itemsBaremacion;
	}

	public List<ApartadoBaremacion> getApartadoBaremacion() {
		return this.apartadoBaremacion;
	}

	public void setApartadoBaremacion(List<ApartadoBaremacion> apartadoBaremacion) {
		this.apartadoBaremacion = apartadoBaremacion;
	}

	public List<BloqueBaremacion> getBloqueBaremacion() {
		return this.bloqueBaremacion;
	}

	public void setBloqueBaremacion(List<BloqueBaremacion> bloqueBaremacion) {
		this.bloqueBaremacion = bloqueBaremacion;
	}

	public BolsaEmpleoDataTable<MeritoPreferente> getDatatable() {
		return dataTable;
	}

	public void setDatatable(BolsaEmpleoDataTable<MeritoPreferente> dt) {
		this.dataTable = dt;
	}

	public BolsaEmpleoDataTable<MeritoPreferenteOpcion> getDatatableOpciones() {
		return dataTableOpciones;
	}

	public void setDatatableOpciones(BolsaEmpleoDataTable<MeritoPreferenteOpcion> dt) {
		this.dataTableOpciones = dt;
	}
}
