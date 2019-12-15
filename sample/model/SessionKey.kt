package com.smackjeeves.model

import com.smackjeeves.model.base.BaseResponse
import com.smackjeeves.model.base.Result
import com.smackjeeves.network.Api
import com.smackjeeves.network.body.LoginBody
import com.smackjeeves.network.send
import com.smackjeeves.utils.HexUtils
import skizo.library.extensions.trace
import java.math.BigInteger
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.RSAPublicKeySpec
import javax.crypto.Cipher

data class SessionKey(override var result: Result, var data: SessionKeyData): BaseResponse() {
    override fun sync() {
        trace("##BaseApi## onResponse : SessionKey")

        var encryptedData = encrypted("skizo80", "skizo80", data.sessionKey, data.publicKeyModulus, data.publicKeyExponent)

        Api.service.login(LoginBody(data.sessionKey, encryptedData)).send()
    }

    fun encrypted(id:String, pw:String, sessionKey:String, publicKeyModulus:String, publicKeyExponent:String): String {

        var modulus = BigInteger(publicKeyModulus, 16)
        var exponent = BigInteger(publicKeyExponent, 16)
        var publicKey = KeyFactory.getInstance("RSA").generatePublic(RSAPublicKeySpec(modulus, exponent)) as RSAPublicKey
        var targetString: String = StringBuffer()
            .append(Character.toChars(sessionKey.length))
            .append(sessionKey)
            .append(Character.toChars(id.length))
            .append(id)
            .append(Character.toChars(pw.length))
            .append(pw)
            .toString()


//        var cipher = Cipher.getInstance("RSA")
//        var cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        var cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        var encryptedBytes = cipher.doFinal(targetString.toByteArray())


        return HexUtils.toHexString(encryptedBytes)
    }



    private fun fromCharCode(vararg codePoints: Int): String {
        val builder = StringBuilder(codePoints.size)
        for (codePoint in codePoints) {
            builder.append(Character.toChars(codePoint))
        }

        return builder.toString()
    }


}
data class SessionKeyData(var sessionKey: String,
                     var publicKeyModulus: String,
                     var publicKeyExponent: String)