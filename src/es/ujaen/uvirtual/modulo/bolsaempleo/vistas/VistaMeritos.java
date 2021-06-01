package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones
 */
public class VistaMeritos extends VistaBEP implements Serializable {
	private static final long serialVersionUID = 1L;
	private BolsaEmpleoDataTable<Merito> dataTable;
	private List<ApartadoBaremacion> apartados = new ArrayList<>();
	private List<ItemBaremacion> items = new ArrayList<>();
	private ApartadoBaremacion apartado;
	private ItemBaremacion item;
	private Merito merito;
	private UsuarioBolsaEmpleo usuarioLogeado;

	public BolsaEmpleoDataTable<Merito> getDatatable() {
		return dataTable;
	}

	public void setDatatable(BolsaEmpleoDataTable<Merito> dt) {
		this.dataTable = dt;
	}

	public List<ApartadoBaremacion> getApartados() {
		return apartados;
	}

	public void setApartados(List<ApartadoBaremacion> apartados) {
		this.apartados = apartados;
	}

	public List<ItemBaremacion> getItems() {
		return items;
	}

	public void setItems(List<ItemBaremacion> items) {
		this.items = items;
	}

	public ApartadoBaremacion getApartado() {
		return this.apartado;
	}

	public void setApartado(ApartadoBaremacion apartado) {
		this.apartado = apartado;
	}

	public ItemBaremacion getItem() {
		return this.item;
	}

	public void setItem(ItemBaremacion item) {
		this.item = item;
	}

	public Merito getMerito() {
		return this.merito;
	}

	public void setMerito(Merito merito) {
		this.merito = merito;
	}

	public void setUsuarioLogeado(UsuarioBolsaEmpleo pusuario) {
		this.usuarioLogeado = pusuario;
	}

	public UsuarioBolsaEmpleo getUsuarioLogeado() {
		return this.usuarioLogeado;
	}
}
