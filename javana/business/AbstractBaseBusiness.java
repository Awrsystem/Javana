package br.com.javana.business;

import java.util.ArrayList;
import java.util.List;

import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.BaseDTO;
import br.com.javana.persistence.dao.BaseDAO;
import br.com.javana.persistence.dao.PersistenceException;
import br.com.javana.persistence.pojo.BasePOJO;

public abstract class AbstractBaseBusiness<DTO extends BaseDTO, POJO extends BasePOJO> {
	private IBusinessDelegate businessDelegate;

	/**
	 * Cria um pojo correspondente ao business
	 * 
	 * @return instancia de um pojo
	 */
	protected abstract POJO createPOJO(DTO dto);

	/**
	 * Verifica se o objeto já existe, baseado em algum criterio do proprio
	 * objeto (geralmente o nome)
	 * 
	 * @param dto
	 * @return
	 */
	protected abstract boolean verifyExistence(DTO dto)
			throws BusinessException;

	/**
	 * Converte uma lista de pojos em uma lista de dtos
	 * 
	 * @param pojos
	 * @return
	 */
	public abstract List<DTO> convertPojosToDTO(List<POJO> pojos);

	public abstract BaseDAO<POJO> createDAO();

	/**
	 * Cria um BaseDAO
	 */
	protected <P extends BasePOJO> BaseDAO<P> createBaseDAO(Class<P> pClass) {
		return new BaseDAO<P>(pClass);
	}

	protected IBusinessDelegate getBusinessDelegate() {
		if (businessDelegate == null)
			businessDelegate = new BusinessDelegate();
		return businessDelegate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.javana.business.IBaseBusiness#save(DTO)
	 */
	@SuppressWarnings("unchecked")
	public Long save(DTO dto) throws BusinessException {
		try {
			BaseDAO<BasePOJO> dao = this.createBaseDAO(BasePOJO.class);

			if (this.verifyExistence(dto)) {
				throw new ObjectAlreadyExistentException();
			}

			return (Long) dao.insert(createPOJO(dto));

		} catch (PersistenceException e) {
			throw new BusinessException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.javana.business.IBaseBusiness#delete(DTO)
	 */
	@SuppressWarnings("unchecked")
	public void delete(DTO dto) throws BusinessException {
		try {
			BaseDAO<BasePOJO> dao = this.createBaseDAO(BasePOJO.class);
			dao.delete(createPOJO(dto));
		} catch (ChildException e) {
			throw new ChildException(e);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	public void update(DTO dto) throws BusinessException {
		try {
			BaseDAO<BasePOJO> dao = this.createBaseDAO(BasePOJO.class);
			dao.update(createPOJO(dto));
		} catch (PersistenceException e) {
			throw new BusinessException(e);
		} catch (Exception e) {
			throw new BusinessException(e);
		}

	}

	@SuppressWarnings("unchecked")
	public List<DTO> findAll() throws BusinessException {
		try {
			BaseDAO<POJO> dao = this.createDAO();
			List<POJO> pojos = (List<POJO>) dao.findAll();
			return this.convertPojosToDTO(pojos);
		} catch (PersistenceException e) {
			throw new BusinessException(e);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<DTO> findAllByNameOrder() throws BusinessException {
		try {
			BaseDAO<POJO> dao = this.createDAO();
			List<POJO> pojos = (List<POJO>) dao.findAllByNameOrder();
			return this.convertPojosToDTO(pojos);
		} catch (PersistenceException e) {
			throw new BusinessException(e);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	public List<DTO> findByName(String name) throws BusinessException {
		try {
			BaseDAO<POJO> dao = this.createDAO();
			List<POJO> clients = dao.findByName(name);

			return this.convertPojosToDTO(clients);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	public DTO findByExactName(String name) throws BusinessException {
		try {
			BaseDAO<POJO> dao = this.createDAO();
			POJO pojo = dao.findByExactName(name);

			if (pojo == null) {
				return null;
			} else {
				List<POJO> pojos = new ArrayList<POJO>();
				pojos.add(pojo);
				List<DTO> dtos = this.convertPojosToDTO(pojos);

				return dtos.get(0);
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}
}