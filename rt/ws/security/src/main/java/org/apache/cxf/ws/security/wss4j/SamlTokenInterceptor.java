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
name|ws
operator|.
name|security
operator|.
name|wss4j
package|;
end_package

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
name|Principal
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|Element
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
name|binding
operator|.
name|soap
operator|.
name|SoapMessage
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
name|headers
operator|.
name|Header
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
name|CastUtils
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
name|interceptor
operator|.
name|security
operator|.
name|DefaultSecurityContext
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
name|rt
operator|.
name|security
operator|.
name|utils
operator|.
name|SecurityUtils
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
name|SecurityContext
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
name|policy
operator|.
name|AssertionInfo
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
name|policy
operator|.
name|AssertionInfoMap
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
name|cxf
operator|.
name|ws
operator|.
name|security
operator|.
name|policy
operator|.
name|PolicyUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|WSS4JConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
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
name|wss4j
operator|.
name|common
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
name|wss4j
operator|.
name|common
operator|.
name|crypto
operator|.
name|PasswordEncryptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|ext
operator|.
name|WSPasswordCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|ext
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
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|SAMLCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|SAMLUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|SamlAssertionWrapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|bean
operator|.
name|Version
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|WSConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|WSDocInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|engine
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
name|wss4j
operator|.
name|dom
operator|.
name|engine
operator|.
name|WSSecurityEngineResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
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
name|wss4j
operator|.
name|dom
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
name|wss4j
operator|.
name|dom
operator|.
name|handler
operator|.
name|WSHandlerResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|processor
operator|.
name|SAMLTokenProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|saml
operator|.
name|DOMSAMLUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|SPConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|model
operator|.
name|AbstractToken
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|model
operator|.
name|SamlToken
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|model
operator|.
name|SamlToken
operator|.
name|SamlTokenType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|common
operator|.
name|SAMLVersion
import|;
end_import

begin_comment
comment|/**  * An interceptor to create and add a SAML token to the security header of an outbound  * request, and to process a SAML Token on an inbound request.  */
end_comment

