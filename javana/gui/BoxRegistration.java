package br.com.javana.gui;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import br.com.javana.business.BusinessDelegate;
import br.com.javana.business.ObjectAlreadyExistentException;
import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.BoxDTO;
import br.com.javana.dto.DepartmentClientDTO;
import br.com.javana.dto.LostBoxNumbersDTO;
import br.com.javana.utils.Utils;

/**
 * Tela de cadastro de caixas
 */
public class BoxRegistration extends AbstractFrame {
	private static final long serialVersionUID = 1L;
	/**
	 * Panel com os elementos
	 */
	private JPanel panel;
	/**
	 * Label número
	 */
	private JLabel lblCode;
	/**
	 * Label empresa
	 */
	private JLabel lblCompany;
	/**
	 * Label setor
	 */
	private JLabel lblDepartment;
	/**
	 * Label Assunto
	 */
	private JLabel lblSubject;
	/**
	 * Label mês
	 */
	private JLabel lblMonth;
	/**
	 * Botão número
	 */
	private JButton btnChooseNumber;
	/**
	 * Campo ano
	 */
	private JTextField txtYear;
	/**
	 * Label ano
	 */
	private JLabel lblYear;
	/**
	 * Campo mes
	 */
	private JTextField txtMonth;
	/**
	 * Campo assunto
	 */
	private JTextField txtSubject;
	/**
	 * Combo setor
	 */
	private JComboBox cbDepartment;
	/**
	 * Combo empresa
	 */
	private JComboBox cbClient;
	/**
	 * Campo número
	 */
	private JTextField txtCode;

	/**
	 * Id da caixa selecionada para atualização ou exclusão
	 */
	private JTextField txtSelectedBox;

	/**
	 * Se for true, um número perdido esta sendo utilizado
	 */
	private boolean lost = false;

