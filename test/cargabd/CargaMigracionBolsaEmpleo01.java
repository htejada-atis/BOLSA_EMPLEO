package cargabd;

import java.io.IOException;
import java.sql.SQLException;

import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;

/**
 * Migraciones 20210623 .
 * 	- excluir solicitud
 * 	- ajustes mensajería
 * 
 * @author ATISoluciones 2021
 */
public class CargaMigracionBolsaEmpleo01 {

	public static void main(String[] args) {
		BbddRunner.conectarBd();
		try {
			BbddRunner.ejecutar("Documentos/scripts/opc.bolsaempleo/migraciones/01-exclusionsolicitud.sql");			
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
