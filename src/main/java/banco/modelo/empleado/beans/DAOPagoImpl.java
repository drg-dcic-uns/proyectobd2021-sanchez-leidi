package banco.modelo.empleado.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DAOPagoImpl implements DAOPago {

	private static Logger logger = LoggerFactory.getLogger(DAOPagoImpl.class);
	
	private Connection conexion;
	
	public DAOPagoImpl(Connection c) {
		this.conexion = c;
	}

	@Override
	public ArrayList<PagoBean> recuperarPagos(int nroPrestamo) throws Exception {
		logger.info("Inicia la recuperacion de los pagos del prestamo {}", nroPrestamo);
		
		ArrayList<PagoBean> lista = new ArrayList<PagoBean>();
		
		try {
			String consulta = "SELECT * FROM Pago WHERE nro_prestamo = " + nroPrestamo + " ;";
			Statement stmt = conexion.createStatement();			
			ResultSet rs = stmt.executeQuery(consulta);
			
			while (rs.next()) {	
				PagoBean fila = new PagoBeanImpl();
				fila.setNroPrestamo(rs.getInt("nro_prestamo"));
				fila.setNroPago(rs.getInt("nro_pago"));
				fila.setFechaVencimiento(rs.getDate("fecha_venc"));
				fila.setFechaPago(rs.getDate("fecha_pago"));
				lista.add(fila);
			}
			if (lista.isEmpty()) {
				logger.info("No hay pagos del prestamo nro {} en la BD", nroPrestamo);
				throw new Exception("No hay pagos del prestamo nro " + nroPrestamo + " en la BD.");
			}
		}
		catch(SQLException ex) {
			logger.error("SQLException: " + ex.getMessage());
			logger.error("SQLState: " + ex.getSQLState());
			logger.error("VendorError: " + ex.getErrorCode());
			throw new Exception("Falló la operación de recuperar pagos.");
		}
		
		return lista;
	}

	@Override
	public void registrarPagos(int nroCliente, int nroPrestamo, List<Integer> cuotasAPagar)  throws Exception {

		logger.info("Inicia el pago de las {} cuotas del prestamo {}", cuotasAPagar.size(), nroPrestamo);
		
		try {
			Statement stmt = conexion.createStatement();
			String consulta = "SELECT * FROM Pago WHERE nro_prestamo = " + nroPrestamo + ";";
			ResultSet rs = stmt.executeQuery(consulta);
			cuotasAPagar.sort(null);
			int i = 0;
			
			PreparedStatement ps;
			while(rs.next() && !(i == cuotasAPagar.size())) {					//Mientras tenga cuotas que ver
				if(rs.getInt("nro_pago") == cuotasAPagar.get(i)) {
					if(rs.getDate("fecha_pago") == null) {
						i++;
					}
					else {
						throw new Exception("Se seleccionó un pago ya pagado.");
					}
				}
			}
			if(i == cuotasAPagar.size()) {
				for(Integer aux : cuotasAPagar) {
					consulta = "UPDATE Pago SET fecha_pago = CURDATE() WHERE nro_prestamo = " + nroPrestamo +
							" AND nro_pago = " + aux + ";";
					ps = conexion.prepareStatement(consulta);
					ps.executeUpdate();
				}
			}
		}
		catch(SQLException ex) {
			logger.error("SQLException: " + ex.getMessage());
			logger.error("SQLState: " + ex.getSQLState());
			logger.error("VendorError: " + ex.getErrorCode());
			throw new Exception("Falló la operación registrar pagos.");
		}
	}
}
