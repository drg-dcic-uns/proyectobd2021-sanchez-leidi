#Ciudades
INSERT INTO Ciudad VALUES (8000, 'Bahia Blanca');
INSERT INTO Ciudad VALUES (8300, 'Neuquen');
INSERT INTO Ciudad VALUES (7600, 'Mar del Plata');
INSERT INTO Ciudad VALUES (7630, 'Necochea');

#Sucursales
INSERT INTO Sucursal(nombre, direccion, telefono, horario, cod_postal) VALUES ('Banco de madera', 'Elbosque 594', 2567984513, '12 AM a 6 PM', 7630);
INSERT INTO Sucursal(nombre, direccion, telefono, horario, cod_postal) VALUES ('Banco de peces', 'Wallaby 42 Sidney', 2567649723, '1 PM a 2:40 PM', 7600);
INSERT INTO Sucursal(nombre, direccion, telefono, horario, cod_postal) VALUES ('Banco el Loco', 'Tripo 13', 2567984976, '1 PM a 2 AM', 8300);
INSERT INTO Sucursal(nombre, direccion, telefono, horario, cod_postal) VALUES ('Bancoroto', 'Calle 1', 2575984513, '8 AM a 6 PM', 8000);
INSERT INTO Sucursal(nombre, direccion, telefono, horario, cod_postal) VALUES ('Banco a secas', 'Rondeau 400', 2437982513, '2 AM a 3 AM', 8000);


#Empleados
INSERT INTO Empleado(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, cargo, password, nro_suc) VALUES ('Perez', 'Martin', 'DNI', 19765231, 'Calle 401', 2354892131, 'Gerente', md5('pez'), 2);
INSERT INTO Empleado(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, cargo, password, nro_suc) VALUES ('Joestar', 'Joseph', 'DNI', 10357413, 'Rondeau 802', 2364978131, 'Cajero', md5('jojo'), 2);
INSERT INTO Empleado(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, cargo, password, nro_suc) VALUES ('Miguel', 'Lucas', 'DNI', 39765231, 'Wallaby 42 Sidney', 2398893431, 'Gerente', md5('a'), 3);
INSERT INTO Empleado(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, cargo, password, nro_suc) VALUES ('Fernandez', 'Franco', 'DNI', 35385841, 'Colon 438', 2696548548, 'Supervisor', md5('holasoyfranco'), 3);
INSERT INTO Empleado(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, cargo, password, nro_suc) VALUES ('Rodriguez', 'Adrian', 'DNI', 29574841, 'Chiclana 264', 2585749647, 'Analista', md5('contraseña'), 4);
INSERT INTO Empleado(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, cargo, password, nro_suc) VALUES ('Silva', 'Ricardo', 'DNI', 34579357, 'Donado 1564', 2516847693, 'Agente', md5('silva123'), 4);
INSERT INTO Empleado(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, cargo, password, nro_suc) VALUES ('Gonzalez', 'Lautaro', 'DNI', 28468864, 'Sarmiento 451', 2746834853, 'Empleado de ventanilla', md5('gonzalez87'), 5);
INSERT INTO Empleado(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, cargo, password, nro_suc) VALUES ('Sanchez', 'Roberto', 'DNI', 26470631, 'San Marin 964', 2845836953, 'Gerente', md5('robersan'), 5);
INSERT INTO Empleado(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, cargo, password, nro_suc) VALUES ('Borelli', 'Esteban', 'DNI', 24567852, 'Belgrano 234', 2946847909, 'Cajero', md5('boborelli'), 4);
INSERT INTO Empleado(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, cargo, password, nro_suc) VALUES ('Gomez', 'Agustin', 'DNI', 31753689, 'Avellaneda 123', 2912678965, 'Empleado de ventanilla', md5('ggomez'), 1);
INSERT INTO Empleado(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, cargo, password, nro_suc) VALUES ('Diaz', 'Facundo', 'DNI', 32467864, 'Brasil 1436', 2815845378, 'Analista', md5('diazzz'), 3);
INSERT INTO Empleado(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, cargo, password, nro_suc) VALUES ('Garcia', 'Valentino', 'DNI', 21578456, 'Alem 321', 2715674368, 'Agente', md5('vgarcia'), 4);
INSERT INTO Empleado(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, cargo, password, nro_suc) VALUES ('Gallardo', 'Marcelo', 'DNI', 28376432, 'Bernabeu 912', 2913457685, 'Supervisor', md5('riverplate'), 2);


