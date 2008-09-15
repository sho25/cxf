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
name|frontend
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
name|net
operator|.
name|URL
import|;
end_import

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
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
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
name|Document
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
name|databinding
operator|.
name|AbstractDataBinding
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
name|databinding
operator|.
name|DataBinding
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
name|DOMUtils
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
name|jaxb
operator|.
name|JAXBDataBinding
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
name|resource
operator|.
name|ResourceManager
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
name|resource
operator|.
name|URIResolver
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
name|ReflectionServiceFactoryBean
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
name|BeanInvoker
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
name|FactoryInvoker
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
name|SingletonFactory
import|;
end_import

begin_comment
comment|/**  * This class helps take a {@link org.apache.cxf.service.Service} and  * expose as a server side endpoint.  * If there is no Service, it can create one for you using a  * {@link ReflectionServiceFactoryBean}.  *<p>  * For most scenarios you'll want to just have the ServerFactoryBean handle everything  * for you. In such a case, usage might look like this:  *</p>  *<pre>  * ServerFactoryBean sf = new ServerFactoryBean();  * sf.setServiceClass(MyService.class);  * sf.setAddress("http://localhost:8080/MyService");  * sf.create();  *</pre>  *<p>  * You can also get more advanced and customize the service factory used:  *<pre>  * ReflectionServiceFactory serviceFactory = new ReflectionServiceFactory();  * serviceFactory.setServiceClass(MyService.class);  * ..  * \/\/ Customize service factory here...  * serviceFactory.setWrapped(false);  * ...  * ServerFactoryBean sf = new ServerFactoryBean();  * sf.setServiceFactory(serviceFactory);  * sf.setAddress("http://localhost:8080/MyService");  * sf.create();  *</pre>  */
end_comment