	@Override
	public void btnDeleteAction() {
		Object[] options = { "Sim", "Não" };
		int answer = JOptionPane.showOptionDialog(this, "Você tem certeza que deseja excluir esta caixa?", "Confirme",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

		if (answer == 0) {
			try {
				BusinessDelegate.getInstance().getBox().delete(this.populateDTO());
				JOptionPane.showMessageDialog(this, "Caixa excluído com sucesso!");

				this.resetFields();
			} catch (BusinessException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
		}
	}

	@Override
	public void btnSearchAction() {
		BoxSearch cs = new BoxSearch();
		super.getDesktopPane().add(cs, 0);
	}

	@Override
	public void createPanel() {
		panel = new JPanel();
		TableLayout panelLayout = new TableLayout(
				new double[][] {
						{ 10.0, 57.0, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, 27.0, 76.0, 13.0, 43.0,
								49.0, 10.0 }, { 10.0, 24.0, 24.0, 24.0, 24.0, 24.0, 24.0, 24.0, TableLayout.FILL } });
		panelLayout.setHGap(5);
		panelLayout.setVGap(5);
		panel.setLayout(panelLayout);
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setPreferredSize(new java.awt.Dimension(467, 269));

		lblCode = new JLabel();
		panel.add(lblCode, "1, 1, r, f");
		lblCode.setText("Número:");

		txtCode = new JTextField();
		panel.add(txtCode, "2, 1, 3, 1");
		txtCode.setEditable(false);
		this.pickANumber();

		lblCompany = new JLabel();
		panel.add(lblCompany, "1, 2, r, f");
		lblCompany.setText("Empresa:");

		String[] companies;
		try {
			companies = Utils.populateClientsCombo("É preciso cadastrar empresas antes de cadastrar uma caixa!");
			if (companies != null) {
				ComboBoxModel cbCompanyModel = new DefaultComboBoxModel(companies);
				cbClient = new JComboBox();
				panel.add(cbClient, "2, 2, 3, 2");
				cbClient.setModel(cbCompanyModel);
				cbClient.addActionListener(this);
			} else {
				cbClient = new JComboBox();
				panel.add(cbClient, "2, 2, 3, 2");
			}
		} catch (BusinessException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}

		lblDepartment = new JLabel();
		panel.add(lblDepartment, "1, 3, r, f");
		lblDepartment.setText("Setor:");

		ComboBoxModel cbDepartmentModel = new DefaultComboBoxModel(new String[] { "Selecione" });
		cbDepartment = new JComboBox();
		panel.add(cbDepartment, "2, 3, 3, 3");
		cbDepartment.setModel(cbDepartmentModel);

		lblSubject = new JLabel();
		panel.add(lblSubject, "1, 4, r, f");
		lblSubject.setText("Assunto:");

		txtSubject = new JTextField();
		panel.add(txtSubject, "2, 4, 6, 4");
		txtSubject.addCaretListener(new CaretListener() {

			public void caretUpdate(CaretEvent arg0) {
				if (txtSubject.getText().length() == 255)
					JOptionPane.showMessageDialog(null, "O campo chegou no seu limite máximo");
			}

		});

		lblMonth = new JLabel();
		panel.add(lblMonth, "1, 5, r, f");
		lblMonth.setText("Mês:");

		txtMonth = new JTextField();
		panel.add(txtMonth, "2, 5, 3, 5");

		lblYear = new JLabel();
		panel.add(lblYear, "1, 6, r, f");
		lblYear.setText("Ano:");

		txtYear = new JTextField();
		panel.add(txtYear, "2, 6, 3, 6");

		btnSearch = new JButton();
		panel.add(btnSearch, "8, 1, 9, 1");
		btnSearch.setText("Buscar");
		btnSearch.addActionListener(this);

		btnOK = new JButton();
		panel.add(btnOK, "6, 7, 7, 7");
		btnOK.setText("OK");
		btnOK.addActionListener(this);

		btnCancel = new JButton();
		panel.add(btnCancel, "8, 7, 9, 7");
		btnCancel.setText("Cancelar");
		btnCancel.addActionListener(this);

		btnDelete = new JButton();
		panel.add(btnDelete, "8, 2, 9, 2");
		btnDelete.setText("Excluir");
		btnDelete.setEnabled(false);
		btnDelete.addActionListener(this);

		btnUpdate = new JButton();
		panel.add(btnUpdate, "8, 3, 9, 3");
		btnUpdate.setText("Corrigir");
		btnUpdate.setEnabled(false);
		btnUpdate.addActionListener(this);

		btnChooseNumber = new JButton();
		panel.add(btnChooseNumber, "8, 4, 9, 4");
		btnChooseNumber.setText("Número");
		btnChooseNumber.addActionListener(this);

		txtSelectedBox = new JTextField();
		txtSelectedBox.setVisible(false);
		panel.add(txtSelectedBox, "4, 1");
	}

	/**
	 * Seleciona um número para a caixa
	 */
	private void pickANumber() {
		try {
			long number = BusinessDelegate.getInstance().getBox().pickANumber();
			this.txtCode.setText("" + number);
		} catch (BusinessException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	@Override
	public void resetFields() {
		// 0 - sim
		// 1 - não
		Object[] options = { "Sim", "Não" };
		int answer = JOptionPane.showOptionDialog(this, "Você deseja incluir outra caixa com os mesmos dados?",
				"Confirme", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		try {
			this.txtCode.setText("" + BusinessDelegate.getInstance().getBox().pickANumber());
		} catch (BusinessException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}

		btnDelete.setEnabled(false);
		btnUpdate.setEnabled(false);
		btnOK.setEnabled(true);

		if (answer == 1) {
			this.txtMonth.setText("");
			this.txtSelectedBox.setText("");
			this.txtSubject.setText("");
			this.txtYear.setText("");
			this.cbClient.setSelectedIndex(0);
			this.cbDepartment.setSelectedIndex(0);
		}
	}

	@Override
	public void save() {
		try {
			BusinessDelegate.getInstance().getBox().save(this.populateDTO());
			if (lost) {
				LostBoxNumbersDTO dto = new LostBoxNumbersDTO();
				dto.setNumber(new Long(this.txtCode.getText()));
				BusinessDelegate.getInstance().getLostBox().deleteByNumber(dto);
				lost = false;
			}
		} catch (ObjectAlreadyExistentException e2) {
			JOptionPane.showMessageDialog(this, "Já existe uma caixa com esses dados.");
			return;
		} catch (BusinessException e1) {
			JOptionPane.showMessageDialog(this, e1.getMessage());
		}

		JOptionPane.showMessageDialog(this, "Caixa incluida com sucesso!");
	}

	private BoxDTO populateDTO() {
		BoxDTO dto = new BoxDTO();
		DepartmentClientDTO deptClient = new DepartmentClientDTO();

		try {
			deptClient = BusinessDelegate.getInstance().getDeptClient().findByClientDepartment(
					this.cbClient.getSelectedItem().toString(), this.cbDepartment.getSelectedItem().toString());
		} catch (BusinessException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}

		dto.setCode(new Integer(this.txtCode.getText().toString()));
		dto.setDeptClient(deptClient);
		dto.setMonth(this.txtMonth.getText());
		dto.setRegistrationDate(new Timestamp(System.currentTimeMillis()));
		dto.setSubject(this.txtSubject.getText());
		dto.setYear(this.txtYear.getText());

		if (!this.txtSelectedBox.getText().equals("")) {
			dto.setId(new Integer(this.txtSelectedBox.getText()).intValue());
		}

		return dto;
	}

	@Override
	public void screenChecks() {
		if (this.cbClient.getItemCount() == 1) {
			JOptionPane.showMessageDialog(null,
					"É preciso cadastrar pelo menos uma empresa antes de cadastrar uma caixa!");
		}
	}

	@Override
	public void setDimensions() {
		this.setPreferredSize(new java.awt.Dimension(617, 253));
		this.setBounds(0, 0, 617, 253);
		this.setTitle("Cadastro de Caixas");
	}

	@Override
	public void update() {
		try {
			BusinessDelegate.getInstance().getBox().update(this.populateDTO());
			if (lost) {
				LostBoxNumbersDTO dto = new LostBoxNumbersDTO();
				dto.setNumber(new Long(this.txtCode.getText()));
				BusinessDelegate.getInstance().getLostBox().deleteByNumber(dto);
				lost = false;
			}
		} catch (BusinessException e1) {
			JOptionPane.showMessageDialog(this, e1.getMessage());
		}

		JOptionPane.showMessageDialog(this, "Caixa atualizada com sucesso!");
	}

	@Override
	public boolean validateFields() {
		if (this.cbClient.getSelectedItem().toString().equals("Selecione")) {
			JOptionPane.showMessageDialog(this, "É preciso selecionar uma empresa!");
			return false;
		}

		if (this.cbDepartment.getSelectedItem().toString().equals("Selecione")) {
			JOptionPane.showMessageDialog(this, "É preciso selecionar um setor!");
			return false;
		}

		if (this.txtSubject.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "É preciso colocar um assunto!");
			return false;
		}

		if (this.txtSubject.getText().length() > 255) {
			String temp = this.txtSubject.getText().substring(0, 255);
			this.txtSubject.setText(temp);
		}

		return true;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnSearch)) {
			btnSearchAction();
		}

		if (e.getSource().equals(btnOK)) {
			btnOKAction();
		}

		if (e.getSource().equals(btnCancel)) {
			this.dispose();
		}

		if (e.getSource().equals(btnUpdate)) {
			btnUpdateAction();
		}

		if (e.getSource().equals(btnDelete)) {
			btnDeleteAction();
		}

		if (e.getSource().equals(btnChooseNumber)) {
			btnChooseNumberAction();
		}

		if (e.getSource().equals(cbClient)) {
			if (!cbClient.getSelectedItem().equals("Selecione")) {
				String[] departments;
				try {
					departments = Utils.populateDepartmentsCombo(this.cbClient.getSelectedItem().toString());
					this.cbDepartment.setModel(new DefaultComboBoxModel(departments));
				} catch (BusinessException e1) {
					JOptionPane.showMessageDialog(this, e1.getMessage());
				}
			}
		}
	}

	private void btnChooseNumberAction() {
		BoxNumberSearch cs = new BoxNumberSearch();
		super.getDesktopPane().add(cs, 0);
	}

	public JTextField getTxtSelectedBox() {
		return txtSelectedBox;
	}

	public void setTxtSelectedBox(JTextField txtSelectedBox) {
		this.txtSelectedBox = txtSelectedBox;
	}

	public JComboBox getCbClient() {
		return cbClient;
	}

	public void setCbClient(JComboBox cbClient) {
		this.cbClient = cbClient;
	}

	public JComboBox getCbDepartment() {
		return cbDepartment;
	}

	public void setCbDepartment(JComboBox cbDepartment) {
		this.cbDepartment = cbDepartment;
	}

	public JTextField getTxtCode() {
		return txtCode;
	}

	public void setTxtCode(JTextField txtCode) {
		this.txtCode = txtCode;
	}

	public JTextField getTxtMonth() {
		return txtMonth;
	}

	public void setTxtMonth(JTextField txtMonth) {
		this.txtMonth = txtMonth;
	}

	public JTextField getTxtSubject() {
		return txtSubject;
	}

	public void setTxtSubject(JTextField txtSubject) {
		this.txtSubject = txtSubject;
	}

	public JTextField getTxtYear() {
		return txtYear;
	}

	public void setTxtYear(JTextField txtYear) {
		this.txtYear = txtYear;
	}

	public boolean isLost() {
		return lost;
	}

	public void setLost(boolean lost) {
		this.lost = lost;
	}

}