#Clientes
INSERT INTO Cliente(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, fecha_nac) VALUES ('Perez', 'Enzo', 'DNI', 32698451, 'Rondeau 431', 43126795, str_to_date('15/11/1982', '%d/%m/%Y'));
INSERT INTO Cliente(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, fecha_nac) VALUES ('Marquez', 'Mariano', 'DNI', 31256754, 'Farias 1543', 2916458743, str_to_date('12/09/1992', '%d/%m/%Y'));
INSERT INTO Cliente(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, fecha_nac) VALUES ('Jara', 'Pablo', 'DNI', 30278642, 'Paunero 231', 2913658604, str_to_date('03/05/1978', '%d/%m/%Y'));
INSERT INTO Cliente(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, fecha_nac) VALUES ('Andrada', 'Diego', 'DNI', 31567753, 'Chile 1264', 2814753987, str_to_date('23/10/1985', '%d/%m/%Y'));
INSERT INTO Cliente(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, fecha_nac) VALUES ('Ferreyra', 'Martin', 'DNI', 32567964, 'Angel Brunel 165', 2913658965, str_to_date('23/12/1977', '%d/%m/%Y'));
INSERT INTO Cliente(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, fecha_nac) VALUES ('Lopez', 'Julio', 'DNI', 25678321, 'Italia 654', 2918457306, str_to_date('31/01/1979', '%d/%m/%Y'));
INSERT INTO Cliente(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, fecha_nac) VALUES ('Ledesma', 'Norberto', 'DNI', 35678543, 'Mexico 543', 2918646842, str_to_date('25/05/1985', '%d/%m/%Y'));
INSERT INTO Cliente(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, fecha_nac) VALUES ('Gonzalez', 'Lucas', 'DNI', 33456789, 'Darregueira 1267', 2918643219, str_to_date('22/07/1981', '%d/%m/%Y'));
INSERT INTO Cliente(apellido, nombre, tipo_doc, nro_doc, direccion, telefono, fecha_nac) VALUES ('Sills', 'Federico', 'DNI', 34521876, 'Rio Negro 1276', 2916327890, str_to_date('15/09/1989', '%d/%m/%Y'));


#Plazo Fijos
INSERT INTO Plazo_Fijo(capital, fecha_inicio, fecha_fin, tasa_interes, interes, nro_suc) VALUES (4123.12, str_to_date('01/05/2020', '%d/%m/%Y'), str_to_date('01/06/2020', '%d/%m/%Y'), 5.0, 206.15, 2);
INSERT INTO Plazo_Fijo(capital, fecha_inicio, fecha_fin, tasa_interes, interes, nro_suc) VALUES (15000.00, str_to_date('12/06/2021', '%d/%m/%Y'), str_to_date('15/07/2021', '%d/%m/%Y'), 7.0, 1050.00, 3);
INSERT INTO Plazo_Fijo(capital, fecha_inicio, fecha_fin, tasa_interes, interes, nro_suc) VALUES (10000.00, str_to_date('15/03/2021', '%d/%m/%Y'), str_to_date('15/04/2021', '%d/%m/%Y'), 3.0, 300.00, 4);
INSERT INTO Plazo_Fijo(capital, fecha_inicio, fecha_fin, tasa_interes, interes, nro_suc) VALUES (7000.00, str_to_date('01/07/2020', '%d/%m/%Y'), str_to_date('01/08/2020', '%d/%m/%Y'), 4.0, 280.00, 3);
INSERT INTO Plazo_Fijo(capital, fecha_inicio, fecha_fin, tasa_interes, interes, nro_suc) VALUES (1000.00, str_to_date('01/08/2021', '%d/%m/%Y'), str_to_date('01/09/2021', '%d/%m/%Y'), 8.0, 80.00, 5);
INSERT INTO Plazo_Fijo(capital, fecha_inicio, fecha_fin, tasa_interes, interes, nro_suc) VALUES (3500.00, str_to_date('01/02/2021', '%d/%m/%Y'), str_to_date('01/03/2021', '%d/%m/%Y'), 3.0, 105.00, 3);


