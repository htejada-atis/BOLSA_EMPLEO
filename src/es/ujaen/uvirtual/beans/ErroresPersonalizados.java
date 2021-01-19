package es.ujaen.uvirtual.beans;

/**
 * Clase con los códigos de error personalizados.
 * 
 * @author julopez
 *
 */
public class ErroresPersonalizados {
	// 0 - 100 Errores globales
	public static final String ERROR_EN_CONFIGURACION = "UJA0000"; 
	public static final String ERROR_EN_BASEDEDATOS = "UJA0001";
	public static final String ERROR_PARAMETROS = "UJA0002";
	public static final String ERROR_UV_NO_DISPONIBLE = "UJA0003"; // BD uvirtual no disponible
	public static final String ERROR_SERV_NO_DISPONIBLE = "UJA0004"; // Sistema operacional no disponible
	public static final String ERROR_CONTROLADOR_NO_EXISTE = "UJA0005"; // Página no encontrada (controlado no existe)
	public static final String ERROR_USUARIO_NO_EXISTE = "UJA0006"; // Usuario no encontrado	
	
	// 101- 200 Errores de filtros
	public static final String ERROR_IDIOMA = "UJA0101";
	public static final String ERROR_SERVICIO = "UJA0102";
	public static final String ERROR_PRIVILEGIOS = "UJA0103";
	public static final String ERROR_MENU_DESACTIVADO = "UJA0104";
	public static final String ERROR_IP_ACCESO_MENU = "UJA0105";
	
	// 201-220 Errores en administracion
	public static final String ERROR_ADM_PARAMETRO_MENU = "UJA0201";
	public static final String ERROR_ADM_NO_EXIXTE_MENU = "UJA0202";
	public static final String ERROR_ADM_NO_AUTORIZADO = "UJA0203";
	
	private ErroresPersonalizados() { }
}
