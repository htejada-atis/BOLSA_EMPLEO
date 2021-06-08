Insert into ADM_PARAMETROS
   (CONFIG_CODALF, PARAM_CODALF, DESID, VALOR)
 Values
   ('default', 'administracion.recaptcha.claveDelSitio', 'clave del sitio para recaptcha', '6LfN36sUAAAAAIJvKq9WgOnlTxoCiumJPKks0l_2');
Insert into ADM_PARAMETROS
   (CONFIG_CODALF, PARAM_CODALF, DESID, VALOR)
 Values
   ('default', 'administracion.recaptcha.claveSecreta', 'clave secreta para recaptcha', '6LfN36sUAAAAAFbswh6onFXl8Z9uCppEnuMBx5q2');

INSERT INTO adm_parametros (
   config_codalf, param_codalf, desid, 
   valor) 
VALUES ( DEFAULT,
 'autoaprovisionado.recaptcha.activado',
 'indica si está activado recaptcha para autoregistrados',
  'S');

INSERT INTO adm_parametros (
   config_codalf, param_codalf, desid, 
   valor) 
VALUES ( DEFAULT,
 'autoaprovisionado.mail.asunto',
 'asunto del mail que se manda para verificar un correo autoregistrada ',
  'Solicitud de validación de correo para ujaen.es');
  
INSERT INTO adm_parametros (
   config_codalf, param_codalf, desid, 
   valor) 
VALUES ( DEFAULT,
 'autoaprovisionado.mail.cuerpo',
 'cuerpo del mail que se manda para verificar un correo autoregistrada ',
  'Ha solicitado la generación de una nueva cuenta para su uso en ujaen.es.

El código de autorización para autorizar el uso del correo es: %%CODIGO%%

Puede acceder a la siguiente dirección https://uvirtual.ujaen.es/pub/es/operaciones/autoregistrado/usuarioautoresgistrado?idCambio=%%IDSOLICITUD%%&email=%%CORREO%%

En caso de que no haya realizado esta petición ignore este email.');

  Insert into ADM_MENU
   (MNU_CODNUM, CONTROLADOR, FLG_DISPONIBLE, FLG_MOSTRAR, FLG_CREAMENU, 
    FLG_PDFINTERNO, FLG_EXCEL, FLG_WORD, FLG_ANONIMO, 
    PADRE_CODNUM, FLG_LOG)
 Values
   (398, 'operaciones.autoregistrado.usuarioautoresgistrado', 'S', 'N', 'S', 
    'N', 'N', 'N', 'S', 
    18, 'N');

   Insert into ADM_MENU_IDM
   (MNU_CODNUM, IDM_CODALF, DESCRIPCION)
 Values
   (398, 'es', 'Cuenta autoregistrada');

   Insert into ADM_MENU_SIS
   (MNU_CODNUM, SIS_CODALF)
 Values
   (398, 'uvirtual');

   Insert into ADM_MENU_RED
   (MNU_CODNUM, RED, DESCRIPCION, FLG_DESACT)
 Values
   (398, '0.0.0.0/0', 'Publico', 'N');
   
Insert into ADM_ROL
   (ROL_CODNUM, DESCRIPCION, VALOR)
 Values
   (335, 'Usuario que se ha autoregistrado en uvirtual', 'UsuarioAutoregistrado');

