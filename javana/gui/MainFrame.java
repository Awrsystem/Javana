package br.com.javana.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import br.com.javana.business.BusinessDelegate;
import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.report.ReportConstants;
import br.com.javana.report.ReportManager;
import br.com.javana.utils.MessageLoader;
import br.com.javana.utils.Utils;

/**
 * 
 * Janela principal da aplicação
 */
public class MainFrame extends JFrame implements ActionListener, Observer {
	private static final long serialVersionUID = 1L;

	/**
	 * Barra de Menu principal
	 */
	private JMenuBar jmbMenuBar;

	/**
	 * Menu Cadastro
	 */
	private JMenu jmRegistration;

	/**
	 * Menu Ferramentas
	 */
	private JMenu jmTools;

	/**
	 * Menu Relatórios
	 */
	private JMenu jmReports;

	/**
	 * Item de menu Cadastro -> Cliente
	 */
	private JMenuItem jmiClientRegistration;

	/**
	 * Item de menu Cadastro -> Caixa
	 */
	private JMenuItem jmiBoxRegistration;

	/**
	 * Item de menu Cadastro -> Protocolo
	 */
	private JMenuItem jmiProtocolRegistration;

	/**
	 * Item de Menu Cadastro -> Departamento
	 */
	private JMenuItem jmiDepartmentRegistration;

	/**
	 * Item de menu Cadastro -> Setor x Cliente
	 */
	private JMenuItem jmiDepartmentClient;

	/**
	 * Item de menu Ferramentas -> Números Perdidos
	 */
	private JMenuItem jmiToolsBoxLostNumbers;

	/**
	 * Item de menu Ferramentas -> Números Perdidos
	 */
	private JMenuItem jmiToolsProtocolLostNumbers;

	/**
	 * Item do menu ferramentas -> Baixa Protocolos - Manual
	 */
	private JMenuItem jmiToolsCloseProtocolManually;

	/**
	 * Item do menu ferramentas -> Baixa Protocolos
	 */
	private JMenuItem jmiToolsCloseProtocolAuto;

	/**
	 * Painel onde as janelas internas são posicionadas
	 */
	private JDesktopPane jdp;

	/**
	 * Relatórios -> Geral por Empresa
	 */
	private JMenuItem jmiClientHistoryReport;

	/**
	 * Relatórios -> Geral por Setor
	 */
	private JMenuItem jmiClientHistoryByDepartmentReport;

	/**
	 * Relatórios - Geral por setor e data
	 */
	private JMenuItem jmiClientByDepartmentMonthYrReport;

	/**
	 * Relatório - Caixas Retiradas
	 */
	private JMenuItem jmiBoxesOutReport;

	/**
	 * Relatório - Caixas Retiradas por Cliente
	 */
	private JMenuItem jmiBoxesOutByClientReport;

	/**
	 * Relatório - Caixas Retiradas por Cliente Setor
	 */
	private JMenuItem jmiBoxesOutByClientAndDeptReport;

	/**
	 * Relatório - Caixas em emprestimo por cliente setor e data
	 */
	private JMenuItem jmiBoxesOutByClientDeptAndDateReport;

	/**
	 * Relatório - Caixas Removidas
	 */
	private JMenuItem jmiRemovedBoxesReport;

	/**
	 * Ferramentas - Remoção de caixas
	 */
	private JMenuItem jmiToolsBoxRemoval;

	/**
	 * Ferramentas - Backup
	 */
	private JMenuItem jmiToolsBackup;

	/**
	 * Relatórios - Etiquetas
	 */
	private JMenuItem jmiLabelsReport;

	/**
	 * Relatórios - Total de caixas por cliente
	 */
	private JMenuItem jmiBoxCountByClientReport;

	/**
	 * Relatórios - Últimas N Caixas
	 */
	private JMenuItem jmiNBoxesReport;

	/**
	 * Relatórios - Impressão de Protocolo
	 */
	private JMenuItem jmiPrintProtocol;

	/**
	 * Relatórios - Histórico da Caixa
	 */
	private JMenuItem jmiBoxHistory;

	private JMenuItem jmiBarcode;

	private JMenuItem jmiBackByDate;
	private JMenu jmGeneral;
	private JMenu jmOut;
	private JMenu jmProtocol;
	private JMenu jmLabels;
	private JMenu jmBoxes;
	private JMenu jmExcel;
	//private JMenuItem jmiToolsFtp;

