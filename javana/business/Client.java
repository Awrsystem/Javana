package br.com.javana.business;

import java.util.ArrayList;
import java.util.List;

import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.BoxDTO;
import br.com.javana.dto.ClientDTO;
import br.com.javana.dto.DepartmentClientDTO;
import br.com.javana.dto.UserDTO;
import br.com.javana.persistence.dao.BaseDAO;
import br.com.javana.persistence.dao.ClientDAO;
import br.com.javana.persistence.pojo.BoxSubProtocolPOJO;
import br.com.javana.persistence.pojo.ClientPOJO;

/**
 * @author Rafael Classe com a logica de negocio relativa a um cliente
 */
public class Client extends AbstractBaseBusiness<ClientDTO, ClientPOJO> {

	/**
	 * Cria um pojo baseado no dto que foi passado como parametro
	 * 
	 * @param dto
	 * @return
	 */
	@Override
	public ClientPOJO createPOJO(ClientDTO dto) {
		ClientPOJO pojo = new ClientPOJO();
		pojo.setBairro(dto.getBairro());
		pojo.setCity(dto.getCity());
		pojo.setEmail(dto.getEmail());
		pojo.setName(dto.getName());
		pojo.setNumber(dto.getNumber());
		pojo.setPhone(dto.getPhone());
		pojo.setStreet(dto.getStreet());
		pojo.setZipcode(dto.getZipcode());

		if (dto.getId() != 0) {
			pojo.setId(dto.getId());
		}

		return pojo;
	}

	@Override
	protected boolean verifyExistence(ClientDTO dto) throws BusinessException {
		if (this.findByExactName(dto.getName()) != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<ClientDTO> convertPojosToDTO(List<ClientPOJO> pojos) {
		List<ClientDTO> dtos = new ArrayList<ClientDTO>();

		for (ClientPOJO c : pojos) {
			ClientDTO dto = new ClientDTO();
			dto.setBairro(c.getBairro());
			dto.setCity(c.getCity());
			dto.setEmail(c.getEmail());
			dto.setId(c.getId());
			dto.setName(c.getName());
			dto.setNumber(c.getNumber());
			dto.setPhone(c.getPhone());
			dto.setStreet(c.getStreet());
			dto.setZipcode(c.getZipcode());

			dtos.add(dto);
		}

		return dtos;
	}

	@Override
	public BaseDAO<ClientPOJO> createDAO() {
		return new ClientDAO(ClientPOJO.class);
	}

	public ClientDTO convertPojoToDTO(ClientPOJO client) {
		ClientDTO dto = new ClientDTO();
		dto.setBairro(client.getBairro());
		dto.setCity(client.getCity());
		dto.setEmail(client.getEmail());
		dto.setId(client.getId());
		dto.setName(client.getName());
		dto.setNumber(client.getNumber());
		dto.setPhone(client.getPhone());
		dto.setStreet(client.getStreet());
		dto.setZipcode(client.getZipcode());

		return dto;
	}

	public void delete(ClientDTO dto) throws BusinessException {
		// vai apagando todas as referencias que tem relação com o cliente
		long id = dto.getId();
		BusinessDelegate bd = BusinessDelegate.getInstance();
		try {
			// apaga os protocolos e sub protocolos
			List<BoxSubProtocolPOJO> boxsub = BusinessDelegate.getInstance()
					.getBoxSubprotocol().findByClientId(id);

			for (BoxSubProtocolPOJO pojo : boxsub) {
				bd.getBoxSubprotocol().delete(
						bd.getBoxSubprotocol().convertPojosToDTO(pojo));
				bd.getSubprotocol().delete(
						bd.getSubprotocol().convertPojoToDTO(
								pojo.getSubprotocol()));
				bd.getProtocol().delete(
						bd.getProtocol().convertPojoToDTO(
								pojo.getSubprotocol().getProtocol()));
			}

			// apaga as caixas
			List<BoxDTO> boxes = BusinessDelegate.getInstance().getBox()
					.findByClientId(id);
			if (boxes.size() > 0) {
				List<Long> codes = new ArrayList<Long>();

				for (BoxDTO box : boxes) {
					codes.add(box.getId());
				}

				BusinessDelegate.getInstance().getRemovedBox().removeBoxes(
						"Cliente excluido", codes);
			}

			// apaga as pessoas
			List<UserDTO> users = BusinessDelegate.getInstance().getUser()
					.findByClientId(id);
			for (UserDTO u : users) {
				BusinessDelegate.getInstance().getUser().delete(u);
			}

			// apaga os relacionamentos com setor
			List<DepartmentClientDTO> dcs = BusinessDelegate.getInstance()
					.getDeptClient().findByClientId(id);

			for (DepartmentClientDTO dc : dcs) {
				BusinessDelegate.getInstance().getDeptClient().delete(dc);
			}

			super.delete(dto);

		} catch (BusinessException e) {
			throw e;
		}
	}

}
