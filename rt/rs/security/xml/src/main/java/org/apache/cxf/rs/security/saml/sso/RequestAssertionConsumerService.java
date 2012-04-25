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
name|ResourceBundle
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
name|Encoded
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
specifier|static
specifier|final
name|String
name|SAML_RESPONSE
init|=
literal|"SAMLResponse"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RELAY_STATE
init|=
literal|"RelayState"
decl_stmt|;
specifier|private
name|boolean
name|useDeflateEncoding
init|=
literal|true
decl_stmt|;
specifier|public
name|void
name|setUseDeflateEncoding
parameter_list|(
name|boolean
name|deflate
parameter_list|)
block|{
name|useDeflateEncoding
operator|=
name|deflate
expr_stmt|;
block|}
specifier|public
name|boolean
name|useDeflateEncoding
parameter_list|()
block|{
return|return
name|useDeflateEncoding
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
name|Encoded
annotation|@
name|FormParam
argument_list|(
name|RELAY_STATE
argument_list|)
name|String
name|relayState
parameter_list|,
annotation|@
name|Encoded
annotation|@
name|FormParam
argument_list|(
name|SAML_RESPONSE
argument_list|)
name|String
name|encodedSamlResponse
parameter_list|)
block|{
name|URI
name|relayURI
init|=
name|getRelayURI
argument_list|(
name|relayState
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
argument_list|)
expr_stmt|;
comment|// TODO: set the security context
comment|// finally, redirect to the service provider endpoint
return|return
name|Response
operator|.
name|seeOther
argument_list|(
name|relayURI
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
name|Encoded
annotation|@
name|QueryParam
argument_list|(
name|RELAY_STATE
argument_list|)
name|String
name|relayState
parameter_list|,
annotation|@
name|Encoded
annotation|@
name|QueryParam
argument_list|(
name|SAML_RESPONSE
argument_list|)
name|String
name|samlResponse
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
name|useDeflateEncoding
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
name|getRelayURI
parameter_list|(
name|String
name|relayState
parameter_list|)
block|{
if|if
condition|(
name|relayState
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
name|relayState
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
literal|"INVALID_RELAY_STATE"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|reportError
argument_list|(
literal|"MISSING_RELAY_STATE"
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
block|}
end_class

end_unit