begin_class
specifier|public
class|class
name|ServerFactoryBean
extends|extends
name|AbstractWSDLBasedEndpointFactory
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
name|ServerFactoryBean
operator|.
name|class
argument_list|)
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
name|Object
name|serviceBean
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|schemaLocations
decl_stmt|;
specifier|private
name|Invoker
name|invoker
decl_stmt|;
specifier|public
name|ServerFactoryBean
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|ReflectionServiceFactoryBean
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ServerFactoryBean
parameter_list|(
name|ReflectionServiceFactoryBean
name|sbean
parameter_list|)
block|{
name|super
argument_list|(
name|sbean
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getBeanName
parameter_list|()
block|{
return|return
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|Server
name|create
parameter_list|()
block|{
try|try
block|{
name|applyExtraClass
argument_list|()
expr_stmt|;
if|if
condition|(
name|serviceBean
operator|!=
literal|null
operator|&&
name|getServiceClass
argument_list|()
operator|==
literal|null
condition|)
block|{
name|setServiceClass
argument_list|(
name|ClassHelper
operator|.
name|getRealClass
argument_list|(
name|serviceBean
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|invoker
operator|!=
literal|null
condition|)
block|{
name|getServiceFactory
argument_list|()
operator|.
name|setInvoker
argument_list|(
name|invoker
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|serviceBean
operator|!=
literal|null
condition|)
block|{
name|getServiceFactory
argument_list|()
operator|.
name|setInvoker
argument_list|(
name|createInvoker
argument_list|()
argument_list|)
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
if|if
condition|(
name|ep
operator|.
name|getService
argument_list|()
operator|.
name|getInvoker
argument_list|()
operator|==
literal|null
condition|)
block|{
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
if|if
condition|(
name|serviceBean
operator|!=
literal|null
condition|)
block|{
name|initializeAnnotationInterceptors
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
argument_list|,
name|ClassHelper
operator|.
name|getRealClass
argument_list|(
name|getServiceBean
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getServiceClass
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|initializeAnnotationInterceptors
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
argument_list|,
name|getServiceClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|applyFeatures
argument_list|()
expr_stmt|;
return|return
name|server
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|initializeServiceFactory
parameter_list|()
block|{
name|super
operator|.
name|initializeServiceFactory
argument_list|()
expr_stmt|;
name|DataBinding
name|db
init|=
name|getServiceFactory
argument_list|()
operator|.
name|getDataBinding
argument_list|()
decl_stmt|;
if|if
condition|(
name|db
operator|instanceof
name|AbstractDataBinding
operator|&&
name|schemaLocations
operator|!=
literal|null
condition|)
block|{
name|ResourceManager
name|rr
init|=
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|ResourceManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DOMSource
argument_list|>
name|schemas
init|=
operator|new
name|ArrayList
argument_list|<
name|DOMSource
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|l
range|:
name|schemaLocations
control|)
block|{
name|URL
name|url
init|=
name|rr
operator|.
name|resolveResource
argument_list|(
name|l
argument_list|,
name|URL
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|URIResolver
name|res
decl_stmt|;
try|try
block|{
name|res
operator|=
operator|new
name|URIResolver
argument_list|(
name|l
argument_list|)
expr_stmt|;
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
operator|new
name|Message
argument_list|(
literal|"INVALID_SCHEMA_URL"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|res
operator|.
name|isResolved
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"INVALID_SCHEMA_URL"
argument_list|,
name|LOG
argument_list|)
argument_list|)
throw|;
block|}
name|url
operator|=
name|res
operator|.
name|getURL
argument_list|()
expr_stmt|;
block|}
name|Document
name|d
decl_stmt|;
try|try
block|{
name|d
operator|=
name|DOMUtils
operator|.
name|readXml
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|)
expr_stmt|;
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
operator|new
name|Message
argument_list|(
literal|"ERROR_READING_SCHEMA"
argument_list|,
name|LOG
argument_list|,
name|l
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|schemas
operator|.
name|add
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|d
argument_list|,
name|url
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|AbstractDataBinding
operator|)
name|db
operator|)
operator|.
name|setSchemas
argument_list|(
name|schemas
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
name|void
name|applyExtraClass
parameter_list|()
block|{
name|DataBinding
name|dataBinding
init|=
name|getServiceFactory
argument_list|()
operator|.
name|getDataBinding
argument_list|()
decl_stmt|;
if|if
condition|(
name|dataBinding
operator|instanceof
name|JAXBDataBinding
condition|)
block|{
name|Map
name|props
init|=
name|this
operator|.
name|getProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|props
operator|!=
literal|null
operator|&&
name|props
operator|.
name|get
argument_list|(
literal|"jaxb.additionalContextClasses"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|Class
index|[]
name|extraClass
init|=
operator|(
name|Class
index|[]
operator|)
name|this
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
literal|"jaxb.additionalContextClasses"
argument_list|)
decl_stmt|;
operator|(
operator|(
name|JAXBDataBinding
operator|)
name|dataBinding
operator|)
operator|.
name|setExtraClass
argument_list|(
name|extraClass
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
if|if
condition|(
name|getServiceBean
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|FactoryInvoker
argument_list|(
operator|new
name|SingletonFactory
argument_list|(
name|getServiceClass
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
return|return
operator|new
name|BeanInvoker
argument_list|(
name|getServiceBean
argument_list|()
argument_list|)
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
specifier|public
name|void
name|setServer
parameter_list|(
name|Server
name|server
parameter_list|)
block|{
name|this
operator|.
name|server
operator|=
name|server
expr_stmt|;
block|}
comment|/**      * Whether or not the Server should be started upon creation.      *      * @return<code>false</code> if the server should not be started upon creation      */
specifier|public
name|boolean
name|isStart
parameter_list|()
block|{
return|return
name|start
return|;
block|}
comment|/**      * Specifies if the Server should be started upon creation. The       * default is for Servers to be started upon creation. Passing       *<code>false</code> tells the factory that the Server will be       * started manually using the start method.      *      * @param start<code>false</code> specifies that the Server will not be started upon creation      */
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
specifier|public
name|Object
name|getServiceBean
parameter_list|()
block|{
return|return
name|serviceBean
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getServiceBeanClass
parameter_list|()
block|{
if|if
condition|(
name|serviceBean
operator|!=
literal|null
condition|)
block|{
return|return
name|ClassHelper
operator|.
name|getRealClass
argument_list|(
name|serviceBean
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|getServiceFactory
argument_list|()
operator|.
name|getServiceClass
argument_list|()
return|;
block|}
block|}
comment|/**      * Sets the bean implementing the service. If this is set a       *<code>BeanInvoker</code> is created for the provided bean.      *      * @param serviceBean an instantiated implementaiton object      */
specifier|public
name|void
name|setServiceBean
parameter_list|(
name|Object
name|serviceBean
parameter_list|)
block|{
name|this
operator|.
name|serviceBean
operator|=
name|serviceBean
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSchemaLocations
parameter_list|()
block|{
return|return
name|schemaLocations
return|;
block|}
specifier|public
name|void
name|setSchemaLocations
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|schemaLocations
parameter_list|)
block|{
name|this
operator|.
name|schemaLocations
operator|=
name|schemaLocations
expr_stmt|;
block|}
specifier|public
name|Invoker
name|getInvoker
parameter_list|()
block|{
return|return
name|invoker
return|;
block|}
specifier|public
name|void
name|setInvoker
parameter_list|(
name|Invoker
name|invoker
parameter_list|)
block|{
name|this
operator|.
name|invoker
operator|=
name|invoker
expr_stmt|;
block|}
comment|/**      * Specifies the location of the WSDL defining the service interface       * used by the factory to create services. Typically, the WSDL       * location is specified as a URL.      *       * @param locaiton the URL of the WSDL defining the service interface      */
specifier|public
name|void
name|setWsdlLocation
parameter_list|(
name|String
name|location
parameter_list|)
block|{
name|setWsdlURL
argument_list|(
name|location
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getWsdlLocation
parameter_list|()
block|{
return|return
name|getWsdlURL
argument_list|()
return|;
block|}
block|}
end_class

end_unit

