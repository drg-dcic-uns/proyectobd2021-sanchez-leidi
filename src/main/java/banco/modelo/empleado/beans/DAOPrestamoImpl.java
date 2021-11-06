package banco.modelo.empleado.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import banco.utils.Fechas;

public class DAOPrestamoImpl implements DAOPrestamo {

	private static Logger logger = LoggerFactory.getLogger(DAOPrestamoImpl.class);
	
	private Connection conexion;
	
	public DAOPrestamoImpl(Connection c) {
		this.conexion = c;
	}
	
	
	@Override
	public void crearActualizarPrestamo(PrestamoBean prestamo) throws Exception {

		logger.info("Creación o actualizacion del prestamo.");
		logger.debug("meses : {}", prestamo.getCantidadMeses());
		logger.debug("monto : {}", prestamo.getMonto());
		logger.debug("tasa : {}", prestamo.getTasaInteres());
		logger.debug("interes : {}", prestamo.getInteres());
		logger.debug("cuota : {}", prestamo.getValorCuota());
		logger.debug("legajo : {}", prestamo.getLegajo());
		logger.debug("cliente : {}", prestamo.getNroCliente());	
		
		try
		{
			Statement stmt = conexion.createStatement();
			String consulta = "SELECT CURDATE();";
			ResultSet rs = stmt.executeQuery(consulta);
			rs.next();
			String fecha = Fechas.convertirStringSQL(rs.getString("CURDATE()"));
			logger.debug("fechaHora : {}", fecha);
			
			consulta = "SELECT * FROM Prestamo WHERE nro_prestamo = " + prestamo.getNroPrestamo() + ";";
					
			rs = stmt.executeQuery(consulta);
			PreparedStatement ps;
			if(rs.next()) {										//Encontro un prestamo, lo actualizo
				logger.info("Actualizo un prestamo");
				consulta = "UPDATE Prestamo SET fecha = str_to_date(\"" + fecha + "\", '%d/%m/%Y'), cant_meses = " + prestamo.getCantidadMeses()
						+ ", monto = " + prestamo.getMonto() + ", tasa_interes = " + prestamo.getTasaInteres() + ", interes = " +
						prestamo.getInteres() + ", valor_cuota = " + prestamo.getValorCuota() + ", legajo = " + prestamo.getLegajo()
						+ ", nro_cliente = " + prestamo.getNroCliente() + " WHERE nro_prestamo = " + prestamo.getNroPrestamo() + ";";		
				ps = conexion.prepareStatement(consulta);
				ps.executeUpdate();
			}
			else {												//Si no encontro uno, es que tengo que crearlo
				logger.info("Creo un prestamo");
				consulta = "INSERT INTO Prestamo(fecha, cant_meses, monto, tasa_interes, interes, valor_cuota, legajo, nro_cliente) VALUES (str_to_date(\""
						+ fecha + "\", '%d/%m/%Y'), " + prestamo.getCantidadMeses() + ", " + prestamo.getMonto() + ", " + prestamo.getTasaInteres() +
						", " + prestamo.getInteres() + ", " + prestamo.getValorCuota() + ", " + prestamo.getLegajo() + ", " + prestamo.getNroCliente() + ");";
				ps = conexion.prepareStatement(consulta);
				ps.executeUpdate();
			}
		}
		catch (SQLException ex){
		   logger.error("SQLException: " + ex.getMessage());
		   logger.error("SQLState: " + ex.getSQLState());
		   logger.error("VendorError: " + ex.getErrorCode());
		}
		
		
		
		/** COMPLETED
		 * TODO Crear o actualizar el Prestamo segun el PrestamoBean prestamo. 
		 *      Si prestamo tiene nroPrestamo es una actualizacion, si el nroPrestamo es null entonces es un nuevo prestamo.
		 * 
		 * @throws Exception deberá propagar la excepción si ocurre alguna. Puede capturarla para loguear los errores, ej.
		 *				logger.error("SQLException: " + ex.getMessage());
		 * 				logger.error("SQLState: " + ex.getSQLState());
		 *				logger.error("VendorError: " + ex.getErrorCode());
		 *		   pero luego deberá propagarla para que se encargue el controlador. 
		 */

	}

	@Override
	public PrestamoBean recuperarPrestamo(int nroPrestamo) throws Exception {
		
		logger.info("Recupera el prestamo nro {}.", nroPrestamo);
		
		/** COMPLETED? No sé donde probarlo
		 * TODO Obtiene el prestamo según el id nroPrestamo
		 * 
		 * @param nroPrestamo
		 * @return Un prestamo que corresponde a ese id o null
		 * @throws Exception si hubo algun problema de conexión
		 */		

		/*
		 * Datos estáticos de prueba. Quitar y reemplazar por código que recupera los datos reales.  
		 * Retorna un PretamoBean con información del prestamo nro 4
		 */
		PrestamoBean prestamo = null;
			
		try
		{
			Statement stmt = conexion.createStatement();
			String consulta = "SELECT * FROM Prestamo WHERE nro_prestamo = " + nroPrestamo + ";";
					
			ResultSet rs = stmt.executeQuery(consulta);
			if(rs.next()) {			
				logger.info("Se encontró el préstamo con nro {} en la BD.", nroPrestamo);
				prestamo = new PrestamoBeanImpl();
				prestamo.setNroPrestamo(nroPrestamo);
				prestamo.setFecha(rs.getDate("fecha"));
				prestamo.setCantidadMeses(rs.getInt("cant_meses"));
				prestamo.setMonto(rs.getInt("monto"));
				prestamo.setTasaInteres(rs.getInt("tasa_interes"));
				prestamo.setInteres(rs.getInt("interes"));
				prestamo.setValorCuota(rs.getInt("valor_cuota"));
				prestamo.setLegajo(rs.getInt("legajo"));
				prestamo.setNroCliente(rs.getInt("nro_cliente"));
			}
			else {							
				logger.info("No se encontró el préstamo con nro {} en la BD.", nroPrestamo);				
				
			}
		}
		catch (SQLException ex){
		   logger.error("SQLException: " + ex.getMessage());
		   logger.error("SQLState: " + ex.getSQLState());
		   logger.error("VendorError: " + ex.getErrorCode());
		}
		
		/*
		prestamo = new PrestamoBeanImpl();
		prestamo.setNroPrestamo(4);
		prestamo.setFecha(Fechas.convertirStringADate("2021-04-05"));
		prestamo.setCantidadMeses(6);
		prestamo.setMonto(20000);
		prestamo.setTasaInteres(24);
		prestamo.setInteres(2400);
		prestamo.setValorCuota(3733.33);
		prestamo.setLegajo(2);
		prestamo.setNroCliente(2);
   		*/
		
		return prestamo;
		// Fin datos estáticos de prueba.
	}

}
