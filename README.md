web3j Aion integration samples
==============================

In this project you will learn to generate the code and call an AVM Aion smart contract with Java.

To call and transact with an AVM contract from Java, you will need to generate its 
[web3j contract wrapper](https://docs.web3j.io/smart_contracts.html#deploying-and-interacting-with-smart-contracts) 
from the contract's Abstract Binary Interface (ABI). If you also want to deploy the contract, you will need the Java 
Archive (JAR) file containing its compiled code.

To learn how to create an AVM smart contract and obtain its ABI and JAR files take a look at the 
[aion4j Maven plugin tutorial](https://docs.aion.network/docs/maven-and-aion4j).

## Quick start

This project contains a [Maven module](hello-world-avm) created using the aion4j archetype.
We will use this module to compile a [HelloAvm](hello-world-avm/src/main/java/org/web3j/aion/HelloAvm.java)
contract and with the obtained ABI and JAR files generate the web3j contract wrapper, deploy it and interact with it 
using a [JUnit test](src/test/java/org/web3j/aion/samples/HelloAvmTest.java).

### Compile the Aion contract

From the project root, change to the `hello-world-avm` directory, initialize and install the project:
```bash
cd hello-world-avm
./mvnw initialize
./mvnw install
```
In the `target` folder you should find the ABI and JAR files used in the following step. 
The ABI file should be a text file containing the contract class name, its constructor and other function definitions:
```
0.0
org.web3j.aion.HelloAvm
Clinit: ()
public static void sayHello()
public static String greet(String)
public static String getString()
public static void setString(String)
```

### Generate the web3j contract wrapper

The [Gradle build file](build.gradle) includes this task for the contract wrapper generation:
```groovy
task generateContractWrappers(type: JavaExec, group: 'aion', dependsOn: 'clean') {
    classpath = sourceSets.main.runtimeClasspath
    main = 'org.web3j.aion.codegen.AionGeneratorMain'

    args '--abiFile', "$projectDir/hello-world-avm/target/hello-world-avm-0.1.0-SNAPSHOT.abi",
            '--binFile', "$projectDir/hello-world-avm/target/hello-world-avm-0.1.0-SNAPSHOT.jar",
            '--outputDir', "$project.buildDir/generated/source/web3j-aion/main/java",
            '--package', 'org.web3j.aion.samples.generated'
}
```
You can see how the output from the previous step is used as an input for this task.
To run it, go back to the samples project directory and run the command:
```bash
cd ..
./gradlew generateContractWrappers
```
After a successful run, the contract wrapper should be in the `build/generated/source/web3j-aion/main/java` directory,
as specified in the `generateContractWrappers` Gradle task.

This task will also be triggered automatically during a project `build` task.
Depending on your JDK version, it might output some warnings about Java modules; you can ignore them.

## Building the project

To interact with the contract using the generated contract wrapper, take a look at the
[`HelloAvmTest`](src/test/java/org/web3j/aion/samples/HelloAvmTest.java) class:
```java
// Deploy the contract 
HelloAvm helloContract = HelloAvm.deploy(aion, manager, AionGasProvider.INSTANCE).send();

// Call getString 
String result = helloContract.call_getString().send();
Assert.assertEquals("Hello AVM", result);
```
It is quite straight forward, but before running it you need to set the proper environment 
variables for the test to run.

You could also load the contract from an existing address using one of the the static load methods:
```java
HelloAvm helloContract = HelloAvm.load(
        "0xa0a7cddb89bd3186142545629efa6cb3401a77160e36f26a86d703e0bd478bc1", 
        aion, manager, AionGasProvider.INSTANCE
).send();
```

### Set the environment variables

Open a console and export the environment properties with `export VAR_NAME=...` 
(use single quotes to avoid potential issues):

   * `NODE_ENDPOINT`: Set a valid Aion node URL from Mastery or a local instance, e.g.:
     ```
     'https://aion.api.nodesmith.io/v1/mastery/jsonrpc?apiKey=7b1d449f07774200bc1000a8b0eb1a9e'
     ```
   * `PRIVATE_KEY`: Set a valid Aion private key from the used network, e.g. from Mastery: 
     ```
     '0x4776895c43f77676cdec51a6c92d2a1bacdf16ddcc6e7e07ab39104b42e1e52608fe2bf5757b8261d4937f13b5815448f2144f9c1409a3fab4c99ca86fff8a36'
     ```

### Run the tests and build

Now you can run the command `./gradlew build`. This will run the project test against your given `NODE_ENDPOINT`,
deploying a `HelloAvm` Java contract and changing its state. 

