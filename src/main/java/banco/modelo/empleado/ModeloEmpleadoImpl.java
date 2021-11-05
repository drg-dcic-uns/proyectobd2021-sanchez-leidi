package banco.modelo.empleado;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import banco.modelo.ModeloImpl;
import banco.modelo.empleado.beans.ClienteBean;
import banco.modelo.empleado.beans.ClienteMorosoBean;
import banco.modelo.empleado.beans.DAOCliente;
import banco.modelo.empleado.beans.DAOClienteImpl;
import banco.modelo.empleado.beans.DAOClienteMoroso;
import banco.modelo.empleado.beans.DAOClienteMorosoImpl;
import banco.modelo.empleado.beans.DAOEmpleado;
import banco.modelo.empleado.beans.DAOEmpleadoImpl;
import banco.modelo.empleado.beans.DAOPago;
import banco.modelo.empleado.beans.DAOPagoImpl;
import banco.modelo.empleado.beans.DAOPrestamo;
import banco.modelo.empleado.beans.DAOPrestamoImpl;
import banco.modelo.empleado.beans.EmpleadoBean;
import banco.modelo.empleado.beans.PagoBean;
import banco.modelo.empleado.beans.PrestamoBean;

public class ModeloEmpleadoImpl extends ModeloImpl implements ModeloEmpleado {

	private static Logger logger = LoggerFactory.getLogger(ModeloEmpleadoImpl.class);	

	
	private Integer legajo = null;
	
	public ModeloEmpleadoImpl() {
		logger.debug("Se crea el modelo Empleado.");
	}
	

	@Override
	public boolean autenticarUsuarioAplicacion(String legajo, String password) throws Exception {
		logger.info("Se intenta autenticar el legajo {} con password {}", legajo, password);
		ResultSet rs= this.consulta("select legajo, password from Empleado");
		boolean encontroTarjeta = false;
		boolean autentica = false;
		try {
			while (rs.next() && !encontroTarjeta) {	
				if (rs.getString("legajo").equals(legajo)) {
					encontroTarjeta = true;
					this.legajo = Integer.valueOf(legajo);
					logger.info("El legajo {} existe en la BD", legajo);
					if (rs.getString("password").equals(this.md5OfString(password))) {
						logger.info("La contraseña {} corresponde con la contraseña asociada al empleado en la BD {}", password,legajo);
						autentica = true;
					}
					else {
						logger.info("La contraseña {} no corresponde con la contraseña asociada al empleado en la BD {}", password,legajo);
					}
				}		
			}
			if (!encontroTarjeta) {
				logger.info("El legajo {} no existe en la BD", legajo);
			}
		}
		catch (SQLException ex) {
			   logger.error("SQLException: " + ex.getMessage());
			   logger.error("SQLState: " + ex.getSQLState());
			   logger.error("VendorError: " + ex.getErrorCode());		   
		}
		return autentica;

		/** COMPLETED
		 * TODO Código que autentica que exista un legajo de empleado y que el password corresponda a ese legajo
		 *      (el password guardado en la BD está en MD5) 
		 *      En caso exitoso deberá registrar el legajo en la propiedad legajo y retornar true.
		 *      Si la autenticación no es exitosa porque el legajo no es válido o el password es incorrecto
		 *      deberá retornar falso y si hubo algún otro error deberá producir una excepción.
		 */
		
		/*
		 * Datos estáticos de prueba. Quitar y reemplazar por código que recupera los datos reales.  
		 */
		// Fin datos estáticos de prueba.
	}
	
