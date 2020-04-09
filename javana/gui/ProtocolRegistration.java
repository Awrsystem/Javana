package br.com.javana.gui;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import br.com.javana.business.BusinessDelegate;
import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.BoxSubprotocolDTO;
import br.com.javana.dto.LostProtocolNumberDTO;
import br.com.javana.dto.ProtocolDTO;
import br.com.javana.dto.SubProtocolDTO;
import br.com.javana.utils.MessageLoader;
import br.com.javana.utils.Utils;

/**
 * Tela de cadastro de protocolos
 */
public class ProtocolRegistration extends AbstractFrame {
	private static final long serialVersionUID = 1394953858170378579L;

	private JPanel panel;

	private JLabel lblNumber;

	private JTable tblBoxes;

	private JButton btnRemoveBoxes;

	private JButton btnAddBoxes;

	private JLabel lblBoxes;

	private JComboBox cbRequester;

	private JLabel lblRequester;

	private JComboBox cbClient;

	private JLabel lblClient;

	private JTextField txtNumber;

	private DefaultTableModel model;

	private JScrollPane scrollPane;

	private JTextField txtSelectedProtocol;

	private JComboBox cbDepartment;

	private JLabel lblDepartment;

	private JButton btnManualNUmber;

	private SubProtocolDTO selectedSubprotocol;

	private JButton btnMoreItens;

	private JButton btnAddAutomatically;

	private JButton btnPickNumber;

	/**
	 * Se for true, um número perdido esta sendo utilizado
	 */
	private boolean lost = false;

	@Override
	public void btnDeleteAction() {

	}

	@Override
	public void btnSearchAction() {
		ProtocolSearch ps = new ProtocolSearch();
		super.getDesktopPane().add(ps, 0);
	}

	@Override
	public void createPanel() {
		panel = new JPanel();
		TableLayout panelLayout = new TableLayout(new double[][] {
				{ 10.0, 85.0, TableLayout.FILL, 92.0, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, 53.0,
						TableLayout.FILL, 10.0 },
				{ 10.0, 24.0, 24.0, 24.0, 24.0, 24.0, 24.0, 24.0, 24.0, 24.0, 24.0, 24.0, 10.0 } });
		panelLayout.setHGap(5);
		panelLayout.setVGap(5);
		panel.setLayout(panelLayout);
		getContentPane().add(panel, BorderLayout.CENTER);

		lblNumber = new JLabel();
		panel.add(lblNumber, "1, 1, r, f");
		lblNumber.setText("Número:");

		txtNumber = new JTextField();
		txtNumber.setEditable(false);
		panel.add(txtNumber, "2, 1, 6, 1");
		this.pickANumber();

		lblClient = new JLabel();
		panel.add(lblClient, "1, 2, r, f");
		lblClient.setText("Cliente:");

		String[] companies;
		try {
			companies = Utils
					.populateClientsCombo("É preciso cadastrar empresas antes de cadastrar um protocolo!");
			if (companies != null) {
				ComboBoxModel cbCompanyModel = new DefaultComboBoxModel(companies);
				cbClient = new JComboBox();
				panel.add(cbClient, "2, 2, 6, 2");
				cbClient.setModel(cbCompanyModel);
				cbClient.addActionListener(this);
			} else {
				cbClient = new JComboBox();
				panel.add(cbClient, "2, 2, 6, 2");
			}
		} catch (BusinessException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}

		lblRequester = new JLabel();
		panel.add(lblRequester, "1, 4, r, f");
		lblRequester.setText("Solicitante:");

		ComboBoxModel cbRequesterModel = new DefaultComboBoxModel(new String[] { "Selecione" });
		cbRequester = new JComboBox();
		panel.add(cbRequester, "2, 4, 6, 4");
		cbRequester.setModel(cbRequesterModel);

		lblBoxes = new JLabel();
		panel.add(lblBoxes, "1, 5, r, f");
		lblBoxes.setText("Caixas:");

		btnSearch = new JButton();
		panel.add(btnSearch, "7, 1, 8, 1");
		btnSearch.setText("Buscar");
		btnSearch.addActionListener(this);

		btnOK = new JButton();
		panel.add(btnOK, "5, 10, 6, 10");
		btnOK.setText("OK");
		btnOK.addActionListener(this);

		btnCancel = new JButton();
		panel.add(btnCancel, "7, 10, 8, 10");
		btnCancel.setText("Cancelar");
		btnCancel.addActionListener(this);

		btnAddBoxes = new JButton();
		panel.add(btnAddBoxes, "7, 6, 8, 6");
		btnAddBoxes.setText("Ad. Manual");
		btnAddBoxes.addActionListener(this);

		btnPickNumber = new JButton();
		panel.add(btnPickNumber, "7, 5, 8, 5");
		btnPickNumber.setText("Escolher");
		btnPickNumber.addActionListener(this);

		btnRemoveBoxes = new JButton();
		panel.add(btnRemoveBoxes, "7, 8, 8, 8");
		btnRemoveBoxes.setText("Remover");
		btnRemoveBoxes.addActionListener(this);

		btnAddAutomatically = new JButton();
		panel.add(btnAddAutomatically, "7, 7, 8, 7");
		btnAddAutomatically.setText("Ad. Auto");
		btnAddAutomatically.addActionListener(this);

		model = new DefaultTableModel(new String[] { "Código" }, 0);
		tblBoxes = new JTable();

		scrollPane = new JScrollPane(tblBoxes);
		panel.add(scrollPane, "2, 5, 6, 9, f, f");

		tblBoxes.setModel(model);

		btnUpdate = new JButton();
		panel.add(btnUpdate, "7, 2, 8, 2");
		btnUpdate.setText("Corrigir");
		btnUpdate.addActionListener(this);
		btnUpdate.setEnabled(false);

		txtSelectedProtocol = new JTextField();
		panel.add(txtSelectedProtocol, "1, 7");
		txtSelectedProtocol.setVisible(false);

		lblDepartment = new JLabel();
		panel.add(lblDepartment, "1, 3, r, f");
		lblDepartment.setText("Setor:");

		ComboBoxModel cbDepartmentModel = new DefaultComboBoxModel(new String[] { "Selecione" });
		cbDepartment = new JComboBox();
		panel.add(cbDepartment, "2, 3, 6, 3");
		cbDepartment.setModel(cbDepartmentModel);
		cbDepartment.addActionListener(this);

		btnManualNUmber = new JButton();
		panel.add(btnManualNUmber, "7, 3, 8, 3");
		btnManualNUmber.setText("Num. Manual");
		btnManualNUmber.setEnabled(true);
		btnManualNUmber.addActionListener(this);

		btnMoreItens = new JButton();
		panel.add(btnMoreItens, "7, 4, 8, 4");
		btnMoreItens.setText("Novo Item");
		btnMoreItens.addActionListener(this);
		btnMoreItens.setEnabled(false);
	}

