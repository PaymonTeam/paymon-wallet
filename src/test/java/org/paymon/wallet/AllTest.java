package org.paymon.wallet;

import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

public class AllTest {
    @Test
    public void secp256k1() {
        var secp = new Secp256k1();

        var pair = secp.generateKeypair();
        var first = (ECPrivateKey) pair.getFirst();
        var second = (ECPublicKey) pair.getSecond();

        byte[] d = first.getD().toByteArray();
        byte[] q = second.getQ().getEncoded(false);
        var sk = (ECPrivateKey) Secp256k1.Companion.generateSecretKeyFromBytes(d);
        var pk = (ECPublicKey) Secp256k1.Companion.generatePublicKeyFromSecretKey(sk);
        var pk2 = (ECPublicKey) Secp256k1.Companion.generatePublicKeyFromBytes(q);

        assert sk.equals(first);
        assert pk.equals(second);
        assert pk2.equals(second);
    }

    @Test
    public void addressFromSecretKey() {
        var sk_bytes = Hex.decode("79dc4b29fa50a1f4bdaf04031c2608975a68079e9eae424b6a6d13a43347f266");
        var sk = (ECPrivateKey) Secp256k1.Companion.generateSecretKeyFromBytes(sk_bytes);
        var pk = (ECPublicKey) Secp256k1.Companion.generatePublicKeyFromSecretKey(sk);
        String x = new String(Hex.encode(pk.getQ().getXCoord().toBigInteger().toByteArray()));
        assert x.equals("64aa547ad3d3d8f3b627f4a713b1ce5be99aa55c96581e6cbb5df3a889b06fab");
        var address = Address.Companion.fromPublicKey(pk);
        assert address.toString().equals("PAD2D1B0C7434D03B0C4464AEBC38546155B8B0FD61");
    }
}