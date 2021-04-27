package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;
import java.util.List;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Solicitud;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.BolsaCandidato;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Merito;

/** Bean para la vista.
 * @author ATISoluciones
 */
public class VistaSolicitudes extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private String vista;
	private BolsaEmpleoDataTable<Solicitud> dataTable;
	private BolsaEmpleoDataTable<Bolsa> dataTableAreas;
	private BolsaEmpleoDataTable<BolsaCandidato> dataTableBolsasCandidato;
	private BolsaEmpleoDataTable<Merito> dataTableMeritos;
	private List<Bolsa> listaBolsas;
	private Solicitud solicitud;
	private Bolsa area;
			
	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}
	
	public BolsaEmpleoDataTable<Solicitud> getDatatableSolicitudes() {
		return dataTable;
	}
	
	public void setDatatableSolicitudes(BolsaEmpleoDataTable<Solicitud> dt) {
		this.dataTable = dt;
	}
	
	public BolsaEmpleoDataTable<Bolsa> getDatatableAreas() {
		return dataTableAreas;
	}
	
	public void setDatatableAreas(BolsaEmpleoDataTable<Bolsa> dt) {
		this.dataTableAreas = dt;
	}
	
	public BolsaEmpleoDataTable<BolsaCandidato> getDataTableBolsasCandidato() {
		return dataTableBolsasCandidato;
	}
	
	public void setDataTableBolsasCandidato(BolsaEmpleoDataTable<BolsaCandidato> dt) {
		this.dataTableBolsasCandidato = dt;
	}
	
	public BolsaEmpleoDataTable<Merito> getDataTableMeritos() {
		return dataTableMeritos;
	}
	
	public void setDataTableMeritos(BolsaEmpleoDataTable<Merito> dt) {
		this.dataTableMeritos = dt;
	}
	
	public List<Bolsa> getListaBolsas() {
		return listaBolsas;
	}
	
	public void setListaBolsas(List<Bolsa> bolsas) {
		this.listaBolsas = bolsas;
	}
	
	public Bolsa getArea() {
		return area;
	}
	
	public void setArea(Bolsa area) {
		this.area = area;
	}
	
	public Solicitud getSolicitud() {
		return solicitud;
	}
	
	public void setSolicitud(Solicitud psolicitud) {
		this.solicitud = psolicitud;
	}
}
