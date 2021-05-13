create sequence sec_arg_usuario start with 1 increment by 1; 
--/////////////////////
CREATE TABLE ARCOS.ARG_USUARIO (	
	USUARIO VARCHAR2(100) NOT NULL, 
	NOMBRE VARCHAR2(100 BYTE) NOT NULL, 
	APELLIDO1 VARCHAR2(100 BYTE) NOT NULL, 
	APELLIDO2 VARCHAR2(100 BYTE) DEFAULT NULL,  
	DOCUMENTO VARCHAR2(100 BYTE) NOT NULL,
	TIPODOCUMENTO VARCHAR2(6 BYTE) NOT NULL,
	SEXO VARCHAR2(1) NOT NULL,
	CONSTRAINT CH_ARG_TIPODOCUMENTO CHECK (TIPODOCUMENTO in('NIF','PAS','NIE')), 
	CONSTRAINT CH_ARG_SEXO CHECK (SEXO in('H','M','N')),
	CONSTRAINT pk_arg_usuario PRIMARY KEY (USUARIO)
);
--/////////////////////
COMMENT ON TABLE ARCOS.ARG_USUARIO IS 'Tabla para usuarios autoregistrados';
--/////////////////////
COMMENT ON COLUMN ARCOS.ARG_USUARIO.USUARIO IS 'identificador del usuario autoregistrado';
--/////////////////////
COMMENT ON COLUMN ARCOS.ARG_USUARIO.NOMBRE IS 'Nombre del usuario';
--/////////////////////
COMMENT ON COLUMN ARCOS.ARG_USUARIO.APELLIDO1 IS 'Primer apellido del usuario';
--/////////////////////
COMMENT ON COLUMN ARCOS.ARG_USUARIO.APELLIDO2 IS 'Segundo apellido del usuario';
--/////////////////////
COMMENT ON COLUMN ARCOS.ARG_USUARIO.DOCUMENTO IS 'Documento identificativo del usuario';
--/////////////////////
COMMENT ON COLUMN ARCOS.ARG_USUARIO.TIPODOCUMENTO IS 'Tipo de Documento identificativo del usuario (NIF, PAS, NIE)';
--/////////////////////
COMMENT ON COLUMN ARCOS.ARG_USUARIO.SEXO IS 'Sexo del usuario H-Hombre, M-Mujer, N-No informado';
--/////////////////////

CREATE TABLE ARCOS.ARG_CUENTA (
	USUARIO VARCHAR2(100) NOT NULL, 
	CORREO VARCHAR2(200) NOT NULL, 
	CLAVE VARCHAR2(512) NOT NULL, 
	FECHA_CREACION TIMESTAMP NOT NULL,
	CONSTRAINT pk_arg_cuenta PRIMARY KEY (correo),
	CONSTRAINT fk_arg_cuenta_usuario FOREIGN KEY (USUARIO) REFERENCES ARG_USUARIO(USUARIO)
);
--/////////////////////
COMMENT ON TABLE ARCOS.ARG_CUENTA IS 'Tabla para cuentas de usuarios autoregistrados';
--/////////////////////
COMMENT ON COLUMN ARCOS.ARG_CUENTA.USUARIO IS 'Usuario al que pertenece la cuenta';
--/////////////////////
COMMENT ON COLUMN ARCOS.ARG_CUENTA.CORREO IS 'Correo asociado a la cuenta';
--/////////////////////
COMMENT ON COLUMN ARCOS.ARG_CUENTA.CLAVE IS 'Clave de la cuenta';
--/////////////////////
COMMENT ON COLUMN ARCOS.ARG_CUENTA.FECHA_CREACION IS 'Fecha de creación de la cuenta';
--/////////////////////

CREATE TABLE ARCOS.ARG_PETICION_CAMBIO_CLAVE (
	CORREO VARCHAR2(200) NOT NULL,
	CODIGO_TEMPORAL VARCHAR2(100) NOT NULL,
	FECHA_SOLICITUD TIMESTAMP NOT NULL,
	FECHA_VERIFICACION TIMESTAMP,
	IP_SOLICITUD VARCHAR2(128) NOT NULL,
	IP_VERIFICACION VARCHAR2(128),
	IDENTIFICADOR VARCHAR2(1000) NOT NULL,
	VERIFICADA VARCHAR2(1) DEFAULT 'N' NOT NULL,
	CONSTRAINT pk_arg_peticion_cambio_clave PRIMARY KEY (identificador),
	CONSTRAINT fk_arg_peticion_cam_cuenta FOREIGN KEY (CORREO) REFERENCES ARG_CUENTA (CORREO)
);
--/////////////////////
COMMENT ON TABLE ARCOS.ARG_PETICION_CAMBIO_CLAVE IS 'Tabla para peticiones de cambio de clave';
--/////////////////////
COMMENT ON COLUMN ARCOS.ARG_PETICION_CAMBIO_CLAVE.CORREO IS 'Correo de la cuenta al que se le quiere cambiar la clave';
--/////////////////////
COMMENT ON COLUMN ARCOS.ARG_PETICION_CAMBIO_CLAVE.CODIGO_TEMPORAL IS 'Codigo temporal que tiene que verificar el usuario en su correo';
--/////////////////////
COMMENT ON COLUMN ARCOS.ARG_PETICION_CAMBIO_CLAVE.FECHA_SOLICITUD IS 'Fecha en la que solicita el cambio de clave';
--/////////////////////
COMMENT ON COLUMN ARCOS.ARG_PETICION_CAMBIO_CLAVE.FECHA_VERIFICACION IS 'Fecha en la que ha accedido con el codigo temporal correcto y se le ha dado una clave';
--/////////////////////
COMMENT ON COLUMN ARCOS.ARG_PETICION_CAMBIO_CLAVE.IP_SOLICITUD IS 'ip desde que hace la solicitud del cambio de clave';
--/////////////////////
COMMENT ON COLUMN ARCOS.ARG_PETICION_CAMBIO_CLAVE.IP_VERIFICACION IS 'ip desde la que se ha verificado';
--/////////////////////
COMMENT ON COLUMN ARCOS.ARG_PETICION_CAMBIO_CLAVE.IDENTIFICADOR IS 'Identificador aleatorio para referencial al cambio de clave';
