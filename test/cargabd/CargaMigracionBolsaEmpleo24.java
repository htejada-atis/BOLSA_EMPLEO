package cargabd;

import java.io.IOException;
import java.sql.SQLException;
import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;

/**
 * Migraciones 20240312 . 
 * - menu miembros de la comisión disponible para los candidatos .
 * - ajustes del nombre del menú miembros de la comisión . 
 * @author ATISoluciones 2024
 */
public class CargaMigracionBolsaEmpleo24 {
	/**
	 * Main .
	 * 
	 * @param args .
	 */
	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/36-menu-miembros-comision-candidatos.sql");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
