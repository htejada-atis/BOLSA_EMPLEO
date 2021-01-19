package es.ujaen.uvirtual.beans.vistas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import es.ujaen.uvirtual.utilidades.EscapaHTML;

/**
 * Bean sobre el que extender el resto de vistas, incluye los campos comunes.
 * 20161118 - julopez - creación inicial
 * @author julopez
 */
@SuppressWarnings({"java:S1319", "java:S1948"})
public class Vista implements Serializable {
	public static final String MENSAJE_SIN_ESCAPAR = "{mse}";
	private static final long serialVersionUID = -2309584007060497032L;
	protected boolean modoPdf = false;
	protected boolean modoExcel = false;
	protected ArrayList<String> mensajesInformativos = new ArrayList<>();
	protected ArrayList<String> mensajesDeAdvertencia = new ArrayList<>();
	protected ArrayList<String> mensajesDeError = new ArrayList<>();
	protected ArrayList<String> mensajesDeExito = new ArrayList<>();
	private HashMap<Object, Object> objetos = new HashMap<>(); // HashMap Con objetos para los controladores

	private String prvFormatearMensaje(ArrayList<String> datos) {
		String salida = "";
		if (datos.size() == 1) {
			if (datos.get(0).startsWith(MENSAJE_SIN_ESCAPAR)) {
				salida = datos.get(0).substring(MENSAJE_SIN_ESCAPAR.length());
			} else {
				salida = EscapaHTML.escapa(datos.get(0));
			}
		} else if (datos.size() > 1) {
			StringBuilder sb = new StringBuilder();
			sb.append("<ul>");
			for (String linea : datos) {
				if (!"".equals(linea.trim())) {
					if (linea.startsWith(MENSAJE_SIN_ESCAPAR)) {
						sb.append("<li>").append(linea.substring(MENSAJE_SIN_ESCAPAR.length())).append("</li>");
					} else {
						sb.append("<li>").append(EscapaHTML.escapa(linea)).append("</li>");
					}
				}
			}
			sb.append("</ul>");
			salida = sb.toString();
		}
		return salida;
	}
	
	/** formatear mensaje de exito.
	 * @return mensaje de exito formateado
	 */
	public String formatearMensajesDeExito() {
		return prvFormatearMensaje(mensajesDeExito);
	}

	/** formatear mensaje de error.
	 * @return mensaje de error formateado
	 */
	public String formatearMensajesDeError() {
		return prvFormatearMensaje(mensajesDeError);
	}
	
	/** formatear mensaje de advertencia.
	 * @return mensaje de advertencia formateado
	 */
	public String formatearMensajesDeAdvertencia() {
		return prvFormatearMensaje(mensajesDeAdvertencia);
	}
	
	/** formatear mensaje informativo.
	 * @return mensaje informativo formateado
	 */
	public String formatearMensajesInformativos() {
		return prvFormatearMensaje(mensajesInformativos);
	}
	
	public boolean isModoPdf() {
		return modoPdf;
	}

	public void setModoPdf(boolean modoPdf) {
		this.modoPdf = modoPdf;
	}

	public boolean isModoExcel() {
		return modoExcel;
	}

	public void setModoExcel(boolean modoExcel) {
		this.modoExcel = modoExcel;
	}

	public ArrayList<String> getMensajesInformativos() {
		return mensajesInformativos;
	}

	public ArrayList<String> getMensajesDeAdvertencia() {
		return mensajesDeAdvertencia;
	}

	public ArrayList<String> getMensajesDeError() {
		return mensajesDeError;
	}

	public ArrayList<String> getMensajesDeExito() {
		return mensajesDeExito;
	}

	public HashMap<Object, Object> getObjetos() {
		return objetos;
	}
	
}