	/**
	 * Cadastro - Pessoas
	 */
	private JMenuItem jmiUserRegistration;
	private ClientChooser clientChoser;
	private ClientDepartmentChooser clientDepartmentChooser;
	private ClientDepartmentMonthYearChooser clientDeptMonthYearChooser;
	private LabelsIntervalChooser labelChooser;
	private ClientDepartmentDateChooser clientDepartmentDateChooser;
	private DateIntervalChooser dateIntervalChooser;
	private ClientFileChooser clientFileChooser;
	private String selectedReport;
	private ClientDepartmentFileChooser clientDepartmentFileChooser;
	private CDMYFileChooser clientDeptDateFileChooser;
	
	/**
	 * Geral por empresa em xls
	 */
	private JMenuItem jmiClientHistoryExcelReport;

	private JMenuItem jmExcelGeneral;

	private JMenuItem jmiClientHistoryByDepartmentExcelReport;

	private JMenuItem jmiClientHistoryByDepartmentDateExcelReport;

	public MainFrame() {
		super();
		initMainMenu();
	}

	/**
	 * Cria o menu principal
	 */
	private void initMainMenu() {
		jdp = new JDesktopPane();
		jmbMenuBar = new JMenuBar();
		jmbMenuBar.setPreferredSize(new java.awt.Dimension(521, 20));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setTitle("RG Arquivo");

		setJMenuBar(jmbMenuBar);
		initRegistrationMenu();
		initToolsMenu();
		initReportsMenu();
		jmbMenuBar.add(jmRegistration);
		jmbMenuBar.add(jmTools);
		jmbMenuBar.add(jmReports);

		pack();
		this.setSize(650, 500);
		this.setLocation(300, 200);
	}

	/**
	 * Cria o menu de ferramentas
	 */
	private void initToolsMenu() {
		jmTools = new JMenu();
		jmTools.setText("Ferramentas");
		initToolsMenuItens();

		jmTools.add(jmiToolsBoxLostNumbers);
		jmTools.add(jmiToolsProtocolLostNumbers);
		jmTools.add(jmiToolsCloseProtocolManually);
		jmTools.add(jmiToolsCloseProtocolAuto);
		jmTools.add(jmiToolsBoxRemoval);
		jmTools.add(jmiToolsBackup);
		//jmTools.add(jmiToolsFtp);
	}

	/**
	 * Cria o menu de Relatórios
	 */
	private void initReportsMenu() {
		jmReports = new JMenu();
		jmReports.setText("Relatórios");
		initReportMenuItens();

		jmGeneral.add(jmiClientHistoryReport);
		jmGeneral.add(jmiClientHistoryByDepartmentReport);
		jmGeneral.add(jmiClientByDepartmentMonthYrReport);

		jmOut.add(jmiBoxesOutReport);
		jmOut.add(jmiBoxesOutByClientReport);
		jmOut.add(jmiBoxesOutByClientAndDeptReport);
		jmOut.add(jmiBoxesOutByClientDeptAndDateReport);

		jmProtocol.add(jmiPrintProtocol);
		jmProtocol.add(jmiBackByDate);

		jmLabels.add(jmiLabelsReport);
		jmLabels.add(jmiBarcode);

		jmBoxes.add(jmiRemovedBoxesReport);
		jmBoxes.add(jmiBoxCountByClientReport);
		jmBoxes.add(jmiBoxHistory);
		jmBoxes.add(jmiNBoxesReport);
		
		jmExcelGeneral.add(jmiClientHistoryExcelReport);
		jmExcelGeneral.add(jmiClientHistoryByDepartmentExcelReport);
		jmExcelGeneral.add(jmiClientHistoryByDepartmentDateExcelReport);
		jmExcel.add(jmExcelGeneral);

		jmReports.add(jmGeneral);
		jmReports.add(jmOut);
		jmReports.add(jmBoxes);
		jmReports.add(jmProtocol);
		jmReports.add(jmLabels);
		jmReports.add(jmExcel);
	}