begin_class
specifier|public
class|class
name|SamlTokenInterceptor
extends|extends
name|AbstractTokenInterceptor
block|{
specifier|public
name|SamlTokenInterceptor
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|processToken
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
name|Header
name|h
init|=
name|findSecurityHeader
argument_list|(
name|message
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|h
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Element
name|el
init|=
operator|(
name|Element
operator|)
name|h
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|Element
name|child
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|el
argument_list|)
decl_stmt|;
while|while
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"Assertion"
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|&&
operator|(
name|WSS4JConstants
operator|.
name|SAML_NS
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|||
name|WSS4JConstants
operator|.
name|SAML2_NS
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|)
condition|)
block|{
try|try
block|{
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|samlResults
init|=
name|processToken
argument_list|(
name|child
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|samlResults
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|WSHandlerResult
argument_list|>
name|results
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|WSHandlerConstants
operator|.
name|RECV_RESULTS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|results
operator|==
literal|null
condition|)
block|{
name|results
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|RECV_RESULTS
argument_list|,
name|results
argument_list|)
expr_stmt|;
block|}
name|boolean
name|signed
init|=
literal|false
decl_stmt|;
for|for
control|(
name|WSSecurityEngineResult
name|result
range|:
name|samlResults
control|)
block|{
name|SamlAssertionWrapper
name|wrapper
init|=
operator|(
name|SamlAssertionWrapper
operator|)
name|result
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_SAML_ASSERTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|wrapper
operator|.
name|isSigned
argument_list|()
condition|)
block|{
name|signed
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
name|assertTokens
argument_list|(
name|message
argument_list|,
name|SPConstants
operator|.
name|SAML_TOKEN
argument_list|,
name|signed
argument_list|)
expr_stmt|;
name|Integer
name|key
init|=
name|WSConstants
operator|.
name|ST_UNSIGNED
decl_stmt|;
if|if
condition|(
name|signed
condition|)
block|{
name|key
operator|=
name|WSConstants
operator|.
name|ST_SIGNED
expr_stmt|;
block|}
name|WSHandlerResult
name|rResult
init|=
operator|new
name|WSHandlerResult
argument_list|(
literal|null
argument_list|,
name|samlResults
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
name|key
argument_list|,
name|samlResults
argument_list|)
argument_list|)
decl_stmt|;
name|results
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|rResult
argument_list|)
expr_stmt|;
comment|// Check version against policy
name|AssertionInfoMap
name|aim
init|=
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|PolicyUtils
operator|.
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|SAML_TOKEN
argument_list|)
control|)
block|{
name|SamlToken
name|samlToken
init|=
operator|(
name|SamlToken
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
for|for
control|(
name|WSSecurityEngineResult
name|result
range|:
name|samlResults
control|)
block|{
name|SamlAssertionWrapper
name|assertionWrapper
init|=
operator|(
name|SamlAssertionWrapper
operator|)
name|result
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_SAML_ASSERTION
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|checkVersion
argument_list|(
name|aim
argument_list|,
name|samlToken
argument_list|,
name|assertionWrapper
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Wrong SAML Version"
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
if|if
condition|(
operator|!
name|DOMSAMLUtil
operator|.
name|checkHolderOfKey
argument_list|(
name|assertionWrapper
argument_list|,
literal|null
argument_list|,
name|tlsCerts
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Assertion fails holder-of-key requirements"
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
operator|!
name|DOMSAMLUtil
operator|.
name|checkSenderVouches
argument_list|(
name|assertionWrapper
argument_list|,
name|tlsCerts
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Assertion fails sender-vouches requirements"
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
block|}
if|if
condition|(
name|signed
condition|)
block|{
name|Principal
name|principal
init|=
operator|(
name|Principal
operator|)
name|samlResults
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_PRINCIPAL
argument_list|)
decl_stmt|;
name|SecurityContext
name|sc
init|=
name|message
operator|.
name|get
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|sc
operator|==
literal|null
operator|||
name|sc
operator|.
name|getUserPrincipal
argument_list|()
operator|==
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|,
operator|new
name|DefaultSecurityContext
argument_list|(
name|principal
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|ex
parameter_list|)
block|{
throw|throw
name|WSS4JUtils
operator|.
name|createSoapFault
argument_list|(
name|message
argument_list|,
name|message
operator|.
name|getVersion
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
name|child
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|processToken
parameter_list|(
name|Element
name|tokenElement
parameter_list|,
specifier|final
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|RequestData
name|data
init|=
operator|new
name|CXFRequestData
argument_list|()
decl_stmt|;
name|Object
name|o
init|=
name|SecurityUtils
operator|.
name|getSecurityPropertyValue
argument_list|(
name|SecurityConstants
operator|.
name|CALLBACK_HANDLER
argument_list|,
name|message
argument_list|)
decl_stmt|;
try|try
block|{
name|data
operator|.
name|setCallbackHandler
argument_list|(
name|SecurityUtils
operator|.
name|getCallbackHandler
argument_list|(
name|o
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
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|FAILURE
argument_list|,
name|ex
argument_list|)
throw|;
block|}
name|data
operator|.
name|setMsgContext
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|data
operator|.
name|setWssConfig
argument_list|(
name|WSSConfig
operator|.
name|getNewInstance
argument_list|()
argument_list|)
expr_stmt|;
name|data
operator|.
name|setSigVerCrypto
argument_list|(
name|getCrypto
argument_list|(
literal|null
argument_list|,
name|SecurityConstants
operator|.
name|SIGNATURE_CRYPTO
argument_list|,
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
argument_list|,
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|WSDocInfo
name|wsDocInfo
init|=
operator|new
name|WSDocInfo
argument_list|(
name|tokenElement
operator|.
name|getOwnerDocument
argument_list|()
argument_list|)
decl_stmt|;
name|data
operator|.
name|setWsDocInfo
argument_list|(
name|wsDocInfo
argument_list|)
expr_stmt|;
name|SAMLTokenProcessor
name|p
init|=
operator|new
name|SAMLTokenProcessor
argument_list|()
decl_stmt|;
return|return
name|p
operator|.
name|handleToken
argument_list|(
name|tokenElement
argument_list|,
name|data
argument_list|)
return|;
block|}
specifier|protected
name|AbstractToken
name|assertTokens
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
name|AssertionInfoMap
name|aim
init|=
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
literal|"WssSamlV11Token10"
argument_list|)
expr_stmt|;
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
literal|"WssSamlV11Token11"
argument_list|)
expr_stmt|;
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
literal|"WssSamlV20Token11"
argument_list|)
expr_stmt|;
return|return
name|assertTokens
argument_list|(
name|message
argument_list|,
name|SPConstants
operator|.
name|SAML_TOKEN
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|protected
name|void
name|addToken
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
name|WSSConfig
operator|.
name|init
argument_list|()
expr_stmt|;
name|SamlToken
name|tok
init|=
operator|(
name|SamlToken
operator|)
name|assertTokens
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|Header
name|h
init|=
name|findSecurityHeader
argument_list|(
name|message
argument_list|,
literal|true
argument_list|)
decl_stmt|;
try|try
block|{
name|SamlAssertionWrapper
name|wrapper
init|=
name|addSamlToken
argument_list|(
name|tok
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|wrapper
operator|==
literal|null
condition|)
block|{
name|AssertionInfoMap
name|aim
init|=
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|PolicyUtils
operator|.
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|SAML_TOKEN
argument_list|)
decl_stmt|;
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
if|if
condition|(
name|ai
operator|.
name|isAsserted
argument_list|()
condition|)
block|{
name|ai
operator|.
name|setAsserted
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
return|return;
block|}
name|Element
name|el
init|=
operator|(
name|Element
operator|)
name|h
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|el
operator|=
operator|(
name|Element
operator|)
name|DOMUtils
operator|.
name|getDomElement
argument_list|(
name|el
argument_list|)
expr_stmt|;
name|el
operator|.
name|appendChild
argument_list|(
name|wrapper
operator|.
name|toDOM
argument_list|(
name|el
operator|.
name|getOwnerDocument
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|ex
parameter_list|)
block|{
name|policyNotAsserted
argument_list|(
name|tok
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|SamlAssertionWrapper
name|addSamlToken
parameter_list|(
name|SamlToken
name|token
parameter_list|,
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|WSSecurityException
block|{
comment|//
comment|// Get the SAML CallbackHandler
comment|//
name|Object
name|o
init|=
name|SecurityUtils
operator|.
name|getSecurityPropertyValue
argument_list|(
name|SecurityConstants
operator|.
name|SAML_CALLBACK_HANDLER
argument_list|,
name|message
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
if|if
condition|(
name|handler
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|AssertionInfoMap
name|aim
init|=
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
name|SAMLCallback
name|samlCallback
init|=
operator|new
name|SAMLCallback
argument_list|()
decl_stmt|;
name|SamlTokenType
name|tokenType
init|=
name|token
operator|.
name|getSamlTokenType
argument_list|()
decl_stmt|;
if|if
condition|(
name|tokenType
operator|==
name|SamlTokenType
operator|.
name|WssSamlV11Token10
operator|||
name|tokenType
operator|==
name|SamlTokenType
operator|.
name|WssSamlV11Token11
condition|)
block|{
name|samlCallback
operator|.
name|setSamlVersion
argument_list|(
name|Version
operator|.
name|SAML_11
argument_list|)
expr_stmt|;
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
literal|"WssSamlV11Token10"
argument_list|)
expr_stmt|;
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
literal|"WssSamlV11Token11"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|tokenType
operator|==
name|SamlTokenType
operator|.
name|WssSamlV20Token11
condition|)
block|{
name|samlCallback
operator|.
name|setSamlVersion
argument_list|(
name|Version
operator|.
name|SAML_20
argument_list|)
expr_stmt|;
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
literal|"WssSamlV20Token11"
argument_list|)
expr_stmt|;
block|}
name|SAMLUtil
operator|.
name|doSAMLCallback
argument_list|(
name|handler
argument_list|,
name|samlCallback
argument_list|)
expr_stmt|;
name|SamlAssertionWrapper
name|assertion
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|samlCallback
argument_list|)
decl_stmt|;
if|if
condition|(
name|samlCallback
operator|.
name|isSignAssertion
argument_list|()
condition|)
block|{
name|String
name|issuerName
init|=
name|samlCallback
operator|.
name|getIssuerKeyName
argument_list|()
decl_stmt|;
if|if
condition|(
name|issuerName
operator|==
literal|null
condition|)
block|{
name|String
name|userNameKey
init|=
name|SecurityConstants
operator|.
name|SIGNATURE_USERNAME
decl_stmt|;
name|issuerName
operator|=
operator|(
name|String
operator|)
name|SecurityUtils
operator|.
name|getSecurityPropertyValue
argument_list|(
name|userNameKey
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
name|String
name|password
init|=
name|samlCallback
operator|.
name|getIssuerKeyPassword
argument_list|()
decl_stmt|;
if|if
condition|(
name|password
operator|==
literal|null
condition|)
block|{
name|password
operator|=
operator|(
name|String
operator|)
name|SecurityUtils
operator|.
name|getSecurityPropertyValue
argument_list|(
name|SecurityConstants
operator|.
name|PASSWORD
argument_list|,
name|message
argument_list|)
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|password
argument_list|)
condition|)
block|{
name|password
operator|=
name|getPassword
argument_list|(
name|issuerName
argument_list|,
name|token
argument_list|,
name|WSPasswordCallback
operator|.
name|SIGNATURE
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
block|}
name|Crypto
name|crypto
init|=
name|samlCallback
operator|.
name|getIssuerCrypto
argument_list|()
decl_stmt|;
if|if
condition|(
name|crypto
operator|==
literal|null
condition|)
block|{
name|crypto
operator|=
name|getCrypto
argument_list|(
name|token
argument_list|,
name|SecurityConstants
operator|.
name|SIGNATURE_CRYPTO
argument_list|,
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
name|assertion
operator|.
name|signAssertion
argument_list|(
name|issuerName
argument_list|,
name|password
argument_list|,
name|crypto
argument_list|,
name|samlCallback
operator|.
name|isSendKeyValue
argument_list|()
argument_list|,
name|samlCallback
operator|.
name|getCanonicalizationAlgorithm
argument_list|()
argument_list|,
name|samlCallback
operator|.
name|getSignatureAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|assertion
return|;
block|}
specifier|private
name|Crypto
name|getCrypto
parameter_list|(
name|SamlToken
name|samlToken
parameter_list|,
name|String
name|cryptoKey
parameter_list|,
name|String
name|propKey
parameter_list|,
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|Crypto
name|crypto
init|=
operator|(
name|Crypto
operator|)
name|SecurityUtils
operator|.
name|getSecurityPropertyValue
argument_list|(
name|cryptoKey
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|crypto
operator|!=
literal|null
condition|)
block|{
return|return
name|crypto
return|;
block|}
name|Object
name|o
init|=
name|SecurityUtils
operator|.
name|getSecurityPropertyValue
argument_list|(
name|propKey
argument_list|,
name|message
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
name|URL
name|propsURL
init|=
name|SecurityUtils
operator|.
name|loadResource
argument_list|(
name|message
argument_list|,
name|o
argument_list|)
decl_stmt|;
name|Properties
name|properties
init|=
name|WSS4JUtils
operator|.
name|getProps
argument_list|(
name|o
argument_list|,
name|propsURL
argument_list|)
decl_stmt|;
if|if
condition|(
name|properties
operator|!=
literal|null
condition|)
block|{
name|PasswordEncryptor
name|passwordEncryptor
init|=
name|WSS4JUtils
operator|.
name|getPasswordEncryptor
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|crypto
operator|=
name|CryptoFactory
operator|.
name|getInstance
argument_list|(
name|properties
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|,
name|passwordEncryptor
argument_list|)
expr_stmt|;
block|}
return|return
name|crypto
return|;
block|}
comment|/**      * Check the policy version against the received assertion      */
specifier|private
name|boolean
name|checkVersion
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|SamlToken
name|samlToken
parameter_list|,
name|SamlAssertionWrapper
name|assertionWrapper
parameter_list|)
block|{
name|SamlTokenType
name|tokenType
init|=
name|samlToken
operator|.
name|getSamlTokenType
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|tokenType
operator|==
name|SamlTokenType
operator|.
name|WssSamlV11Token10
operator|||
name|tokenType
operator|==
name|SamlTokenType
operator|.
name|WssSamlV11Token11
operator|)
operator|&&
name|assertionWrapper
operator|.
name|getSamlVersion
argument_list|()
operator|!=
name|SAMLVersion
operator|.
name|VERSION_11
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|tokenType
operator|==
name|SamlTokenType
operator|.
name|WssSamlV20Token11
operator|&&
name|assertionWrapper
operator|.
name|getSamlVersion
argument_list|()
operator|!=
name|SAMLVersion
operator|.
name|VERSION_20
condition|)
block|{
return|return
literal|false
return|;
block|}
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
operator|new
name|QName
argument_list|(
name|samlToken
operator|.
name|getVersion
argument_list|()
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|tokenType
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

