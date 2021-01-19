package controlador.implementacion;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/** Cadena de filtros para pruebas.
 */
public class CadenaFiltros implements FilterChain {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
		return;
	}
}
