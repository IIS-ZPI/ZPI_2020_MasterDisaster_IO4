package zpi.dao;

import zpi.product.IProductDAO;
import zpi.state.IUSStateDAO;

public class DAOFactory {
	static private IUSStateDAO iusStateDAO;
	static private IProductDAO iProductDAO;
	
	private DAOFactory() {
	}
	
	static public void registerUSStateDao(IUSStateDAO dao) {
		iusStateDAO = dao;
	}
	
	static public void registerProductDao(IProductDAO dao) {
		iProductDAO = dao;
	}
	
	static public IUSStateDAO getIUSStateDAO() {
		if (iusStateDAO == null) throw new RuntimeException();
		
		return iusStateDAO;
	}
	
	static public IProductDAO getIProductDAO() {
		if (iProductDAO == null) throw new RuntimeException();
		
		return iProductDAO;
	}
}
