package es.ujaen.uvirtual.modulo.bolsaempleo.utilidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import es.ujaen.uvirtual.utilidades.EscapaHTML;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase para validar el request.
 * @author ATISoluciones 2021 
 */
public class BolsaEmpleoValidator {
	public static final String PARAM_STRING = "string";
	public static final String PARAM_INTEGER = "integer";
	public static final String PARAM_FLOAT = "float";
	public static final String PARAM_DATE = "date";
	public static final String PARAM_BOOLEAN = "boolean";
	
	public static final String MENSAJE_ERROR_PARSEANDO = "Tipo no válido";
	public static final String MENSAJE_ERROR_TIPOPARAMETRONOVALIDO = "Tipo de parámetro no válido";
	public static final String MENSAJE_ERROR_REGLAREQUIEREPARAMETRO = "La regla requiere un parámetro";
	public static final String MENSAJE_ERROR_TIPOREGLANOVALIDA = "La regla del validator no es válida";
	public static final String MENSAJE_ERROR_NOBLANKSTRING = "La regla noBlank solo es válida en string";
	public static final String MENSAJE_ERROR_PAMETRORULEENTERO = "Parámetro de rule debe ser un entero";
	public static final String MENSAJE_ERROR_MAXSTRINGNUMERICOS = "La regla max solo es válida para string y numéricos";
	public static final String MENSAJE_ERROR_MINSTRINGNUMERICOS = "La regla min solo es válida para string y numéricos";
	public static final String MENSAJE_ERROR_SELECTNEGATIVO = "Debe seleccionar una opción válida";

	private HttpServletRequest request;
	private HashMap<String, String> params;
	private HashMap<String, Object> values;
	private HashMap<String, ArrayList<String>> errors;
	
	/**
	 * Constructor del validator.
	 * @param prequest .
	 */
	public BolsaEmpleoValidator(HttpServletRequest prequest) {
		this.request = prequest;
		this.params = new HashMap<String, String>();
		this.values = new HashMap<String, Object>();		
		this.errors = new HashMap<String, ArrayList<String>>();
	}
	
	/**
	 * Añade un parametro de tipo string.
	 * @param param .
	 * @throws UVException .
	 */
	public void addParamString(String param) throws UVException {
		this.addParam(param, PARAM_STRING);
	}
	
	/**
	 * Añade un parametro de tipo integer.
	 * @param param .
	 * @throws UVException .
	 */
	public void addParamInteger(String param) throws UVException {
		this.addParam(param, PARAM_INTEGER);
	}
	
	/**
	 * Añade un parametro de tipo float.
	 * @param param .
	 * @throws UVException .
	 */
	public void addParamFloat(String param) throws UVException {
		this.addParam(param, PARAM_FLOAT);
	}
	
	/**
	 * Añade un parametro de tipo date.
	 * @param param .
	 * @throws UVException .
	 */
	public void addParamDate(String param) throws UVException {
		this.addParam(param, PARAM_DATE);
	}
	
	/**
	 * Añade un parametro de tipo boolean.
	 * @param param .
	 * @throws UVException .
	 */
	public void addParamBoolean(String param) throws UVException {
		this.addParam(param, PARAM_BOOLEAN);
	}
	
	/**
	 * Devuelve el valor de parametro de tipo string.
	 * @param param .
	 * @return .
	 * @throws UVException .
	 */
	public String getValueString(String param) {
		return (String) this.values.get(param);
	}
	
	/**
	 * Devuelve el valor de parametro de tipo integer.
	 * @param param .
	 * @return .
	 * @throws UVException .
	 */
	public Integer getValueInteger(String param) {
		return (Integer) this.values.get(param);
	}
	
	/**
	 * Devuelve el valor de parametro de tipo date.
	 * @param param .
	 * @return .
	 * @throws UVException .
	 */
	public Date getValueDate(String param) {
		return (Date) this.values.get(param);
	}
	
	/**
	 * Devuelve el valor del parámetro de tipo float.
	 * @param param .
	 * @return .
	 * @throws UVException .
	 */
	public Float getValueFloat(String param) {
		return (Float) this.values.get(param);
	}
	
