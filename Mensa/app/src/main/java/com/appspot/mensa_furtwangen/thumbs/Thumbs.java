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
 * (build: 2014-04-01 18:14:47 UTC)
 * on 2014-04-14 at 13:18:43 UTC 
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
 * {@link com.appspot.mensa_furtwangen.thumbs.Thumbs.Builder}.
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
   * Use {@link com.appspot.mensa_furtwangen.thumbs.Thumbs.Builder} if you need to specify any of the optional parameters.
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
   * Create a request for the method "fetch".
   *
   * This request holds the parameters needed by the the thumbs server.  After setting any optional
   * parameters, call the {@link com.appspot.mensa_furtwangen.thumbs.Thumbs.Fetch#execute()} method to invoke the remote operation.
   *
   * @param deviceId
   * @param menuId
   * @return the request
   */
  public Fetch fetch(String deviceId, String menuId) throws java.io.IOException {
    Fetch result = new Fetch(deviceId, menuId);
    initialize(result);
    return result;
  }

  public class Fetch extends ThumbsRequest<com.appspot.mensa_furtwangen.thumbs.model.ThumbsResult> {

    private static final String REST_PATH = "fetchThumbs/{deviceId}/{menuId}";

    /**
     * Create a request for the method "fetch".
     *
     * This request holds the parameters needed by the the thumbs server.  After setting any optional
     * parameters, call the {@link com.appspot.mensa_furtwangen.thumbs.Thumbs.Fetch#execute()} method to invoke the remote operation. <p> {@link
     * com.appspot.mensa_furtwangen.thumbs.Thumbs.Fetch#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)} must
     * be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param deviceId
     * @param menuId
     * @since 1.13
     */
    protected Fetch(String deviceId, String menuId) {
      super(Thumbs.this, "POST", REST_PATH, null, com.appspot.mensa_furtwangen.thumbs.model.ThumbsResult.class);
      this.deviceId = com.google.api.client.util.Preconditions.checkNotNull(deviceId, "Required parameter deviceId must be specified.");
      this.menuId = com.google.api.client.util.Preconditions.checkNotNull(menuId, "Required parameter menuId must be specified.");
    }

    @Override
    public Fetch setAlt(String alt) {
      return (Fetch) super.setAlt(alt);
    }

    @Override
    public Fetch setFields(String fields) {
      return (Fetch) super.setFields(fields);
    }

    @Override
    public Fetch setKey(String key) {
      return (Fetch) super.setKey(key);
    }

    @Override
    public Fetch setOauthToken(String oauthToken) {
      return (Fetch) super.setOauthToken(oauthToken);
    }

    @Override
    public Fetch setPrettyPrint(Boolean prettyPrint) {
      return (Fetch) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public Fetch setQuotaUser(String quotaUser) {
      return (Fetch) super.setQuotaUser(quotaUser);
    }

    @Override
    public Fetch setUserIp(String userIp) {
      return (Fetch) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private String deviceId;

    /**

     */
    public String getDeviceId() {
      return deviceId;
    }

    public Fetch setDeviceId(String deviceId) {
      this.deviceId = deviceId;
      return this;
    }

    @com.google.api.client.util.Key
    private String menuId;

    /**

     */
    public String getMenuId() {
      return menuId;
    }

    public Fetch setMenuId(String menuId) {
      this.menuId = menuId;
      return this;
    }

    @Override
    public Fetch set(String parameterName, Object value) {
      return (Fetch) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "insert".
   *
   * This request holds the parameters needed by the the thumbs server.  After setting any optional
   * parameters, call the {@link com.appspot.mensa_furtwangen.thumbs.Thumbs.Insert#execute()} method to invoke the remote operation.
   *
   * @param content the {@link com.appspot.mensa_furtwangen.thumbs.model.Thumb}
   * @return the request
   */
  public Insert insert(com.appspot.mensa_furtwangen.thumbs.model.Thumb content) throws java.io.IOException {
    Insert result = new Insert(content);
    initialize(result);
    return result;
  }

  public class Insert extends ThumbsRequest<Void> {

    private static final String REST_PATH = "void";

    /**
     * Create a request for the method "insert".
     *
     * This request holds the parameters needed by the the thumbs server.  After setting any optional
     * parameters, call the {@link com.appspot.mensa_furtwangen.thumbs.Thumbs.Insert#execute()} method to invoke the remote operation. <p> {@link
     * com.appspot.mensa_furtwangen.thumbs.Thumbs.Insert#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)} must
     * be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param content the {@link com.appspot.mensa_furtwangen.thumbs.model.Thumb}
     * @since 1.13
     */
    protected Insert(com.appspot.mensa_furtwangen.thumbs.model.Thumb content) {
      super(Thumbs.this, "POST", REST_PATH, content, Void.class);
    }

    @Override
    public Insert setAlt(String alt) {
      return (Insert) super.setAlt(alt);
    }

    @Override
    public Insert setFields(String fields) {
      return (Insert) super.setFields(fields);
    }

    @Override
    public Insert setKey(String key) {
      return (Insert) super.setKey(key);
    }

    @Override
    public Insert setOauthToken(String oauthToken) {
      return (Insert) super.setOauthToken(oauthToken);
    }

    @Override
    public Insert setPrettyPrint(Boolean prettyPrint) {
      return (Insert) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public Insert setQuotaUser(String quotaUser) {
      return (Insert) super.setQuotaUser(quotaUser);
    }

    @Override
    public Insert setUserIp(String userIp) {
      return (Insert) super.setUserIp(userIp);
    }

    @Override
    public Insert set(String parameterName, Object value) {
      return (Insert) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "remove".
   *
   * This request holds the parameters needed by the the thumbs server.  After setting any optional
   * parameters, call the {@link com.appspot.mensa_furtwangen.thumbs.Thumbs.Remove#execute()} method to invoke the remote operation.
   *
   * @param deviceId
   * @param menuId
   * @return the request
   */
  public Remove remove(String deviceId, String menuId) throws java.io.IOException {
    Remove result = new Remove(deviceId, menuId);
    initialize(result);
    return result;
  }

  public class Remove extends ThumbsRequest<Void> {

    private static final String REST_PATH = "thumbs/{deviceId}/{menuId}";

    /**
     * Create a request for the method "remove".
     *
     * This request holds the parameters needed by the the thumbs server.  After setting any optional
     * parameters, call the {@link com.appspot.mensa_furtwangen.thumbs.Thumbs.Remove#execute()} method to invoke the remote operation. <p> {@link
     * com.appspot.mensa_furtwangen.thumbs.Thumbs.Remove#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)} must
     * be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param deviceId
     * @param menuId
     * @since 1.13
     */
    protected Remove(String deviceId, String menuId) {
      super(Thumbs.this, "DELETE", REST_PATH, null, Void.class);
      this.deviceId = com.google.api.client.util.Preconditions.checkNotNull(deviceId, "Required parameter deviceId must be specified.");
      this.menuId = com.google.api.client.util.Preconditions.checkNotNull(menuId, "Required parameter menuId must be specified.");
    }

    @Override
    public Remove setAlt(String alt) {
      return (Remove) super.setAlt(alt);
    }

    @Override
    public Remove setFields(String fields) {
      return (Remove) super.setFields(fields);
    }

    @Override
    public Remove setKey(String key) {
      return (Remove) super.setKey(key);
    }

    @Override
    public Remove setOauthToken(String oauthToken) {
      return (Remove) super.setOauthToken(oauthToken);
    }

    @Override
    public Remove setPrettyPrint(Boolean prettyPrint) {
      return (Remove) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public Remove setQuotaUser(String quotaUser) {
      return (Remove) super.setQuotaUser(quotaUser);
    }

    @Override
    public Remove setUserIp(String userIp) {
      return (Remove) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private String deviceId;

    /**

     */
    public String getDeviceId() {
      return deviceId;
    }

    public Remove setDeviceId(String deviceId) {
      this.deviceId = deviceId;
      return this;
    }

    @com.google.api.client.util.Key
    private String menuId;

    /**

     */
    public String getMenuId() {
      return menuId;
    }

    public Remove setMenuId(String menuId) {
      this.menuId = menuId;
      return this;
    }

    @Override
    public Remove set(String parameterName, Object value) {
      return (Remove) super.set(parameterName, value);
    }
  }

  /**
   * Builder for {@link com.appspot.mensa_furtwangen.thumbs.Thumbs}.
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

    /** Builds a new instance of {@link com.appspot.mensa_furtwangen.thumbs.Thumbs}. */
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