#Tasa Plazos Fijos
INSERT INTO Tasa_Plazo_Fijo VALUES (312, 125.50, 300.25, 15.50);
INSERT INTO Tasa_Plazo_Fijo VALUES (474, 120.57, 354.35, 17.50);
INSERT INTO Tasa_Plazo_Fijo VALUES (469, 175.55, 963.25, 24.54);
INSERT INTO Tasa_Plazo_Fijo VALUES (473, 253.50, 862.25, 15.70);
INSERT INTO Tasa_Plazo_Fijo VALUES (321, 124.55, 753.97, 24.45);
INSERT INTO Tasa_Plazo_Fijo VALUES (123, 134.43, 543.53, 25.46);
INSERT INTO Tasa_Plazo_Fijo VALUES (643, 153.66, 636.86, 55.64);
INSERT INTO Tasa_Plazo_Fijo VALUES (753, 124.99, 355.76, 64.76);
INSERT INTO Tasa_Plazo_Fijo VALUES (422, 129.76, 457.77, 12.55);
INSERT INTO Tasa_Plazo_Fijo VALUES (571, 257.00, 633.22, 57.64);
INSERT INTO Tasa_Plazo_Fijo VALUES (683, 133.56, 643.07, 64.42);
INSERT INTO Tasa_Plazo_Fijo VALUES (135, 157.75, 583.85, 65.46);

#Prestamos
INSERT INTO Prestamo(fecha, cant_meses, monto, tasa_interes, interes, valor_cuota, legajo, nro_cliente) VALUES (str_to_date('14/07/2021', '%d/%m/%Y'), 5, 50000.00, 12.00, 200.00, 1500.00, 1, 1);
INSERT INTO Prestamo(fecha, cant_meses, monto, tasa_interes, interes, valor_cuota, legajo, nro_cliente) VALUES (str_to_date('25/08/2021', '%d/%m/%Y'), 3, 25000.00, 8.00, 159.00, 1256.00, 4, 3);
INSERT INTO Prestamo(fecha, cant_meses, monto, tasa_interes, interes, valor_cuota, legajo, nro_cliente) VALUES (str_to_date('21/05/2021', '%d/%m/%Y'), 2, 5000.00, 5.00, 120.00, 2500.00, 10, 8);
INSERT INTO Prestamo(fecha, cant_meses, monto, tasa_interes, interes, valor_cuota, legajo, nro_cliente) VALUES (str_to_date('07/09/2021', '%d/%m/%Y'), 3, 12000.00, 15.00, 500.00, 1750.00, 12, 6);


#Tasa Prestamos
INSERT INTO Tasa_Prestamo VALUES (1, 100.00, 500.00, 10.00);
INSERT INTO Tasa_Prestamo VALUES (1, 501.00, 1000.00, 20.00);
INSERT INTO Tasa_Prestamo VALUES (3, 100.00, 500.00, 20.00);
INSERT INTO Tasa_Prestamo VALUES (3, 501.00, 1000.00, 30.00);
INSERT INTO Tasa_Prestamo VALUES (5, 100.00, 500.00, 30.00);
INSERT INTO Tasa_Prestamo VALUES (5, 501.00, 1000.00, 40.00);
INSERT INTO Tasa_Prestamo VALUES (7, 100.00, 500.00, 40.00);
INSERT INTO Tasa_Prestamo VALUES (7, 501.00, 1000.00, 50.00);
INSERT INTO Tasa_Prestamo VALUES (11, 100.00, 500.00, 50.00);
INSERT INTO Tasa_Prestamo VALUES (11, 501.00, 1000.00, 60.00);
INSERT INTO Tasa_Prestamo VALUES (12, 100.00, 500.00, 60.00);
INSERT INTO Tasa_Prestamo VALUES (12, 501.00, 1000.00, 70.00);
INSERT INTO Tasa_Prestamo VALUES (12, 1001.00, 2000.00, 80.00);


