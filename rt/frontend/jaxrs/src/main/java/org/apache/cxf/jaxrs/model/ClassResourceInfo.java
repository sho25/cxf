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
name|model
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
name|Method
import|;
end_import

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
name|HashSet
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|BeanParam
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
name|Consumes
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
name|Path
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
name|Produces
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
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
name|BusFactory
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
name|provider
operator|.
name|ServerProviderFactory
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
name|JAXRSUtils
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

begin_class
specifier|public
class|class
name|ClassResourceInfo
extends|extends
name|BeanResourceInfo
block|{
specifier|private
name|URITemplate
name|uriTemplate
decl_stmt|;
specifier|private
name|MethodDispatcher
name|methodDispatcher
decl_stmt|;
specifier|private
name|ResourceProvider
name|resourceProvider
decl_stmt|;
specifier|private
name|ConcurrentHashMap
argument_list|<
name|SubresourceKey
argument_list|,
name|ClassResourceInfo
argument_list|>
name|subResources
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|SubresourceKey
argument_list|,
name|ClassResourceInfo
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|enableStatic
decl_stmt|;
specifier|private
name|boolean
name|createdFromModel
decl_stmt|;
specifier|private
name|String
name|consumesTypes
decl_stmt|;
specifier|private
name|String
name|producesTypes
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|nameBindings
init|=
name|Collections
operator|.
name|emptySet
argument_list|()
decl_stmt|;
specifier|private
name|ClassResourceInfo
name|parent
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|injectedSubInstances
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|ClassResourceInfo
parameter_list|(
name|ClassResourceInfo
name|cri
parameter_list|)
block|{
name|super
argument_list|(
name|cri
operator|.
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|cri
operator|.
name|isCreatedFromModel
argument_list|()
operator|&&
operator|!
name|InjectionUtils
operator|.
name|isConcreteClass
argument_list|(
name|cri
operator|.
name|getServiceClass
argument_list|()
argument_list|)
condition|)
block|{
name|this
operator|.
name|root
operator|=
name|cri
operator|.
name|root
expr_stmt|;
name|this
operator|.
name|serviceClass
operator|=
name|cri
operator|.
name|serviceClass
expr_stmt|;
name|this
operator|.
name|uriTemplate
operator|=
name|cri
operator|.
name|uriTemplate
expr_stmt|;
name|this
operator|.
name|methodDispatcher
operator|=
operator|new
name|MethodDispatcher
argument_list|(
name|cri
operator|.
name|methodDispatcher
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|subResources
operator|=
name|cri
operator|.
name|subResources
expr_stmt|;
comment|//CHECKSTYLE:OFF
name|this
operator|.
name|paramFields
operator|=
name|cri
operator|.
name|paramFields
expr_stmt|;
name|this
operator|.
name|paramMethods
operator|=
name|cri
operator|.
name|paramMethods
expr_stmt|;
comment|//CHECKSTYLE:ON
name|this
operator|.
name|enableStatic
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|nameBindings
operator|=
name|cri
operator|.
name|nameBindings
expr_stmt|;
name|this
operator|.
name|parent
operator|=
name|cri
operator|.
name|parent
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
block|}
specifier|public
name|ClassResourceInfo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|theResourceClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|theServiceClass
parameter_list|,
name|boolean
name|theRoot
parameter_list|,
name|boolean
name|enableStatic
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|super
argument_list|(
name|theResourceClass
argument_list|,
name|theServiceClass
argument_list|,
name|theRoot
argument_list|,
name|theRoot
operator|||
name|enableStatic
argument_list|,
name|bus
argument_list|)
expr_stmt|;
name|this
operator|.
name|enableStatic
operator|=
name|enableStatic
expr_stmt|;
if|if
condition|(
name|resourceClass
operator|!=
literal|null
condition|)
block|{
name|nameBindings
operator|=
name|AnnotationUtils
operator|.
name|getNameBindings
argument_list|(
name|serviceClass
operator|.
name|getAnnotations
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|ClassResourceInfo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|theResourceClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|theServiceClass
parameter_list|,
name|boolean
name|theRoot
parameter_list|,
name|boolean
name|enableStatic
parameter_list|,
name|boolean
name|createdFromModel
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|this
argument_list|(
name|theResourceClass
argument_list|,
name|theServiceClass
argument_list|,
name|theRoot
argument_list|,
name|enableStatic
argument_list|,
name|bus
argument_list|)
expr_stmt|;
name|this
operator|.
name|createdFromModel
operator|=
name|createdFromModel
expr_stmt|;
block|}
comment|//CHECKSTYLE:OFF
specifier|public
name|ClassResourceInfo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|theResourceClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|,
name|boolean
name|theRoot
parameter_list|,
name|boolean
name|enableStatic
parameter_list|,
name|boolean
name|createdFromModel
parameter_list|,
name|String
name|consumesTypes
parameter_list|,
name|String
name|producesTypes
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
comment|//CHECKSTYLE:ON
name|this
argument_list|(
name|theResourceClass
argument_list|,
name|theResourceClass
argument_list|,
name|theRoot
argument_list|,
name|enableStatic
argument_list|,
name|createdFromModel
argument_list|,
name|bus
argument_list|)
expr_stmt|;
name|this
operator|.
name|consumesTypes
operator|=
name|consumesTypes
expr_stmt|;
name|this
operator|.
name|producesTypes
operator|=
name|producesTypes
expr_stmt|;
block|}
comment|// The following constructors are used by tests only
specifier|public
name|ClassResourceInfo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|theResourceClass
parameter_list|)
block|{
name|this
argument_list|(
name|theResourceClass
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ClassResourceInfo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|theResourceClass
parameter_list|,
name|boolean
name|theRoot
parameter_list|)
block|{
name|this
argument_list|(
name|theResourceClass
argument_list|,
name|theResourceClass
argument_list|,
name|theRoot
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ClassResourceInfo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|theResourceClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|theServiceClass
parameter_list|)
block|{
name|this
argument_list|(
name|theResourceClass
argument_list|,
name|theServiceClass
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ClassResourceInfo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|theResourceClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|theServiceClass
parameter_list|,
name|boolean
name|theRoot
parameter_list|)
block|{
name|this
argument_list|(
name|theResourceClass
argument_list|,
name|theServiceClass
argument_list|,
name|theRoot
argument_list|,
literal|false
argument_list|,
name|BusFactory
operator|.
name|getDefaultBus
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ClassResourceInfo
name|findResource
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|typedClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|instanceClass
parameter_list|)
block|{
name|instanceClass
operator|=
name|enableStatic
condition|?
name|typedClass
else|:
name|instanceClass
expr_stmt|;
name|SubresourceKey
name|key
init|=
operator|new
name|SubresourceKey
argument_list|(
name|typedClass
argument_list|,
name|instanceClass
argument_list|)
decl_stmt|;
return|return
name|subResources
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|contextsAvailable
parameter_list|()
block|{
comment|// avoid re-injecting the contexts if the root acts as subresource
return|return
name|super
operator|.
name|contextsAvailable
argument_list|()
operator|&&
operator|(
name|isRoot
argument_list|()
operator|||
name|parent
operator|!=
literal|null
operator|)
return|;
block|}
specifier|public
name|ClassResourceInfo
name|getSubResource
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|typedClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|instanceClass
parameter_list|)
block|{
return|return
name|getSubResource
argument_list|(
name|typedClass
argument_list|,
name|instanceClass
argument_list|,
literal|null
argument_list|,
name|enableStatic
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|ClassResourceInfo
name|getSubResource
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|typedClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|instanceClass
parameter_list|,
name|Object
name|instance
parameter_list|)
block|{
name|instanceClass
operator|=
name|enableStatic
condition|?
name|typedClass
else|:
name|instanceClass
expr_stmt|;
return|return
name|getSubResource
argument_list|(
name|typedClass
argument_list|,
name|instanceClass
argument_list|,
name|instance
argument_list|,
name|enableStatic
argument_list|,
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|ClassResourceInfo
name|getSubResource
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|typedClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|instanceClass
parameter_list|,
name|Object
name|instance
parameter_list|,
name|boolean
name|resolveContexts
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|SubresourceKey
name|key
init|=
operator|new
name|SubresourceKey
argument_list|(
name|typedClass
argument_list|,
name|instanceClass
argument_list|)
decl_stmt|;
name|ClassResourceInfo
name|cri
init|=
name|subResources
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|cri
operator|==
literal|null
condition|)
block|{
name|cri
operator|=
name|ResourceUtils
operator|.
name|createClassResourceInfo
argument_list|(
name|typedClass
argument_list|,
name|instanceClass
argument_list|,
name|this
argument_list|,
literal|false
argument_list|,
name|resolveContexts
argument_list|,
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|cri
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|cri
operator|.
name|initBeanParamInfo
argument_list|(
name|ServerProviderFactory
operator|.
name|getInstance
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|subResources
operator|.
name|putIfAbsent
argument_list|(
name|key
argument_list|,
name|cri
argument_list|)
expr_stmt|;
block|}
block|}
comment|// this branch will run only if ResourceContext is used
comment|// or static resolution is enabled for subresources initialized
comment|// from within singleton root resources (not default)
if|if
condition|(
name|resolveContexts
operator|&&
name|cri
operator|!=
literal|null
operator|&&
name|cri
operator|.
name|isSingleton
argument_list|()
operator|&&
name|instance
operator|!=
literal|null
operator|&&
name|cri
operator|.
name|contextsAvailable
argument_list|()
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
operator|!
name|injectedSubInstances
operator|.
name|contains
argument_list|(
name|instance
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
name|Application
name|app
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|ProviderInfo
argument_list|<
name|?
argument_list|>
name|appProvider
init|=
operator|(
name|ProviderInfo
argument_list|<
name|?
argument_list|>
operator|)
name|message
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
if|if
condition|(
name|appProvider
operator|!=
literal|null
condition|)
block|{
name|app
operator|=
operator|(
name|Application
operator|)
name|appProvider
operator|.
name|getProvider
argument_list|()
expr_stmt|;
block|}
block|}
name|InjectionUtils
operator|.
name|injectContextProxiesAndApplication
argument_list|(
name|cri
argument_list|,
name|instance
argument_list|,
name|app
argument_list|)
expr_stmt|;
name|injectedSubInstances
operator|.
name|add
argument_list|(
name|instance
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|cri
return|;
block|}
specifier|public
name|void
name|addSubClassResourceInfo
parameter_list|(
name|ClassResourceInfo
name|cri
parameter_list|)
block|{
name|subResources
operator|.
name|putIfAbsent
argument_list|(
operator|new
name|SubresourceKey
argument_list|(
name|cri
operator|.
name|getResourceClass
argument_list|()
argument_list|,
name|cri
operator|.
name|getServiceClass
argument_list|()
argument_list|)
argument_list|,
name|cri
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|ClassResourceInfo
argument_list|>
name|getSubResources
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|subResources
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getNameBindings
parameter_list|()
block|{
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
return|return
name|nameBindings
return|;
block|}
else|else
block|{
return|return
name|parent
operator|.
name|getNameBindings
argument_list|()
return|;
block|}
block|}
specifier|public
name|void
name|setNameBindings
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|names
parameter_list|)
block|{
name|nameBindings
operator|=
name|names
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getAllowedMethods
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|methods
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|OperationResourceInfo
name|o
range|:
name|methodDispatcher
operator|.
name|getOperationResourceInfos
argument_list|()
control|)
block|{
name|String
name|method
init|=
name|o
operator|.
name|getHttpMethod
argument_list|()
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|methods
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|methods
return|;
block|}
specifier|public
name|URITemplate
name|getURITemplate
parameter_list|()
block|{
return|return
name|uriTemplate
return|;
block|}
specifier|public
name|void
name|setURITemplate
parameter_list|(
name|URITemplate
name|u
parameter_list|)
block|{
name|uriTemplate
operator|=
name|u
expr_stmt|;
block|}
specifier|public
name|MethodDispatcher
name|getMethodDispatcher
parameter_list|()
block|{
return|return
name|methodDispatcher
return|;
block|}
specifier|public
name|void
name|setMethodDispatcher
parameter_list|(
name|MethodDispatcher
name|md
parameter_list|)
block|{
name|methodDispatcher
operator|=
name|md
expr_stmt|;
block|}
specifier|public
name|boolean
name|hasSubResources
parameter_list|()
block|{
return|return
operator|!
name|subResources
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isCreatedFromModel
parameter_list|()
block|{
return|return
name|createdFromModel
return|;
block|}
specifier|public
name|ResourceProvider
name|getResourceProvider
parameter_list|()
block|{
return|return
name|resourceProvider
return|;
block|}
specifier|public
name|void
name|setResourceProvider
parameter_list|(
name|ResourceProvider
name|rp
parameter_list|)
block|{
name|resourceProvider
operator|=
name|rp
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|MediaType
argument_list|>
name|getProduceMime
parameter_list|()
block|{
if|if
condition|(
name|producesTypes
operator|!=
literal|null
condition|)
block|{
return|return
name|JAXRSUtils
operator|.
name|parseMediaTypes
argument_list|(
name|producesTypes
argument_list|)
return|;
block|}
name|Produces
name|produces
init|=
name|AnnotationUtils
operator|.
name|getClassAnnotation
argument_list|(
name|getServiceClass
argument_list|()
argument_list|,
name|Produces
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|produces
operator|!=
literal|null
operator|||
name|parent
operator|==
literal|null
condition|)
block|{
return|return
name|JAXRSUtils
operator|.
name|getProduceTypes
argument_list|(
name|produces
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|parent
operator|.
name|getProduceMime
argument_list|()
return|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|MediaType
argument_list|>
name|getConsumeMime
parameter_list|()
block|{
if|if
condition|(
name|consumesTypes
operator|!=
literal|null
condition|)
block|{
return|return
name|JAXRSUtils
operator|.
name|parseMediaTypes
argument_list|(
name|consumesTypes
argument_list|)
return|;
block|}
name|Consumes
name|consumes
init|=
name|AnnotationUtils
operator|.
name|getClassAnnotation
argument_list|(
name|getServiceClass
argument_list|()
argument_list|,
name|Consumes
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|consumes
operator|!=
literal|null
operator|||
name|parent
operator|==
literal|null
condition|)
block|{
return|return
name|JAXRSUtils
operator|.
name|getConsumeTypes
argument_list|(
name|consumes
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|parent
operator|.
name|getConsumeMime
argument_list|()
return|;
block|}
block|}
specifier|public
name|Path
name|getPath
parameter_list|()
block|{
return|return
name|AnnotationUtils
operator|.
name|getClassAnnotation
argument_list|(
name|getServiceClass
argument_list|()
argument_list|,
name|Path
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
return|return
name|resourceProvider
operator|!=
literal|null
operator|&&
name|resourceProvider
operator|.
name|isSingleton
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|parent
operator|.
name|isSingleton
argument_list|()
return|;
block|}
block|}
specifier|public
name|void
name|setParent
parameter_list|(
name|ClassResourceInfo
name|parent
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
block|}
specifier|public
name|ClassResourceInfo
name|getParent
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
specifier|public
name|void
name|initBeanParamInfo
parameter_list|(
name|ServerProviderFactory
name|factory
parameter_list|)
block|{
if|if
condition|(
name|factory
operator|!=
literal|null
condition|)
block|{
name|Set
argument_list|<
name|OperationResourceInfo
argument_list|>
name|oris
init|=
name|getMethodDispatcher
argument_list|()
operator|.
name|getOperationResourceInfos
argument_list|()
decl_stmt|;
for|for
control|(
name|OperationResourceInfo
name|ori
range|:
name|oris
control|)
block|{
name|List
argument_list|<
name|Parameter
argument_list|>
name|params
init|=
name|ori
operator|.
name|getParameters
argument_list|()
decl_stmt|;
for|for
control|(
name|Parameter
name|param
range|:
name|params
control|)
block|{
if|if
condition|(
name|param
operator|.
name|getType
argument_list|()
operator|==
name|ParameterType
operator|.
name|BEAN
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
operator|.
name|getParameterTypes
argument_list|()
index|[
name|param
operator|.
name|getIndex
argument_list|()
index|]
decl_stmt|;
name|BeanParamInfo
name|bpi
init|=
operator|new
name|BeanParamInfo
argument_list|(
name|cls
argument_list|,
name|getBus
argument_list|()
argument_list|)
decl_stmt|;
name|factory
operator|.
name|addBeanParamInfo
argument_list|(
name|bpi
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|List
argument_list|<
name|Method
argument_list|>
name|methods
init|=
name|super
operator|.
name|getParameterMethods
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|m
range|:
name|methods
control|)
block|{
if|if
condition|(
name|m
operator|.
name|getAnnotation
argument_list|(
name|BeanParam
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|BeanParamInfo
name|bpi
init|=
operator|new
name|BeanParamInfo
argument_list|(
name|m
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
argument_list|,
name|getBus
argument_list|()
argument_list|)
decl_stmt|;
name|factory
operator|.
name|addBeanParamInfo
argument_list|(
name|bpi
argument_list|)
expr_stmt|;
block|}
block|}
name|List
argument_list|<
name|Field
argument_list|>
name|fields
init|=
name|super
operator|.
name|getParameterFields
argument_list|()
decl_stmt|;
for|for
control|(
name|Field
name|f
range|:
name|fields
control|)
block|{
if|if
condition|(
name|f
operator|.
name|getAnnotation
argument_list|(
name|BeanParam
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|BeanParamInfo
name|bpi
init|=
operator|new
name|BeanParamInfo
argument_list|(
name|f
operator|.
name|getType
argument_list|()
argument_list|,
name|getBus
argument_list|()
argument_list|)
decl_stmt|;
name|factory
operator|.
name|addBeanParamInfo
argument_list|(
name|bpi
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|clearThreadLocalProxies
parameter_list|()
block|{
name|super
operator|.
name|clearThreadLocalProxies
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|injectedSubInstances
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|ClassResourceInfo
name|sub
range|:
name|subResources
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|sub
operator|!=
name|this
condition|)
block|{
name|sub
operator|.
name|clearThreadLocalProxies
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|void
name|injectContexts
parameter_list|(
name|Object
name|resourceObject
parameter_list|,
name|OperationResourceInfo
name|ori
parameter_list|,
name|Message
name|inMessage
parameter_list|)
block|{
specifier|final
name|boolean
name|contextsAvailable
init|=
name|contextsAvailable
argument_list|()
decl_stmt|;
specifier|final
name|boolean
name|paramsAvailable
init|=
name|paramsAvailable
argument_list|()
decl_stmt|;
if|if
condition|(
name|contextsAvailable
operator|||
name|paramsAvailable
condition|)
block|{
name|Object
name|realResourceObject
init|=
name|ClassHelper
operator|.
name|getRealObject
argument_list|(
name|resourceObject
argument_list|)
decl_stmt|;
if|if
condition|(
name|paramsAvailable
condition|)
block|{
name|JAXRSUtils
operator|.
name|injectParameters
argument_list|(
name|ori
argument_list|,
name|this
argument_list|,
name|realResourceObject
argument_list|,
name|inMessage
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|contextsAvailable
condition|)
block|{
name|InjectionUtils
operator|.
name|injectContexts
argument_list|(
name|realResourceObject
argument_list|,
name|this
argument_list|,
name|inMessage
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

