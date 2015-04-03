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
name|sts
operator|.
name|distributed_caching
package|;
end_package

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
name|sts
operator|.
name|token
operator|.
name|provider
operator|.
name|TokenProvider
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
name|sts
operator|.
name|token
operator|.
name|provider
operator|.
name|TokenProviderParameters
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
name|sts
operator|.
name|token
operator|.
name|provider
operator|.
name|TokenProviderResponse
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
name|sts
operator|.
name|provider
operator|.
name|STSException
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
name|message
operator|.
name|token
operator|.
name|UsernameToken
import|;
end_import

begin_comment
comment|/**  * A TokenProvider implementation that creates a UsernameToken.  */
end_comment

begin_class
specifier|public
class|class
name|CustomUsernameTokenProvider
implements|implements
name|TokenProvider
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TOKEN_TYPE
init|=
literal|"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#UsernameToken"
decl_stmt|;
specifier|public
name|boolean
name|canHandleToken
parameter_list|(
name|String
name|tokenType
parameter_list|)
block|{
return|return
name|TOKEN_TYPE
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|canHandleToken
parameter_list|(
name|String
name|tokenType
parameter_list|,
name|String
name|realm
parameter_list|)
block|{
return|return
name|canHandleToken
argument_list|(
name|tokenType
argument_list|)
return|;
block|}
specifier|public
name|TokenProviderResponse
name|createToken
parameter_list|(
name|TokenProviderParameters
name|tokenParameters
parameter_list|)
block|{
try|try
block|{
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
comment|// Mock up a UsernameToken
name|UsernameToken
name|usernameToken
init|=
operator|new
name|UsernameToken
argument_list|(
literal|true
argument_list|,
name|doc
argument_list|,
name|WSConstants
operator|.
name|PASSWORD_TEXT
argument_list|)
decl_stmt|;
name|usernameToken
operator|.
name|setName
argument_list|(
literal|"alice"
argument_list|)
expr_stmt|;
name|usernameToken
operator|.
name|setPassword
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
name|String
name|id
init|=
literal|"UT-1234"
decl_stmt|;
name|usernameToken
operator|.
name|addWSSENamespace
argument_list|()
expr_stmt|;
name|usernameToken
operator|.
name|addWSUNamespace
argument_list|()
expr_stmt|;
name|usernameToken
operator|.
name|setID
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|TokenProviderResponse
name|response
init|=
operator|new
name|TokenProviderResponse
argument_list|()
decl_stmt|;
name|response
operator|.
name|setToken
argument_list|(
name|usernameToken
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|.
name|setTokenId
argument_list|(
name|id
argument_list|)
expr_stmt|;
comment|// Store the token in the cache
if|if
condition|(
name|tokenParameters
operator|.
name|getTokenStore
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|SecurityToken
name|securityToken
init|=
operator|new
name|SecurityToken
argument_list|(
name|usernameToken
operator|.
name|getID
argument_list|()
argument_list|)
decl_stmt|;
name|securityToken
operator|.
name|setToken
argument_list|(
name|usernameToken
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|hashCode
init|=
name|usernameToken
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|String
name|identifier
init|=
name|Integer
operator|.
name|toString
argument_list|(
name|hashCode
argument_list|)
decl_stmt|;
name|securityToken
operator|.
name|setTokenHash
argument_list|(
name|hashCode
argument_list|)
expr_stmt|;
name|tokenParameters
operator|.
name|getTokenStore
argument_list|()
operator|.
name|add
argument_list|(
name|identifier
argument_list|,
name|securityToken
argument_list|)
expr_stmt|;
block|}
return|return
name|response
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Can't serialize SAML assertion"
argument_list|,
name|e
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

