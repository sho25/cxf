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
name|tokenstore
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
name|net
operator|.
name|URL
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

begin_comment
comment|/**  * An abstract factory to return a TokenStore instance. It returns an EHCacheTokenStoreFactory  * if EH-Cache is available. Otherwise it returns a MemoryTokenStoreFactory.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|TokenStoreFactory
block|{
specifier|private
specifier|static
name|boolean
name|ehCacheInstalled
decl_stmt|;
static|static
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cacheManagerClass
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"net.sf.ehcache.CacheManager"
argument_list|)
decl_stmt|;
if|if
condition|(
name|cacheManagerClass
operator|!=
literal|null
condition|)
block|{
name|ehCacheInstalled
operator|=
literal|true
expr_stmt|;
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
block|}
specifier|protected
specifier|static
specifier|synchronized
name|boolean
name|isEhCacheInstalled
parameter_list|()
block|{
return|return
name|ehCacheInstalled
return|;
block|}
specifier|public
specifier|static
name|TokenStoreFactory
name|newInstance
parameter_list|()
block|{
if|if
condition|(
name|isEhCacheInstalled
argument_list|()
condition|)
block|{
return|return
operator|new
name|EHCacheTokenStoreFactory
argument_list|()
return|;
block|}
return|return
operator|new
name|MemoryTokenStoreFactory
argument_list|()
return|;
block|}
specifier|public
specifier|abstract
name|TokenStore
name|newTokenStore
parameter_list|(
name|String
name|key
parameter_list|,
name|Message
name|message
parameter_list|)
function_decl|;
specifier|protected
name|URL
name|getConfigFileURL
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
name|CACHE_CONFIG_FILE
argument_list|)
decl_stmt|;
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
literal|null
decl_stmt|;
name|ResourceManager
name|rm
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
name|url
operator|=
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
expr_stmt|;
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
name|TokenStoreFactory
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
return|return
name|url
return|;
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
block|}
end_class

end_unit

