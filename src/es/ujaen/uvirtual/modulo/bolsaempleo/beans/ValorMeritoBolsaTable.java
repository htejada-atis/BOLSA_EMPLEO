package es.ujaen.uvirtual.modulo.bolsaempleo.beans;

import java.io.Serializable;

/**
 * Clase valor mérito bolsa table de bolsa empleo. Contiene el valor de un mérito para una bolsa .
 * 
 * @author ATISoluciones 2021
 */
public class ValorMeritoBolsaTable implements Serializable {

	private static final long serialVersionUID = 1L;
	private MeritoSolicitud meritoSolicitud;
	private Bolsa bolsa;
	private Convocatoria convocatoria;
	private String uidUsuario;

	/**
	 * Constructor por defecto.
	 */
	public ValorMeritoBolsaTable() {

	}

	/** Constructor con parametros .
	 * @param pmeritoSolicitud .
	 * @param pconvocatoria .
	 * @param pbolsa .
	 * @param puidUsuario .
	 */
	public ValorMeritoBolsaTable(MeritoSolicitud pmeritoSolicitud, Convocatoria pconvocatoria, Bolsa pbolsa, String puidUsuario) {
		this.meritoSolicitud = pmeritoSolicitud;
		this.convocatoria = pconvocatoria;
		this.bolsa = pbolsa;
		this.uidUsuario = puidUsuario;
	}
	
	/** Constructor con parametros .
	 * @param pmeritoSolicitud .
	 * @param pbolsa .
	 */
	public ValorMeritoBolsaTable(MeritoSolicitud pmeritoSolicitud, Bolsa pbolsa) {
		this.meritoSolicitud = pmeritoSolicitud;
		this.bolsa = pbolsa;
	}

	public MeritoSolicitud getMeritoSolicitud() {
		return this.meritoSolicitud;
	}

	public void setMeritoSolicitud(MeritoSolicitud merito) {
		this.meritoSolicitud = merito;
	}
	
	public Convocatoria getConvocatoria() {
		return convocatoria;
	}
	
	public void setConvocatoria(Convocatoria convocatoria) {
		this.convocatoria = convocatoria;
	}
	
	public Bolsa getBolsa() {
		return this.bolsa;
	}

	public void setBolsa(Bolsa bolsa) {
		this.bolsa = bolsa;
	}
	
	public String getUidUsuario() {
		return uidUsuario;
	}

	public void setUidUsuario(String uidUsuario) {
		this.uidUsuario = uidUsuario;
	}
			
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "ValorMeritoBolsaTable [meritoSolicitud=" + meritoSolicitud + ", bolsa=" + bolsa + ", convocatoria=" + convocatoria + ", uidUsuario=" + uidUsuario + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((meritoSolicitud == null) ? 0 : meritoSolicitud.hashCode());
		result = prime * result + ((bolsa == null) ? 0 : bolsa.hashCode());
		result = prime * result + ((convocatoria == null) ? 0 : convocatoria.hashCode());
		result = prime * result + ((uidUsuario == null) ? 0 : uidUsuario.hashCode());
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
		ValorMeritoBolsaTable other = (ValorMeritoBolsaTable) obj;
		if (meritoSolicitud == null) {
			if (other.meritoSolicitud != null) {
				return false;
			}
		} else if (!meritoSolicitud.equals(other.meritoSolicitud)) {
			return false;
		}
		if (bolsa == null) {
			if (other.bolsa != null) {
				return false;
			}
		} else if (!bolsa.equals(other.bolsa)) {
			return false;
		}
		if (convocatoria == null) {
			if (other.convocatoria != null) {
				return false;
			}
		} else if (!convocatoria.equals(other.convocatoria)) {
			return false;
		}
		if (uidUsuario == null) {
			if (other.uidUsuario != null) {
				return false;
			}
		} else if (!uidUsuario.equals(other.uidUsuario)) {
			return false;
		}
		
		return true;
	}

}
