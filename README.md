Job Hunter
===================

A simple desktop application to aid in tracking the status of submitted resumes to tech companies and recruiters.  This project was originally forked from scalafx/scalafx-hello-world in order to make use of the configurations in the build.sbt file, the plugins.sbt file and the project and target folders.

Content
-------

* `src/main/scala/jobHunter.scala` - ScalaFX application.
* `build.sbt` - the main SBT configuration file.
* `project/build.properties` - version of SBT to use.
* `project/plugins.sbt` - plugins used for creation of IDEA and Eclipse projects.



How to build and Run
--------------------

1. Install [Java 8 JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) update 60 or newer.

2. Install [SBT](http://www.scala-sbt.org/)

3. Run the example: change to directory containing this example and use SBT to
   build and run the example:

   ```
    %> sbt run
   ```

   It will download needed dependencies, including Scala and ScalaFX, and run 
   the example code. 


Import into IDEA or NetBeans
----------------------------

IntelliJ IDEA and NetBeans with Scala plugins can directly import SBT projects. 


Create project for Eclipse
-------------------------

If you want to create project that can be used with Eclipse, inside
this project directory, at command prompt type:

    %> sbt eclipse
