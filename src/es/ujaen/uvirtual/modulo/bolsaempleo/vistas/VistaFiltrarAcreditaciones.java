package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.List;

import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoAcreditacionesTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones 2021
 */
public class VistaFiltrarAcreditaciones extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private transient BolsaEmpleoDataTable<CandidatoAcreditacionesTable> dataTableCandidatos;
	private transient BolsaEmpleoDataTable<MeritoPreferenteUsuario> dataTableAcreditaciones;
	private List<MeritoPreferenteUsuario> listaValidadas;
	private UsuarioBolsaEmpleo candidato;
	private MeritoPreferenteUsuario acreditacion;
	private String codigoPadreMeritoPreferente;
	private String vista;
	private UsuarioBolsaEmpleo usuarioLogeado;

	public BolsaEmpleoDataTable<CandidatoAcreditacionesTable> getDatatableCandidatos() {
		return dataTableCandidatos;
	}

	public void setDatatableCandidatos(BolsaEmpleoDataTable<CandidatoAcreditacionesTable> dt) {
		this.dataTableCandidatos = dt;
	}

	public BolsaEmpleoDataTable<MeritoPreferenteUsuario> getDataTableAcreditaciones() {
		return dataTableAcreditaciones;
	}

	public void setDataTableAcreditaciones(BolsaEmpleoDataTable<MeritoPreferenteUsuario> dt) {
		this.dataTableAcreditaciones = dt;
	}

	public UsuarioBolsaEmpleo getCandidato() {
		return candidato;
	}

	public void setCandidato(UsuarioBolsaEmpleo usuario) {
		this.candidato = usuario;
	}

	public MeritoPreferenteUsuario getAcreditacion() {
		return acreditacion;
	}

	public void setAcreditacion(MeritoPreferenteUsuario acreditacion) {
		this.acreditacion = acreditacion;
	}

	public void setCodigoPadreMeritoPreferente(String codigo) {
		this.codigoPadreMeritoPreferente = codigo;
	}

	public String getCodigoPadreMeritoPreferente() {
		return this.codigoPadreMeritoPreferente;
	}
	
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
	
	public void setValidadas(List<MeritoPreferenteUsuario> validadas) {
		this.listaValidadas = validadas;
	} 
	
	public List<MeritoPreferenteUsuario> getValidadas() {
		return this.listaValidadas;
	}
}