	/**
	 * Cria os subitens do menu relatorios
	 */
	private void initReportMenuItens() {
		jmGeneral = new JMenu("Geral");
		jmGeneral.addActionListener(this);
		
		jmExcelGeneral = new JMenu("Geral");
		jmExcelGeneral.addActionListener(this);

		jmOut = new JMenu("Empréstimo");
		jmOut.addActionListener(this);

		jmBoxes = new JMenu("Caixas");
		jmBoxes.addActionListener(this);

		jmProtocol = new JMenu("Protocolo");
		jmProtocol.addActionListener(this);

		jmLabels = new JMenu("Etiquetas");
		jmLabels.addActionListener(this);
		
		jmExcel = new JMenu("Excel");
		jmExcel.addActionListener(this);

		jmiClientHistoryReport = new JMenuItem();
		jmiClientHistoryReport.setText("Empresa");
		jmiClientHistoryReport.addActionListener(this);

		jmiClientHistoryByDepartmentReport = new JMenuItem();
		jmiClientHistoryByDepartmentReport.setText("Setor");
		jmiClientHistoryByDepartmentReport.addActionListener(this);

		jmiClientByDepartmentMonthYrReport = new JMenuItem();
		jmiClientByDepartmentMonthYrReport.setText("Setor e Data");
		jmiClientByDepartmentMonthYrReport.addActionListener(this);

		jmiBoxesOutReport = new JMenuItem();
		jmiBoxesOutReport.setText("Geral");
		jmiBoxesOutReport.addActionListener(this);

		jmiBoxesOutByClientReport = new JMenuItem();
		jmiBoxesOutByClientReport.setText("Cliente");
		jmiBoxesOutByClientReport.addActionListener(this);

		jmiBoxesOutByClientAndDeptReport = new JMenuItem();
		jmiBoxesOutByClientAndDeptReport.setText("Cliente e Setor");
		jmiBoxesOutByClientAndDeptReport.addActionListener(this);

		jmiBoxesOutByClientDeptAndDateReport = new JMenuItem();
		jmiBoxesOutByClientDeptAndDateReport.setText("Cliente, Setor e Data");
		jmiBoxesOutByClientDeptAndDateReport.addActionListener(this);

		jmiBackByDate = new JMenuItem();
		jmiBackByDate.setText("Protocolo de Retorno");
		jmiBackByDate.addActionListener(this);

		jmiRemovedBoxesReport = new JMenuItem();
		jmiRemovedBoxesReport.setText("Caixas Excluídas");
		jmiRemovedBoxesReport.addActionListener(this);

		jmiLabelsReport = new JMenuItem();
		jmiLabelsReport.setText("Geral");
		jmiLabelsReport.addActionListener(this);

		jmiBoxCountByClientReport = new JMenuItem();
		jmiBoxCountByClientReport.setText("Total de Caixas por Cliente");
		jmiBoxCountByClientReport.addActionListener(this);

		jmiPrintProtocol = new JMenuItem();
		jmiPrintProtocol.setText("Impressão de Protocolo");
		jmiPrintProtocol.addActionListener(this);

		jmiBoxHistory = new JMenuItem();
		jmiBoxHistory.setText("Histórico da Caixa");
		jmiBoxHistory.addActionListener(this);

		jmiBarcode = new JMenuItem();
		jmiBarcode.setText("Barras");
		jmiBarcode.addActionListener(this);

		jmiNBoxesReport = new JMenuItem();
		jmiNBoxesReport.setText("Relatório Parcial de Caixas");
		jmiNBoxesReport.addActionListener(this);
		
		jmiClientHistoryExcelReport = new JMenuItem();
		jmiClientHistoryExcelReport.setText("Empresa");
		jmiClientHistoryExcelReport.addActionListener(this);
		
		jmiClientHistoryByDepartmentExcelReport = new JMenuItem();
		jmiClientHistoryByDepartmentExcelReport.setText("Setor");
		jmiClientHistoryByDepartmentExcelReport.addActionListener(this);
		
		jmiClientHistoryByDepartmentDateExcelReport = new JMenuItem();
		jmiClientHistoryByDepartmentDateExcelReport.setText("Empresa, Setor e Data");
		jmiClientHistoryByDepartmentDateExcelReport.addActionListener(this);
	}

