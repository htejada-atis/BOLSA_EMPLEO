package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Clase historial .
 * 
 * @author ATISoluciones
 */
public class Historial implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codCambio;
	private Date fechaLog;
	private String log;
	private String uidUsuario;
	private String rolUsuario;
	
	
	/**
	 * Constructor por defecto.
	 */
	public Historial() {

	}

	/** Constructor con parametros.
	 * @param pcodCambio .
	 * @param pfechaLog .
	 * @param plog .
	 * @param puidUsuario .
	 * @param prolUsuario .
	 */
	public Historial(Integer pcodCambio, Date pfechaLog, String plog, String puidUsuario, String prolUsuario) {
		super();
		this.codCambio = pcodCambio;
		this.fechaLog = pfechaLog;
		this.log = plog;
		this.uidUsuario = puidUsuario;
		this.rolUsuario = prolUsuario;
	}
	
	/** Constructor con parametros.
	 * @param pfechaLog .
	 * @param plog .
	 * @param puidUsuario .
	 * @param prolUsuario .
	 */
	public Historial(Date pfechaLog, String plog, String puidUsuario, String prolUsuario) {
		super();
		this.fechaLog = pfechaLog;
		this.log = plog;
		this.uidUsuario = puidUsuario;
		this.rolUsuario = prolUsuario;
	}

	/**
	 * Constructor copia.
	 * 
	 * @param copia Historial Validación a copiar
	 */
	public Historial(Historial copia) {
		this.codCambio = copia.codCambio;
		this.fechaLog = copia.fechaLog;
		this.log = copia.log;
		this.uidUsuario = copia.uidUsuario;
		this.rolUsuario = copia.rolUsuario;
	}

	public Date getFechaLog() {
		return fechaLog;
	}

	public void setFechaLog(Date fecha) {
		this.fechaLog = fecha;
	}

	public Integer getCodCambio() {
		return codCambio;
	}

	public void setCodCambio(Integer codCambio) {
		this.codCambio = codCambio;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}
	
	public String getUidUsuario() {
		return uidUsuario;
	}

	public void setUidUsuario(String uidUsuario) {
		this.uidUsuario = uidUsuario;
	}

	public String getRolUsuario() {
		return rolUsuario;
	}

	public void setRolUsuario(String rolUsuario) {
		this.rolUsuario = rolUsuario;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Historial [fechaLog=" + fechaLog + ", codCambio=" + codCambio + ", log=" + log + ", uidUsuario=" + uidUsuario + ", rolUsuario=" + rolUsuario + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fechaLog == null) ? 0 : fechaLog.hashCode());
		result = prime * result + ((codCambio == null) ? 0 : codCambio.hashCode());
		result = prime * result + ((log == null) ? 0 : log.hashCode());
		result = prime * result + ((uidUsuario == null) ? 0 : uidUsuario.hashCode());
		result = prime * result + ((rolUsuario == null) ? 0 : rolUsuario.hashCode());
		return result;
	}

	@Override
	@SuppressWarnings({ "checkstyle:CyclomaticComplexity", "checkstyle:NPathComplexity" })
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Historial other = (Historial) obj;
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (fechaLog == null) {
			if (other.fechaLog != null) {
				return false;
			}
		} else if (formater.format(fechaLog).compareTo(formater.format(other.fechaLog)) != 0) {
			return false;
		}
		if (codCambio == null) {
			if (other.codCambio != null) {
				return false;
			}
		} else if (!codCambio.equals(other.codCambio)) {
			return false;
		}
		if (log == null) {
			if (other.log != null) {
				return false;
			}
		} else if (!log.equals(other.log)) {
			return false;
		}
		if (uidUsuario == null) {
			if (other.uidUsuario != null) {
				return false;
			}
		} else if (!uidUsuario.equals(other.uidUsuario)) {
			return false;
		}
		if (rolUsuario == null) {
			if (other.rolUsuario != null) {
				return false;
			}
		} else if (!rolUsuario.equals(other.rolUsuario)) {
			return false;
		}
		
		return true;
	}

}
