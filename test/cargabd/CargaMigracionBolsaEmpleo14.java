package cargabd;

import java.io.IOException;
import java.sql.SQLException;
import bbdd.BbddRunner;

/**
 * Migraciones 20211008 .
 *	- curso para la plaza .
 *	- cambido estado de tramitación a estado de aprobación .
 *
 * @author ATISoluciones 2021
 */
public class CargaMigracionBolsaEmpleo14 {
	
	/**
	 * Main .
	 * @param args .
	 */
	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/25-cursoacademicoplaza.sql");
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/26-estadoaprobacionplaza.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