	/**
	 * Devuelve el valor de parametro de tipo boolean.
	 * @param param .
	 * @return .
	 * @throws UVException .
	 */
	public Boolean getValueBoolean(String param) {
		return (Boolean) this.values.get(param);
	}
		
	/**
	 * Añade un párametro que será validado por la rules.
	 * @param param .
	 * @param type .
	 * @throws UVException .
	 */
	private void addParam(String param, String type) throws UVException {
		Object value;
		this.params.put(param, type);
		
		switch (type) {
		case PARAM_STRING:
			value = EscapaHTML.ajustaCodificacion(this.request.getParameter(param));			
			break;
		case PARAM_INTEGER:
			value = Formateador.leeParametroInteger(this.request.getParameter(param));
			break;
		case PARAM_FLOAT:
			value = BolsaEmpleoUtils.leeParametroFloat(this.request.getParameter(param));
			break;
		case PARAM_BOOLEAN:
			value = Formateador.leeParametroBoolean(this.request.getParameter(param), "1", "0");
			break;
		case PARAM_DATE:
			try {
				value = Formateador.leeParametroFecha(this.request.getParameter(param), Formateador.FORMATO_FECHA_DDMMYYYY, "/");
			} catch (UVException err) {
				value = null;
			}
			break;				
		default: 
			throw new UVException(MENSAJE_ERROR_TIPOPARAMETRONOVALIDO);
		}
		
		this.values.put(param, value);
	}
	
	/**
	 * Añade una regla de validación sobre un parámetro.
	 * @param param .
	 * @param rule .
	 * @param mensajeError .
	 * @throws UVException .
	 */
	public void addRule(String param, String rule, String mensajeError) throws UVException {
		String type = this.params.get(param);
		Object value = this.values.get(param);
		String[] ruleSplit = rule.split(":");
		
		// si ya hay error sobre un parametro no comprobamos más para no saturar los mensajes de error
		if (this.errors.containsKey(param)) {
			return;
		}
		
		switch (ruleSplit[0]) {
		case "required":
			this.checkRuleRequired(param, type, value, mensajeError);
			break;
		case "noBlank":
			this.checkRuleNoBlank(param, type, value, mensajeError);			
			break;
		case "max":
			if (ruleSplit.length != 2) {
				throw new UVException(MENSAJE_ERROR_REGLAREQUIEREPARAMETRO);
			}
			this.checkRuleMax(param, type, value, mensajeError, ruleSplit[1]);
			break;
		case "min":
			if (ruleSplit.length != 2) {
				throw new UVException(MENSAJE_ERROR_REGLAREQUIEREPARAMETRO);
			}
			this.checkRuleMin(param, type, value, mensajeError, ruleSplit[1]);
			break;	
		case "number":
			this.checkRuleNum(param, type, value, mensajeError);			
			break;	
		case "float":
			this.checkRuleFloat(param, type, value, mensajeError);			
			break;			
		case "select":
			this.checkSelect(param, type, value, mensajeError);			
			break;	
		default:
			throw new UVException(MENSAJE_ERROR_TIPOREGLANOVALIDA);			
		}
	}
	
	/**
	 * Comprueba si el request es válido.
	 * @return true o false si el request válido.
	 */
	public boolean isValid() {
		return this.errors.size() == 0;					
	}	
	
	public HashMap<String, ArrayList<String>> getErrors() {
		return this.errors;
	}
	
	private void addError(String param, String error) {
		ArrayList<String> errorsParam = this.errors.get(param);
		if (errorsParam == null) {
			errorsParam = new ArrayList<String>();
		}
		errorsParam.add(error);
		this.errors.put(param, errorsParam);
	}
	
	private Integer getParamInteger(String param) throws UVException {
		try {
			return Integer.parseInt(param);
		} catch (Exception e) {
			throw new UVException(MENSAJE_ERROR_PAMETRORULEENTERO);
		}
	}
	
	private Date getParamDate(String param) throws UVException {
		try {
			return Formateador.leeParametroFecha(param, Formateador.FORMATO_FECHA_DDMMYYYY, Formateador.FORMATO_FECHA_SEPARADOR_DIAS);			
		} catch (Exception e) {
			throw new UVException(MENSAJE_ERROR_PAMETRORULEENTERO);
		}
	}
	
