# solo probar con docker
sudo docker build -t uv-externo:v1 .
sudo docker run -d --name uv-externo-container -p 8888:8080 -e TZ=CST uv-externo:v1
# docker compose
sudo docker-compose up
sudo docker-compose up --build

# pruebas jest
npm install --save-dev jest
npm install jest-html-reporter --save-dev