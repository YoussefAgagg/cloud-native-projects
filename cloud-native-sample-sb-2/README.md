# Cloud native application with spring boot 2.7.16
to run the application 
```bash
./gradlew bootrun
```
to package your application as a container image, using Cloud Native [Buildpacks](https://buildpacks.io/)
```bash
./gradlew bootBuildImage
```
to run the build image 
```bash
docker run --rm  --platform linux/amd64 --name cloud-native-sample-sb-2 -p 8080:8080 cloud-native-sample-sb-2:0.0.1-SNAPSHOT
```
go to: [http://localhost:8080/](http://localhost:8080/)
you will see the welcome message.

install minikube and set up a Kubernetes cluster in your local development environment.
You can find the installation guide here [minikube ](https://minikube.sigs.k8s.io/docs/start/)

start a local Kubernetes cluster:
```bash
minikube start
```
to deploy it to a cluster using Kubernetes
```bash
minikube image load cloud-native-sample-sb-2:0.0.1-SNAPSHOT
```
```bash
kubectl create deployment cloud-native-sample-sb-2 --image=cloud-native-sample-sb-2:0.0.1-SNAPSHOT
```
verify the creation of the Deployment object as follows:
```bash
kubectl get deployment
```
```bash
kubectl get pod
```
```bash
kubectl logs deployment/cloud-native-sample-sb-2
```
expose the  Service to the cluster:
```bash
kubectl expose deployment cloud-native-sample-sb-2 --name=cloud-native-sample-sb-2 --port=8080
```
verify that it’s been created correctly:
```bash
kubectl get service cloud-native-sample-sb-2
```
forward the traffic from a local port on your computer (for example, 8000) to the port exposed by the Service inside the cluster (8080):
```bash
kubectl port-forward service/cloud-native-sample-sb-2 8000:8080
```
Open a browser window, navigate to http://localhost:8000/ and verify that you still get the same greeting as before.

delete the Service : 
```bash
kubectl delete service cloud-native-sample-sb-2
```
delete the Deployment :
```bash
kubectl delete deployment cloud-native-sample-sb-2
```
you can stop the Kubernetes cluster with 
```bash
minikube stop
```