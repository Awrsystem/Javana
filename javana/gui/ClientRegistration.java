package br.com.javana.gui;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import br.com.javana.business.BusinessDelegate;
import br.com.javana.business.ChildException;
import br.com.javana.business.ObjectAlreadyExistentException;
import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.ClientDTO;

/**
 * Tela de cadastro de clientes
 */
public class ClientRegistration extends AbstractFrame {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(ClientRegistration.class);

	/**
	 * Panel onde os elementos são posicionados
	 */
	private JPanel panel;

	/**
	 * Label cliente:
	 */
	private JLabel lblClientName;

	/**
	 * Campo texto cidade
	 */
	private JTextField txtClientCity;

	/**
	 * Label cidade:
	 */
	private JLabel lblClientCity;

	/**
	 * Campo texto bairro
	 */
	private JTextField txtClientBairro;

	/**
	 * Label Bairro:
	 */
	private JLabel lblClientBairro;

	/**
	 * Campo texto Número (do endereço)
	 */
	private JTextField txtClientNumber;

	/**
	 * Label Número: (do endereço)
	 */
	private JLabel lblClientNumber;

	/**
	 * Campo texto e-mail
	 */
	private JTextField txtClientEmail;

	/**
	 * Label E-mail
	 */
	private JLabel lblClientEmail;

	/**
	 * Campo texto do telefone
	 */
	private JTextField txtClientPhone;

	/**
	 * Label telefone
	 */
	private JLabel lblClientPhone;

	/**
	 * Campo texto cep
	 */
	private JTextField txtClientZipcode;

	/**
	 * Label cep
	 */
	private JLabel lblClientZipcode;

	/**
	 * Campo texto rua
	 */
	private JTextField txtClientStreet;

	/**
	 * Label rua
	 */
	private JLabel lblClientStreet;

	/**
	 * Campo texto nome
	 */
	private JTextField txtClientName;

	/**
	 * Campo escondido que guarda o id do cliente selecionado para atualização
	 * ou exclusão
	 */
	private JTextField txtSelectedClient;
	
	private JButton btnValues;

	public ClientRegistration() {
		super();
	}

	/**
	 * Posiciona os elemenos no panel
	 */
	@Override
	public void createPanel() {
		panel = new JPanel();
		TableLayout panelLayout = new TableLayout(new double[][] {
				{ 10.0, 61.0, TableLayout.FILL, 105.0, 37.0, 60.0, 10.0 },
				{ 10.0, 24.0, 24.0, 24.0, 24.0, 24.0, 24.0, 24.0, 24.0, 24.0, 24.0, 10.0, 10.0 } });
		panelLayout.setHGap(5);
		panelLayout.setVGap(5);
		panel.setLayout(panelLayout);
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setPreferredSize(new java.awt.Dimension(395, 314));

		lblClientName = new JLabel();
		panel.add(lblClientName, "1, 1, r, f");
		lblClientName.setText("Nome:");

		txtClientName = new JTextField();
		panel.add(txtClientName, "2, 1, 3, 1");

		btnOK = new JButton();
		panel.add(btnOK, "3, 10");
		btnOK.setText("OK");
		btnOK.addActionListener(this);

		btnCancel = new JButton();
		panel.add(btnCancel, "4, 10, 5, 10");
		btnCancel.setText("Cancelar");
		btnCancel.addActionListener(this);

		lblClientStreet = new JLabel();
		panel.add(lblClientStreet, "1, 2, r, f");
		lblClientStreet.setText("Rua:");

		txtClientStreet = new JTextField();
		panel.add(txtClientStreet, "2, 2, 3, 2");

		lblClientNumber = new JLabel();
		panel.add(lblClientNumber, "1, 3, r, f");
		lblClientNumber.setText("Número:");

		txtClientNumber = new JTextField();
		panel.add(txtClientNumber, "2, 3");

		lblClientBairro = new JLabel();
		panel.add(lblClientBairro, "1, 4, r, f");
		lblClientBairro.setText("Bairro:");

		txtClientBairro = new JTextField();
		panel.add(txtClientBairro, "2, 4, 3, 4");

		lblClientCity = new JLabel();
		panel.add(lblClientCity, "1, 5, r, f");
		lblClientCity.setText("Cidade:");

		txtClientCity = new JTextField();
		panel.add(txtClientCity, "2, 5, 3, 5");

		lblClientZipcode = new JLabel();
		panel.add(lblClientZipcode, "1, 6, r, f");
		lblClientZipcode.setText("CEP:");

		txtClientZipcode = new JTextField();
		panel.add(txtClientZipcode, "2, 6");

		lblClientPhone = new JLabel();
		panel.add(lblClientPhone, "1, 7, r, f");
		lblClientPhone.setText("Telefone:");

		txtClientPhone = new JTextField();
		panel.add(txtClientPhone, "2, 7");

		lblClientEmail = new JLabel();
		panel.add(lblClientEmail, "1, 8, r, f");
		lblClientEmail.setText("E-mail:");

		txtClientEmail = new JTextField();
		panel.add(txtClientEmail, "2, 8, 3, 8");

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
		
		btnValues = new JButton();
		panel.add(btnValues, "4, 5, 5, 5");
		btnValues.setText("Valores");
		btnValues.setEnabled(false);
		btnValues.addActionListener(this);

		txtSelectedClient = new JTextField();
		panel.add(txtSelectedClient, "5, 4");
		txtSelectedClient.setVisible(false);
	}

