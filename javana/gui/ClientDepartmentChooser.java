package br.com.javana.gui;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.report.ReportConstants;
import br.com.javana.utils.MessageLoader;
import br.com.javana.utils.Utils;

public class ClientDepartmentChooser extends AbstractChooser {
	private static final long serialVersionUID = 1L;

	private JLabel lblClient;

	private JComboBox cbClient;

	private JComboBox cbDepartment;

	private JLabel lblDepartment;

	private String chosenClient;

	private String chosenDepartment;

	protected void initGUI() {
		this.setPreferredSize(new java.awt.Dimension(388, 139));
		this.setBounds(0, 0, 388, 139);
		setVisible(true);
		this.setClosable(true);
		this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

		panel = new JPanel();
		TableLayout panelLayout = new TableLayout(new double[][] { { 10.0, 52.0, 228.0, TableLayout.FILL, 10.0 },
				{ 10.0, 24.0, 24.0, 24.0, 10.0 } });
		panelLayout.setHGap(5);
		panelLayout.setVGap(5);
		panel.setLayout(panelLayout);
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setPreferredSize(new java.awt.Dimension(383, 111));

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
				cbClient.addActionListener(this);
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

		lblDepartment = new JLabel();
		panel.add(lblDepartment, "1, 2, r, f");
		lblDepartment.setText("Setor:");

		ComboBoxModel cbDepartmentModel = new DefaultComboBoxModel(new String[] { "Selecione" });
		cbDepartment = new JComboBox();
		panel.add(cbDepartment, "2, 2, 3, 1");
		cbDepartment.setModel(cbDepartmentModel);

	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		if (e.getSource().equals(cbClient)) {
			String[] departments;
			try {
				departments = Utils.populateDepartmentsCombo(this.cbClient.getSelectedItem().toString());
				this.cbDepartment.setModel(new DefaultComboBoxModel(departments));
			} catch (BusinessException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
		}
	}

	protected void btnOKAction() throws BusinessException {
		chosenClient = this.cbClient.getSelectedItem().toString();
		chosenDepartment = this.cbDepartment.getSelectedItem().toString();
		if (chosenClient.equals("Selecione")) {
			throw new BusinessException(MessageLoader.getInstance().getMessage(10));
		} else {
			if (chosenDepartment.equals("Selecione")) {
				throw new BusinessException(MessageLoader.getInstance().getMessage(11));
			}
		}
	}

	@Override
	protected void setParams() {
		params.put(ReportConstants.CLIENT_NAME_PARAMETER, chosenClient);
		params.put(ReportConstants.DEPARTMENT_NAME_PARAMETER, chosenDepartment);
	}
}
