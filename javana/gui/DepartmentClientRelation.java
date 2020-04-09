package br.com.javana.gui;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import br.com.javana.business.BusinessDelegate;
import br.com.javana.business.ChildException;
import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.ClientDTO;
import br.com.javana.dto.DepartmentDTO;
import br.com.javana.utils.Utils;

public class DepartmentClientRelation extends JInternalFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Panel com os elementos
	 */
	private JPanel panel;
	/**
	 * Label empresa:
	 */
	private JLabel lblClient;
	/**
	 * Lista com todos os departamentos que a empresa selecionada NAO possui
	 */
	private JList listLeft;
	/**
	 * Lista com todos os departamentos que a empresa selecionada POSSUI
	 */
	private JList listRight;
	/**
	 * Botão OK
	 */
	private JButton btnOK;
	/**
	 * Botão <<
	 */
	private JButton btnRemove;
	/**
	 * Botão >>
	 */
	private JButton btnAdd;
	/**
	 * Combo com as empresas
	 */
	private JComboBox cbClient;
	private JScrollPane scrollRight;
	private JScrollPane scrollLeft;

	public DepartmentClientRelation() {
		super();
		initGUI();
	}

	public void initGUI() {
		this.setDimensions();

		setVisible(true);
		this.setTitle("Vincular Setores ao Cliente");
		this.setResizable(false);
		this.setClosable(true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setIconifiable(true);
		this.createPanel();

		this.screenChecks();
	}

	private void screenChecks() {
		if (this.cbClient.getItemCount() == 1) {
			JOptionPane.showMessageDialog(this, "É preciso cadastrar pelo menos um cliente antes de vincular setores!");
		}

	}

	private void createPanel() {
		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		TableLayout panelLayout = new TableLayout(new double[][] {
				{ 10.0, 52.0, TableLayout.FILL, 42.0, 20.0, 10.0, 62.0, 10.0, TableLayout.FILL, TableLayout.FILL,
						TableLayout.FILL, TableLayout.FILL, 19.0, 10.0 },
				{ 10.0, 24.0, 10.0, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL,
						TableLayout.FILL, TableLayout.FILL, 16.0, 24.0, 10.0 } });
		panelLayout.setHGap(5);
		panelLayout.setVGap(5);
		panel.setLayout(panelLayout);
		panel.setPreferredSize(new java.awt.Dimension(556, 317));

		lblClient = new JLabel();
		panel.add(lblClient, "1, 1");
		lblClient.setText("Cliente:");

		String[] clients;
		try {
			clients = Utils.populateClientsCombo("É preciso ter clientes cadastrados");
			if (clients != null) {
				ComboBoxModel cbClientModel = new DefaultComboBoxModel(clients);
				cbClient = new JComboBox();
				panel.add(cbClient, "2, 1, 12, 1");
				cbClient.setModel(cbClientModel);
			} else {
				cbClient = new JComboBox();
				panel.add(cbClient, "2, 1, 12, 1");
			}
		} catch (BusinessException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}

		cbClient.addActionListener(this);

		listLeft = new JList();
		panel.add(listLeft, "1, 3, 4, 8");
		listLeft.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listLeft.setLayoutOrientation(JList.VERTICAL);

		listRight = new JList();
		panel.add(listRight, "8, 3, 12, 8");
		listRight.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listRight.setLayoutOrientation(JList.VERTICAL);

		btnAdd = new JButton();
		panel.add(btnAdd, "6, 5");
		btnAdd.setText(">>");
		btnAdd.addActionListener(this);

		btnRemove = new JButton();
		panel.add(btnRemove, "6, 6");
		btnRemove.setText("<<");
		btnRemove.addActionListener(this);

		btnOK = new JButton();
		panel.add(btnOK, "11, 10, 12, 10");
		btnOK.setText("OK");
		btnOK.addActionListener(this);
		btnOK.setEnabled(false);

		scrollLeft = new JScrollPane(listLeft);
		panel.add(scrollLeft, "1, 3, 4, 8");

		scrollRight = new JScrollPane(listRight);
		panel.add(scrollRight, "8, 3, 12, 8");
	}

	private void setDimensions() {
		this.setPreferredSize(new java.awt.Dimension(472, 342));
		this.setBounds(0, 0, 472, 342);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.cbClient)) {
			this.cbAction();
		}

		if (e.getSource().equals(this.btnAdd)) {
			this.addAction();
		}

		if (e.getSource().equals(this.btnRemove)) {
			this.removeAction();
		}

		if (e.getSource().equals(this.btnOK)) {
			this.saveRelation();
		}
	}

	/**
	 * Salva o relacionamento entre a empresa e o setor
	 */
	private void saveRelation() {
		List<DepartmentDTO> toAdd = new ArrayList<DepartmentDTO>();

		toAdd.addAll(this.populateDTOs(this.listRight.getModel()));

		ClientDTO company = new ClientDTO();
		company.setName(this.cbClient.getSelectedItem().toString());
		try {
			BusinessDelegate.getInstance().getDeptClient().updateRelations(company, toAdd);

			JOptionPane.showMessageDialog(this, "Atualizado com sucesso!");
		} catch (ChildException e) {
			JOptionPane.showMessageDialog(this,
					"Não foi possível atualizar! Este setor está vinculado a pessoas e possivelmente a protocolos");
		} catch (BusinessException e) {
			JOptionPane.showMessageDialog(this, "Não foi possível atualizar!");
		}
	}

	private List<DepartmentDTO> populateDTOs(ListModel values) {
		List<DepartmentDTO> list = new ArrayList<DepartmentDTO>();

		for (int i = 0; i < values.getSize(); i++) {
			DepartmentDTO dto = new DepartmentDTO();
			dto.setName(values.getElementAt(i).toString());
			list.add(dto);
		}

		return list;
	}

	/**
	 * Adiciona elementos da lista da direita na lista da esquerda
	 */
	private void removeAction() {
		List<String> add = new ArrayList<String>();
		Object[] selected = this.listRight.getSelectedValues();

		for (int i = 0; i < selected.length; i++) {
			DefaultListModel model = (DefaultListModel) this.listRight.getModel();
			model.removeElement(selected[i]);
			add.add(selected[i].toString());
			DepartmentDTO dto = new DepartmentDTO();
			dto.setName(selected[i].toString());
		}

		this.populateLeftList(add);
	}

	/**
	 * Adiciona elementos da lista da esquerda na lista da direita
	 */
	private void addAction() {
		List<String> add = new ArrayList<String>();
		Object[] selected = this.listLeft.getSelectedValues();

		for (int i = 0; i < selected.length; i++) {
			DefaultListModel model = (DefaultListModel) this.listLeft.getModel();
			model.removeElement(selected[i]);
			add.add(selected[i].toString());
		}

		this.populateRightList(add);
	}

	/**
	 * Verifica se o valor do combo é diferente de 'selecione' e popula as duas
	 * listas de setores
	 */
	private void cbAction() {
		listLeft.setModel(new DefaultListModel());
		listRight.setModel(new DefaultListModel());
		if (!this.cbClient.getSelectedItem().toString().equals("Selecione")) {
			try {
				List<DepartmentDTO> all = BusinessDelegate.getInstance().getDepartment().findAll();
				List<DepartmentDTO> self = BusinessDelegate.getInstance().getDeptClient().findByClient(
						this.cbClient.getSelectedItem().toString());

				List<String> left = new ArrayList<String>();
				List<String> right = new ArrayList<String>();
				boolean has = false;

				for (DepartmentDTO d : all) {
					for (DepartmentDTO contains : self) {
						if (d.getId() == contains.getId()) {
							has = true;
						}
					}

					if (has) {
						right.add(d.getName());
					} else {
						left.add(d.getName());
					}

					has = false;
				}

				this.populateLeftList(left);
				this.populateRightList(right);
				this.btnOK.setEnabled(true);
			} catch (BusinessException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
		}
	}

	/**
	 * Popula a lista da direita com os departamentos que a empresa selecionada
	 * já possui
	 * 
	 * @param right
	 */
	private void populateRightList(List<String> right) {
		DefaultListModel rightModel = (DefaultListModel) this.listRight.getModel();

		for (String dept : right) {
			rightModel.addElement(dept);
		}

		this.listRight.setModel(rightModel);

	}

	/**
	 * Popula a lista da esquerda com os departamentos que a empresa selecionada
	 * ainda não possui
	 * 
	 * @param left
	 */
	private void populateLeftList(List<String> left) {
		DefaultListModel leftModel = (DefaultListModel) this.listLeft.getModel();

		for (String dept : left) {
			leftModel.addElement(dept);
		}

		this.listLeft.setModel(leftModel);

	}

}
