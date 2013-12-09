/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2013-11-22 19:59:01 UTC)
 * on 2013-12-09 at 01:36:37 UTC 
 * Modify at your own risk.
 */

package com.appspot.mensa_furtwangen.thumbs;

/**
 * Service definition for Thumbs (v1).
 *
 * <p>
 * Thumbs API for Mensa Furtwangen.
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link ThumbsRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class Thumbs extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.16.0-rc of the thumbs library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
  }

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://mensa-furtwangen.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "thumbs/v1/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   */
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Constructor.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport HTTP transport, which should normally be:
   *        <ul>
   *        <li>Google App Engine:
   *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
   *        <li>Android: {@code newCompatibleTransport} from
   *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
   *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
   *        </li>
   *        </ul>
   * @param jsonFactory JSON factory, which may be:
   *        <ul>
   *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
   *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
   *        <li>Android Honeycomb or higher:
   *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
   *        </ul>
   * @param httpRequestInitializer HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public Thumbs(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  Thumbs(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * Create a request for the method "fetchThumbs".
   *
   * This request holds the parameters needed by the the thumbs server.  After setting any optional
   * parameters, call the {@link FetchThumbs#execute()} method to invoke the remote operation.
   *
   * @param content the {@link com.appspot.mensa_furtwangen.thumbs.model.ThumbsQuery}
   * @return the request
   */
  public FetchThumbs fetchThumbs(com.appspot.mensa_furtwangen.thumbs.model.ThumbsQuery content) throws java.io.IOException {
    FetchThumbs result = new FetchThumbs(content);
    initialize(result);
    return result;
  }

  public class FetchThumbs extends ThumbsRequest<com.appspot.mensa_furtwangen.thumbs.model.ThumbsResult> {

    private static final String REST_PATH = "fetchThumbs";

    /**
     * Create a request for the method "fetchThumbs".
     *
     * This request holds the parameters needed by the the thumbs server.  After setting any optional
     * parameters, call the {@link FetchThumbs#execute()} method to invoke the remote operation. <p>
     * {@link
     * FetchThumbs#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param content the {@link com.appspot.mensa_furtwangen.thumbs.model.ThumbsQuery}
     * @since 1.13
     */
    protected FetchThumbs(com.appspot.mensa_furtwangen.thumbs.model.ThumbsQuery content) {
      super(Thumbs.this, "POST", REST_PATH, content, com.appspot.mensa_furtwangen.thumbs.model.ThumbsResult.class);
    }

    @Override
    public FetchThumbs setAlt(java.lang.String alt) {
      return (FetchThumbs) super.setAlt(alt);
    }

    @Override
    public FetchThumbs setFields(java.lang.String fields) {
      return (FetchThumbs) super.setFields(fields);
    }

    @Override
    public FetchThumbs setKey(java.lang.String key) {
      return (FetchThumbs) super.setKey(key);
    }

    @Override
    public FetchThumbs setOauthToken(java.lang.String oauthToken) {
      return (FetchThumbs) super.setOauthToken(oauthToken);
    }

    @Override
    public FetchThumbs setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (FetchThumbs) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public FetchThumbs setQuotaUser(java.lang.String quotaUser) {
      return (FetchThumbs) super.setQuotaUser(quotaUser);
    }

    @Override
    public FetchThumbs setUserIp(java.lang.String userIp) {
      return (FetchThumbs) super.setUserIp(userIp);
    }

    @Override
    public FetchThumbs set(String parameterName, Object value) {
      return (FetchThumbs) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "insertThumb".
   *
   * This request holds the parameters needed by the the thumbs server.  After setting any optional
   * parameters, call the {@link InsertThumb#execute()} method to invoke the remote operation.
   *
   * @param content the {@link com.appspot.mensa_furtwangen.thumbs.model.Thumb}
   * @return the request
   */
  public InsertThumb insertThumb(com.appspot.mensa_furtwangen.thumbs.model.Thumb content) throws java.io.IOException {
    InsertThumb result = new InsertThumb(content);
    initialize(result);
    return result;
  }

  public class InsertThumb extends ThumbsRequest<Void> {

    private static final String REST_PATH = "void";

    /**
     * Create a request for the method "insertThumb".
     *
     * This request holds the parameters needed by the the thumbs server.  After setting any optional
     * parameters, call the {@link InsertThumb#execute()} method to invoke the remote operation. <p>
     * {@link
     * InsertThumb#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param content the {@link com.appspot.mensa_furtwangen.thumbs.model.Thumb}
     * @since 1.13
     */
    protected InsertThumb(com.appspot.mensa_furtwangen.thumbs.model.Thumb content) {
      super(Thumbs.this, "POST", REST_PATH, content, Void.class);
    }

    @Override
    public InsertThumb setAlt(java.lang.String alt) {
      return (InsertThumb) super.setAlt(alt);
    }

    @Override
    public InsertThumb setFields(java.lang.String fields) {
      return (InsertThumb) super.setFields(fields);
    }

    @Override
    public InsertThumb setKey(java.lang.String key) {
      return (InsertThumb) super.setKey(key);
    }

    @Override
    public InsertThumb setOauthToken(java.lang.String oauthToken) {
      return (InsertThumb) super.setOauthToken(oauthToken);
    }

    @Override
    public InsertThumb setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (InsertThumb) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public InsertThumb setQuotaUser(java.lang.String quotaUser) {
      return (InsertThumb) super.setQuotaUser(quotaUser);
    }

    @Override
    public InsertThumb setUserIp(java.lang.String userIp) {
      return (InsertThumb) super.setUserIp(userIp);
    }

    @Override
    public InsertThumb set(String parameterName, Object value) {
      return (InsertThumb) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "removeThumbs".
   *
   * This request holds the parameters needed by the the thumbs server.  After setting any optional
   * parameters, call the {@link RemoveThumbs#execute()} method to invoke the remote operation.
   *
   * @param content the {@link com.appspot.mensa_furtwangen.thumbs.model.ThumbsQuery}
   * @return the request
   */
  public RemoveThumbs removeThumbs(com.appspot.mensa_furtwangen.thumbs.model.ThumbsQuery content) throws java.io.IOException {
    RemoveThumbs result = new RemoveThumbs(content);
    initialize(result);
    return result;
  }

  public class RemoveThumbs extends ThumbsRequest<Void> {

    private static final String REST_PATH = "thumbs";

    /**
     * Create a request for the method "removeThumbs".
     *
     * This request holds the parameters needed by the the thumbs server.  After setting any optional
     * parameters, call the {@link RemoveThumbs#execute()} method to invoke the remote operation. <p>
     * {@link
     * RemoveThumbs#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param content the {@link com.appspot.mensa_furtwangen.thumbs.model.ThumbsQuery}
     * @since 1.13
     */
    protected RemoveThumbs(com.appspot.mensa_furtwangen.thumbs.model.ThumbsQuery content) {
      super(Thumbs.this, "DELETE", REST_PATH, content, Void.class);
    }

    @Override
    public RemoveThumbs setAlt(java.lang.String alt) {
      return (RemoveThumbs) super.setAlt(alt);
    }

    @Override
    public RemoveThumbs setFields(java.lang.String fields) {
      return (RemoveThumbs) super.setFields(fields);
    }

    @Override
    public RemoveThumbs setKey(java.lang.String key) {
      return (RemoveThumbs) super.setKey(key);
    }

    @Override
    public RemoveThumbs setOauthToken(java.lang.String oauthToken) {
      return (RemoveThumbs) super.setOauthToken(oauthToken);
    }

    @Override
    public RemoveThumbs setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (RemoveThumbs) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public RemoveThumbs setQuotaUser(java.lang.String quotaUser) {
      return (RemoveThumbs) super.setQuotaUser(quotaUser);
    }

    @Override
    public RemoveThumbs setUserIp(java.lang.String userIp) {
      return (RemoveThumbs) super.setUserIp(userIp);
    }

    @Override
    public RemoveThumbs set(String parameterName, Object value) {
      return (RemoveThumbs) super.set(parameterName, value);
    }
  }

  /**
   * Builder for {@link Thumbs}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport HTTP transport, which should normally be:
     *        <ul>
     *        <li>Google App Engine:
     *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
     *        <li>Android: {@code newCompatibleTransport} from
     *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
     *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
     *        </li>
     *        </ul>
     * @param jsonFactory JSON factory, which may be:
     *        <ul>
     *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
     *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
     *        <li>Android Honeycomb or higher:
     *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
     *        </ul>
     * @param httpRequestInitializer HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
        com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      super(
          transport,
          jsonFactory,
          DEFAULT_ROOT_URL,
          DEFAULT_SERVICE_PATH,
          httpRequestInitializer,
          false);
    }

    /** Builds a new instance of {@link Thumbs}. */
    @Override
    public Thumbs build() {
      return new Thumbs(this);
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      return (Builder) super.setRootUrl(rootUrl);
    }

    @Override
    public Builder setServicePath(String servicePath) {
      return (Builder) super.setServicePath(servicePath);
    }

    @Override
    public Builder setHttpRequestInitializer(com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      return (Builder) super.setApplicationName(applicationName);
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      return (Builder) super.setSuppressPatternChecks(suppressPatternChecks);
    }

    @Override
    public Builder setSuppressRequiredParameterChecks(boolean suppressRequiredParameterChecks) {
      return (Builder) super.setSuppressRequiredParameterChecks(suppressRequiredParameterChecks);
    }

    @Override
    public Builder setSuppressAllChecks(boolean suppressAllChecks) {
      return (Builder) super.setSuppressAllChecks(suppressAllChecks);
    }

    /**
     * Set the {@link ThumbsRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setThumbsRequestInitializer(
        ThumbsRequestInitializer thumbsRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(thumbsRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}
