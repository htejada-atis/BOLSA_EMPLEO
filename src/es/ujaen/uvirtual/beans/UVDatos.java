package es.ujaen.uvirtual.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.ujaen.uvirtual.beans.vistas.Vista;

/**
 * Objeto con los datos de la petición para incluir las vistas que se deben cargar (JSP), datos de CSS, JS, etc.
 * que permite pasar la información entre el framework uvirtual el controlador y la vista
 * @author julopez
 *
 */
@SuppressWarnings("java:S1319")
public class UVDatos {
	public static final String NOMBRE_ATRIBUTO = "UVDatosFramework";	// Nombre con el que se almacenar en la peticin
	public static final String ID_USUARIO_SESION = "uid";				// Constante para el identificador del usuario en la sesin 
	
	
	private Acceso acceso = new Acceso();						// Datos del acceso para log, etc
	private boolean usuarioAutenticado = false;					// Indica si el usuario est autenticado
	private ArrayList<String> ficherosJSP = new ArrayList<>();	// Lista de ficheros JSPs que se deben incluir para generar la respuest
	private ArrayList<String> ficherosJS = new ArrayList<>();	// Lista de ficheros JS que se deben incluir en la cabecera
	private ArrayList<String> ficherosCSS = new ArrayList<>();	// Lista de ficheros CSS que se deben incluir en la cabecera
	private String ficheroXSL = null;							// Fichero XSL para generacin mediante el fichero FOP del PDF
	private boolean respuestaEnviada = false;					// Indica que ya se ha enviado la respuesta y que no se debe enviar
	private String docType = null;								// DocType para enviar en la respuesta
	private String contentType = null;							// ContentType a enviar en la respuesta
	private String formatoSalida = null;						// Formato de salida
	private String identificadorUsuario = null;					// Identificador del usuario
	private HashMap<String, Vista> vistas = new HashMap<>();	// Objeto con los datos de las vistas
	private Usuario usuario = null;								// Usuario que realiza la peticin
	private Menu menu = null;									// Entrada de men sobre la que se realiza la peticin
	private Long identificadorPeticion = null;					// Identificador de la peticin para generar PDF, etc.
	private Map<String, String[]> parametrosPeticionPrevia = null;	// Parmetros de la peticin previa para la generacin del PDF, Excel, etc.

	public Acceso getAcceso() {
		return acceso;
	}

	public void setAcceso(Acceso acceso) {
		this.acceso = acceso;
	}

	public boolean isUsuarioAutenticado() {
		return usuarioAutenticado;
	}

	public void setUsuarioAutenticado(boolean usuarioAutenticado) {
		this.usuarioAutenticado = usuarioAutenticado;
	}

	public ArrayList<String> getFicherosJSP() {
		return ficherosJSP;
	}

	public void setFicherosJSP(ArrayList<String> ficherosJSP) {
		this.ficherosJSP = ficherosJSP;
	}

	public ArrayList<String> getFicherosJS() {
		return ficherosJS;
	}

	public void setFicherosJS(ArrayList<String> ficherosJS) {
		this.ficherosJS = ficherosJS;
	}

	public ArrayList<String> getFicherosCSS() {
		return ficherosCSS;
	}

	public void setFicherosCSS(ArrayList<String> ficherosCSS) {
		this.ficherosCSS = ficherosCSS;
	}

	public String getFicheroXSL() {
		return ficheroXSL;
	}

	public void setFicheroXSL(String ficheroXSL) {
		this.ficheroXSL = ficheroXSL;
	}

	public boolean isRespuestaEnviada() {
		return respuestaEnviada;
	}

	public void setRespuestaEnviada(boolean respuestaEnviada) {
		this.respuestaEnviada = respuestaEnviada;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFormatoSalida() {
		return formatoSalida;
	}

	public void setFormatoSalida(String formatoSalida) {
		this.formatoSalida = formatoSalida;
	}

	public String getIdentificadorUsuario() {
		return identificadorUsuario;
	}

	public void setIdentificadorUsuario(String identificadorUsuario) {
		this.identificadorUsuario = identificadorUsuario;
	}

	public HashMap<String, Vista> getVistas() {
		return vistas;
	}

	public void setVistas(HashMap<String, Vista> vistas) {
		this.vistas = vistas;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Long getIdentificadorPeticion() {
		return identificadorPeticion;
	}

	public void setIdentificadorPeticion(Long identificadorPeticion) {
		this.identificadorPeticion = identificadorPeticion;
	}

	public Map<String, String[]> getParametrosPeticionPrevia() {
		return parametrosPeticionPrevia;
	}

	public void setParametrosPeticionPrevia(Map<String, String[]> parametrosPeticionPrevia) {
		this.parametrosPeticionPrevia = parametrosPeticionPrevia;
	}
}