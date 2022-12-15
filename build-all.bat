cd site
call npm run build --production
cd ..
call .\mvnw.cmd clean package
cd repo-service
call docker build -t hipravin/devcompanion-repo-service .
cd ..\article-service
call docker build -t hipravin/devcompanion-article-service .
cd ..\gateway
call docker build -t hipravin/devcompanion-gateway .
cd ..\site
call docker build -t hipravin/devcompanion-ui .
cd ..\admin
call docker build -t hipravin/devcompanion-admin .
cd ..
echo BUILD ALL COMPLETED
pause;