#Pagos
UPDATE Pago SET fecha_pago = str_to_date('16/02/2021', '%d/%m/%Y') WHERE nro_prestamo = 1 AND nro_pago = 1;
UPDATE Pago SET fecha_pago = str_to_date('25/03/2021', '%d/%m/%Y') WHERE nro_prestamo = 1 AND nro_pago = 2;
UPDATE Pago SET fecha_pago = str_to_date('17/06/2021', '%d/%m/%Y') WHERE nro_prestamo = 3 AND nro_pago = 1;
UPDATE Pago SET fecha_pago = str_to_date('6/10/2021', '%d/%m/%Y') WHERE nro_prestamo = 4 AND nro_pago = 1;
UPDATE Pago SET fecha_pago = str_to_date('6/11/2021', '%d/%m/%Y') WHERE nro_prestamo = 4 AND nro_pago = 2;

#Cajas Ahorro
INSERT INTO Caja_Ahorro(CBU, saldo) VALUES (456132189756432187, 5000.00);
INSERT INTO Caja_Ahorro(CBU, saldo) VALUES (642797531468085315, 1054.94);
INSERT INTO Caja_Ahorro(CBU, saldo) VALUES (747953790743797435, 1700.01);
INSERT INTO Caja_Ahorro(CBU, saldo) VALUES (986314790753257897, 1356.84);
INSERT INTO Caja_Ahorro(CBU, saldo) VALUES (863468985432356775, 14589.64);
INSERT INTO Caja_Ahorro(CBU, saldo) VALUES (855383936454882532, 15000.00);
INSERT INTO Caja_Ahorro(CBU, saldo) VALUES (542234235556456476, 100.00);
INSERT INTO Caja_Ahorro(CBU, saldo) VALUES (893248734294793231, 5000.00);
INSERT INTO Caja_Ahorro(CBU, saldo) VALUES (525256757241313131, 75000.00);
INSERT INTO Caja_Ahorro(CBU, saldo) VALUES (927352728292725252, 5225.00);
INSERT INTO Caja_Ahorro(CBU, saldo) VALUES (483836353839383635, 55685.73);
INSERT INTO Caja_Ahorro(CBU, saldo) VALUES (523235236754743222, 7532.67);
INSERT INTO Caja_Ahorro(CBU, saldo) VALUES (625235235568684321, 5511.05);
INSERT INTO Caja_Ahorro(CBU, saldo) VALUES (564456456242342364, 12500.00);
INSERT INTO Caja_Ahorro(CBU, saldo) VALUES (148343545346464634, 50.00);


