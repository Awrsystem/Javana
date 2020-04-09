package br.com.javana.business;

import java.util.ArrayList;
import java.util.List;

import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.UserDTO;
import br.com.javana.persistence.dao.BaseDAO;
import br.com.javana.persistence.dao.PersistenceException;
import br.com.javana.persistence.dao.UserDAO;
import br.com.javana.persistence.pojo.UserPOJO;

public class User extends AbstractBaseBusiness<UserDTO, UserPOJO> {

	@Override
	public List<UserDTO> convertPojosToDTO(List<UserPOJO> pojos) {
		List<UserDTO> list = new ArrayList<UserDTO>();

		for (UserPOJO pojo : pojos) {
			UserDTO dto = new UserDTO();
			dto.setDeptClient(BusinessDelegate.getInstance().getDeptClient().convertPojoToDTO(pojo.getDeptClient()));
			dto.setId(pojo.getId());
			dto.setName(pojo.getName());
			list.add(dto);
		}

		return list;
	}

	@Override
	public BaseDAO<UserPOJO> createDAO() {
		return new UserDAO(UserPOJO.class);
	}

	@Override
	public UserPOJO createPOJO(UserDTO dto) {
		UserPOJO pojo = new UserPOJO();
		pojo.setDeptClient(BusinessDelegate.getInstance().getDeptClient().createPOJO(dto.getDeptClient()));

		if (dto.getId() != 0) {
			pojo.setId(dto.getId());
		}

		pojo.setName(dto.getName());
		return pojo;
	}

	@Override
	protected boolean verifyExistence(UserDTO dto) throws BusinessException {
		return false;
	}

	public List<UserDTO> findByClientDept(String client, String department) throws BusinessException {
		UserDAO dao = new UserDAO(UserPOJO.class);
		try {
			return this.convertPojosToDTO(dao.findByClientDept(client, department));
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	public UserDTO convertPojoToDTO(UserPOJO requester) {
		UserDTO dto = new UserDTO();
		dto.setDeptClient(BusinessDelegate.getInstance().getDeptClient().convertPojoToDTO(requester.getDeptClient()));
		dto.setId(requester.getId());
		dto.setName(requester.getName());

		return dto;
	}

	public List<UserDTO> findByClientId(long id) throws BusinessException {
		UserDAO dao = new UserDAO(UserPOJO.class);
		try {
			return this.convertPojosToDTO(dao.findByClientId(id));
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
	}
}
