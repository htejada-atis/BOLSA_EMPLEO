package cargabd;

import java.io.IOException;
import java.sql.SQLException;

import bbdd.BbddRunner;

/** Carga inicial docentia.
 */
public class CargaInicialDocentia {
	
	private CargaInicialDocentia() {
		
	}

	/** Carga inicial docentia.
	 * @param args argumentos
	 */
	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.docentia/datos_desarrollo/dropTables.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.docentia/creartablasdocentia.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.docentia/creacionsecuenciasytriggers.sql");
			BbddRunner.insertMasivo("Documentos/scripts/opc.docentia/uv.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}

	}

}
