
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
import java.io.OutputStream;
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


public class RNClientCertificateFetchModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNClientCertificateFetchModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  String TAG = "RNClientCertificateFetchModule";

	private String convertInputStreamToString(InputStream inputStream)
		throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        return result.toString(StandardCharsets.UTF_8.name());
    }


  @ReactMethod
  public void fetchPost(String alias, String address, String basicAuth, String soapAction, String postData, Promise p) {
    String resultData = "";

    try {
      // Tell the URLConnection to use a SocketFactory from our SSLContext
      URL url = new URL(address);
      HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
      Log.d(TAG, "Connection created");
      X509Impl.setForConnection(urlConnection, reactContext, alias);
      urlConnection.setRequestProperty ("Authorization", basicAuth);
      urlConnection.setRequestMethod("POST");
      urlConnection.setRequestProperty("Content-Type", "text/xml");
      urlConnection.setRequestProperty("SOAPAction", soapAction);
      urlConnection.setDoOutput(true);

      try(OutputStream os = urlConnection.getOutputStream()) {
        byte[] input = postData.getBytes();
        os.write(input, 0, input.length);
      }catch (Exception e) {
        Log.e(TAG, "Writing body failed");
        e.printStackTrace();
        p.reject("Writing body: " + e.getMessage());
      }

      InputStream in = urlConnection.getInputStream();
      resultData = convertInputStreamToString(in);

      p.resolve(resultData);
    } catch (Exception e) {
      Log.e(TAG, "Request failed");
      e.printStackTrace();
      p.reject("Request failed: " + e.getMessage());
    }
  }


  @ReactMethod
  public void getCertAlias(Promise promise) {
    final Promise p = promise;
    KeyChain.choosePrivateKeyAlias (reactContext.getCurrentActivity(), new KeyChainAliasCallback(){
      public void alias(String alias) {
        if(alias != null) {
          // Do something with the selected alias
          p.resolve(alias);
        } else {
          // User didn't select an alias, do something
          p.reject("User did not select cert");
        }
      }
    }, null, null, null, -1, null);
  }

  @Override
  public String getName() {
    return "RNClientCertificateFetch";
  }
}