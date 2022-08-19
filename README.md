# DSF Process Plugin Tutorial
This repository contains exercises to learn how to implement process plugins for the [Data Sharing Framework (DSF)](https://github.com/highmed/highmed-dsf). The tutorial is divided into several exercises that build on each other. For each exercise, a sample solution is provided in a separate branch.

## Prerequisites
In order to be able to solve the exercises described in this tutorial a software development environment with git, Java 11, Maven 3.8, Docker, docker-compose, a Java IDE like Eclipse or IntelliJ, a BPMN Editor like the Camunda Modeller and minimum 16GB of RAM is needed. For more details see the [detailed prerequisites document](exercises/prerequisites.md).

## Exercise 1 - Simple Process
The first exercise focuses on setting up the testing environment used in this tutorial and shows how to implement and execute a simple BPMN process. For more details see the [exercise 1 description](exercises/exercise-1.md).

## Exercise 1.1 - Process Debugging
Exercise 1.1 looks at how to use the Java debugger of your IDE to remote debug the execution of a process plugin. For more details see the [exercise 1.1 description](exercises/exercise-1-1.md).

## Exercise 2 - Input Parameter
In order to dynamically control processes that are packaged as a process plugins, we will take a look at two possibilities on how to pass input parameters to a process. For more details see the [exercise 2 description](exercises/exercise-2.md).

## Exercise 3 - Message Events
Communication between organizations is modeled using message flow in BPMN processes. The third exercise shows how a process at one organization can trigger a process at another organization. For more details see the [exercise 3 description](exercises/exercise-3.md).

## Exercise 4 - Exclusive Gateway
Different sequence flows of a process instance execution based on execution variables can be facilitated using exclusive
gateways. We will examine in exercise four how this can be implemented. For more details see the [exercise 4 description](exercises/exercise-4.md).

## Exercise 5 - Event Gateway and Intermediate Events
In the final exercise we look at message flow between three organizations as well as continuing a process if no return message arrives. For more details see the [exercise 5 description](exercises/exercise-5.md).