	/**
	 * Cria os subitens do menu ferramentas
	 */
	private void initToolsMenuItens() {
		jmiToolsBoxLostNumbers = new JMenuItem();
		jmiToolsBoxLostNumbers.setText("Números Perdidos - Caixas");
		jmiToolsBoxLostNumbers.addActionListener(this);

		jmiToolsProtocolLostNumbers = new JMenuItem();
		jmiToolsProtocolLostNumbers.setText("Números Perdidos - Protocolos");
		jmiToolsProtocolLostNumbers.addActionListener(this);

		jmiToolsCloseProtocolManually = new JMenuItem();
		jmiToolsCloseProtocolManually.setText("Baixa Protocolos - Manual");
		jmiToolsCloseProtocolManually.addActionListener(this);

		jmiToolsCloseProtocolAuto = new JMenuItem();
		jmiToolsCloseProtocolAuto.setText("Baixa Protocolos - Auto");
		jmiToolsCloseProtocolAuto.addActionListener(this);

		jmiToolsBoxRemoval = new JMenuItem();
		jmiToolsBoxRemoval.setText("Exclusão de Caixas");
		jmiToolsBoxRemoval.addActionListener(this);

		jmiToolsBackup = new JMenuItem();
		jmiToolsBackup.setText("Backup");
		jmiToolsBackup.addActionListener(this);
		
//		jmiToolsFtp = new JMenuItem("Backup na RG");
//		jmiToolsFtp.addActionListener(this);
	}

	/**
	 * Cria o menu de cadastro
	 */
	private void initRegistrationMenu() {
		jmRegistration = new JMenu();
		jmRegistration.setText("Cadastro");
		initRegistrationMenuItens();

		jmRegistration.add(jmiClientRegistration);
		jmRegistration.add(jmiDepartmentRegistration);
		jmRegistration.add(jmiDepartmentClient);
		jmRegistration.add(jmiBoxRegistration);
		jmRegistration.add(jmiProtocolRegistration);
		jmRegistration.add(jmiUserRegistration);
	}

	/**
	 * Cria os subitens do menu de cadastro
	 */
	private void initRegistrationMenuItens() {
		jmiClientRegistration = new JMenuItem();
		jmiClientRegistration.setText("Cliente");
		jmiClientRegistration.addActionListener(this);

		jmiDepartmentRegistration = new JMenuItem();
		jmiDepartmentRegistration.setText("Setor");
		jmiDepartmentRegistration.addActionListener(this);

		jmiBoxRegistration = new JMenuItem();
		jmiBoxRegistration.setText("Caixa");
		jmiBoxRegistration.addActionListener(this);

		jmiProtocolRegistration = new JMenuItem();
		jmiProtocolRegistration.setText("Protocolo");
		jmiProtocolRegistration.addActionListener(this);

		jmiDepartmentClient = new JMenuItem();
		jmiDepartmentClient.setText("Cliente x Setor");
		jmiDepartmentClient.addActionListener(this);

		jmiUserRegistration = new JMenuItem();
		jmiUserRegistration.setText("Pessoas");
		jmiUserRegistration.addActionListener(this);
	}

	/**
	 * Define as ações executadas quando os itens de menu são acionados
	 */
	public void actionPerformed(final ActionEvent e) {
		registrationMenuActions(e);
		toolsMenuActions(e);
		reportMenuActions(e);

		this.add(jdp);
	}

