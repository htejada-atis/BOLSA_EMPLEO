package bbdd;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;

/** utils para inicializar las bd.
 *
 */
public class UtilsInicializacionBd {
	
	/** inicializa la bd arcos.
	 * @throws IOException si error io
	 * @throws SQLException si error bd
	 */
	@Test
	public void inicializaBdArcos() throws IOException, SQLException {
		BbddRunner.inicializaBd();
	}
}
