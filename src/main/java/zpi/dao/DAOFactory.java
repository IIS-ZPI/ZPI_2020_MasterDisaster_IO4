package zpi.dao;

import zpi.state.IUSStateDAO;

public class DAOFactory {
	static private IUSStateDAO iusStateDAO;
	
	private DAOFactory() {
	}
	
	static public void registerUSStateDao(IUSStateDAO dao) {
		iusStateDAO = dao;
	}
	
	static public IUSStateDAO getIUSStateDAO() {
		if (iusStateDAO == null) throw new RuntimeException();
		
		return iusStateDAO;
	}
}
