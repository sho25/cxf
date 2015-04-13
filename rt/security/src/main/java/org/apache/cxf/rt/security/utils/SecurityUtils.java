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
name|rt
operator|.
name|security
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

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
name|URI
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
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
operator|.
name|ClassLoaderHolder
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
name|phase
operator|.
name|PhaseInterceptorChain
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

begin_comment
comment|/**  * Some common functionality  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|SecurityUtils
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
name|SecurityUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|SecurityUtils
parameter_list|()
block|{
comment|// complete
block|}
specifier|public
specifier|static
name|CallbackHandler
name|getCallbackHandler
parameter_list|(
name|Object
name|o
parameter_list|)
throws|throws
name|InstantiationException
throws|,
name|IllegalAccessException
throws|,
name|ClassNotFoundException
block|{
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
name|SecurityUtils
operator|.
name|class
argument_list|)
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
return|return
name|handler
return|;
block|}
specifier|public
specifier|static
name|URL
name|getConfigFileURL
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|configFileKey
parameter_list|,
name|String
name|configFileDefault
parameter_list|)
block|{
name|Object
name|o
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|configFileKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
name|o
operator|=
name|configFileDefault
expr_stmt|;
block|}
return|return
name|loadResource
argument_list|(
name|message
argument_list|,
name|o
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|URL
name|loadResource
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|loadResource
argument_list|(
operator|(
name|Message
operator|)
literal|null
argument_list|,
name|o
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|URL
name|loadResource
parameter_list|(
name|Message
name|message
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
name|Message
name|msg
init|=
name|message
decl_stmt|;
if|if
condition|(
name|msg
operator|==
literal|null
condition|)
block|{
name|msg
operator|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
expr_stmt|;
block|}
name|ResourceManager
name|manager
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|msg
operator|!=
literal|null
operator|&&
name|msg
operator|.
name|getExchange
argument_list|()
operator|!=
literal|null
operator|&&
name|msg
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
operator|!=
literal|null
condition|)
block|{
name|manager
operator|=
name|msg
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
expr_stmt|;
block|}
return|return
name|loadResource
argument_list|(
name|manager
argument_list|,
name|o
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|URL
name|loadResource
parameter_list|(
name|ResourceManager
name|manager
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
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
name|SecurityUtils
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
return|return
name|url
return|;
block|}
name|ClassLoaderHolder
name|orig
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|manager
operator|!=
literal|null
condition|)
block|{
name|ClassLoader
name|loader
init|=
name|manager
operator|.
name|resolveResource
argument_list|(
operator|(
name|String
operator|)
name|o
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
name|orig
operator|=
name|ClassLoaderUtils
operator|.
name|setThreadContextClassloader
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
name|IOException
name|e
parameter_list|)
block|{
comment|// Do nothing
block|}
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
name|URI
name|propResourceUri
init|=
name|URI
operator|.
name|create
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|)
decl_stmt|;
if|if
condition|(
name|propResourceUri
operator|.
name|getScheme
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|url
operator|=
name|propResourceUri
operator|.
name|toURL
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|propResourceUri
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|.
name|exists
argument_list|()
condition|)
block|{
name|url
operator|=
name|f
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|// Do nothing
block|}
block|}
return|return
name|url
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|orig
operator|!=
literal|null
condition|)
block|{
name|orig
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
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
return|return
operator|(
name|URL
operator|)
name|o
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|Properties
name|loadProperties
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|Properties
condition|)
block|{
return|return
operator|(
name|Properties
operator|)
name|o
return|;
block|}
name|URL
name|url
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
name|url
operator|=
name|SecurityUtils
operator|.
name|loadResource
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|URL
condition|)
block|{
name|url
operator|=
operator|(
name|URL
operator|)
name|o
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
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
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
name|LOG
operator|.
name|fine
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|properties
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

