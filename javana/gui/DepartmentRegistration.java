package br.com.javana.gui;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.com.javana.business.BusinessDelegate;
import br.com.javana.business.ChildException;
import br.com.javana.business.ObjectAlreadyExistentException;
import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.DepartmentDTO;

/**
 * Tela de Cadastro de Setor
 */
public class DepartmentRegistration extends AbstractFrame {
	private static final long serialVersionUID = 1L;

	/**
	 * Panel com os elementos
	 */
	private JPanel panel;

	/**
	 * Label nome
	 */
	private JLabel lblName;

	/**
	 * Campo texto para o nome
	 */
	private JTextField txtName;

	/**
	 * Campo que guarda o id do setor selecionado para alteração ou exclusao
	 */
	private JTextField txtSelectedDepartment;

	public DepartmentRegistration() {
		super();
	}

	@Override
	public void createPanel() {
		panel = new JPanel();
		TableLayout panelLayout = new TableLayout(
				new double[][] { { 10.0, 51.0, 167.0, 2.0, 81.0, 94.0, 122.0, 85.0, 10.0 },
						{ 10.0, 24.0, 24.0, 24.0, 24.0, 24.0, 10.0 } });
		panelLayout.setHGap(5);
		panelLayout.setVGap(5);
		panel.setLayout(panelLayout);
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setPreferredSize(new java.awt.Dimension(443, 175));

		lblName = new JLabel();
		panel.add(lblName, "1, 1, r, f");
		lblName.setText("Nome:");

		txtName = new JTextField();
		panel.add(txtName, "2, 1, 4, 1");

		btnOK = new JButton();
		panel.add(btnOK, "4, 5");
		btnOK.setText("OK");
		btnOK.addActionListener(this);

		btnCancel = new JButton();
		panel.add(btnCancel, "5, 5");
		btnCancel.setText("Cancelar");
		btnCancel.addActionListener(this);

		btnSearch = new JButton();
		panel.add(btnSearch, "5, 1");
		btnSearch.setText("Buscar");
		btnSearch.addActionListener(this);

		btnDelete = new JButton();
		panel.add(btnDelete, "5, 2");
		btnDelete.setText("Excluir");
		btnDelete.setEnabled(false);
		btnDelete.addActionListener(this);

		btnUpdate = new JButton();
		panel.add(btnUpdate, "5, 3");
		btnUpdate.setText("Corrigir");
		btnUpdate.setEnabled(false);
		btnUpdate.addActionListener(this);

		txtSelectedDepartment = new JTextField();
		panel.add(txtSelectedDepartment, "2, 2");
		txtSelectedDepartment.setVisible(false);
	}

	public JTextField getTxtName() {
		return txtName;
	}

	public void setTxtName(JTextField txtName) {
		this.txtName = txtName;
	}

	/**
	 * Cria um dto com os dados que serão incluidos ou alterados no cliente
	 */
	private DepartmentDTO populateDTO() {
		DepartmentDTO dto = new DepartmentDTO();
		dto.setName(this.txtName.getText());

		if (!this.txtSelectedDepartment.getText().equals("")) {
			dto.setId(new Integer(this.txtSelectedDepartment.getText()).intValue());
		}

		return dto;
	}

	@Override
	public void btnDeleteAction() {
		Object[] options = { "Sim", "Não" };
		int answer = JOptionPane.showOptionDialog(this, "Você tem certeza que deseja excluir este cliente?",
				"Confirme", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

		if (answer == 0) {
			try {
				BusinessDelegate.getInstance().getDepartment().delete(this.populateDTO());
				JOptionPane.showMessageDialog(this, "Setor excluído com sucesso!");
				this.resetFields();
			} catch (BusinessException e1) {
				if (e1 instanceof ChildException) {
					JOptionPane.showMessageDialog(this,
							"Existem clientes vinculdas a este setor. Para excluir o setor,"
									+ "você deve primeiro excluir os clientes vinculados a ele.");
				}
			}
		}

	}

	@Override
	public void btnSearchAction() {
		DepartmentSearch cs = new DepartmentSearch();
		super.getDesktopPane().add(cs, 0);
	}

	@Override
	public void resetFields() {
		this.txtName.setText("");

		btnDelete.setEnabled(false);
		btnUpdate.setEnabled(false);
		btnOK.setEnabled(true);

	}

	@Override
	public void save() {
		try {
			BusinessDelegate.getInstance().getDepartment().save(this.populateDTO());
		} catch (ObjectAlreadyExistentException e2) {
			JOptionPane.showMessageDialog(this, "Já existe um setor cadastrado com este nome.");
		} catch (BusinessException e1) {
			JOptionPane.showMessageDialog(this, e1.getMessage());
		}

		JOptionPane.showMessageDialog(this, "Setor incluido com sucesso!");
	}

	@Override
	public void screenChecks() {

	}

	@Override
	public void setDimensions() {
		this.setPreferredSize(new java.awt.Dimension(448, 191));
		this.setBounds(0, 0, 448, 191);
		this.setTitle("Cadastro de Setor");
	}

	@Override
	public void update() {
		try {
			BusinessDelegate.getInstance().getDepartment().update(this.populateDTO());
		} catch (BusinessException e1) {
			JOptionPane.showMessageDialog(this, e1.getMessage());
		}

		JOptionPane.showMessageDialog(this, "Setor atualizado com sucesso!");
		this.resetFields();

	}

	@Override
	public boolean validateFields() {
		if (this.txtName.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "O campo nome é obrigatório.");
			return false;
		} else {
			return true;
		}
	}

	public JTextField getTxtSelectedDepartment() {
		return txtSelectedDepartment;
	}

	public void setTxtSelectedDepartment(JTextField txtSelectedDepartment) {
		this.txtSelectedDepartment = txtSelectedDepartment;
	}
}
