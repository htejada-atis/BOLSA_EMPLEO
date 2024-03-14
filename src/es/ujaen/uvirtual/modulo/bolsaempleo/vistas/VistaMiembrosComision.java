package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.EvaluadorCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones
 */
public class VistaMiembrosComision extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Area> areas = new ArrayList<>();
	private transient BolsaEmpleoDataTable<EvaluadorCandidato> dataTableEvaluadores;
	private String vista;
	private UsuarioBolsaEmpleo usuarioLogeado;
	private Area area;
	
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
	
	public Area getArea() {
		return area;
	}
	
	public void setArea(Area area) {
		this.area = area;
	}
	
	public BolsaEmpleoDataTable<EvaluadorCandidato> getDatatableEvaluadores() {
		return dataTableEvaluadores;
	}
	
	public void setDatatableEvaluadores(BolsaEmpleoDataTable<EvaluadorCandidato> dt) {
		this.dataTableEvaluadores = dt;
	}
}
