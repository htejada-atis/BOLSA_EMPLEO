package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.AreaEvaluadoresTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Departamento;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Evaluador;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/** Bean para la vista.
 * @author ATISoluciones
 */
public class VistaEvaluadores extends VistaBEP implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Departamento> departamentos = new ArrayList<>();
	private BolsaEmpleoDataTable<AreaEvaluadoresTable> dataTableAreas;
	private BolsaEmpleoDataTable<Evaluador> dataTableEvaluadores;
	private Area area;
	private Departamento departamento;
	private Evaluador evaluador;
	
	public List<Departamento> getDepartamentos() {
		return departamentos;
	}
	
	public void setDepartamentos(List<Departamento> departamentos) {
		this.departamentos = departamentos;
	}
	
	public BolsaEmpleoDataTable<AreaEvaluadoresTable> getDatatableAreas() {
		return dataTableAreas;
	}
	
	public void setDatatableAreas(BolsaEmpleoDataTable<AreaEvaluadoresTable> dt) {
		this.dataTableAreas = dt;
	}
	
	public BolsaEmpleoDataTable<Evaluador> getDatatableEvaluadores() {
		return dataTableEvaluadores;
	}
	
	public void setDatatableEvaluadores(BolsaEmpleoDataTable<Evaluador> dt) {
		this.dataTableEvaluadores = dt;
	}
	
	public Area getArea() {
		return area;
	}
	
	public void setArea(Area area) {
		this.area = area;
	}
	
	public Departamento getDepartamento() {
		return departamento;
	}
	
	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}
	
	public Evaluador getEvaluador() {
		return evaluador;
	}
	
	public void setEvaluador(Evaluador evaluador) {
		this.evaluador = evaluador;
	}
}
