package org.example.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class PemKeyService {

  @Value("classpath:private_key.pem")
  private Resource privateKeyPemFile;

  @Value("classpath:public_key.pem")
  private Resource publicKeyPemFile;

  private PrivateKey privateKey;
  private PublicKey publicKey;

  public PrivateKey getPrivateKey() {
    if (privateKey != null) {
      return privateKey;
    }

    try {
      InputStream inputStream = privateKeyPemFile.getInputStream();
      if (inputStream == null) {
        throw new RuntimeException("Cannot find key: ");
      }

      String pemFile = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);


      String privateKeyPEM = pemFile
          .replace("-----BEGIN PRIVATE KEY-----", "")
          .replace("-----END PRIVATE KEY-----", "")
          .replaceAll("\\s", "");

      byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
      privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

      return privateKey;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  public PublicKey getPublicKey()  {
    if (publicKey != null) {
      return publicKey;
    }

    try {
      InputStream inputStream = publicKeyPemFile.getInputStream();
      if (inputStream == null) {
        throw new RuntimeException("Cannot find key: ");
      }

      String pemFile = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

      String publicKeyPEM = pemFile
          .replace("-----BEGIN PUBLIC KEY-----", "")
          .replace("-----END PUBLIC KEY-----", "")
          .replaceAll("\\s", "");

      // Decode and create PublicKey
      byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
      X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
      publicKey = KeyFactory.getInstance("RSA").generatePublic(spec);

      return publicKey;

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
