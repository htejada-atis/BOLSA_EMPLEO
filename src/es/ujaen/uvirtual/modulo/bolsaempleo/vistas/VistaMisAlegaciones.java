package es.ujaen.uvirtual.modulo.bolsaempleo.vistas;
import java.io.Serializable;
import es.ujaen.uvirtual.beans.vistas.Vista;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Alegacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoSolicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;

/**
 * Bean para la vista de alegaciones.
 *
 * @author ATISoluciones
 */
public class VistaMisAlegaciones extends Vista implements Serializable {
	private static final long serialVersionUID = 1L;
		
	private String vista;
	private transient BolsaEmpleoDataTable<Alegacion> dataTableAlegaciones;
	private UsuarioBolsaEmpleo usuarioLogeado;
	private Alegacion alegacion;
	private Convocatoria convocatoria;
	private VistaMisResultados beanResultados; 
	private MeritoSolicitud meritoSolicitud;	

	public String getVista() {
		return vista;
	}
	
	public void setVista(String vista) {
		this.vista = vista;
	}

	public BolsaEmpleoDataTable<Alegacion> getDataTableAlegaciones() {
		return dataTableAlegaciones;
	}

	public void setDataTableAlegaciones(BolsaEmpleoDataTable<Alegacion> dataTableAlegaciones) {
		this.dataTableAlegaciones = dataTableAlegaciones;
	}

	public UsuarioBolsaEmpleo getUsuarioLogeado() {
		return usuarioLogeado;
	}

	public void setUsuarioLogeado(UsuarioBolsaEmpleo usuarioLogeado) {
		this.usuarioLogeado = usuarioLogeado;
	}

	public Alegacion getAlegacion() {
		return alegacion;
	}

	public void setAlegacion(Alegacion alegacion) {
		this.alegacion = alegacion;
	}

	public Convocatoria getConvocatoria() {
		return convocatoria;
	}
	
	public void setConvocatoria(Convocatoria convocatoria) {
		this.convocatoria = convocatoria;
	}
	
	public void setMeritoSolicitud(MeritoSolicitud meritoSolicitud) {
		this.meritoSolicitud = meritoSolicitud;
	}
	
	public MeritoSolicitud getMeritoSolicitud() {
		return meritoSolicitud;
	}
	
	public VistaMisResultados getBeanResultados() {
		return this.beanResultados;
	}
	
	public void setBeanResultados(VistaMisResultados bean) {
		this.beanResultados = bean;
	}
}