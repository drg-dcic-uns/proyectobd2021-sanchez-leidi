package banco.modelo.empleado.beans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import banco.utils.Fechas;

public class DAOClienteImpl implements DAOCliente {

	private static Logger logger = LoggerFactory.getLogger(DAOClienteImpl.class);
	
	private Connection conexion;
	
	public DAOClienteImpl(Connection c) {
		this.conexion = c;
	}
	
	@Override
	public ClienteBean recuperarCliente(String tipoDoc, int nroDoc) throws Exception {
		
		logger.info("recupera el cliente con documento de tipo {} y nro {}.", tipoDoc, nroDoc);
		
		String consulta = "SELECT * FROM Cliente;";
		boolean encontro = false;
		ClienteBean cliente = null;
		
		try {
			Statement stmt = conexion.createStatement();			
			ResultSet rs = stmt.executeQuery(consulta);
			while (rs.next() && !encontro) {	
				if (rs.getString("tipo_doc").equals(tipoDoc) && rs.getInt("nro_doc") == nroDoc) {
					encontro = true;
					cliente = new ClienteBeanImpl();
					logger.info("El documento {} con tipo {} existe en la BD", nroDoc, tipoDoc);
					cliente.setNroCliente(rs.getInt("nro_cliente"));
					cliente.setApellido(rs.getString("apellido"));
					cliente.setNombre(rs.getString("nombre"));
					cliente.setTipoDocumento(tipoDoc);
					cliente.setNroDocumento(nroDoc);
					cliente.setDireccion(rs.getString("direccion"));
					cliente.setTelefono(rs.getString("telefono"));
					cliente.setFechaNacimiento(Fechas.convertirStringADate(rs.getString("fecha_nac")));
				}		
			}
			if (!encontro) {
				logger.info("El documento {} con tipo {} no existe en la BD", nroDoc, tipoDoc);
				throw new Exception("El documento es inválido.");
			}
		}
		catch(SQLException ex) {
			logger.error("SQLException: " + ex.getMessage());
			logger.error("SQLState: " + ex.getSQLState());
			logger.error("VendorError: " + ex.getErrorCode());
			throw new Exception("Falló la operación de recuperar cliente.");
		}
		return cliente;		

	}

	@Override
	public ClienteBean recuperarCliente(Integer nroCliente) throws Exception {
		logger.info("recupera el cliente por nro de cliente.");
		
		String consulta = "SELECT * FROM Cliente;";
		boolean encontro = false;
		ClienteBean cliente = new ClienteBeanImpl();
		
		try {
			Statement stmt = conexion.createStatement();			
			ResultSet rs = stmt.executeQuery(consulta);
			while (rs.next() && !encontro) {	
				if (rs.getInt("nro_cliente") == (nroCliente)) {
					encontro = true;
					logger.info("El nroCliente {} existe en la BD", nroCliente);
					cliente.setNroCliente(nroCliente);
					cliente.setApellido(rs.getString("apellido"));
					cliente.setNombre(rs.getString("nombre"));
					cliente.setTipoDocumento(rs.getString("tipo_doc"));
					cliente.setNroDocumento(rs.getInt("nro_doc"));
					cliente.setDireccion(rs.getString("direccion"));
					cliente.setTelefono(rs.getString("telefono"));
					cliente.setFechaNacimiento(Fechas.convertirStringADate(rs.getString("fecha_nac")));
				}		
			}
			if (!encontro) {
				logger.info("El nroCliente {} no existe en la BD", nroCliente);
				throw new Exception("El número de cliente es inválido.");
			}
		}
		catch(SQLException ex) {
			logger.error("SQLException: " + ex.getMessage());
			logger.error("SQLState: " + ex.getSQLState());
			logger.error("VendorError: " + ex.getErrorCode());
			throw new Exception("Falló la operación de recuperar cliente.");
		}
		
		return cliente;		

	}

}
