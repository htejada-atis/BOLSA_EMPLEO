package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;

import java.io.Serializable;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Fichero;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;

/**
 * Bean para la vista.
 * 
 * @author ATISoluciones
 */
public class VistaDescargaFicheros extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
	private String vista;
	private BolsaResultado bolsaResultado;
	private Fichero fichero;
	private Merito merito;
	private MeritoPreferenteUsuario acreditacion;
	private Solicitud solicitud;
	private TitulacionUsuario titulacion;
	private UsuarioBolsaEmpleo usuarioLogeado;
	
	
	public Fichero getFichero() {
		return fichero;
	}

	public void setFichero(Fichero fichero) {
		this.fichero = fichero;
	}
	
	public Merito getMerito() {
		return this.merito;
	}

	public void setMerito(Merito merito) {
		this.merito = merito;
	}
	
	public MeritoPreferenteUsuario getAcreditacion() {
		return this.acreditacion;
	}

	public void setAcreditacion(MeritoPreferenteUsuario acreditacion) {
		this.acreditacion = acreditacion;
	}
	
	public Solicitud getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(Solicitud psolicitud) {
		this.solicitud = psolicitud;
	}
	
	public TitulacionUsuario getTitulacion() {
		return this.titulacion;
	}

	public void setTitulacion(TitulacionUsuario titulacion) {
		this.titulacion = titulacion;
	}
	
	public UsuarioBolsaEmpleo getUsuarioLogeado() {
		return this.usuarioLogeado;
	}
	
	public void setUsuarioLogeado(UsuarioBolsaEmpleo usuario) {
		this.usuarioLogeado = usuario;
	}
	
	public String getVista() {
		return vista;
	}

	public void setVista(String vista) {
		this.vista = vista;
	}

	public BolsaResultado getBolsaResultado() {
		return bolsaResultado;
	}

	public void setBolsaResultado(BolsaResultado bolsaResultado) {
		this.bolsaResultado = bolsaResultado;
	}
	
}
