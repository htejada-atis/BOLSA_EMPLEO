package usuario;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/** ejecutar todos los tests. */
@RunWith(Suite.class)
@SuiteClasses({ TestConvocatoriaCrud.class })
public class AllTests {
	
	private AllTests() { }
	
	/** inicializacion. */
	@BeforeClass
	public static void setUp() {
		System.out.println("Inicio all tests");
	}

	/** finalizacion. */
	@AfterClass
	public static void tearDown() {
		System.out.println("fin all tests");
	}
}