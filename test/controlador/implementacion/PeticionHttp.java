package controlador.implementacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import es.ujaen.uvirtual.beans.UVDatos;

/** petitionHttp.
 *
 */
public class PeticionHttp implements HttpServletRequest {

	Map<String, String> parametros = new HashMap<>();
	Map<String, Object> atributos = new HashMap<>();
	String requestURI = null;
	SesionHttp sesion = null;
	
	/** set UVDatos.
	 * @param datos datos UV
	 */
	public void setUVDatos(UVDatos datos) {
		this.setAttribute(UVDatos.NOMBRE_ATRIBUTO, datos);
	}
	
	public UVDatos getUVDatos() {
		return (UVDatos) this.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
	}

	/** set parameter.
	 * @param clave clave del parametro
	 * @param valor valor del parametro
	 */
	public void setParameter(String clave, String valor) {
		parametros.put(clave, valor);
	}
	
	@Override
	public AsyncContext getAsyncContext() {
		
		return null;
	}

	@Override
	public Object getAttribute(String name) {
		return atributos.get(name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		
		return null;
	}

	@Override
	public String getCharacterEncoding() {
		
		return null;
	}

	@Override
	public int getContentLength() {
		
		return 0;
	}

	@Override
	public long getContentLengthLong() {
		
		return 0;
	}

	@Override
	public String getContentType() {
		
		return null;
	}

	@Override
	public DispatcherType getDispatcherType() {
		
		return null;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		
		return null;
	}

	@Override
	public String getLocalAddr() {
		
		return null;
	}

	@Override
	public String getLocalName() {
		
		return null;
	}

	@Override
	public int getLocalPort() {
		
		return 0;
	}

	@Override
	public Locale getLocale() {
		
		return null;
	}

	@Override
	public Enumeration<Locale> getLocales() {
		
		return null;
	}

	@Override
	public String getParameter(String name) {
		
		return parametros.get(name);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> parametrosMap = new HashMap<>();
		for (Map.Entry<String, String> entry : parametros.entrySet()) {
			String[] valor = new String[1];
			valor[0] = entry.getValue();
			parametrosMap.put(entry.getKey(), valor);
		}
		return parametrosMap;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(parametros.keySet());
	}

	@Override
	public String[] getParameterValues(String name) {
		
		return null;
	}

	@Override
	public String getProtocol() {
		
		return null;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		
		return null;
	}

	@Override
	public String getRealPath(String path) {
		
		return null;
	}

	@Override
	public String getRemoteAddr() {
		
		return null;
	}

	@Override
	public String getRemoteHost() {
		
		return null;
	}

	@Override
	public int getRemotePort() {
		
		return 0;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		
		return null;
	}

	@Override
	public String getScheme() {
		
		return null;
	}

	@Override
	public String getServerName() {
		
		return "peticion http para junit";
	}

	@Override
	public int getServerPort() {
		
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		
		return new ContextoServlet();
	}

	@Override
	public boolean isAsyncStarted() {
		
		return false;
	}

	@Override
	public boolean isAsyncSupported() {
		
		return false;
	}

	@Override
	public boolean isSecure() {
		
		return false;
	}

	@Override
	public void removeAttribute(String name) {
		//
		
	}

	@Override
	public void setAttribute(String name, Object o) {
		atributos.put(name, o);
	}

	@Override
	public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
		//
		
	}

	@Override
	public AsyncContext startAsync() throws IllegalStateException {
		return null;
	}

	@Override
	public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
			throws IllegalStateException {
		return null;
	}

	@Override
	public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
		return false;
	}

	@Override
	public String changeSessionId() {
		return null;
	}

	@Override
	public String getAuthType() {
		return null;
	}

	@Override
	public String getContextPath() {
		return null;
	}

	@Override
	public Cookie[] getCookies() {
		return null;
	}

	@Override
	public long getDateHeader(String name) {
		return 0;
	}

	@Override
	public String getHeader(String name) {
		return null;
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		return null;
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		return null;
	}

	@Override
	public int getIntHeader(String name) {
		return 0;
	}

	@Override
	public String getMethod() {
		return null;
	}

	@Override
	public Part getPart(String name) throws IOException, ServletException {
		return null;
	}

	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		return null;
	}

	@Override
	public String getPathInfo() {
		return null;
	}

	@Override
	public String getPathTranslated() {
		return null;
	}

	@Override
	public String getQueryString() {
		return null;
	}

	@Override
	public String getRemoteUser() {
		return null;
	}

	public void setRequestURI(String uri) {
		this.requestURI = uri;
	}

	@Override
	public String getRequestURI() {
		return this.requestURI;
	}

	@Override
	public StringBuffer getRequestURL() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("https://localhost:8080/");
		buffer.append(this.requestURI);
		return buffer;
	}

	@Override
	public String getRequestedSessionId() {
		return null;
	}

	@Override
	public String getServletPath() {
		return null;
	}

	@Override
	public HttpSession getSession() {
		if (this.sesion != null) {
			return this.sesion;
		} else {
			return getSession(true);
		}
    }

	@Override
	public HttpSession getSession(boolean create) {
		if (create) {
			this.sesion = new SesionHttp();
		}
		return getSession();
	}

	@Override
	public Principal getUserPrincipal() {
		return null;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		return false;
	}

	@Override
	public boolean isUserInRole(String role) {
		return false;
	}

	@Override
	public void login(String username, String password) throws ServletException {
		//
	}

	@Override
	public void logout() throws ServletException {
		//
	}

	@Override
	public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
		return null;
	}

}
