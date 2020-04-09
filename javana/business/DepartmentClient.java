package br.com.javana.business;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.ObjectNotFoundException;

import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.ClientDTO;
import br.com.javana.dto.DepartmentClientDTO;
import br.com.javana.dto.DepartmentDTO;
import br.com.javana.persistence.dao.BaseDAO;
import br.com.javana.persistence.dao.DepartmentClientDAO;
import br.com.javana.persistence.dao.PersistenceException;
import br.com.javana.persistence.pojo.ClientPOJO;
import br.com.javana.persistence.pojo.DepartmentClientPOJO;
import br.com.javana.persistence.pojo.DepartmentPOJO;
import br.com.javana.utils.MessageLoader;

public class DepartmentClient extends AbstractBaseBusiness<DepartmentClientDTO, DepartmentClientPOJO> {

	@Override
	public List<DepartmentClientDTO> convertPojosToDTO(List<DepartmentClientPOJO> pojos) {
		List<DepartmentClientDTO> result = new ArrayList<DepartmentClientDTO>();
		List<ClientDTO> comps = new ArrayList<ClientDTO>();
		List<ClientPOJO> compPojos = new ArrayList<ClientPOJO>();
		List<DepartmentDTO> depts = new ArrayList<DepartmentDTO>();
		List<DepartmentPOJO> deptPojos = new ArrayList<DepartmentPOJO>();

		for (DepartmentClientPOJO pojo : pojos) {
			DepartmentClientDTO dto = new DepartmentClientDTO();

			compPojos.add(pojo.getClient());
			comps = BusinessDelegate.getInstance().getClient().convertPojosToDTO(compPojos);
			deptPojos.add(pojo.getDepartment());
			depts = BusinessDelegate.getInstance().getDepartment().convertPojosToDTO(deptPojos);

			dto.setClient(comps.get(0));
			dto.setDepartment(depts.get(0));
			dto.setId(pojo.getId());

			result.add(dto);
		}

		return result;
	}

	@Override
	public BaseDAO<DepartmentClientPOJO> createDAO() {
		return new DepartmentClientDAO(DepartmentClientPOJO.class);
	}

	@Override
	public DepartmentClientPOJO createPOJO(DepartmentClientDTO dto) {
		DepartmentClientPOJO pojo = new DepartmentClientPOJO();
		pojo.setClient(BusinessDelegate.getInstance().getClient().createPOJO(dto.getClient()));
		pojo.setDepartment(BusinessDelegate.getInstance().getDepartment().createPOJO(dto.getDepartment()));
		pojo.setId(dto.getId());

		return pojo;
	}

	@Override
	protected boolean verifyExistence(DepartmentClientDTO dto) throws BusinessException {
		DepartmentClientDAO dao = new DepartmentClientDAO(DepartmentClientPOJO.class);
		DepartmentClientPOJO pojo;
		try {
			pojo = dao.findByClientAndDepartment(dto.getClient().getName(), dto.getDepartment()
					.getName());
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}

		if (pojo == null) {
			return false;
		} else {
			return true;
		}
	}

	public void updateRelations(ClientDTO comp, List<DepartmentDTO> toAdd) throws BusinessException {
		ClientDTO compdto = BusinessDelegate.getInstance().getClient().findByExactName(comp.getName());
		DepartmentClientDAO dao = new DepartmentClientDAO(DepartmentClientPOJO.class);

		List<DepartmentDTO> self;
		try {
			self = BusinessDelegate.getInstance().getDepartment().convertPojosToDTO(
					dao.findByClient(comp.getName()));
		} catch (PersistenceException e1) {
			throw new BusinessException(e1.getMessage());
		}
		boolean has = false;

		for (DepartmentDTO selfDept : self) {
			for (DepartmentDTO addDept : toAdd) {
				if (selfDept.getName().equals(addDept.getName())) {
					has = true;
				}
			}

			if (!has) {
				List<DepartmentClientPOJO> pojo = new ArrayList<DepartmentClientPOJO>();
				try {
					pojo.add(dao.findByClientAndDepartment(comp.getName(), selfDept.getName()));
				} catch (PersistenceException e) {
					throw new BusinessException(e.getMessage());
				}
				List<DepartmentClientDTO> dtos = this.convertPojosToDTO(pojo);

				DepartmentClientDTO dc = dtos.get(0);
				this.delete(dc);
			}

			has = false;
		}

		for (DepartmentDTO addDept : toAdd) {
			for (DepartmentDTO selfDept : self) {
				if (addDept.getName().equals(selfDept.getName())) {
					has = true;
				}
			}

			if (!has) {
				DepartmentClientDTO dc = new DepartmentClientDTO();
				DepartmentDTO full = BusinessDelegate.getInstance().getDepartment().findByExactName(addDept.getName());
				dc.setClient(compdto);
				dc.setDepartment(full);

				this.save(dc);
			}

			has = false;
		}
	}

	public List<DepartmentDTO> findByClient(String Client) throws BusinessException {
		DepartmentClientDAO dao = new DepartmentClientDAO(DepartmentClientPOJO.class);

		try {
			List<DepartmentPOJO> pojos = dao.findByClient(Client);

			return BusinessDelegate.getInstance().getDepartment().convertPojosToDTO(pojos);

		} catch (ObjectNotFoundException e) {
			throw new BusinessException(MessageLoader.getInstance().getMessage(3));
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	public DepartmentClientDTO findByClientDepartment(String client, String department) throws BusinessException {
		DepartmentClientDAO dao = new DepartmentClientDAO(DepartmentClientPOJO.class);
		DepartmentClientPOJO pojo;
		try {
			pojo = dao.findByClientAndDepartment(client, department);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
		List<DepartmentClientPOJO> pojos = new ArrayList<DepartmentClientPOJO>();
		pojos.add(pojo);

		List<DepartmentClientDTO> dtos = this.convertPojosToDTO(pojos);

		return dtos.get(0);
	}

	public DepartmentClientDTO convertPojoToDTO(DepartmentClientPOJO pojo) {
		DepartmentClientDTO dto = new DepartmentClientDTO();
		dto.setClient(BusinessDelegate.getInstance().getClient().convertPojoToDTO(pojo.getClient()));
		dto.setDepartment(BusinessDelegate.getInstance().getDepartment().convertPojosToDTO(pojo.getDepartment()));
		dto.setId(pojo.getId());

		return dto;
	}

	public List<DepartmentClientDTO> findByClientId(long id) throws BusinessException {
		DepartmentClientDAO dao = new DepartmentClientDAO(DepartmentClientPOJO.class);
		try {
			return this.convertPojosToDTO(dao.findByClientId(id));
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
	}

}
