package banco.modelo.atm;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import banco.modelo.ModeloImpl;
import banco.utils.Fechas;
import banco.utils.Parsing;


public class ModeloATMImpl extends ModeloImpl implements ModeloATM {
	
	private static Logger logger = LoggerFactory.getLogger(ModeloATMImpl.class);	

	private String tarjeta = null;   // mantiene la tarjeta del cliente actual
	private Integer codigoATM = null;
	
	/*
	 * La información del cajero ATM se recupera del archivo que se encuentra definido en ModeloATM.CONFIG
	 */
	public ModeloATMImpl() {
		logger.debug("Se crea el modelo ATM.");

		logger.debug("Recuperación de la información sobre el cajero");
		
		Properties prop = new Properties();
		try (FileInputStream file = new FileInputStream(ModeloATM.CONFIG))
		{
			logger.debug("Se intenta leer el archivo de propiedades {}",ModeloATM.CONFIG);
			prop.load(file);

			codigoATM = Integer.valueOf(prop.getProperty("atm.codigo.cajero"));

			logger.debug("Código cajero ATM: {}", codigoATM);
		}
		catch(Exception ex)
		{
        	logger.error("Se produjo un error al recuperar el archivo de propiedades {}.",ModeloATM.CONFIG); 
		}
		return;
	}

	@Override
	public boolean autenticarUsuarioAplicacion(String tarjeta, String pin)	{
		logger.info("Se intenta autenticar la tarjeta {} con pin {}", tarjeta, pin);
		ResultSet rs= this.consulta("select nro_tarjeta, PIN from Tarjeta");
		boolean encontroTarjeta = false;
		boolean autentica = false;
		try {
			while (rs.next() && !encontroTarjeta) {	
				if (rs.getString("nro_tarjeta").equals(tarjeta)) {
					encontroTarjeta = true;
					this.tarjeta = tarjeta;
					logger.info("La tarjeta {} existe en la BD", tarjeta);
					if (rs.getString("PIN").equals(this.md5OfString(pin))) {
						logger.info("El pin {} corresponde con el pin asociado a la tarjeta en la BD {}", pin,tarjeta);
						autentica = true;
					}
					else {
						logger.info("El pin {} no corresponde con el pin asociado a la tarjeta en la BD {}", pin,tarjeta);
					}
				}		
			}
			if (!encontroTarjeta) {
				logger.info("La tarjeta {} no existe en la BD", tarjeta);
			}
		}
		catch (SQLException ex) {
			   logger.error("SQLException: " + ex.getMessage());
			   logger.error("SQLState: " + ex.getSQLState());
			   logger.error("VendorError: " + ex.getErrorCode());		   
		}
		return autentica;

		/** 
		 * TODO Código que autentica que exista una tarjeta con ese pin (el pin guardado en la BD está en MD5)
		 *      En caso exitoso deberá registrar la tarjeta en la propiedad tarjeta y retornar true.
		 *      Si la autenticación no es exitosa porque no coincide el pin o la tarjeta no existe deberá retornar falso
		 *      y si hubo algún otro error deberá producir una excepción.
		 */
		
		/*
		 * Datos estáticos de prueba. Quitar y reemplazar por código que recupera los datos reales.  
		 */
		
		// Fin datos estáticos de prueba.
	}
	
