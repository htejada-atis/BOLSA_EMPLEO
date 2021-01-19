package controlador.implementacion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/** Cadena de filtros para pruebas.
 */
public class CadenaFiltrosEnlazada implements FilterChain {

	List<Filter> filtros = new ArrayList<>();
	int posicion = 0;
	@Override
	public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
		if (posicion < filtros.size()) {
			Filter filter = filtros.get(posicion);
			posicion++;
			filter.doFilter(request, response, this);
		}
	}
	
	/** incluye un filtro en la cadena.
	 * @param filtro filtro
	 */
	public void addFilter(Filter filtro) {
		filtros.add(filtro);
	}

}
