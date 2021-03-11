package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.util.List;
import java.io.Serializable;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.beans.vistas.Vista;

/** Bean para la vista.
 * @author atis
 */
public class VistaEstadoBolsas extends Vista implements Serializable {
    private static final long serialVersionUID = 1L;
    private String vista;
    private List<Bolsa> bolsasEmpleo;

    public String getVista() {
        return vista;
    }
    
    public void setVista(String vista) {
        this.vista = vista;
    }
    
    public List<Bolsa> getBolsasEmpleo() {
		return bolsasEmpleo;
	}
	
	public void setBolsasEmpleo(List<Bolsa> bolsas) {
		this.bolsasEmpleo = bolsas;
	}
}
