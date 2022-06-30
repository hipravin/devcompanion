call kubectl delete -f .\devcompanion-ui-service.yml
call kubectl delete -f .\devcompanion-ui-deployment.yml
call kubectl delete -f .\gateway-deployment.yml
call kubectl delete -f .\gateway-service.yml
call kubectl delete -f .\article-service-deployment.yml
call kubectl delete -f .\article-service.yml
call kubectl delete -f .\article-repo-pv-claim.yml
call kubectl delete -f .\article-repo-pv.yml
