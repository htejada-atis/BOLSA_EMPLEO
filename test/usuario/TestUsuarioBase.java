package usuario;

import org.junit.After;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/** Clase de la que heredar los test de usuario.
 */
public class TestUsuarioBase {
	
	protected String nombrePrueba;
	
	/** Se ejecuta despues de cada test.
	 * Aqui grabamos una captura de pantalla
	 */
	@After
	public void capturaPantalla() {
		DriverUv.capturaPantalla(nombrePrueba);
	}		
	
	@Rule
	public TestRule watcher = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			nombrePrueba = description.getMethodName();
		}
	};
}