#Transacciones
INSERT INTO Transaccion(fecha, hora, monto) VALUES (str_to_date('30/09/2021', '%d/%m/%Y'), '15:30', 2000.00);
INSERT INTO Transaccion(fecha, hora, monto) VALUES (str_to_date('12/05/2021', '%d/%m/%Y'), '12:00', 10000.00);
INSERT INTO Transaccion(fecha, hora, monto) VALUES (str_to_date('21/01/2021', '%d/%m/%Y'), '09:35', 1000.00);
INSERT INTO Transaccion(fecha, hora, monto) VALUES (str_to_date('30/09/2021', '%d/%m/%Y'), '11:21', 25000.00);
INSERT INTO Transaccion(fecha, hora, monto) VALUES (str_to_date('13/05/2021', '%d/%m/%Y'), '12:58', 550.00);
INSERT INTO Transaccion(fecha, hora, monto) VALUES (str_to_date('01/07/2021', '%d/%m/%Y'), '13:13', 1313.13);
INSERT INTO Transaccion(fecha, hora, monto) VALUES (str_to_date('30/09/2021', '%d/%m/%Y'), '12:56', 21000.00);
INSERT INTO Transaccion(fecha, hora, monto) VALUES (str_to_date('29/11/2020', '%d/%m/%Y'), '16:21', 1543.00);
INSERT INTO Transaccion(fecha, hora, monto) VALUES (str_to_date('22/02/2021', '%d/%m/%Y'), '08:13', 7500.00);
INSERT INTO Transaccion(fecha, hora, monto) VALUES (str_to_date('13/12/2020', '%d/%m/%Y'), '10:39', 8000.00);
INSERT INTO Transaccion(fecha, hora, monto) VALUES (str_to_date('30/08/2021', '%d/%m/%Y'), '11:23', 15000.00);
INSERT INTO Transaccion(fecha, hora, monto) VALUES (str_to_date('18/05/2021', '%d/%m/%Y'), '14:42', 1000.00);
INSERT INTO Transaccion(fecha, hora, monto) VALUES (str_to_date('13/03/2021', '%d/%m/%Y'), '13:47', 1500.00);
INSERT INTO Transaccion(fecha, hora, monto) VALUES (str_to_date('19/07/2021', '%d/%m/%Y'), '11:06', 6000.00);
INSERT INTO Transaccion(fecha, hora, monto) VALUES (str_to_date('21/09/2021', '%d/%m/%Y'), '10:49', 12500.00);
INSERT INTO Transaccion(fecha, hora, monto) VALUES (str_to_date('12/05/2021', '%d/%m/%Y'), '17:15', 5000.00);
INSERT INTO Transaccion(fecha, hora, monto) VALUES (str_to_date('01/02/2021', '%d/%m/%Y'), '12:13', 1200.00);
INSERT INTO Transaccion(fecha, hora, monto) VALUES (str_to_date('21/09/2020', '%d/%m/%Y'), '16:00', 20000.00);
INSERT INTO Transaccion(fecha, hora, monto) VALUES (str_to_date('30/09/2020', '%d/%m/%Y'), '20:12', 25000.00);
INSERT INTO Transaccion(fecha, hora, monto) VALUES (str_to_date('15/03/2021', '%d/%m/%Y'), '18:40', 5000.00);
INSERT INTO Transaccion(fecha, hora, monto) VALUES (str_to_date('21/01/2021', '%d/%m/%Y'), '23:25', 100000.00);
INSERT INTO Transaccion(fecha, hora, monto) VALUES (str_to_date('01/09/2021', '%d/%m/%Y'), '25:59', 500.00);

#Cajas
INSERT INTO Caja VALUES ();
INSERT INTO Caja VALUES ();
INSERT INTO Caja VALUES ();
INSERT INTO Caja VALUES ();
INSERT INTO Caja VALUES ();
INSERT INTO Caja VALUES ();
INSERT INTO Caja VALUES ();
INSERT INTO Caja VALUES ();
INSERT INTO Caja VALUES ();
INSERT INTO Caja VALUES ();
INSERT INTO Caja VALUES ();
INSERT INTO Caja VALUES ();
INSERT INTO Caja VALUES (100); #Hay que agregarla, ya que es el codigoATM


#ATMs
INSERT INTO ATM VALUES (1, 8000, 'Rondeau 200');
INSERT INTO ATM VALUES (5, 8300, 'Buenos Aires 1200');
INSERT INTO ATM VALUES (2, 8000, 'Colon 146');
INSERT INTO ATM VALUES (12, 7600, 'Figueroa 1530');
INSERT INTO ATM VALUES (11, 7630, 'Florencio Sanchez 135');
INSERT INTO ATM VALUES (9, 8000, 'Undiano 764');
INSERT INTO ATM VALUES (3, 8300, 'San Martin 1574');
INSERT INTO ATM VALUES (7, 8000, 'Bolivia 450');
INSERT INTO ATM VALUES (4, 7600, 'Sarmiento 125');
INSERT INTO ATM VALUES (8, 7630, 'Tesei 642');
INSERT INTO ATM VALUES (10, 7600, 'Funes 479');
INSERT INTO ATM VALUES (6, 8000, 'Alem 146');
INSERT INTO ATM VALUES (100, 8000, 'Mi Casa 123');

