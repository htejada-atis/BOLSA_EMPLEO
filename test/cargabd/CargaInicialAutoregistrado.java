package cargabd;

import java.io.IOException;
import java.sql.SQLException;

import bbdd.BbddRunner;

/** Carga inicial autoregistrado.
 *
 */
public class CargaInicialAutoregistrado {
	
	private CargaInicialAutoregistrado() {
		//no se puede instanciar
	}

	/** main.
	 * @param args parametros
	 */
	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutarArcos("Documentos/scripts/opc.autoaprovisionado/Desarrollo/dropTables.sql");
			BbddRunner.ejecutarArcos("Documentos/scripts/opc.autoaprovisionado/01-createTable.sql");
			BbddRunner.ejecutarArcos("Documentos/scripts/opc.autoaprovisionado/02-createTrigger.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}

	}
}