	private void pickANumber() {
		long number = BusinessDelegate.getInstance().getProtocol().pickANumber();
		this.txtNumber.setText("" + number);
	}

	@Override
	public void resetFields() {
		Object[] options = { "Sim", "Não" };
		int answer = JOptionPane.showOptionDialog(this, "Você deseja incluir outro item ao protocolo?", "Confirme",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		if (answer == 0) {
			this.txtNumber.setEditable(false);
			this.cbClient.setEnabled(false);
			btnUpdate.setEnabled(false);
		} else {
			this.txtNumber.setEditable(true);
			this.txtNumber.setText("");
			this.cbClient.setSelectedIndex(0);
			this.cbClient.setEnabled(true);
			this.cbDepartment.setSelectedIndex(0);
			this.cbDepartment.setEnabled(true);
			this.cbRequester.setSelectedIndex(0);
			this.cbRequester.setEnabled(true);
			this.selectedSubprotocol = null;
			btnUpdate.setEnabled(false);
		}

		int size = model.getRowCount();
		for (int i = 0; i < size; i++) {
			model.removeRow(0);
		}

		this.cbRequester.setSelectedIndex(0);
		btnOK.setEnabled(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.javana.gui.AbstractFrame#btnOKAction()
	 * 
	 * Cada vez que dá um ok, pergunta se que incluir mais coisas no protocolo.
	 * Cada vez que salvar, salva um sub-protocolo do protocolo que está sendo
	 * preenchido. Se disser que quer incluir mais dados no mesmo protocolo,
	 * mantem o mesmo numero, e o mesmo cliente. Se disser que nao, limpa todos
	 * os dados assumindo que vai incluir um novo protocolo.
	 */
	@Override
	public void btnOKAction() {
		if (this.validateFields()) {
			this.save();
		}
	}

	/*
	 * Verifica se as caixas estao disponiveis Verifica se ja nao existe um
	 * protocolo com esse numero Verifica se ja existe pra esse protocolo essa
	 * combinação de cliente + setor + usuario Pra cada combinacao protocolo +
	 * empresa + setor + usuario deve haver apenas um subprotocolo Depois que
	 * salvar um sub protocolo pergunta se quer salvar outro
	 */
	@Override
	public void save() {
		LostProtocolNumberDTO lost = new LostProtocolNumberDTO();
		if (this.verifyBoxAvailability()) {
			try {
				ProtocolDTO existentProtocol = BusinessDelegate.getInstance().getProtocol().findByNumber(
						Long.parseLong(this.txtNumber.getText()), true);
				lost.setNumber(Long.parseLong(this.txtNumber.getText()));

				if (existentProtocol != null) {
					List<SubProtocolDTO> subs = BusinessDelegate.getInstance().getSubprotocol().findByProtocolId(
							existentProtocol.getId());
					boolean subExists = false;

					for (SubProtocolDTO dto : subs) {
						if ((dto.getRequester().getDeptClient().getClient().getName().equals(this.cbClient
								.getSelectedItem().toString()))
								&& (dto.getRequester().getDeptClient().getDepartment().getName()
										.equals(this.cbDepartment.getSelectedItem().toString()))
								&& (dto.getRequester().getName().equals(this.cbRequester.getSelectedItem().toString()))) {
							subExists = true;
						}
					}

					if (subExists) {
						JOptionPane.showMessageDialog(this,
								"Este protocolo ja contem um item para este cliente, setor e solicitante!");
						return;
					} else {
						Long subProtcolId = BusinessDelegate.getInstance().getSubprotocol().save(
								this.populateSubprotocol(existentProtocol.getId()));

						for (int i = 0; i < this.model.getRowCount(); i++) {
							BusinessDelegate.getInstance().getBoxSubprotocol().save(
									this.populateBoxSubProtocolDTO(subProtcolId, Long.parseLong(this.model.getValueAt(
											i, 0).toString())));
						}

						JOptionPane.showMessageDialog(this, "Protocolo incluido com sucesso!");
						this.resetFields();
					}
				} else {
					Long protocolId = BusinessDelegate.getInstance().getProtocol().save(this.populateProtocolDTO());
					Long subProtcolId = BusinessDelegate.getInstance().getSubprotocol().save(
							populateSubprotocol(protocolId));

					for (int i = 0; i < this.model.getRowCount(); i++) {
						BusinessDelegate.getInstance().getBoxSubprotocol().save(
								this.populateBoxSubProtocolDTO(subProtcolId, Long.parseLong(this.model.getValueAt(i, 0)
										.toString())));
					}

					BusinessDelegate.getInstance().getLostProtocol().deleteByNumber(lost);
					JOptionPane.showMessageDialog(this, "Protocolo incluido com sucesso!");
					this.resetFields();
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			} catch (BusinessException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
		}
	}

	private BoxSubprotocolDTO populateBoxSubProtocolDTO(Long subId, Long boxCode) {
		BoxSubprotocolDTO dto = new BoxSubprotocolDTO();

		try {
			SubProtocolDTO sub = BusinessDelegate.getInstance().getSubprotocol().findById(subId);
			dto.setSubprotocol(sub);
			dto.setBox(BusinessDelegate.getInstance().getBox().findByCode(boxCode));
		} catch (BusinessException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}

		return dto;
	}

	private SubProtocolDTO populateSubprotocol(Long protocolId) {
		SubProtocolDTO dto = new SubProtocolDTO();

		try {
			ProtocolDTO protocol = new ProtocolDTO();
			protocol.setId(protocolId);
			dto.setProtocol(protocol);
			dto.setRequester(BusinessDelegate.getInstance().getUser().findByExactName(
					this.cbRequester.getSelectedItem().toString()));
		} catch (BusinessException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}

		return dto;
	}

	private ProtocolDTO populateProtocolDTO() {
		ProtocolDTO dto = new ProtocolDTO();
		dto.setLeaveDate(new Timestamp(System.currentTimeMillis()));

		try {
			Long number = Long.parseLong(this.txtNumber.getText());
			dto.setNumber(number);
			dto.setStatus(0);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, MessageLoader.getInstance().getMessage(46));
		}

		return dto;
	}

	private boolean verifyBoxAvailability() {
		List<Long> boxes = new ArrayList<Long>();

		for (int i = 0; i < tblBoxes.getRowCount(); i++) {
			boxes.add(Long.parseLong(model.getValueAt(i, 0).toString()));
		}

		List<Long> result = new ArrayList<Long>();
		if (this.selectedSubprotocol == null) {
			try {
				result = BusinessDelegate.getInstance().getBoxSubprotocol().checkForUnavailableBoxes(boxes);
			} catch (BusinessException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
		} else {
			try {
				result = BusinessDelegate.getInstance().getBoxSubprotocol().findBoxCodesBySubProtocolId(
						this.selectedSubprotocol.getId());
			} catch (BusinessException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}

			for (Long code : result) {
				if (boxes.contains(code)) {
					boxes.remove(code);
				}
			}
			if (boxes.size() > 0) {
				try {
					result = BusinessDelegate.getInstance().getBoxSubprotocol().checkForUnavailableBoxes(boxes);
				} catch (BusinessException e) {
					JOptionPane.showMessageDialog(this, e.getMessage());
				}
				Set<Long> numbers = new HashSet<Long>();
				numbers.addAll(result);

				if (numbers.size() > 0) {
					JOptionPane.showMessageDialog(this, MessageLoader.getInstance().getMessage(47, numbers.toString()));
					return false;
				}
			}

		}

		return true;
	}

	@Override
	public void screenChecks() {
		if (this.cbClient.getItemCount() == 1) {
			JOptionPane.showMessageDialog(null,
					"É preciso cadastrar pelo menos uma empresa antes de cadastrar um protocolo!");
		}
	}

	@Override
	public void setDimensions() {
		this.setPreferredSize(new java.awt.Dimension(512, 339));
		this.setBounds(0, 0, 512, 339);
		setVisible(true);
		this.setTitle("Cadastro de Protocolos");
	}

	@Override
	public void update() {
		List<Long> boxes = new ArrayList<Long>();

		for (int i = 0; i < this.model.getRowCount(); i++) {
			boxes.add(new Long(this.model.getValueAt(i, 0).toString()));
		}

		try {
			this.selectedSubprotocol.setRequester(BusinessDelegate.getInstance().getUser().findByExactName(
					this.cbRequester.getSelectedItem().toString()));
			BusinessDelegate.getInstance().getSubprotocol().update(this.selectedSubprotocol, boxes);
		} catch (BusinessException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}

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

		if (this.txtNumber.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "É preciso colocar um número para o protocolo!");
			return false;
		}

		try {
			Long.parseLong(txtNumber.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "O número do protocolo deve conter somente números!");
			return false;
		}

		if (this.cbRequester.getSelectedItem().toString().equals("Selecione")) {
			JOptionPane.showMessageDialog(this, "É preciso colocar o solcitante!");
			return false;
		}

		if (this.model.getRowCount() == 0) {
			JOptionPane.showMessageDialog(this, "É preciso selecionar pelo menos uma caixa para o protocolo!");
			return false;
		}
		
		Set<String> data = new HashSet<String>();
		for(int i = 0; i < this.model.getRowCount(); i++){
			data.add((String) model.getValueAt(i, 0));
			model.removeRow(i);
			i--;
		}
		
		for(String s: data){
			String d[] = new String[1];
			d[0] = s;
			this.model.addRow(d);
		}

		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		if (e.getSource().equals(btnAddBoxes)) {
			PickBox pb = new PickBox();
			if (this.cbClient.getSelectedItem() == "Selecione") {
				JOptionPane.showMessageDialog(this, "Selecione um cliente");
			} else {
				pb.setClient(this.cbClient.getSelectedItem().toString());
				super.getDesktopPane().add(pb, 0);
			}
		}

		if (e.getSource().equals(btnAddAutomatically)) {
			PickBoxAutomatically pb = new PickBoxAutomatically();
			if (this.cbClient.getSelectedItem() == "Selecione") {
				JOptionPane.showMessageDialog(this, "Selecione um cliente");
			} else {
				pb.setClient(this.cbClient.getSelectedItem().toString());
				super.getDesktopPane().add(pb, 0);
			}
		}

		if (e.getSource().equals(btnRemoveBoxes)) {
			btnRemoveBoxesAction();
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

		if (e.getSource().equals(cbDepartment)) {
			String[] people;
			try {
				people = Utils.populateUsersCombo(this.cbClient.getSelectedItem().toString(), this.cbDepartment
						.getSelectedItem().toString());
				if (people == null) {
					people = new String[1];
					people[0] = "Selecione";
				} else {
					this.cbRequester.setModel(new DefaultComboBoxModel(people));
				}
			} catch (BusinessException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
		}

		if (e.getSource().equals(btnManualNUmber)) {
			this.btnManualNumberAction();
		}

		if (e.getSource().equals(btnMoreItens)) {
			this.btnMoreItensAction();
		}

		if (e.getSource().equals(btnPickNumber)) {
			ProtocolNumberSearch cs = new ProtocolNumberSearch();
			super.getDesktopPane().add(cs, 0);
		}
	}

	private void btnMoreItensAction() {
		this.txtNumber.setEditable(false);
		this.cbClient.setEnabled(false);
		this.cbDepartment.setEnabled(true);
		btnUpdate.setEnabled(false);

		int size = model.getRowCount();
		for (int i = 0; i < size; i++) {
			model.removeRow(0);
		}

		this.cbRequester.setSelectedIndex(0);
		btnOK.setEnabled(true);
	}

	private void btnManualNumberAction() {
		String value = JOptionPane.showInputDialog(this, "Digite o número do protocolo:");

		try {
			Long.parseLong(value);
			this.txtNumber.setText(value);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Digite um número válido.");
		}
	}

	private void btnRemoveBoxesAction() {
		int[] rows = this.tblBoxes.getSelectedRows();

		for (int i = 0; i < rows.length; i++) {
			this.model.removeRow(rows[i]);

			// diminui 1 de cada uma das posições restantes no array pq foi
			// retirada uma linha do modelo
			for (int j = i + 1; j < rows.length; j++) {
				rows[j] = rows[j] - 1;
			}
		}
	}

	@Override
	public void btnUpdateAction() {
		if (this.validateFields()) {
			if (this.verifyBoxAvailability()) {
				this.update();
				JOptionPane.showMessageDialog(this, "Atualizado com sucesso!");
				this.txtNumber.setText("");
				this.cbClient.setSelectedIndex(0);
				this.cbDepartment.setSelectedIndex(0);
				this.txtNumber.setEditable(true);
				this.cbClient.setEnabled(true);
				this.cbDepartment.setEditable(true);
				this.btnUpdate.setEnabled(false);
				this.btnManualNUmber.setEnabled(false);

				int size = model.getRowCount();
				for (int i = 0; i < size; i++) {
					model.removeRow(0);
				}

				this.cbRequester.setSelectedIndex(0);
				btnOK.setEnabled(true);
			}
		}
	}

	public JTable getTblBoxes() {
		return tblBoxes;
	}

	public void setTblBoxes(JTable tblBoxes) {
		this.tblBoxes = tblBoxes;
	}

	public DefaultTableModel getModel() {
		return model;
	}

	public void setModel(DefaultTableModel model) {
		this.model = model;
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	public JComboBox getCbClient() {
		return cbClient;
	}

	public void setCbClient(JComboBox cbCompany) {
		this.cbClient = cbCompany;
	}

	public JTextField getTxtNumber() {
		return txtNumber;
	}

	public void setTxtNumber(JTextField txtNumber) {
		this.txtNumber = txtNumber;
	}

	public JTextField getTxtSelectedProtocol() {
		return txtSelectedProtocol;
	}

	public void setTxtSelectedProtocol(JTextField txtSelectedProtocol) {
		this.txtSelectedProtocol = txtSelectedProtocol;
	}

	public JButton getBtnManualNUmber() {
		return btnManualNUmber;
	}

	public void setBtnManualNUmber(JButton btnIvalidate) {
		this.btnManualNUmber = btnIvalidate;
	}

	public JComboBox getCbDepartment() {
		return cbDepartment;
	}

	public void setCbDepartment(JComboBox cbDepartment) {
		this.cbDepartment = cbDepartment;
	}

	public SubProtocolDTO getSelectedSubprotocol() {
		return selectedSubprotocol;
	}

	public void setSelectedSubprotocol(SubProtocolDTO selectedSubprotocol) {
		this.selectedSubprotocol = selectedSubprotocol;
	}

	public JButton getBtnMoreItens() {
		return btnMoreItens;
	}

	public void setBtnMoreItens(JButton btnMoreItens) {
		this.btnMoreItens = btnMoreItens;
	}

	public JComboBox getCbRequester() {
		return cbRequester;
	}

	public void setCbRequester(JComboBox cbRequester) {
		this.cbRequester = cbRequester;
	}

	public boolean isLost() {
		return lost;
	}

	public void setLost(boolean lost) {
		this.lost = lost;
	}
}
