package es.ujaen.uvirtual.beans.uxxirrhh;

import java.io.Serializable;

/**
 * Clase que representa una plaza en UXXIRRHH.
 * @author julopez
 *
 */
public class Plaza implements Serializable {

	private static final long serialVersionUID = 4136395622856802279L;
	protected String codigo = null;
	protected String descripcion = null;
	protected String tipoPersonal = null;
	protected String localidad = null;
	protected String despacho = null;
	protected String telefono = null;
	protected String buzon = null;
	protected Unidad unidad = null;
	protected Unidad subunidad = null;
	protected Edificio edificio = null;
	
	/**
	 * Constructor por defecto.
	 */
	public Plaza() {
		super();
	}

	/**
	 * Constructor con todos los parámetros.
	 * @param pcodigo codigo
	 * @param pdescripcion descripcion
	 * @param ptipoPersonal tipo personal
	 * @param plocalidad localidad
	 * @param pdespacho despacho
	 * @param ptelefono telefono
	 * @param pbuzon buzon
	 * @param punidad unidad
	 * @param psubunidad subunidad
	 * @param pedificio edificio
	 */
	@SuppressWarnings({"checkstyle:ParameterNumber", "java:S107"})
	public Plaza(String pcodigo, String pdescripcion, String ptipoPersonal,
			String plocalidad, String pdespacho, String ptelefono, String pbuzon,
			Unidad punidad, Unidad psubunidad, Edificio pedificio) {
		super();
		this.codigo = pcodigo;
		this.descripcion = pdescripcion;
		this.tipoPersonal = ptipoPersonal;
		this.localidad = plocalidad;
		this.despacho = pdespacho;
		this.telefono = ptelefono;
		this.buzon = pbuzon;
		this.unidad = punidad;
		this.subunidad = psubunidad;
		this.edificio = pedificio;
	}

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

	public String getTipoPersonal() {
		return tipoPersonal;
	}

	public void setTipoPersonal(String tipoPersonal) {
		this.tipoPersonal = tipoPersonal;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getDespacho() {
		return despacho;
	}

	public void setDespacho(String despacho) {
		this.despacho = despacho;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getBuzon() {
		return buzon;
	}

	public void setBuzon(String buzon) {
		this.buzon = buzon;
	}

	public Unidad getUnidad() {
		return unidad;
	}

	public void setUnidad(Unidad unidad) {
		this.unidad = unidad;
	}

	public Unidad getSubunidad() {
		return subunidad;
	}

	public void setSubunidad(Unidad subunidad) {
		this.subunidad = subunidad;
	}

	public Edificio getEdificio() {
		return edificio;
	}

	public void setEdificio(Edificio edificio) {
		this.edificio = edificio;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("Código ......: ").append(this.codigo).append("\n");
		buffer.append("Tipo personal: ").append(this.tipoPersonal).append("\n");
		buffer.append("Localidad ...: ").append(this.localidad).append("\n");
		buffer.append("Despacho ....: ").append(this.despacho).append("\n");
		buffer.append("Teléfono ....: ").append(this.telefono).append("\n");
		buffer.append("Buzón .......: ").append(this.buzon).append("\n");
		if (this.unidad != null) {
			buffer.append("Unidad ......: ").append(this.unidad.codigo).append(" - ").append(this.unidad.nombre).append("\n");
		}
		if (this.subunidad != null) {
			buffer.append("Subunidad ...: ").append(this.subunidad.codigo).append(" - ").append(this.subunidad.nombre).append("\n");
		}
		if (this.edificio != null) {
			buffer.append("Edificio ....: ").append(this.edificio.getCodigo()).append(this.edificio.getDescripcion()).append("\n");
		}
		return buffer.toString();
	}
}