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
name|util
operator|.
name|KeyUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|utils
operator|.
name|Base64
import|;
end_import

begin_class
specifier|public
class|class
name|TokenStoreCallbackHandler
implements|implements
name|CallbackHandler
block|{
specifier|private
name|CallbackHandler
name|internal
decl_stmt|;
specifier|private
name|TokenStore
name|store
decl_stmt|;
specifier|public
name|TokenStoreCallbackHandler
parameter_list|(
name|CallbackHandler
name|in
parameter_list|,
name|TokenStore
name|st
parameter_list|)
block|{
name|internal
operator|=
name|in
expr_stmt|;
name|store
operator|=
name|st
expr_stmt|;
block|}
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
name|Callback
name|callback
range|:
name|callbacks
control|)
block|{
if|if
condition|(
name|callback
operator|instanceof
name|WSPasswordCallback
condition|)
block|{
name|WSPasswordCallback
name|pc
init|=
operator|(
name|WSPasswordCallback
operator|)
name|callback
decl_stmt|;
name|String
name|id
init|=
name|pc
operator|.
name|getIdentifier
argument_list|()
decl_stmt|;
name|SecurityToken
name|tok
init|=
name|store
operator|.
name|getToken
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|tok
operator|!=
literal|null
operator|&&
operator|!
name|tok
operator|.
name|isExpired
argument_list|()
condition|)
block|{
if|if
condition|(
name|tok
operator|.
name|getSHA1
argument_list|()
operator|==
literal|null
operator|&&
name|pc
operator|.
name|getKey
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|tok
operator|.
name|setSHA1
argument_list|(
name|getSHA1
argument_list|(
name|pc
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// Create another cache entry with the SHA1 Identifier as the key for easy retrieval
name|store
operator|.
name|add
argument_list|(
name|tok
operator|.
name|getSHA1
argument_list|()
argument_list|,
name|tok
argument_list|)
expr_stmt|;
block|}
name|pc
operator|.
name|setKey
argument_list|(
name|tok
operator|.
name|getSecret
argument_list|()
argument_list|)
expr_stmt|;
name|pc
operator|.
name|setKey
argument_list|(
name|tok
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|pc
operator|.
name|setCustomToken
argument_list|(
name|tok
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
if|if
condition|(
name|internal
operator|!=
literal|null
condition|)
block|{
name|internal
operator|.
name|handle
argument_list|(
name|callbacks
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|String
name|getSHA1
parameter_list|(
name|byte
index|[]
name|input
parameter_list|)
block|{
try|try
block|{
name|byte
index|[]
name|digestBytes
init|=
name|KeyUtils
operator|.
name|generateDigest
argument_list|(
name|input
argument_list|)
decl_stmt|;
return|return
name|Base64
operator|.
name|encode
argument_list|(
name|digestBytes
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|e
parameter_list|)
block|{
comment|//REVISIT
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

