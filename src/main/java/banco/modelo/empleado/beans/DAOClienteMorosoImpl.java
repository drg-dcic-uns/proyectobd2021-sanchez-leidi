package banco.modelo.empleado.beans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DAOClienteMorosoImpl implements DAOClienteMoroso {

	private static Logger logger = LoggerFactory.getLogger(DAOClienteMorosoImpl.class);
	
	private Connection conexion;
	
	public DAOClienteMorosoImpl(Connection c) {
		this.conexion = c;
	}
	
	@Override
	public ArrayList<ClienteMorosoBean> recuperarClientesMorosos() throws Exception {
		
		logger.info("Busca los clientes morosos.");
		
		DAOPrestamo daoPrestamo = new DAOPrestamoImpl(this.conexion);		
		DAOCliente daoCliente = new DAOClienteImpl(this.conexion);
		
		/** COMPLETED, PREGUNTAR EXCEPCIONES
		 * TODO Deberá recuperar un listado de clientes morosos los cuales consisten de un bean ClienteMorosoBeanImpl
		 *      deberá indicar para dicho cliente cual es el prestamo sobre el que está moroso y la cantidad de cuotas que 
		 *      tiene atrasadas. En todos los casos deberá generar excepciones que será capturadas por el controlador
		 *      si hay algún error que necesita ser informado al usuario. 
		 */
		
		/*
		 * Datos estáticos de prueba. Quitar y reemplazar por código que recupera los datos reales.  
		 */
		ArrayList<ClienteMorosoBean> morosos = new ArrayList<ClienteMorosoBean>();
		PrestamoBean prestamo = null;
		ClienteBean cliente = null;
		
		
		
		try {
			Statement stmt = conexion.createStatement();
			String consulta = "SELECT * FROM (SELECT nro_prestamo, COUNT(nro_prestamo) cuotas_atrasadas FROM "
					+ " pago WHERE fecha_venc < CURDATE() AND fecha_pago is NULL GROUP BY nro_prestamo) pp WHERE cuotas_atrasadas >= 2";
			ResultSet rs = stmt.executeQuery(consulta);
			ClienteMorosoBean morosoEncontrado;
			while(rs.next()) {
				morosoEncontrado = new ClienteMorosoBeanImpl();
				prestamo = daoPrestamo.recuperarPrestamo(rs.getInt("nro_prestamo"));
				cliente = daoCliente.recuperarCliente(prestamo.getNroCliente());
				morosoEncontrado.setCliente(cliente);
				morosoEncontrado.setPrestamo(prestamo);
				morosoEncontrado.setCantidadCuotasAtrasadas(rs.getInt("cuotas_atrasadas"));
				morosos.add(morosoEncontrado);
			}
		}
		catch(SQLException ex) {
			logger.error("SQLException: " + ex.getMessage());
			logger.error("SQLState: " + ex.getSQLState());
			logger.error("VendorError: " + ex.getErrorCode());	
			throw new Exception("Falló la operación de recuperar clientes morosos.");
		}
		
		return morosos;		
		
	}

}

