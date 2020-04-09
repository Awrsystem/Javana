package br.com.javana.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.SubProtocolDTO;
import br.com.javana.persistence.dao.BaseDAO;
import br.com.javana.persistence.dao.BoxSubprotocolDAO;
import br.com.javana.persistence.dao.PersistenceException;
import br.com.javana.persistence.dao.SubProtocolDAO;
import br.com.javana.persistence.pojo.BoxSubProtocolPOJO;
import br.com.javana.persistence.pojo.SubProtocolPOJO;
import br.com.javana.utils.MessageLoader;

public class SubProtocol extends AbstractBaseBusiness<SubProtocolDTO, SubProtocolPOJO> {

	@Override
	public List<SubProtocolDTO> convertPojosToDTO(List<SubProtocolPOJO> pojos) {
		List<SubProtocolDTO> result = new ArrayList<SubProtocolDTO>();

		for (SubProtocolPOJO p : pojos) {
			SubProtocolDTO dto = new SubProtocolDTO();
			dto.setId(p.getId());
			dto.setRequester(BusinessDelegate.getInstance().getUser().convertPojoToDTO(p.getRequester()));
			dto.setProtocol(BusinessDelegate.getInstance().getProtocol().convertPojoToDTO(p.getProtocol()));

			result.add(dto);
		}

		return result;
	}

	@Override
	public BaseDAO<SubProtocolPOJO> createDAO() {
		return new SubProtocolDAO(SubProtocolPOJO.class);
	}

	@Override
	public SubProtocolPOJO createPOJO(SubProtocolDTO dto) {
		SubProtocolPOJO pojo = new SubProtocolPOJO();
		pojo.setId(dto.getId());
		pojo.setRequester(BusinessDelegate.getInstance().getUser().createPOJO(dto.getRequester()));
		pojo.setProtocol(BusinessDelegate.getInstance().getProtocol().createPOJO(dto.getProtocol()));

		return pojo;
	}

	@Override
	protected boolean verifyExistence(SubProtocolDTO dto) throws BusinessException {
		return false;
	}

	public SubProtocolDTO convertPojoToDTO(SubProtocolPOJO subProtocol) {
		SubProtocolDTO dto = new SubProtocolDTO();
		dto.setId(subProtocol.getId());
		dto.setRequester(BusinessDelegate.getInstance().getUser().convertPojoToDTO(subProtocol.getRequester()));
		dto.setProtocol(BusinessDelegate.getInstance().getProtocol().convertPojoToDTO(subProtocol.getProtocol()));

		return dto;
	}

	public SubProtocolDTO findById(Long subId) throws BusinessException {
		SubProtocolDAO dao = new SubProtocolDAO(SubProtocolPOJO.class);

		try {
			SubProtocolPOJO pojo = dao.findByPK(subId);
			return this.convertPojoToDTO(pojo);
		} catch (Exception e) {
			throw new BusinessException(MessageLoader.getInstance().getMessage(9));
		}

	}

	public List<SubProtocolDTO> findByProtocolId(long protocolId) throws BusinessException {
		SubProtocolDAO dao = new SubProtocolDAO(SubProtocolPOJO.class);
		List<SubProtocolPOJO> pojos;
		try {
			pojos = dao.findByProtocolId(protocolId);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
		return this.convertPojosToDTO(pojos);
	}

	public List<SubProtocolDTO> findAllByProtocolNumber(Long protocolNumber) throws BusinessException {
		SubProtocolDAO dao = new SubProtocolDAO(SubProtocolPOJO.class);
		List<SubProtocolPOJO> pojos;
		try {
			pojos = dao.findByProtocolNumber(protocolNumber);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
		return this.convertPojosToDTO(pojos);
	}

	public void update(SubProtocolDTO selectedSubprotocol, List<Long> add) throws BusinessException {
		SubProtocolDAO dao = new SubProtocolDAO(SubProtocolPOJO.class);
		BoxSubprotocolDAO boxSubDAO = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);

		try {
			dao.update(this.createPOJO(selectedSubprotocol));
			List<Long> self = boxSubDAO.findBoxCodesBySubId(selectedSubprotocol);

			for (Long code : add) {
				if (add.contains(code) && !self.contains(code)) {
					BoxSubProtocolPOJO pojo = new BoxSubProtocolPOJO();
					pojo.setSubprotocol(this.createPOJO(selectedSubprotocol));
					pojo.setBox(BusinessDelegate.getInstance().getBox().createPOJO(
							BusinessDelegate.getInstance().getBox().findByCode(code)));
					boxSubDAO.insert(pojo);
				}

				if (!add.contains(code) && self.contains(code)) {
					boxSubDAO.delete(BusinessDelegate.getInstance().getBoxSubprotocol().findByBoxCodeAndSubId(code,
							selectedSubprotocol.getId()));
				}
			}

			for (Long code : self) {
				if (add.contains(code) && !self.contains(code)) {
					BoxSubProtocolPOJO pojo = new BoxSubProtocolPOJO();
					pojo.setSubprotocol(this.createPOJO(selectedSubprotocol));
					pojo.setBox(BusinessDelegate.getInstance().getBox().createPOJO(
							BusinessDelegate.getInstance().getBox().findByCode(code)));
					boxSubDAO.insert(pojo);
				}

				if (!add.contains(code) && self.contains(code)) {
					BoxSubProtocolPOJO pojo = BusinessDelegate.getInstance().getBoxSubprotocol().findByBoxCodeAndSubId(
							code, selectedSubprotocol.getId());
					boxSubDAO.delete(pojo);
				}
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	public List<SubProtocolPOJO> findByIds(Set<Long> subIds) throws BusinessException {
		SubProtocolDAO dao = new SubProtocolDAO(SubProtocolPOJO.class);
		try {
			return dao.findByIds(subIds);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
	}
}
