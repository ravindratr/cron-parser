# <ins>Service Cron</ins>

### <ins>Responsibility</ins>

- Program accepts a cron expression and a command as arguments. Program parses cron expression and provide details on when this command will be run based on cron expression.

## <ins>Set Up Instructions</ins>

* This project requires below 2 components to be present in the machine to build and run the project.
    * Java 8
    * Maven
    * HomeBrew (Only For MacOS)

### <ins>Java 8 installation Steps</ins>

#### Ubuntu

```
sudo apt-get update
sudo apt-get install openjdk-8-jdk
java -version
```

#### MacOS

Install Homebrew first, if not installed

```
export HOMEBREW_INSTALL_FROM_API=1
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install.sh)"
```

```
brew tap homebrew/cask-versions
brew install --cask adoptopenjdk8
```

### <ins>Maven installation Steps</ins>

#### Ubuntu

```
sudo apt update
sudo apt install maven
mvn -version
```

#### MacOS

```
brew install maven
mvn -version
```

## <ins>Running / Executing the project</ins>

- All commands are run in the root folder of project

#### Running tests and building jar file 
```
mvn clean test package shade:shade
```

#### Executing the jar file with parameters

- Program takes 2 parameters
     1. Cron expression with 5 parameters separated by space (minute, hour, day of month, month, day of week)
     2. Command to be run

```
java -jar target/service-cron-1.0-shaded.jar "2 2-6 1-4 * *" "usr/execute"
```

#### Sample output

```
minute        2
hour          2 3 4 5 6
day of month  1 2 3 4
month         1 2 3 4 5 6 7 8 9 10 11 12
day of week   1 2 3 4 5 6 7
command       usr/execute
```

#### Supported Cron Value Types

```
 SPECIFIC_VALUE ( Ex: 5 ) 
 RANGE  ( Ex: 2-6 ) start < end
 INTERVAL ( Ex: */10 )
 INTERVAL_WITH_RANGE ( Ex: 2-6/2 ) start < end
 ALL ( Ex: * )
```