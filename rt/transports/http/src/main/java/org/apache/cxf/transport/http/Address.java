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
name|transport
operator|.
name|http
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Proxy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|ProxySelector
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
name|URISyntaxException
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
name|Iterator
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
name|injection
operator|.
name|NoJSR250Annotations
import|;
end_import

begin_comment
comment|/**  * A convenient class for storing URI and URL representation of an address and avoid useless conversions.  * A proxy for the current address is also lazily resolved and stored; most of the times, that proxy can  * be used to prevent the HttpURLConnection from computing the proxy when the connection is opened.  *  * The class is thread-safe.  */
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
specifier|final
class|class
name|Address
block|{
specifier|private
specifier|final
name|String
name|str
decl_stmt|;
specifier|private
specifier|final
name|URI
name|uri
decl_stmt|;
specifier|private
specifier|volatile
name|URL
name|url
decl_stmt|;
specifier|private
specifier|volatile
name|Proxy
name|defaultProxy
decl_stmt|;
specifier|public
name|Address
parameter_list|(
name|String
name|str
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|this
operator|.
name|str
operator|=
name|str
expr_stmt|;
name|this
operator|.
name|uri
operator|=
operator|new
name|URI
argument_list|(
name|str
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Address
parameter_list|(
name|String
name|str
parameter_list|,
name|URI
name|uri
parameter_list|)
block|{
name|this
operator|.
name|str
operator|=
name|str
expr_stmt|;
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
specifier|public
name|URL
name|getURL
parameter_list|()
throws|throws
name|MalformedURLException
block|{
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
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
name|uri
operator|.
name|toURL
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|url
return|;
block|}
specifier|public
name|URI
name|getURI
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
specifier|public
name|String
name|getString
parameter_list|()
block|{
return|return
name|str
return|;
block|}
specifier|public
name|Proxy
name|getDefaultProxy
parameter_list|()
block|{
if|if
condition|(
name|defaultProxy
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|defaultProxy
operator|==
literal|null
condition|)
block|{
name|defaultProxy
operator|=
name|chooseProxy
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|defaultProxy
return|;
block|}
specifier|private
specifier|static
name|Proxy
name|chooseProxy
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|ProxySelector
name|sel
init|=
name|java
operator|.
name|security
operator|.
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|java
operator|.
name|security
operator|.
name|PrivilegedAction
argument_list|<
name|ProxySelector
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ProxySelector
name|run
parameter_list|()
block|{
return|return
name|ProxySelector
operator|.
name|getDefault
argument_list|()
return|;
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|sel
operator|==
literal|null
condition|)
block|{
return|return
name|Proxy
operator|.
name|NO_PROXY
return|;
block|}
comment|//detect usage of user-defined proxy and avoid optimizations in that case
if|if
condition|(
operator|!
literal|"sun.net.spi.DefaultProxySelector"
operator|.
name|equals
argument_list|(
name|sel
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Iterator
argument_list|<
name|Proxy
argument_list|>
name|it
init|=
name|sel
operator|.
name|select
argument_list|(
name|uri
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return
name|it
operator|.
name|next
argument_list|()
return|;
block|}
return|return
name|Proxy
operator|.
name|NO_PROXY
return|;
block|}
block|}
end_class

end_unit

