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
operator|.
name|delegation
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
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|Callback
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
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|UnsupportedCallbackException
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
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|message
operator|.
name|token
operator|.
name|UsernameToken
import|;
end_import

begin_comment
comment|/**  * This CallbackHandler implementation obtains a username via the jaxws property   * "ws-security.username", as defined in SecurityConstants, and creates a wss UsernameToken   * (with no password) to be used as the delegation token.  */
end_comment

begin_class
specifier|public
class|class
name|WSSUsernameCallbackHandler
implements|implements
name|CallbackHandler
block|{
specifier|public
name|void
name|handle
parameter_list|(
name|Callback
index|[]
name|callbacks
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnsupportedCallbackException
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|callbacks
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|callbacks
index|[
name|i
index|]
operator|instanceof
name|DelegationCallback
condition|)
block|{
name|DelegationCallback
name|callback
init|=
operator|(
name|DelegationCallback
operator|)
name|callbacks
index|[
name|i
index|]
decl_stmt|;
name|Message
name|message
init|=
name|callback
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|String
name|username
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
name|USERNAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|username
operator|!=
literal|null
condition|)
block|{
name|Node
name|contentNode
init|=
name|message
operator|.
name|getContent
argument_list|(
name|Node
operator|.
name|class
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|contentNode
operator|!=
literal|null
condition|)
block|{
name|doc
operator|=
name|contentNode
operator|.
name|getOwnerDocument
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|doc
operator|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
expr_stmt|;
block|}
name|UsernameToken
name|usernameToken
init|=
name|createWSSEUsernameToken
argument_list|(
name|username
argument_list|,
name|doc
argument_list|)
decl_stmt|;
name|callback
operator|.
name|setToken
argument_list|(
name|usernameToken
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|UnsupportedCallbackException
argument_list|(
name|callbacks
index|[
name|i
index|]
argument_list|,
literal|"Unrecognized Callback"
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
name|UsernameToken
name|createWSSEUsernameToken
parameter_list|(
name|String
name|username
parameter_list|,
name|Document
name|doc
parameter_list|)
block|{
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
literal|null
argument_list|)
decl_stmt|;
name|usernameToken
operator|.
name|setName
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|usernameToken
operator|.
name|addWSUNamespace
argument_list|()
expr_stmt|;
name|usernameToken
operator|.
name|addWSSENamespace
argument_list|()
expr_stmt|;
name|usernameToken
operator|.
name|setID
argument_list|(
literal|"id-"
operator|+
name|username
argument_list|)
expr_stmt|;
return|return
name|usernameToken
return|;
block|}
block|}
end_class

end_unit