	private void checkRuleRequired(String param, String type, Object value, String mensajeError) throws UVException {
		if (value == null) {
			this.addError(param, mensajeError);
		}
	}
	
	private void checkRuleNoBlank(String param, String type, Object value, String mensajeError) throws UVException {
		if (value != null) {			
			if (!"string".equals(type)) {
				throw new UVException(MENSAJE_ERROR_NOBLANKSTRING);
			}
			
			if (((String) value).isBlank()) {
				this.addError(param, mensajeError);				
			}
		}			
	}
	
	private void checkRuleMax(String param, String type, Object value, String mensajeError, String paramRule) throws UVException {
		if (value != null) {			
			switch (type) {
			case PARAM_STRING:		
				Integer paramRuleString = this.getParamInteger(paramRule);
				if (((String) value).length() > paramRuleString) {
					this.addError(param, mensajeError);
				}
				break;
			case PARAM_INTEGER:
				Integer paramRuleInteger = this.getParamInteger(paramRule);
				Integer valueCasting = (Integer) value;
				if (valueCasting > paramRuleInteger) {
					this.addError(param, mensajeError);
				}
				break;
			case PARAM_DATE:
				Date paramRuleDate = this.getParamDate(paramRule);
				Date valueCastingDate = (Date) value;
				if (valueCastingDate.after(paramRuleDate)) {
					this.addError(param, mensajeError);
				}				
				break; 
			default:
				throw new UVException(MENSAJE_ERROR_MAXSTRINGNUMERICOS);
			}	
		}			
	}
	
	private void checkRuleMin(String param, String type, Object value, String mensajeError, String paramRule) throws UVException {
		if (value != null) {
			switch (type) {
			case PARAM_STRING:	
				Integer paramRuleString = this.getParamInteger(paramRule);				
				if (((String) value).length() <= paramRuleString) {
					this.addError(param, mensajeError);
				}
				break;
			case PARAM_INTEGER:
				Integer paramRuleInteger = this.getParamInteger(paramRule);
				Integer valueCasting = (Integer) value;
				if (valueCasting <= paramRuleInteger) {
					this.addError(param, mensajeError);
				}
				break;
			case PARAM_DATE:
				Date paramRuleDate = this.getParamDate(paramRule);
				Date valueCastingDate = (Date) value;
				if (valueCastingDate.before(paramRuleDate)) {
					this.addError(param, mensajeError);
				}				
				break;
			default:
				throw new UVException(MENSAJE_ERROR_MINSTRINGNUMERICOS);
			}	
		}			
	}
	
	/**
	 * Checkea si un string es un numero o texto plano.
	 * @param param .
	 * @param type .
	 * @param value .
	 * @param mensajeError .
	 * @throws UVException .
	 */
	public void checkRuleNum(String param, String type, Object value, String mensajeError) throws UVException {
		if (value != null) {
			String regex = "[0-9]+";
	        Pattern p = Pattern.compile(regex);
	        Matcher m = p.matcher((String) value);

	        if (!m.matches()) {
	        	this.addError(param, mensajeError);		
	        }
		}	
	}
	
	private void checkRuleFloat(String param, String type, Object value, String mensajeError) throws UVException {
		if (value != null) {
			String regex = "[0-9]+(,[0-9]+)?";
	        Pattern p = Pattern.compile(regex);
	        Matcher m = p.matcher((String) value);

	        if (!m.matches()) {
	        	this.addError(param, mensajeError);		
	        }
		}
	}
	
	/**
	 * Checkea si un select no tiene ninguna opcion seleccionada.
	 * @param param .
	 * @param type .
	 * @param value .
	 * @param mensajeError .
	 * @throws UVException .
	 */
	public void checkSelect(String param, String type, Object value, String mensajeError) throws UVException {
		if (value != null) {
			if (value.equals("-1")) {
				throw new UVException(MENSAJE_ERROR_SELECTNEGATIVO);	
			}
			
			if (((String) value).isBlank()) {
				this.addError(param, mensajeError);				
			}
		}	
	}
}
