package banco.modelo.empleado.beans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DAOEmpleadoImpl implements DAOEmpleado {

	private static Logger logger = LoggerFactory.getLogger(DAOEmpleadoImpl.class);
	
	private Connection conexion;
	
	public DAOEmpleadoImpl(Connection c) {
		this.conexion = c;
	}


	@Override
	public EmpleadoBean recuperarEmpleado(int legajo) throws Exception {
		logger.info("recupera el empleado que corresponde al legajo {}.", legajo);
		
		EmpleadoBean empleado = null;
		
		try
		{
			Statement stmt = conexion.createStatement();
			String consulta = "SELECT * FROM Empleado WHERE legajo = " + legajo + ";";
					
			ResultSet rs = stmt.executeQuery(consulta);
			if(rs.next()) {			
				logger.info("Se encontró el empleado con legajo {} en la BD.", legajo);
				empleado = new EmpleadoBeanImpl();
				empleado.setLegajo(legajo);
				empleado.setApellido(rs.getString("apellido"));
				empleado.setNombre(rs.getString("nombre")); 
				empleado.setTipoDocumento(rs.getString("tipo_doc")); 
				empleado.setNroDocumento(rs.getInt("nro_doc"));
				empleado.setDireccion(rs.getString("direccion")); 
				empleado.setTelefono(rs.getString("telefono"));
				empleado.setCargo(rs.getString("cargo"));
				empleado.setPassword(rs.getString("password"));
				empleado.setNroSucursal(rs.getInt("nro_suc"));
			}
			else {							
				logger.info("No se encontró el empleado con legajo {} en la BD.", legajo);			
				throw new Exception("No se encontro el empleado con legajo " + legajo + " en la BD.");
			}
		}
		catch (SQLException ex){
		   logger.error("SQLException: " + ex.getMessage());
		   logger.error("SQLState: " + ex.getSQLState());
		   logger.error("VendorError: " + ex.getErrorCode());
		}
		
		return empleado;
	}

}
