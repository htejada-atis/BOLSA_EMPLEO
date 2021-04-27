package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;

/** Bean para la vista.
 * @author atis
 */
public class VistaEstadoBolsas extends Vista implements Serializable {
    private static final long serialVersionUID = 1L;
    private String vista;
    private BolsaEmpleoDataTable<Bolsa> dataTable;
    private Integer totalBolsas; 
    private Integer totalBolsasBloqueadas; 
    private Integer totalBolsasRevisadas; 
    
    public String getVista() {
        return vista;
    }
    
    public void setVista(String vista) {
        this.vista = vista;
    }
    
    public Integer getTotalBolsas() {
        return totalBolsas;
    }
    
    public void setTotalBolsas(Integer num) {
        this.totalBolsas = num;
    }
    
    public Integer getTotalBolsasBloqueadas() {
        return totalBolsasBloqueadas;
    }
    
    public void setTotalBolsasBloqueadas(Integer num) {
        this.totalBolsasBloqueadas = num;
    }
    
    public Integer getTotalBolsasRevisadas() {
        return totalBolsasRevisadas;
    }
    
    public void setTotalBolsasRevisadas(Integer num) {
        this.totalBolsasRevisadas = num;
    }
    
    public BolsaEmpleoDataTable<Bolsa> getDatatableBolsas() {
    	return dataTable;
    }
    
    public void setDatatableBolsas(BolsaEmpleoDataTable<Bolsa> dt) {
    	this.dataTable = dt;    	
    }
}
