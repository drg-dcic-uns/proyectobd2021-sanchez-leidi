#Creación de la base de datos

CREATE DATABASE banco;

USE banco;

#-----------------------------------------------------------------
#Creación Tablas para las entidades

CREATE TABLE Ciudad (
 cod_postal SMALLINT(4) UNSIGNED NOT NULL, 
 nombre VARCHAR(45) NOT NULL, 
 
 PRIMARY KEY (cod_postal)
) ENGINE=InnoDB;


CREATE TABLE Sucursal (
 nro_suc SMALLINT(3) UNSIGNED NOT NULL AUTO_INCREMENT, 
 nombre VARCHAR(45) NOT NULL, 
 direccion VARCHAR(45) NOT NULL, 
 telefono VARCHAR(45) NOT NULL, 
 horario VARCHAR(45) NOT NULL, 
 cod_postal SMALLINT(4) UNSIGNED NOT NULL,  

 PRIMARY KEY (nro_suc),

 FOREIGN KEY (cod_postal) REFERENCES Ciudad (cod_postal) 
   ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;


CREATE TABLE Empleado (
 legajo SMALLINT(4) UNSIGNED NOT NULL AUTO_INCREMENT, 
 apellido VARCHAR(45) NOT NULL, 
 nombre VARCHAR(45) NOT NULL, 
 tipo_doc VARCHAR(20) NOT NULL, 
 nro_doc INT(8) UNSIGNED NOT NULL, 
 direccion VARCHAR(45) NOT NULL,  
 telefono VARCHAR(45) NOT NULL, 
 cargo VARCHAR(45) NOT NULL, 
 password CHAR(32) NOT NULL, 
 nro_suc SMALLINT(3) UNSIGNED NOT NULL, 
 
 PRIMARY KEY (legajo),

 FOREIGN KEY (nro_suc) REFERENCES Sucursal (nro_suc) 
   ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;


CREATE TABLE Cliente (
 nro_cliente INT(5) UNSIGNED NOT NULL AUTO_INCREMENT, 
 apellido VARCHAR(45) NOT NULL, 
 nombre VARCHAR(45) NOT NULL, 
 tipo_doc VARCHAR(20) NOT NULL, 
 nro_doc INT(8) UNSIGNED NOT NULL, 
 direccion VARCHAR(45) NOT NULL,  
 telefono VARCHAR(45) NOT NULL, 
 fecha_nac DATE NOT NULL, 
 
 PRIMARY KEY (nro_cliente)
) ENGINE=InnoDB;

CREATE TABLE Plazo_Fijo (
 nro_plazo INT(8) UNSIGNED NOT NULL AUTO_INCREMENT, 
 capital DECIMAL (16, 2) UNSIGNED NOT NULL, 
 fecha_inicio DATE NOT NULL, 
 fecha_fin DATE NOT NULL, 
 tasa_interes DECIMAL (4, 2) UNSIGNED NOT NULL, 
 interes DECIMAL (16, 2) UNSIGNED NOT NULL, 
 nro_suc SMALLINT(3) UNSIGNED NOT NULL, 
 
 PRIMARY KEY (nro_plazo),

 FOREIGN KEY (nro_suc) REFERENCES Sucursal (nro_suc) 
   ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE Tasa_Plazo_Fijo (
 periodo SMALLINT(3) UNSIGNED NOT NULL, 
 monto_inf DECIMAL (16, 2) UNSIGNED NOT NULL, 
 monto_sup DECIMAL (16, 2) UNSIGNED NOT NULL, 
 tasa DECIMAL (4, 2) UNSIGNED NOT NULL, 
 
 PRIMARY KEY (periodo,monto_inf,monto_sup)
) ENGINE=InnoDB;


CREATE TABLE Prestamo (
 nro_prestamo INT(8) UNSIGNED NOT NULL AUTO_INCREMENT, 
 fecha DATE NOT NULL, 
 cant_meses SMALLINT(2) UNSIGNED NOT NULL, 
 monto DECIMAL (10, 2) UNSIGNED NOT NULL, 
 tasa_interes DECIMAL (4, 2) UNSIGNED NOT NULL, 
 interes DECIMAL (9, 2) UNSIGNED NOT NULL, 
 valor_cuota DECIMAL (9, 2) UNSIGNED NOT NULL, 
 legajo SMALLINT(4) UNSIGNED NOT NULL, 
 nro_cliente INT(5) UNSIGNED NOT NULL, 

 PRIMARY KEY (nro_prestamo),

 FOREIGN KEY (legajo) REFERENCES Empleado (legajo) 
   ON DELETE RESTRICT ON UPDATE CASCADE,

 FOREIGN KEY (nro_cliente) REFERENCES Cliente (nro_cliente) 
   ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;


CREATE TABLE Tasa_Prestamo (
 periodo SMALLINT(3) UNSIGNED NOT NULL, 
 monto_inf DECIMAL (10, 2) UNSIGNED NOT NULL, 
 monto_sup DECIMAL (10, 2) UNSIGNED NOT NULL, 
 tasa DECIMAL (4, 2) UNSIGNED NOT NULL, 

 PRIMARY KEY (periodo,monto_inf,monto_sup)

) ENGINE=InnoDB;


CREATE TABLE Pago (
 nro_prestamo INT(8) UNSIGNED NOT NULL, 
 nro_pago SMALLINT(2) UNSIGNED NOT NULL, 
 fecha_venc DATE NOT NULL, 
 fecha_pago DATE, 

 PRIMARY KEY (nro_prestamo,nro_pago),

 FOREIGN KEY (nro_prestamo) REFERENCES Prestamo (nro_prestamo) 
   ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;


CREATE TABLE Caja_Ahorro (
 nro_ca INT(8) UNSIGNED NOT NULL AUTO_INCREMENT, 
 CBU BIGINT(18) UNSIGNED NOT NULL, 
 saldo DECIMAL (16, 2) UNSIGNED NOT NULL, 

 PRIMARY KEY (nro_ca)

) ENGINE=InnoDB;


CREATE TABLE Transaccion(
	nro_trans BIGINT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	fecha DATE NOT NULL,
	hora TIME NOT NULL,
	monto DECIMAL (16, 2) UNSIGNED NOT NULL,
	PRIMARY KEY (nro_trans)
) ENGINE=InnoDB;


CREATE TABLE Caja(
	cod_caja INT(5) UNSIGNED NOT NULL AUTO_INCREMENT, 
	PRIMARY KEY (cod_caja)
) ENGINE=InnoDB;


CREATE TABLE ATM(
	cod_caja INT(5) UNSIGNED NOT NULL, 
	cod_postal SMALLINT(4) UNSIGNED NOT NULL, 
	direccion VARCHAR(45) NOT NULL,
	PRIMARY KEY (cod_caja),
	FOREIGN KEY (cod_caja) REFERENCES Caja(cod_caja)
		ON DELETE RESTRICT ON UPDATE CASCADE,
	FOREIGN KEY (cod_postal) REFERENCES Ciudad(cod_postal)
		ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;


CREATE TABLE Ventanilla(
	cod_caja INT(5) UNSIGNED NOT NULL,
	nro_suc SMALLINT(3) UNSIGNED NOT NULL,
	PRIMARY KEY (cod_caja),
	FOREIGN KEY (cod_caja) REFERENCES Caja(cod_caja)
		ON DELETE RESTRICT ON UPDATE CASCADE,
	FOREIGN KEY (nro_suc) REFERENCES Sucursal(nro_suc)
		ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;


CREATE TABLE Transaccion_por_caja(
	nro_trans BIGINT(10) UNSIGNED NOT NULL,
	cod_caja INT(5) UNSIGNED NOT NULL,
	PRIMARY KEY (nro_trans),
	FOREIGN KEY (nro_trans) REFERENCES Transaccion(nro_trans)
		ON DELETE RESTRICT ON UPDATE CASCADE,
	FOREIGN KEY (cod_caja) REFERENCES Caja(cod_caja)
		ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;


CREATE TABLE Deposito(
	nro_trans BIGINT(10) UNSIGNED NOT NULL,
	nro_ca INT(8) UNSIGNED NOT NULL, 
	PRIMARY KEY (nro_trans),
	FOREIGN KEY (nro_trans) REFERENCES Transaccion_Por_Caja(nro_trans)
		ON DELETE RESTRICT ON UPDATE CASCADE,
	FOREIGN KEY (nro_ca) REFERENCES Caja_Ahorro(nro_ca)
		ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;


CREATE TABLE Plazo_Cliente (
 nro_plazo INT(8) UNSIGNED NOT NULL, 
 nro_cliente INT(5) UNSIGNED NOT NULL, 
 
 PRIMARY KEY (nro_plazo,nro_cliente),

 FOREIGN KEY (nro_plazo) REFERENCES Plazo_Fijo (nro_plazo) 
   ON DELETE RESTRICT ON UPDATE CASCADE,

 FOREIGN KEY (nro_cliente) REFERENCES Cliente (nro_cliente) 
   ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;


CREATE TABLE Cliente_CA (
 nro_cliente INT(5) UNSIGNED NOT NULL, 
 nro_ca INT(8) UNSIGNED NOT NULL, 

 PRIMARY KEY (nro_cliente,nro_ca),

 FOREIGN KEY (nro_cliente) REFERENCES Cliente (nro_cliente)
            ON DELETE RESTRICT ON UPDATE CASCADE,

  FOREIGN KEY (nro_ca) REFERENCES Caja_Ahorro (nro_ca)
            ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;


CREATE TABLE Tarjeta(
	nro_tarjeta BIGINT(16) UNSIGNED NOT NULL AUTO_INCREMENT,
	PIN CHAR(32) NOT NULL,
	CVT CHAR(32) NOT NULL,	
fecha_venc DATE NOT NULL,
nro_cliente INT(5) UNSIGNED NOT NULL,
nro_ca INT(8) UNSIGNED NOT NULL, 
	PRIMARY KEY (nro_tarjeta),

	FOREIGN KEY (nro_cliente,nro_ca) REFERENCES Cliente_CA (nro_cliente,nro_ca)
            ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;


CREATE TABLE Debito(
	nro_trans BIGINT(10) UNSIGNED NOT NULL,
	descripcion TEXT,
	nro_cliente INT(5) UNSIGNED NOT NULL,
	nro_ca INT(8) UNSIGNED NOT NULL, 
	PRIMARY KEY (nro_trans),
	FOREIGN KEY (nro_trans) REFERENCES Transaccion(nro_trans)
		ON DELETE RESTRICT ON UPDATE CASCADE,
	FOREIGN KEY (nro_cliente, nro_ca) REFERENCES Cliente_CA(nro_cliente, nro_ca)
		ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;


CREATE TABLE Transferencia(
 	nro_trans BIGINT(10) UNSIGNED NOT NULL,
 	nro_cliente INT(5) UNSIGNED NOT NULL,
 	origen INT(8) UNSIGNED NOT NULL, 
	destino INT(8) UNSIGNED NOT NULL, 
PRIMARY KEY (nro_trans),
FOREIGN KEY (nro_trans) REFERENCES Transaccion_por_caja (nro_trans)
        ON DELETE RESTRICT ON UPDATE CASCADE,
FOREIGN KEY (nro_cliente, origen) REFERENCES Cliente_CA(nro_cliente, nro_ca)
	ON DELETE RESTRICT ON UPDATE CASCADE,
FOREIGN KEY (destino) REFERENCES Caja_Ahorro (nro_ca)
	ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;


CREATE TABLE Extraccion(
	nro_trans BIGINT(10) UNSIGNED NOT NULL,
	nro_cliente INT(5) UNSIGNED NOT NULL,
	nro_ca INT(8) UNSIGNED NOT NULL, 
	PRIMARY KEY (nro_trans),
	FOREIGN KEY (nro_trans) REFERENCES Transaccion_Por_Caja(nro_trans)
		ON DELETE RESTRICT ON UPDATE CASCADE,
	FOREIGN KEY (nro_cliente, nro_ca) REFERENCES Cliente_CA(nro_cliente, nro_ca)
		ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

#-------------------------------------------------------------------------
# Creación de usuarios y otorgamiento de privilegios

# Usuario admin
	# el usuario admin con password 'admin' puede conectarse solo desde la desde la computadora donde se encuentra el servidor de MySQL (localhost) 
	CREATE USER 'admin'@'localhost'  IDENTIFIED BY 'admin';
	# El usuario 'admin' tiene acceso total a todas las tablas de la B.D. banco y puede crear nuevos usuarios y otorgar privilegios.
	GRANT ALL PRIVILEGES ON banco.* TO 'admin'@'localhost' WITH GRANT OPTION;


# Usuario empleado
	# el usuario empleado con password 'empleado' puede conectarse desde cualquier dominio
	CREATE USER 'empleado'@'%'  IDENTIFIED BY 'empleado';
	# el usuario 'empleado' puede realizar consultas sobre: Empleado, Sucursal, Tasa Plazo Fijo y Tasa Prestamo
	GRANT SELECT ON banco.Empleado TO 'empleado'@'%';
	GRANT SELECT ON banco.Sucursal TO 'empleado'@'%';	
	GRANT SELECT ON banco.Tasa_Plazo_Fijo TO 'empleado'@'%';
	GRANT SELECT ON banco.Tasa_Prestamo TO 'empleado'@'%';
	# el usuario 'empleado' puede realizar consultas e ingresar datos sobre: Préstamo, Plazo Fijo, Plazo Cliente, Caja Ahorro y Tarjeta.
	GRANT SELECT, INSERT ON banco.Prestamo TO 'empleado'@'%';
	GRANT SELECT, INSERT ON banco.Plazo_Fijo TO 'empleado'@'%';
	GRANT SELECT, INSERT ON banco.Plazo_Cliente TO 'empleado'@'%';
	GRANT SELECT, INSERT ON banco.Caja_Ahorro TO 'empleado'@'%';
	GRANT SELECT, INSERT ON banco.Tarjeta TO 'empleado'@'%';
	# el usuario 'empleado' puede realizar consultas, ingresar y modificar datos sobre: Cliente CA, Cliente y Pago.
	GRANT SELECT, INSERT, UPDATE ON banco.Cliente_CA TO 'empleado'@'%';
	GRANT SELECT, INSERT, UPDATE ON banco.Cliente TO 'empleado'@'%';
	GRANT SELECT, INSERT, UPDATE ON banco.Pago TO 'empleado'@'%';
	
# Creación vista
	
	CREATE VIEW debito_caja_ahorro AS
	SELECT ca.nro_ca, saldo, trn.nro_trans, fecha, hora, 'debito' AS tipo, monto, NULL AS 'cod_caja', clca.nro_cliente, tipo_doc, nro_doc, nombre, apellido, NULL AS 'destino' FROM banco.Caja_Ahorro ca
		INNER JOIN banco.Cliente_CA clca ON ca.nro_ca = clca.nro_ca
		INNER JOIN banco.Cliente cl ON clca.nro_cliente = cl.nro_cliente
		INNER JOIN banco.Debito deb ON ca.nro_ca = deb.nro_ca AND deb.nro_cliente = cl.nro_cliente
		INNER JOIN banco.Transaccion trn ON trn.nro_trans = deb.nro_trans;
		
	CREATE VIEW deposito_caja_ahorro AS
	SELECT ca.nro_ca, saldo, trn.nro_trans, fecha, hora, 'deposito' AS tipo, monto, cod_caja, NULL AS 'nro_cliente', NULL AS 'tipo_doc', NULL AS 'nro_doc', NULL AS 'nombre', NULL AS 'apellido', NULL AS 'destino' FROM banco.Caja_Ahorro ca
		INNER JOIN banco.Deposito dep ON ca.nro_ca = dep.nro_ca
		INNER JOIN banco.Transaccion trn ON trn.nro_trans = dep.nro_trans
		INNER JOIN banco.Transaccion_por_caja tca ON trn.nro_trans = tca.nro_trans;
		
	CREATE VIEW extraccion_caja_ahorro AS
	SELECT ca.nro_ca, saldo, trn.nro_trans, fecha, hora, 'extraccion' AS tipo, monto, cod_caja, clca.nro_cliente, tipo_doc, nro_doc, nombre, apellido, NULL AS 'destino' FROM banco.Caja_Ahorro ca
		INNER JOIN banco.Cliente_CA clca ON ca.nro_ca = clca.nro_ca
		INNER JOIN banco.Cliente cl ON clca.nro_cliente = cl.nro_cliente
		INNER JOIN banco.Extraccion ext ON ca.nro_ca = ext.nro_ca AND ext.nro_cliente = cl.nro_cliente
		INNER JOIN banco.Transaccion trn ON trn.nro_trans = ext.nro_trans
		INNER JOIN banco.Transaccion_por_caja tca ON ext.nro_trans = tca.nro_trans;
		
	CREATE VIEW transferencia_caja_ahorro AS
	SELECT ca.nro_ca, saldo, trn.nro_trans, fecha, hora, 'transferencia' AS tipo, monto, cod_caja, clca.nro_cliente, tipo_doc, nro_doc, nombre, apellido, destino FROM banco.Caja_Ahorro ca
		INNER JOIN banco.Cliente_CA clca ON ca.nro_ca = clca.nro_ca
		INNER JOIN banco.Cliente cl ON clca.nro_cliente = cl.nro_cliente
		INNER JOIN banco.Transferencia trf ON ca.nro_ca = trf.origen AND trf.nro_cliente = cl.nro_cliente
		INNER JOIN banco.Transaccion trn ON trn.nro_trans = trf.nro_trans
		INNER JOIN banco.Transaccion_por_caja tca ON trf.nro_trans = tca.nro_trans;
		
	CREATE VIEW trans_cajas_ahorro AS
		SELECT * FROM debito_caja_ahorro 
		UNION 
		SELECT * FROM deposito_caja_ahorro
		UNION 
		SELECT * FROM extraccion_caja_ahorro
		UNION 
		SELECT * FROM transferencia_caja_ahorro;
	
	
# Usuario atm
	# el usuario atm con password 'atm' puede conectarse desde cualquier dominio
	CREATE USER 'atm'@'%' IDENTIFIED BY 'atm';
	# el usuario 'atm' puede realizar consultas y actualizar datos sobre: Tarjeta
	GRANT SELECT, UPDATE ON banco.Tarjeta TO 'atm'@'%';
	# el usuario 'atm' puede realizar consultas datos sobre: trans_caja_ahorro
	GRANT SELECT ON banco.trans_cajas_ahorro TO 'atm'@'%';