package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.List;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones
 */
public class VistaAreasBaremar extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private transient BolsaEmpleoDataTable<Bolsa> dataTableAreas;
	private List<Bolsa> listaBolsas;
	private String vista;
	private UsuarioBolsaEmpleo usuarioLogeado;

	public BolsaEmpleoDataTable<Bolsa> getDatatableAreas() {
		return dataTableAreas;
	}

	public void setDatatableAreas(BolsaEmpleoDataTable<Bolsa> dt) {
		this.dataTableAreas = dt;
	}

	public List<Bolsa> getListaBolsas() {
		return listaBolsas;
	}

	public void setListaBolsas(List<Bolsa> bolsas) {
		this.listaBolsas = bolsas;
	}
	
	public String getVista() {
		return vista;
	}

	public void setVista(String vista) {
		this.vista = vista;
	}
	
	public UsuarioBolsaEmpleo getUsuarioLogeado() {
		return this.usuarioLogeado;
	}
	
	public void setUsuarioLogeado(UsuarioBolsaEmpleo usuario) {
		this.usuarioLogeado = usuario;
	}
}
