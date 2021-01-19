package controlador.implementacion;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

/** Configuracion filtro.
 *
 */
public class ConfiguracionFiltro implements FilterConfig {

	Map<String, String> parametros = new HashMap<>();

	/** set init parameter.
	 * @param name nombre
	 * @param value valor
	 */
	public void setInitParameter(String name, String value) {
		this.parametros.put(name, value);
	}
	
	@Override
	public String getFilterName() {
		
		return null;
	}

	@Override
	public String getInitParameter(String name) {
		return parametros.get(name);
	}

	@Override
	public Enumeration<String> getInitParameterNames() {
		
		return null;
	}

	@Override
	public ServletContext getServletContext() {
		
		return null;
	}

}
