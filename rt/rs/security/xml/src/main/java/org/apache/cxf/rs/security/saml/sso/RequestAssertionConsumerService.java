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
name|ByteArrayInputStream
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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|ResourceBundle
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|DataFormatException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|FormParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|POST
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|QueryParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|WebApplicationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|i18n
operator|.
name|BundleUtils
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
name|Base64Exception
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
name|Base64Utility
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
name|StringUtils
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
name|helpers
operator|.
name|DOMUtils
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
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|DeflateEncoderDecoder
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
name|RequestState
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
name|ResponseState
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
name|saml
operator|.
name|ext
operator|.
name|OpenSAMLUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|xml
operator|.
name|XMLObject
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"sso"
argument_list|)
specifier|public
class|class
name|RequestAssertionConsumerService
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
name|RequestAssertionConsumerService
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|RequestAssertionConsumerService
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|supportDeflateEncoding
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|supportBase64Encoding
init|=
literal|true
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
specifier|public
name|void
name|setSupportDeflateEncoding
parameter_list|(
name|boolean
name|deflate
parameter_list|)
block|{
name|supportDeflateEncoding
operator|=
name|deflate
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSupportDeflateEncoding
parameter_list|()
block|{
return|return
name|supportDeflateEncoding
return|;
block|}
specifier|public
name|void
name|setSupportBase64Encoding
parameter_list|(
name|boolean
name|supportBase64Encoding
parameter_list|)
block|{
name|this
operator|.
name|supportBase64Encoding
operator|=
name|supportBase64Encoding
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSupportBase64Encoding
parameter_list|()
block|{
return|return
name|supportBase64Encoding
return|;
block|}
annotation|@
name|POST
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED
argument_list|)
specifier|public
name|Response
name|processSamlResponse
parameter_list|(
annotation|@
name|FormParam
argument_list|(
name|SSOConstants
operator|.
name|SAML_RESPONSE
argument_list|)
name|String
name|encodedSamlResponse
parameter_list|,
annotation|@
name|FormParam
argument_list|(
name|SSOConstants
operator|.
name|RELAY_STATE
argument_list|)
name|String
name|relayState
parameter_list|)
block|{
if|if
condition|(
name|relayState
operator|==
literal|null
condition|)
block|{
name|reportError
argument_list|(
literal|"MISSING_RELAY_STATE"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
name|RequestState
name|requestState
init|=
name|stateProvider
operator|.
name|removeRequestState
argument_list|(
name|relayState
argument_list|)
decl_stmt|;
if|if
condition|(
name|requestState
operator|==
literal|null
condition|)
block|{
name|reportError
argument_list|(
literal|"MISSING_REQUEST_STATE"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
name|long
name|stateCreatedAt
init|=
name|requestState
operator|.
name|getCreatedAt
argument_list|()
decl_stmt|;
if|if
condition|(
operator|new
name|Date
argument_list|()
operator|.
name|after
argument_list|(
operator|new
name|Date
argument_list|(
name|stateCreatedAt
operator|+
name|stateTimeToLive
argument_list|)
argument_list|)
condition|)
block|{
name|reportError
argument_list|(
literal|"EXPIRED_REQUEST_STATE"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
name|URI
name|targetURI
init|=
name|getTargetURI
argument_list|(
name|requestState
operator|.
name|getTargetAddress
argument_list|()
argument_list|)
decl_stmt|;
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Response
name|samlResponse
init|=
name|readSAMLResponse
argument_list|(
name|encodedSamlResponse
argument_list|)
decl_stmt|;
name|validateSamlResponse
argument_list|(
name|samlResponse
argument_list|,
name|requestState
argument_list|)
expr_stmt|;
comment|// Set the security context
name|String
name|securityContextKey
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|long
name|currentTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|ResponseState
name|responseState
init|=
operator|new
name|ResponseState
argument_list|(
name|currentTime
argument_list|)
decl_stmt|;
name|stateProvider
operator|.
name|setResponseState
argument_list|(
name|securityContextKey
argument_list|,
name|responseState
argument_list|)
expr_stmt|;
name|String
name|contextCookie
init|=
name|SSOConstants
operator|.
name|SECURITY_CONTEXT_TOKEN
operator|+
literal|"="
operator|+
name|securityContextKey
decl_stmt|;
name|Date
name|expiresDate
init|=
operator|new
name|Date
argument_list|(
name|currentTime
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
comment|// Finally, redirect to the service provider endpoint
return|return
name|Response
operator|.
name|seeOther
argument_list|(
name|targetURI
argument_list|)
operator|.
name|header
argument_list|(
literal|"Set-Cookie"
argument_list|,
name|contextCookie
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|GET
specifier|public
name|Response
name|getSamlResponse
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
name|SSOConstants
operator|.
name|SAML_RESPONSE
argument_list|)
name|String
name|samlResponse
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
name|SSOConstants
operator|.
name|RELAY_STATE
argument_list|)
name|String
name|relayState
parameter_list|)
block|{
return|return
name|processSamlResponse
argument_list|(
name|relayState
argument_list|,
name|samlResponse
argument_list|)
return|;
block|}
specifier|private
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Response
name|readSAMLResponse
parameter_list|(
name|String
name|samlResponse
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|samlResponse
argument_list|)
condition|)
block|{
name|reportError
argument_list|(
literal|"MISSING_SAML_RESPONSE"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
name|InputStream
name|tokenStream
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isSupportBase64Encoding
argument_list|()
condition|)
block|{
try|try
block|{
name|byte
index|[]
name|deflatedToken
init|=
name|Base64Utility
operator|.
name|decode
argument_list|(
name|samlResponse
argument_list|)
decl_stmt|;
name|tokenStream
operator|=
name|isSupportDeflateEncoding
argument_list|()
condition|?
operator|new
name|DeflateEncoderDecoder
argument_list|()
operator|.
name|inflateToken
argument_list|(
name|deflatedToken
argument_list|)
else|:
operator|new
name|ByteArrayInputStream
argument_list|(
name|deflatedToken
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Base64Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|DataFormatException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
block|}
else|else
block|{
try|try
block|{
name|tokenStream
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|samlResponse
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
block|}
name|Document
name|responseDoc
init|=
literal|null
decl_stmt|;
try|try
block|{
name|responseDoc
operator|=
name|DOMUtils
operator|.
name|readXml
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|tokenStream
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
name|XMLObject
name|responseObject
init|=
literal|null
decl_stmt|;
try|try
block|{
name|responseObject
operator|=
name|OpenSAMLUtil
operator|.
name|fromDom
argument_list|(
name|responseDoc
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
operator|(
name|responseObject
operator|instanceof
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Response
operator|)
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
return|return
operator|(
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Response
operator|)
name|responseObject
return|;
block|}
specifier|protected
name|void
name|validateSamlResponse
parameter_list|(
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Response
name|samlResponse
parameter_list|,
name|RequestState
name|requestState
parameter_list|)
block|{
name|SAMLProtocolResponseValidator
name|protocolValidator
init|=
operator|new
name|SAMLProtocolResponseValidator
argument_list|()
decl_stmt|;
comment|// TODO Configure Crypto& CallbackHandler object here to validate signatures
try|try
block|{
name|protocolValidator
operator|.
name|validateSamlResponse
argument_list|(
name|samlResponse
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|ex
parameter_list|)
block|{
name|reportError
argument_list|(
literal|"INVALID_SAML_RESPONSE"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
block|}
specifier|private
name|URI
name|getTargetURI
parameter_list|(
name|String
name|targetAddress
parameter_list|)
block|{
if|if
condition|(
name|targetAddress
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|URI
operator|.
name|create
argument_list|(
name|targetAddress
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
name|reportError
argument_list|(
literal|"INVALID_TARGET_URI"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|reportError
argument_list|(
literal|"MISSING_TARGET_URI"
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|400
argument_list|)
throw|;
block|}
specifier|private
name|void
name|reportError
parameter_list|(
name|String
name|code
parameter_list|)
block|{
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
name|errorMsg
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
name|code
argument_list|,
name|BUNDLE
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|errorMsg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setStateTimeToLive
parameter_list|(
name|long
name|stateTime
parameter_list|)
block|{
name|this
operator|.
name|stateTimeToLive
operator|=
name|stateTime
expr_stmt|;
block|}
specifier|public
name|void
name|setStateProvider
parameter_list|(
name|SPStateManager
name|provider
parameter_list|)
block|{
name|this
operator|.
name|stateProvider
operator|=
name|provider
expr_stmt|;
block|}
block|}
end_class

end_unit

