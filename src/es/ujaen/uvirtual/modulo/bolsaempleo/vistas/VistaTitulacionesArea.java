package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionArea;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones 2021
 */
public class VistaTitulacionesArea extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Area> areas = new ArrayList<>();
	private transient BolsaEmpleoDataTable<Titulacion> dataTableTitulaciones;
	private transient BolsaEmpleoDataTable<TitulacionArea> dataTableTitulacionesArea;
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

	public List<Area> getAreas() {
		return areas;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}

	public BolsaEmpleoDataTable<Titulacion> getDatatableTitulaciones() {
		return dataTableTitulaciones;
	}

	public void setDatatableTitulaciones(BolsaEmpleoDataTable<Titulacion> dt) {
		this.dataTableTitulaciones = dt;
	}

	public BolsaEmpleoDataTable<TitulacionArea> getDatatableTitulacionesArea() {
		return dataTableTitulacionesArea;
	}

	public void setDatatableTitulacionesArea(BolsaEmpleoDataTable<TitulacionArea> dt) {
		this.dataTableTitulacionesArea = dt;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
}
