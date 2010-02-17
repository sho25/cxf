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
name|LinkedList
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|BusException
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
name|endpoint
operator|.
name|Endpoint
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
name|endpoint
operator|.
name|EndpointException
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
name|endpoint
operator|.
name|Server
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
name|endpoint
operator|.
name|ServerImpl
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
name|feature
operator|.
name|AbstractFeature
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
name|ext
operator|.
name|ResourceComparator
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
name|impl
operator|.
name|RequestPreprocessor
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
name|PerRequestResourceProvider
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
name|ClassResourceInfo
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
name|ProviderFactory
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
name|message
operator|.
name|MessageUtils
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
name|service
operator|.
name|factory
operator|.
name|ServiceConstructionException
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
name|service
operator|.
name|invoker
operator|.
name|Invoker
import|;
end_import

begin_comment
comment|/**  * Bean to help easily create Server endpoints for JAX-RS. Example:  *<pre>  * JAXRSServerFactoryBean sf = JAXRSServerFactoryBean();  * sf.setResourceClasses(Book.class);  * sf.setBindingId(JAXRSBindingFactory.JAXRS_BINDING_ID);  * sf.setAddress("http://localhost:9080/");  * sf.create();  *</pre>  * This will start a server for you and register it with the ServerManager.  */
end_comment

