package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;

import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones
 */
public class VistaMisResultados extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private transient BolsaEmpleoDataTable<Area> dataTableAreas;
	private Area area;
	private String vista;
	private UsuarioBolsaEmpleo usuarioLogeado;
	
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

	public BolsaEmpleoDataTable<Area> getDatatableAreas() {
		return dataTableAreas;
	}

	public void setDatatableAreas(BolsaEmpleoDataTable<Area> dt) {
		this.dataTableAreas = dt;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
}
