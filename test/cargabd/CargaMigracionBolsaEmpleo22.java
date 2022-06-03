package cargabd;

import java.io.IOException;
import java.sql.SQLException;
import bbdd.BbddRunner;

/**
 * Migraciones 20220602 .
 *	- estado para los meritos preferentes del candidato .
 *
 * @author ATISoluciones 2021
 */
public class CargaMigracionBolsaEmpleo22 {
	/**
	 * Main .
	 * @param args .
	 */
	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/34-acreditacionesestado.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
