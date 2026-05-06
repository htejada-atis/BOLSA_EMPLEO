package es.ujaen.uvirtual.modulo.bolsaempleo.informes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Alegacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ArchivoAlegacionGeneral;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ArchivoAlegacionMerito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.BolsaResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ItemBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoResultado;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.SolMerBolAlegacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.modelo.ModeloResultados;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase para generar pdfs de alegaciones y de resolución de alegaciones.
 *
 * @author ATISoluciones (fusionado)
 */
public class GenerarAlegacionesPDF extends BolsaEmpleoPDFGenerator {

    private static final String NOMBREDEESTACLASE = GenerarAlegacionesPDF.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);

    private static final String PDF_NOMBRE = "RS%sC%s U%s";
    private static final String PDF_TITULO_ALEGACIONES = "Informe de alegaciones del área: %s";
    private static final String PDF_TITULO_RESOLUCION = "Informe de resolución de alegaciones del área: %s";

    /**
     * Generar PDF de alegaciones de los resultados de una solicitud para una bolsa.
     */
    @SuppressWarnings({ "checkstyle:executablestatementcount" })
    public static InputStream generarPDFAlegaciones(UsuarioBolsaEmpleo candidato, BolsaResultado bolsa,
            Convocatoria convocatoria, SolMerBolAlegacion solicitudMerBolAlegacion, Alegacion alegacion)
            throws UVException, SQLException {
        initPDFProperties();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (Document document = new Document()) {
            PdfWriter.getInstance(document, out);

            document.open();
            document.addAuthor(PDF_AUTHOR);
            document.addTitle(String.format(PDF_NOMBRE, candidato.getCodNum(), convocatoria.getCodNum(),
                    candidato.getPrsNif()));
            document.addCreationDate();

            document.add(new Paragraph(new Chunk(String.format(PDF_TITULO_ALEGACIONES,
                    bolsa.getArea().getDescripcion()), fontTitle)));

            document.add(new Paragraph("\n"));

            addCommonHeader(document, candidato, bolsa, convocatoria, alegacion);

            document.add(new Paragraph("\n"));

            generarMeritosEvaluadosPDF(bolsa, document);
            generarMeritosExcluidosPDF(bolsa, document);
            generarMeritosNoEvaluadosPDF(bolsa, document);

            document.add(new Paragraph("\n"));

            generarTitulacionesValidadas(bolsa, document);
            generarAcreditacionesValidadas(bolsa, document);

            generarAlegacionGeneralPDF(solicitudMerBolAlegacion, document);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
            LOGGER.log(Level.SEVERE, e.toString());
            throw new UVException(MENSAJE_ERROR_GENERANDO_PDF);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    /**
     * Generar PDF de resolución de alegaciones de los resultados de una solicitud para una bolsa.
     */
    @SuppressWarnings({ "checkstyle:executablestatementcount" })
    public static InputStream generarPDFResolucionAlegacion(UsuarioBolsaEmpleo candidato, BolsaResultado bolsa,
            Convocatoria convocatoria, SolMerBolAlegacion solicitudMerBolAlegacion, Alegacion alegacion)
            throws UVException, SQLException {
        initPDFProperties();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (Document document = new Document()) {
            PdfWriter.getInstance(document, out);

            document.open();
            document.addAuthor(PDF_AUTHOR);
            document.addTitle(String.format(PDF_NOMBRE, candidato.getCodNum(), convocatoria.getCodNum(),
                    candidato.getPrsNif()));
            document.addCreationDate();

            document.add(new Paragraph(new Chunk(String.format(PDF_TITULO_RESOLUCION,
                    bolsa.getArea().getDescripcion()), fontTitle)));

            document.add(new Paragraph("\n"));

            addCommonHeader(document, candidato, bolsa, convocatoria, alegacion);

            document.add(new Paragraph("\n"));

            // Resolución del Departamento
            document.add(new Paragraph("Resolución del Departamento", fontH12Bold));
            String resolucionDepartamento = "No se ha proporcionado una resolución del departamento.";
            if (alegacion != null && alegacion.getDesResolucionDep() != null && !alegacion.getDesResolucionDep().trim().isEmpty()) {
                resolucionDepartamento = alegacion.getDesResolucionDep();
            }
            document.add(new Paragraph(resolucionDepartamento, fontH10));
            document.add(new Paragraph("\n"));

            // Resolución Final
            document.add(new Paragraph("Resolución Final", fontH12Bold));
            String resolucionFinal = "No se ha proporcionado una resolución final.";
            if (alegacion != null && alegacion.getDesResolucionFinal() != null && !alegacion.getDesResolucionFinal().trim().isEmpty()) {
                resolucionFinal = alegacion.getDesResolucionFinal();
            }
            document.add(new Paragraph(resolucionFinal, fontH10));
            document.add(new Paragraph("\n"));

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, Formateador.getStackTrace(e));
            LOGGER.log(Level.SEVERE, e.toString());
            throw new UVException(MENSAJE_ERROR_GENERANDO_PDF);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private static void addCommonHeader(Document document, UsuarioBolsaEmpleo candidato, BolsaResultado bolsa,
            Convocatoria convocatoria, Alegacion alegacion) throws DocumentException {
        document.add(new Paragraph("Convocatoria: " + convocatoria.getDescripcion(), fontBold));
        document.add(new Paragraph("Actualizado a fecha de: "
                + Formateador.formatoFecha(bolsa.getFechaBaremacionDefinitiva() != null
                        ? bolsa.getFechaBaremacionDefinitiva()
                        : bolsa.getFechaBaremacion(), Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS),
                fontBold));

        document.add(new Paragraph("Usuario: "
                + Formateador.leeParametroString(candidato.getPrsNif()) + " "
                + Formateador.leeParametroString(candidato.getNombre()) + " "
                + Formateador.leeParametroString(candidato.getPrimerApellido()) + " "
                + Formateador.leeParametroString(candidato.getSegundoApellido()), fontBold));

        document.add(new Paragraph("Dirección: "
                + Formateador.leeParametroString(candidato.getDireccion()) + " "
                + Formateador.leeParametroString(candidato.getCodigoPostal()) + " "
                + Formateador.leeParametroString(candidato.getLocalidad()) + " "
                + Formateador.leeParametroString(candidato.getProvincia()) + " "
                + Formateador.leeParametroString(candidato.getTelefono()), fontBold));

        if (alegacion != null) {
            if (alegacion.getFechaCreacion() != null) {
                document.add(new Paragraph("Fecha Creación Alegación: "
                        + Formateador.formatoFecha(alegacion.getFechaCreacion(),
                                Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS),
                        fontBold));
            }
            if (alegacion.getFechaConfirmacion() != null) {
                document.add(new Paragraph("Fecha Confirmación Alegación: "
                        + Formateador.formatoFecha(alegacion.getFechaConfirmacion(),
                                Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS),
                        fontBold));
            }

//            String fechaEnvioDepartamento = alegacion.getFechaEnvioDepartamento() != null
//                    ? Formateador.formatoFecha(alegacion.getFechaEnvioDepartamento(),
//                            Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS)
//                    : "Sin enviar";
//            document.add(new Paragraph("Fecha Envío a Departamento: " + fechaEnvioDepartamento, fontBold));
//
//            String fechaResolucionDepartamento = alegacion.getFechaResolucionDepartamento() != null
//                    ? Formateador.formatoFecha(alegacion.getFechaResolucionDepartamento(),
//                            Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS)
//                    : "Sin resolver";
//            document.add(new Paragraph("Fecha Resolución Departamento: " + fechaResolucionDepartamento, fontBold));
//
//            String fechaResolucionFinal = alegacion.getFechaResolucionFinal() != null
//                    ? Formateador.formatoFecha(alegacion.getFechaResolucionFinal(),
//                            Formateador.FORMATO_FECHA_DDMMYYYY_HHMMSS)
//                    : "Sin resolver";
//            document.add(new Paragraph("Fecha Resolución Final: " + fechaResolucionFinal, fontBold));
        }
    }

    private static void generarMeritosEvaluadosPDF(BolsaResultado bolsa, Document document)
            throws DocumentException {
        document.add(new Paragraph("\n"));
        document.add(new Paragraph(ModeloResultados.MERITOS_VALIDADOS, fontBold));
        document.add(new Paragraph("\n"));

        if (bolsa.getListaMeritos().size() > 0) {
            for (MeritoResultado merito : bolsa.getListaMeritos()) {
                ItemBaremacion itemBaremacion = merito.getItemMeritoSolicitud();

                boolean tieneDescripcion = merito.getDescripcionAlegacion() != null
                        && !merito.getDescripcionAlegacion().trim().isEmpty();
                boolean tieneFicheros = merito.getArchivosAlegacion() != null
                        && !merito.getArchivosAlegacion().isEmpty();

                if (!tieneDescripcion && !tieneFicheros) {
                    continue;
                }

                String codigoItem = itemBaremacion.getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "."
                        + itemBaremacion.getBloqueBaremacion().getCodigo() + "." + itemBaremacion.getCodigo();

                List<ArchivoAlegacionMerito> archivosAlegacion = merito.getArchivosAlegacion();
                StringBuilder ficherosAlegacionTexto = new StringBuilder();

                if (tieneFicheros) {
                    for (ArchivoAlegacionMerito archivo : archivosAlegacion) {
                        ficherosAlegacionTexto.append("[").append(archivo.getId()).append("] ")
                                .append(archivo.getNombre()).append("\n");
                    }
                } else {
                    ficherosAlegacionTexto.append("Sin ficheros asociados");
                }

                document.add(new Paragraph("ID del Mérito: " + merito.getCodNum(), fontH12Bold));
                document.add(new Paragraph("Código del Mérito: " + codigoItem, fontH10));
                document.add(new Paragraph("Tipo de Mérito: " + itemBaremacion.getNombre(), fontH10));
                document.add(new Paragraph("Descripción de la Alegación:", fontH10));
                document.add(new Paragraph(merito.getDescripcionAlegacion(), fontH8));
                document.add(new Paragraph("Ficheros de la Alegación:", fontH10));
                document.add(new Paragraph(ficherosAlegacionTexto.toString().trim(), fontH8));
                document.add(new Paragraph("\n"));
            }
        } else {
            document.add(new Paragraph(ModeloResultados.MENSAJE_SIN_MERITOS_EVALUADOS));
        }
    }

    private static void generarMeritosExcluidosPDF(BolsaResultado bolsa, Document document)
            throws DocumentException {
        document.add(new Paragraph("\n"));
        document.add(new Paragraph(ModeloResultados.MERITOS_EXCLUIDOS, fontBold));
        document.add(new Paragraph("\n"));

        if (bolsa.getListaMeritosExcluidos().size() > 0) {
            for (MeritoResultado merito : bolsa.getListaMeritosExcluidos()) {
                ItemBaremacion itemBaremacion = merito.getItemBaremacion();

                boolean tieneDescripcion = merito.getDescripcionAlegacion() != null
                        && !merito.getDescripcionAlegacion().trim().isEmpty();
                boolean tieneFicheros = merito.getArchivosAlegacion() != null
                        && !merito.getArchivosAlegacion().isEmpty();

                if (!tieneDescripcion && !tieneFicheros) {
                    continue;
                }

                String codigoItem = itemBaremacion.getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "."
                        + itemBaremacion.getBloqueBaremacion().getCodigo() + "." + itemBaremacion.getCodigo();

                List<ArchivoAlegacionMerito> archivosAlegacion = merito.getArchivosAlegacion();
                StringBuilder ficherosAlegacionTexto = new StringBuilder();

                if (archivosAlegacion != null && !archivosAlegacion.isEmpty()) {
                    for (ArchivoAlegacionMerito archivo : archivosAlegacion) {
                        ficherosAlegacionTexto.append("[").append(archivo.getId()).append("] ")
                                .append(archivo.getNombre()).append("\n");
                    }
                } else {
                    ficherosAlegacionTexto.append("Sin ficheros asociados");
                }

                document.add(new Paragraph("ID del Mérito: " + merito.getCodNum(), fontH12Bold));
                document.add(new Paragraph("Código del Mérito: " + codigoItem, fontH10));
                document.add(new Paragraph("Tipo de Mérito: " + itemBaremacion.getNombre(), fontH10));
                document.add(new Paragraph("Descripción de la Alegación:", fontH10));
                document.add(new Paragraph(merito.getDescripcionAlegacion(), fontH8));
                document.add(new Paragraph("Ficheros de la Alegación:", fontH10));
                document.add(new Paragraph(ficherosAlegacionTexto.toString().trim(), fontH8));
                document.add(new Paragraph("\n"));
            }
        } else {
            document.add(new Paragraph(ModeloResultados.MENSAJE_SIN_MERITOS_EXCLUIDOS));
        }
    }

    private static void generarMeritosNoEvaluadosPDF(BolsaResultado bolsa, Document document)
            throws DocumentException {
        document.add(new Paragraph("\n"));
        document.add(new Paragraph(ModeloResultados.MERITOS_NO_EVALUADOS, fontBold));
        document.add(new Paragraph("\n"));

        if (bolsa.getListaMeritosNoEvaluados().size() > 0) {
            for (MeritoResultado merito : bolsa.getListaMeritosNoEvaluados()) {
                ItemBaremacion itemBaremacion = merito.getItemBaremacion();

                boolean tieneDescripcion = merito.getDescripcionAlegacion() != null
                        && !merito.getDescripcionAlegacion().trim().isEmpty();
                boolean tieneFicheros = merito.getArchivosAlegacion() != null
                        && !merito.getArchivosAlegacion().isEmpty();

                if (!tieneDescripcion && !tieneFicheros) {
                    continue;
                }

                String codigoItem = itemBaremacion.getBloqueBaremacion().getApartadoBaremacion().getCodigo() + "."
                        + itemBaremacion.getBloqueBaremacion().getCodigo() + "." + itemBaremacion.getCodigo();

                List<ArchivoAlegacionMerito> archivosAlegacion = merito.getArchivosAlegacion();
                StringBuilder ficherosAlegacionTexto = new StringBuilder();

                if (archivosAlegacion != null && !archivosAlegacion.isEmpty()) {
                    for (ArchivoAlegacionMerito archivo : archivosAlegacion) {
                        ficherosAlegacionTexto.append("[").append(archivo.getId()).append("] ")
                                .append(archivo.getNombre()).append("\n");
                    }
                } else {
                    ficherosAlegacionTexto.append("Sin ficheros asociados");
                }

                document.add(new Paragraph("ID del Mérito: " + merito.getCodNum(), fontH12Bold));
                document.add(new Paragraph("Código del Mérito: " + codigoItem, fontH10));
                document.add(new Paragraph("Tipo de Mérito: " + itemBaremacion.getNombre(), fontH10));
                document.add(new Paragraph("Descripción de la Alegación:", fontH10));
                document.add(new Paragraph(merito.getDescripcionAlegacion(), fontH8));
                document.add(new Paragraph("Ficheros de la Alegación:", fontH10));
                document.add(new Paragraph(ficherosAlegacionTexto.toString().trim(), fontH8));
                document.add(new Paragraph("\n"));
            }
        } else {
            document.add(new Paragraph(ModeloResultados.MENSAJE_SIN_MERITOS_NO_EVALUADOS));
        }
    }

    private static void generarTitulacionesValidadas(BolsaResultado bolsa, Document document)
            throws DocumentException, SQLException, IOException {
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Titulaciones validadas", fontBold));

        String titulaciones = BolsaEmpleoUtils.clobToString(bolsa.getTitulacionesValidadas());
        document.add(new Paragraph(titulaciones.isEmpty() ? "No hay titulaciones validadas" : titulaciones));
    }

    private static void generarAcreditacionesValidadas(BolsaResultado bolsa, Document document)
            throws DocumentException, SQLException, IOException {
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Acreditaciones validadas", fontBold));

        String acreditaciones = BolsaEmpleoUtils.clobToString(bolsa.getAcreditacionesValidadas());
        document.add(new Paragraph(acreditaciones.isEmpty() ? "No hay acreditaciones validadas" : acreditaciones));
    }

    private static void generarAlegacionGeneralPDF(SolMerBolAlegacion solicitudMerBolAlegacion, Document document)
            throws DocumentException {
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Alegación General", fontBold));
        document.add(new Paragraph("\n"));

        String descripcion = solicitudMerBolAlegacion != null ? solicitudMerBolAlegacion.getDescripcion() : null;
        if (descripcion == null || descripcion.isEmpty()) {
            descripcion = "Sin descripción disponible";
        }

        List<ArchivoAlegacionGeneral> archivos = solicitudMerBolAlegacion != null
                ? solicitudMerBolAlegacion.getArchivosAlegacion()
                : null;
        StringBuilder ficherosAlegacionTexto = new StringBuilder();
        if (archivos != null && !archivos.isEmpty()) {
            for (ArchivoAlegacionGeneral archivo : archivos) {
                ficherosAlegacionTexto.append("[").append(archivo.getId()).append("] ")
                        .append(archivo.getNombre()).append("\n");
            }
        } else {
            ficherosAlegacionTexto.append("Sin ficheros asociados");
        }

        document.add(new Paragraph("Descripción de la Alegación General:", fontH10));
        document.add(new Paragraph(descripcion, fontH8));

        document.add(new Paragraph("Ficheros de la Alegación General:", fontH10));
        document.add(new Paragraph(ficherosAlegacionTexto.toString().trim(), fontH8));
        document.add(new Paragraph("\n"));
    }
}
