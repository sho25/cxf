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
name|xkms
operator|.
name|crypto
operator|.
name|impl
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
name|net
operator|.
name|URL
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
name|xkms
operator|.
name|crypto
operator|.
name|CryptoProviderException
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
name|Merlin
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
name|WSPasswordCallback
operator|.
name|Usage
import|;
end_import

begin_class
specifier|final
class|class
name|CryptoProviderUtils
block|{
specifier|private
name|CryptoProviderUtils
parameter_list|()
block|{     }
specifier|public
specifier|static
name|Properties
name|loadKeystoreProperties
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|propKey
parameter_list|)
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
throw|throw
operator|new
name|CryptoProviderException
argument_list|(
literal|"Keystore properties path is not defined"
argument_list|)
throw|;
block|}
name|Properties
name|properties
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|Properties
condition|)
block|{
name|properties
operator|=
operator|(
name|Properties
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
name|ResourceManager
name|rm
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
operator|.
name|getExtension
argument_list|(
name|ResourceManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|URL
name|url
init|=
name|rm
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
decl_stmt|;
try|try
block|{
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|url
operator|=
name|ClassLoaderUtils
operator|.
name|getResource
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|,
name|CryptoProviderUtils
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
block|}
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|InputStream
name|ins
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|properties
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|ins
argument_list|)
expr_stmt|;
name|ins
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|CryptoProviderException
argument_list|(
literal|"Keystore properties url is not resolved: "
operator|+
name|o
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CryptoProviderException
argument_list|(
literal|"Cannot load keystore properties: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|URL
condition|)
block|{
name|properties
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
try|try
block|{
name|InputStream
name|ins
init|=
operator|(
operator|(
name|URL
operator|)
name|o
operator|)
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|ins
argument_list|)
expr_stmt|;
name|ins
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CryptoProviderException
argument_list|(
literal|"Cannot load keystore properties: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|properties
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CryptoProviderException
argument_list|(
literal|"Cannot load keystore properties: "
operator|+
name|o
argument_list|)
throw|;
block|}
return|return
name|properties
return|;
block|}
specifier|public
specifier|static
name|String
name|getKeystoreAlias
parameter_list|(
name|Properties
name|keystoreProps
parameter_list|)
block|{
name|String
name|keystoreAlias
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|keystoreProps
operator|.
name|containsKey
argument_list|(
name|Merlin
operator|.
name|KEYSTORE_ALIAS
argument_list|)
condition|)
block|{
name|keystoreAlias
operator|=
name|keystoreProps
operator|.
name|getProperty
argument_list|(
name|Merlin
operator|.
name|KEYSTORE_ALIAS
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|keystoreAlias
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CryptoProviderException
argument_list|(
literal|"Alias is not found in keystore properties file: "
operator|+
name|Merlin
operator|.
name|KEYSTORE_ALIAS
argument_list|)
throw|;
block|}
return|return
name|keystoreAlias
return|;
block|}
specifier|public
specifier|static
name|CallbackHandler
name|getCallbackHandler
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
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
name|CryptoProviderUtils
operator|.
name|class
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
specifier|public
specifier|static
name|String
name|getCallbackPwdFromMessage
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|userName
parameter_list|,
name|Usage
name|usage
parameter_list|)
block|{
comment|// Then try to get the password from the given callback handler
name|CallbackHandler
name|handler
init|=
name|getCallbackHandler
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|handler
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CryptoProviderException
argument_list|(
literal|"No callback handler and no password available"
argument_list|)
throw|;
block|}
return|return
name|getCallbackPwd
argument_list|(
name|userName
argument_list|,
name|usage
argument_list|,
name|handler
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getCallbackPwd
parameter_list|(
name|String
name|userName
parameter_list|,
name|Usage
name|usage
parameter_list|,
name|CallbackHandler
name|handler
parameter_list|)
block|{
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
name|usage
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
throw|throw
operator|new
name|CryptoProviderException
argument_list|(
literal|"Cannot get password from callback: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// get the password
return|return
name|cb
index|[
literal|0
index|]
operator|.
name|getPassword
argument_list|()
return|;
block|}
block|}
end_class

end_unit

