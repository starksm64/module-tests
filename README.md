# TAG JPMS Recommendations and Tests

This repository contains recommendations and tests for the Java Platform Module System (JPMS).
for JBoss middleware projects.

## JPMS Overview

## Classpath vs Modulepath
Jars and directories on the classpath(--class-path -classpath -cp) are treated as a single, monolithic collection of classes in an unnamed module. The modulepath (--module-path -p) specifies directoires that contain exploded module roots and/or modular jars or jmod (compile time) files.

  
### Module descriptor keywords
- requires : specifies a dependency on another module
- requires static : specifies a dependency on another module at compile time, but optional at runtime (optional dependency)
- requires transitive : specifies a dependency on another module and makes it available to other modules that depend on the current module (transitive dependency)
- exports : makes a package public and protected types available to other modules
- exports X to A,B,...: makes a package public and protected types of package X available to specific modules A,B,... (qualified export)
- uses : specifies a service used by the current module. It is an interface or abstract class that some other module must define a provides directive for.
- provides X with Y : specifies a service implementation provided by the current module. X is an interface or extends an abstract class defined the service contract, and Y specifies the name of the service provider class that implements the interface or extends the abstract class.
- opens : makes a package accessible to reflection at runtime
- opens X to A,B,...: makes package X accessible to reflection at runtime to specific modules A,B,... (qualified open)
- open module M ...: opens all packages in module M to reflection at runtime

## Tools
### jdeps
The jdeps tool analyzes class files and JAR files to determine package-level or class-level dependencies. It can generate a module descriptor for a JAR file.
### jmod
The jmod tool creates JMOD files, which are a packaging format for the Java runtime system. A JMOD file is a compressed file that contains a set of directories and files. It can contain class files, native libraries, configuration files, and other resources.
### jlink
The jlink tool links a set of modules and their dependencies to create a custom runtime image. The custom runtime image contains only the modules that are required to run the application. The custom runtime image can be smaller than the full JDK distribution.
### DML module-info
https://github.com/dmlloyd/module-info
### Layrry and Moditect
https://github.com/moditect/layrry
https://github.com/moditect/moditect

## Unsafe
The `sun.misc.Unsafe` class is a part of the Java Platform API, but it is not part of the public API. It is used by the Java runtime system and some libraries to perform low-level operations. The `sun.misc.Unsafe` class is not guaranteed to be present in all Java implementations, and it is not recommended to use it in application code.

## Test Modules
- auto
- basic
- open