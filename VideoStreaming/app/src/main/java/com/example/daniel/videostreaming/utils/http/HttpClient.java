package com.example.daniel.videostreaming.utils.http;

import com.example.daniel.videostreaming.utils.http.response.ByteArrayResponse;
import com.example.daniel.videostreaming.utils.http.response.StringResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Esta classe encapsula a biblioteca OkHttp.
 * Ela implementa a Interface HttpRequester.
 * @author Leonardo Pires, Felipe Knop
 */

public class HttpClient implements HttpRequester {

    //TODO: Refatorar com Builder para evitar exceção no construtor

    /**
     * Unidade de medida do tempo de timeout.
     */
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    /**
     * Tempo do timeout. O valor usado aqui é em função da unidade de medida usada.
     */
    private static final int DEFAULT_TIMEOUT = 5;

    /**
     * Instância do cliente HTTP usado.
     */
    private final OkHttpClient httpClient;

    public HttpClient() {
        this.httpClient = buildDefaultClient();
    }

    public HttpClient(InputStream cert) throws HttpClientException {

        OkHttpClient client;

        CertificateFactory cf;
        Certificate ca;
        SSLContext sslContext;
        X509TrustManager trustManager;

        try {
            cf = CertificateFactory.getInstance("X.509");

            ca = cf.generateCertificate(cert);
            cert.close();

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);
            trustManager = (X509TrustManager) tmf.getTrustManagers()[0];

            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException | KeyManagementException e) {
            this.httpClient = buildDefaultClient();
            throw new HttpClientException("Erro ao criar SSLContext para o certificado. Client criado sem SSL.", e.getCause());
        }

        try {
            client = new OkHttpClient.Builder()
                    .connectTimeout(DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT)
                    .readTimeout(DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT)
                    .writeTimeout(DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT)
                    .retryOnConnectionFailure(false)
                    .sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                    .build();
        } catch (NullPointerException e) {
            this.httpClient = buildDefaultClient();
            throw new HttpClientException("Problema ao criar o client com o sslSocketFactory. Client criado sem SSL.", e.getCause());
        }

        this.httpClient = client;
    }

    private OkHttpClient buildDefaultClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT)
                .readTimeout(DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT)
                .writeTimeout(DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT)
                .retryOnConnectionFailure(false)
                .build();
    }

    private Response doRequest(String url) throws ServerProblemException {

        Request request = new Request.Builder().url(url).build();

        try {
            return this.httpClient.newCall(request).execute();
        } catch (IOException ex) {
            throw new ServerProblemException(url, ex);
        }
    }

    /**
     * faz uma requisição do tipo GET em um Servidor Web.
     *
     * @param url Endereço do servidor.
     * @return Objeto de retorno.
     * @throws HttpClientException quando alguma coisa acontece de errado.
     */
    @Override
    public StringResponse doGetRequest(String url) throws HttpClientException {

        Response response = this.doRequest(url);

        try {
            return new StringResponse(url, response.code(), response.body().string());
        } catch (SocketTimeoutException ex) {
            throw new TimeoutException(url, ex);
        } catch (IOException ex) {
            throw new HttpClientException("Erro durante leitura do conteúdo da resposta da requisição.", ex);
        }

    }

    @Override
    public ByteArrayResponse doGetRequestAsByteArray(String url) throws HttpClientException {

        Response response = this.doRequest(url);

        try {
            return new ByteArrayResponse(url, response.code(), response.body().bytes());
        } catch (SocketTimeoutException ex) {
            throw new TimeoutException(url, ex);
        } catch (IOException ex) {
            throw new HttpClientException("Erro durante leitura do conteúdo da resposta da requisição.", ex);
        }
    }

    /**
     * Faz uma requisição do tipo POST em um Servidor Web.
     *
     * @param url         Endereço do servidor.
     * @param contentType Tipo do conteúdo.
     * @param dataToPost  {@link PostData} Conteúdo.
     * @return Objeto de retorno.
     * @throws HttpClientException quando alguma coisa acontece de errado.
     */
    @Override
    public StringResponse doPostRequest(String url, String contentType, PostData dataToPost) throws HttpClientException {
        MediaType mediaType = MediaType.parse(contentType);
        RequestBody body = RequestBody.create(mediaType, dataToPost.getContent());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = this.httpClient.newCall(request).execute();
            return new StringResponse(url, response.code(), response.body().string());
        } catch (UnknownHostException ex) {
            throw new ServerProblemException(url, ex);
        } catch (SocketTimeoutException ex) {
            throw new TimeoutException(url, ex);
        } catch (IOException ex) {
            throw new HttpClientException("Ocorreu um erro ao acessar a URL: " + url, ex);
        }
    }
}
