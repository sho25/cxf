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
name|servlet
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
name|MalformedURLException
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|InitialContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContext
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
name|ResourceResolver
import|;
end_import

begin_class
specifier|public
class|class
name|ServletContextResourceResolver
implements|implements
name|ResourceResolver
block|{
name|ServletContext
name|servletContext
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|URL
argument_list|>
name|urlMap
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|URL
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|ServletContextResourceResolver
parameter_list|(
name|ServletContext
name|sc
parameter_list|)
block|{
name|servletContext
operator|=
name|sc
expr_stmt|;
block|}
specifier|public
specifier|final
name|InputStream
name|getAsStream
parameter_list|(
specifier|final
name|String
name|string
parameter_list|)
block|{
if|if
condition|(
name|urlMap
operator|.
name|containsKey
argument_list|(
name|string
argument_list|)
condition|)
block|{
try|try
block|{
return|return
name|urlMap
operator|.
name|get
argument_list|(
name|string
argument_list|)
operator|.
name|openStream
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
return|return
name|servletContext
operator|.
name|getResourceAsStream
argument_list|(
name|string
argument_list|)
return|;
block|}
specifier|public
specifier|final
parameter_list|<
name|T
parameter_list|>
name|T
name|resolve
parameter_list|(
specifier|final
name|String
name|entryName
parameter_list|,
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|clz
parameter_list|)
block|{
name|Object
name|obj
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|entryName
operator|!=
literal|null
condition|)
block|{
name|InitialContext
name|ic
init|=
operator|new
name|InitialContext
argument_list|()
decl_stmt|;
try|try
block|{
name|obj
operator|=
name|ic
operator|.
name|lookup
argument_list|(
name|entryName
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|ic
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|NamingException
name|e
parameter_list|)
block|{
comment|//do nothing
block|}
if|if
condition|(
name|obj
operator|!=
literal|null
operator|&&
name|clz
operator|.
name|isInstance
argument_list|(
name|obj
argument_list|)
condition|)
block|{
return|return
name|clz
operator|.
name|cast
argument_list|(
name|obj
argument_list|)
return|;
block|}
if|if
condition|(
name|clz
operator|.
name|isAssignableFrom
argument_list|(
name|URL
operator|.
name|class
argument_list|)
condition|)
block|{
if|if
condition|(
name|urlMap
operator|.
name|containsKey
argument_list|(
name|entryName
argument_list|)
condition|)
block|{
return|return
name|clz
operator|.
name|cast
argument_list|(
name|urlMap
operator|.
name|get
argument_list|(
name|entryName
argument_list|)
argument_list|)
return|;
block|}
try|try
block|{
name|URL
name|url
init|=
name|servletContext
operator|.
name|getResource
argument_list|(
name|entryName
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|urlMap
operator|.
name|put
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|,
name|url
argument_list|)
expr_stmt|;
return|return
name|clz
operator|.
name|cast
argument_list|(
name|url
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
comment|//fallthrough
block|}
try|try
block|{
name|URL
name|url
init|=
name|servletContext
operator|.
name|getResource
argument_list|(
literal|"/"
operator|+
name|entryName
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|urlMap
operator|.
name|put
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|,
name|url
argument_list|)
expr_stmt|;
return|return
name|clz
operator|.
name|cast
argument_list|(
name|url
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e1
parameter_list|)
block|{
comment|//ignore
block|}
block|}
elseif|else
if|if
condition|(
name|clz
operator|.
name|isAssignableFrom
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
name|clz
operator|.
name|cast
argument_list|(
name|getAsStream
argument_list|(
name|entryName
argument_list|)
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

