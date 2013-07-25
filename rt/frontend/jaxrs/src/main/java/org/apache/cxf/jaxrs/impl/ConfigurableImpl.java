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
name|jaxrs
operator|.
name|impl
package|;
end_package

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
name|ws
operator|.
name|rs
operator|.
name|Priorities
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|RuntimeType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Configurable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Feature
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|FeatureContext
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
name|jaxrs
operator|.
name|utils
operator|.
name|AnnotationUtils
import|;
end_import

begin_class
specifier|public
class|class
name|ConfigurableImpl
parameter_list|<
name|C
extends|extends
name|Configurable
parameter_list|<
name|C
parameter_list|>
parameter_list|>
implements|implements
name|Configurable
argument_list|<
name|C
argument_list|>
block|{
specifier|private
name|ConfigurationImpl
name|config
decl_stmt|;
specifier|private
name|C
name|configurable
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|supportedProviderClasses
decl_stmt|;
specifier|public
name|ConfigurableImpl
parameter_list|(
name|C
name|configurable
parameter_list|,
name|RuntimeType
name|rt
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|supportedProviderClasses
parameter_list|)
block|{
name|this
argument_list|(
name|configurable
argument_list|,
name|rt
argument_list|,
name|supportedProviderClasses
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ConfigurableImpl
parameter_list|(
name|C
name|configurable
parameter_list|,
name|RuntimeType
name|rt
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|supportedProviderClasses
parameter_list|,
name|Configuration
name|config
parameter_list|)
block|{
name|this
operator|.
name|configurable
operator|=
name|configurable
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
operator|==
literal|null
condition|?
operator|new
name|ConfigurationImpl
argument_list|(
name|rt
argument_list|)
else|:
operator|new
name|ConfigurationImpl
argument_list|(
name|rt
argument_list|,
name|config
argument_list|)
expr_stmt|;
name|this
operator|.
name|supportedProviderClasses
operator|=
name|supportedProviderClasses
expr_stmt|;
block|}
specifier|protected
name|C
name|getConfigurable
parameter_list|()
block|{
return|return
name|configurable
return|;
block|}
annotation|@
name|Override
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|config
return|;
block|}
annotation|@
name|Override
specifier|public
name|C
name|property
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|config
operator|.
name|setProperty
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|configurable
return|;
block|}
annotation|@
name|Override
specifier|public
name|C
name|register
parameter_list|(
name|Object
name|provider
parameter_list|)
block|{
return|return
name|register
argument_list|(
name|provider
argument_list|,
name|AnnotationUtils
operator|.
name|getBindingPriority
argument_list|(
name|provider
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|C
name|register
parameter_list|(
name|Object
name|provider
parameter_list|,
name|int
name|bindingPriority
parameter_list|)
block|{
return|return
name|doRegister
argument_list|(
name|provider
argument_list|,
name|bindingPriority
argument_list|,
name|supportedProviderClasses
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|C
name|register
parameter_list|(
name|Object
name|provider
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|contracts
parameter_list|)
block|{
return|return
name|doRegister
argument_list|(
name|provider
argument_list|,
name|Priorities
operator|.
name|USER
argument_list|,
name|contracts
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|C
name|register
parameter_list|(
name|Object
name|provider
parameter_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|contracts
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|entry
range|:
name|contracts
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|doRegister
argument_list|(
name|provider
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|configurable
return|;
block|}
annotation|@
name|Override
specifier|public
name|C
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|providerClass
parameter_list|)
block|{
return|return
name|register
argument_list|(
name|providerClass
argument_list|,
name|AnnotationUtils
operator|.
name|getBindingPriority
argument_list|(
name|providerClass
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|C
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|providerClass
parameter_list|,
name|int
name|bindingPriority
parameter_list|)
block|{
return|return
name|doRegister
argument_list|(
name|createProvider
argument_list|(
name|providerClass
argument_list|)
argument_list|,
name|bindingPriority
argument_list|,
name|supportedProviderClasses
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|C
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|providerClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|contracts
parameter_list|)
block|{
return|return
name|doRegister
argument_list|(
name|providerClass
argument_list|,
name|Priorities
operator|.
name|USER
argument_list|,
name|contracts
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|C
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|providerClass
parameter_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|contracts
parameter_list|)
block|{
return|return
name|register
argument_list|(
name|createProvider
argument_list|(
name|providerClass
argument_list|)
argument_list|,
name|contracts
argument_list|)
return|;
block|}
specifier|protected
name|C
name|doRegister
parameter_list|(
name|Object
name|provider
parameter_list|,
name|int
name|bindingPriority
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|contracts
parameter_list|)
block|{
if|if
condition|(
name|provider
operator|instanceof
name|Feature
condition|)
block|{
name|Feature
name|feature
init|=
operator|(
name|Feature
operator|)
name|provider
decl_stmt|;
name|config
operator|.
name|setFeature
argument_list|(
name|feature
argument_list|)
expr_stmt|;
name|feature
operator|.
name|configure
argument_list|(
operator|new
name|FeatureContextImpl
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|configurable
return|;
block|}
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|contract
range|:
name|contracts
control|)
block|{
if|if
condition|(
name|contract
operator|.
name|isAssignableFrom
argument_list|(
name|provider
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|config
operator|.
name|register
argument_list|(
name|provider
argument_list|,
name|contract
argument_list|,
name|bindingPriority
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|configurable
return|;
block|}
specifier|private
specifier|static
name|Object
name|createProvider
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
try|try
block|{
return|return
name|cls
operator|.
name|newInstance
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
class|class
name|FeatureContextImpl
implements|implements
name|FeatureContext
block|{
specifier|private
name|Configurable
argument_list|<
name|?
argument_list|>
name|cfg
decl_stmt|;
specifier|public
name|FeatureContextImpl
parameter_list|(
name|Configurable
argument_list|<
name|?
argument_list|>
name|cfg
parameter_list|)
block|{
name|this
operator|.
name|cfg
operator|=
name|cfg
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|cfg
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|FeatureContext
name|property
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|cfg
operator|.
name|property
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|FeatureContext
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
name|cfg
operator|.
name|register
argument_list|(
name|cls
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|FeatureContext
name|register
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
name|cfg
operator|.
name|register
argument_list|(
name|obj
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|FeatureContext
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|int
name|priority
parameter_list|)
block|{
name|cfg
operator|.
name|register
argument_list|(
name|cls
argument_list|,
name|priority
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|FeatureContext
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|contract
parameter_list|)
block|{
name|cfg
operator|.
name|register
argument_list|(
name|cls
argument_list|,
name|contract
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|FeatureContext
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|map
parameter_list|)
block|{
name|cfg
operator|.
name|register
argument_list|(
name|cls
argument_list|,
name|map
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|FeatureContext
name|register
parameter_list|(
name|Object
name|obj
parameter_list|,
name|int
name|priority
parameter_list|)
block|{
name|cfg
operator|.
name|register
argument_list|(
name|obj
argument_list|,
name|priority
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|FeatureContext
name|register
parameter_list|(
name|Object
name|obj
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|contract
parameter_list|)
block|{
name|cfg
operator|.
name|register
argument_list|(
name|obj
argument_list|,
name|contract
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|FeatureContext
name|register
parameter_list|(
name|Object
name|obj
parameter_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|map
parameter_list|)
block|{
name|cfg
operator|.
name|register
argument_list|(
name|obj
argument_list|,
name|map
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
block|}
end_class

end_unit