	private void reportMenuActions(final ActionEvent e) {
		if (e.getSource().equals(jmiClientHistoryReport)) {
			selectedReport = ReportConstants.CLIENT_HISTORY;
			this.clientChoser = new ClientChooser();
			clientChoser.registerObserver(this);
			jdp.add(this.clientChoser);
		}

		if (e.getSource().equals(jmiClientHistoryByDepartmentReport)) {
			selectedReport = ReportConstants.CLIENT_HISTORY_BY_DEPARTMENT;
			this.clientDepartmentChooser = new ClientDepartmentChooser();
			clientDepartmentChooser.registerObserver(this);
			jdp.add(this.clientDepartmentChooser);
		}

		if (e.getSource().equals(jmiClientByDepartmentMonthYrReport)) {
			selectedReport = ReportConstants.CLIENT_HISTORY_BY_DEPARTMENT_MONTH_YEAR;
			this.clientDeptMonthYearChooser = new ClientDepartmentMonthYearChooser();
			clientDeptMonthYearChooser.registerObserver(this);
			jdp.add(this.clientDeptMonthYearChooser);
		}

		if (e.getSource().equals(jmiBoxesOutReport)) {
			selectedReport = ReportConstants.BOXES_OUT;
			try {
				ReportManager.getInstance().processReport(selectedReport, new HashMap<String, String>());
			} catch (BusinessException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
		}

		if (e.getSource().equals(jmiBoxesOutByClientReport)) {
			selectedReport = ReportConstants.BOXES_OUT_BY_CLIENT;
			this.clientChoser = new ClientChooser();
			clientChoser.registerObserver(this);
			jdp.add(this.clientChoser);
		}

		if (e.getSource().equals(jmiBoxesOutByClientAndDeptReport)) {
			selectedReport = ReportConstants.BOXES_OUT_BY_CLIENT_AND_DEPARTMENT;
			this.clientDepartmentChooser = new ClientDepartmentChooser();
			clientDepartmentChooser.registerObserver(this);
			jdp.add(this.clientDepartmentChooser);
		}

		if (e.getSource().equals(jmiRemovedBoxesReport)) {
			selectedReport = ReportConstants.REMOVED_BOXES_REPORT;
			this.clientChoser = new ClientChooser();
			clientChoser.registerObserver(this);
			jdp.add(this.clientChoser);
		}

		if (e.getSource().equals(jmiLabelsReport)) {
			selectedReport = ReportConstants.LABELS;
			this.labelChooser = new LabelsIntervalChooser();
			labelChooser.registerObserver(this);
			jdp.add(this.labelChooser);
		}

		if (e.getSource().equals(jmiBoxCountByClientReport)) {
			selectedReport = ReportConstants.BOXES_COUNT;
			try {
				ReportManager.getInstance().processReport(selectedReport, new HashMap<String, String>());
			} catch (BusinessException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
		}

		if (e.getSource().equals(jmiBoxesOutByClientDeptAndDateReport)) {
			selectedReport = ReportConstants.BOXES_OUT_BY_CLIENT_AND_DEPARTMENT_AND_DATE;
			this.clientDepartmentDateChooser = new ClientDepartmentDateChooser();
			clientDepartmentDateChooser.registerObserver(this);
			jdp.add(this.clientDepartmentDateChooser);
		}

		if (e.getSource().equals(jmiPrintProtocol)) {
			selectedReport = ReportConstants.PRINT_PROTOCOL;

			try {
				Long number = Long.parseLong(JOptionPane.showInputDialog(this,
						"Qual o número do protocolo que você deseja imprimir?"));
				if (number == 0) {
					JOptionPane.showMessageDialog(this, "Digite um número!");
				} else {
					Map<String, String> param = new HashMap<String, String>();
					param.put(ReportConstants.PROTOCOL_NUMBER, number.toString());
					try {
						ReportManager.getInstance().processReport(selectedReport, param);
					} catch (BusinessException e1) {
						JOptionPane.showMessageDialog(this, e1.getMessage());
					}
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Digite um número!");
			}
		}

		if (e.getSource().equals(jmiBoxHistory)) {
			selectedReport = ReportConstants.BOX_HISTORY;

			try {
				Long number = Long.parseLong(JOptionPane.showInputDialog(this, "Qual o código da caixa?"));
				if (number == 0) {
					JOptionPane.showMessageDialog(this, "Digite um número!");
				} else {
					Map<String, String> param = new HashMap<String, String>();
					param.put(ReportConstants.BOX_NUMBER, number.toString());
					ReportManager.getInstance().processReport(selectedReport, param);
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Digite um número!");
			} catch (BusinessException e2) {
				JOptionPane.showMessageDialog(this, e2.getMessage());
			}
		}

		if (e.getSource().equals(jmiBarcode)) {
			selectedReport = ReportConstants.BARCODE;
			Map<String, String> param = new HashMap<String, String>();
			try {
				ReportManager.getInstance().processReport(selectedReport, param);
			} catch (BusinessException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
		}

		if (e.getSource().equals(jmiNBoxesReport)) {
			selectedReport = ReportConstants.N_BOXES_REPORT;
			this.labelChooser = new LabelsIntervalChooser();
			labelChooser.registerObserver(this);
			jdp.add(this.labelChooser);
		}

		if (e.getSource().equals(jmiBackByDate)) {
			selectedReport = ReportConstants.BOX_PROTOCOL;
			this.dateIntervalChooser = new DateIntervalChooser();
			dateIntervalChooser.registerObserver(this);
			jdp.add(this.dateIntervalChooser);
		}
		
		if (e.getSource().equals(jmiClientHistoryExcelReport)) {
			selectedReport = ReportConstants.CLIENT_HISTORY_XLS;
			this.clientFileChooser = new ClientFileChooser();
			clientFileChooser.registerObserver(this);
			jdp.add(this.clientFileChooser);
		}
		
		if (e.getSource().equals(jmiClientHistoryByDepartmentExcelReport)) {
			selectedReport = ReportConstants.CLIENT_HISTORY_BY_DEPARTMENT_XLS;
			this.clientDepartmentFileChooser = new ClientDepartmentFileChooser();
			clientDepartmentFileChooser.registerObserver(this);
			jdp.add(this.clientDepartmentFileChooser);
		}
		
		if (e.getSource().equals(jmiClientHistoryByDepartmentDateExcelReport)) {
			selectedReport = ReportConstants.CLIENT_HISTORY_BY_DEPARTMENT_MONTH_YEAR_XLS;
			this.clientDeptDateFileChooser = new CDMYFileChooser();
			clientDeptDateFileChooser.registerObserver(this);
			jdp.add(this.clientDeptDateFileChooser);
		}
	}

	private void toolsMenuActions(final ActionEvent e) {
		if (e.getSource().equals(jmiToolsBoxLostNumbers)) {
			Object[] options = { "Sim", "Não" };
			int answer = JOptionPane.showOptionDialog(this, "Calcular números?", "Confirme", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

			if (answer == 0) {
				try {
					BusinessDelegate.getInstance().getLostBox().calculateNumbers();
					JOptionPane.showMessageDialog(this, "Números calculados com sucesso!");
				} catch (BusinessException e1) {
					JOptionPane.showMessageDialog(this, e1.getMessage());
				}
			}
		}

		if (e.getSource().equals(jmiToolsProtocolLostNumbers)) {
			Object[] options = { "Sim", "Não" };
			int answer = JOptionPane.showOptionDialog(this, "Calcular números?", "Confirme", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

			if (answer == 0) {
				try {
					BusinessDelegate.getInstance().getLostProtocol().calculateNumbers();
					JOptionPane.showMessageDialog(this, "Números calculados com sucesso!");
				} catch (BusinessException e1) {
					JOptionPane.showMessageDialog(this, e1.getMessage());
				}
				JOptionPane.showMessageDialog(this, "Números calculados com sucesso!");
			}
		}

		if (e.getSource().equals(jmiToolsCloseProtocolManually)) {
			ReturnBox rb = new ReturnBox();
			jdp.add(rb);
		}

		if (e.getSource().equals(jmiToolsCloseProtocolAuto)) {
			ReturnBoxAutomatically rb = new ReturnBoxAutomatically();
			jdp.add(rb);
		}

		if (e.getSource().equals(jmiToolsBoxRemoval)) {
			BoxRemoval br = new BoxRemoval();
			jdp.add(br);
		}

		if (e.getSource().equals(jmiToolsBackup)) {
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.showDialog(this, "OK");

			try {
				Utils.backup(fc.getSelectedFile());
				JOptionPane.showMessageDialog(this, MessageLoader.getInstance().getMessage(72));
			} catch (BusinessException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
		}
//		if(e.getSource().equals(jmiToolsFtp)){
//			JFileChooser fc = new JFileChooser();
//			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
//			fc.setMultiSelectionEnabled(true);
//			fc.showDialog(this, "OK");
//			TransferFrame tf = new TransferFrame(fc.getSelectedFiles());
//			jdp.add(tf);
//		}
	}

	private void registrationMenuActions(final ActionEvent e) {
		if (e.getSource().equals(jmiClientRegistration)) {
			ClientRegistration cr = new ClientRegistration();
			jdp.add(cr);
		}

		if (e.getSource().equals(jmiDepartmentRegistration)) {
			DepartmentRegistration dr = new DepartmentRegistration();
			jdp.add(dr);
		}

		if (e.getSource().equals(jmiBoxRegistration)) {
			BoxRegistration br = new BoxRegistration();
			jdp.add(br);
		}

		if (e.getSource().equals(jmiProtocolRegistration)) {
			ProtocolRegistration pr = new ProtocolRegistration();
			jdp.add(pr);
		}

		if (e.getSource().equals(jmiDepartmentClient)) {
			DepartmentClientRelation dcr = new DepartmentClientRelation();
			jdp.add(dcr);
		}

		if (e.getSource().equals(jmiUserRegistration)) {
			UserRegistration ur = new UserRegistration();
			jdp.add(ur);
		}
	}

	public void update(final Map<String, String> data) {
		try {
			ReportManager.getInstance().processReport(selectedReport, data);
		} catch (BusinessException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	public void update(final String msg) {
		
	}
}
