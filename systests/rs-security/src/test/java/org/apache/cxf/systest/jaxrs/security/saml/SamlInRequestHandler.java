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
name|systest
operator|.
name|jaxrs
operator|.
name|security
operator|.
name|saml
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
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|Certificate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|zip
operator|.
name|Inflater
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Context
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
name|HttpHeaders
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
name|ext
operator|.
name|RequestHandler
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
name|model
operator|.
name|ClassResourceInfo
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
name|message
operator|.
name|Message
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
name|message
operator|.
name|MessageUtils
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
name|security
operator|.
name|transport
operator|.
name|TLSSessionInfo
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
name|ws
operator|.
name|security
operator|.
name|SecurityConstants
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
name|WSSConfig
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
name|handler
operator|.
name|RequestData
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
name|handler
operator|.
name|WSHandlerConstants
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
name|AssertionWrapper
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
name|validate
operator|.
name|Credential
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
name|validate
operator|.
name|SamlAssertionValidator
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
name|validate
operator|.
name|Validator
import|;
end_import

begin_class
specifier|public
class|class
name|SamlInRequestHandler
implements|implements
name|RequestHandler
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SAML_AUTH
init|=
literal|"SAML"
decl_stmt|;
annotation|@
name|Context
specifier|private
name|HttpHeaders
name|headers
decl_stmt|;
specifier|private
name|Validator
name|samlValidator
init|=
operator|new
name|SamlAssertionValidator
argument_list|()
decl_stmt|;
specifier|public
name|void
name|setValidator
parameter_list|(
name|Validator
name|validator
parameter_list|)
block|{
name|samlValidator
operator|=
name|validator
expr_stmt|;
block|}
specifier|public
name|Response
name|handleRequest
parameter_list|(
name|Message
name|message
parameter_list|,
name|ClassResourceInfo
name|resourceClass
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|headers
operator|.
name|getRequestHeader
argument_list|(
name|HttpHeaders
operator|.
name|AUTHORIZATION
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
operator|==
literal|null
operator|||
name|values
operator|.
name|size
argument_list|()
operator|!=
literal|1
operator|||
operator|!
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|startsWith
argument_list|(
name|SAML_AUTH
argument_list|)
condition|)
block|{
return|return
name|Response
operator|.
name|status
argument_list|(
literal|401
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
name|String
index|[]
name|parts
init|=
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|!=
literal|2
condition|)
block|{
return|return
name|Response
operator|.
name|status
argument_list|(
literal|401
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
name|Document
name|doc
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
name|parts
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|Inflater
name|inflater
init|=
operator|new
name|Inflater
argument_list|()
decl_stmt|;
name|inflater
operator|.
name|setInput
argument_list|(
name|deflatedToken
argument_list|)
expr_stmt|;
name|byte
index|[]
name|input
init|=
operator|new
name|byte
index|[
literal|4096
index|]
decl_stmt|;
name|int
name|length
init|=
name|inflater
operator|.
name|inflate
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|bis
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|input
argument_list|,
literal|0
argument_list|,
name|length
argument_list|)
decl_stmt|;
name|doc
operator|=
name|DOMUtils
operator|.
name|readXml
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|bis
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
return|return
name|Response
operator|.
name|status
argument_list|(
literal|401
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
try|try
block|{
name|AssertionWrapper
name|assertion
init|=
operator|new
name|AssertionWrapper
argument_list|(
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|assertion
operator|.
name|isSigned
argument_list|()
condition|)
block|{
name|RequestData
name|data
init|=
operator|new
name|RequestData
argument_list|()
decl_stmt|;
name|WSSConfig
name|cfg
init|=
operator|new
name|WSSConfig
argument_list|()
decl_stmt|;
name|data
operator|.
name|setWssConfig
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|data
operator|.
name|setCallbackHandler
argument_list|(
name|getCallbackHandler
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|setSigCrypto
argument_list|(
name|getCrypto
argument_list|(
name|message
argument_list|,
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|setEnableRevocation
argument_list|(
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|ENABLE_REVOCATION
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertion
operator|.
name|verifySignature
argument_list|(
name|data
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertion
operator|.
name|parseHOKSubject
argument_list|(
name|data
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Credential
name|credential
init|=
operator|new
name|Credential
argument_list|()
decl_stmt|;
name|credential
operator|.
name|setAssertion
argument_list|(
name|assertion
argument_list|)
expr_stmt|;
if|if
condition|(
name|samlValidator
operator|!=
literal|null
condition|)
block|{
name|samlValidator
operator|.
name|validate
argument_list|(
name|credential
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
name|TLSSessionInfo
name|tlsInfo
init|=
name|message
operator|.
name|get
argument_list|(
name|TLSSessionInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|Certificate
index|[]
name|tlsCerts
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|tlsInfo
operator|!=
literal|null
condition|)
block|{
name|tlsCerts
operator|=
name|tlsInfo
operator|.
name|getPeerCertificates
argument_list|()
expr_stmt|;
block|}
comment|// AbstractSamlPolicyValidator:
comment|//if (!checkHolderOfKey(assertion, null, tlsCerts)) {
comment|//    return Response.status(401).build();
comment|//}
if|if
condition|(
operator|!
name|checkSenderVouches
argument_list|(
name|assertion
argument_list|,
name|tlsCerts
argument_list|)
condition|)
block|{
return|return
name|Response
operator|.
name|status
argument_list|(
literal|401
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
return|return
name|Response
operator|.
name|status
argument_list|(
literal|401
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|Crypto
name|getCrypto
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|propKey
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|o
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|propKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|ClassLoader
name|orig
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
try|try
block|{
name|URL
name|url
init|=
name|ClassLoaderUtils
operator|.
name|getResource
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
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|ResourceManager
name|manager
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|ResourceManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|ClassLoader
name|loader
init|=
name|manager
operator|.
name|resolveResource
argument_list|(
literal|""
argument_list|,
name|ClassLoader
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
block|}
name|url
operator|=
name|manager
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
block|}
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|InputStream
name|in
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|props
operator|.
name|load
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|CryptoFactory
operator|.
name|getInstance
argument_list|(
name|props
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//ignore
block|}
return|return
name|CryptoFactory
operator|.
name|getInstance
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|)
return|;
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|orig
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|CallbackHandler
name|getCallbackHandler
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
comment|//Then try to get the password from the given callback handler
name|Object
name|o
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|CALLBACK_HANDLER
argument_list|)
decl_stmt|;
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
comment|// SamlTokenPolicyValidator
comment|/**      * Check the sender-vouches requirements against the received assertion. The SAML      * Assertion and the SOAP Body must be signed by the same signature.      */
specifier|private
name|boolean
name|checkSenderVouches
parameter_list|(
name|AssertionWrapper
name|assertionWrapper
parameter_list|,
name|Certificate
index|[]
name|tlsCerts
parameter_list|)
block|{
comment|//
comment|// If we have a 2-way TLS connection, then we don't have to check that the
comment|// assertion + SOAP body are signed
comment|//
if|if
condition|(
name|tlsCerts
operator|!=
literal|null
operator|&&
name|tlsCerts
operator|.
name|length
operator|>
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
comment|//        List<String> confirmationMethods = assertionWrapper.getConfirmationMethods();
comment|//        for (String confirmationMethod : confirmationMethods) {
comment|//            if (OpenSAMLUtil.isMethodSenderVouches(confirmationMethod)) {
comment|//                if (signedResults == null || signedResults.isEmpty()) {
comment|//                    return false;
comment|//                }
comment|//                if (!checkAssertionAndBodyAreSigned(assertionWrapper)) {
comment|//                    return false;
comment|//                }
comment|//            }
comment|//        }
comment|//        return true;
block|}
block|}
end_class

end_unit

