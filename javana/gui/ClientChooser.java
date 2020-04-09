package br.com.javana.gui;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.report.ReportConstants;
import br.com.javana.utils.Utils;

public class ClientChooser extends AbstractChooser {
	private static final long serialVersionUID = 1L;

	private JLabel lblClient;

	private JComboBox cbClient;

	private String chosenClient;

	protected void initGUI() {
		this.setPreferredSize(new java.awt.Dimension(388, 105));
		this.setBounds(0, 0, 388, 105);
		this.setTitle("Escolha o cliente:");
		this.setClosable(true);
		setVisible(true);

		createPanel();
	}

	private void createPanel() {
		panel = new JPanel();
		TableLayout panelLayout = new TableLayout(new double[][] {
				{ 10.0, 52.0, 228.0, TableLayout.FILL, 10.0 },
				{ 10.0, 24.0, 24.0, 10.0 } });
		panelLayout.setHGap(5);
		panelLayout.setVGap(5);
		panel.setLayout(panelLayout);
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setPreferredSize(new java.awt.Dimension(452, 69));

		lblClient = new JLabel();
		panel.add(lblClient, "1, 1, r, f");
		lblClient.setText("Cliente:");

		String[] clients;
		try {
			clients = Utils
					.populateClientsCombo("É preciso cadastrar um cliente antes de executar o relatório!");
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
		panel.add(btnOK, "3, 2");
		btnOK.setText("OK");
		btnOK.addActionListener(this);
	}

	@Override
	protected void btnOKAction() {
		chosenClient = this.cbClient.getSelectedItem().toString();
	}

	@Override
	protected void setParams() {
		params.put(ReportConstants.CLIENT_NAME_PARAMETER, chosenClient);
	}
}
