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
name|bus
operator|.
name|spring
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|BeansException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|NoSuchBeanDefinitionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContextAware
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|Resource
import|;
end_import

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
class|class
name|BusApplicationContextResourceResolver
implements|implements
name|ResourceResolver
implements|,
name|ApplicationContextAware
block|{
name|ApplicationContext
name|context
decl_stmt|;
specifier|public
name|BusApplicationContextResourceResolver
parameter_list|()
block|{     }
specifier|public
name|BusApplicationContextResourceResolver
parameter_list|(
name|ApplicationContext
name|c
parameter_list|)
block|{
name|context
operator|=
name|c
expr_stmt|;
block|}
specifier|public
name|InputStream
name|getAsStream
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Resource
name|r
init|=
name|context
operator|.
name|getResource
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|!=
literal|null
operator|&&
name|r
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
return|return
name|r
operator|.
name|getInputStream
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//ignore and return null
block|}
block|}
name|r
operator|=
name|context
operator|.
name|getResource
argument_list|(
literal|"/"
operator|+
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|r
operator|!=
literal|null
operator|&&
name|r
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
return|return
name|r
operator|.
name|getInputStream
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//ignore and return null
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|resolve
parameter_list|(
name|String
name|resourceName
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|resourceType
parameter_list|)
block|{
if|if
condition|(
name|resourceName
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
return|return
name|resourceType
operator|.
name|cast
argument_list|(
name|context
operator|.
name|getBean
argument_list|(
name|resourceName
argument_list|,
name|resourceType
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchBeanDefinitionException
name|def
parameter_list|)
block|{
comment|//ignore
block|}
try|try
block|{
if|if
condition|(
name|ClassLoader
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|resourceType
argument_list|)
condition|)
block|{
return|return
name|resourceType
operator|.
name|cast
argument_list|(
name|context
operator|.
name|getClassLoader
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|URL
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|resourceType
argument_list|)
condition|)
block|{
name|Resource
name|r
init|=
name|context
operator|.
name|getResource
argument_list|(
name|resourceName
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|!=
literal|null
operator|&&
name|r
operator|.
name|exists
argument_list|()
condition|)
block|{
name|r
operator|.
name|getInputStream
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
comment|//checks to see if the URL really can resolve
return|return
name|resourceType
operator|.
name|cast
argument_list|(
name|r
operator|.
name|getURL
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
throws|throws
name|BeansException
block|{
name|context
operator|=
name|applicationContext
expr_stmt|;
block|}
block|}
end_class

end_unit

