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
name|tools
operator|.
name|util
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
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLStreamHandler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|URLFactory
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PROTOCOL_HANDLER_PKGS
init|=
literal|"java.protocol.handler.pkgs"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|UNKNOWN_PROPTOCL_EX_MSG
init|=
literal|"unknown protocol: "
decl_stmt|;
specifier|private
name|URLFactory
parameter_list|()
block|{              }
specifier|public
specifier|static
name|URL
name|createURL
parameter_list|(
name|String
name|spec
parameter_list|)
throws|throws
name|MalformedURLException
block|{
return|return
name|createURL
argument_list|(
literal|null
argument_list|,
name|spec
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|URL
name|createURL
parameter_list|(
name|URL
name|context
parameter_list|,
name|String
name|spec
parameter_list|)
throws|throws
name|MalformedURLException
block|{
name|URL
name|url
init|=
literal|null
decl_stmt|;
try|try
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
name|context
argument_list|,
name|spec
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|mue
parameter_list|)
block|{
name|String
name|msg
init|=
name|mue
operator|.
name|getMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|msg
operator|.
name|indexOf
argument_list|(
name|UNKNOWN_PROPTOCL_EX_MSG
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|URLStreamHandler
name|handler
init|=
name|findHandler
argument_list|(
name|msg
operator|.
name|substring
argument_list|(
name|UNKNOWN_PROPTOCL_EX_MSG
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|handler
operator|!=
literal|null
condition|)
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
name|context
argument_list|,
name|spec
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
throw|throw
name|mue
throw|;
block|}
block|}
return|return
name|url
return|;
block|}
specifier|public
specifier|static
name|URLStreamHandler
name|findHandler
parameter_list|(
name|String
name|protocol
parameter_list|)
block|{
name|URLStreamHandler
name|handler
init|=
literal|null
decl_stmt|;
name|String
name|packagePrefixList
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|PROTOCOL_HANDLER_PKGS
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|StringTokenizer
name|packagePrefixIter
init|=
operator|new
name|StringTokenizer
argument_list|(
name|packagePrefixList
argument_list|,
literal|"|"
argument_list|)
decl_stmt|;
while|while
condition|(
name|handler
operator|==
literal|null
operator|&&
name|packagePrefixIter
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|String
name|packagePrefix
init|=
name|packagePrefixIter
operator|.
name|nextToken
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
try|try
block|{
name|String
name|clsName
init|=
name|packagePrefix
operator|+
literal|"."
operator|+
name|protocol
operator|+
literal|".Handler"
decl_stmt|;
name|Class
name|cls
init|=
literal|null
decl_stmt|;
try|try
block|{
name|cls
operator|=
name|Class
operator|.
name|forName
argument_list|(
name|clsName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
name|ClassLoader
name|cl
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|cl
operator|!=
literal|null
condition|)
block|{
name|cls
operator|=
name|cl
operator|.
name|loadClass
argument_list|(
name|clsName
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|cls
operator|!=
literal|null
condition|)
block|{
name|handler
operator|=
operator|(
name|URLStreamHandler
operator|)
name|cls
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ignored
parameter_list|)
block|{
name|ignored
operator|.
name|getMessage
argument_list|()
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

