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
name|helpers
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedAction
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
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|NamespaceContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPath
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathFactory
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
name|w3c
operator|.
name|dom
operator|.
name|NodeList
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

begin_class
specifier|public
class|class
name|XPathUtils
block|{
specifier|private
specifier|static
name|XPathFactory
name|xpathFactory
init|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
specifier|private
name|XPath
name|xpath
decl_stmt|;
specifier|public
name|XPathUtils
parameter_list|()
block|{
name|xpath
operator|=
name|xpathFactory
operator|.
name|newXPath
argument_list|()
expr_stmt|;
block|}
specifier|public
name|XPathUtils
parameter_list|(
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ns
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
if|if
condition|(
name|ns
operator|!=
literal|null
condition|)
block|{
name|xpath
operator|.
name|setNamespaceContext
argument_list|(
operator|new
name|MapNamespaceContext
argument_list|(
name|ns
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|XPathUtils
parameter_list|(
specifier|final
name|NamespaceContext
name|ctx
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|xpath
operator|.
name|setNamespaceContext
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Object
name|getValue
parameter_list|(
name|String
name|xpathExpression
parameter_list|,
name|Node
name|node
parameter_list|,
name|QName
name|type
parameter_list|)
block|{
name|ClassLoaderHolder
name|loader
init|=
name|ClassLoaderUtils
operator|.
name|setThreadContextClassloader
argument_list|(
name|getClassLoader
argument_list|(
name|xpath
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|xpath
operator|.
name|evaluate
argument_list|(
name|xpathExpression
argument_list|,
name|node
argument_list|,
name|type
argument_list|)
return|;
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
finally|finally
block|{
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
name|loader
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|NodeList
name|getValueList
parameter_list|(
name|String
name|xpathExpression
parameter_list|,
name|Node
name|node
parameter_list|)
block|{
return|return
operator|(
name|NodeList
operator|)
name|getValue
argument_list|(
name|xpathExpression
argument_list|,
name|node
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
return|;
block|}
specifier|public
name|String
name|getValueString
parameter_list|(
name|String
name|xpathExpression
parameter_list|,
name|Node
name|node
parameter_list|)
block|{
return|return
operator|(
name|String
operator|)
name|getValue
argument_list|(
name|xpathExpression
argument_list|,
name|node
argument_list|,
name|XPathConstants
operator|.
name|STRING
argument_list|)
return|;
block|}
specifier|public
name|Node
name|getValueNode
parameter_list|(
name|String
name|xpathExpression
parameter_list|,
name|Node
name|node
parameter_list|)
block|{
return|return
operator|(
name|Node
operator|)
name|getValue
argument_list|(
name|xpathExpression
argument_list|,
name|node
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isExist
parameter_list|(
name|String
name|xpathExpression
parameter_list|,
name|Node
name|node
parameter_list|,
name|QName
name|type
parameter_list|)
block|{
return|return
name|getValue
argument_list|(
name|xpathExpression
argument_list|,
name|node
argument_list|,
name|type
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|private
specifier|static
name|ClassLoader
name|getClassLoader
parameter_list|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
specifier|final
name|SecurityManager
name|sm
init|=
name|System
operator|.
name|getSecurityManager
argument_list|()
decl_stmt|;
if|if
condition|(
name|sm
operator|!=
literal|null
condition|)
block|{
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|ClassLoader
argument_list|>
argument_list|()
block|{
specifier|public
name|ClassLoader
name|run
parameter_list|()
block|{
return|return
name|clazz
operator|.
name|getClassLoader
argument_list|()
return|;
block|}
block|}
argument_list|)
return|;
block|}
return|return
name|clazz
operator|.
name|getClassLoader
argument_list|()
return|;
block|}
block|}
end_class

end_unit

