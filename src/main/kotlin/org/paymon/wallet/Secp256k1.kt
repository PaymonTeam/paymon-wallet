package org.paymon.wallet

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.spec.ECGenParameterSpec
import java.security.KeyPairGenerator
import java.security.Security
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.spec.ECNamedCurveSpec
import java.math.BigInteger
import java.security.KeyFactory
import java.security.spec.ECPrivateKeySpec
import org.bouncycastle.jce.ECPointUtil
import org.bouncycastle.jce.interfaces.ECPrivateKey
import org.bouncycastle.jce.interfaces.ECPublicKey
//import java.security.Signature
import java.security.spec.ECPublicKeySpec
import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.crypto.signers.HMacDSAKCalculator
import org.bouncycastle.crypto.signers.ECDSASigner
import org.bouncycastle.crypto.params.ECPrivateKeyParameters
import org.bouncycastle.asn1.sec.SECNamedCurves
import org.bouncycastle.crypto.params.ECDomainParameters
import org.bouncycastle.util.encoders.Hex
import org.bouncycastle.crypto.params.ECPublicKeyParameters
import java.lang.RuntimeException

typealias SecretKey = ECPrivateKey
typealias PublicKey = ECPublicKey

class Signature(val r: BigInteger, val s: BigInteger) {
    companion object {
        fun fromByteArray(bytes: ByteArray) : Signature {
            throw RuntimeException("unimplemented")
        }
    }
}

class Secp256k1 : Signable<PublicKey, SecretKey, Signature> {
    companion object {
        fun generateSecretKeyFromBytes(bytes: ByteArray): SecretKey {
            val spec = ECNamedCurveTable.getParameterSpec("secp256k1")
            val kf = KeyFactory.getInstance("ECDSA", BouncyCastleProvider())
            val params = ECNamedCurveSpec("secp256k1", spec.curve, spec.g, spec.n)
            val privKeySpec = ECPrivateKeySpec(BigInteger(bytes), params)
            return kf.generatePrivate(privKeySpec) as ECPrivateKey
        }

        fun generatePublicKeyFromBytes(bytes: ByteArray): PublicKey {
            val spec = ECNamedCurveTable.getParameterSpec("secp256k1")
            val kf = KeyFactory.getInstance("ECDSA", BouncyCastleProvider())
            val params = ECNamedCurveSpec("secp256k1", spec.curve, spec.g, spec.n)
            val point = ECPointUtil.decodePoint(params.curve, bytes)
            val pubKeySpec = ECPublicKeySpec(point, params)
            return kf.generatePublic(pubKeySpec) as ECPublicKey
        }

        fun generatePublicKeyFromSecretKey(sk: SecretKey): PublicKey {
            val spec = ECNamedCurveTable.getParameterSpec("secp256k1")
            val kf = KeyFactory.getInstance("ECDSA", BouncyCastleProvider())
            val params = ECNamedCurveSpec("secp256k1", spec.curve, spec.g, spec.n)
            val q = spec.g.multiply(sk.d)
            val pubKeySpec = org.bouncycastle.jce.spec.ECPublicKeySpec(q, spec)
            return kf.generatePublic(pubKeySpec) as ECPublicKey
        }
    }

    override fun generateKeypair(): Pair<SecretKey, PublicKey> {
        Security.addProvider(BouncyCastleProvider())
        var keygen = KeyPairGenerator.getInstance("ECDSA", "BC")
        keygen.initialize(ECGenParameterSpec("secp256k1"))
        val pair = keygen.genKeyPair()
        (pair.private as SecretKey).d.toByteArray()
        return Pair(pair.private as ECPrivateKey, pair.public as ECPublicKey)
    }

    override fun sign(privateKey: SecretKey, message: ByteArray): Signature {
        val publicKey = Secp256k1.generatePublicKeyFromSecretKey(privateKey)

        val signer = ECDSASigner(HMacDSAKCalculator(SHA256Digest()))
//        val privKey = ECPrivateKeyParameters(keyPair.secretKey().bytes().toUnsignedBigInteger(), Parameters.CURVE)
        val spec = ECNamedCurveTable.getParameterSpec("secp256k1")
        val params = SECNamedCurves.getByName("secp256k1")
        val kf = KeyFactory.getInstance("ECDSA", BouncyCastleProvider())
        val curveSpec = ECNamedCurveSpec("secp256k1", spec.curve, spec.g, spec.n)
        val curve = ECDomainParameters(params.curve, params.g, params.n, params.h)
        val privKey = ECPrivateKeyParameters(privateKey.d.toBigDecimal().toBigInteger(), curve)
        signer.init(true, privKey)
        val components = signer.generateSignature(message)
        val r = components[0]
        var s = components[1]
        val bytes = ByteArray(64)
        val CURVE_ORDER = curve.n
        val HALF_CURVE_ORDER = CURVE_ORDER.shiftRight(1)
        println("r=${Hex.toHexString(r.toByteArray())}")
        println("s=${Hex.toHexString(s.toByteArray())}")
//        if (s.compareTo(HALF_CURVE_ORDER) > 0) {
//            s = CURVE_ORDER.subtract(s)
//        }
//
//        var recId = -1
//        val publicKeyBI = publicKey.
//        for (i in 0..1) {
//            val k = recoverFromSignature(i, r, s, hash)
//            if (k != null && k == publicKeyBI) {
//                recId = i
//                break
//            }
//        }
//        if (recId == -1) {
//            // this should never happen
//            throw RuntimeException("Unexpected error - could not construct a recoverable key.")
//        }
//
//        val v = recId.toByte()

//        bytes[0..31] =
        System.arraycopy(r.toByteArray(), 0, bytes, 0, 32)
        System.arraycopy(s.toByteArray(), 0, bytes, 32, 32)

//        val signature = Signature.getInstance("SHA256withECDSA", BouncyCastleProvider())
//        signature.initSign(privateKey)
//        signature.update(message)
//        return signature.sign()
        return Signature(r, s)
    }

    override fun verify(publicKey: PublicKey, signature: Signature, message: ByteArray): Boolean {

//        val signature = Signature.getInstance("SHA256withECDSA", BouncyCastleProvider())
//        signature.initVerify(publicKey)
//        signature.update(message)
//        return signature.verify(signature_bytes)

        val secp_params = SECNamedCurves.getByName("secp256k1")
        val curve = ECDomainParameters(secp_params.curve, secp_params.g, secp_params.n, secp_params.h)
        val signer = ECDSASigner()
        val x = publicKey.q.xCoord.encoded
        val y = publicKey.q.yCoord.encoded
        println(Hex.toHexString(x))
        println(Hex.toHexString(y))
        val toDecode = ByteArray(1) { 4 } + x + y
        println(Hex.toHexString(toDecode))
        val params = ECPublicKeyParameters(curve.curve.decodePoint(toDecode), curve)
        signer.init(false, params)

        try {
            return signer.verifySignature(message, signature.r, signature.s)
        } catch (e: NullPointerException) {
            // Bouncy Castle contains a bug that can cause NPEs given specially crafted signatures. Those signatures
            // are inherently invalid/attack sigs so we just fail them here rather than crash the thread.
        }
        return false
    }
}