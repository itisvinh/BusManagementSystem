package com.busmanagementsystem.Interface;

import javafx.concurrent.Task;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import com.busmanagementsystem.Database.Configs.DBConnection;

import java.sql.DriverManager;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

public class PrintTicket {
    Task<Void> task = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            HashMap parameters = new HashMap();
            JasperPrint jp = JasperFillManager.fillReport("Report/ticket.jasper", parameters, DriverManager.getConnection());
            JasperViewer viewer = new JasperViewer(jp, false);
            viewer.setVisible(true);
            return null;
        }
    };
    ExecutorService service = ExecutorService.newCachedThreadPool();
    service.execute(task);
    service.shutdown();
}
