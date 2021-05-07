package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;

import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaValidacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoValidacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/** Bean para la vista.
 * @author fcampos
 */
public class VistaValidarNoAfines extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private String vista;	
	private Bolsa bolsa;
	private Convocatoria conv;
	private BolsaEmpleoDataTable<BolsaValidacion> datatableBolsas;
	private BolsaEmpleoDataTable<CandidatoValidacion> datatableCandidatos;
	
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
}