begin_class
specifier|public
class|class
name|JAXRSServerFactoryBean
extends|extends
name|AbstractJAXRSFactoryBean
block|{
specifier|protected
name|Map
argument_list|<
name|Class
argument_list|,
name|ResourceProvider
argument_list|>
name|resourceProviders
init|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|,
name|ResourceProvider
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Server
name|server
decl_stmt|;
specifier|private
name|boolean
name|start
init|=
literal|true
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|languageMappings
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|extensionMappings
decl_stmt|;
specifier|private
name|ResourceComparator
name|rc
decl_stmt|;
specifier|public
name|JAXRSServerFactoryBean
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|JAXRSServiceFactoryBean
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JAXRSServerFactoryBean
parameter_list|(
name|JAXRSServiceFactoryBean
name|sf
parameter_list|)
block|{
name|super
argument_list|(
name|sf
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setResourceComparator
parameter_list|(
name|ResourceComparator
name|rcomp
parameter_list|)
block|{
name|rc
operator|=
name|rcomp
expr_stmt|;
block|}
specifier|public
name|void
name|setStaticSubresourceResolution
parameter_list|(
name|boolean
name|enableStatic
parameter_list|)
block|{
name|serviceFactory
operator|.
name|setEnableStaticResolution
argument_list|(
name|enableStatic
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Server
name|create
parameter_list|()
block|{
try|try
block|{
name|checkResources
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|serviceFactory
operator|.
name|getService
argument_list|()
operator|==
literal|null
condition|)
block|{
name|serviceFactory
operator|.
name|setServiceName
argument_list|(
name|getServiceName
argument_list|()
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|create
argument_list|()
expr_stmt|;
name|updateClassResourceProviders
argument_list|()
expr_stmt|;
block|}
name|Endpoint
name|ep
init|=
name|createEndpoint
argument_list|()
decl_stmt|;
name|server
operator|=
operator|new
name|ServerImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|ep
argument_list|,
name|getDestinationFactory
argument_list|()
argument_list|,
name|getBindingFactory
argument_list|()
argument_list|)
expr_stmt|;
name|Invoker
name|invoker
init|=
name|serviceFactory
operator|.
name|getInvoker
argument_list|()
decl_stmt|;
if|if
condition|(
name|invoker
operator|==
literal|null
condition|)
block|{
name|ep
operator|.
name|getService
argument_list|()
operator|.
name|setInvoker
argument_list|(
name|createInvoker
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ep
operator|.
name|getService
argument_list|()
operator|.
name|setInvoker
argument_list|(
name|invoker
argument_list|)
expr_stmt|;
block|}
name|ProviderFactory
name|factory
init|=
name|setupFactory
argument_list|(
name|ep
argument_list|)
decl_stmt|;
name|checkIfPrivate
argument_list|(
name|factory
argument_list|,
name|ep
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setRequestPreprocessor
argument_list|(
operator|new
name|RequestPreprocessor
argument_list|(
name|languageMappings
argument_list|,
name|extensionMappings
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|rc
operator|!=
literal|null
condition|)
block|{
name|ep
operator|.
name|put
argument_list|(
literal|"org.apache.cxf.jaxrs.comparator"
argument_list|,
name|rc
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|start
condition|)
block|{
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|EndpointException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|BusException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|applyFeatures
argument_list|()
expr_stmt|;
return|return
name|server
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|protected
name|void
name|checkIfPrivate
parameter_list|(
name|ProviderFactory
name|factory
parameter_list|,
name|Endpoint
name|ep
parameter_list|)
block|{
if|if
condition|(
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|ep
operator|.
name|get
argument_list|(
literal|"org.apache.cxf.endpoint.private"
argument_list|)
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|addresses
init|=
operator|(
name|List
argument_list|<
name|String
argument_list|>
operator|)
name|getBus
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"org.apache.cxf.private.endpoints"
argument_list|)
decl_stmt|;
if|if
condition|(
name|addresses
operator|==
literal|null
condition|)
block|{
name|addresses
operator|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|addresses
operator|.
name|add
argument_list|(
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|bus
operator|.
name|setProperty
argument_list|(
literal|"org.apache.cxf.private.endpoints"
argument_list|,
name|addresses
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|applyFeatures
parameter_list|()
block|{
if|if
condition|(
name|getFeatures
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AbstractFeature
name|feature
range|:
name|getFeatures
argument_list|()
control|)
block|{
name|feature
operator|.
name|initialize
argument_list|(
name|server
argument_list|,
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|Invoker
name|createInvoker
parameter_list|()
block|{
return|return
operator|new
name|JAXRSInvoker
argument_list|()
return|;
block|}
specifier|public
name|void
name|setLanguageMappings
parameter_list|(
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|lMaps
parameter_list|)
block|{
name|languageMappings
operator|=
name|lMaps
expr_stmt|;
block|}
specifier|public
name|void
name|setExtensionMappings
parameter_list|(
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|extMaps
parameter_list|)
block|{
name|extensionMappings
operator|=
name|extMaps
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getResourceClasses
parameter_list|()
block|{
return|return
name|serviceFactory
operator|.
name|getResourceClasses
argument_list|()
return|;
block|}
specifier|public
name|void
name|setServiceClass
parameter_list|(
name|Class
name|clazz
parameter_list|)
block|{
name|serviceFactory
operator|.
name|setResourceClasses
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setResourceClasses
parameter_list|(
name|List
argument_list|<
name|Class
argument_list|>
name|classes
parameter_list|)
block|{
name|serviceFactory
operator|.
name|setResourceClasses
argument_list|(
name|classes
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setResourceClasses
parameter_list|(
name|Class
modifier|...
name|classes
parameter_list|)
block|{
name|serviceFactory
operator|.
name|setResourceClasses
argument_list|(
name|classes
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the backing service bean. If this is set, JAX-RS runtime will not be      * responsible for the lifecycle of resource classes.      *       * @return      */
specifier|public
name|void
name|setServiceBeans
parameter_list|(
name|Object
modifier|...
name|beans
parameter_list|)
block|{
name|setServiceBeans
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|beans
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setServiceBeans
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|beans
parameter_list|)
block|{
name|serviceFactory
operator|.
name|setResourceClassesFromBeans
argument_list|(
name|beans
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setResourceProvider
parameter_list|(
name|Class
name|c
parameter_list|,
name|ResourceProvider
name|rp
parameter_list|)
block|{
name|resourceProviders
operator|.
name|put
argument_list|(
name|c
argument_list|,
name|rp
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setResourceProvider
parameter_list|(
name|ResourceProvider
name|rp
parameter_list|)
block|{
name|setResourceProviders
argument_list|(
name|CastUtils
operator|.
name|cast
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|rp
argument_list|)
argument_list|,
name|ResourceProvider
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setResourceProviders
parameter_list|(
name|List
argument_list|<
name|ResourceProvider
argument_list|>
name|rps
parameter_list|)
block|{
for|for
control|(
name|ResourceProvider
name|rp
range|:
name|rps
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|c
init|=
name|rp
operator|.
name|getResourceClass
argument_list|()
decl_stmt|;
name|setServiceClass
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|resourceProviders
operator|.
name|put
argument_list|(
name|c
argument_list|,
name|rp
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setInvoker
parameter_list|(
name|Invoker
name|invoker
parameter_list|)
block|{
name|serviceFactory
operator|.
name|setInvoker
argument_list|(
name|invoker
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setStart
parameter_list|(
name|boolean
name|start
parameter_list|)
block|{
name|this
operator|.
name|start
operator|=
name|start
expr_stmt|;
block|}
specifier|private
name|void
name|injectContexts
parameter_list|()
block|{
for|for
control|(
name|ClassResourceInfo
name|cri
range|:
name|serviceFactory
operator|.
name|getClassResourceInfo
argument_list|()
control|)
block|{
if|if
condition|(
name|cri
operator|.
name|isSingleton
argument_list|()
condition|)
block|{
name|InjectionUtils
operator|.
name|injectContextProxies
argument_list|(
name|cri
argument_list|,
name|cri
operator|.
name|getResourceProvider
argument_list|()
operator|.
name|getInstance
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|updateClassResourceProviders
parameter_list|()
block|{
for|for
control|(
name|ClassResourceInfo
name|cri
range|:
name|serviceFactory
operator|.
name|getClassResourceInfo
argument_list|()
control|)
block|{
if|if
condition|(
name|cri
operator|.
name|getResourceProvider
argument_list|()
operator|!=
literal|null
condition|)
block|{
continue|continue;
block|}
name|ResourceProvider
name|rp
init|=
name|resourceProviders
operator|.
name|get
argument_list|(
name|cri
operator|.
name|getResourceClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|rp
operator|!=
literal|null
condition|)
block|{
name|cri
operator|.
name|setResourceProvider
argument_list|(
name|rp
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//default lifecycle is per-request
name|rp
operator|=
operator|new
name|PerRequestResourceProvider
argument_list|(
name|cri
operator|.
name|getResourceClass
argument_list|()
argument_list|)
expr_stmt|;
name|cri
operator|.
name|setResourceProvider
argument_list|(
name|rp
argument_list|)
expr_stmt|;
block|}
block|}
name|injectContexts
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

