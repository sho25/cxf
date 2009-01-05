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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Set
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
name|WSDLConstants
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
name|addressing
operator|.
name|JAXWSAConstants
import|;
end_import

begin_comment
comment|/***  * Only public/static/final fields can be resolved  * The prefix MUST ends with _PREFIX  * Namespace MUST starts with NS_  * The value of the _PREFIX is the suffix of NS_    e.g     public static final String WSAW_PREFIX = "wsaw";     public static final String NS_WSAW = "http://www.w3.org/2006/05/addressing/wsdl";  ***/
end_comment

begin_class
specifier|public
specifier|final
class|class
name|NSManager
block|{
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|cache
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|NSManager
parameter_list|()
block|{
name|resolveConstants
argument_list|(
name|JAXWSAConstants
operator|.
name|class
argument_list|)
expr_stmt|;
name|resolveConstants
argument_list|(
name|WSDLConstants
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|resolveConstants
parameter_list|(
specifier|final
name|Class
name|clz
parameter_list|)
block|{
for|for
control|(
name|Field
name|field
range|:
name|clz
operator|.
name|getFields
argument_list|()
control|)
block|{
if|if
condition|(
name|field
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"_PREFIX"
argument_list|)
operator|&&
name|isPulicStaticFinal
argument_list|(
name|field
argument_list|)
condition|)
block|{
try|try
block|{
name|String
name|prefix
init|=
operator|(
name|String
operator|)
name|field
operator|.
name|get
argument_list|(
name|clz
argument_list|)
decl_stmt|;
name|Field
name|nsField
init|=
name|clz
operator|.
name|getField
argument_list|(
literal|"NS_"
operator|+
name|prefix
operator|.
name|toUpperCase
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|nsField
operator|!=
literal|null
condition|)
block|{
name|cache
operator|.
name|put
argument_list|(
operator|(
name|String
operator|)
name|nsField
operator|.
name|get
argument_list|(
name|clz
argument_list|)
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
block|}
specifier|public
name|String
name|getPrefixFromNS
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
return|return
name|cache
operator|.
name|get
argument_list|(
name|namespace
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isPulicStaticFinal
parameter_list|(
specifier|final
name|Field
name|field
parameter_list|)
block|{
return|return
name|field
operator|.
name|getModifiers
argument_list|()
operator|==
operator|(
name|Modifier
operator|.
name|PUBLIC
operator||
name|Modifier
operator|.
name|STATIC
operator||
name|Modifier
operator|.
name|FINAL
operator|)
return|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getNamespaces
parameter_list|()
block|{
return|return
name|this
operator|.
name|cache
operator|.
name|keySet
argument_list|()
return|;
block|}
block|}
end_class

end_unit

