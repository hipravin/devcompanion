cd site
call npm run build --production
cd ..
call .\mvnw.cmd clean package
docker-compose down
docker-compose up --build -d
pause;