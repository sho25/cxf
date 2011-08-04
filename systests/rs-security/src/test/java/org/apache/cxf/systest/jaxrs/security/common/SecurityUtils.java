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
name|common
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
name|WSPasswordCallback
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

begin_class
specifier|public
specifier|final
class|class
name|SecurityUtils
block|{
specifier|private
name|SecurityUtils
parameter_list|()
block|{              }
specifier|public
specifier|static
name|Crypto
name|getCrypto
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|cryptoKey
parameter_list|,
name|String
name|propKey
parameter_list|)
throws|throws
name|IOException
throws|,
name|WSSecurityException
block|{
return|return
operator|new
name|CryptoLoader
argument_list|()
operator|.
name|getCrypto
argument_list|(
name|message
argument_list|,
name|cryptoKey
argument_list|,
name|propKey
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getUserName
parameter_list|(
name|Message
name|message
parameter_list|,
name|Crypto
name|crypto
parameter_list|,
name|String
name|userNameKey
parameter_list|)
block|{
name|String
name|user
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|userNameKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|crypto
operator|!=
literal|null
operator|&&
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|user
argument_list|)
condition|)
block|{
try|try
block|{
name|user
operator|=
name|crypto
operator|.
name|getDefaultX509Identifier
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|e1
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e1
argument_list|)
throw|;
block|}
block|}
return|return
name|user
return|;
block|}
specifier|public
specifier|static
name|String
name|getPassword
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|userName
parameter_list|,
name|int
name|type
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|callingClass
parameter_list|)
block|{
name|CallbackHandler
name|handler
init|=
name|getCallbackHandler
argument_list|(
name|message
argument_list|,
name|callingClass
argument_list|)
decl_stmt|;
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
name|WSPasswordCallback
index|[]
name|cb
init|=
block|{
operator|new
name|WSPasswordCallback
argument_list|(
name|userName
argument_list|,
name|type
argument_list|)
block|}
decl_stmt|;
try|try
block|{
name|handler
operator|.
name|handle
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
comment|//get the password
name|String
name|password
init|=
name|cb
index|[
literal|0
index|]
operator|.
name|getPassword
argument_list|()
decl_stmt|;
return|return
name|password
operator|==
literal|null
condition|?
literal|""
else|:
name|password
return|;
block|}
specifier|public
specifier|static
name|CallbackHandler
name|getCallbackHandler
parameter_list|(
name|Message
name|message
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|callingClass
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
name|callingClass
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
block|}
end_class

end_unit

