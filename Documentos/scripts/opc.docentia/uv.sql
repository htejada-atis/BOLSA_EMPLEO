INSERT INTO adm_parametros (
   config_codalf, param_codalf, desid, 
   valor) 
VALUES ( DEFAULT,
 'docentia.urlAyuda',
 'url que enlace a la ayuda ',
  'https://www.ujaen.es/gobierno/secord/docentia-academia');
  
  Insert into ADM_MENU
   (MNU_CODNUM, CONTROLADOR, FLG_DISPONIBLE, FLG_MOSTRAR, FLG_CREAMENU, 
    FLG_PDFINTERNO, FLG_EXCEL, FLG_WORD, FLG_ANONIMO, 
    PADRE_CODNUM, FLG_LOG)
 Values
   (369, 'informacionadministrativa.docentia', 'S', 'S', 'S', 
    'N', 'N', 'N', 'N', 
    16, 'N');

Insert into ADM_MENU
   (MNU_CODNUM, CONTROLADOR, FLG_DISPONIBLE, FLG_MOSTRAR, FLG_CREAMENU, 
    FLG_PDFINTERNO, FLG_EXCEL, FLG_WORD, FLG_ANONIMO, 
    PADRE_CODNUM, FLG_LOG)
 Values
   (383, 'informacionadministrativa.docentia.convocatoriacrud', 'S', 'S', 'S', 
    'N', 'N', 'N', 'N', 
    369, 'N');

    Insert into ADM_MENU_DOM
   (MNU_CODNUM, DOM_CODALF)
 Values
   (369, 'ujaen.es');

Insert into ADM_MENU_DOM
   (MNU_CODNUM, DOM_CODALF)
 Values
   (383, 'ujaen.es');

   Insert into ADM_MENU_IDM
   (MNU_CODNUM, IDM_CODALF, DESCRIPCION)
 Values
   (369, 'es', 'Docentia');

Insert into ADM_MENU_IDM
   (MNU_CODNUM, IDM_CODALF, DESCRIPCION)
 Values
   (383, 'es', 'Datos Convocatorias');

   insert into ADM_ROL
	(rol_codnum, descripcion, valor)
values(
    328, 'docnetia administrador', 'dnctadministrador');
   
   insert into ADM_ROL
	(rol_codnum, descripcion, valor)
values(
    332, 'docentia responsable centro', 'dnctresponsablecentro');
   insert into ADM_ROL
	(rol_codnum, descripcion, valor)
values(
    333, 'docentia responsable departamento', 'dnctresponsabledepartamento');
   insert into ADM_ROL
	(rol_codnum, descripcion, valor)
values(
    334, 'docentia miemro comision', 'dnctmiembrocomision');
   
Insert into ADM_MENU_ROL
   (MNU_CODNUM, ROL_CODNUM, FLG_DESACT, FLG_ADMIN)
 Values
   (369, 328, 'N', 'N');

Insert into ADM_MENU_ROL
   (MNU_CODNUM, ROL_CODNUM, FLG_DESACT, FLG_ADMIN)
 Values
   (383, 328, 'N', 'N');

   Insert into ADM_MENU_SIS
   (MNU_CODNUM, SIS_CODALF)
 Values
   (369, 'uvirtual');

Insert into ADM_MENU_SIS
   (MNU_CODNUM, SIS_CODALF)
 Values
   (383, 'uvirtual');

   Insert into ADM_MENU_RED
   (MNU_CODNUM, RED, DESCRIPCION, FLG_DESACT)
 Values
   (369, '0.0.0.0/0', 'Publico', 'N');

Insert into ADM_MENU_RED
   (MNU_CODNUM, RED, DESCRIPCION, FLG_DESACT)
 Values
   (383, '0.0.0.0/0', 'Publico', 'N');    
   
   Insert into ADM_USUARIO_ROL
    (USERUID, ROL_CODNUM, FLG_ADMIN)
 Values 
    ('usig', 328, 'N');