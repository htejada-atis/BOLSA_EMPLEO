DELETE FROM ADM_MENU_ROL WHERE MNU_CODNUM = 1139;
--/////////////////////
DELETE FROM ADM_MENU_RED WHERE MNU_CODNUM = 1139;
--/////////////////////
DELETE FROM ADM_MENU_SIS WHERE MNU_CODNUM = 1139;
--/////////////////////
DELETE FROM ADM_MENU_IDM WHERE MNU_CODNUM = 1139;
--/////////////////////
DELETE FROM ADM_MENU_DOM WHERE MNU_CODNUM = 1139;
--/////////////////////
DELETE FROM ADM_MENU WHERE MNU_CODNUM = 1139;
--/////////////////////
DELETE FROM TBEP_PARAMETROS_CONFIG WHERE NOMBRE IN ('bolsaempleo.local.idPlantillaAlegacionResuelta','bolsaempleo.local.idPlantillaAlegacionEnviadaDepartamento');
--/////////////////////
DELETE FROM TBEP_PLANTILLAS WHERE NOMBRE IN ('Plantilla alegación resuelta','Plantilla alegación enviada a departamento');
--/////////////////////
DROP TABLE TBEP_HTO_SOL_MER_BOL_ALE_FILE;
--/////////////////////
DROP TABLE TBEP_SOL_MER_BOL_ALE_FILE;
--/////////////////////
DROP SEQUENCE QBEP_HTO_ALF;
--/////////////////////
DROP SEQUENCE QBEP_ALF;
--/////////////////////
DROP TABLE TBEP_HTO_SOL_MER_BOL_ALEGACION;
--/////////////////////
DROP TABLE TBEP_SOL_MER_BOL_ALEGACION;
--/////////////////////
DROP SEQUENCE QBEP_HTO_ALM;
--/////////////////////
DROP SEQUENCE QBEP_ALM;
--/////////////////////
DROP TABLE TBEP_HTO_ALEGACIONES;
--/////////////////////
DROP TABLE TBEP_ALEGACIONES;
--/////////////////////
DROP SEQUENCE QBEP_HTO_ALE;
--/////////////////////
DROP SEQUENCE QBEP_ALE;