	@Override
	public void btnDeleteAction() {
		Object[] options = { "Sim", "Não" };
		int answer = JOptionPane.showOptionDialog(this, "Você tem certeza que deseja excluir este cliente?",
				"Confirme", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

		if (answer == 0) {
			try {
				if (BusinessDelegate.getInstance().getBoxSubprotocol().findOpenProtocolsByClient(
						new Long(this.txtSelectedClient.getText()).longValue())) {
					JOptionPane.showMessageDialog(this,
							"Este cliente tem protocolos abertos. Dê baixa nos protocolos antes de excluir o cliente.");
				} else {
					BusinessDelegate.getInstance().getClient().delete(this.populateDTO());
					JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
					this.resetFields();
				}

			} catch (BusinessException e1) {
				if (e1 instanceof ChildException) {
					JOptionPane.showMessageDialog(this,
							"Existem setores vinculdos a este cliente. Para excluir o cliente,"
									+ "você deve primeiro excluir a relação com os setores.");
				}
			}
		}
	}

	@Override
	public void update() {
		try {
			BusinessDelegate.getInstance().getClient().update(this.populateDTO());
		} catch (BusinessException e1) {
			JOptionPane.showMessageDialog(this, e1.getMessage());
		}

		JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
		this.resetFields();
	}

	@Override
	public void save() {
		try {
			BusinessDelegate.getInstance().getClient().save(this.populateDTO());
		} catch (ObjectAlreadyExistentException e2) {
			JOptionPane.showMessageDialog(this, "Já existe um cliente cadastrado com este nome.");
			return;
		} catch (BusinessException e1) {
			log.debug(e1);
		}

		JOptionPane.showMessageDialog(this, "Cliente incluido com sucesso!");
	}

	@Override
	public void btnSearchAction() {
		ClientSearch cs = new ClientSearch();
		super.getDesktopPane().add(cs, 0);
	}

	/**
	 * Limpa os campos e habilita o botao ok
	 */
	@Override
	public void resetFields() {
		this.txtClientBairro.setText("");
		this.txtClientCity.setText("");
		this.txtClientEmail.setText("");
		this.txtClientName.setText("");
		this.txtClientNumber.setText("");
		this.txtClientPhone.setText("");
		this.txtClientStreet.setText("");
		this.txtClientZipcode.setText("");

		btnDelete.setEnabled(false);
		btnUpdate.setEnabled(false);
		btnOK.setEnabled(true);
	}

	/**
	 * Verifica se os campos obrigatorios estao preenchidos
	 * 
	 * @return
	 */
	@Override
	public boolean validateFields() {
		if (this.txtClientName.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "O campo nome é obrigatório.");
			return false;
		} 
		
		if(!txtClientNumber.getText().equals("")){
			try{
				Integer.parseInt(txtClientNumber.getText());
				return true;
			}
			catch(NumberFormatException e){
				JOptionPane.showMessageDialog(this, "O campo numero deve conter somente numeros.");
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Cria um dto com os dados que serão incluidos ou alterados no cliente
	 */
	private ClientDTO populateDTO() {
		ClientDTO dto = new ClientDTO();
		dto.setBairro(this.txtClientBairro.getText());
		dto.setCity(this.txtClientCity.getText());
		dto.setEmail(this.txtClientEmail.getText());
		dto.setName(this.txtClientName.getText());
		if (this.txtClientNumber.getText().equals("")) {
			dto.setNumber(0);
		} else {
			dto.setNumber(new Integer(this.txtClientNumber.getText()).intValue());
		}

		dto.setPhone(this.txtClientPhone.getText());
		dto.setStreet(this.txtClientStreet.getText());
		dto.setZipcode(this.txtClientZipcode.getText());

		if (!this.txtSelectedClient.getText().equals("")) {
			dto.setId(new Integer(this.txtSelectedClient.getText()).intValue());
		}

		return dto;
	}

	public JTextField getTxtClientName() {
		return txtClientName;
	}

	public void setTxtClientName(JTextField txtClientName) {
		this.txtClientName = txtClientName;
	}

	public JTextField getTxtClientBairro() {
		return txtClientBairro;
	}

	public void setTxtClientBairro(JTextField txtClientBairro) {
		this.txtClientBairro = txtClientBairro;
	}

	public JTextField getTxtClientCity() {
		return txtClientCity;
	}

	public void setTxtClientCity(JTextField txtClientCity) {
		this.txtClientCity = txtClientCity;
	}

	public JTextField getTxtClientEmail() {
		return txtClientEmail;
	}

	public void setTxtClientEmail(JTextField txtClientEmail) {
		this.txtClientEmail = txtClientEmail;
	}

	public JTextField getTxtClientNumber() {
		return txtClientNumber;
	}

	public void setTxtClientNumber(JTextField txtClientNumber) {
		this.txtClientNumber = txtClientNumber;
	}

	public JTextField getTxtClientPhone() {
		return txtClientPhone;
	}

	public void setTxtClientPhone(JTextField txtClientPhone) {
		this.txtClientPhone = txtClientPhone;
	}

	public JTextField getTxtClientStreet() {
		return txtClientStreet;
	}

	public void setTxtClientStreet(JTextField txtClientStreet) {
		this.txtClientStreet = txtClientStreet;
	}

	public JTextField getTxtClientZipcode() {
		return txtClientZipcode;
	}

	public void setTxtClientZipcode(JTextField txtClientZipcode) {
		this.txtClientZipcode = txtClientZipcode;
	}

	public JTextField getTxtSelectedClient() {
		return txtSelectedClient;
	}

	public void setTxtSelectedClient(JTextField txtSelectedClient) {
		this.txtSelectedClient = txtSelectedClient;
	}

	@Override
	public void screenChecks() {

	}

	@Override
	public void setDimensions() {
		this.setPreferredSize(new java.awt.Dimension(422, 343));
		this.setBounds(0, 0, 422, 343);
		this.setTitle("Cadastro de Cliente");
	}
}
