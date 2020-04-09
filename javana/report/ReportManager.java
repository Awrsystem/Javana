package br.com.javana.report;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import br.com.javana.business.BusinessDelegate;
import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.BaseDTO;
import br.com.javana.utils.MessageLoader;

public class ReportManager {
	private static ReportManager instance = new ReportManager();

	private Map<String, JasperReport> compiledReports = new HashMap<String, JasperReport>();

	public static ReportManager getInstance() {
		return instance;
	}

	public void processReport(String selectedReport, Map<String, String> params) throws BusinessException {
		try {

			JasperReport report = this.getCompiledReport(selectedReport);
			JRBeanCollectionDataSource ds = this.getReportData(selectedReport, params);
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, ds);
			if (params.containsKey(ReportConstants.PATH_TO_XLS)) {
				createXLS(jasperPrint, params.get(ReportConstants.PATH_TO_XLS), params.get(ReportConstants.OUTPUT_NAME));
			} else {
				JasperViewer.viewReport(jasperPrint, false);
			}

		} catch (JRException e) {
			throw new BusinessException(MessageLoader.getInstance().getMessage(71));
		}
	}

	private void createXLS(JasperPrint print, String path, String output) throws JRException {
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setParameter(net.sf.jasperreports.engine.JRExporterParameter.JASPER_PRINT, print);
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, java.lang.Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, java.lang.Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, java.lang.Boolean.TRUE);

		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date(System.currentTimeMillis());
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, new File(path + File.separator
				+ output + " - " + df.format(date) + ".xls").getAbsolutePath());
		exporter.exportReport();
		JOptionPane.showMessageDialog(null, "Relatório gerado com sucesso!");
	}

	public void processReport(String selectedReport, List<? extends BaseDTO> data) throws BusinessException {
		try {
			JasperReport report = this.getCompiledReport(selectedReport);
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(data);
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, new HashMap<String, String>(), ds);
			JasperViewer.viewReport(jasperPrint, false);
		} catch (JRException e) {
			throw new BusinessException(MessageLoader.getInstance().getMessage(66));
		}
	}

	private JRBeanCollectionDataSource getReportData(String selectedReport, Map<String, String> params)
			throws BusinessException {
		List<? extends BaseDTO> data = null;

		if (selectedReport.equals(ReportConstants.CLIENT_HISTORY)
				|| selectedReport.equals(ReportConstants.CLIENT_HISTORY_BY_DEPARTMENT)
				|| selectedReport.equals(ReportConstants.CLIENT_HISTORY_XLS)
				|| selectedReport.equalsIgnoreCase(ReportConstants.CLIENT_HISTORY_BY_DEPARTMENT_MONTH_YEAR_XLS)
				|| selectedReport.equals(ReportConstants.CLIENT_HISTORY_BY_DEPARTMENT_XLS)
				|| selectedReport.equals(ReportConstants.CLIENT_HISTORY_BY_DEPARTMENT_MONTH_YEAR)) {
			data = BusinessDelegate.getInstance().getBox().findBoxesByParams(params);
		}

		if (selectedReport.equals(ReportConstants.BOXES_OUT)
				|| selectedReport.equals(ReportConstants.BOXES_OUT_BY_CLIENT)
				|| selectedReport.equals(ReportConstants.BOXES_OUT_BY_CLIENT_AND_DEPARTMENT)
				|| selectedReport.equals(ReportConstants.BOXES_OUT_BY_CLIENT_AND_DEPARTMENT_AND_DATE)) {
			data = BusinessDelegate.getInstance().getBoxSubprotocol().findUnavailableBoxes(params);
		}

		if (selectedReport.equals(ReportConstants.REMOVED_BOXES_REPORT)) {
			try {
				data = BusinessDelegate.getInstance().getRemovedBox().findByParams(params);
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}

		if (selectedReport.equals(ReportConstants.LABELS) || (selectedReport.equals(ReportConstants.N_BOXES_REPORT))) {
			try {
				data = BusinessDelegate.getInstance().getBox().findByInterval(
						new Long(params.get(ReportConstants.FROM_PARAMETER)),
						new Long(params.get(ReportConstants.TO_PARAMETER)));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		if (selectedReport.equals(ReportConstants.BARCODE)) {
			List<Long> codes = new ArrayList<Long>();
			try {
				Long amount = Long.parseLong(JOptionPane.showInputDialog(null,
						"Quantas etiquetas você deseja imprimir?"));
				if (amount == 0)
					throw new NumberFormatException();

				for (int i = 0; i < amount; i++) {
					Long number = Long.parseLong(JOptionPane.showInputDialog(null, "Digite um número: "));
					codes.add(number);
				}
				data = BusinessDelegate.getInstance().getBox().findBySet(codes);
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null, "Digite um número maior que zero!");
			}

		}

		if (selectedReport.equals(ReportConstants.BOXES_COUNT)) {
			data = BusinessDelegate.getInstance().getBox().boxCountByClient();
		}

		if (selectedReport.equals(ReportConstants.PRINT_PROTOCOL)) {
			data = BusinessDelegate.getInstance().getBoxSubprotocol().findProtocol(
					new Long(params.get(ReportConstants.PROTOCOL_NUMBER)));
		}

		if (selectedReport.equals(ReportConstants.BOX_HISTORY)) {
			data = BusinessDelegate.getInstance().getBoxSubprotocol().boxHistory(
					new Long(params.get(ReportConstants.BOX_NUMBER)));
		}

		if (selectedReport.equals(ReportConstants.BOX_PROTOCOL)) {
			data = BusinessDelegate.getInstance().getBoxSubprotocol().backBetweenDates(
					params.get(ReportConstants.FROM_DATE_PARAMETER), params.get(ReportConstants.TO_DATE_PARAMETER));
		}

		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(data);

		return ds;
	}

	private JasperReport getCompiledReport(String selectedReport) throws JRException {
		JasperReport rep = compiledReports.get(selectedReport);
		if (rep == null) {
			InputStream reportStream = this.getClass().getClassLoader().getResourceAsStream(selectedReport);

			try {
				JasperDesign jasperDesign = JRXmlLoader.load(reportStream);
				JasperReport report = JasperCompileManager.compileReport(jasperDesign);
				return report;
			} catch (JRException e) {
				e.printStackTrace();
				throw e;
			}
		} else {
			return rep;
		}
	}

}
