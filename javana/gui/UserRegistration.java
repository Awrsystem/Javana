package br.com.javana.gui;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.com.javana.business.BusinessDelegate;
import br.com.javana.business.ChildException;
import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.UserDTO;
import br.com.javana.utils.Utils;

public class UserRegistration extends AbstractFrame {

	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JLabel lblName;
	private JLabel lblDepartment;
	private JTextField txtName;
	private JComboBox cbDepartment;
	private JComboBox cbClient;
	private JLabel lblClient;
	/**
	 * Id do objeto selecionado na pesquisa
	 */
	private long selectedUserId;

	@Override
	public void btnDeleteAction() {
		Object[] options = { "Sim", "Não" };
		int answer = JOptionPane.showOptionDialog(this, "Você tem certeza que deseja excluir esta pessoa?", "Confirme",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

		if (answer == 0) {
			try {
				BusinessDelegate.getInstance().getUser().delete(this.populateDTO());
				JOptionPane.showMessageDialog(this, "Excluído com sucesso!");
				this.resetFields();
			} catch (BusinessException e1) {
				if (e1 instanceof ChildException) {
					JOptionPane.showMessageDialog(this,
							"Esta pessoa está vinculada a protocolos. Não pode ser excluída");
				}
			}
		}
	}

	@Override
	public void btnSearchAction() {
		UserSearch cs = new UserSearch();
		super.getDesktopPane().add(cs, 0);
	}

	@Override
	public void createPanel() {
		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		TableLayout panelLayout = new TableLayout(new double[][] {
				{ 10.0, 61.0, TableLayout.FILL, 97.0, 30.0, 60.0, 10.0 },
				{ 10.0, 24.0, 24.0, 24.0, 24.0, 24.0, 10.0, 10.0 } });
		panelLayout.setHGap(5);
		panelLayout.setVGap(5);
		panel.setLayout(panelLayout);
		panel.setPreferredSize(new java.awt.Dimension(395, 180));

		lblName = new JLabel();
		panel.add(lblName, "1, 1, r, f");
		lblName.setText("Nome:");

		lblClient = new JLabel();
		panel.add(lblClient, "1, 2, r, f");
		lblClient.setText("Cliente:");

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

		txtName = new JTextField();
		panel.add(txtName, "2, 1, 3, 1");

		btnSearch = new JButton();
		panel.add(btnSearch, "4, 1, 5, 1");
		btnSearch.setText("Buscar");
		btnSearch.addActionListener(this);

		btnDelete = new JButton();
		panel.add(btnDelete, "4, 2, 5, 2");
		btnDelete.setText("Excluir");
		btnDelete.setEnabled(false);
		btnDelete.addActionListener(this);

		btnUpdate = new JButton();
		panel.add(btnUpdate, "4, 3, 5, 3");
		btnUpdate.setText("Corrigir");
		btnUpdate.setEnabled(false);
		btnUpdate.addActionListener(this);

		btnOK = new JButton();
		panel.add(btnOK, "3, 5");
		btnOK.setText("OK");
		btnOK.addActionListener(this);

		btnCancel = new JButton();
		panel.add(btnCancel, "4, 5, 5, 5");
		btnCancel.setText("Cancelar");
		btnCancel.addActionListener(this);
	}

	@Override
	public void resetFields() {
		// 0 - sim
		// 1 - não
		Object[] options = { "Sim", "Não" };
		int answer = JOptionPane.showOptionDialog(this, "Você deseja incluir outra pessoa?", "Confirme",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		btnDelete.setEnabled(false);
		btnUpdate.setEnabled(false);
		btnOK.setEnabled(true);
		this.selectedUserId = 0;

		if (answer == 1) {
			this.txtName.setText("");
			this.cbClient.setSelectedIndex(0);
			this.cbDepartment.setSelectedIndex(0);
		}

	}

	@Override
	public void save() {
		try {
			BusinessDelegate.getInstance().getUser().save(this.populateDTO());
			JOptionPane.showMessageDialog(this, "Incluído com sucesso!");
		} catch (BusinessException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}

	}

	private UserDTO populateDTO() {
		UserDTO user = new UserDTO();
		try {
			user.setDeptClient(BusinessDelegate.getInstance().getDeptClient().findByClientDepartment(
					this.cbClient.getSelectedItem().toString(), this.cbDepartment.getSelectedItem().toString()));
			user.setName(this.txtName.getText());

			if (this.selectedUserId == 0) {
				user.setId(0);
			} else {
				user.setId(this.selectedUserId);
			}

			return user;

		} catch (BusinessException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			return null;
		}
	}

	@Override
	public void screenChecks() {
		if (this.cbClient.getItemCount() == 1) {
			JOptionPane.showMessageDialog(this,
					"É preciso cadastrar pelo menos uma empresa antes de cadastrar um usuário!");
		}
	}

	@Override
	public void setDimensions() {
		this.setPreferredSize(new java.awt.Dimension(400, 195));
		this.setBounds(0, 0, 400, 195);
		setVisible(true);
		this.setTitle("Cadastro de Pessoas");
	}

	@Override
	public void update() {
		try {
			BusinessDelegate.getInstance().getUser().update(this.populateDTO());
		} catch (BusinessException e1) {
			JOptionPane.showMessageDialog(this, e1.getMessage());
		}

		JOptionPane.showMessageDialog(this, "Atualizado com sucesso!");
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

		if (this.txtName.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "É preciso colocar um nome!");
			return false;
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

		if (e.getSource().equals(cbClient)) {
			String[] departments;
			try {
				departments = Utils.populateDepartmentsCombo(this.cbClient.getSelectedItem().toString());
				if (departments == null) {
					departments = new String[1];
					departments[0] = "Selecione";
				} else {
					this.cbDepartment.setModel(new DefaultComboBoxModel(departments));
				}
			} catch (BusinessException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
		}
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

	public long getSelectedUserId() {
		return selectedUserId;
	}

	public void setSelectedUserId(long selectedUserId) {
		this.selectedUserId = selectedUserId;
	}

	public JTextField getTxtName() {
		return txtName;
	}

	public void setTxtName(JTextField txtName) {
		this.txtName = txtName;
	}

}
