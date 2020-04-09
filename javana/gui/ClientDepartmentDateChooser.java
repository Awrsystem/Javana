package br.com.javana.gui;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.report.ReportConstants;
import br.com.javana.utils.MessageLoader;
import br.com.javana.utils.Utils;

public class ClientDepartmentDateChooser extends AbstractChooser {
	private static final long serialVersionUID = 1L;
	private JLabel lblClient;
	private JComboBox cbClient;
	private JLabel lblFrom;
	private JFormattedTextField ftxtTo;
	private JFormattedTextField ftxtFrom;
	private JLabel lblTo;
	private JComboBox cbDepartment;
	private JLabel lblDepartment;
	private String chosenClient;
	private String chosenDepartment;
	private Date chosenFromDate;
	private Date chosenToDate;
	private SimpleDateFormat formatter;

	protected void initGUI() {
		this.setPreferredSize(new java.awt.Dimension(388, 197));
		this.setBounds(0, 0, 388, 197);
		setVisible(true);
		this.setClosable(true);

		panel = new JPanel();
		TableLayout panelLayout = new TableLayout(new double[][] { { 10.0, 52.0, 228.0, TableLayout.FILL, 10.0 },
				{ 10.0, 24.0, 24.0, 24.0, 24.0, 24.0, 10.0 } });
		panelLayout.setHGap(5);
		panelLayout.setVGap(5);
		panel.setLayout(panelLayout);
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setPreferredSize(new java.awt.Dimension(383, 172));

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
		} catch (BusinessException e1) {
			JOptionPane.showMessageDialog(this, e1);
		}

		btnOK = new JButton();
		panel.add(btnOK, "3, 5");
		btnOK.setText("OK");
		btnOK.addActionListener(this);

		lblDepartment = new JLabel();
		panel.add(lblDepartment, "1, 2, r, f");
		lblDepartment.setText("Setor:");

		ComboBoxModel cbDepartmentModel = new DefaultComboBoxModel(new String[] { "Selecione" });
		cbDepartment = new JComboBox();
		panel.add(cbDepartment, "2, 2, 3, 1");
		cbDepartment.setModel(cbDepartmentModel);

		lblFrom = new JLabel();
		panel.add(lblFrom, "1, 3, r, f");
		lblFrom.setText("De:");

		lblTo = new JLabel();
		panel.add(lblTo, "1, 4, r, f");
		lblTo.setText("Até");

		try {
			MaskFormatter maskFormat = new MaskFormatter("##/##/####");
			ftxtFrom = new JFormattedTextField(maskFormat);
			panel.add(ftxtFrom, "2, 3");

			ftxtTo = new JFormattedTextField(maskFormat);
			panel.add(ftxtTo, "2, 4");
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(this, "A data deve estar no formato dd/MM/yyyy");
		}

	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		if (e.getSource().equals(cbClient)) {
			String[] departments;
			try {
				departments = Utils.populateDepartmentsCombo(this.cbClient.getSelectedItem().toString());
				this.cbDepartment.setModel(new DefaultComboBoxModel(departments));
			} catch (BusinessException e1) {
				JOptionPane.showMessageDialog(this, e1);
			}
		}
	}

	protected void btnOKAction() throws BusinessException {
		chosenClient = this.cbClient.getSelectedItem().toString();
		chosenDepartment = this.cbDepartment.getSelectedItem().toString();

		try {
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			chosenFromDate = formatter.parse(this.ftxtFrom.getText());
			chosenToDate = formatter.parse(this.ftxtTo.getText());
		} catch (ParseException e) {
			throw new BusinessException(MessageLoader.getInstance().getMessage(12));
		}

		if (chosenClient.equals("Selecione")) {
			throw new BusinessException(MessageLoader.getInstance().getMessage(10));
		} else {
			if (chosenDepartment.equals("Selecione")) {
				throw new BusinessException(MessageLoader.getInstance().getMessage(11));
			} else {
				if (chosenFromDate.equals("")) {
					throw new BusinessException(MessageLoader.getInstance().getMessage(13));
				} else {
					if (chosenToDate.equals("")) {
						throw new BusinessException(MessageLoader.getInstance().getMessage(14));
					}
				}
			}
		}
	}

	@Override
	protected void setParams() {
		params.put(ReportConstants.CLIENT_NAME_PARAMETER, chosenClient);
		params.put(ReportConstants.DEPARTMENT_NAME_PARAMETER, chosenDepartment);
		params.put(ReportConstants.FROM_PARAMETER, formatter.format(chosenFromDate));
		params.put(ReportConstants.TO_PARAMETER, formatter.format(chosenToDate));
	}
}
