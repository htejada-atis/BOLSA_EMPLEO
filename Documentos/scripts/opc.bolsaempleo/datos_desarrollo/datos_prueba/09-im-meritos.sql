INSERT INTO TBEP_MERITOS (BEPITE_CODNUM,BEPUSU_CODNUM,VALOR,DESCRIPCION,OBSERVACION,ARCHIVO,UID_USUARIO)
VALUES (
    (SELECT bepite.CODNUM FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 1),
    (SELECT bepusu.CODNUM FROM TBEP_USUARIOS bepusu WHERE bepusu.CODCUENTA = 'candidato1'),
    (SELECT greatest(1, round(dbms_random.value(bepite.VALOR_MINIMO, bepite.VALOR_MAXIMO))) FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 1),
    'descripción mérito 1',
    'observación mérito 1',
    hextoraw('453d7a34'),
    'CARGA_INICIAL'
);
INSERT INTO TBEP_MERITOS (BEPITE_CODNUM,BEPUSU_CODNUM,VALOR,DESCRIPCION,OBSERVACION,ARCHIVO,UID_USUARIO)
VALUES (
    (SELECT bepite.CODNUM FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 4),
    (SELECT bepusu.CODNUM FROM TBEP_USUARIOS bepusu WHERE bepusu.CODCUENTA = 'candidato1'),
    (SELECT greatest(1, round(dbms_random.value(bepite.VALOR_MINIMO, bepite.VALOR_MAXIMO))) FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 4),
    'descripción mérito 2',
    'observación mérito 2',
    hextoraw('453d7a34'),
    'CARGA_INICIAL'
);
INSERT INTO TBEP_MERITOS (BEPITE_CODNUM,BEPUSU_CODNUM,VALOR,DESCRIPCION,OBSERVACION,ARCHIVO,UID_USUARIO)
VALUES (
    (SELECT bepite.CODNUM FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 3),
    (SELECT bepusu.CODNUM FROM TBEP_USUARIOS bepusu WHERE bepusu.CODCUENTA = 'candidato1'),
    (SELECT greatest(1, round(dbms_random.value(bepite.VALOR_MINIMO, bepite.VALOR_MAXIMO))) FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 3),
    'descripción mérito 3',
    'observación mérito 3',
    hextoraw('453d7a34'),
    'CARGA_INICIAL'
);
INSERT INTO TBEP_MERITOS (BEPITE_CODNUM,BEPUSU_CODNUM,VALOR,DESCRIPCION,OBSERVACION,ARCHIVO,UID_USUARIO)
VALUES (
    (SELECT bepite.CODNUM FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 7),
    (SELECT bepusu.CODNUM FROM TBEP_USUARIOS bepusu WHERE bepusu.CODCUENTA = 'candidato1'),
    (SELECT greatest(1, round(dbms_random.value(bepite.VALOR_MINIMO, bepite.VALOR_MAXIMO))) FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 7),
    'descripción mérito 4',
    'observación mérito 4',
    hextoraw('453d7a34'),
    'CARGA_INICIAL'
);
INSERT INTO TBEP_MERITOS (BEPITE_CODNUM,BEPUSU_CODNUM,VALOR,DESCRIPCION,OBSERVACION,ARCHIVO,UID_USUARIO)
VALUES (
    (SELECT bepite.CODNUM FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 8),
    (SELECT bepusu.CODNUM FROM TBEP_USUARIOS bepusu WHERE bepusu.CODCUENTA = 'candidato1'),
    (SELECT greatest(1, round(dbms_random.value(bepite.VALOR_MINIMO, bepite.VALOR_MAXIMO))) FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 8),
    'descripción mérito 5',
    'observación mérito 5',
    hextoraw('453d7a34'),
    'CARGA_INICIAL'
);
INSERT INTO TBEP_MERITOS (BEPITE_CODNUM,BEPUSU_CODNUM,VALOR,DESCRIPCION,OBSERVACION,ARCHIVO,UID_USUARIO)
VALUES (
    (SELECT bepite.CODNUM FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 1),
    (SELECT bepusu.CODNUM FROM TBEP_USUARIOS bepusu WHERE bepusu.CODCUENTA = 'candidato2'),
    (SELECT greatest(1, round(dbms_random.value(bepite.VALOR_MINIMO, bepite.VALOR_MAXIMO))) FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 1),
    'descripción mérito 6',
    'observación mérito 6',
    hextoraw('453d7a34'),
    'CARGA_INICIAL'
);
INSERT INTO TBEP_MERITOS (BEPITE_CODNUM,BEPUSU_CODNUM,VALOR,DESCRIPCION,OBSERVACION,ARCHIVO,UID_USUARIO)
VALUES (
    (SELECT bepite.CODNUM FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 4),
    (SELECT bepusu.CODNUM FROM TBEP_USUARIOS bepusu WHERE bepusu.CODCUENTA = 'candidato2'),
    (SELECT greatest(1, round(dbms_random.value(bepite.VALOR_MINIMO, bepite.VALOR_MAXIMO))) FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 4),
    'descripción mérito 7',
    'observación mérito 7',
    hextoraw('453d7a34'),
    'CARGA_INICIAL'
);
INSERT INTO TBEP_MERITOS (BEPITE_CODNUM,BEPUSU_CODNUM,VALOR,DESCRIPCION,OBSERVACION,ARCHIVO,UID_USUARIO)
VALUES (
    (SELECT bepite.CODNUM FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 3),
    (SELECT bepusu.CODNUM FROM TBEP_USUARIOS bepusu WHERE bepusu.CODCUENTA = 'candidato2'),
    (SELECT greatest(1, round(dbms_random.value(bepite.VALOR_MINIMO, bepite.VALOR_MAXIMO))) FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 3),
    'descripción mérito 8',
    'observación mérito 8',
    hextoraw('453d7a34'),
    'CARGA_INICIAL'
);
INSERT INTO TBEP_MERITOS (BEPITE_CODNUM,BEPUSU_CODNUM,VALOR,DESCRIPCION,OBSERVACION,ARCHIVO,UID_USUARIO)
VALUES (
    (SELECT bepite.CODNUM FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 17),
    (SELECT bepusu.CODNUM FROM TBEP_USUARIOS bepusu WHERE bepusu.CODCUENTA = 'candidato1'),
    (SELECT greatest(1, round(dbms_random.value(bepite.VALOR_MINIMO, bepite.VALOR_MAXIMO))) FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 17),
    'descripción mérito 9',
    'observación mérito 9',
    hextoraw('453d7a34'),
    'CARGA_INICIAL'
);
INSERT INTO TBEP_MERITOS (BEPITE_CODNUM,BEPUSU_CODNUM,VALOR,DESCRIPCION,OBSERVACION,ARCHIVO,UID_USUARIO)
VALUES (
    (SELECT bepite.CODNUM FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 1),
    (SELECT bepusu.CODNUM FROM TBEP_USUARIOS bepusu WHERE bepusu.CODCUENTA = 'candidato3'),
    (SELECT greatest(1, round(dbms_random.value(bepite.VALOR_MINIMO, bepite.VALOR_MAXIMO))) FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 1),
    'descripción mérito 10',
    'observación mérito 10',
    hextoraw('453d7a34'),
    'CARGA_INICIAL'
);
INSERT INTO TBEP_MERITOS (BEPITE_CODNUM,BEPUSU_CODNUM,VALOR,DESCRIPCION,OBSERVACION,ARCHIVO,UID_USUARIO)
VALUES (
    (SELECT bepite.CODNUM FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 4),
    (SELECT bepusu.CODNUM FROM TBEP_USUARIOS bepusu WHERE bepusu.CODCUENTA = 'candidato3'),
    (SELECT greatest(1, round(dbms_random.value(bepite.VALOR_MINIMO, bepite.VALOR_MAXIMO))) FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 4),
    'descripción mérito 11',
    'observación mérito 11',
    hextoraw('453d7a34'),
    'CARGA_INICIAL'
);
INSERT INTO TBEP_MERITOS (BEPITE_CODNUM,BEPUSU_CODNUM,VALOR,DESCRIPCION,OBSERVACION,ARCHIVO,UID_USUARIO)
VALUES (
    (SELECT bepite.CODNUM FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 37),
    (SELECT bepusu.CODNUM FROM TBEP_USUARIOS bepusu WHERE bepusu.CODCUENTA = 'candidato3'),
    (SELECT greatest(1, round(dbms_random.value(bepite.VALOR_MINIMO, bepite.VALOR_MAXIMO))) FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 37),
    'descripción mérito 12',
    'observación mérito 12',
    hextoraw('453d7a34'),
    'CARGA_INICIAL'
);
INSERT INTO TBEP_MERITOS (BEPITE_CODNUM,BEPUSU_CODNUM,VALOR,DESCRIPCION,OBSERVACION,ARCHIVO,UID_USUARIO)
VALUES (
    (SELECT bepite.CODNUM FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 9),
    (SELECT bepusu.CODNUM FROM TBEP_USUARIOS bepusu WHERE bepusu.CODCUENTA = 'candidato3'),
    (SELECT greatest(1, round(dbms_random.value(bepite.VALOR_MINIMO, bepite.VALOR_MAXIMO))) FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 9),
    'descripción mérito 13',
    'observación mérito 13',
    hextoraw('453d7a34'),
    'CARGA_INICIAL'
);
INSERT INTO TBEP_MERITOS (BEPITE_CODNUM,BEPUSU_CODNUM,VALOR,DESCRIPCION,OBSERVACION,ARCHIVO,UID_USUARIO)
VALUES (
    (SELECT bepite.CODNUM FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 17),
    (SELECT bepusu.CODNUM FROM TBEP_USUARIOS bepusu WHERE bepusu.CODCUENTA = 'candidato3'),
    (SELECT greatest(1, round(dbms_random.value(bepite.VALOR_MINIMO, bepite.VALOR_MAXIMO))) FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 17),
    'descripción mérito 14',
    'observación mérito 14',
    hextoraw('453d7a34'),
    'CARGA_INICIAL'
);
INSERT INTO TBEP_MERITOS (BEPITE_CODNUM,BEPUSU_CODNUM,VALOR,DESCRIPCION,OBSERVACION,ARCHIVO,UID_USUARIO)
VALUES (
    (SELECT bepite.CODNUM FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 16),
    (SELECT bepusu.CODNUM FROM TBEP_USUARIOS bepusu WHERE bepusu.CODCUENTA = 'candidato3'),
    (SELECT greatest(1, round(dbms_random.value(bepite.VALOR_MINIMO, bepite.VALOR_MAXIMO))) FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 16),
    'descripción mérito 15',
    'observación mérito 15',
    hextoraw('453d7a34'),
    'CARGA_INICIAL'
);
INSERT INTO TBEP_MERITOS (BEPITE_CODNUM,BEPUSU_CODNUM,VALOR,DESCRIPCION,OBSERVACION,ARCHIVO,UID_USUARIO)
VALUES (
    (SELECT bepite.CODNUM FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 16),
    (SELECT bepusu.CODNUM FROM TBEP_USUARIOS bepusu WHERE bepusu.CODCUENTA = 'candidato1'),
    (SELECT greatest(1, round(dbms_random.value(bepite.VALOR_MINIMO, bepite.VALOR_MAXIMO))) FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = 16),
    'descripción mérito 16',
    'observación mérito 16',
    hextoraw('453d7a34'),
    'CARGA_INICIAL'
);