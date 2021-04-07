package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ApartadoBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ItemBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Merito;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.utilidades.DataTable;

/** Bean para la vista.
 * @author atis
 */
public class VistaMeritos extends Vista implements Serializable {
    private static final long serialVersionUID = 1L;
    private String vista;
    private DataTable<Merito> dataTable;
    private List<ApartadoBaremacion> apartados = new ArrayList<>();
    private List<ItemBaremacion> items = new ArrayList<>();
    private ApartadoBaremacion apartado;
    private ItemBaremacion item;
    private Merito merito;
    
    public String getVista() {
        return vista;
    }
    
    public void setVista(String vista) {
        this.vista = vista;
    }
    
    public DataTable<Merito> getDatatable() {
		return dataTable;
	}
	
	public void setDatatable(DataTable<Merito> dt) {
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
	
}
