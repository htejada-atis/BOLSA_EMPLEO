package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
public class VistaMisResultados extends Vista implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private List<Convocatoria> listaConvocatorias = new ArrayList<>();
	private transient BolsaEmpleoDataTable<BolsaResultado> dataTableBolsas;
	private transient BolsaEmpleoDataTable<CandidatoResultadoTable> dataTableCandidatos;
	private Bolsa bolsa;
	private BolsaResultado bolsaResultado;
	private Convocatoria convocatoria;
	private MeritoPreferente meritoPreferente;
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
	
	public BolsaEmpleoDataTable<BolsaResultado> getDataTableBolsas() {
		return dataTableBolsas;
	}
	
	public void setDataTableBolsas(BolsaEmpleoDataTable<BolsaResultado> dataTableBolsas) {
		this.dataTableBolsas = dataTableBolsas;
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
	
	public BolsaEmpleoDataTable<CandidatoResultadoTable> getDataTableCandidatos() {
		return dataTableCandidatos;
	}
	
	public void setDataTableCandidatos(BolsaEmpleoDataTable<CandidatoResultadoTable> dataTableCandidatos) {
		this.dataTableCandidatos = dataTableCandidatos;
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
	
	public List<Convocatoria> getListaConvocatorias() {
		return listaConvocatorias;
	}
	
	public void setListaConvocatorias(List<Convocatoria> listaConvocatorias) {
		this.listaConvocatorias = listaConvocatorias;
	}
}
