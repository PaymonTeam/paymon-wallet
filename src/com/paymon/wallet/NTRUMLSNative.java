package com.paymon.wallet;


import org.jetbrains.annotations.Nullable;

public class NTRUMLSNative {
    @Nullable
    public static byte[] publicKeyFromPrivate(@Nullable byte[] privateKey) {
        return NTRUMLSNative.generateKeyPairFromFg(NTRUMLSNative.unpackFgFromPrivateKey(privateKey)).publicKey;
    }

    public static class KeyPair {
        byte[] privateKey;
        byte[] publicKey;

        public KeyPair(byte[] privateKey, byte[] publicKey) {
            this.privateKey = privateKey;
            this.publicKey = publicKey;
        }
    }

    native public static KeyPair generateKeyPair();
    native public static short[] unpackFgFromPrivateKey(byte[] sk);
    native public static KeyPair generateKeyPairFromFg(short[] fg);
    native public static byte[] sign(byte[] msg, byte[] sk, byte[] pk);
    native public static boolean verify(byte[] msg, byte[] signature, byte[] pk);

    static {
        System.loadLibrary("libntrumls");
    }
}
