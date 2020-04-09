package br.com.javana.business;

import java.util.ArrayList;
import java.util.List;

import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.LostProtocolNumberDTO;
import br.com.javana.persistence.dao.BaseDAO;
import br.com.javana.persistence.dao.LostProtocolNumberDAO;
import br.com.javana.persistence.dao.PersistenceException;
import br.com.javana.persistence.pojo.LostProtocolNumberPOJO;
import br.com.javana.persistence.pojo.ProtocolPOJO;
import br.com.javana.utils.MessageLoader;

public class LostProtocolNumber extends
		AbstractBaseBusiness<LostProtocolNumberDTO, LostProtocolNumberPOJO> {

	@Override
	public List<LostProtocolNumberDTO> convertPojosToDTO(
			List<LostProtocolNumberPOJO> pojos) {
		List<LostProtocolNumberDTO> dtos = new ArrayList<LostProtocolNumberDTO>();

		for (LostProtocolNumberPOJO pojo : pojos) {
			LostProtocolNumberDTO dto = new LostProtocolNumberDTO();
			dto.setId(pojo.getId());
			dto.setNumber(pojo.getNumber());
			dtos.add(dto);
		}

		return dtos;
	}

	@Override
	public BaseDAO<LostProtocolNumberPOJO> createDAO() {
		return new LostProtocolNumberDAO(LostProtocolNumberPOJO.class);
	}

	@Override
	protected LostProtocolNumberPOJO createPOJO(LostProtocolNumberDTO dto) {
		LostProtocolNumberPOJO pojo = new LostProtocolNumberPOJO();
		pojo.setId(dto.getId());
		pojo.setNumber(dto.getNumber());

		return pojo;
	}

	@Override
	protected boolean verifyExistence(LostProtocolNumberDTO dto)
			throws BusinessException {
		if (BusinessDelegate.getInstance().getProtocol().findByNumber(
				dto.getNumber(), true) == null) {
			return true;
		}
		return false;
	}

	public List<Long> calculateNumbers() throws BusinessException {
		List<ProtocolPOJO> pojos = BusinessDelegate.getInstance().getProtocol()
				.getAllNumbers();
		List<Long> codes = new ArrayList<Long>();

		if (pojos.get(0).getNumber() > 1) {
			long aux = 1;
			while (aux < pojos.get(0).getNumber()) {
				codes.add(aux);
				aux++;
			}
		}

		for (int i = 0; i < pojos.size() - 1; i++) {
			long b = pojos.get(i + 1).getNumber();
			long a = pojos.get(i).getNumber();
			if (b > a + 1) {
				long aux = pojos.get(i).getNumber();
				while (aux < (pojos.get(i + 1).getNumber() - 1)) {
					aux++;
					codes.add(aux);
				}
			}
		}

		BaseDAO<LostProtocolNumberPOJO> dao = this.createDAO();

		// antes de inserir, apaga todos os que já estão na base
		try {
			List<LostProtocolNumberPOJO> all = dao.findAll();
			for (LostProtocolNumberPOJO pojo : all) {
				dao.delete(pojo);
			}
		} catch (Exception e1) {
			throw new BusinessException(MessageLoader.getInstance().getMessage(5));
		}

		for (Long code : codes) {
			LostProtocolNumberPOJO pojo = new LostProtocolNumberPOJO();
			pojo.setNumber(code);
			try {
				dao.insert(pojo);
			} catch (PersistenceException e) {
				throw new BusinessException(MessageLoader.getInstance().getMessage(5));
			}
		}

		return codes;
	}

	public void deleteByNumber(LostProtocolNumberDTO dto) throws BusinessException {
		LostProtocolNumberDAO dao = new LostProtocolNumberDAO(
				LostProtocolNumberPOJO.class);
		try {
			dao.deleteByNumber(dto.getNumber());
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
	}

}
