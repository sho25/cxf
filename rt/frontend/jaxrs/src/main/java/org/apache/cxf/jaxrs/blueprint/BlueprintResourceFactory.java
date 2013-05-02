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
name|blueprint
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
name|Constructor
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
name|Method
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
name|Proxy
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
name|core
operator|.
name|Application
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
name|util
operator|.
name|ClassHelper
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
name|helpers
operator|.
name|CastUtils
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
name|lifecycle
operator|.
name|ResourceProvider
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
name|model
operator|.
name|ProviderInfo
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
name|InjectionUtils
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
name|ResourceUtils
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
name|message
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|BlueprintContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|reflect
operator|.
name|BeanMetadata
import|;
end_import

begin_class
specifier|public
class|class
name|BlueprintResourceFactory
implements|implements
name|ResourceProvider
block|{
specifier|private
name|BlueprintContainer
name|blueprintContainer
decl_stmt|;
specifier|private
name|Constructor
argument_list|<
name|?
argument_list|>
name|c
decl_stmt|;
specifier|private
name|String
name|beanId
decl_stmt|;
specifier|private
name|Method
name|postConstructMethod
decl_stmt|;
specifier|private
name|Method
name|preDestroyMethod
decl_stmt|;
specifier|private
name|boolean
name|isSingleton
decl_stmt|;
specifier|public
name|BlueprintResourceFactory
parameter_list|()
block|{      }
specifier|public
name|BlueprintResourceFactory
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|beanId
operator|=
name|name
expr_stmt|;
block|}
specifier|private
name|void
name|init
parameter_list|()
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|ClassHelper
operator|.
name|getRealClassFromClass
argument_list|(
name|blueprintContainer
operator|.
name|getComponentInstance
argument_list|(
name|beanId
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|Proxy
operator|.
name|isProxyClass
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|type
operator|=
name|ClassHelper
operator|.
name|getRealClass
argument_list|(
name|blueprintContainer
operator|.
name|getComponentInstance
argument_list|(
name|beanId
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|c
operator|=
name|ResourceUtils
operator|.
name|findResourceConstructor
argument_list|(
name|type
argument_list|,
operator|!
name|isSingleton
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Resource class "
operator|+
name|type
operator|+
literal|" has no valid constructor"
argument_list|)
throw|;
block|}
name|postConstructMethod
operator|=
name|ResourceUtils
operator|.
name|findPostConstructMethod
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|preDestroyMethod
operator|=
name|ResourceUtils
operator|.
name|findPreDestroyMethod
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|Object
name|component
init|=
name|blueprintContainer
operator|.
name|getComponentMetadata
argument_list|(
name|beanId
argument_list|)
decl_stmt|;
if|if
condition|(
name|component
operator|instanceof
name|BeanMetadata
condition|)
block|{
name|BeanMetadata
name|local
init|=
operator|(
name|BeanMetadata
operator|)
name|component
decl_stmt|;
name|isSingleton
operator|=
name|BeanMetadata
operator|.
name|SCOPE_SINGLETON
operator|.
name|equals
argument_list|(
name|local
operator|.
name|getScope
argument_list|()
argument_list|)
operator|||
operator|(
name|local
operator|.
name|getScope
argument_list|()
operator|==
literal|null
operator|&&
name|local
operator|.
name|getId
argument_list|()
operator|!=
literal|null
operator|)
expr_stmt|;
block|}
block|}
specifier|public
name|Object
name|getInstance
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
comment|//TODO -- This is not the BP way.
name|ProviderInfo
argument_list|<
name|?
argument_list|>
name|application
init|=
name|m
operator|==
literal|null
condition|?
literal|null
else|:
operator|(
name|ProviderInfo
argument_list|<
name|?
argument_list|>
operator|)
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|get
argument_list|(
name|Application
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
name|mapValues
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|application
operator|==
literal|null
condition|?
literal|null
else|:
name|Collections
operator|.
name|singletonMap
argument_list|(
name|Application
operator|.
name|class
argument_list|,
name|application
operator|.
name|getProvider
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Object
index|[]
name|values
init|=
name|ResourceUtils
operator|.
name|createConstructorArguments
argument_list|(
name|c
argument_list|,
name|m
argument_list|,
operator|!
name|isSingleton
argument_list|()
argument_list|,
name|mapValues
argument_list|)
decl_stmt|;
comment|//TODO Very springish...
name|Object
name|instance
init|=
name|values
operator|.
name|length
operator|>
literal|0
condition|?
name|blueprintContainer
operator|.
name|getComponentInstance
argument_list|(
name|beanId
argument_list|)
else|:
name|blueprintContainer
operator|.
name|getComponentInstance
argument_list|(
name|beanId
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isSingleton
argument_list|()
operator|||
name|m
operator|==
literal|null
condition|)
block|{
name|InjectionUtils
operator|.
name|invokeLifeCycleMethod
argument_list|(
name|instance
argument_list|,
name|postConstructMethod
argument_list|)
expr_stmt|;
block|}
return|return
name|instance
return|;
block|}
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
name|isSingleton
return|;
block|}
specifier|public
name|void
name|releaseInstance
parameter_list|(
name|Message
name|m
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSingleton
argument_list|()
condition|)
block|{
name|InjectionUtils
operator|.
name|invokeLifeCycleMethod
argument_list|(
name|o
argument_list|,
name|preDestroyMethod
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setBeanId
parameter_list|(
name|String
name|serviceBeanId
parameter_list|)
block|{
name|this
operator|.
name|beanId
operator|=
name|serviceBeanId
expr_stmt|;
block|}
name|Constructor
argument_list|<
name|?
argument_list|>
name|getBeanConstructor
parameter_list|()
block|{
return|return
name|c
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getResourceClass
parameter_list|()
block|{
return|return
name|c
operator|.
name|getDeclaringClass
argument_list|()
return|;
block|}
specifier|public
name|void
name|setBlueprintContainer
parameter_list|(
name|BlueprintContainer
name|blueprintContainer
parameter_list|)
block|{
name|this
operator|.
name|blueprintContainer
operator|=
name|blueprintContainer
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

