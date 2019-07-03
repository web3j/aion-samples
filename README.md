web3j Aion integration samples
==============================

In this project you will learn to generate the code and call an AVM Aion smart contract with Java.

To call and transact with an AVM contract from Java, you will need to generate its 
[web3j contract wrapper](https://docs.web3j.io/smart_contracts.html#deploying-and-interacting-with-smart-contracts).
This is done with a specific web3j toolset for Aion which requires the contract's 
[Abstract Binary Interface (ABI)](HelloAvm.abi).

If you also want to *deploy* the contract, you will need the [Java Archive (JAR)](HelloAvm.jar) 
file containing its compiled code.

To learn how to create an AVM smart contract and obtain its ABI and JAR files take a look at the 
[aion4j Maven plugin tutorial](https://docs.aion.network/docs/maven-and-aion4j).

In this project we will use the sample [ABI](HelloAvm.abi) and [JAR](HelloAvm.jar) files obtained using the Maven plugin
tutorial to generate the web3j contract wrapper, deploy it and interact with it using a 
[JUnit test](src/test/java/org/web3j/aion/samples/HelloAvmTest.java).

## Generate `HelloAvm` contract wrapper

The [Gradle build file](build.gradle) includes a task for wrapper generation.
To run it, go back to the samples project directory and run the command:

```bash
./gradlew generateContractWrappers
```

This task is bound to the project `build` task, so it'll be triggered automatically during a project build.

Depending on your JDK version, it might output some warnings about Java modules; you can ignore them.


## Build the project

1. Open a console and export the environment properties:

   * `NODE_ENDPOINT`: The Aion node URL, e.g.
     ```
     https://aion.api.nodesmith.io/v1/mastery/jsonrpc?apiKey=7b1d449f07774200bc1000a8b0eb1a9e
     ```
   * `PRIVATE_KEY`: The Aion account private key, e.g. 
     ```
     0x4776895c43f77676cdec51a6c92d2a1bacdf16ddcc6e7e07ab39104b42e1e52608fe2bf5757b8261d4937f13b5815448f2144f9c1409a3fab4c99ca86fff8a36
     ```

2. Run the command `./gradlew build`. This will run the project test against your given `NODE_ENDPOINT`.

