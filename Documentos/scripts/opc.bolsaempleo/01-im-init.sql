INSERT INTO adm_parametros (config_codalf, param_codalf, desid, valor) VALUES ( DEFAULT, 'bolsaempleo.urlAyuda', 'url que enlace a la ayuda ', 'https://www.ujaen.es/gobierno/secord/bolsaempleo-academia'); 
INSERT INTO ADM_ROL (rol_codnum, descripcion, valor) VALUES(1050, 'Integrantes del Servicio de Personal', 'bolemppersonal');
INSERT INTO ADM_ROL (rol_codnum, descripcion, valor) VALUES(1051, 'Miembro de la Comision', 'bolempcomision');
INSERT INTO ADM_ROL (rol_codnum, descripcion, valor) VALUES(1052, 'Candidatos', 'bolempcandidato');
INSERT INTO ADM_USUARIO_ROL (USERUID, ROL_CODNUM, FLG_ADMIN) VALUES ('usig', 1050, 'N');