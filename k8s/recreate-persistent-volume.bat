call kubectl delete -f .\article-repo-pv-claim.yml
call kubectl delete -f .\article-repo-pv.yml
call kubectl apply -f .\article-repo-pv.yml
call kubectl apply -f .\article-repo-pv-claim.yml
