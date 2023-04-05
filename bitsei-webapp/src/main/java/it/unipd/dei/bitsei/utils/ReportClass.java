package it.unipd.dei.bitsei.utils;


import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * Utils class for Jasper Reports library usage.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class ReportClass {

    public static void exportReport(List<?> l, String path, String jrxmlPath, String fileOutputName, Map<String, Object> map) throws FileNotFoundException, JRException {
        File f = new File(jrxmlPath);
        JasperReport jasperReport = JasperCompileManager.compileReport(f.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(l);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, path + "/pdf/" + fileOutputName);
    }


}
