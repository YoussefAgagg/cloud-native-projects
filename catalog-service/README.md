# run the application 
* run configuration server todo add link to readme for config server
database:
```bash
docker run -d --name bookshop-postgres -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password  -e POSTGRES_DB=bookshopdb_catalog -p 5432:5432 postgres:14.4
```
stop the container with
```bash
docker stop bookshop-postgres 
```
remove the container with
```bash
docker rm  bookshop-postgres 
```
# 6 Containerizing the application
Docker has a built-in DNS server that can enable containers in the same network to find each other using the container name rather than a hostname or an IP address. For example, Catalog Service will be able to call the PostgreSQL server through the URL jdbc:postgresql://polar-postgres:5432, where polar-postgres is the container name.
```bash
docker network create catalog-network
```
```bash
docker run -d --name bookshop-postgres  --net catalog-network -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password  -e POSTGRES_DB=bookshop_catalog -p 5432:5432 postgres:14.4
```
Using the updated Dockerfile, build a new container image for Catalog Service. Open a Terminal window, navigate to the Catalog Service root folder, and run this command (don’t forget the final dot):

```bash
docker build -t catalog-service .
```
Next, use [grype](https://github.com/anchore/grype) to check if the newly created image contains any vulnerabilities:

 ```bash
grype catalog-service
```
#### note : use [Buildpacks](https://buildpacks.io) to build the image instead of dockerfile
```bash
./gradlew bootBuildImage
```
```bash
docker run -d --name catalog-service --net catalog-network -p 9001:9001 -e SPRING_DATASOURCE_URL=jdbc:postgresql://bookshop-postgres:5432/bookshop_catalog -e SPRING_PROFILES_ACTIVE=testdata catalog-service
```
remove containers 
```bash
docker rm -f catalog-service bookshop-postgres
```
remove network
```bash
 docker network rm catalog-network
```
to publish the imge build by Buildpacks use:
```bash
./gradlew bootBuildImage  --imageName ghcr.io/youssefagagg/catalog-service   --publishImage  -PregistryUrl=ghcr.io -PregistryUsername=youssefagagg  -PregistryToken=<your_github_token>
```
# 7 Kubernetes fundamentals for Spring Boot
to load an image to k8s
```bash
 minikube image load catalog-service --profile bookshop
```
CREATING A DEPLOYMENT OBJECT FROM A MANIFEST
```bash
kubectl apply -f k8s/deployment.yml
```
```bash
 kubectl get all -l app=catalog-service
```

```bash
kubectl logs deployment/catalog-service
```
CREATING A SERVICE OBJECT FROM A MANIFEST
```bash
kubectl apply -f k8s/service.yml
```
```bash
kubectl get svc -l app=catalog-service
```
```bash
kubectl port-forward service/catalog-service 9001:80
```
```bash
kubectl apply -f k8s/deployment.yml.
```
```bash
kubectl delete -f k8s
```
#  local Kubernetes development workflow
[Tilt](https://tilt.dev)
[install](https://docs.tilt.dev/install.html)
in  directory contains Tiltfile run:
```bash
tilt up
```