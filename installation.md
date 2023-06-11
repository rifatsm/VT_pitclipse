# Pitclipse Installation Pre-requisites

The following three tools are essential to develop Pitclipse. Install them first. 

1. [Java 8 JDK](https://adoptium.net/temurin/releases/)
2. [Maven 3.6.3](https://maven.apache.org/download.cgi) [Installation notes](https://maven.apache.org/install.html)
3. [Eclipse RCP](https://www.eclipse.org/downloads/packages/release/2023-03/r/eclipse-ide-rcp-and-rap-developers)

# Local Machine Configuration
## Java 8 JDK installation
1. Set the JAVA_HOME environment variable to point to the JDK installation directory.

* Select the desired JDK version 
[Follow the steps to select Java 8 JDK](https://stackoverflow.com/questions/21964709/how-to-set-or-change-the-default-java-jdk-version-on-macos)

One thing to keep in mind, to check the current version of Java, use the following commands:

### Java 8:

```
java -version
```

### Java 9+:

```
java --version
```


Update the JAVA_HOME environment variable to point to the JDK installation directory.

Add the following lines to the end of your ~/.zshrc file:

export JAVA_HOME=$(/usr/libexec/java_home -v 1.8.0)

## Maven installation

Here is a even verbose installation guide for Maven: [Maven Installation Guide](https://www.digitalocean.com/community/tutorials/install-maven-mac-os)

* Extract distribution archive in any directory
* Add the bin directory of the created directory apache-maven-3.9.2 to the PATH environment variable
    - Add the following lines to the end of your ~/.zshrc file:

```
export M2_HOME="/Users/ri/Downloads/apache-maven-3.9.2"
PATH="${M2_HOME}/bin:${PATH}"
export PATH
```

## Eclipse RCP installation
* Install from the link above.
* If there are more than one instance of Eclipse installed, it is better to rename this one to Eclipse RCP. 
* You can further name the other Eclipse instances according to their version numbers.