package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ApartadoBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.BloqueBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ItemBaremacion;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.utilidades.DataTable;

/** Bean para la vista.
 * @author atis
 */
public class VistaItemsBaremacion extends Vista implements Serializable {
    private static final long serialVersionUID = 1L;
    private String vista;
    private DataTable<?> dataTable;
    private ApartadoBaremacion apartado;
    private BloqueBaremacion bloque;
    private ItemBaremacion item;
    
    public String getVista() {
        return vista;
    }
    
    public void setVista(String vista) {
        this.vista = vista;
    }
    
    public DataTable<?> getDatatable() {
		return dataTable;
	}
	
	public void setDatatable(DataTable<?> dt) {
		this.dataTable = dt;
	}
	
	public ApartadoBaremacion getApartadoBaremacion() {
		return this.apartado;
	}
	
	public void setApartadoBaremacion(ApartadoBaremacion papartado) {
		this.apartado = papartado;
	}
	
	public BloqueBaremacion getBloqueBaremacion() {
		return this.bloque;
	}
	
	public void setBloqueBaremacion(BloqueBaremacion pbloque) {
		this.bloque = pbloque;
	}
	
	public ItemBaremacion getItemBaremacion() {
		return this.item;
	}
	
	public void setItemBaremacion(ItemBaremacion pitem) {
		this.item = pitem;
	}
    
}
