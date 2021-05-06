package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.List;

import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Afinidad;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TablaBolsaSolicitud;

/** Bean para la vista.
 * @author ATISoluciones 2021
 */
public class VistaSolicitudes extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private String vista;
	private BolsaEmpleoDataTable<Solicitud> dataTable;
	private BolsaEmpleoDataTable<Bolsa> dataTableAreas;
	private BolsaEmpleoDataTable<TablaBolsaSolicitud> dataTableBolsasSolicitud;
	private BolsaEmpleoDataTable<BolsaCandidato> dataTableBolsasCandidato;
	private BolsaEmpleoDataTable<MeritoSolicitud> dataTableMeritos;
	private List<Bolsa> listaBolsas;
	private List<TablaBolsaSolicitud> listaTablaBolsasSolicitud;
	private List<BolsaSolicitud> listaBolsasSolicitud;
	private List<MeritoSolicitud> listaMeritos;
	private List<Titulacion> listaTitulaciones;
	private List<Afinidad> listaAfinidades;
	private List<String> listaTiposAfinidades;
	private Solicitud solicitud;
	private Bolsa area;
	private Merito merito;
			
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
	
	public BolsaEmpleoDataTable<TablaBolsaSolicitud> getDatatableBolsasSolicitud() {
		return dataTableBolsasSolicitud;
	}
	
	public void setDatatableBolsasSolicitud(BolsaEmpleoDataTable<TablaBolsaSolicitud> dt) {
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
	
	public List<TablaBolsaSolicitud> getListaTablaBolsasSolicitud() {
		return listaTablaBolsasSolicitud;
	}
	
	public void setListaTablaBolsasSolicitud(List<TablaBolsaSolicitud> bolsas) {
		this.listaTablaBolsasSolicitud = bolsas;
	}
	
	public List<BolsaSolicitud> getListaBolsasSolicitud() {
		return listaBolsasSolicitud;
	}
	
	public void setListaBolsasSolicitud(List<BolsaSolicitud> bolsas) {
		this.listaBolsasSolicitud = bolsas;
	}
	
	public List<MeritoSolicitud> getListaMeritosSolicitud() {
		return listaMeritos;
	}
	
	public void setListaMeritosSolicitud(List<MeritoSolicitud> listaMeritos2) {
		this.listaMeritos = listaMeritos2;
	}
	
	public List<Titulacion> getListaTitulaciones() {
		return listaTitulaciones;
	}
	
	public void setListaTitulaciones(List<Titulacion> titulaciones) {
		this.listaTitulaciones = titulaciones;
	}
	
	public List<Afinidad> getListaAfinidades() {
		return listaAfinidades;
	}
	
	public void setListaAfinidades(List<Afinidad> afinidades) {
		this.listaAfinidades = afinidades;
	}
	
	public List<String> getListaTipoAfinidades() {
		return listaTiposAfinidades;
	}
	
	public void setListaTipoAfinidades(List<String> afinidades) {
		this.listaTiposAfinidades = afinidades;
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
	
	public Merito getMerito() {
		return merito;
	}
	
	public void setMerito(Merito merito) {
		this.merito = merito;
	}
}
