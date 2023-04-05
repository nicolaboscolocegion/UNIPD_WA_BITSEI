package it.unipd.dei.bitsei.utils;

import it.unipd.dei.bitsei.resources.Customer;
import it.unipd.dei.bitsei.resources.Product;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportClass {

    public static void exportReport(List<?> l, String path, String jrxmlPath, String fileOutputName, Map<String, Object> map) throws FileNotFoundException, JRException {
        File f = new File(path + jrxmlPath);
        JasperReport jasperReport = JasperCompileManager.compileReport(f.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(l);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, path + "/pdf/" + fileOutputName);
    }

}
