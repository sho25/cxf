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
name|HashSet
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
name|Set
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
name|neethi
operator|.
name|AssertionBuilderFactoryImpl
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
name|PolicyBuilder
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
name|builders
operator|.
name|AssertionBuilder
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
name|builders
operator|.
name|xml
operator|.
name|XMLPrimitiveAssertionBuilder
import|;
end_import

begin_comment
comment|/**  *  */
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
name|AssertionBuilderFactoryImpl
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
name|boolean
name|ignoreUnknownAssertions
init|=
literal|true
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|QName
argument_list|>
name|ignored
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
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
name|PolicyBuilder
name|builder
init|=
name|b
operator|.
name|getExtension
argument_list|(
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
name|PolicyBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|builder
operator|instanceof
name|PolicyBuilder
condition|)
block|{
name|engine
operator|=
operator|(
name|PolicyBuilder
operator|)
name|builder
expr_stmt|;
block|}
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
for|for
control|(
name|AssertionBuilder
argument_list|<
name|?
argument_list|>
name|b
range|:
name|c
operator|.
name|getBeansOfType
argument_list|(
name|AssertionBuilder
operator|.
name|class
argument_list|)
control|)
block|{
name|registerBuilder
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
name|AssertionBuilder
argument_list|<
name|?
argument_list|>
name|handleNoRegisteredBuilder
parameter_list|(
name|QName
name|qname
parameter_list|)
block|{
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
operator|!
name|alreadyWarned
condition|)
block|{
name|ignored
operator|.
name|add
argument_list|(
name|qname
argument_list|)
expr_stmt|;
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
return|return
operator|new
name|XMLPrimitiveAssertionBuilder
argument_list|()
return|;
block|}
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
throw|throw
operator|new
name|PolicyException
argument_list|(
name|m
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

