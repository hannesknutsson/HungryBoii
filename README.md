<!--
This README is viciously stolen from https://gist.github.com/PurpleBooth/109311bb0361f32d87a2
-->

# HungryBoii

A simple but beautiful API for your Discord and Slack server made to retrieve daily lunch alternatives at local restaurants.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

1. Using Git, clone the repository to your local machine
2. Import the project into your favourite IDE using the pom.xml file
3. Build the project by running ```mvn clean install``` in the project's base directory
4. In your IDE, set up a profile for running the bot in your development environment by specifying the following properties

### API
```
Main class:         com.github.hannesknutsson.hungryboii.api.ApiApplication
Environment varibles:  TESSDATA_PREFIX=tessdata
Working directory:  <wherever you placed the project>/HungryBoii/Api
Module classpath:   Api
```

### Discord bot
```
Main class:         com.github.hannesknutsson.hungryboii.discord.Application
Program arguments:  -t <your Discord bot API token>
Working directory:  <wherever you placed the project>/HungryBoii/Discord/target/HungryBoii-distributable
Module classpath:   Discord
```

After this, you should be ready to run and develop the bot in your IDE using the profile you created.

### Prerequisites

The software that is required to develop and deploy the project will be specified below. This will be split into two sections, one for development and one for deployment.

Depending on your environment and whether you will make use of optical character recognition you **may require** the Tesseract package for the project to use. I recommend waiting with installing this package until you are certain you need it. This applies to both development and deployment environments. For installation instructions see [the Tesseract project](https://github.com/tesseract-ocr/tesseract/wiki).

#### Development

To develop this project you **will require** the following software:

* Your favourite Java IDE
* Java 17 JDK
* Maven (most recent versions should work, 3.6.1 is verified to work)


#### Deployment

To deploy this project you **will require** the following software:

* Java 17 JRE

OR

* A Docker environment

### Installation/Deployment (Discord bot)

There are some different ways of getting this project spinning on your machine.

1. Clone and build the project, then run the result using Java
2. Pull the Docker image from DockerHub and start it from there
3. Build your own custom Docker image by customizing the project's Dockerfile

#### 1. Manual build and run using Java

Using Git, clone the repository.

```
git clone https://github.com/hannesknutsson/HungryBoii.git
```

Enter the newly cloned project repository.

```
cd HungryBoii/Discord
```

Build the project from its sources.

```
mvn clean install
```

Extract the directory ```HungryBoii/Discord/target/HungryBoii-distributable``` onto whatever machine you wish to install the project (or just do all these steps on the machine you wish to install to from the beginning).

On the machine you wish to install to, enter the HungryBoii-distributable directory and run the project.

```
cd Discord-distributable
java -jar HungryBoii-<whatever version we're at>.jar -t <your Discord bot API token>
```

Your discord bot should now be ready to receive commands.

In a Discord text chat where the bot has permissions to read messages, send messages and add reactions, send a message/command to test the bot:

```
!help
```

The bot should by now have replied to your command with a list of commands it responds to. Your bot is now installed and ready for use!
 
#### 2. Running Docker image from DockerHub

In an environment with Docker installed, run the following commands.

```
mkdir Discord
docker run --name HungryBoii -e TZ=Europe/Stockholm -d -v Discord/logs/:/Discord/logs/ -v Discord/conf/:/Discord/conf/ hannesknutsson/hungryboii:latest -t <your Discord bot API token>
```

These simple commands will pull the latest Docker image from DockerHub and run it as a daemon for you.

If you want to run a more stable version of the bot, swap out ```hannesknutsson/hungryboii:latest``` to ```hannesknutsson/hungryboii:release-1.0```.

Change the time zone parameter to fit the time zone where it will be used. This enables the subscription feature to be in sync with its users.
<!--

Here starts the comment of eternal shame.
If anyone wants to criticise me for not having done this part of the project, I welcome you to do your part!

## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

-->

## Built With

* [Java](https://www.java.com/) - Programming language used
* [Maven](https://maven.apache.org/) - Dependency Management
* [JDA](https://github.com/DV8FromTheWorld/JDA) - Discord API wrapper

<!--

More commenting out for the peeky people who might be inspecting sources

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

-->

## Authors

[Hannes Knutsson](https://www.github.com/hannesknutsson)  
Team Nature@Fortnox

## Acknowledgments

* Tip of the feodora to anyone whose code was used through dependencies or just straight up copy+paste solutions from stackoverflow.
