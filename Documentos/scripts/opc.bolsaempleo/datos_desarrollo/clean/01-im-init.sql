DELETE FROM ADM_USUARIO_ROL WHERE USERUID = 'usig' AND ROL_CODNUM = 1050;
DELETE FROM ADM_ROL WHERE rol_codnum IN (1050, 1051, 1052, 1053);
DELETE FROM adm_parametros WHERE config_codalf = 'default' AND param_codalf = 'bolsaempleo.urlAyuda';