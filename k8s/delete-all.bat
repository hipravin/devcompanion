call kubectl delete -f .\admin\admin-service.yml
call kubectl delete -f .\admin\admin-deployment.yml
call kubectl delete -f .\devcompanion-ui-service.yml
call kubectl delete -f .\devcompanion-ui-deployment.yml
call kubectl delete -f .\gateway-deployment.yml
call kubectl delete -f .\gateway-service.yml
call kubectl delete -f .\repo-service.yml
call kubectl delete -f .\repo-service-deployment.yml
call kubectl delete -f .\article\article-service-deployment.yml
call kubectl delete -f .\article\article-service.yml
call kubectl delete -f .\article\article-repo-pv-claim.yml
call kubectl delete -f .\article\article-repo-pv.yml
call kubectl delete -f .\secrets\boot-admin-secret.yml
call kubectl delete -f .\secrets\repo-db-secret.yml
call kubectl delete -f .\ingress\ingress.yml
call kubectl delete -f .\ingress\devcompanion-tls.yml

