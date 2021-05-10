package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BloqueBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.beans.vistas.Vista;

/** Bean para la vista.
 * @author atis
 */
public class VistaItemsBaremacion extends Vista implements Serializable {
    private static final long serialVersionUID = 1L;
    private String vista;
    private BolsaEmpleoDataTable<?> dataTable;
    private ApartadoBaremacion apartado;
    private BloqueBaremacion bloque;
    private ItemBaremacion item;
    private String ultimoCodigo; 
    private List<String> afinidades = new ArrayList<>();
	private List<ItemBaremacion> listaItemsExcluyentes;
        
    public String getVista() {
        return vista;
    }
    
    public void setVista(String vista) {
        this.vista = vista;
    }
    
    public BolsaEmpleoDataTable<?> getDatatable() {
		return dataTable;
	}
	
	public void setDatatable(BolsaEmpleoDataTable<?> dt) {
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
	
	public String getUltimoCodigo() {
		return this.ultimoCodigo;
	}
	
	public void setUltimoCodigo(String codigo) {
		this.ultimoCodigo = codigo;
	}    
		
	public List<String> getAfinidades() {
		return afinidades;
	}
	
	public void setAfinidades(List<String> pafinidades) {
		this.afinidades = pafinidades;
	}
	
	public List<ItemBaremacion> getListaItemsExcluyentes() {
		return listaItemsExcluyentes;
	}
	
	public void setListaItemsExcluyentes(List<ItemBaremacion> listaItemsExcluyentes) {
		this.listaItemsExcluyentes = listaItemsExcluyentes;
	}
}
