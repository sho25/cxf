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
name|trust
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|logging
operator|.
name|Level
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
name|ws
operator|.
name|security
operator|.
name|tokenstore
operator|.
name|SecurityToken
import|;
end_import

begin_comment
comment|/**  * A extension of AbstractSTSClient to communicate with an STS and return a SecurityToken  * to the client.  */
end_comment

begin_class
specifier|public
class|class
name|STSClient
extends|extends
name|AbstractSTSClient
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
name|STSClient
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|STSClient
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|super
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SecurityToken
name|requestSecurityToken
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|requestSecurityToken
argument_list|(
literal|null
argument_list|)
return|;
block|}
specifier|public
name|SecurityToken
name|requestSecurityToken
parameter_list|(
name|String
name|appliesTo
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|requestSecurityToken
argument_list|(
name|appliesTo
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|SecurityToken
name|requestSecurityToken
parameter_list|(
name|String
name|appliesTo
parameter_list|,
name|String
name|binaryExchange
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|requestSecurityToken
argument_list|(
name|appliesTo
argument_list|,
literal|null
argument_list|,
literal|"/Issue"
argument_list|,
name|binaryExchange
argument_list|)
return|;
block|}
specifier|public
name|SecurityToken
name|requestSecurityToken
parameter_list|(
name|String
name|appliesTo
parameter_list|,
name|String
name|action
parameter_list|,
name|String
name|requestType
parameter_list|,
name|String
name|binaryExchange
parameter_list|)
throws|throws
name|Exception
block|{
name|STSResponse
name|response
init|=
name|issue
argument_list|(
name|appliesTo
argument_list|,
name|action
argument_list|,
name|requestType
argument_list|,
name|binaryExchange
argument_list|)
decl_stmt|;
name|SecurityToken
name|token
init|=
name|createSecurityToken
argument_list|(
name|getDocumentElement
argument_list|(
name|response
operator|.
name|getResponse
argument_list|()
argument_list|)
argument_list|,
name|response
operator|.
name|getEntropy
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|response
operator|.
name|getCert
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|token
operator|.
name|setX509Certificate
argument_list|(
name|response
operator|.
name|getCert
argument_list|()
argument_list|,
name|response
operator|.
name|getCrypto
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|token
operator|.
name|getTokenType
argument_list|()
operator|==
literal|null
condition|)
block|{
name|String
name|tokenTypeFromTemplate
init|=
name|getTokenTypeFromTemplate
argument_list|()
decl_stmt|;
if|if
condition|(
name|tokenTypeFromTemplate
operator|!=
literal|null
condition|)
block|{
name|token
operator|.
name|setTokenType
argument_list|(
name|tokenTypeFromTemplate
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|tokenType
operator|!=
literal|null
condition|)
block|{
name|token
operator|.
name|setTokenType
argument_list|(
name|tokenType
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|token
return|;
block|}
specifier|public
name|SecurityToken
name|renewSecurityToken
parameter_list|(
name|SecurityToken
name|tok
parameter_list|)
throws|throws
name|Exception
block|{
name|STSResponse
name|response
init|=
name|renew
argument_list|(
name|tok
argument_list|)
decl_stmt|;
name|SecurityToken
name|token
init|=
name|createSecurityToken
argument_list|(
name|getDocumentElement
argument_list|(
name|response
operator|.
name|getResponse
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|token
operator|.
name|getTokenType
argument_list|()
operator|==
literal|null
condition|)
block|{
name|String
name|tokenTypeFromTemplate
init|=
name|getTokenTypeFromTemplate
argument_list|()
decl_stmt|;
if|if
condition|(
name|tokenTypeFromTemplate
operator|!=
literal|null
condition|)
block|{
name|token
operator|.
name|setTokenType
argument_list|(
name|tokenTypeFromTemplate
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|tokenType
operator|!=
literal|null
condition|)
block|{
name|token
operator|.
name|setTokenType
argument_list|(
name|tokenType
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|token
return|;
block|}
specifier|public
name|List
argument_list|<
name|SecurityToken
argument_list|>
name|validateSecurityToken
parameter_list|(
name|SecurityToken
name|tok
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|validateTokenType
init|=
name|tokenType
decl_stmt|;
if|if
condition|(
name|validateTokenType
operator|==
literal|null
condition|)
block|{
name|validateTokenType
operator|=
name|namespace
operator|+
literal|"/RSTR/Status"
expr_stmt|;
block|}
return|return
name|validateSecurityToken
argument_list|(
name|tok
argument_list|,
name|validateTokenType
argument_list|)
return|;
block|}
specifier|protected
name|List
argument_list|<
name|SecurityToken
argument_list|>
name|validateSecurityToken
parameter_list|(
name|SecurityToken
name|tok
parameter_list|,
name|String
name|tokentype
parameter_list|)
throws|throws
name|Exception
block|{
name|STSResponse
name|response
init|=
name|validate
argument_list|(
name|tok
argument_list|,
name|tokentype
argument_list|)
decl_stmt|;
name|Element
name|el
init|=
name|getDocumentElement
argument_list|(
name|response
operator|.
name|getResponse
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"RequestSecurityTokenResponseCollection"
operator|.
name|equals
argument_list|(
name|el
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|el
operator|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|el
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
literal|"RequestSecurityTokenResponse"
operator|.
name|equals
argument_list|(
name|el
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
literal|"Unexpected element "
operator|+
name|el
operator|.
name|getLocalName
argument_list|()
argument_list|,
name|LOG
argument_list|)
throw|;
block|}
name|el
operator|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|el
argument_list|)
expr_stmt|;
name|String
name|reason
init|=
literal|null
decl_stmt|;
name|boolean
name|valid
init|=
literal|false
decl_stmt|;
name|List
argument_list|<
name|SecurityToken
argument_list|>
name|tokens
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
name|el
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"Status"
operator|.
name|equals
argument_list|(
name|el
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|Element
name|e2
init|=
name|DOMUtils
operator|.
name|getFirstChildWithName
argument_list|(
name|el
argument_list|,
name|el
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|"Code"
argument_list|)
decl_stmt|;
name|String
name|s
init|=
name|DOMUtils
operator|.
name|getContent
argument_list|(
name|e2
argument_list|)
decl_stmt|;
name|valid
operator|=
name|s
operator|.
name|endsWith
argument_list|(
literal|"/status/valid"
argument_list|)
expr_stmt|;
name|e2
operator|=
name|DOMUtils
operator|.
name|getFirstChildWithName
argument_list|(
name|el
argument_list|,
name|el
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|"Reason"
argument_list|)
expr_stmt|;
if|if
condition|(
name|e2
operator|!=
literal|null
condition|)
block|{
name|reason
operator|=
name|DOMUtils
operator|.
name|getContent
argument_list|(
name|e2
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"RequestedSecurityToken"
operator|.
name|equals
argument_list|(
name|el
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|SecurityToken
name|token
init|=
name|createSecurityToken
argument_list|(
name|getDocumentElement
argument_list|(
name|response
operator|.
name|getResponse
argument_list|()
argument_list|)
argument_list|,
name|response
operator|.
name|getEntropy
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|response
operator|.
name|getCert
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|token
operator|.
name|setX509Certificate
argument_list|(
name|response
operator|.
name|getCert
argument_list|()
argument_list|,
name|response
operator|.
name|getCrypto
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|token
operator|.
name|getTokenType
argument_list|()
operator|==
literal|null
condition|)
block|{
name|String
name|tokenTypeFromTemplate
init|=
name|getTokenTypeFromTemplate
argument_list|()
decl_stmt|;
if|if
condition|(
name|tokenTypeFromTemplate
operator|!=
literal|null
condition|)
block|{
name|token
operator|.
name|setTokenType
argument_list|(
name|tokenTypeFromTemplate
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|tokenType
operator|!=
literal|null
condition|)
block|{
name|token
operator|.
name|setTokenType
argument_list|(
name|tokenType
argument_list|)
expr_stmt|;
block|}
block|}
name|tokens
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
name|el
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|el
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|valid
condition|)
block|{
throw|throw
operator|new
name|TrustException
argument_list|(
name|LOG
argument_list|,
literal|"VALIDATION_FAILED"
argument_list|,
name|reason
argument_list|)
throw|;
block|}
if|if
condition|(
name|tokens
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|tokens
operator|.
name|add
argument_list|(
name|tok
argument_list|)
expr_stmt|;
block|}
return|return
name|tokens
return|;
block|}
specifier|public
name|boolean
name|cancelSecurityToken
parameter_list|(
name|SecurityToken
name|token
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|cancel
argument_list|(
name|token
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Problem cancelling token"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
specifier|private
name|String
name|getTokenTypeFromTemplate
parameter_list|()
block|{
if|if
condition|(
name|template
operator|!=
literal|null
operator|&&
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|template
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|Element
name|tl
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|template
argument_list|)
decl_stmt|;
while|while
condition|(
name|tl
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"TokenType"
operator|.
name|equals
argument_list|(
name|tl
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|DOMUtils
operator|.
name|getContent
argument_list|(
name|tl
argument_list|)
return|;
block|}
name|tl
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|tl
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

