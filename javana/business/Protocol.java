package br.com.javana.business;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.BoxSubprotocolDTO;
import br.com.javana.dto.ProtocolDTO;
import br.com.javana.dto.SubProtocolDTO;
import br.com.javana.persistence.dao.BaseDAO;
import br.com.javana.persistence.dao.BoxSubprotocolDAO;
import br.com.javana.persistence.dao.LostProtocolNumberDAO;
import br.com.javana.persistence.dao.PersistenceException;
import br.com.javana.persistence.dao.ProtocolDAO;
import br.com.javana.persistence.pojo.BoxSubProtocolPOJO;
import br.com.javana.persistence.pojo.LostProtocolNumberPOJO;
import br.com.javana.persistence.pojo.ProtocolPOJO;
import br.com.javana.utils.MessageLoader;

public class Protocol extends AbstractBaseBusiness<ProtocolDTO, ProtocolPOJO> {
	@Override
	public List<ProtocolDTO> convertPojosToDTO(List<ProtocolPOJO> pojos) {
		List<ProtocolDTO> dtos = new ArrayList<ProtocolDTO>();

		for (ProtocolPOJO p : pojos) {
			ProtocolDTO dto = new ProtocolDTO();
			dto.setId(p.getId());
			dto.setLeaveDate(p.getLeaveDate());
			dto.setNumber(p.getNumber());
			dto.setStatus(p.getStatus());

			dtos.add(dto);
		}

		return dtos;
	}

	@Override
	public BaseDAO<ProtocolPOJO> createDAO() {
		return new ProtocolDAO(ProtocolPOJO.class);
	}

	@Override
	public ProtocolPOJO createPOJO(ProtocolDTO dto) {
		ProtocolPOJO pojo = new ProtocolPOJO();
		pojo.setId(dto.getId());
		pojo.setLeaveDate(dto.getLeaveDate());
		pojo.setNumber(dto.getNumber());
		pojo.setStatus(dto.getStatus());

		return pojo;
	}

	@Override
	protected boolean verifyExistence(ProtocolDTO dto) throws BusinessException {
		if (this.findByNumber(dto.getNumber(), true) != null) {
			return true;
		} else {
			return false;
		}
	}

	public ProtocolDTO findByNumber(long number, boolean invalid) throws BusinessException {
		try {
			ProtocolDAO dao = new ProtocolDAO(ProtocolPOJO.class);
			ProtocolPOJO pojo = dao.findByNumber(number, invalid);

			if (pojo == null) {
				return null;
			} else {
				List<ProtocolPOJO> pojos = new ArrayList<ProtocolPOJO>();
				pojos.add(pojo);
				List<ProtocolDTO> dtos = this.convertPojosToDTO(pojos);

				return dtos.get(0);
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	public ProtocolDTO convertPojoToDTO(ProtocolPOJO pojo) {
		ProtocolDTO dto = new ProtocolDTO();
		dto.setId(pojo.getId());
		dto.setLeaveDate(pojo.getLeaveDate());
		dto.setNumber(pojo.getNumber());
		dto.setStatus(pojo.getStatus());
		return dto;
	}

	public void invalidate(long number) throws BusinessException {
		ProtocolDAO dao = new ProtocolDAO(ProtocolPOJO.class);
		ProtocolPOJO pojo;
		try {
			pojo = dao.findByNumber(number, false);
		} catch (PersistenceException e1) {
			throw new BusinessException(e1.getMessage());
		}

		this.protocolReturn(pojo.getId());

		pojo.setStatus(1);
		try {
			dao.update(pojo);
		} catch (Exception e) {
			throw new BusinessException(MessageLoader.getInstance().getMessage(6));
		}
	}

	/**
	 * Baixa todas as caixas do protocolo pq ele foi invalidado
	 * 
	 * @param id
	 * @throws BusinessException
	 */
	private void protocolReturn(long id) throws BusinessException {
		BoxSubprotocolDAO dao = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);
		List<SubProtocolDTO> subs = BusinessDelegate.getInstance().getSubprotocol().findByProtocolId(id);

		for (SubProtocolDTO dto : subs) {
			List<BoxSubprotocolDTO> boxes = BusinessDelegate.getInstance().getBoxSubprotocol().findBoxesBySubId(
					dto.getId());

			for (BoxSubprotocolDTO box : boxes) {
				BoxSubProtocolPOJO pojo = BusinessDelegate.getInstance().getBoxSubprotocol().createPOJO(box);
				pojo.setBackDate(new Timestamp(System.currentTimeMillis()));
				try {
					dao.update(pojo);
				} catch (Exception e) {
					throw new BusinessException(MessageLoader.getInstance().getMessage(7));
				}
			}

		}

	}

	public long pickANumber() {
		LostProtocolNumberDAO dao = new LostProtocolNumberDAO(LostProtocolNumberPOJO.class);
		long lastNumber = dao.getFirst();

		return lastNumber;
	}

	public List<ProtocolPOJO> getAllNumbers() throws BusinessException {
		ProtocolDAO dao = new ProtocolDAO(ProtocolPOJO.class);

		try {
			return dao.getAllNumbers();
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
	}

}
