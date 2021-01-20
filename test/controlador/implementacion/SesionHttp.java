package controlador.implementacion;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

/** sesion simulada para junit.
 *
 */
public class SesionHttp implements HttpSession {
	
	private String sessionId = "23149874";
	Map<String, Object> atributos = new HashMap<>();
	

	@Override
	public Object getAttribute(String name) {
		return atributos.get(name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		
		return null;
	}

	@Override
	public long getCreationTime() {
		
		return 0;
	}

	@Override
	public String getId() {
		return this.sessionId;
	}

	@Override
	public long getLastAccessedTime() {
		
		return 0;
	}

	@Override
	public int getMaxInactiveInterval() {
		
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		
		return null;
	}

	/** get session context.
	 * @deprecated deprecated
	 */
	@Override
	@SuppressWarnings("java:S1133")
	public javax.servlet.http.HttpSessionContext getSessionContext() {
		return null;
	}

	@Override
	public Object getValue(String name) {
		
		return null;
	}

	@Override
	public String[] getValueNames() {
		
		return new String[0];
	}

	@Override
	public void invalidate() {
		//
	}

	@Override
	public boolean isNew() {
		
		return false;
	}

	@Override
	public void putValue(String name, Object value) {
		//
	}

	@Override
	public void removeAttribute(String name) {
		//
	}

	@Override
	public void removeValue(String name) {
		//
	}

	@Override
	public void setAttribute(String name, Object value) {
		atributos.put(name, value);
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		//
	}

}
