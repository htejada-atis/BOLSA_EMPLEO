package cargabd;

import java.io.IOException;
import java.sql.SQLException;
import bbdd.BbddRunner;

/**
 * Migraciones 20210720 .
 * 	- menu evaluadores para directores departamento .
 * 
 * @author ATISoluciones 2021
 */
public class CargaMigracionBolsaEmpleo06 {

	/**
	 * Main .
	 * @param args .
	 */
	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/08-directoresasignarevaluadores.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
