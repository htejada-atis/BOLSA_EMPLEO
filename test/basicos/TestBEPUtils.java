package basicos;

import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertThrows;
//import static org.mockito.Mockito.when;

//import javax.servlet.http.Part;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
//import org.mockito.Mock;

import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;

/**
 * test utilidades bolsa empleo.
 * 
 * @author ATISoluciones
 */
// @RunWith(MockitoJUnitRunner.class)
public class TestBEPUtils {
	public static final int COUNT_1 = 1;
	public static final int COUNT_3 = 3;	
	
//	@Mock
//	private Part part;
	
	/**
	 * Tests para método obtenerNombreFichero.
	 */
//	@Test
//	public void testA01() {
//		when(part.getHeader("content-disposition")).thenReturn("filename=\\\"Report.pdf\\\"");		
//		String file = BolsaEmpleoUtils.obtenerNombreFichero(part);
//		assertEquals("Report.pdf", file);
//
//		when(part.getHeader("content-disposition")).thenReturn("other");
//		String empty = BolsaEmpleoUtils.obtenerNombreFichero(part);
//		assertEquals("", empty);
//	}

	/**
	 * Tests método consultaMultiplesParametros.
	 */
	@Test
	public void testA02() {
		String query = BolsaEmpleoUtils.consultaMultiplesParametros(COUNT_1);
		long count = query.chars().filter(ch -> ch == '?').count();
		assertEquals(COUNT_1, count);

		query = BolsaEmpleoUtils.consultaMultiplesParametros(COUNT_3);
		count = query.chars().filter(ch -> ch == '?').count();
		assertEquals(COUNT_3, count);
	}
}
