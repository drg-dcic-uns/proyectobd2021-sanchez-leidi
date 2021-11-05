package banco.modelo.empleado.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
		
		/** COMPLETED? Repliqué código de la clase modelo (ya que no tengo la función consulta) y no sé si las exepciones están bien
		 * TODO Recuperar el cliente que tenga un documento que se corresponda con los parámetros recibidos.  
		 *		Deberá generar o propagar una excepción si no existe dicho cliente o hay un error de conexión.		
		 */
		
		/*
		 * Datos estáticos de prueba. Quitar y reemplazar por código que recupera los datos reales.  
		 */
		
		String consulta = "SELECT * FROM Cliente;";
		boolean encontro = false;
		ClienteBean cliente = new ClienteBeanImpl();
		
		try
		{
			Statement stmt = conexion.createStatement();			
			ResultSet rs = stmt.executeQuery(consulta);
			while (rs.next() && !encontro) {	
				if (rs.getString("tipo_doc").equals(tipoDoc) && rs.getInt("nro_doc") == nroDoc) {
					encontro = true;
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
			}
		}
		catch (SQLException ex){
		   logger.error("SQLException: " + ex.getMessage());
		   logger.error("SQLState: " + ex.getSQLState());
		   logger.error("VendorError: " + ex.getErrorCode());
		}
		
		/*
		ClienteBean cliente = new ClienteBeanImpl();
		cliente.setNroCliente(3);
		cliente.setApellido("Apellido3");
		cliente.setNombre("Nombre3");
		cliente.setTipoDocumento("DNI");
		cliente.setNroDocumento(3);
		cliente.setDireccion("Direccion3");
		cliente.setTelefono("0291-3333333");
		cliente.setFechaNacimiento(Fechas.convertirStringADate("1983-03-03","13:30:00"));
		*/
		
		return cliente;		

	}

	@Override
	public ClienteBean recuperarCliente(Integer nroCliente) throws Exception {
		logger.info("recupera el cliente por nro de cliente.");
		
		/** COMPLETED? No sé bien dónde se usa
		 * TODO Recuperar el cliente que tenga un número de cliente de acuerdo al parámetro recibido.  
		 *		Deberá generar o propagar una excepción si no existe dicho cliente o hay un error de conexión.		
		 */
		
		/*
		 * Datos estáticos de prueba. Quitar y reemplazar por código que recupera los datos reales.  
		 */
		
		String consulta = "SELECT * FROM Cliente;";
		boolean encontro = false;
		ClienteBean cliente = new ClienteBeanImpl();
		
		try
		{
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
			}
		}
		catch (SQLException ex){
		   logger.error("SQLException: " + ex.getMessage());
		   logger.error("SQLState: " + ex.getSQLState());
		   logger.error("VendorError: " + ex.getErrorCode());
		}
		
		/*
		ClienteBean cliente = new ClienteBeanImpl();
		cliente.setNroCliente(3);
		cliente.setApellido("Apellido3");
		cliente.setNombre("Nombre3");
		cliente.setTipoDocumento("DNI");
		cliente.setNroDocumento(3);
		cliente.setDireccion("Direccion3");
		cliente.setTelefono("0291-3333333");
		cliente.setFechaNacimiento(Fechas.convertirStringADate("1983-03-03","13:30:00"));
		*/
		
		return cliente;		
		// Fin datos estáticos de prueba.

	}

}
