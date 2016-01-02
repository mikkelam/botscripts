# botscripts
- Bot scripts for https://topbot.org
- Documentation of topbot api can be found at https://topbot.org/doc/

## Workflow
Create package, in said package, add a mainhandler which handles the main flow of the bot. 


## Development Setup
This project can be built with Gradle. It includes a runnable version in the repository itself. If using IntelliJ IDEA, run

```sh
$ ./gradlew idea
```

from the root directory to generate all the files needed by IDEA to recognize imported libs (topbotclient.jar) and the Gradle build system itself, allowing for easy running of Gradle tasks from the IntelliJ IDE. Alternatively, run

```sh
$ ./gradlew build
```

to build the entire project. 
