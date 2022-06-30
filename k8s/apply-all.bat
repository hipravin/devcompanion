call kubectl apply -f .\article-repo-pv.yml
call kubectl apply -f .\article-repo-pv-claim.yml
call kubectl apply -f .\article-service.yml
call kubectl apply -f .\article-service-deployment.yml
call kubectl apply -f .\gateway-deployment.yml
call kubectl apply -f .\gateway-service.yml
call kubectl apply -f .\devcompanion-ui-service.yml
call kubectl apply -f .\devcompanion-ui-deployment.yml

