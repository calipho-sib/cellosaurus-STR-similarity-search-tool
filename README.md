Cellosaurus STR Similarity Search Tool
======

Build
------

The project can be built from the [webapp repository](https://github.com/calipho-sib/cellosaurus-STR-similarity-search-tool/tree/master/webapp) as a java WAR file using an IDE. 

Update
------

When a new Cellosaurus version is released, the Tomcat webapp has to be restarted for the additions and changes to be integrated. Note that the tool uses the [Cellosaurus FTP](ftp://ftp.expasy.org/databases/cellosaurus) as source and the corresponding files need to be updated prior to the restart.

To make sure the STR Similarity Search Tool uses the latest Cellosaurus version, the following GET method can be used:
http://dev.glyconnect.org:8080/str-sst/api/database

Help
------

Help can be accessed on the STR Similarity Search Tool [help page](https://dev.glyconnect.org/str-sst/help.html).

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
