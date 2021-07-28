package cargabd;

import java.io.IOException;
import java.sql.SQLException;
import bbdd.BbddRunner;

/**
 * Migraciones 20210726 .
 * 	- arreglados triggers de solicitud bolsas méritos .
 * 
 * @author ATISoluciones 2021
 */
public class CargaMigracionBolsaEmpleo09 {

	/**
	 * Main .
	 * @param args .
	 */
	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/11-solicitudbolsasmeritostriggers.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
