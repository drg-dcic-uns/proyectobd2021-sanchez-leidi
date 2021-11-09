package banco.modelo.atm;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
	public boolean autenticarUsuarioAplicacion(String tarjeta, String pin) throws Exception {
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

		if (this.tarjeta == null) {
			throw new Exception("El cliente no ingresó la tarjeta");
		}
		boolean encontroTarjeta = false;
		Double saldo = 0.0;
		ResultSet rs= this.consulta("select saldo, nro_tarjeta from Tarjeta natural join trans_cajas_ahorro;");
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
			throw new Exception("Falló la operación obtener saldo.");
		}
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
		ResultSet rs= this.consulta("select fecha, hora, tipo, IF(tipo='extraccion' OR tipo='transferencia' OR tipo='debito',monto * -1,monto) AS monto, cod_caja, destino from Tarjeta JOIN trans_cajas_ahorro where nro_tarjeta="+this.tarjeta+" and tarjeta.nro_ca = trans_cajas_ahorro.nro_ca ORDER BY fecha DESC");
		ArrayList<TransaccionCajaAhorroBean> lista = new ArrayList<TransaccionCajaAhorroBean>();
		int i=0;
		while (rs.next() && i<=cantidad) {	
			i++;
			this.insertarTransaccionCajaAhorroBeanEnLista(lista, rs);
		}
		return lista;
	}	
	
	@Override
	public ArrayList<TransaccionCajaAhorroBean> cargarMovimientosPorPeriodo(Date desde, Date hasta) throws Exception {
		if (hasta.before(desde)) {
			logger.error("La fecha hasta es menor a la fecha desde");
			throw new Exception("Las fechas ingresadas son incorrectas.");
		}
		logger.info("Busca las transacciones en la BD de la tarjeta {} donde su fecha se encuentre entre {} y {}.", Integer.valueOf(this.tarjeta.trim()),desde,hasta);
		ResultSet rs= this.consulta("select fecha, hora, tipo, IF(tipo='extraccion' OR tipo='transferencia' OR tipo='debito',monto * -1,monto) AS monto, cod_caja, destino from Tarjeta NATURAL JOIN trans_cajas_ahorro where nro_tarjeta="+this.tarjeta+" AND fecha >= '"+Fechas.convertirDateAStringDB(desde)+"' and fecha <= '"+Fechas.convertirDateAStringDB(hasta)+"'");
		ArrayList<TransaccionCajaAhorroBean> lista = new ArrayList<TransaccionCajaAhorroBean>();
		while (rs.next()) {	
			this.insertarTransaccionCajaAhorroBeanEnLista(lista, rs);
		}
		logger.debug("Retorna una lista con {} elementos", lista.size());
		return lista;
	}
	
	private void insertarTransaccionCajaAhorroBeanEnLista(ArrayList<TransaccionCajaAhorroBean> lista, ResultSet rs) throws Exception {
		TransaccionCajaAhorroBean t = new TransaccionCajaAhorroBeanImpl();
		try {
			t.setTransaccionFechaHora(Fechas.convertirStringADate(rs.getString("fecha"),rs.getString("hora")));
			t.setTransaccionTipo(rs.getString("tipo"));
			t.setTransaccionMonto(Parsing.parseMonto(rs.getString("monto")));
			if (rs.getString("cod_caja") != null) {
				t.setTransaccionCodigoCaja((int) Parsing.parseMonto(rs.getString("cod_caja")));
			}
			if (rs.getString("destino") != null) {
				t.setCajaAhorroDestinoNumero((int) Parsing.parseMonto(rs.getString("destino")));
			}
			lista.add(t);
		}
		catch (SQLException ex) {
		   logger.error("SQLException: " + ex.getMessage());
		   logger.error("SQLState: " + ex.getSQLState());
		   logger.error("VendorError: " + ex.getErrorCode());		
		   throw new Exception("Falló la operación cargar movimientos.");
		}
	}
	
	@Override
	public Double extraer(Double monto) throws Exception {
		if (this.tarjeta == null) {
			throw new Exception("El usuario no ingresó la tarjeta.");
		}
		if (this.codigoATM == null) {
			throw new Exception("El cajero no tiene código.");
		}
		logger.info("Realiza la extraccion de ${} sobre la cuenta", monto);
		try {
			ResultSet rs = this.consulta("CALL extraer("+monto+","+this.tarjeta+","+this.codigoATM+");");
			rs.next();
			String resultado = rs.getString("resultado");
			
			if (!resultado.equals(ModeloATM.EXTRACCION_EXITOSA)) {
				logger.info("La extracción no fue exitosa.");
				throw new Exception(resultado);
			}
			logger.info("La extracción fue exitosa.");
		}
		catch (SQLException ex) {
		   logger.error("SQLException: " + ex.getMessage());
		   logger.error("SQLState: " + ex.getSQLState());
		   logger.error("VendorError: " + ex.getErrorCode());		
		   throw new Exception("Falló la operación extraer.");
		}
		return this.obtenerSaldo();

	}

	
	@Override
	public int parseCuenta(String p_cuenta) throws Exception {
		logger.info("Intenta realizar el parsing de un codigo de cuenta {}", p_cuenta);
		int cuenta = Integer.parseInt(p_cuenta);
		if (cuenta < 0) {
			throw new Exception("Código de cuenta inválido.");
		}		
		logger.info("El código de cuenta es válido.");
        return cuenta;
	}	
	
	@Override
	public Double transferir(Double monto, int cajaDestino) throws Exception {
		if (this.tarjeta == null) {
			throw new Exception("El usuario no ingresó la tarjeta.");
		}
		if (this.codigoATM == null) {
			throw new Exception("El cajero no tiene código.");
		}
		try {
			logger.info("Realiza la transferencia de ${} sobre a la cuenta {}", monto, cajaDestino);
			ResultSet rs = this.consulta("CALL transferir(" + monto + "," + this.tarjeta + ", " + cajaDestino +","+this.codigoATM+");");
			rs.next();
			String resultado = rs.getString("resultado");
			
			if (!resultado.equals(ModeloATM.TRANSFERENCIA_EXITOSA)) {
				logger.info("La transacción no fue exitosa.");
				throw new Exception(resultado);
			}
			logger.info("La transacción fue exitosa.");
		}
		catch (SQLException ex) {
		   logger.error("SQLException: " + ex.getMessage());
		   logger.error("SQLState: " + ex.getSQLState());
		   logger.error("VendorError: " + ex.getErrorCode());		
		   throw new Exception("Falló la operación transferir.");
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
