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