	private static String md5OfString(String input) {
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
	public Double obtenerSaldo() throws Exception
	{
		logger.info("Se intenta obtener el saldo de la caja de ahorro asociada a la tarjeta ingresada");

		if (this.tarjeta == null ) {
			throw new Exception("El cliente no ingresó la tarjeta");
		}
		boolean encontroTarjeta = false;
		Double saldo = 0.0;
		ResultSet rs= this.consulta("SELECT saldo, nro_tarjeta from Tarjeta natural join Cliente_CA natural join Caja_Ahorro");
		//NO FUNCIONA PQ EL USUARIO ATM NO PUEDE HACER CONSULTA SOBRE LA TABLA CLIENTE_CA
		try {
			
			while (rs.next() && !encontroTarjeta) {	
				
				if (rs.getString("nro_tarjeta").equals(tarjeta)) {
					encontroTarjeta = true;
					saldo = Parsing.parseMonto(rs.getString("saldo"));
					logger.info("El saldo de la caja de ahorro asociada a la tarjeta {} es {}",this.tarjeta,saldo);
				}		
			}
		}
		catch (SQLException ex) {
			   logger.error("SQLException: " + ex.getMessage());
			   logger.error("SQLState: " + ex.getSQLState());
			   logger.error("VendorError: " + ex.getErrorCode());		   
		}
		/** 
		 * TODO Obtiene el saldo.
		 *      Debe capturar la excepción SQLException y propagar una Exception más amigable.
		 */
		return saldo;
	}	

	@Override
	public ArrayList<TransaccionCajaAhorroBean> cargarUltimosMovimientos() throws Exception {
		return this.cargarUltimosMovimientos(ModeloATM.ULTIMOS_MOVIMIENTOS_CANTIDAD);
	}	
	
	@Override
	public ArrayList<TransaccionCajaAhorroBean> cargarUltimosMovimientos(int cantidad) throws Exception
	{
		logger.info("Busca las ultimas {} transacciones en la BD de la tarjeta {}",cantidad, Integer.valueOf(this.tarjeta.trim()));
		ResultSet rs= this.consulta("select fecha, hora, tipo, monto, cod_caja, destino from Tarjeta NATURAL JOIN trans_cajas_ahorro");
		ArrayList<TransaccionCajaAhorroBean> lista = new ArrayList<TransaccionCajaAhorroBean>();
		try {
			while (rs.next()) {	
				TransaccionCajaAhorroBean t = new TransaccionCajaAhorroBeanImpl();
				t.setTransaccionFechaHora(Fechas.convertirStringADate(rs.getString("fecha"),rs.getString("hora")));
				t.setTransaccionTipo(rs.getString("tipo"));
				if (rs.getString("tipo").equals("extraccion") || rs.getString("tipo").equals("transferencia") || rs.getString("tipo").equals("debito")) {
					t.setTransaccionMonto(Parsing.parseMonto(rs.getString("monto")));//ACA FALTA PONER EL - ANTES DEL MONTO
				}
				else {
					t.setTransaccionMonto(Parsing.parseMonto(rs.getString("monto")));
				}
				t.setTransaccionCodigoCaja((int) Parsing.parseMonto(rs.getString("cod_caja")));
				t.setCajaAhorroDestinoNumero((int) Parsing.parseMonto(rs.getString("destino")));
				lista.add(t);
			}
		}
		catch (SQLException ex) {
			   logger.error("SQLException: " + ex.getMessage());
			   logger.error("SQLState: " + ex.getSQLState());
			   logger.error("VendorError: " + ex.getErrorCode());		   
		}
		/**
		 * TODO Deberá recuperar los ultimos movimientos del cliente, la cantidad está definida en el parámetro.
		 * 		Debe capturar la excepción SQLException y propagar una Exception más amigable. 
		 */
		
		/*
		 * Datos estáticos de prueba. Quitar y reemplazar por código que recupera los datos reales.
		 * 
		+------------+----------+---------------+---------+----------+---------+
		| fecha      | hora     | tipo          | monto   | cod_caja | destino |
		+------------+----------+---------------+---------+----------+---------+
		| 2021-09-16 | 11:10:00 | transferencia | -700.00 |       18 |      32 |
		| 2021-09-15 | 17:20:00 | extraccion    | -200.00 |        2 |    NULL |
		| 2021-09-14 | 09:03:00 | deposito      | 1600.00 |        2 |    NULL |
		| 2021-09-13 | 13:30:00 | debito        |  -50.00 |     NULL |    NULL |
		| 2021-09-12 | 15:00:00 | transferencia | -400.00 |       41 |       7 |
		+------------+----------+---------------+---------+----------+---------+
 		 */
		
		/*ArrayList<TransaccionCajaAhorroBean> lista = new ArrayList<TransaccionCajaAhorroBean>();
		TransaccionCajaAhorroBean fila1 = new TransaccionCajaAhorroBeanImpl();
		fila1.setTransaccionFechaHora(Fechas.convertirStringADate("2021-09-16","11:10:00"));
		fila1.setTransaccionTipo("transferencia");
		fila1.setTransaccionMonto(-700.00);
		fila1.setTransaccionCodigoCaja(18);
		fila1.setCajaAhorroDestinoNumero(32);
		lista.add(fila1);

		TransaccionCajaAhorroBean fila2 = new TransaccionCajaAhorroBeanImpl();
		fila2.setTransaccionFechaHora(Fechas.convertirStringADate("2021-09-15","17:20:00"));
		fila2.setTransaccionTipo("extraccion");
		fila2.setTransaccionMonto(-200.00);
		fila2.setTransaccionCodigoCaja(2);
		fila2.setCajaAhorroDestinoNumero(0);	
		lista.add(fila2);
		
		TransaccionCajaAhorroBean fila3 = new TransaccionCajaAhorroBeanImpl();
		fila3.setTransaccionFechaHora(Fechas.convertirStringADate("2021-09-14","09:03:00"));
		fila3.setTransaccionTipo("deposito");
		fila3.setTransaccionMonto(1600.00);
		fila3.setTransaccionCodigoCaja(2);
		fila3.setCajaAhorroDestinoNumero(0);	
		lista.add(fila3);		

		TransaccionCajaAhorroBean fila4 = new TransaccionCajaAhorroBeanImpl();
		fila4.setTransaccionFechaHora(Fechas.convertirStringADate("2021-09-13","13:30:00"));
		fila4.setTransaccionTipo("debito");
		fila4.setTransaccionMonto(-50.00);
		fila4.setTransaccionCodigoCaja(0);
		fila4.setCajaAhorroDestinoNumero(0);	
		lista.add(fila4);	
		
		TransaccionCajaAhorroBean fila5 = new TransaccionCajaAhorroBeanImpl();
		fila5.setTransaccionFechaHora(Fechas.convertirStringADate("2021-09-12","15:00:00"));
		fila5.setTransaccionTipo("transferencia");
		fila5.setTransaccionMonto(-400.00);
		fila5.setTransaccionCodigoCaja(41);
		fila5.setCajaAhorroDestinoNumero(7);	
		lista.add(fila5);*/
		
		return lista;
		
		// Fin datos estáticos de prueba.
	}	
	
	@Override
	public ArrayList<TransaccionCajaAhorroBean> cargarMovimientosPorPeriodo(Date desde, Date hasta)
			throws Exception {

		/**
		 * TODO Deberá recuperar los ultimos del cliente que se han realizado entre las fechas indicadas.
		 * 		Debe capturar la excepción SQLException y propagar una Exception más amigable. 
		 * 		Debe generar excepción sin las fechas son erroneas (ver descripción en interface)
		 */
		
		/*
		 * Datos estáticos de prueba. Quitar y reemplazar por código que recupera los datos reales.
		 * 
		+------------+----------+---------------+---------+----------+---------+
		| fecha      | hora     | tipo          | monto   | cod_caja | destino |
		+------------+----------+---------------+---------+----------+---------+
		| 2021-09-16 | 11:10:00 | transferencia | -700.00 |       18 |      32 |
		| 2021-09-15 | 17:20:00 | extraccion    | -200.00 |        2 |    NULL |
		| 2021-09-14 | 09:03:00 | deposito      | 1600.00 |        2 |    NULL |
		| 2021-09-13 | 13:30:00 | debito        |  -50.00 |     NULL |    NULL |
		| 2021-09-12 | 15:00:00 | transferencia | -400.00 |       41 |       7 |
		+------------+----------+---------------+---------+----------+---------+
 		 */
		
		ArrayList<TransaccionCajaAhorroBean> lista = new ArrayList<TransaccionCajaAhorroBean>();
		TransaccionCajaAhorroBean fila1 = new TransaccionCajaAhorroBeanImpl();
		fila1.setTransaccionFechaHora(Fechas.convertirStringADate("2021-09-16","11:10:00"));
		fila1.setTransaccionTipo("transferencia");
		fila1.setTransaccionMonto(-700.00);
		fila1.setTransaccionCodigoCaja(18);
		fila1.setCajaAhorroDestinoNumero(32);
		lista.add(fila1);

		TransaccionCajaAhorroBean fila2 = new TransaccionCajaAhorroBeanImpl();
		fila2.setTransaccionFechaHora(Fechas.convertirStringADate("2021-09-15","17:20:00"));
		fila2.setTransaccionTipo("extraccion");
		fila2.setTransaccionMonto(-200.00);
		fila2.setTransaccionCodigoCaja(2);
		fila2.setCajaAhorroDestinoNumero(0);	
		lista.add(fila2);
		
		TransaccionCajaAhorroBean fila3 = new TransaccionCajaAhorroBeanImpl();
		fila3.setTransaccionFechaHora(Fechas.convertirStringADate("2021-09-14","09:03:00"));
		fila3.setTransaccionTipo("deposito");
		fila3.setTransaccionMonto(1600.00);
		fila3.setTransaccionCodigoCaja(2);
		fila3.setCajaAhorroDestinoNumero(0);	
		lista.add(fila3);		

		TransaccionCajaAhorroBean fila4 = new TransaccionCajaAhorroBeanImpl();
		fila4.setTransaccionFechaHora(Fechas.convertirStringADate("2021-09-13","13:30:00"));
		fila4.setTransaccionTipo("debito");
		fila4.setTransaccionMonto(-50.00);
		fila4.setTransaccionCodigoCaja(0);
		fila4.setCajaAhorroDestinoNumero(0);	
		lista.add(fila4);	
		
		TransaccionCajaAhorroBean fila5 = new TransaccionCajaAhorroBeanImpl();
		fila5.setTransaccionFechaHora(Fechas.convertirStringADate("2021-09-12","15:00:00"));
		fila5.setTransaccionTipo("transferencia");
		fila5.setTransaccionMonto(-400.00);
		fila5.setTransaccionCodigoCaja(41);
		fila5.setCajaAhorroDestinoNumero(7);	
		lista.add(fila5);
		
		logger.debug("Retorna una lista con {} elementos", lista.size());
		
		return lista;
		
		// Fin datos estáticos de prueba.
	}
	
	@Override
	public Double extraer(Double monto) throws Exception {
		logger.info("Realiza la extraccion de ${} sobre la cuenta", monto);
		
		/**
		 * TODO Deberá extraer de la cuenta del cliente el monto especificado (ya validado) y de obtener el saldo de la cuenta como resultado.
		 * 		Debe capturar la excepción SQLException y propagar una Exception más amigable. 
		 * 		Debe generar excepción si las propiedades codigoATM o tarjeta no tienen valores
		 */		
		
		String resultado = ModeloATM.EXTRACCION_EXITOSA;
		
		if (!resultado.equals(ModeloATM.EXTRACCION_EXITOSA)) {
			throw new Exception(resultado);
		}
		return this.obtenerSaldo();

	}

	
	@Override
	public int parseCuenta(String p_cuenta) throws Exception {
		
		logger.info("Intenta realizar el parsing de un codigo de cuenta {}", p_cuenta);

		/**
		 * TODO Verifica que el codigo de la cuenta sea valido. 
		 * 		Debe capturar la excepción SQLException y propagar una Exception más amigable. 
		 * 		Debe generar excepción si la cuenta es vacia, entero negativo o no puede generar el parsing.
		 * retorna la cuenta en formato int
		 */	
		
		logger.info("Encontró la cuenta en la BD.");
        return 1;
	}	
	
	@Override
	public Double transferir(Double monto, int cajaDestino) throws Exception {
		logger.info("Realiza la transferencia de ${} sobre a la cuenta {}", monto, cajaDestino);
		
		/**
		 * TODO Deberá extraer de la cuenta del cliente el monto especificado (ya validado) y de obtener el saldo de la cuenta como resultado.
		 * 		Debe capturar la excepción SQLException y propagar una Exception más amigable. 
		 * 		Debe generar excepción si las propiedades codigoATM o tarjeta no tienen valores
		 */		
		

		String resultado = ModeloATM.TRANSFERENCIA_EXITOSA;
		
		if (!resultado.equals(ModeloATM.TRANSFERENCIA_EXITOSA)) {
			throw new Exception(resultado);
		}
		return this.obtenerSaldo();
	}


	@Override
	public Double parseMonto(String p_monto) throws Exception {
		
		logger.info("Intenta realizar el parsing del monto {}", p_monto);
		
		if (p_monto == null) {
			throw new Exception("El monto no puede estar vacío");
		}

		try 
		{
			double monto = Double.parseDouble(p_monto);
			DecimalFormat df = new DecimalFormat("#.00");

			monto = Double.parseDouble(corregirComa(df.format(monto)));
			
			if(monto < 0)
			{
				throw new Exception("El monto no debe ser negativo.");
			}
			
			return monto;
		}		
		catch (NumberFormatException e)
		{
			throw new Exception("El monto no tiene un formato válido.");
		}	
	}

	private String corregirComa(String n)
	{
		String toReturn = "";
		
		for(int i = 0;i<n.length();i++)
		{
			if(n.charAt(i)==',')
			{
				toReturn = toReturn + ".";
			}
			else
			{
				toReturn = toReturn+n.charAt(i);
			}
		}
		
		return toReturn;
	}	
	
	

	
}
