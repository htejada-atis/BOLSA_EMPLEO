package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.CandidatoResultadoTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferente;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones
 */
public class VistaResultados extends Vista implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private transient BolsaEmpleoDataTable<BolsaResultado> dataTableBolsas;
	private transient BolsaEmpleoDataTable<CandidatoResultadoTable> dataTableCandidatos;
	private Bolsa bolsa;
	private BolsaResultado bolsaResultado;
	private Convocatoria convocatoria;
	private MeritoPreferente meritoPreferente;
	private UsuarioBolsaEmpleo candidato;
	private UsuarioBolsaEmpleo usuarioLogeado;
	private String vista;
	
	public String getVista() {
		return vista;
	}

	public void setVista(String vista) {
		this.vista = vista;
	}
	
	public Bolsa getBolsa() {
		return bolsa;
	}
	
	public void setBolsa(Bolsa bolsa) {
		this.bolsa = bolsa;
	}
	
	public Convocatoria getConvocatoria() {
		return convocatoria;
	}
	
	public void setConvocatoria(Convocatoria pconvocatoria) {
		this.convocatoria = pconvocatoria;
	}
	
	public UsuarioBolsaEmpleo getUsuarioLogeado() {
		return this.usuarioLogeado;
	}
	
	public void setUsuarioLogeado(UsuarioBolsaEmpleo usuario) {
		this.usuarioLogeado = usuario;
	}
	
	public BolsaEmpleoDataTable<BolsaResultado> getDataTableBolsas() {
		return dataTableBolsas;
	}

	public void setDataTableBolsas(BolsaEmpleoDataTable<BolsaResultado> dataTableBolsas) {
		this.dataTableBolsas = dataTableBolsas;
	}

	public BolsaEmpleoDataTable<CandidatoResultadoTable> getDataTableCandidatos() {
		return dataTableCandidatos;
	}

	public void setDataTableCandidatos(BolsaEmpleoDataTable<CandidatoResultadoTable> dataTableCandidatos) {
		this.dataTableCandidatos = dataTableCandidatos;
	}
	
	public UsuarioBolsaEmpleo getCandidato() {
		return candidato;
	}

	public void setCandidato(UsuarioBolsaEmpleo usuario) {
		this.candidato = usuario;
	}
	
	public BolsaResultado getBolsaResultado() {
		return bolsaResultado;
	}
	
	public void setBolsaResultado(BolsaResultado bolsaResultado) {
		this.bolsaResultado = bolsaResultado;
	}

	public MeritoPreferente getMeritoPreferente() {
		return meritoPreferente;
	}

	public void setMeritoPreferente(MeritoPreferente meritoPreferente) {
		this.meritoPreferente = meritoPreferente;
	}
}
