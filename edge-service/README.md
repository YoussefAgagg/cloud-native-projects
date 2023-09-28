# run the application

* run configuration server todo add link to readme for config server

Next, use [grype](https://github.com/anchore/grype) to check if the newly created image contains any vulnerabilities:

 ```bash
grype edge-service
```

#### note : use [Buildpacks](https://buildpacks.io) to build the image instead of dockerfile

```bash
./gradlew bootBuildImage
```

```bash
docker run -d --name edge-service --net edge-network -p 9001:9001 -e SPRING_DATASOURCE_URL=jdbc:postgresql://bookshop-postgres:5432/bookshop_edge -e SPRING_PROFILES_ACTIVE=testdata edge-service
```

remove containers

```bash
docker rm -f edge-service bookshop-postgres
```

remove network

```bash
 docker network rm edge-network
```

to publish the imge build by Buildpacks use:

```bash
./gradlew bootBuildImage  --imageName ghcr.io/youssefagagg/edge-service   --publishImage  -PregistryUrl=ghcr.io -PregistryUsername=youssefagagg  -PregistryToken=<your_github_token>
```

# 7 Kubernetes fundamentals for Spring Boot

to load an image to k8s

```bash
 minikube image load edge-service --profile bookshop
```

CREATING A DEPLOYMENT OBJECT FROM A MANIFEST

```bash
kubectl apply -f k8s/deployment.yml
```

```bash
 kubectl get all -l app=edge-service
```

```bash
kubectl logs deployment/edge-service
```

CREATING A SERVICE OBJECT FROM A MANIFEST

```bash
kubectl apply -f k8s/service.yml
```

```bash
kubectl get svc -l app=edge-service
```

```bash
kubectl port-forward service/edge-service 9001:80
```

```bash
kubectl apply -f k8s/deployment.yml.
```

```bash
kubectl delete -f k8s
```

# local Kubernetes development workflow

[Tilt](https://tilt.dev)
[install](https://docs.tilt.dev/install.html)
in directory contains Tiltfile run:

```bash
tilt up
```