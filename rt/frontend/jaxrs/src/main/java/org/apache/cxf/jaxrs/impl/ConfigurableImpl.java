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
name|Arrays
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
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|client
operator|.
name|ClientRequestFilter
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
name|client
operator|.
name|ClientResponseFilter
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
name|container
operator|.
name|ContainerRequestFilter
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
name|container
operator|.
name|ContainerResponseFilter
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
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|ConfigurableImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|RESTRICTED_CLASSES_IN_SERVER
init|=
block|{
name|ClientRequestFilter
operator|.
name|class
block|,
name|ClientResponseFilter
operator|.
name|class
block|}
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|RESTRICTED_CLASSES_IN_CLIENT
init|=
block|{
name|ContainerRequestFilter
operator|.
name|class
block|,
name|ContainerResponseFilter
operator|.
name|class
block|}
decl_stmt|;
specifier|private
name|ConfigurationImpl
name|config
decl_stmt|;
specifier|private
specifier|final
name|C
name|configurable
decl_stmt|;
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|restrictedContractTypes
decl_stmt|;
specifier|public
interface|interface
name|Instantiator
block|{
parameter_list|<
name|T
parameter_list|>
name|Object
name|create
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
function_decl|;
block|}
specifier|public
name|ConfigurableImpl
parameter_list|(
name|C
name|configurable
parameter_list|,
name|RuntimeType
name|rt
parameter_list|)
block|{
name|this
argument_list|(
name|configurable
argument_list|,
operator|new
name|ConfigurationImpl
argument_list|(
name|rt
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ConfigurableImpl
parameter_list|(
name|C
name|configurable
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
operator|instanceof
name|ConfigurationImpl
condition|?
operator|(
name|ConfigurationImpl
operator|)
name|config
else|:
operator|new
name|ConfigurationImpl
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|restrictedContractTypes
operator|=
name|RuntimeType
operator|.
name|CLIENT
operator|.
name|equals
argument_list|(
name|config
operator|.
name|getRuntimeType
argument_list|()
argument_list|)
condition|?
name|RESTRICTED_CLASSES_IN_CLIENT
else|:
name|RESTRICTED_CLASSES_IN_SERVER
expr_stmt|;
block|}
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|getImplementedContracts
parameter_list|(
name|Object
name|provider
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|restrictedClasses
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|providerClass
init|=
name|provider
operator|instanceof
name|Class
argument_list|<
name|?
argument_list|>
condition|?
operator|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|provider
operator|)
else|:
name|provider
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|implementedContracts
init|=
name|Arrays
operator|.
name|stream
argument_list|(
name|providerClass
operator|.
name|getInterfaces
argument_list|()
argument_list|)
operator|.
name|filter
argument_list|(
name|el
lambda|->
name|Arrays
operator|.
name|stream
argument_list|(
name|restrictedClasses
argument_list|)
operator|.
name|noneMatch
argument_list|(
name|el
operator|::
name|equals
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|implementedContracts
operator|.
name|toArray
argument_list|(
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{}
block|)
function|;
block|}
end_class

begin_function
specifier|protected
name|C
name|getConfigurable
parameter_list|()
block|{
return|return
name|configurable
return|;
block|}
end_function

begin_function
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
end_function

begin_function
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
end_function

begin_function
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
end_function

begin_function
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
name|getImplementedContracts
argument_list|(
name|provider
argument_list|,
name|restrictedContractTypes
argument_list|)
argument_list|)
return|;
block|}
end_function

begin_function
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
end_function

begin_function
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
return|return
name|doRegister
argument_list|(
name|provider
argument_list|,
name|contracts
argument_list|)
return|;
block|}
end_function

begin_function
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
end_function

begin_function
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
name|getInstantiator
argument_list|()
operator|.
name|create
argument_list|(
name|providerClass
argument_list|)
argument_list|,
name|bindingPriority
argument_list|,
name|getImplementedContracts
argument_list|(
name|providerClass
argument_list|,
name|restrictedContractTypes
argument_list|)
argument_list|)
return|;
block|}
end_function

begin_function
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
end_function

begin_function
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
name|getInstantiator
argument_list|()
operator|.
name|create
argument_list|(
name|providerClass
argument_list|)
argument_list|,
name|contracts
argument_list|)
return|;
block|}
end_function

begin_function
specifier|protected
name|Instantiator
name|getInstantiator
parameter_list|()
block|{
return|return
name|ConfigurationImpl
operator|::
name|createProvider
return|;
block|}
end_function

begin_function
specifier|private
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
name|contracts
operator|==
literal|null
operator|||
name|contracts
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Null or empty contracts specified for "
operator|+
name|provider
operator|+
literal|"; ignoring."
argument_list|)
expr_stmt|;
return|return
name|configurable
return|;
block|}
return|return
name|doRegister
argument_list|(
name|provider
argument_list|,
name|ConfigurationImpl
operator|.
name|initContractsMap
argument_list|(
name|bindingPriority
argument_list|,
name|contracts
argument_list|)
argument_list|)
return|;
block|}
end_function

begin_function
specifier|private
name|C
name|doRegister
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
name|boolean
name|enabled
init|=
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
decl_stmt|;
name|config
operator|.
name|setFeature
argument_list|(
name|feature
argument_list|,
name|enabled
argument_list|)
expr_stmt|;
return|return
name|configurable
return|;
block|}
name|config
operator|.
name|register
argument_list|(
name|provider
argument_list|,
name|contracts
argument_list|)
expr_stmt|;
return|return
name|configurable
return|;
block|}
end_function

unit|}
end_unit

