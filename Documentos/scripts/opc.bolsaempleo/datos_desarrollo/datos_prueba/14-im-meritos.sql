INSERT INTO TBEP_MERITOS (BEPITE_CODNUM,BEPUSU_CODNUM,VALOR,DESCRIPCION,OBSERVACION,ARCHIVO)
VALUES (
    (SELECT bepite.CODNUM FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 1),
    (SELECT bepusu.CODNUM FROM TBEP_USUARIOS bepusu WHERE bepusu.CODCUENTA = 'candidato1'),
    2.4,
    'descripción mérito 1',
    'observación mérito 1',
    hextoraw('453d7a34')
);
INSERT INTO TBEP_MERITOS (BEPITE_CODNUM,BEPUSU_CODNUM,VALOR,DESCRIPCION,OBSERVACION,ARCHIVO)
VALUES (
    (SELECT bepite.CODNUM FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 2),
    (SELECT bepusu.CODNUM FROM TBEP_USUARIOS bepusu WHERE bepusu.CODCUENTA = 'candidato1'),
    4,
    'descripción mérito 1',
    'observación mérito 1',
    hextoraw('453d7a34')
);
INSERT INTO TBEP_MERITOS (BEPITE_CODNUM,BEPUSU_CODNUM,VALOR,DESCRIPCION,OBSERVACION,ARCHIVO)
VALUES (
    (SELECT bepite.CODNUM FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 3),
    (SELECT bepusu.CODNUM FROM TBEP_USUARIOS bepusu WHERE bepusu.CODCUENTA = 'candidato1'),
    3,
    'descripción mérito 1',
    'observación mérito 1',
    hextoraw('453d7a34')
);