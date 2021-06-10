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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitudTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaSolicitudTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones 2021
 */
public class VistaSolicitudes extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private transient BolsaEmpleoDataTable<Solicitud> dataTable;
	private transient BolsaEmpleoDataTable<Bolsa> dataTableAreas;
	private transient BolsaEmpleoDataTable<BolsaSolicitudTable> dataTableBolsasSolicitud;	
	private transient BolsaEmpleoDataTable<BolsaCandidato> dataTableBolsasCandidato;
	private transient BolsaEmpleoDataTable<Bolsa> dataTableBolsasCandidatoSeleccionadas;
	private transient BolsaEmpleoDataTable<MeritoSolicitudTable> dataTableMeritos;
	private List<Bolsa> listaBolsas;
	private List<BolsaSolicitudTable> listaTablaBolsasSolicitud;
	private List<BolsaSolicitud> listaBolsasSolicitud;
	private List<MeritoSolicitud> listaMeritos;
	private List<TitulacionUsuario> listaTitulaciones;
	private List<Afinidad> listaAfinidades;
	private List<String> listaTiposAfinidades;
	private Solicitud solicitud;
	private Bolsa area;
	private Merito merito;
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

	public BolsaEmpleoDataTable<BolsaSolicitudTable> getDatatableBolsasSolicitud() {
		return dataTableBolsasSolicitud;
	}

	public void setDatatableBolsasSolicitud(BolsaEmpleoDataTable<BolsaSolicitudTable> dt) {
		this.dataTableBolsasSolicitud = dt;
	}

	public BolsaEmpleoDataTable<BolsaCandidato> getDataTableBolsasCandidato() {
		return dataTableBolsasCandidato;
	}

	public void setDataTableBolsasCandidato(BolsaEmpleoDataTable<BolsaCandidato> dt) {
		this.dataTableBolsasCandidato = dt;
	}

	public BolsaEmpleoDataTable<MeritoSolicitudTable> getDataTableMeritos() {
		return dataTableMeritos;
	}
	
	public void setDataTableBolsasCandidatoSeleccionadas(BolsaEmpleoDataTable<Bolsa> dt) {
		this.dataTableBolsasCandidatoSeleccionadas = dt;
	}

	public BolsaEmpleoDataTable<Bolsa> getDataTableBolsasCandidatoSeleccionadas() {
		return dataTableBolsasCandidatoSeleccionadas;
	}

	public void setDataTableMeritos(BolsaEmpleoDataTable<MeritoSolicitudTable> dataTable2) {
		this.dataTableMeritos = dataTable2;
	}

	public List<Bolsa> getListaBolsas() {
		return listaBolsas;
	}

	public void setListaBolsas(List<Bolsa> bolsas) {
		this.listaBolsas = bolsas;
	}

	public List<BolsaSolicitudTable> getListaTablaBolsasSolicitud() {
		return listaTablaBolsasSolicitud;
	}

	public void setListaTablaBolsasSolicitud(List<BolsaSolicitudTable> bolsas) {
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

	public List<TitulacionUsuario> getListaTitulaciones() {
		return listaTitulaciones;
	}

	public void setListaTitulaciones(List<TitulacionUsuario> titulaciones) {
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
