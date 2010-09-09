begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|https
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Constructor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationHandler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|HttpURLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Proxy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Handler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|HostnameVerifier
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|HttpsURLConnection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLSocketFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|util
operator|.
name|ReflectionInvokationHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|configuration
operator|.
name|jsse
operator|.
name|TLSClientParameters
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|http
operator|.
name|HTTPConduit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|http
operator|.
name|HttpURLConnectionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|http
operator|.
name|HttpURLConnectionInfo
import|;
end_import

begin_comment
comment|/**  * This HttpsURLConnectionFactory implements the HttpURLConnectionFactory  * for using the given SSL Policy to configure TLS connections for "https:"  * URLs.  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|HttpsURLConnectionFactory
implements|implements
name|HttpURLConnectionFactory
block|{
comment|/**      * This constant holds the URL Protocol Identifier for HTTPS      */
specifier|public
specifier|static
specifier|final
name|String
name|HTTPS_URL_PROTOCOL_ID
init|=
literal|"https"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|HttpsURLConnectionFactory
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|weblogicWarned
decl_stmt|;
comment|/**      * This field holds the conduit to which this connection factory      * is a slave.      */
name|HTTPConduit
name|conduit
decl_stmt|;
comment|/**      * This field contains the TLS configuration for the URLs created by      * this factory.      */
name|TLSClientParameters
name|tlsClientParameters
decl_stmt|;
comment|/**      * Cache the last SSLContext to avoid recreation      */
name|SSLSocketFactory
name|socketFactory
decl_stmt|;
comment|/**      * This constructor initialized the factory with the configured TLS      * Client Parameters for the HTTPConduit for which this factory is used.      *       * @param params The TLS Client Parameters. This parameter is guaranteed       *               to be non-null.      */
specifier|public
name|HttpsURLConnectionFactory
parameter_list|(
name|TLSClientParameters
name|params
parameter_list|)
block|{
name|tlsClientParameters
operator|=
name|params
expr_stmt|;
assert|assert
name|tlsClientParameters
operator|!=
literal|null
assert|;
block|}
comment|/**      * Create a HttpURLConnection, proxified if necessary.      *       *       * @param proxy This parameter is non-null if connection should be proxied.      * @param url   The target URL. This parameter must be an https url.      *       * @return The HttpsURLConnection for the given URL.      * @throws IOException This exception is thrown if       *         the "url" is not "https" or other IOException      *         is thrown.       *                           */
specifier|public
name|HttpURLConnection
name|createConnection
parameter_list|(
name|Proxy
name|proxy
parameter_list|,
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|url
operator|.
name|getProtocol
argument_list|()
operator|.
name|equals
argument_list|(
name|HTTPS_URL_PROTOCOL_ID
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Illegal Protocol "
operator|+
name|url
operator|.
name|getProtocol
argument_list|()
operator|+
literal|" for HTTPS URLConnection Factory."
argument_list|)
throw|;
block|}
name|HttpURLConnection
name|connection
init|=
call|(
name|HttpURLConnection
call|)
argument_list|(
name|proxy
operator|!=
literal|null
condition|?
name|url
operator|.
name|openConnection
argument_list|(
name|proxy
argument_list|)
else|:
name|url
operator|.
name|openConnection
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|tlsClientParameters
operator|!=
literal|null
condition|)
block|{
name|Exception
name|ex
init|=
literal|null
decl_stmt|;
try|try
block|{
name|decorateWithTLS
argument_list|(
name|connection
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|ex
operator|=
name|e
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|ex
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|ex
operator|instanceof
name|IOException
condition|)
block|{
throw|throw
operator|(
name|IOException
operator|)
name|ex
throw|;
block|}
comment|// use exception.initCause(ex) to be java 5 compatible
name|IOException
name|ioException
init|=
operator|new
name|IOException
argument_list|(
literal|"Error while initializing secure socket"
argument_list|)
decl_stmt|;
name|ioException
operator|.
name|initCause
argument_list|(
name|ex
argument_list|)
expr_stmt|;
throw|throw
name|ioException
throw|;
block|}
block|}
block|}
return|return
name|connection
return|;
block|}
comment|/**      * This method assigns the various TLS parameters on the HttpsURLConnection      * from the TLS Client Parameters. Connection parameter is of supertype HttpURLConnection,       * which allows internal cast to potentially divergent subtype (https) implementations.      */
specifier|protected
specifier|synchronized
name|void
name|decorateWithTLS
parameter_list|(
name|HttpURLConnection
name|connection
parameter_list|)
throws|throws
name|GeneralSecurityException
block|{
comment|// always reload socketFactory from HttpsURLConnection.defaultSSLSocketFactory and
comment|// tlsClientParameters.sslSocketFactory to allow runtime configuration change
if|if
condition|(
name|tlsClientParameters
operator|.
name|isUseHttpsURLConnectionDefaultSslSocketFactory
argument_list|()
condition|)
block|{
name|socketFactory
operator|=
name|HttpsURLConnection
operator|.
name|getDefaultSSLSocketFactory
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|tlsClientParameters
operator|.
name|getSSLSocketFactory
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// see if an SSLSocketFactory was set. This allows easy interop
comment|// with not-yet-commons-ssl.jar, or even just people who like doing their
comment|// own JSSE.
name|socketFactory
operator|=
name|tlsClientParameters
operator|.
name|getSSLSocketFactory
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|socketFactory
operator|==
literal|null
condition|)
block|{
comment|// ssl socket factory not yet instantiated, create a new one with tlsClientParameters's Trust
comment|// Managers, Key Managers, etc
name|String
name|provider
init|=
name|tlsClientParameters
operator|.
name|getJsseProvider
argument_list|()
decl_stmt|;
name|String
name|protocol
init|=
name|tlsClientParameters
operator|.
name|getSecureSocketProtocol
argument_list|()
operator|!=
literal|null
condition|?
name|tlsClientParameters
operator|.
name|getSecureSocketProtocol
argument_list|()
else|:
literal|"TLS"
decl_stmt|;
name|SSLContext
name|ctx
init|=
name|provider
operator|==
literal|null
condition|?
name|SSLContext
operator|.
name|getInstance
argument_list|(
name|protocol
argument_list|)
else|:
name|SSLContext
operator|.
name|getInstance
argument_list|(
name|protocol
argument_list|,
name|provider
argument_list|)
decl_stmt|;
name|ctx
operator|.
name|getClientSessionContext
argument_list|()
operator|.
name|setSessionTimeout
argument_list|(
name|tlsClientParameters
operator|.
name|getSslCacheTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|init
argument_list|(
name|tlsClientParameters
operator|.
name|getKeyManagers
argument_list|()
argument_list|,
name|tlsClientParameters
operator|.
name|getTrustManagers
argument_list|()
argument_list|,
name|tlsClientParameters
operator|.
name|getSecureRandom
argument_list|()
argument_list|)
expr_stmt|;
comment|// The "false" argument means opposite of exclude.
name|String
index|[]
name|cipherSuites
init|=
name|SSLUtils
operator|.
name|getCiphersuites
argument_list|(
name|tlsClientParameters
operator|.
name|getCipherSuites
argument_list|()
argument_list|,
name|SSLUtils
operator|.
name|getSupportedCipherSuites
argument_list|(
name|ctx
argument_list|)
argument_list|,
name|tlsClientParameters
operator|.
name|getCipherSuitesFilter
argument_list|()
argument_list|,
name|LOG
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|// The SSLSocketFactoryWrapper enables certain cipher suites
comment|// from the policy.
name|socketFactory
operator|=
operator|new
name|SSLSocketFactoryWrapper
argument_list|(
name|ctx
operator|.
name|getSocketFactory
argument_list|()
argument_list|,
name|cipherSuites
argument_list|,
name|tlsClientParameters
operator|.
name|getSecureSocketProtocol
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// ssl socket factory already initialized, reuse it to benefit of keep alive
block|}
name|HostnameVerifier
name|verifier
decl_stmt|;
if|if
condition|(
name|tlsClientParameters
operator|.
name|isUseHttpsURLConnectionDefaultHostnameVerifier
argument_list|()
condition|)
block|{
name|verifier
operator|=
name|HttpsURLConnection
operator|.
name|getDefaultHostnameVerifier
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|tlsClientParameters
operator|.
name|isDisableCNCheck
argument_list|()
condition|)
block|{
name|verifier
operator|=
name|CertificateHostnameVerifier
operator|.
name|ALLOW_ALL
expr_stmt|;
block|}
else|else
block|{
name|verifier
operator|=
name|CertificateHostnameVerifier
operator|.
name|DEFAULT
expr_stmt|;
block|}
if|if
condition|(
name|connection
operator|instanceof
name|HttpsURLConnection
condition|)
block|{
comment|// handle the expected case (javax.net.ssl)
name|HttpsURLConnection
name|conn
init|=
operator|(
name|HttpsURLConnection
operator|)
name|connection
decl_stmt|;
name|conn
operator|.
name|setHostnameVerifier
argument_list|(
name|verifier
argument_list|)
expr_stmt|;
name|conn
operator|.
name|setSSLSocketFactory
argument_list|(
name|socketFactory
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// handle the deprecated sun case and other possible hidden API's
comment|// that are similar to the Sun cases
try|try
block|{
name|Method
name|method
init|=
name|connection
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getHostnameVerifier"
argument_list|)
decl_stmt|;
name|InvocationHandler
name|handler
init|=
operator|new
name|ReflectionInvokationHandler
argument_list|(
name|verifier
argument_list|)
block|{
specifier|public
name|Object
name|invoke
parameter_list|(
name|Object
name|proxy
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
try|try
block|{
return|return
name|super
operator|.
name|invoke
argument_list|(
name|proxy
argument_list|,
name|method
argument_list|,
name|args
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
decl_stmt|;
name|Object
name|proxy
init|=
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|,
operator|new
name|Class
index|[]
block|{
name|method
operator|.
name|getReturnType
argument_list|()
block|}
argument_list|,
name|handler
argument_list|)
decl_stmt|;
name|method
operator|=
name|connection
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"setHostnameVerifier"
argument_list|,
name|method
operator|.
name|getReturnType
argument_list|()
argument_list|)
expr_stmt|;
name|method
operator|.
name|invoke
argument_list|(
name|connection
argument_list|,
name|proxy
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//Ignore this one
block|}
try|try
block|{
name|Method
name|getSSLSocketFactory
init|=
name|connection
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getSSLSocketFactory"
argument_list|)
decl_stmt|;
name|Method
name|setSSLSocketFactory
init|=
name|connection
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"setSSLSocketFactory"
argument_list|,
name|getSSLSocketFactory
operator|.
name|getReturnType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|getSSLSocketFactory
operator|.
name|getReturnType
argument_list|()
operator|.
name|isInstance
argument_list|(
name|socketFactory
argument_list|)
condition|)
block|{
name|setSSLSocketFactory
operator|.
name|invoke
argument_list|(
name|connection
argument_list|,
name|socketFactory
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//need to see if we can create one - mostly the weblogic case.   The
comment|//weblogic SSLSocketFactory has a protected constructor that can take
comment|//a JSSE SSLSocketFactory so we'll try and use that
name|Constructor
name|c
init|=
name|getSSLSocketFactory
operator|.
name|getReturnType
argument_list|()
operator|.
name|getDeclaredConstructor
argument_list|(
name|SSLSocketFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|c
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setSSLSocketFactory
operator|.
name|invoke
argument_list|(
name|connection
argument_list|,
name|c
operator|.
name|newInstance
argument_list|(
name|socketFactory
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
if|if
condition|(
name|connection
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"weblogic"
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|weblogicWarned
condition|)
block|{
name|weblogicWarned
operator|=
literal|true
expr_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
literal|"Could not configure SSLSocketFactory on Weblogic.  "
operator|+
literal|" Use the Weblogic control panel to configure the SSL settings."
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
comment|//if we cannot set the SSLSocketFactor, we're in serious trouble.
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Error decorating connection class "
operator|+
name|connection
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
comment|/*      *  For development and testing only      */
specifier|protected
name|void
name|addLogHandler
parameter_list|(
name|Handler
name|handler
parameter_list|)
block|{
name|LOG
operator|.
name|addHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
comment|/**      * This operation returns an HttpsURLConnectionInfo for the       * given HttpsURLConnection.       *       * @param connection The HttpsURLConnection      * @return The HttpsURLConnectionInfo object for the given       *         HttpsURLConnection.      * @throws IOException Normal IO Exceptions.      * @throws ClassCastException If "connection" is not an HttpsURLConnection       *         (or a supported subtype of HttpURLConnection)      */
specifier|public
name|HttpURLConnectionInfo
name|getConnectionInfo
parameter_list|(
name|HttpURLConnection
name|connection
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|HttpsURLConnectionInfo
argument_list|(
name|connection
argument_list|)
return|;
block|}
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
return|return
literal|"https"
return|;
block|}
block|}
end_class

end_unit

