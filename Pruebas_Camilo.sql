-- --------------------------------------------------------------------- TABLA CLIENTE ---------------------------------------------------------------------

DESC cliente;

SELECT * FROM cliente;

ALTER TABLE cliente ADD estado BIT NOT NULL;

ALTER TABLE cliente DROP rol;


INSERT INTO cliente(nombre,apellido,identificacion,telefono,direccion,usuario,contrasena,fecha_nacimiento,correo,rol,id_tipo_cliente,id_tipo_documento) 
VALUES('Camilo','Diaz','1105681370','3103192771','Cra 80 G # 43 - 51 Sur','CamiloDiaz201','38FED759B60A9A01173651F929F73E3E6FFF82DAF40746AFE5BA15202F6ECAB9','2017-09-05','camilo_diaz_201@hotmail.com',1,1,2,1);

UPDATE cliente SET contrasena = '38FED759B60A9A01173651F929F73E3E6FFF82DAF40746AFE5BA15202F6ECAB9' WHERE identificacion = '1105681370';

UPDATE cliente SET estado = 1 WHERE identificacion = '1105681370';


-- ---------------------------------------------------------------------TABLA TIPO CLIENTE ---------------------------------------------------------------------

DESC tipo_cliente;

SELECT * FROM tipo_cliente;

UPDATE tipo_cliente SET nombre = 'USUARIO', estado = 0 WHERE id_tipo_cliente = 5;

DELETE FROM tipo_cliente WHERE id_tipo_cliente IN (3,4);

UPDATE tipo_cliente SET nombre = 'Cliente' WHERE id_tipo_cliente = 2;

INSERT INTO tipo_cliente(nombre,estado) 
VALUE('Administrador',1);

INSERT INTO tipo_cliente(nombre,estado) 
VALUE('cliente',1);

-- --------------------------------------------------------------------- TABLA TIPO DOCUMENTO ---------------------------------------------------------------------

DESC tipo_documento;

SELECT * FROM tipo_documento;

INSERT INTO tipo_documento(nombre,estado) 
VALUE('Cedula',1);

INSERT INTO tipo_documento(nombre,estado) 
VALUE('Pasaporte',1);

INSERT INTO tipo_documento(nombre,estado) 
VALUE('Tarjeta de Identidad',1);

INSERT INTO tipo_documento(nombre,estado) 
VALUE('Registro Civil',1);

INSERT INTO tipo_documento(nombre,estado) 
VALUE('Cedula Extranjera',1);

-- --------------------------------------------------------------------- TABLA AUDITORIA ---------------------------------------------------------------------

DESC auditoria;

ALTER TABLE auditoria ADD estado BIT NOT NULL;

-- --------------------------------------------------------------------- TABLA CLIENTE PRODUCTO ---------------------------------------------------------------------

DESC cliente_producto;

ALTER TABLE cliente_producto CHANGE estado_pago estado BIT;

SELECT * FROM tipo_producto;

UPDATE tipo_producto SET nombre = 'credito'
WHERE id_tipo_producto = 1;

UPDATE tipo_producto SET nombre = 'ahorros'
WHERE id_tipo_producto = 2;

INSERT INTO tipo_producto(nombre,estado) 
VALUE('credito',1);

INSERT INTO tipo_producto(nombre,estado) 
VALUE('ahorros',1);

INSERT INTO tipo_producto(nombre,estado) 
VALUE('corriente',1);

-- --------------------------------------------------------------------- TABLA MOVIMIENTO ---------------------------------------------------------------------

DESC movimiento;

ALTER TABLE movimiento ADD estado BIT NOT NULL;

-- --------------------------------------------------------------------- TABLA TIPO MOVIMIENTO ---------------------------------------------------------------------

SELECT * FROM tipo_movimiento;

