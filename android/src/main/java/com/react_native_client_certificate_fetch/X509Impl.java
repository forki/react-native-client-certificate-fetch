package com.react_native_client_certificate_fetch;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import javax.net.ssl.HttpsURLConnection;
import android.util.Log;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import java.security.KeyStore;
import android.security.KeyChain;
import android.security.KeyChainException;
import android.security.KeyChainAliasCallback;

import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509KeyManager;

import android.content.Context;
import android.security.KeyChain;
import android.security.KeyChainException;

/***
 * Gets the credentials associated with an alias and presents them so that they can be used in an ssl context
 * Copied from https://medium.com/@_kbremner/android-and-client-authentication-fd416af19a04
 */
public class X509Impl implements X509KeyManager {
	private final String alias;
	private final X509Certificate[] certChain;
	private final PrivateKey privateKey;

    // This can be any protocol supported by your target devices.
    // For example "TLSv1.2" is supported by the latest versions of Android
    final static String SSL_PROTOCOL = "TLSv1.2";


	public static SSLContext setForConnection(HttpsURLConnection con, Context context, String alias) throws CertificateException, KeyManagementException {
		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance(SSL_PROTOCOL);
		} catch(NoSuchAlgorithmException e) {
			throw new RuntimeException("Should not happen...", e);
		}

		sslContext.init(new KeyManager[] { fromAlias(context, alias)}, null, null);
		con.setSSLSocketFactory(TLSSocketFactory(sslContext.getSocketFactory()));
		return sslContext;
	}

	public static X509Impl fromAlias(Context context, String alias) throws CertificateException {
		X509Certificate[] certChain;
		PrivateKey privateKey;
		try {
			certChain = KeyChain.getCertificateChain(context, alias);
			privateKey = KeyChain.getPrivateKey(context, alias);
		} catch (KeyChainException e) {
			throw new CertificateException(e);
		} catch (InterruptedException e) {
			throw new CertificateException(e);
		}
		if(certChain == null || privateKey == null){
			throw new CertificateException("Can't access certificate from keystore");
		}

		return new X509Impl(alias, certChain, privateKey);
	}

	public X509Impl(String alias, X509Certificate[] certChain, PrivateKey privateKey) throws CertificateException {
		this.alias = alias;
		this.certChain = certChain;
		this.privateKey = privateKey;
	}

	@Override
	public String chooseClientAlias(String[] arg0, Principal[] arg1, Socket arg2) {
		return alias;
	}

	@Override
	public X509Certificate[] getCertificateChain(String alias) {
		if(this.alias.equals(alias)) return certChain;
		return null;
	}

	@Override
	public PrivateKey getPrivateKey(String alias) {
		if(this.alias.equals(alias)) return privateKey;
		return null;
	}


    // Methods unused (for client SSLSocket callbacks)
    @Override public final String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
        throw new UnsupportedOperationException();
    }

    @Override public final String[] getClientAliases(String keyType, Principal[] issuers) {
        throw new UnsupportedOperationException();
    }

    @Override public final String[] getServerAliases(String keyType, Principal[] issuers) {
        throw new UnsupportedOperationException();
    }
}