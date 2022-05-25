package cargabd;

import java.io.IOException;
import java.sql.SQLException;
import bbdd.BbddRunner;

/**
 * Migraciones 20220525 .
 *	- curso para las convocatorias .
 *
 * @author ATISoluciones 2021
 */
public class CargaMigracionBolsaEmpleo21 {
	/**
	 * Main .
	 * @param args .
	 */
	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/33-convocatoriascontracion.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
