package basicos;

import static org.junit.Assert.assertEquals;

import java.util.ResourceBundle;

import org.junit.Test;

/** Tests para propiedades.
 */
public class TestPropiedad {

	/** Obtiene una propiedad con datos.
	 */
	@Test
	public final void testA01() {
		String s1 = ResourceBundle.getBundle("mail").getString("mail.host");
		assertEquals("mail.ujaen.es", s1);
	}

}
