call kubectl delete -f .\admin-service.yml
call kubectl delete -f .\admin-deployment.yml
call kubectl delete -f .\devcompanion-ui-service.yml
call kubectl delete -f .\devcompanion-ui-deployment.yml
call kubectl delete -f .\gateway-deployment.yml
call kubectl delete -f .\gateway-service.yml
call kubectl delete -f .\article-service-deployment.yml
call kubectl delete -f .\article-service.yml
call kubectl delete -f .\article-repo-pv-claim.yml
call kubectl delete -f .\article-repo-pv.yml
call kubectl delete -f .\boot-admin-secret.yml
call kubectl delete -f .\ingress.yml
call kubectl delete -f .\cert\devcompanion-tls.yml

