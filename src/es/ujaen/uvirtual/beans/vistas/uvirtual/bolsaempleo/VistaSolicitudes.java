package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;
import java.util.List;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Solicitud;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.BolsaCandidato;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.BolsaSolicitud;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Merito;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.MeritoSolicitud;

/** Bean para la vista.
 * @author ATISoluciones
 */
public class VistaSolicitudes extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private String vista;
	private BolsaEmpleoDataTable<Solicitud> dataTable;
	private BolsaEmpleoDataTable<Bolsa> dataTableAreas;
	private BolsaEmpleoDataTable<BolsaSolicitud> dataTableBolsasSolicitud;
	private BolsaEmpleoDataTable<BolsaCandidato> dataTableBolsasCandidato;
	private BolsaEmpleoDataTable<MeritoSolicitud> dataTableMeritos;
	private List<Bolsa> listaBolsas;
	private List<BolsaSolicitud> listaBolsasSolicitud;
	private List<Merito> listaMeritos;
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
	
	public BolsaEmpleoDataTable<BolsaSolicitud> getDatatableBolsasSolicitud() {
		return dataTableBolsasSolicitud;
	}
	
	public void setDatatableBolsasSolicitud(BolsaEmpleoDataTable<BolsaSolicitud> dt) {
		this.dataTableBolsasSolicitud = dt;
	}
	
	public BolsaEmpleoDataTable<BolsaCandidato> getDataTableBolsasCandidato() {
		return dataTableBolsasCandidato;
	}
	
	public void setDataTableBolsasCandidato(BolsaEmpleoDataTable<BolsaCandidato> dt) {
		this.dataTableBolsasCandidato = dt;
	}
	
	public BolsaEmpleoDataTable<MeritoSolicitud> getDataTableMeritos() {
		return dataTableMeritos;
	}
	
	public void setDataTableMeritos(BolsaEmpleoDataTable<MeritoSolicitud> dt) {
		this.dataTableMeritos = dt;
	}
	
	public List<Bolsa> getListaBolsas() {
		return listaBolsas;
	}
	
	public void setListaBolsas(List<Bolsa> bolsas) {
		this.listaBolsas = bolsas;
	}
	
	public List<BolsaSolicitud> getListaBolsasSolicitud() {
		return listaBolsasSolicitud;
	}
	
	public void setListaBolsasSolicitud(List<BolsaSolicitud> bolsas) {
		this.listaBolsasSolicitud = bolsas;
	}
	
	public List<Merito> getListaMeritos() {
		return listaMeritos;
	}
	
	public void setListaMeritos(List<Merito> meritos) {
		this.listaMeritos = meritos;
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
