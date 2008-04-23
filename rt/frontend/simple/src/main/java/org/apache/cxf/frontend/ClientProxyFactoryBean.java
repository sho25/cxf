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
name|ArrayList
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
name|binding
operator|.
name|BindingConfiguration
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
name|security
operator|.
name|AuthorizationPolicy
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
name|Client
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
name|ConduitSelector
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
name|interceptor
operator|.
name|AbstractBasicInterceptorProvider
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

begin_comment
comment|/**  * This class will create a client for you which implements the specified  * service class. Example:  *<pre>  * ClientProxyFactoryBean factory = new ClientProxyFactoryBean();  * factory.setServiceClass(YourServiceInterface.class);  * YourServiceInterface client = (YourServiceInterface) factory.create();  *</pre>  * To access the underlying Client object:  *<pre>  * Client cxfClient = ClientProxy.getClient(client);  *</pre>  */
end_comment

begin_class
specifier|public
class|class
name|ClientProxyFactoryBean
extends|extends
name|AbstractBasicInterceptorProvider
block|{
specifier|private
name|ClientFactoryBean
name|clientFactoryBean
decl_stmt|;
specifier|private
name|String
name|username
decl_stmt|;
specifier|private
name|String
name|password
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|List
argument_list|<
name|AbstractFeature
argument_list|>
name|features
init|=
operator|new
name|ArrayList
argument_list|<
name|AbstractFeature
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|DataBinding
name|dataBinding
decl_stmt|;
specifier|public
name|ClientProxyFactoryBean
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|ClientFactoryBean
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ClientProxyFactoryBean
parameter_list|(
name|ClientFactoryBean
name|fact
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|clientFactoryBean
operator|=
name|fact
expr_stmt|;
block|}
specifier|public
name|void
name|initFeatures
parameter_list|()
block|{
name|this
operator|.
name|clientFactoryBean
operator|.
name|setFeatures
argument_list|(
name|features
argument_list|)
expr_stmt|;
name|this
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setFeatures
argument_list|(
name|features
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Object
name|create
parameter_list|()
block|{
if|if
condition|(
name|properties
operator|==
literal|null
condition|)
block|{
name|properties
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|username
operator|!=
literal|null
condition|)
block|{
name|AuthorizationPolicy
name|authPolicy
init|=
operator|new
name|AuthorizationPolicy
argument_list|()
decl_stmt|;
name|authPolicy
operator|.
name|setUserName
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|authPolicy
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|AuthorizationPolicy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|authPolicy
argument_list|)
expr_stmt|;
block|}
name|initFeatures
argument_list|()
expr_stmt|;
name|clientFactoryBean
operator|.
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|clientFactoryBean
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dataBinding
operator|!=
literal|null
condition|)
block|{
name|clientFactoryBean
operator|.
name|setDataBinding
argument_list|(
name|dataBinding
argument_list|)
expr_stmt|;
block|}
name|Client
name|c
init|=
name|clientFactoryBean
operator|.
name|create
argument_list|()
decl_stmt|;
if|if
condition|(
name|getInInterceptors
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|addAll
argument_list|(
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getOutInterceptors
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|addAll
argument_list|(
name|getOutInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getInFaultInterceptors
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|addAll
argument_list|(
name|getInFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getOutFaultInterceptors
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|addAll
argument_list|(
name|getOutFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ClientProxy
name|handler
init|=
name|clientClientProxy
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|Object
name|obj
init|=
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|clientFactoryBean
operator|.
name|getServiceClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|,
name|getImplementingClasses
argument_list|()
argument_list|,
name|handler
argument_list|)
decl_stmt|;
return|return
name|obj
return|;
block|}
specifier|protected
name|Class
index|[]
name|getImplementingClasses
parameter_list|()
block|{
name|Class
name|cls
init|=
name|clientFactoryBean
operator|.
name|getServiceClass
argument_list|()
decl_stmt|;
return|return
operator|new
name|Class
index|[]
block|{
name|cls
block|}
return|;
block|}
specifier|protected
name|ClientProxy
name|clientClientProxy
parameter_list|(
name|Client
name|c
parameter_list|)
block|{
return|return
operator|new
name|ClientProxy
argument_list|(
name|c
argument_list|)
return|;
block|}
specifier|public
name|ClientFactoryBean
name|getClientFactoryBean
parameter_list|()
block|{
return|return
name|clientFactoryBean
return|;
block|}
specifier|public
name|void
name|setClientFactoryBean
parameter_list|(
name|ClientFactoryBean
name|clientFactoryBean
parameter_list|)
block|{
name|this
operator|.
name|clientFactoryBean
operator|=
name|clientFactoryBean
expr_stmt|;
block|}
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
specifier|public
name|Class
name|getServiceClass
parameter_list|()
block|{
return|return
name|clientFactoryBean
operator|.
name|getServiceClass
argument_list|()
return|;
block|}
specifier|public
name|void
name|setServiceClass
parameter_list|(
name|Class
name|serviceClass
parameter_list|)
block|{
name|clientFactoryBean
operator|.
name|setServiceClass
argument_list|(
name|serviceClass
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
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
specifier|public
name|void
name|setWsdlLocation
parameter_list|(
name|String
name|wsdlURL
parameter_list|)
block|{
name|setWsdlURL
argument_list|(
name|wsdlURL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getWsdlURL
parameter_list|()
block|{
return|return
name|clientFactoryBean
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|getWsdlURL
argument_list|()
return|;
block|}
specifier|public
name|void
name|setWsdlURL
parameter_list|(
name|String
name|wsdlURL
parameter_list|)
block|{
name|clientFactoryBean
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setWsdlURL
argument_list|(
name|wsdlURL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|QName
name|getEndpointName
parameter_list|()
block|{
return|return
name|clientFactoryBean
operator|.
name|getEndpointName
argument_list|()
return|;
block|}
specifier|public
name|void
name|setEndpointName
parameter_list|(
name|QName
name|endpointName
parameter_list|)
block|{
name|clientFactoryBean
operator|.
name|setEndpointName
argument_list|(
name|endpointName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|QName
name|getServiceName
parameter_list|()
block|{
return|return
name|getServiceFactory
argument_list|()
operator|.
name|getServiceQName
argument_list|()
return|;
block|}
specifier|public
name|void
name|setServiceName
parameter_list|(
name|QName
name|serviceName
parameter_list|)
block|{
name|getServiceFactory
argument_list|()
operator|.
name|setServiceName
argument_list|(
name|serviceName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getAddress
parameter_list|()
block|{
return|return
name|clientFactoryBean
operator|.
name|getAddress
argument_list|()
return|;
block|}
specifier|public
name|void
name|setAddress
parameter_list|(
name|String
name|add
parameter_list|)
block|{
name|clientFactoryBean
operator|.
name|setAddress
argument_list|(
name|add
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ConduitSelector
name|getConduitSelector
parameter_list|()
block|{
return|return
name|clientFactoryBean
operator|.
name|getConduitSelector
argument_list|()
return|;
block|}
specifier|public
name|void
name|setConduitSelector
parameter_list|(
name|ConduitSelector
name|selector
parameter_list|)
block|{
name|clientFactoryBean
operator|.
name|setConduitSelector
argument_list|(
name|selector
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setBindingId
parameter_list|(
name|String
name|bind
parameter_list|)
block|{
name|clientFactoryBean
operator|.
name|setBindingId
argument_list|(
name|bind
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getBindingId
parameter_list|()
block|{
return|return
name|clientFactoryBean
operator|.
name|getBindingId
argument_list|()
return|;
block|}
specifier|public
name|ReflectionServiceFactoryBean
name|getServiceFactory
parameter_list|()
block|{
return|return
name|clientFactoryBean
operator|.
name|getServiceFactory
argument_list|()
return|;
block|}
specifier|public
name|void
name|setServiceFactory
parameter_list|(
name|ReflectionServiceFactoryBean
name|sf
parameter_list|)
block|{
name|clientFactoryBean
operator|.
name|setServiceFactory
argument_list|(
name|sf
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Bus
name|getBus
parameter_list|()
block|{
return|return
name|bus
return|;
block|}
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
name|clientFactoryBean
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
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
name|properties
return|;
block|}
specifier|public
name|void
name|setProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|AbstractFeature
argument_list|>
name|getFeatures
parameter_list|()
block|{
return|return
name|features
return|;
block|}
specifier|public
name|void
name|setFeatures
parameter_list|(
name|List
argument_list|<
name|AbstractFeature
argument_list|>
name|f
parameter_list|)
block|{
name|this
operator|.
name|features
operator|=
name|f
expr_stmt|;
block|}
specifier|public
name|DataBinding
name|getDataBinding
parameter_list|()
block|{
return|return
name|dataBinding
return|;
block|}
specifier|public
name|void
name|setDataBinding
parameter_list|(
name|DataBinding
name|dataBinding
parameter_list|)
block|{
name|this
operator|.
name|dataBinding
operator|=
name|dataBinding
expr_stmt|;
block|}
specifier|public
name|void
name|setBindingConfig
parameter_list|(
name|BindingConfiguration
name|config
parameter_list|)
block|{
name|getClientFactoryBean
argument_list|()
operator|.
name|setBindingConfig
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BindingConfiguration
name|getBindingConfig
parameter_list|()
block|{
return|return
name|getClientFactoryBean
argument_list|()
operator|.
name|getBindingConfig
argument_list|()
return|;
block|}
block|}
end_class

end_unit

