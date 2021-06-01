package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.List;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoTitulacionTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Titulacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones.
 */
public class VistaFiltrar extends VistaBEP implements Serializable {
	private static final long serialVersionUID = 1L;
	private transient BolsaEmpleoDataTable<CandidatoTitulacionTable> dataTableCandidatos;
	private transient BolsaEmpleoDataTable<TitulacionUsuario> dataTableTitulaciones;
	private UsuarioBolsaEmpleo candidato;
	private TitulacionUsuario titulacion;
	private List<TitulacionUsuario> listaValidadas;
	private List<Titulacion> listaTitulacion;

	public BolsaEmpleoDataTable<CandidatoTitulacionTable> getDatatableCandidatos() {
		return dataTableCandidatos;
	}

	public void setDatatableCandidatos(BolsaEmpleoDataTable<CandidatoTitulacionTable> dt) {
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

	public void setCandidato(UsuarioBolsaEmpleo pusuario) {
		this.candidato = pusuario;
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

	public List<Titulacion> getTitulaciones() {
		return listaTitulacion;
	}

	public void setTitulaciones(List<Titulacion> titulaciones) {
		this.listaTitulacion = titulaciones;
	}
}
