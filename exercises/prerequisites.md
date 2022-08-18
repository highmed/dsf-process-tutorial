# Prerequisites

In order to be able to solve the exercises described in this tutorial a software development environment with git, Java 11, Maven 3.8, Docker, docker-compose, a Java IDE like Eclipse or IntelliJ, a BPMN Editor like the Camunda Modeller and minimum 16GB of RAM is needed.


## git
[git](https://git-scm.com) is a free and open source distributed version control system designed to handle everything from small to very large projects with speed and efficiency.

- An installation guide for Linux, Mac and Windows can be found here: [installation guide](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
- The most basic git CLI commands are described here: [commands](https://git-scm.com/book/en/v2/Git-Basics-Getting-a-Git-Repository)


## Java 11
Processes for the DSF are written using the [Java](https://www.java.com) programming language in version 11. Various open source releases of the Java Developer Kit (JDK) 11 exist, you are free in your choice.


## Maven 3.8
When implementing DSF processes, we use Maven 3.8 to manage the software project's build, reporting and documentation workflow.

- An installation guide for Maven 3.8 can be found here: [installation guide](https://maven.apache.org/install.html)
- The most important maven commands are described here: [commands](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)

In order to install DSF packages from the GitHub Package Registry using Maven you need a personal GitHub access token. This [GitHub documentation](https://docs.github.com/en/free-pro-team@latest/github/authenticating-to-github/creating-a-personal-access-token) shows you how to generate one.

After that, add the following `server` configuration to your local `.m2/settings.xml`. Replace `USERNAME` with your GitHub username and `TOKEN` with the previously generated personal GitHub access token. The token needs at least the scope `read:packages`.

```
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">

  ...

  <servers>
    <server>
      <id>github</id>
      <username>USERNAME</username>
      <password>TOKEN</password>
    </server>
  </servers>
</settings>
```


## Docker and docker-compose
To be able to test the implemented processes, we use a test-setup based on Docker and docker-compose. This allows us to simulate multiple organizations with different roles and run the processes across "organizational boundaries".

- An installation guide for Docker and docker-compose can be found here: [installation guide](https://docs.docker.com/get-docker/)
- The most important Docker commands are described here: [Docker commands](https://docs.docker.com/engine/reference/run/)
- An overview of docker-compose commands are described here: [docker-compose commands](https://docs.docker.com/compose/reference/)

The following entry is required in the `hosts` file of your computer so that the FHIR servers of the simulated organizations can be accessed in your web browser. On Linux and Mac this file is located under the path `/etc/hosts`. On Windows you can access it under `C:\Windows\System32\drivers\etc\hosts`.

```
127.0.0.1	dic
127.0.0.1	cos
127.0.0.1	hrp
```


## Java IDE
For the development of the processes we recommend the use of an IDE, e.g. Eclipse or IntelliJ:

- An installation guide for Eclipse can be found here: [Eclipse installation guide](https://wiki.eclipse.org/Eclipse/Installation)
- An installation guide for IntelliJ can be found here: [IntelliJ installation guide](https://www.jetbrains.com/help/idea/installation-guide.html)


## BPMN Editor
To simplify modeling of BPMN processes, we recommend a graphical editor, e.g. the Camunda Modeler:

- An installation guide for the Camunda Modeler can be found here: [installation guide](https://camunda.com/de/download/modeler/)


## Hardware
The minimum hardware requirements to run all simulated organizations in Docker test-setup are 16 GB RAM.