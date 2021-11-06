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

import banco.utils.Fechas;


public class DAOPagoImpl implements DAOPago {

	private static Logger logger = LoggerFactory.getLogger(DAOPagoImpl.class);
	
	private Connection conexion;
	
	public DAOPagoImpl(Connection c) {
		this.conexion = c;
	}

	@Override
	public ArrayList<PagoBean> recuperarPagos(int nroPrestamo) throws Exception {
		logger.info("Inicia la recuperacion de los pagos del prestamo {}", nroPrestamo);
		
		/** COMPLETED? Falta probar
		 * TODO Recupera todos los pagos del prestamo (pagos e impagos) del prestamo nroPrestamo
		 * 	    Si ocurre algún error deberá propagar una excepción.
		 */
		
		/*
		 * Datos estáticos de prueba. Quitar y reemplazar por código que recupera los datos reales.  : 
		 * Retorna los pagos de un prestamo nro 4
		 */
		ArrayList<PagoBean> lista = new ArrayList<PagoBean>();
		
		try
		{
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
			}
		}
		catch (SQLException ex){
		   logger.error("SQLException: " + ex.getMessage());
		   logger.error("SQLState: " + ex.getSQLState());
		   logger.error("VendorError: " + ex.getErrorCode());
		}
		
		/*
		PagoBean fila = new PagoBeanImpl();
		fila.setNroPrestamo(4);
		fila.setNroPago(1);
		fila.setFechaVencimiento(Fechas.convertirStringADate("2021-05-05"));
		fila.setFechaPago(Fechas.convertirStringADate("2021-05-10"));
		lista.add(fila);
		
		fila = new PagoBeanImpl();
		fila.setNroPrestamo(4);
		fila.setNroPago(2);
		fila.setFechaVencimiento(Fechas.convertirStringADate("2021-06-05"));
		fila.setFechaPago(Fechas.convertirStringADate("2021-06-11"));
		lista.add(fila);
		
		fila = new PagoBeanImpl();
		fila.setNroPrestamo(4);
		fila.setNroPago(3);
		fila.setFechaVencimiento(Fechas.convertirStringADate("2021-07-05"));
		fila.setFechaPago(Fechas.convertirStringADate("2021-07-15"));
		lista.add(fila);
		
		fila = new PagoBeanImpl();
		fila.setNroPrestamo(4);
		fila.setNroPago(4);
		fila.setFechaVencimiento(Fechas.convertirStringADate("2021-08-05"));
		fila.setFechaPago(null);
		lista.add(fila);

		fila = new PagoBeanImpl();
		fila.setNroPrestamo(4);
		fila.setNroPago(5);
		fila.setFechaVencimiento(Fechas.convertirStringADate("2021-09-05"));
		fila.setFechaPago(null);
		lista.add(fila);

		fila = new PagoBeanImpl();
		fila.setNroPrestamo(4);
		fila.setNroPago(6);
		fila.setFechaVencimiento(Fechas.convertirStringADate("2021-10-05"));
		fila.setFechaPago(null);
		lista.add(fila);
		*/
		
		return lista;
		// Fin datos estáticos de prueba.
	}

	@Override
	public void registrarPagos(int nroCliente, int nroPrestamo, List<Integer> cuotasAPagar)  throws Exception {

		logger.info("Inicia el pago de las {} cuotas del prestamo {}", cuotasAPagar.size(), nroPrestamo);

		/**	COMPLETED? Probarlo
		 * TODO Registra los pagos de cuotas definidos en cuotasAPagar.
		 * 
		 * nroCliente asume que esta validado
		 * nroPrestamo asume que está validado
		 * cuotasAPagar Debe verificar que las cuotas a pagar no estén pagas (fecha_pago = NULL)
		 * @throws Exception Si hubo error en la conexión
		 */		
		
		try
		{
			Statement stmt = conexion.createStatement();
			String consulta = "SELECT * FROM Pago WHERE nro_prestamo = " + nroPrestamo + ";";
			ResultSet rs = stmt.executeQuery(consulta);
			cuotasAPagar.sort(null);
			int i = 0;
			
			PreparedStatement ps;
			while(rs.next()) {										//Encontro un prestamo, lo actualizo
				if(rs.getInt("nro_pago") == cuotasAPagar.get(i)) {
					if(rs.getDate("fecha_pago") != null) {
						i++;
					}
					else {
						throw new Exception("Se seleccionó un pago ya pagado.");
					}
				}
				/*
				logger.info("Actualizo un prestamo");
				consulta = "UPDATE Pago SET fecha = str_to_date(\"" + fechaHora + "\", '%d/%m/%Y'), cant_meses = " + prestamo.getCantidadMeses()
						+ ", monto = " + prestamo.getMonto() + ", tasa_interes = " + prestamo.getTasaInteres() + ", interes = " +
						prestamo.getInteres() + ", valor_cuota = " + prestamo.getValorCuota() + ", legajo = " + prestamo.getLegajo()
						+ ", nro_cliente = " + prestamo.getNroCliente() + " WHERE nro_prestamo = " + prestamo.getNroPrestamo() + ";";		
				ps = conexion.prepareStatement(consulta);
				ps.executeUpdate();
				*/
			}
			if(i == cuotasAPagar.size()) {
				consulta = "SELECT CURDATE()";
				rs = stmt.executeQuery(consulta);
				rs.next();
				String fecha = Fechas.convertirStringSQL(rs.getString("CURDATE()"));
				for(Integer aux : cuotasAPagar) {
					consulta = "UPDATE Pago SET fecha_venc = \"" + fecha + "\" WHERE nro_prestamo = " + nroPrestamo +
							" AND nro_pago = " + aux + ";";
					ps = conexion.prepareStatement(consulta);
					ps.executeUpdate();
				}
			}
			
		}
		catch (SQLException ex){
		   logger.error("SQLException: " + ex.getMessage());
		   logger.error("SQLState: " + ex.getSQLState());
		   logger.error("VendorError: " + ex.getErrorCode());
		}
	}
}
