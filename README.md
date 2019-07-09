CLASTR: the Cellosaurus STR Similarity Search Tool
======

Build
------

The project can be built as a WAR file from the [webapp repository](https://github.com/calipho-sib/cellosaurus-STR-similarity-search-tool/tree/master/webapp) using the following Maven command:
```shell
mvn compile war:war
```
Note that the backend has first to be installed from the [backend repository](https://github.com/calipho-sib/cellosaurus-STR-similarity-search-tool/tree/master/backend) using the following Maven command:
```shell
mvn install
```
Deployment
------

Place the built WAR file in the `tomcat/webapps` directory.

Update
------

When a new Cellosaurus version is released, the Tomcat webapp has to be restarted for the additions and changes to be integrated. Note that the tool uses the [Cellosaurus FTP](ftp://ftp.expasy.org/databases/cellosaurus) as source and the corresponding files need to be updated prior to the restart.

To make sure that CLASTR uses the latest Cellosaurus version, the [database GET method](https://web.expasy.org/cellosaurus-str-search/api/database) can be used.

Help
------

Help can be accessed on the CLASTR [Help page](https://web.expasy.org/cellosaurus-str-search/help.html).

Repository Details
------

### backend

Java backend handling the similarity search and score computation.

### frontend

HTML/CSS/Javascript frontend handling the tool webpage.

### scripts

Python scripts showcasing examples of using the API.

### webapp

Java webapp handling the RESTful API and processing queries.
