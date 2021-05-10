package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaValidacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoValidacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/** Bean para la vista.
 * @author fcampos
 */
public class VistaValidar extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private String vista;
	private Bolsa bolsa;
	private Convocatoria conv;
	private Merito merito;
	private UsuarioBolsaEmpleo usuario;
	private BolsaEmpleoDataTable<BolsaValidacion> datatableBolsas;
	private BolsaEmpleoDataTable<Bolsa> datatableBolsasCandidato;
	private BolsaEmpleoDataTable<CandidatoValidacion> datatableCandidatos;
	private BolsaEmpleoDataTable<Merito> datatableMeritos;
	
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
	
	public void setDatatableBolsasCandidato(BolsaEmpleoDataTable<Bolsa> dt) {
		this.datatableBolsasCandidato = dt;
	}
	
	public BolsaEmpleoDataTable<Bolsa> getDatatableBolsasCandidato() {
		return this.datatableBolsasCandidato;	
	}
	
	public void setDatatableCandidatos(BolsaEmpleoDataTable<CandidatoValidacion> dt) {
		this.datatableCandidatos = dt;
	}
	
	public BolsaEmpleoDataTable<CandidatoValidacion> getDatatableCandidatos() {
		return this.datatableCandidatos;	
	}
	
	public void setDatatableMeritos(BolsaEmpleoDataTable<Merito> dt) {
		this.datatableMeritos = dt;
	}
	
	public BolsaEmpleoDataTable<Merito> getDatatableMeritos() {
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
	
	public Merito getMerito() {
		return merito;
	}
	
	public void setMerito(Merito merito) {
		this.merito = merito;
	}
	
	public UsuarioBolsaEmpleo getCandidato() {
		return usuario;
	}
	
	public void setCandidato(UsuarioBolsaEmpleo pusuario) {
		this.usuario = pusuario;
	}
}
