SETLOCAL

set version="0.0.1-SNAPSHOT"
call echo building with version=%version%

call npm run --prefix site build --production

call mvn clean package

call docker build -t hipravin/devcompanion-repo-service:%version% repo-service
call docker build -t hipravin/devcompanion-article-service:%version% article-service
call docker build -t hipravin/devcompanion-gateway:%version% gateway
call docker build -t hipravin/devcompanion-ui:%version% site
call docker build -t hipravin/devcompanion-admin:%version% admin

ENDLOCAL

echo BUILD ALL COMPLETED
pause;