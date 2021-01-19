package es.ujaen.uvirtual.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * Bean sobre los sistemas y el estado. 
 * 20130508 - julopez - creación inicial
 * 
 * @author julopez
 *
 */
public class Sistema implements Serializable {
	private static final long serialVersionUID = 71437524723280169L;
	String codigo = null;
	String descripcion = null;
	boolean enMantenimiento = false;
	boolean sinConexion = false;
	Date fechaEntradaEnMantenimiento = null;
	Date fechaComprobacion = null;
	String motivoDelMantenimiento = null;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isEnMantenimiento() {
		return enMantenimiento;
	}

	public void setEnMantenimiento(boolean enMantenimiento) {
		this.enMantenimiento = enMantenimiento;
	}

	public boolean isSinConexion() {
		return sinConexion;
	}

	public void setSinConexion(boolean sinConexion) {
		this.sinConexion = sinConexion;
	}

	public Date getFechaEntradaEnMantenimiento() {
		return fechaEntradaEnMantenimiento;
	}

	public void setFechaEntradaEnMantenimiento(Date fechaEntradaEnMantenimiento) {
		this.fechaEntradaEnMantenimiento = fechaEntradaEnMantenimiento;
	}

	public Date getFechaComprobacion() {
		return fechaComprobacion;
	}

	public void setFechaComprobacion(Date fechaComprobacion) {
		this.fechaComprobacion = fechaComprobacion;
	}

	public String getMotivoDelMantenimiento() {
		return motivoDelMantenimiento;
	}

	public void setMotivoDelMantenimiento(String motivoDelMantenimiento) {
		this.motivoDelMantenimiento = motivoDelMantenimiento;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("\n\n");
		buffer.append("Código ..................: ").append(this.codigo).append("\n");
		buffer.append("Descripción .............: ").append(this.descripcion).append("\n");
		buffer.append("En mantenimiento ........: ").append(this.enMantenimiento).append("\n");
		buffer.append("Fecha entrada en manto. .: ").append(this.fechaEntradaEnMantenimiento).append("\n");
		buffer.append("Motivo del mantenimiento : ").append(this.motivoDelMantenimiento).append("\n");
		buffer.append("Sin conexión al sistema .: ").append(this.sinConexion).append("\n");
		buffer.append("Fecha de comprobación ...: ").append(this.fechaComprobacion).append("\n");
		buffer.append("\n\n");
		
		return buffer.toString();
	}
}