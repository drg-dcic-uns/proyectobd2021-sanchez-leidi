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
	public boolean autenticarUsuarioAplicacion(String tarjeta, String pin)	throws Exception{
		logger.info("Se intenta autenticar la tarjeta {} con pin {}", tarjeta, pin);
		boolean encontroTarjeta = false;
		boolean autentica = false;
		try {
			if (tarjeta.equals("") || pin.equals("")) {
				throw new Exception("Por favor, ingrese todos los campos.");
			}
			ResultSet rs= this.consulta("select nro_tarjeta, PIN from Tarjeta");
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
				throw new Exception("La tarjeta " + tarjeta + " es inválida.");
			}
		}
		catch(SQLException ex) {
			logger.error("SQLException: " + ex.getMessage());
			logger.error("SQLState: " + ex.getSQLState());
			logger.error("VendorError: " + ex.getErrorCode());
			   throw new Exception("Falló la operación de autenticar usuario.");
		}
		return autentica;
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
	public Double obtenerSaldo() throws Exception
	{
		logger.info("Se intenta obtener el saldo de la caja de ahorro asociada a la tarjeta ingresada");

		if (this.tarjeta == null) {
			throw new Exception("El cliente no ingresó la tarjeta");
		}
		Double saldo = 0.0;
		try {
			if (this.tarjeta == null ) {
				throw new Exception("El cliente no ingresó la tarjeta");
			}
			ResultSet rs= this.consulta("SELECT saldo FROM tarjeta NATURAL JOIN caja_ahorro WHERE nro_tarjeta = "+this.tarjeta+";");
			rs.next();
			saldo = rs.getDouble("saldo");
			logger.info("El saldo de la caja de ahorro asociada a la tarjeta {} es {}",this.tarjeta,saldo);;
		}
		catch(SQLException ex) {
			logger.error("SQLException: " + ex.getMessage());
			logger.error("SQLState: " + ex.getSQLState());
			logger.error("VendorError: " + ex.getErrorCode());
			throw new Exception("Falló la operación de obtener saldo.");
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
		ArrayList<TransaccionCajaAhorroBean> lista = new ArrayList<TransaccionCajaAhorroBean>();
		try {
			ResultSet rs= this.consulta("select fecha, hora, tipo, IF(tipo='extraccion' OR tipo='transferencia' OR tipo='debito',monto * -1,monto) AS monto, cod_caja, destino from Tarjeta  JOIN trans_cajas_ahorro where nro_tarjeta="+this.tarjeta+" and tarjeta.nro_ca = trans_cajas_ahorro.nro_ca ORDER BY fecha DESC, hora DESC");
			int i=0;
			while (rs.next() && i<=cantidad) {	
				i++;
				this.insertarTransaccionCajaAhorroBeanEnLista(lista, rs);
			}
		}
		catch(SQLException ex) {
			logger.error("SQLException: " + ex.getMessage());
			logger.error("SQLState: " + ex.getSQLState());
			logger.error("VendorError: " + ex.getErrorCode());
			throw new Exception("Falló la operación de cargar ultimos movimientos.");
		}
		return lista;
	}	
	
	@Override
	public ArrayList<TransaccionCajaAhorroBean> cargarMovimientosPorPeriodo(Date desde, Date hasta) throws Exception {
		if (desde == null || hasta == null) {
			logger.error("Alguna de las fechas es null.");
			throw new Exception("Por favor, ingrese ambas fechas.");
		}
		if (hasta.before(desde)) {
			logger.error("La fecha hasta es menor a la fecha desde");
			throw new Exception("Las fechas ingresadas son incorrectas.");
		}
		ArrayList<TransaccionCajaAhorroBean> lista = new ArrayList<TransaccionCajaAhorroBean>();
		try {
			logger.info("Busca las transacciones en la BD de la tarjeta {} donde su fecha se encuentre entre {} y {}.", Integer.valueOf(this.tarjeta.trim()),desde,hasta);
			ResultSet rs= this.consulta("select fecha, hora, tipo, IF(tipo='extraccion' OR tipo='transferencia' OR tipo='debito',monto * -1,monto) AS monto, cod_caja, destino from Tarjeta JOIN trans_cajas_ahorro where nro_tarjeta="+this.tarjeta+" and tarjeta.nro_ca = trans_cajas_ahorro.nro_ca AND fecha >= '"+Fechas.convertirDateAStringDB(desde)+"' and fecha <= '"+Fechas.convertirDateAStringDB(hasta)+"'");
			while (rs.next()) {	
				this.insertarTransaccionCajaAhorroBeanEnLista(lista, rs);
			}
		}
		catch(SQLException ex) {
			logger.error("SQLException: " + ex.getMessage());
			logger.error("SQLState: " + ex.getSQLState());
			logger.error("VendorError: " + ex.getErrorCode());
			throw new Exception("Falló la operación de cargar ultimos movimientos.");
		}
		logger.debug("Retorna una lista con {} elementos", lista.size());
		return lista;
	}
	
	private void insertarTransaccionCajaAhorroBeanEnLista(ArrayList<TransaccionCajaAhorroBean> lista, ResultSet rs) throws Exception {
		TransaccionCajaAhorroBean t = new TransaccionCajaAhorroBeanImpl();
		try {
			t.setTransaccionFechaHora(Fechas.convertirStringADate(rs.getString("fecha"),rs.getString("hora")));
			t.setTransaccionTipo(rs.getString("tipo"));
			t.setTransaccionMonto(rs.getDouble("monto"));
			if (rs.getString("cod_caja") != null) {
				t.setTransaccionCodigoCaja(rs.getInt("cod_caja"));
			}
			if (rs.getString("destino") != null) {
				t.setCajaAhorroDestinoNumero(rs.getInt("destino"));
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
			ResultSet rs = this.consulta("CALL extraer(" + monto + ", " + this.tarjeta + ", " + this.codigoATM + ");");
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
		
		if(p_cuenta == null) {
			throw new Exception("Codigo de cuenta nulo");
		}
		try {
			int nro_ca = Integer.parseInt(p_cuenta);
			if(nro_ca > 0) {
				logger.info("Codigo de cuenta valido.");
				return nro_ca;
			}
			else {
				logger.info("Codigo de cuenta invalido, deberia ser positivo.");
				throw new Exception("Codigo de cuenta invalido, deberia ser positivo.");
			}
		
		}
		catch(NumberFormatException e) {
			throw new Exception("La cuenta no son solo numeros.");
		}
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
