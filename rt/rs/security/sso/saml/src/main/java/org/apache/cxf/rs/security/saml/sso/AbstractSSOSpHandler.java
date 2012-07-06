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
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|sso
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
name|io
operator|.
name|InputStream
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
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|annotation
operator|.
name|PreDestroy
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|CallbackHandler
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
name|Bus
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|jaxrs
operator|.
name|utils
operator|.
name|HttpUtils
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
name|phase
operator|.
name|PhaseInterceptorChain
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
name|resource
operator|.
name|ResourceManager
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
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|sso
operator|.
name|state
operator|.
name|SPStateManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|WSSecurityException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|components
operator|.
name|crypto
operator|.
name|Crypto
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|components
operator|.
name|crypto
operator|.
name|CryptoFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|saml
operator|.
name|ext
operator|.
name|OpenSAMLUtil
import|;
end_import

begin_class
specifier|public
class|class
name|AbstractSSOSpHandler
block|{
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
name|AbstractSSOSpHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|SPStateManager
name|stateProvider
decl_stmt|;
specifier|private
name|long
name|stateTimeToLive
init|=
name|SSOConstants
operator|.
name|DEFAULT_STATE_TIME
decl_stmt|;
specifier|private
name|Crypto
name|signatureCrypto
decl_stmt|;
specifier|private
name|String
name|signaturePropertiesFile
decl_stmt|;
specifier|private
name|CallbackHandler
name|callbackHandler
decl_stmt|;
specifier|private
name|String
name|callbackHandlerClass
decl_stmt|;
static|static
block|{
name|OpenSAMLUtil
operator|.
name|initSamlEngine
argument_list|()
expr_stmt|;
block|}
annotation|@
name|PreDestroy
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|stateProvider
operator|!=
literal|null
condition|)
block|{
name|stateProvider
operator|.
name|close
argument_list|()
expr_stmt|;
name|stateProvider
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setSignatureCrypto
parameter_list|(
name|Crypto
name|crypto
parameter_list|)
block|{
name|signatureCrypto
operator|=
name|crypto
expr_stmt|;
block|}
comment|/**      * Set the String corresponding to the signature Properties class      * @param signaturePropertiesFile the String corresponding to the signature properties file      */
specifier|public
name|void
name|setSignaturePropertiesFile
parameter_list|(
name|String
name|signaturePropertiesFile
parameter_list|)
block|{
name|this
operator|.
name|signaturePropertiesFile
operator|=
name|signaturePropertiesFile
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Setting signature properties: "
operator|+
name|signaturePropertiesFile
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the CallbackHandler object.       * @param callbackHandler the CallbackHandler object.       */
specifier|public
name|void
name|setCallbackHandler
parameter_list|(
name|CallbackHandler
name|callbackHandler
parameter_list|)
block|{
name|this
operator|.
name|callbackHandler
operator|=
name|callbackHandler
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Setting callbackHandler: "
operator|+
name|callbackHandler
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the String corresponding to the CallbackHandler class.       * @param callbackHandlerClass the String corresponding to the CallbackHandler class.       */
specifier|public
name|void
name|setCallbackHandlerClass
parameter_list|(
name|String
name|callbackHandlerClass
parameter_list|)
block|{
name|this
operator|.
name|callbackHandlerClass
operator|=
name|callbackHandlerClass
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Setting callbackHandlerClass: "
operator|+
name|callbackHandlerClass
argument_list|)
expr_stmt|;
block|}
comment|//TODO: support attaching a signature to the cookie value
specifier|protected
name|String
name|createCookie
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|domain
parameter_list|)
block|{
name|String
name|contextCookie
init|=
name|name
operator|+
literal|"="
operator|+
name|value
decl_stmt|;
comment|// Setting a specific path restricts the browsers
comment|// to return a cookie only to the web applications
comment|// listening on that specific context path
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
name|contextCookie
operator|+=
literal|";Path="
operator|+
name|path
expr_stmt|;
block|}
comment|// Setting a specific domain further restricts the browsers
comment|// to return a cookie only to the web applications
comment|// listening on the specific context path within a particular domain
if|if
condition|(
name|domain
operator|!=
literal|null
condition|)
block|{
name|contextCookie
operator|+=
literal|";Domain="
operator|+
name|domain
expr_stmt|;
block|}
comment|// Keep the cookie across the browser restarts until it actually expires.
comment|// Note that the Expires property has been deprecated but apparently is
comment|// supported better than 'max-age' property by different browsers
comment|// (Firefox, IE, etc)
name|Date
name|expiresDate
init|=
operator|new
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
name|stateTimeToLive
argument_list|)
decl_stmt|;
name|String
name|cookieExpires
init|=
name|HttpUtils
operator|.
name|getHttpDateFormat
argument_list|()
operator|.
name|format
argument_list|(
name|expiresDate
argument_list|)
decl_stmt|;
name|contextCookie
operator|+=
literal|";Expires="
operator|+
name|cookieExpires
expr_stmt|;
comment|//TODO: Consider adding an 'HttpOnly' attribute
return|return
name|contextCookie
return|;
block|}
specifier|protected
name|boolean
name|isStateExpired
parameter_list|(
name|long
name|stateCreatedAt
parameter_list|,
name|long
name|expiresAt
parameter_list|)
block|{
name|Date
name|currentTime
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
if|if
condition|(
name|currentTime
operator|.
name|after
argument_list|(
operator|new
name|Date
argument_list|(
name|stateCreatedAt
operator|+
name|getStateTimeToLive
argument_list|()
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|expiresAt
operator|>
literal|0
operator|&&
name|currentTime
operator|.
name|after
argument_list|(
operator|new
name|Date
argument_list|(
name|expiresAt
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|setStateProvider
parameter_list|(
name|SPStateManager
name|stateProvider
parameter_list|)
block|{
name|this
operator|.
name|stateProvider
operator|=
name|stateProvider
expr_stmt|;
block|}
specifier|public
name|SPStateManager
name|getStateProvider
parameter_list|()
block|{
return|return
name|stateProvider
return|;
block|}
specifier|public
name|void
name|setStateTimeToLive
parameter_list|(
name|long
name|stateTimeToLive
parameter_list|)
block|{
name|this
operator|.
name|stateTimeToLive
operator|=
name|stateTimeToLive
expr_stmt|;
block|}
specifier|public
name|long
name|getStateTimeToLive
parameter_list|()
block|{
return|return
name|stateTimeToLive
return|;
block|}
specifier|protected
specifier|static
name|Properties
name|getProps
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|Properties
name|properties
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|Properties
condition|)
block|{
name|properties
operator|=
operator|(
name|Properties
operator|)
name|o
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
name|URL
name|url
init|=
literal|null
decl_stmt|;
name|Bus
name|bus
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
decl_stmt|;
name|ResourceManager
name|rm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ResourceManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|url
operator|=
name|rm
operator|.
name|resolveResource
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|,
name|URL
operator|.
name|class
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|url
operator|=
name|ClassLoaderUtils
operator|.
name|getResource
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|,
name|AbstractSSOSpHandler
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|properties
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
name|InputStream
name|ins
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|ins
argument_list|)
expr_stmt|;
name|ins
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|=
literal|null
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|URL
condition|)
block|{
name|properties
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
try|try
block|{
name|InputStream
name|ins
init|=
operator|(
operator|(
name|URL
operator|)
name|o
operator|)
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|ins
argument_list|)
expr_stmt|;
name|ins
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|=
literal|null
expr_stmt|;
block|}
block|}
return|return
name|properties
return|;
block|}
specifier|protected
name|Crypto
name|getSignatureCrypto
parameter_list|()
block|{
if|if
condition|(
name|signatureCrypto
operator|==
literal|null
operator|&&
name|signaturePropertiesFile
operator|!=
literal|null
condition|)
block|{
name|Properties
name|sigProperties
init|=
name|getProps
argument_list|(
name|signaturePropertiesFile
argument_list|)
decl_stmt|;
if|if
condition|(
name|sigProperties
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Cannot load signature properties using: "
operator|+
name|signaturePropertiesFile
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
try|try
block|{
name|signatureCrypto
operator|=
name|CryptoFactory
operator|.
name|getInstance
argument_list|(
name|sigProperties
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Error in loading the signature Crypto object: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
return|return
name|signatureCrypto
return|;
block|}
specifier|protected
name|CallbackHandler
name|getCallbackHandler
parameter_list|()
block|{
if|if
condition|(
name|callbackHandler
operator|==
literal|null
operator|&&
name|callbackHandlerClass
operator|!=
literal|null
condition|)
block|{
name|callbackHandler
operator|=
name|getCallbackHandler
argument_list|(
name|callbackHandlerClass
argument_list|)
expr_stmt|;
if|if
condition|(
name|callbackHandler
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Cannot load CallbackHandler using: "
operator|+
name|callbackHandlerClass
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
return|return
name|callbackHandler
return|;
block|}
specifier|private
name|CallbackHandler
name|getCallbackHandler
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|CallbackHandler
name|handler
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|CallbackHandler
condition|)
block|{
name|handler
operator|=
operator|(
name|CallbackHandler
operator|)
name|o
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
try|try
block|{
name|handler
operator|=
operator|(
name|CallbackHandler
operator|)
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|handler
operator|=
literal|null
expr_stmt|;
block|}
block|}
return|return
name|handler
return|;
block|}
block|}
end_class

end_unit

