package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Afinidad;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaValidacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoValidacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoValidarTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ValorMeritoBolsaTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/** Bean para la vista.
 * @author ATISoluciones 2021
 */
public class VistaValidar extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private String vista;
	private Bolsa bolsa;
	private Convocatoria conv;
	private MeritoSolicitud merito;
	private UsuarioBolsaEmpleo usuario;
	private BolsaEmpleoDataTable<BolsaValidacion> datatableBolsas;
	private BolsaEmpleoDataTable<CandidatoValidacion> datatableCandidatos;
	private BolsaEmpleoDataTable<MeritoValidarTable> datatableMeritos;
	private List<ItemBaremacion> items = new ArrayList<>();
	private List<ValorMeritoBolsaTable> bolsas = new ArrayList<>();
	private List<Afinidad> listaAfinidades;
	private List<String> listaTiposAfinidades;
	
	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}
	
	public void setDatatableBolsas(BolsaEmpleoDataTable<BolsaValidacion> dt) {
		this.datatableBolsas = dt;
	}
	
	public BolsaEmpleoDataTable<BolsaValidacion> getDatatableBolsas() {
		return this.datatableBolsas;	
	}
	
	public void setDatatableCandidatos(BolsaEmpleoDataTable<CandidatoValidacion> dt) {
		this.datatableCandidatos = dt;
	}
	
	public BolsaEmpleoDataTable<CandidatoValidacion> getDatatableCandidatos() {
		return this.datatableCandidatos;	
	}
	
	public void setDatatableMeritos(BolsaEmpleoDataTable<MeritoValidarTable> dt) {
		this.datatableMeritos = dt;
	}
	
	public BolsaEmpleoDataTable<MeritoValidarTable> getDatatableMeritos() {
		return this.datatableMeritos;
	}
	
	public Bolsa getBolsa() {
		return bolsa;
	}
	
	public void setBolsa(Bolsa bolsa) {
		this.bolsa = bolsa;
	}
	
	public Convocatoria getConvocatoria() {
		return conv;
	}
	
	public void setConvocatoria(Convocatoria pconv) {
		this.conv = pconv;
	}
	
	public MeritoSolicitud getMerito() {
		return merito;
	}
	
	public void setMerito(MeritoSolicitud merito) {
		this.merito = merito;
	}
	
	public UsuarioBolsaEmpleo getCandidato() {
		return usuario;
	}
	
	public void setCandidato(UsuarioBolsaEmpleo pusuario) {
		this.usuario = pusuario;
	}
	
	public List<ItemBaremacion> getItems() {
		return items;
	}
	
	public void setItems(List<ItemBaremacion> items) {
		this.items = items;
	}
	
	public List<ValorMeritoBolsaTable> getBolsas() {
		return bolsas;
	}
	
	public void setBolsas(List<ValorMeritoBolsaTable> bolsas) {
		this.bolsas = bolsas;
	}
	
	public List<Afinidad> getListaAfinidades() {
		return listaAfinidades;
	}
	
	public void setListaAfinidades(List<Afinidad> afinidades) {
		this.listaAfinidades = afinidades;
	}
	
}
