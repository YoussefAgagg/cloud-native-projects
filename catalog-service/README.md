# run the application 
* run configuration server todo add link to readme for config server
database:
```bash
docker run -d --name bookshop-postgres -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password  -e POSTGRES_DB=bookshop_catalog -p 5432:5432 postgres:14.4
```
stop the container with
```bash
docker stop book-shop-postgres 
```
stop the container with
```bash
docker stop book-shop-postgres 
```
remove the container with
```bash
docker rm  book-shop-postgres 
```