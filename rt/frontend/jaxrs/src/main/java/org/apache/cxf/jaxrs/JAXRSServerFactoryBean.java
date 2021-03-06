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
name|LinkedHashSet
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
name|Set
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
name|lifecycle
operator|.
name|SingletonResourceProvider
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
name|ApplicationInfo
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
name|service
operator|.
name|factory
operator|.
name|FactoryBeanListener
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
comment|/**  * Bean to help easily create Server endpoints for JAX-RS. Example:  *<pre>  * JAXRSServerFactoryBean sf = JAXRSServerFactoryBean();  * sf.setResourceClasses(Book.class);  * sf.setBindingId(JAXRSBindingFactory.JAXRS_BINDING_ID);  * sf.setAddress("http://localhost:9080/");  * Server myServer = sf.create();  *</pre>  * This will start a server for you and register it with the ServerManager.  Note  * you should explicitly close the {@link org.apache.cxf.endpoint.Server} created  * when finished with it:  *<pre>  * myServer.close();  * myServer.destroy(); // closes first if close() not previously called  *</pre>  */
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
argument_list|<
name|?
argument_list|>
argument_list|,
name|ResourceProvider
argument_list|>
name|resourceProviders
init|=
operator|new
name|HashMap
argument_list|<>
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
specifier|private
name|ApplicationInfo
name|appProvider
decl_stmt|;
specifier|private
name|String
name|documentLocation
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
comment|/**      * Saves the reference to the JAX-RS {@link Application}      * @param app      */
specifier|public
name|void
name|setApplication
parameter_list|(
name|Application
name|app
parameter_list|)
block|{
name|setApplicationInfo
argument_list|(
operator|new
name|ApplicationInfo
argument_list|(
name|app
argument_list|,
name|getBus
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setApplicationInfo
parameter_list|(
name|ApplicationInfo
name|provider
parameter_list|)
block|{
name|appProvider
operator|=
name|provider
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|appNameBindings
init|=
name|AnnotationUtils
operator|.
name|getNameBindings
argument_list|(
name|provider
operator|.
name|getProvider
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotations
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ClassResourceInfo
name|cri
range|:
name|getServiceFactory
argument_list|()
operator|.
name|getClassResourceInfo
argument_list|()
control|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|clsNameBindings
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|(
name|appNameBindings
argument_list|)
decl_stmt|;
name|clsNameBindings
operator|.
name|addAll
argument_list|(
name|AnnotationUtils
operator|.
name|getNameBindings
argument_list|(
name|cri
operator|.
name|getServiceClass
argument_list|()
operator|.
name|getAnnotations
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cri
operator|.
name|setNameBindings
argument_list|(
name|clsNameBindings
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Resource comparator which may be used to customize the way      * a root resource or resource method is selected      * @param rcomp comparator      */
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
comment|/**      * By default the subresources are resolved dynamically, mainly due to      * the JAX-RS specification allowing Objects being returned from the subresource      * locators. Setting this property to true enables the runtime to do the      * early resolution.      *      * @param enableStatic enabling the static resolution if set to true      */
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
comment|/**      * Creates the JAX-RS Server instance      * @return the server      */
specifier|public
name|void
name|init
parameter_list|()
block|{
if|if
condition|(
name|server
operator|==
literal|null
condition|)
block|{
name|create
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Creates the JAX-RS Server instance      * @return the server      */
specifier|public
name|Server
name|create
parameter_list|()
block|{
name|ClassLoaderHolder
name|origLoader
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Bus
name|bus
init|=
name|getBus
argument_list|()
decl_stmt|;
name|ClassLoader
name|loader
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ClassLoader
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
name|origLoader
operator|=
name|ClassLoaderUtils
operator|.
name|setThreadContextClassloader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
block|}
name|serviceFactory
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
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
name|create
argument_list|()
expr_stmt|;
block|}
name|Endpoint
name|ep
init|=
name|createEndpoint
argument_list|()
decl_stmt|;
name|getServiceFactory
argument_list|()
operator|.
name|sendEvent
argument_list|(
name|FactoryBeanListener
operator|.
name|Event
operator|.
name|PRE_SERVER_CREATE
argument_list|,
name|server
argument_list|)
expr_stmt|;
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
name|ServerProviderFactory
name|factory
init|=
name|setupFactory
argument_list|(
name|ep
argument_list|)
decl_stmt|;
name|ep
operator|.
name|put
argument_list|(
name|Application
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|appProvider
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
name|ep
operator|.
name|put
argument_list|(
name|Bus
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|documentLocation
operator|!=
literal|null
condition|)
block|{
name|ep
operator|.
name|put
argument_list|(
name|JAXRSUtils
operator|.
name|DOC_LOCATION
argument_list|,
name|documentLocation
argument_list|)
expr_stmt|;
block|}
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
name|checkPrivateEndpoint
argument_list|(
name|ep
argument_list|)
expr_stmt|;
name|applyBusFeatures
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|applyFeatures
argument_list|()
expr_stmt|;
name|updateClassResourceProviders
argument_list|(
name|ep
argument_list|)
expr_stmt|;
name|injectContexts
argument_list|(
name|factory
argument_list|,
operator|(
name|ApplicationInfo
operator|)
name|ep
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
argument_list|)
expr_stmt|;
name|factory
operator|.
name|applyDynamicFeatures
argument_list|(
name|getServiceFactory
argument_list|()
operator|.
name|getClassResourceInfo
argument_list|()
argument_list|)
expr_stmt|;
name|getServiceFactory
argument_list|()
operator|.
name|sendEvent
argument_list|(
name|FactoryBeanListener
operator|.
name|Event
operator|.
name|SERVER_CREATED
argument_list|,
name|server
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|start
condition|)
block|{
try|try
block|{
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|re
parameter_list|)
block|{
name|server
operator|.
name|destroy
argument_list|()
expr_stmt|;
comment|// prevent resource leak
throw|throw
name|re
throw|;
block|}
block|}
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
finally|finally
block|{
if|if
condition|(
name|origLoader
operator|!=
literal|null
condition|)
block|{
name|origLoader
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|server
return|;
block|}
specifier|public
name|Server
name|getServer
parameter_list|()
block|{
return|return
name|server
return|;
block|}
specifier|protected
name|ServerProviderFactory
name|setupFactory
parameter_list|(
name|Endpoint
name|ep
parameter_list|)
block|{
name|ServerProviderFactory
name|factory
init|=
name|ServerProviderFactory
operator|.
name|createInstance
argument_list|(
name|getBus
argument_list|()
argument_list|)
decl_stmt|;
name|setBeanInfo
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setApplicationProvider
argument_list|(
name|appProvider
argument_list|)
expr_stmt|;
name|super
operator|.
name|setupFactory
argument_list|(
name|factory
argument_list|,
name|ep
argument_list|)
expr_stmt|;
return|return
name|factory
return|;
block|}
specifier|protected
name|void
name|setBeanInfo
parameter_list|(
name|ServerProviderFactory
name|factory
parameter_list|)
block|{
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|cris
init|=
name|serviceFactory
operator|.
name|getClassResourceInfo
argument_list|()
decl_stmt|;
for|for
control|(
name|ClassResourceInfo
name|cri
range|:
name|cris
control|)
block|{
name|cri
operator|.
name|initBeanParamInfo
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|applyBusFeatures
parameter_list|(
specifier|final
name|Bus
name|bus
parameter_list|)
block|{
if|if
condition|(
name|bus
operator|.
name|getFeatures
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Feature
name|feature
range|:
name|bus
operator|.
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
name|bus
argument_list|)
expr_stmt|;
block|}
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
name|Feature
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
name|serviceFactory
operator|.
name|createInvoker
argument_list|()
return|;
block|}
comment|/**      * Sets the language mappings,      * example, 'en' is the key and 'en-gb' is the value.      *      * @param lMaps the language mappings      */
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
comment|/**      * Sets the extension mappings,      * example, 'xml' is the key and 'text/xml' is the value.      *      * @param extMaps the extension mappings      */
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
comment|/**      * This method is used primarily by Spring handler processing      * the jaxrs:server/@serviceClass attribute. It delegates to      * setResourceClasses method accepting the array of Class parameters.      * @param clazz the service/resource class      */
specifier|public
name|void
name|setServiceClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
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
comment|/**      * Sets one or more root resource classes      * @param classes the list of resource classes      */
specifier|public
name|void
name|setResourceClasses
parameter_list|(
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
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
comment|/**      * Sets one or more root resource classes      * @param classes the array of resource classes      */
specifier|public
name|void
name|setResourceClasses
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
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
comment|/**      * Sets the resource beans. If this is set then the JAX-RS runtime      * will not be responsible for the life-cycle of resource classes.      *      * @param beans the array of resource instances      */
specifier|public
name|void
name|setServiceBeanObjects
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
comment|/**      * Sets the single resource bean. If this is set then the JAX-RS runtime      * will not be responsible for the life-cycle of resource classes.      * Please avoid setting the resource class of this bean explicitly,      * the runtime will determine it itself.      *      * @param bean resource instance      */
specifier|public
name|void
name|setServiceBean
parameter_list|(
name|Object
name|bean
parameter_list|)
block|{
name|setServiceBeans
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|bean
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the resource beans. If this is set then the JAX-RS runtime      * will not be responsible for the life-cycle of resource classes.      *      * @param beans the list of resource instances      */
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
name|List
argument_list|<
name|Object
argument_list|>
name|newBeans
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|addToBeans
argument_list|(
name|newBeans
argument_list|,
name|beans
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|setResourceClassesFromBeans
argument_list|(
name|newBeans
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the provider managing the life-cycle of the given resource class      *<pre>      * Example:      *  setResourceProvider(BookStoreInterface.class, new SingletonResourceProvider(new BookStore()));      *</pre>      * @param c resource class      * @param rp resource provider      */
specifier|public
name|void
name|setResourceProvider
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
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
comment|/**      * Sets the provider managing the life-cycle of the resource class      *<pre>      * Example:      *  setResourceProvider(new SingletonResourceProvider(new BookStore()));      *</pre>      * @param rp resource provider      */
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
comment|/**      * Sets the list of providers managing the life-cycle of the resource classes      *      * @param rps resource providers      */
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
comment|/**      * Sets the custom Invoker which can be used to customize the way      * the default JAX-RS invoker calls on the service bean      * @param invoker      */
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
comment|/**      * Determines whether Services are automatically started during the create() call.  Default is true.      * If false will need to call start() method on Server to activate it.      * @param start Whether (true) or not (false) to start the Server during Server creation.      */
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
specifier|protected
name|void
name|injectContexts
parameter_list|(
name|ServerProviderFactory
name|factory
parameter_list|,
name|ApplicationInfo
name|fallback
parameter_list|)
block|{
comment|// Sometimes the application provider (ApplicationInfo) is injected through
comment|// the endpoint, not JAXRSServerFactoryBean (like for example OpenApiFeature
comment|// or Swagger2Feature do). As such, without consulting the endpoint, the injection
comment|// may not work properly.
specifier|final
name|ApplicationInfo
name|appInfoProvider
init|=
operator|(
name|appProvider
operator|==
literal|null
operator|)
condition|?
name|fallback
else|:
name|appProvider
decl_stmt|;
specifier|final
name|Application
name|application
init|=
name|appInfoProvider
operator|==
literal|null
condition|?
literal|null
else|:
name|appInfoProvider
operator|.
name|getProvider
argument_list|()
decl_stmt|;
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
name|injectContextProxiesAndApplication
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
argument_list|,
name|application
argument_list|,
name|factory
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|application
operator|!=
literal|null
condition|)
block|{
name|InjectionUtils
operator|.
name|injectContextProxiesAndApplication
argument_list|(
name|appInfoProvider
argument_list|,
name|application
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|updateClassResourceProviders
parameter_list|(
name|Endpoint
name|ep
parameter_list|)
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
operator|==
literal|null
condition|)
block|{
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
name|setDefaultResourceProvider
argument_list|(
name|cri
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|cri
operator|.
name|getResourceProvider
argument_list|()
operator|instanceof
name|SingletonResourceProvider
condition|)
block|{
operator|(
operator|(
name|SingletonResourceProvider
operator|)
name|cri
operator|.
name|getResourceProvider
argument_list|()
operator|)
operator|.
name|init
argument_list|(
name|ep
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|setDefaultResourceProvider
parameter_list|(
name|ClassResourceInfo
name|cri
parameter_list|)
block|{
name|cri
operator|.
name|setResourceProvider
argument_list|(
operator|new
name|PerRequestResourceProvider
argument_list|(
name|cri
operator|.
name|getResourceClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the reference to the document (WADL, etc) describing the endpoint      * @param docLocation document location      */
specifier|public
name|void
name|setDocLocation
parameter_list|(
name|String
name|docLocation
parameter_list|)
block|{
name|this
operator|.
name|documentLocation
operator|=
name|docLocation
expr_stmt|;
block|}
comment|/**      * Get the reference to the document (WADL, etc) describing the endpoint      * @return document location      */
specifier|public
name|String
name|getDocLocation
parameter_list|()
block|{
return|return
name|documentLocation
return|;
block|}
block|}
end_class

end_unit