#Ventanillas
INSERT INTO Ventanilla VALUES (1, 2);
INSERT INTO Ventanilla VALUES (2, 4);
INSERT INTO Ventanilla VALUES (3, 3);
INSERT INTO Ventanilla VALUES (12, 1);
INSERT INTO Ventanilla VALUES (11, 4);
INSERT INTO Ventanilla VALUES (10, 4);
INSERT INTO Ventanilla VALUES (4, 2);
INSERT INTO Ventanilla VALUES (6, 1);
INSERT INTO Ventanilla VALUES (9, 3);
INSERT INTO Ventanilla VALUES (8, 3);



#Transacciones por caja
INSERT INTO Transaccion_por_caja VALUES (16, 12);
INSERT INTO Transaccion_por_caja VALUES (5, 3);
INSERT INTO Transaccion_por_caja VALUES (12, 10);
INSERT INTO Transaccion_por_caja VALUES (11, 9);
INSERT INTO Transaccion_por_caja VALUES (6, 11);
INSERT INTO Transaccion_por_caja VALUES (7, 1);
INSERT INTO Transaccion_por_caja VALUES (2, 4);
INSERT INTO Transaccion_por_caja VALUES (1, 3);
INSERT INTO Transaccion_por_caja VALUES (4, 8);
INSERT INTO Transaccion_por_caja VALUES (9, 12);
INSERT INTO Transaccion_por_caja VALUES (8, 4);
INSERT INTO Transaccion_por_caja VALUES (3, 7);
INSERT INTO Transaccion_por_caja VALUES (17, 5);
INSERT INTO Transaccion_por_caja VALUES (18, 10);
INSERT INTO Transaccion_por_caja VALUES (19, 1);
INSERT INTO Transaccion_por_caja VALUES (20, 8);
INSERT INTO Transaccion_por_caja VALUES (21, 9);
INSERT INTO Transaccion_por_caja VALUES (22, 6);


#Depositos
INSERT INTO Deposito VALUES (16, 15);
INSERT INTO Deposito VALUES (1, 14);
INSERT INTO Deposito VALUES (3, 12);
INSERT INTO Deposito VALUES (5, 7);
INSERT INTO Deposito VALUES (6, 5);
INSERT INTO Deposito VALUES (12, 1);
INSERT INTO Deposito VALUES (11, 10);
INSERT INTO Deposito VALUES (7, 2);
INSERT INTO Deposito VALUES (2, 6);
INSERT INTO Deposito VALUES (4, 11);


#Plazos Cliente
INSERT INTO Plazo_Cliente VALUES (1, 1);
INSERT INTO Plazo_Cliente VALUES (2, 1);
INSERT INTO Plazo_Cliente VALUES (3, 2);
INSERT INTO Plazo_Cliente VALUES (4, 4);
INSERT INTO Plazo_Cliente VALUES (5, 7);
INSERT INTO Plazo_Cliente VALUES (6, 8);




#Clientes CA
INSERT INTO Cliente_CA VALUES (1, 1);
INSERT INTO Cliente_CA VALUES (2, 2);
INSERT INTO Cliente_CA VALUES (3, 3);
INSERT INTO Cliente_CA VALUES (4, 4);
INSERT INTO Cliente_CA VALUES (5, 5);
INSERT INTO Cliente_CA VALUES (6, 6);
INSERT INTO Cliente_CA VALUES (7, 7);
INSERT INTO Cliente_CA VALUES (8, 8);
INSERT INTO Cliente_CA VALUES (9, 9);
INSERT INTO Cliente_CA VALUES (1, 10);
INSERT INTO Cliente_CA VALUES (6, 11);
INSERT INTO Cliente_CA VALUES (2, 12);
INSERT INTO Cliente_CA VALUES (2, 13);
INSERT INTO Cliente_CA VALUES (7, 14);
INSERT INTO Cliente_CA VALUES (9, 15);




