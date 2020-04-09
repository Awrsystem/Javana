package br.com.javana.business;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.BaseDTO;
import br.com.javana.dto.BoxDTO;
import br.com.javana.dto.RemovedBoxDTO;
import br.com.javana.persistence.dao.BaseDAO;
import br.com.javana.persistence.dao.PersistenceException;
import br.com.javana.persistence.dao.RemovedBoxDAO;
import br.com.javana.persistence.pojo.BoxPOJO;
import br.com.javana.persistence.pojo.RemovedBoxPOJO;
import br.com.javana.utils.MessageLoader;

public class RemovedBox extends AbstractBaseBusiness<RemovedBoxDTO, RemovedBoxPOJO> {

	@Override
	public List<RemovedBoxDTO> convertPojosToDTO(List<RemovedBoxPOJO> pojos) {
		List<RemovedBoxDTO> dtos = new ArrayList<RemovedBoxDTO>();

		for (RemovedBoxPOJO pojo : pojos) {
			RemovedBoxDTO dto = new RemovedBoxDTO();
			dto.setClient(pojo.getClient());
			dto.setCode(pojo.getCode());
			dto.setDepartment(pojo.getDepartment());
			dto.setId(pojo.getId());
			dto.setRemovalDate(pojo.getRemovalDate());
			dto.setRequester(pojo.getRequester());

			dtos.add(dto);
		}

		return dtos;
	}

	@Override
	public BaseDAO<RemovedBoxPOJO> createDAO() {
		return new RemovedBoxDAO(RemovedBoxPOJO.class);
	}

	@Override
	protected RemovedBoxPOJO createPOJO(RemovedBoxDTO dto) {
		RemovedBoxPOJO pojo = new RemovedBoxPOJO();
		pojo.setClient(dto.getClient());
		pojo.setCode(dto.getCode());
		pojo.setDepartment(dto.getDepartment());
		pojo.setId(pojo.getId());
		pojo.setRemovalDate(dto.getRemovalDate());
		pojo.setRequester(dto.getRequester());
		return pojo;
	}

	@Override
	protected boolean verifyExistence(RemovedBoxDTO dto) throws BusinessException {
		/*
		 * Não faz sentido verificar se o registro ja existe pois esta tabela
		 * existe somente com o proposito de log, e uma caixa com o mesmo numero
		 * pode ter sido excluido varias vezes
		 */
		return false;
	}

	public void removeBoxes(String requester, List<Long> codes) throws BusinessException {
		save(requester, codes);

		BusinessDelegate.getInstance().getBox().deleteByBoxCodes(codes);

	}

	private void save(String requester, List<Long> codes) throws BusinessException {
		RemovedBoxDAO dao = new RemovedBoxDAO(RemovedBoxPOJO.class);

		for (Long code : codes) {
			BoxDTO boxDTO = null;
			boxDTO = BusinessDelegate.getInstance().getBox().findByCode(code);

			RemovedBoxPOJO pojo = new RemovedBoxPOJO();
			pojo.setClient(boxDTO.getDeptClient().getClient().getName());
			pojo.setCode(boxDTO.getCode());
			pojo.setDepartment(boxDTO.getDeptClient().getDepartment().getName());
			pojo.setRemovalDate(new Timestamp(System.currentTimeMillis()));
			pojo.setRequester(requester);

			try {
				dao.insert(pojo);
			} catch (PersistenceException e) {
				throw new BusinessException(MessageLoader.getInstance().getMessage(8));
			}
		}
	}

	public List<RemovedBoxDTO> findByParams(Map<String, String> params) throws BusinessException {
		List<RemovedBoxPOJO> interval;
		RemovedBoxDAO dao = new RemovedBoxDAO(RemovedBoxPOJO.class);
		try {
			interval = dao.findBoxesByParams(params);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
		return this.convertPojosToDTO(interval);
	}
}
