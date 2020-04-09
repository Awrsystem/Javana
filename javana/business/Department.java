package br.com.javana.business;

import java.util.ArrayList;
import java.util.List;

import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.DepartmentDTO;
import br.com.javana.persistence.dao.BaseDAO;
import br.com.javana.persistence.dao.DepartmentDAO;
import br.com.javana.persistence.pojo.DepartmentPOJO;

public class Department extends AbstractBaseBusiness<DepartmentDTO, DepartmentPOJO> {
	protected DepartmentDAO createDepartmentDAO() throws Exception {
		return new DepartmentDAO(DepartmentPOJO.class);
	}

	@Override
	public DepartmentPOJO createPOJO(DepartmentDTO dto) {
		DepartmentPOJO pojo = new DepartmentPOJO();
		pojo.setId(dto.getId());
		pojo.setName(dto.getName());

		return pojo;
	}

	@Override
	protected boolean verifyExistence(DepartmentDTO dto) throws BusinessException {
		if (this.findByExactName(dto.getName()) != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<DepartmentDTO> convertPojosToDTO(List<DepartmentPOJO> departments) {
		List<DepartmentDTO> dtos = new ArrayList<DepartmentDTO>();
		for (DepartmentPOJO pojo : departments) {
			DepartmentDTO dto = new DepartmentDTO();
			dto.setId(pojo.getId());
			dto.setName(pojo.getName());

			dtos.add(dto);
		}

		return dtos;
	}

	@Override
	public BaseDAO<DepartmentPOJO> createDAO() {
		return new DepartmentDAO(DepartmentPOJO.class);
	}

	public DepartmentDTO convertPojosToDTO(DepartmentPOJO pojo) {
		DepartmentDTO dto = new DepartmentDTO();
		dto.setId(pojo.getId());
		dto.setName(pojo.getName());

		return dto;
	}
}
