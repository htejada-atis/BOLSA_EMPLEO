package es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo;

import java.io.Serializable;
import java.util.List;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Candidato;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Titulacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;

/** Bean para la vista.
 * @author fcampos
 */
public class VistaFiltrar extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private String vista;
	private BolsaEmpleoDataTable<Candidato> dataTableCandidatos;
	private BolsaEmpleoDataTable<Titulacion> dataTableTitulaciones;
	private UsuarioBolsaEmpleo candidato;
	private Titulacion titulacion;
	private List<Titulacion> listaValidadas;
	
	
	public BolsaEmpleoDataTable<Candidato> getDatatableCandidatos() {
		return dataTableCandidatos;
	}
	
	public void setDatatableCandidatos(BolsaEmpleoDataTable<Candidato> dt) {
		this.dataTableCandidatos = dt;
	}
	
	public BolsaEmpleoDataTable<Titulacion> getDatatableTitulaciones() {
		return dataTableTitulaciones;
	}
	
	public void setDatatableTitulaciones(BolsaEmpleoDataTable<Titulacion> dt) {
		this.dataTableTitulaciones = dt;
	}
	
	public UsuarioBolsaEmpleo getCandidato() {
		return candidato;
	}
	
	public void setCandidato(UsuarioBolsaEmpleo usuario) {
		this.candidato = usuario;
	}
	
	public Titulacion getTitulacion() {
		return titulacion;
	}
	
	public void setTitulacion(Titulacion titulacion) {
		this.titulacion = titulacion;
	}
	
	public List<Titulacion> getValidadas() {
		return listaValidadas;
	}
	
	public void setValidadas(List<Titulacion> validadas) {
		this.listaValidadas = validadas;
	}
	
	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}
}
