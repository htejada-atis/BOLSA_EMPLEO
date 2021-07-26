package cargabd;

import java.io.IOException;
import java.sql.SQLException;
import bbdd.BbddRunner;

/**
 * Migraciones 20210714 .
 * 	- ítem de baremación en solicitud bolsas méritos .
 * 
 * @author ATISoluciones 2021
 */
public class CargaMigracionBolsaEmpleo04 {

	/**
	 * Main .
	 * @param args .
	 */
	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/05-itemsolicitudbolsasmeritos.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
