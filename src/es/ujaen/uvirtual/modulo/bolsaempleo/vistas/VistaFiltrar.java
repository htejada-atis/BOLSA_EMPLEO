package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.List;

import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Candidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/** Bean para la vista.
 * @author fcampos
 */
public class VistaFiltrar extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private String vista;
	private transient BolsaEmpleoDataTable<Candidato> dataTableCandidatos;
	private transient BolsaEmpleoDataTable<TitulacionUsuario> dataTableTitulaciones;
	private UsuarioBolsaEmpleo candidato;
	private TitulacionUsuario titulacion;
	private List<TitulacionUsuario> listaValidadas;
	
	
	public BolsaEmpleoDataTable<Candidato> getDatatableCandidatos() {
		return dataTableCandidatos;
	}
	
	public void setDatatableCandidatos(BolsaEmpleoDataTable<Candidato> dt) {
		this.dataTableCandidatos = dt;
	}
	
	public BolsaEmpleoDataTable<TitulacionUsuario> getDatatableTitulaciones() {
		return dataTableTitulaciones;
	}
	
	public void setDatatableTitulaciones(BolsaEmpleoDataTable<TitulacionUsuario> dt) {
		this.dataTableTitulaciones = dt;
	}
	
	public UsuarioBolsaEmpleo getCandidato() {
		return candidato;
	}
	
	public void setCandidato(UsuarioBolsaEmpleo usuario) {
		this.candidato = usuario;
	}
	
	public TitulacionUsuario getTitulacion() {
		return titulacion;
	}
	
	public void setTitulacion(TitulacionUsuario titulacion) {
		this.titulacion = titulacion;
	}
	
	public List<TitulacionUsuario> getValidadas() {
		return listaValidadas;
	}
	
	public void setValidadas(List<TitulacionUsuario> validadas) {
		this.listaValidadas = validadas;
	}
	
	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}
}
