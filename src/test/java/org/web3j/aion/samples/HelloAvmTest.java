package org.web3j.aion.samples;

import org.junit.Assert;
import org.junit.Test;
import org.web3j.aion.VirtualMachine;
import org.web3j.aion.crypto.Ed25519KeyPair;
import org.web3j.aion.protocol.Aion;
import org.web3j.aion.samples.generated.HelloAvm;
import org.web3j.aion.tx.AionTransactionManager;
import org.web3j.aion.tx.gas.AionGasProvider;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.TransactionManager;

public class HelloAvmTest {

    private static String NODE_ENDPOINT = System.getenv("NODE_ENDPOINT");
    private static String PRIVATE_KEY = System.getenv("PRIVATE_KEY");

    private final Aion aion = Aion.build(new HttpService(NODE_ENDPOINT));

    private final TransactionManager manager = new AionTransactionManager(
            aion, new Ed25519KeyPair(PRIVATE_KEY), VirtualMachine.AVM
    );

    @Test
    public void deployAndCallContract() throws Exception {

        // Deploy the contract 
        final HelloAvm helloContract = HelloAvm.deploy(aion, manager, AionGasProvider.INSTANCE).send();

        // Call getString 
        String result = helloContract.call_getString().send();
        Assert.assertEquals("Hello AVM", result);

        // Call setString 
        final long millis = System.currentTimeMillis();
        TransactionReceipt receipt = helloContract.send_setString("Hello AVM at " + millis).send();
        Assert.assertTrue(receipt.isStatusOK());

        result = helloContract.call_getString().send();
        Assert.assertEquals("Hello AVM at " + millis, result);
    }
}
