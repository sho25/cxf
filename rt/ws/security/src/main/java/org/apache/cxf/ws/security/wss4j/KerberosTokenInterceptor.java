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
name|security
operator|.
name|Principal
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
name|List
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
name|Fault
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
name|tokenstore
operator|.
name|SecurityToken
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
name|tokenstore
operator|.
name|TokenStore
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
name|BinarySecurityTokenProcessor
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
name|validate
operator|.
name|Validator
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

begin_comment
comment|/**  * An interceptor to add a Kerberos token to the security header of an outbound request, and to  * process a Kerberos Token on an inbound request. It takes the Kerberos Token from the message   * context on the outbound side, where it was previously placed by the   * KerberosTokenInterceptorProvider.  */
end_comment

begin_class
specifier|public
class|class
name|KerberosTokenInterceptor
extends|extends
name|AbstractTokenInterceptor
block|{
specifier|public
name|KerberosTokenInterceptor
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
name|WSConstants
operator|.
name|BINARY_TOKEN_LN
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|&&
name|WSConstants
operator|.
name|WSSE_NS
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
try|try
block|{
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|bstResults
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
name|bstResults
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
argument_list|<
name|WSHandlerResult
argument_list|>
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
name|WSHandlerResult
name|rResult
init|=
operator|new
name|WSHandlerResult
argument_list|(
literal|null
argument_list|,
name|bstResults
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
name|assertTokens
argument_list|(
name|message
argument_list|,
name|SPConstants
operator|.
name|KERBEROS_TOKEN
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|Principal
name|principal
init|=
operator|(
name|Principal
operator|)
name|bstResults
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
name|message
operator|.
name|put
argument_list|(
name|WSS4JInInterceptor
operator|.
name|PRINCIPAL_RESULT
argument_list|,
name|principal
argument_list|)
expr_stmt|;
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
catch|catch
parameter_list|(
name|WSSecurityException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
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
name|RequestData
name|data
init|=
operator|new
name|RequestData
argument_list|()
block|{
specifier|public
name|CallbackHandler
name|getCallbackHandler
parameter_list|()
block|{
return|return
name|getCallback
argument_list|(
name|message
argument_list|)
return|;
block|}
specifier|public
name|Validator
name|getValidator
parameter_list|(
name|QName
name|qName
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|String
name|key
init|=
name|SecurityConstants
operator|.
name|BST_TOKEN_VALIDATOR
decl_stmt|;
name|Object
name|o
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|key
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|o
operator|instanceof
name|Validator
condition|)
block|{
return|return
operator|(
name|Validator
operator|)
name|o
return|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|Class
condition|)
block|{
return|return
call|(
name|Validator
call|)
argument_list|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|o
argument_list|)
operator|.
name|newInstance
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
return|return
operator|(
name|Validator
operator|)
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|o
operator|.
name|toString
argument_list|()
argument_list|,
name|KerberosTokenInterceptor
operator|.
name|class
argument_list|)
operator|.
name|newInstance
argument_list|()
return|;
block|}
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|t
parameter_list|)
block|{
throw|throw
name|t
throw|;
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
return|return
name|super
operator|.
name|getValidator
argument_list|(
name|qName
argument_list|)
return|;
block|}
block|}
decl_stmt|;
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
name|BinarySecurityTokenProcessor
name|p
init|=
operator|new
name|BinarySecurityTokenProcessor
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|results
init|=
name|p
operator|.
name|handleToken
argument_list|(
name|tokenElement
argument_list|,
name|data
argument_list|,
name|wsDocInfo
argument_list|)
decl_stmt|;
return|return
name|results
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
return|return
name|assertTokens
argument_list|(
name|message
argument_list|,
name|SPConstants
operator|.
name|KERBEROS_TOKEN
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
name|SecurityToken
name|securityToken
init|=
name|getSecurityToken
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|securityToken
operator|==
literal|null
operator|||
name|securityToken
operator|.
name|getToken
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// No SecurityToken so just return
return|return;
block|}
name|assertTokens
argument_list|(
name|message
argument_list|)
expr_stmt|;
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
operator|.
name|appendChild
argument_list|(
name|el
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|importNode
argument_list|(
name|securityToken
operator|.
name|getToken
argument_list|()
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|SecurityToken
name|getSecurityToken
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
comment|// Get the TokenStore
name|TokenStore
name|tokenStore
init|=
name|getTokenStore
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|tokenStore
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|id
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
return|return
name|tokenStore
operator|.
name|getToken
argument_list|(
name|id
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

