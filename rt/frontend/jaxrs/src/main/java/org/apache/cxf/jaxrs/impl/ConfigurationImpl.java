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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|Map
operator|.
name|Entry
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
name|ConfigurationImpl
implements|implements
name|Configuration
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
name|ConfigurationImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|RuntimeType
name|runtimeType
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Object
argument_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
argument_list|>
name|providers
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|Object
argument_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Feature
argument_list|,
name|Boolean
argument_list|>
name|features
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|Feature
argument_list|,
name|Boolean
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|ConfigurationImpl
parameter_list|(
name|RuntimeType
name|rt
parameter_list|)
block|{
name|this
operator|.
name|runtimeType
operator|=
name|rt
expr_stmt|;
block|}
specifier|public
name|ConfigurationImpl
parameter_list|(
name|Configuration
name|parent
parameter_list|)
block|{
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|props
operator|.
name|putAll
argument_list|(
name|parent
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|runtimeType
operator|=
name|parent
operator|.
name|getRuntimeType
argument_list|()
expr_stmt|;
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|providerClasses
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|(
name|parent
operator|.
name|getClasses
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|parent
operator|.
name|getInstances
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|Feature
operator|)
condition|)
block|{
name|registerParentProvider
argument_list|(
name|o
argument_list|,
name|parent
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Feature
name|f
init|=
operator|(
name|Feature
operator|)
name|o
decl_stmt|;
name|features
operator|.
name|put
argument_list|(
name|f
argument_list|,
name|parent
operator|.
name|isEnabled
argument_list|(
name|f
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|providerClasses
operator|.
name|remove
argument_list|(
name|o
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
range|:
name|providerClasses
control|)
block|{
name|registerParentProvider
argument_list|(
name|createProvider
argument_list|(
name|cls
argument_list|)
argument_list|,
name|parent
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|registerParentProvider
parameter_list|(
name|Object
name|o
parameter_list|,
name|Configuration
name|parent
parameter_list|)
block|{
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
init|=
name|parent
operator|.
name|getContracts
argument_list|(
name|o
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|contracts
operator|!=
literal|null
condition|)
block|{
name|providers
operator|.
name|put
argument_list|(
name|o
argument_list|,
name|contracts
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|register
argument_list|(
name|o
argument_list|,
name|AnnotationUtils
operator|.
name|getBindingPriority
argument_list|(
name|o
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|,
name|ConfigurableImpl
operator|.
name|getImplementedContracts
argument_list|(
name|o
argument_list|,
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{}
block|)
block|)
function|;
block|}
end_class

begin_function
unit|}      @
name|Override
specifier|public
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getClasses
parameter_list|()
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|getInstances
argument_list|()
control|)
block|{
name|classes
operator|.
name|add
argument_list|(
name|o
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|classes
return|;
block|}
end_function

begin_function
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|getContracts
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
for|for
control|(
name|Object
name|o
range|:
name|getInstances
argument_list|()
control|)
block|{
if|if
condition|(
name|cls
operator|.
name|isAssignableFrom
argument_list|(
name|o
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|Feature
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|providers
operator|.
name|get
argument_list|(
name|o
argument_list|)
return|;
block|}
block|}
block|}
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Object
argument_list|>
name|getInstances
parameter_list|()
block|{
name|Set
argument_list|<
name|Object
argument_list|>
name|allInstances
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|allInstances
operator|.
name|addAll
argument_list|(
name|providers
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
name|allInstances
operator|.
name|addAll
argument_list|(
name|features
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|allInstances
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getProperties
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|props
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
specifier|public
name|Object
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|props
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getPropertyNames
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|props
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
specifier|public
name|RuntimeType
name|getRuntimeType
parameter_list|()
block|{
return|return
name|runtimeType
return|;
block|}
end_function

begin_function
annotation|@
name|Override
specifier|public
name|boolean
name|isEnabled
parameter_list|(
name|Feature
name|f
parameter_list|)
block|{
return|return
name|features
operator|.
name|containsKey
argument_list|(
name|f
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
specifier|public
name|boolean
name|isEnabled
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Feature
argument_list|>
name|f
parameter_list|)
block|{
for|for
control|(
name|Feature
name|feature
range|:
name|features
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|feature
operator|.
name|getClass
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|f
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
end_function

begin_function
annotation|@
name|Override
specifier|public
name|boolean
name|isRegistered
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
for|for
control|(
name|Object
name|o
range|:
name|getInstances
argument_list|()
control|)
block|{
if|if
condition|(
name|o
operator|.
name|equals
argument_list|(
name|obj
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
end_function

begin_function
annotation|@
name|Override
specifier|public
name|boolean
name|isRegistered
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
for|for
control|(
name|Object
name|o
range|:
name|getInstances
argument_list|()
control|)
block|{
if|if
condition|(
name|cls
operator|==
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
end_function

begin_function
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|props
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|props
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_function

begin_function
specifier|public
name|void
name|setFeature
parameter_list|(
name|Feature
name|f
parameter_list|,
name|boolean
name|enabled
parameter_list|)
block|{
name|features
operator|.
name|put
argument_list|(
name|f
argument_list|,
name|enabled
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
specifier|private
name|void
name|register
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
name|register
argument_list|(
name|provider
argument_list|,
name|initContractsMap
argument_list|(
name|bindingPriority
argument_list|,
name|contracts
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
specifier|public
name|boolean
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
if|if
condition|(
name|provider
operator|.
name|getClass
argument_list|()
operator|==
name|Class
operator|.
name|class
condition|)
block|{
if|if
condition|(
name|isRegistered
argument_list|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|provider
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Provider class "
operator|+
operator|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|provider
operator|)
operator|.
name|getName
argument_list|()
operator|+
literal|" has already been registered"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|provider
operator|=
name|createProvider
argument_list|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|provider
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isRegistered
argument_list|(
name|provider
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Provider "
operator|+
name|provider
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" has already been registered"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|contractsValid
argument_list|(
name|provider
argument_list|,
name|contracts
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|metadata
init|=
name|providers
operator|.
name|get
argument_list|(
name|provider
argument_list|)
decl_stmt|;
if|if
condition|(
name|metadata
operator|==
literal|null
condition|)
block|{
name|metadata
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|providers
operator|.
name|put
argument_list|(
name|provider
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
block|}
for|for
control|(
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
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
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
name|metadata
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
end_function

begin_function
specifier|private
name|boolean
name|contractsValid
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
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|providerClass
init|=
name|provider
operator|.
name|getClass
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|contractInterface
range|:
name|contracts
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|contractInterface
operator|.
name|isAssignableFrom
argument_list|(
name|providerClass
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Provider "
operator|+
name|providerClass
operator|.
name|getName
argument_list|()
operator|+
literal|" does not implement specified contract: "
operator|+
name|contractInterface
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
end_function

begin_function
specifier|public
specifier|static
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|initContractsMap
parameter_list|(
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
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|metadata
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
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
name|metadata
operator|.
name|put
argument_list|(
name|contract
argument_list|,
name|bindingPriority
argument_list|)
expr_stmt|;
block|}
return|return
name|metadata
return|;
block|}
end_function

begin_function
specifier|public
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
end_function

unit|}
end_unit

