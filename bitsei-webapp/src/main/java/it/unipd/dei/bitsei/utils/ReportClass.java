package it.unipd.dei.bitsei.utils;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * Utils class for report generation.
 * This class is used to generate a report in PDF format.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public class ReportClass {

    /**
     * Empty constructor.
     */
    public ReportClass(){
        // empty constructor
    }

    /**
     * Generate a report in PDF format.
     *
     * @param l              the list of objects to be included in the report
     * @param path           the path of the report
     * @param jrxmlPath      the path of the jrxml file
     * @param fileOutputName the name of the output file
     * @param map            the map of the report
     * @throws FileNotFoundException if the file is not found
     * @throws JRException           if the report cannot be generated
     */
    public static void exportReport(List<?> l, String path, String jrxmlPath, String fileOutputName, Map<String, Object> map) throws FileNotFoundException, JRException {
        File f = new File(path + jrxmlPath);
        JasperReport jasperReport = JasperCompileManager.compileReport(f.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(l);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, path + "/pdf/" + fileOutputName);
    }
}