#Tarjetas
INSERT INTO Tarjeta(PIN, CVT, fecha_venc, nro_cliente, nro_ca) VALUES (md5('1234'), md5('7561'), str_to_date('25/12/2021', '%d/%m/%Y'), 1, 1);
INSERT INTO Tarjeta(PIN, CVT, fecha_venc, nro_cliente, nro_ca) VALUES (md5('2345'), md5('6431'), str_to_date('25/12/2021', '%d/%m/%Y'), 2, 2);
INSERT INTO Tarjeta(PIN, CVT, fecha_venc, nro_cliente, nro_ca) VALUES (md5('3456'), md5('7894'), str_to_date('25/12/2021', '%d/%m/%Y'), 3, 3);
INSERT INTO Tarjeta(PIN, CVT, fecha_venc, nro_cliente, nro_ca) VALUES (md5('5678'), md5('1234'), str_to_date('25/12/2021', '%d/%m/%Y'), 5, 5);
INSERT INTO Tarjeta(PIN, CVT, fecha_venc, nro_cliente, nro_ca) VALUES (md5('4567'), md5('6451'), str_to_date('25/12/2021', '%d/%m/%Y'), 4, 4);
INSERT INTO Tarjeta(PIN, CVT, fecha_venc, nro_cliente, nro_ca) VALUES (md5('6789'), md5('6784'), str_to_date('25/12/2021', '%d/%m/%Y'), 6, 6);
INSERT INTO Tarjeta(PIN, CVT, fecha_venc, nro_cliente, nro_ca) VALUES (md5('7890'), md5('4531'), str_to_date('25/12/2021', '%d/%m/%Y'), 7, 7);
INSERT INTO Tarjeta(PIN, CVT, fecha_venc, nro_cliente, nro_ca) VALUES (md5('8901'), md5('7684'), str_to_date('25/12/2021', '%d/%m/%Y'), 8, 8);
INSERT INTO Tarjeta(PIN, CVT, fecha_venc, nro_cliente, nro_ca) VALUES (md5('9012'), md5('4145'), str_to_date('25/12/2021', '%d/%m/%Y'), 9, 9);
INSERT INTO Tarjeta(PIN, CVT, fecha_venc, nro_cliente, nro_ca) VALUES (md5('9876'), md5('1245'), str_to_date('25/12/2021', '%d/%m/%Y'), 1, 10);
INSERT INTO Tarjeta(PIN, CVT, fecha_venc, nro_cliente, nro_ca) VALUES (md5('8765'), md5('4185'), str_to_date('25/12/2021', '%d/%m/%Y'), 6, 11);
INSERT INTO Tarjeta(PIN, CVT, fecha_venc, nro_cliente, nro_ca) VALUES (md5('7654'), md5('3418'), str_to_date('25/12/2021', '%d/%m/%Y'), 2, 12);
INSERT INTO Tarjeta(PIN, CVT, fecha_venc, nro_cliente, nro_ca) VALUES (md5('6543'), md5('4859'), str_to_date('25/12/2021', '%d/%m/%Y'), 2, 13);
INSERT INTO Tarjeta(PIN, CVT, fecha_venc, nro_cliente, nro_ca) VALUES (md5('5432'), md5('7485'), str_to_date('25/12/2021', '%d/%m/%Y'), 7, 14);
INSERT INTO Tarjeta(PIN, CVT, fecha_venc, nro_cliente, nro_ca) VALUES (md5('15'), md5('3415'), str_to_date('25/12/2021', '%d/%m/%Y'), 9, 15);




#Debitos
INSERT INTO Debito VALUES (10, 'Para album de stickers de Dragon Ball', 2, 2);
INSERT INTO Debito VALUES (13, 'Descripcion generica', 1, 1);
INSERT INTO Debito VALUES (14, 'Un auto', 5, 5);
INSERT INTO Debito VALUES (15, 'GTX 4050 Tii', 1, 10);



#Transferencia
INSERT INTO Transferencia VALUES (8, 1, 1, 2);
INSERT INTO Transferencia VALUES (9, 3, 3, 5);
INSERT INTO Transferencia VALUES (17, 5, 5, 7);
INSERT INTO Transferencia VALUES (18, 1, 10, 1);



#Extracciones
INSERT INTO Extraccion VALUES (19, 1, 1);
INSERT INTO Extraccion VALUES (20, 8, 8);
INSERT INTO Extraccion VALUES (21, 9, 9);
INSERT INTO Extraccion VALUES (22, 6, 6);