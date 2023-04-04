package it.unipd.dei.bitsei.utils;

import it.unipd.dei.bitsei.resources.Customer;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerReport {

    public static String exportCustomerReport(List<Customer> lc, String path) throws FileNotFoundException, JRException {
        File f = new File("webapps/bitsei-1.0/jrxml/CustomerList.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(f.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lc);
        Map<String, Object> map = new HashMap<>();
        map.put("proudlyCreatedBy", "Dott. Mirco CAZZARO");
        map.put("banner", path + "jrxml\\leaf_banner_green.png");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, path + "/pdf/customers_report.pdf");
        return "";
    }
}
