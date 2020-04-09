package br.com.javana.gui;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.report.ReportConstants;
import br.com.javana.utils.MessageLoader;
import br.com.javana.utils.Utils;

public class ClientFileChooser extends AbstractChooser {

	private static final long serialVersionUID = 8388587586243028661L;
	private JPanel panel;
	private JButton btnPath;
	private JTextField txtSaveAs;
	private JLabel lblSave;
	private JLabel lblClient;
	private JComboBox cbClient;
	private String chosenClient;
	private String chosenPath;

	@Override
	protected void btnOKAction() throws BusinessException {
		chosenClient = this.cbClient.getSelectedItem().toString();
		chosenPath = this.txtSaveAs.getText();
		
		if(chosenPath == null || chosenPath.equals("")){
			throw new BusinessException(MessageLoader.getInstance().getMessage(84));
		}
	}

	@Override
	protected void initGUI() {
		this.setPreferredSize(new java.awt.Dimension(386, 145));
		this.setBounds(0, 0, 386, 145);
		this.setTitle("Escolha o cliente e o local onde o arquivo será salvo:");
		this.setClosable(true);
		setVisible(true);

		createPanel();
	}

	private void createPanel() {
		panel = new JPanel();
		TableLayout panelLayout = new TableLayout(new double[][] { { 10.0, 52.0, 228.0, TableLayout.FILL, 10.0 },
				{ 10.0, 24.0, 24.0, 24.0, 10.0 } });
		panelLayout.setHGap(5);
		panelLayout.setVGap(5);
		panel.setLayout(panelLayout);
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setPreferredSize(new java.awt.Dimension(381, 130));

		lblClient = new JLabel();
		panel.add(lblClient, "1, 1, r, f");
		lblClient.setText("Cliente:");

		String[] clients;
		try {
			clients = Utils.populateClientsCombo("É preciso cadastrar um cliente antes de executar o relatório!");
			if (clients != null) {
				ComboBoxModel cbClientModel = new DefaultComboBoxModel(clients);
				cbClient = new JComboBox();
				panel.add(cbClient, "2, 1, 3, 1");
				cbClient.setModel(cbClientModel);
			} else {
				cbClient = new JComboBox();
				panel.add(cbClient, "2, 1, 3, 1");
			}
		} catch (BusinessException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}

		btnOK = new JButton();
		panel.add(btnOK, "3, 3");
		btnOK.setText("OK");
		btnOK.addActionListener(this);

		lblSave = new JLabel();
		panel.add(lblSave, "1, 2, r, f");
		lblSave.setText("Salvar:");

		txtSaveAs = new JTextField();
		txtSaveAs.setEditable(false);
		panel.add(txtSaveAs, "2, 2");

		btnPath = new JButton();
		panel.add(btnPath, "3, 2");
		btnPath.setText("...");
		btnPath.addActionListener(this);

	}

	@Override
	protected void setParams() {
		params.put(ReportConstants.CLIENT_NAME_PARAMETER, chosenClient);
		params.put(ReportConstants.PATH_TO_XLS, chosenPath);
		params.put(ReportConstants.OUTPUT_NAME, "Geral por Empresa");
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		if (e.getSource().equals(btnPath)) {
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.showDialog(this, "OK");
			txtSaveAs.setText(fc.getSelectedFile().getAbsolutePath());
		}
	}

}
