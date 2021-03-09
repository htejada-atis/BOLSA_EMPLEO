# solo probar con docker
sudo docker build -t uv-externo:v1 .
sudo docker run -d --name uv-externo-container -p 8888:8080 -e TZ=CST uv-externo:v1
# docker compose
sudo docker-compose up
sudo docker-compose up --build
#solo oracle 
sh "docker run --name oracle -p 1521:1521 -p 5000:5500 -e ORACLE_PWD=pwd123 -e ORACLE_CHARACTERSET=AL32UTF8 -v ${WORKSPACE}/Documentos/docker/init-oracle:/opt/oracle/scripts/startup usig-registrydocker.ujaen.es:5044/uvirtual/oracle/database:18.4.0-xe"

# pruebas jest
npm install --save-dev jest
npm install jest-html-reporter --save-dev