	private String md5OfString(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
	
	@Override
	public EmpleadoBean obtenerEmpleadoLogueado() throws Exception {
		logger.info("Solicita al DAO un empleado con legajo {}", this.legajo);
		if (this.legajo == null) {
			logger.info("No hay un empleado logueado.");
			throw new Exception("No hay un empleado logueado. La sesión terminó.");
		}
		
		DAOEmpleado dao = new DAOEmpleadoImpl(this.conexion);
		return dao.recuperarEmpleado(this.legajo);
	}	
	
	@Override
	public ArrayList<String> obtenerTiposDocumento() throws Exception{
		logger.info("recupera los tipos de documentos.");
		ArrayList<String> tipos = new ArrayList<String>();
		ResultSet rs= this.consulta("SELECT DISTINCT tipo_doc FROM Empleado;");
		try {
			while (rs.next()) {	
				tipos.add(rs.getString("tipo_doc"));
			}
		}
		catch (SQLException ex) {
			   logger.error("SQLException: " + ex.getMessage());
			   logger.error("SQLState: " + ex.getSQLState());
			   logger.error("VendorError: " + ex.getErrorCode());		   
		}
		/** COMPLETED
		 * TODO Debe retornar una lista de strings con los tipos de documentos. 
		 *      Deberia propagar una excepción si hay algún error en la consulta.
		 */
		
		/*
		 * Datos estáticos de prueba. Quitar y reemplazar por código que recupera los datos reales.  
		 */
		return tipos;
		// Fin datos estáticos de prueba.
	}	

	@Override
	public double obtenerTasa(double monto, int cantidadMeses) throws Exception {

		logger.info("Busca la tasa correspondiente a el monto {} con una cantidad de meses {}", monto, cantidadMeses);
		ResultSet rs= this.consulta("SELECT * FROM Tasa_Prestamo;");
		boolean encontroTasa = false;
		double tasa = 0;

		try {
			while (rs.next() && !encontroTasa) {	
				if (rs.getInt("periodo") == cantidadMeses && rs.getInt("monto_inf") <= monto && rs.getInt("monto_sup") >= monto) {
					encontroTasa = true;
					tasa = rs.getInt("tasa");
					logger.info("Se encontró la tasa {} para el monto {}, con meses {} en la BD", rs.getInt("tasa"), monto, cantidadMeses);
				}		
			}
			if (!encontroTasa) {
				logger.info("No se encontró la tasa para el monto {}, con meses {} en la BD", monto, cantidadMeses);
			}
		}
		catch (SQLException ex) {
			   logger.error("SQLException: " + ex.getMessage());
			   logger.error("SQLState: " + ex.getSQLState());
			   logger.error("VendorError: " + ex.getErrorCode());		   
		}
		
		/** 
		 * TODO Debe buscar la tasa correspondiente según el monto y la cantidadMeses. 
		 *      Deberia propagar una excepción si hay algún error de conexión o 
		 *      no encuentra el monto dentro del [monto_inf,monto_sup] y la cantidadMeses.
		 */
		
		/*
		 * Datos estáticos de prueba. Quitar y reemplazar por código que recupera los datos reales.  
		 */
   		return tasa;
     	// Fin datos estáticos de prueba.
	}

	@Override
	public double obtenerInteres(double monto, double tasa, int cantidadMeses) {
		return (monto * tasa * cantidadMeses) / 1200;
	}


	@Override
	public double obtenerValorCuota(double monto, double interes, int cantidadMeses) {
		return (monto + interes) / cantidadMeses;
	}
		

	@Override
	public ClienteBean recuperarCliente(String tipoDoc, int nroDoc) throws Exception {
		DAOCliente dao = new DAOClienteImpl(this.conexion);
		return dao.recuperarCliente(tipoDoc, nroDoc);
	}


	@Override
	public ArrayList<Integer> obtenerCantidadMeses(double monto) throws Exception {
		logger.info("recupera los períodos (cantidad de meses) según el monto {} para el prestamo.", monto);

		/** 
		 * TODO Debe buscar los períodos disponibles según el monto. 
		 *      Deberia propagar una excepción si hay algún error de conexión o 
		 *      no encuentra el monto dentro del [monto_inf,monto_sup].
		 */
		
		/*
		 * Datos estáticos de prueba. Quitar y reemplazar por código que recupera los datos reales.  
		 */		
		ArrayList<Integer> cantMeses = new ArrayList<Integer>();
		
		ResultSet rs= this.consulta("SELECT periodo, monto_inf, monto_sup FROM Tasa_Prestamo;");

		try {
			while (rs.next()) {	
				if (rs.getInt("monto_inf") <= monto && rs.getInt("monto_sup") >= monto) {
					cantMeses.add(rs.getInt("periodo"));
					logger.info("Se encontró el mes {} para el monto {} en la BD", rs.getInt("periodo"), monto);
				}		
			}
			if (cantMeses.isEmpty()) {
				logger.info("No se encontraron meses para el monto {} en la BD", monto);
			}
		}
		catch (SQLException ex) {
			   logger.error("SQLException: " + ex.getMessage());
			   logger.error("SQLState: " + ex.getSQLState());
			   logger.error("VendorError: " + ex.getErrorCode());		   
		}
		
		return cantMeses;
		// Fin datos estáticos de prueba.
	}

	@Override	
	public Integer prestamoVigente(int nroCliente) throws Exception 
	{
		logger.info("Verifica si el cliente {} tiene algun prestamo que tienen cuotas por pagar.", nroCliente);

		/** 
		 * TODO Busca algún prestamo del cliente que tenga cuotas sin pagar (vigente) retornando el nro_prestamo
		 *      si no existe prestamo del cliente o todos están pagos retorna null.
		 *      Si hay una excepción la propaga con un mensaje apropiado.
		 */
		
		/*
		 * Datos estáticos de prueba. Quitar y reemplazar por código que recupera los datos reales.  
		 */
		
		ResultSet rs= this.consulta("SELECT nro_prestamo, nro_cliente FROM Prestamo;");
		Integer nroPrestamo = -1;
		boolean encontro = false;
		

		try {
			while (rs.next() && !encontro) {	
				if (rs.getInt("nro_cliente") == nroCliente) {
					nroPrestamo = rs.getInt("nro_prestamo");
					encontro = true;
					logger.info("Se encontró el prestamo vigente {} para el cliente {} en la BD", nroPrestamo, nroCliente);
				}
			}
			if (!encontro) {
				logger.info("No se encontró prestamo vigente para el cliente {} en la BD", nroCliente);
			}
		}
		catch (SQLException ex) {
			   logger.error("SQLException: " + ex.getMessage());
			   logger.error("SQLState: " + ex.getSQLState());
			   logger.error("VendorError: " + ex.getErrorCode());		   
		}
		
		
		return encontro ? nroPrestamo : null;
		// Fin datos estáticos de prueba.
	}


	@Override
	public void crearPrestamo(PrestamoBean prestamo) throws Exception {
		logger.info("Crea un nuevo prestamo.");
		
		if (this.legajo == null) {
			throw new Exception("No hay un empleado registrado en el sistema que se haga responsable por este prestamo.");
		}
		else 
		{
			logger.info("Actualiza el prestamo con el legajo {}",this.legajo);
			prestamo.setLegajo(this.legajo);
			
			DAOPrestamo dao = new DAOPrestamoImpl(this.conexion);		
			dao.crearActualizarPrestamo(prestamo);
		}
	}
	
	@Override
	public PrestamoBean recuperarPrestamo(int nroPrestamo) throws Exception {
		logger.info("Busca el prestamo número {}", nroPrestamo);
		
		DAOPrestamo dao = new DAOPrestamoImpl(this.conexion);		
		return dao.recuperarPrestamo(nroPrestamo);
	}
	
	@Override
	public ArrayList<PagoBean> recuperarPagos(Integer prestamo) throws Exception {
		logger.info("Solicita la busqueda de pagos al modelo sobre el prestamo {}.", prestamo);
		
		DAOPago dao = new DAOPagoImpl(this.conexion);		
		return dao.recuperarPagos(prestamo);
	}
	

	@Override
	public void pagarCuotas(String p_tipo, int p_dni, int nroPrestamo, List<Integer> cuotasAPagar) throws Exception {
		
		// Valida que sea un cliente que exista sino genera una excepción
		ClienteBean c = this.recuperarCliente(p_tipo.trim(), p_dni);

		// Valida el prestamo
		if (nroPrestamo != this.prestamoVigente(c.getNroCliente())) {
			throw new Exception ("El nro del prestamo no coincide con un prestamo vigente del cliente");
		}

		if (cuotasAPagar.size() == 0) {
			throw new Exception ("Debe seleccionar al menos una cuota a pagar.");
		}
		
		DAOPago dao = new DAOPagoImpl(this.conexion);
		dao.registrarPagos(c.getNroCliente(), nroPrestamo, cuotasAPagar);		
	}


	@Override
	public ArrayList<ClienteMorosoBean> recuperarClientesMorosos() throws Exception {
		logger.info("Modelo solicita al DAO que busque los clientes morosos");
		DAOClienteMoroso dao = new DAOClienteMorosoImpl(this.conexion);
		return dao.recuperarClientesMorosos();	
	}
	

	
}
