package cargabd;

import java.io.IOException;
import java.sql.SQLException;

import bbdd.BbddRunner;
import bbdd.UtilsTestBolsaEmpleo;

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
