# Stored procedure
delimiter ! # define “!” como delimitador
#..............................................................................
# transacción para extraer un monto M de la caja de ahorro asociada a un numero de tarjeta
CREATE PROCEDURE extraer(IN monto DECIMAL(16,2), IN tarjeta BIGINT(16), IN codigoATM INT)
	BEGIN
     		# Declaro una variable local saldo_actual
   		DECLARE saldo_actual DECIMAL(16,2);
    		# Declaro una variable local caja_ahorro
     		DECLARE caja_ahorro INT;
		# Declaro una variable local cliente
     		DECLARE cliente INT;
		# Declaro variables locales para recuperar los errores 
	 	DECLARE codigo_SQL  CHAR(5) DEFAULT '00000';	 
	 	DECLARE codigo_MYSQL INT DEFAULT 0;
	 	DECLARE mensaje_error TEXT;

		DECLARE EXIT HANDLER FOR SQLEXCEPTION 	 	 
	 		BEGIN #En caso de una excepción SQLEXCEPTION retrocede la transacción y
         			# devuelve el código de error especifico de MYSQL (MYSQL_ERRNO), 
				# el código de error SQL  (SQLSTATE) y el mensaje de error  	  
	    			# "GET DIAGNOSTICS" solo disponible a partir de la versión 5.6, 
				# más info en: http://dev.mysql.com/doc/refman/5.6/en/get-diagnostics.html
				GET DIAGNOSTICS CONDITION 1  codigo_MYSQL= MYSQL_ERRNO, codigo_SQL= RETURNED_SQLSTATE, mensaje_error= MESSAGE_TEXT;
	    			SELECT 'SQLEXCEPTION!, transacción abortada' AS resultado, codigo_MySQL, codigo_SQL,  mensaje_error;		
        		ROLLBACK;
	  		END;

     		START TRANSACTION;    # Comienza la transacción
        		SELECT nro_ca INTO caja_ahorro FROM tarjeta WHERE nro_tarjeta = tarjeta; #Guardo la caja de ahorro asociado al número de tarjeta
			      IF EXISTS (SELECT * FROM cliente_ca NATURAL JOIN caja_ahorro WHERE nro_ca = caja_ahorro) THEN
              SELECT nro_cliente INTO cliente FROM cliente_ca NATURAL JOIN caja_ahorro WHERE nro_ca = caja_ahorro; #Guardo el numero de cliente asociado a la caja de ahorro
              SELECT saldo INTO saldo_actual FROM caja_ahorro WHERE nro_ca = caja_ahorro FOR UPDATE; #Guardo el saldo actual de la caja de ahorro y bloqueo exclusivamente
                  IF saldo_actual >= monto THEN #Si el saldo de la caja de ahorro es suficiente como para extraer monto, realizo la extracción
                      UPDATE caja_ahorro SET saldo = saldo - monto WHERE nro_ca = caja_ahorro;#Actualizo el saldod de la caja de ahorro
                      INSERT INTO Transaccion(fecha, hora, monto) VALUES (CURDATE(), CURTIME(), monto); #inserto una transacción
                      INSERT INTO Transaccion_por_caja VALUES (LAST_INSERT_ID(), codigoATM); #Inserto una transacción por caja
                      INSERT INTO Extraccion VALUES (LAST_INSERT_ID(), cliente, caja_ahorro); #Inserto la extracción
                      SELECT 'Extraccion Exitosa' AS resultado;
                ELSE
                      SELECT 'Saldo insuficiente para realizar la extraccion.' AS resultado;
                END IF;
            ELSE
              SELECT 'Error, no existe un cliente asociado a la caja de ahorro.' AS resultado;
            END IF;
     		COMMIT;   # Comete la transacción
 	END; !
delimiter ; # reestablece el “;” como delimitador