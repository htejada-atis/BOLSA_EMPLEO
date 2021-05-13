Insert into ARCOS.V_PERSONA_INTRANET
   (CODINT, UJAENDNI, NUMDOCUMENTO, STRTIPODOCUMENTO, STRNOMBRE, 
    STRAPELLIDO1, STRAPELLIDO2, SEXO, COD_PERSONA_AC, COD_PERSONA_RH, 
    BLNMANUAL)
 Values
   (1, '12345678', '12345678Z', 'NIF', 'USIG', 
    'Usuario de', 'pruebas', 'V', 1, 1, 
    'S');

Insert into ARCOS.V_CUENTAS_INTRANET
   (IDCUENTA, IDIDENTIFICADOR, STRDN, CODINT, IDDOMINIO, 
    BLNBLOQUEADA, BLNCTAGGL, BLNCTAGGLSUSP, BLNINSTITUCIONAL, STRDESCRIPCION, 
    STRCORREORUTA)
 Values
   (1, 'usig', 'uid=usig,cn=users,dc=ujaen,dc=es', 1, 'ujaen.es', 
    'N', 'N', 'N', 'N', 'Usuario de pruebas, USIG', 
    'usig@xxx');

Insert into ARCOS.V_PERSONA_INTRANET
   (CODINT, UJAENDNI, NUMDOCUMENTO, STRTIPODOCUMENTO, STRNOMBRE, 
    STRAPELLIDO1, STRAPELLIDO2, SEXO, COD_PERSONA_AC, COD_PERSONA_RH, 
    BLNMANUAL)
 Values
   (2, '12345679', '12345679S', 'NIF', 'estudiante prueba1', 
    'Usuario de', 'pruebas', 'V', 2, 2, 
    'S');

Insert into ARCOS.V_CUENTAS_INTRANET
   (IDCUENTA, IDIDENTIFICADOR, STRDN, CODINT, IDDOMINIO, 
    BLNBLOQUEADA, BLNCTAGGL, BLNCTAGGLSUSP, BLNINSTITUCIONAL, STRDESCRIPCION, 
    STRCORREORUTA)
 Values
   (2, 'estudiante1', 'uid=estudiante1,cn=users,dc=ujaen,dc=es', 1, 'red.ujaen.es', 
    'N', 'N', 'N', 'N', 'Usuario de pruebas, estudiante', 
    'estudiante1@xxx');
    

