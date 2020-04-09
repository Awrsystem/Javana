package br.com.javana.business;

import java.util.ArrayList;
import java.util.List;

import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.LostBoxNumbersDTO;
import br.com.javana.persistence.dao.BaseDAO;
import br.com.javana.persistence.dao.LostBoxNumberDAO;
import br.com.javana.persistence.dao.PersistenceException;
import br.com.javana.persistence.pojo.BoxPOJO;
import br.com.javana.persistence.pojo.LostBoxNumbersPOJO;
import br.com.javana.utils.MessageLoader;

public class LostBoxNumbers extends AbstractBaseBusiness<LostBoxNumbersDTO, LostBoxNumbersPOJO> {

	public List<Long> calculateNumbers() throws BusinessException {
		List<BoxPOJO> pojos = BusinessDelegate.getInstance().getBox().getAllCodes();
		List<Long> codes = new ArrayList<Long>();

		if (pojos.get(0).getCode() > 1) {
			long aux = 1;
			while (aux < pojos.get(0).getCode()) {
				codes.add(aux);
				aux++;
			}
		}

		for (int i = 0; i < pojos.size() - 1; i++) {
			long b = pojos.get(i + 1).getCode();
			long a = pojos.get(i).getCode();
			if (b > a + 1) {
				long aux = pojos.get(i).getCode();
				while (aux < (pojos.get(i + 1).getCode() - 1)) {
					aux++;
					codes.add(aux);
				}
			}
		}

		BaseDAO<LostBoxNumbersPOJO> dao = this.createDAO();

		// antes de inserir, apaga todos os que já estão na base
		try {
			List<LostBoxNumbersPOJO> all = dao.findAll();
			for (LostBoxNumbersPOJO pojo : all) {
				dao.delete(pojo);
			}
		} catch (Exception e1) {
			throw new BusinessException(MessageLoader.getInstance().getMessage(4));
		}

		for (Long code : codes) {
			LostBoxNumbersPOJO pojo = new LostBoxNumbersPOJO();
			pojo.setNumber(code);
			try {
				dao.insert(pojo);
			} catch (PersistenceException e) {
				throw new BusinessException(MessageLoader.getInstance().getMessage(4));
			}
		}

		return codes;

	}

	@Override
	public List<LostBoxNumbersDTO> convertPojosToDTO(List<LostBoxNumbersPOJO> pojos) {
		List<LostBoxNumbersDTO> dtos = new ArrayList<LostBoxNumbersDTO>();
		for (LostBoxNumbersPOJO pojo : pojos) {
			LostBoxNumbersDTO dto = new LostBoxNumbersDTO();
			dto.setId(pojo.getId());
			dto.setNumber(pojo.getNumber());

			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public BaseDAO<LostBoxNumbersPOJO> createDAO() {
		return new LostBoxNumberDAO(LostBoxNumbersPOJO.class);
	}

	@Override
	protected LostBoxNumbersPOJO createPOJO(LostBoxNumbersDTO dto) {
		LostBoxNumbersPOJO pojo = new LostBoxNumbersPOJO();
		pojo.setId(dto.getId());
		pojo.setNumber(dto.getNumber());
		return pojo;
	}

	@Override
	protected boolean verifyExistence(LostBoxNumbersDTO dto) throws BusinessException {
		if (BusinessDelegate.getInstance().getBox().findByCode(dto.getNumber()) == null) {
			return true;
		}
		return false;
	}

	public void deleteByNumber(LostBoxNumbersDTO dto) throws BusinessException {
		LostBoxNumberDAO dao = new LostBoxNumberDAO(LostBoxNumbersPOJO.class);
		try {
			dao.deleteByNumber(dto.getNumber());
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
	}
}
