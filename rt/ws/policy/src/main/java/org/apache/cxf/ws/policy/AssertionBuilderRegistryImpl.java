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
name|policy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|ResourceBundle
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
name|annotation
operator|.
name|Resource
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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|i18n
operator|.
name|BundleUtils
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
name|i18n
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
name|configuration
operator|.
name|ConfiguredBeanLocator
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
name|configuration
operator|.
name|spring
operator|.
name|MapProvider
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
name|extension
operator|.
name|BusExtension
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
name|extension
operator|.
name|RegistryImpl
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
name|policy
operator|.
name|builder
operator|.
name|primitive
operator|.
name|NestedPrimitiveAssertionBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Assertion
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|AssertionBuilderFactory
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
argument_list|(
name|unlessNull
operator|=
literal|"bus"
argument_list|)
specifier|public
class|class
name|AssertionBuilderRegistryImpl
extends|extends
name|RegistryImpl
argument_list|<
name|QName
argument_list|,
name|AssertionBuilder
argument_list|>
implements|implements
name|AssertionBuilderRegistry
implements|,
name|BusExtension
block|{
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|AssertionBuilderRegistryImpl
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|AssertionBuilderRegistryImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|IGNORED_CACHE_SIZE
init|=
literal|10
decl_stmt|;
specifier|private
name|boolean
name|ignoreUnknownAssertions
init|=
literal|true
decl_stmt|;
specifier|private
name|List
argument_list|<
name|QName
argument_list|>
name|ignored
init|=
operator|new
name|ArrayList
argument_list|<
name|QName
argument_list|>
argument_list|(
name|IGNORED_CACHE_SIZE
argument_list|)
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|boolean
name|dynamicLoaded
decl_stmt|;
specifier|public
name|AssertionBuilderRegistryImpl
parameter_list|()
block|{
name|super
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AssertionBuilderRegistryImpl
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|super
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|setBus
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AssertionBuilderRegistryImpl
parameter_list|(
name|Map
argument_list|<
name|QName
argument_list|,
name|AssertionBuilder
argument_list|>
name|builders
parameter_list|)
block|{
name|super
argument_list|(
name|builders
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AssertionBuilderRegistryImpl
parameter_list|(
name|MapProvider
argument_list|<
name|QName
argument_list|,
name|AssertionBuilder
argument_list|>
name|builders
parameter_list|)
block|{
name|super
argument_list|(
name|builders
operator|.
name|createMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AssertionBuilderRegistryImpl
parameter_list|(
name|Bus
name|b
parameter_list|,
name|MapProvider
argument_list|<
name|QName
argument_list|,
name|AssertionBuilder
argument_list|>
name|builders
parameter_list|)
block|{
name|super
argument_list|(
name|builders
operator|.
name|createMap
argument_list|()
argument_list|)
expr_stmt|;
name|setBus
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Resource
specifier|public
specifier|final
name|void
name|setBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
if|if
condition|(
name|b
operator|!=
literal|null
condition|)
block|{
name|b
operator|.
name|setExtension
argument_list|(
name|this
argument_list|,
name|AssertionBuilderRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getRegistrationType
parameter_list|()
block|{
return|return
name|AssertionBuilderRegistry
operator|.
name|class
return|;
block|}
specifier|public
name|void
name|register
parameter_list|(
name|AssertionBuilder
name|builder
parameter_list|)
block|{
name|QName
name|names
index|[]
init|=
name|builder
operator|.
name|getKnownElements
argument_list|()
decl_stmt|;
for|for
control|(
name|QName
name|n
range|:
name|names
control|)
block|{
name|super
operator|.
name|register
argument_list|(
name|n
argument_list|,
name|builder
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|isIgnoreUnknownAssertions
parameter_list|()
block|{
return|return
name|ignoreUnknownAssertions
return|;
block|}
specifier|public
name|void
name|setIgnoreUnknownAssertions
parameter_list|(
name|boolean
name|ignore
parameter_list|)
block|{
name|ignoreUnknownAssertions
operator|=
name|ignore
expr_stmt|;
block|}
specifier|protected
specifier|synchronized
name|void
name|loadDynamic
parameter_list|()
block|{
if|if
condition|(
operator|!
name|dynamicLoaded
operator|&&
name|bus
operator|!=
literal|null
condition|)
block|{
name|dynamicLoaded
operator|=
literal|true
expr_stmt|;
name|ConfiguredBeanLocator
name|c
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ConfiguredBeanLocator
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|getBeansOfType
argument_list|(
name|AssertionBuilderLoader
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|Assertion
name|build
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
return|return
name|build
argument_list|(
name|element
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|Assertion
name|build
parameter_list|(
name|Element
name|element
parameter_list|,
name|AssertionBuilderFactory
name|factory
parameter_list|)
block|{
name|loadDynamic
argument_list|()
expr_stmt|;
name|AssertionBuilder
name|builder
decl_stmt|;
name|QName
name|qname
init|=
operator|new
name|QName
argument_list|(
name|element
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|element
operator|.
name|getLocalName
argument_list|()
argument_list|)
decl_stmt|;
name|builder
operator|=
name|get
argument_list|(
name|qname
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|==
name|builder
condition|)
block|{
name|Message
name|m
init|=
operator|new
name|Message
argument_list|(
literal|"NO_ASSERTIONBUILDER_EXC"
argument_list|,
name|BUNDLE
argument_list|,
name|qname
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ignoreUnknownAssertions
condition|)
block|{
name|boolean
name|alreadyWarned
init|=
name|ignored
operator|.
name|contains
argument_list|(
name|qname
argument_list|)
decl_stmt|;
if|if
condition|(
name|alreadyWarned
condition|)
block|{
name|ignored
operator|.
name|remove
argument_list|(
name|qname
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ignored
operator|.
name|size
argument_list|()
operator|==
name|IGNORED_CACHE_SIZE
condition|)
block|{
name|ignored
operator|.
name|remove
argument_list|(
name|IGNORED_CACHE_SIZE
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|ignored
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|qname
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|alreadyWarned
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
name|m
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|builder
operator|=
operator|new
name|NestedPrimitiveAssertionBuilder
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyBuilder
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|PolicyException
argument_list|(
name|m
argument_list|)
throw|;
block|}
block|}
return|return
name|builder
operator|.
name|build
argument_list|(
name|element
argument_list|,
name|factory
argument_list|)
return|;
block|}
block|}
end_class

end_unit

