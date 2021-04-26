package es.ujaen.uvirtual.utilidades;

/**
 * Clase para validar el request.
 * @author ATISoluciones 2021 
 */
public class BolsaEmpleoSimpleValidator {	
	/**
	 * Constructor del validator.
	 */
	private BolsaEmpleoSimpleValidator() { }
	
	/**
	 * Chequea una rule.
	 * @param value .
	 * @param rule .
	 * @param errorMensaje .
	 * @throws UVException .
	 */
	public static void check(Object value, String rule, String errorMensaje) throws UVException {
		check(value, rule, errorMensaje, null);
	}
	
	/**
	 * Chequea una rule.
	 * @param value .
	 * @param rule .
	 * @param errorMensaje .
	 * @param ruleParams .
	 * @throws UVException .
	 */
	public static void check(Object value, String rule, String errorMensaje, Object ruleParams) throws UVException {
		switch (rule) {
		case "required":
			checkRuleRequired(value, errorMensaje);
			break;
		case "max":
			checkRuleMax(value, errorMensaje, ruleParams);
			break;
		case "in":
			checkRuleIn(value, errorMensaje, ruleParams);
			break;
		}
		
	}

	private static void checkRuleRequired(Object value, String errorMensaje) throws UVException {
		if (value.getClass() == String.class) {
			if (value == null || ((String) value).isBlank()) {
				throw new UVException(errorMensaje);
			}
		} else {
			throw new UVException("No se chequear required con " + value.getClass());
		}		
	}
	
	private static void checkRuleMax(Object value, String errorMensaje, Object params) throws UVException {
		Integer max = (Integer) params;
		
		if (value.getClass() == String.class) {
			if (value != null && ((String) value).length() > max) {
				throw new UVException(errorMensaje);
			}
		} else {
			throw new UVException("No se chequear max con " + value.getClass());
		}		
	}
	
	private static void checkRuleIn(Object value, String errorMensaje, Object params) throws UVException {
		if (value.getClass() == String.class) {
			if (value != null) {
				boolean find = false;
				for (String param : (String[]) params) {
					if (param.equals((String) value)) {
						find = true;
						break;
					}					
				}
				if (!find) {
					throw new UVException(errorMensaje);
				}
			}			
		} else {
			throw new UVException("No se chequear max con " + value.getClass());
		}
	}
}
