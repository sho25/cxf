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
name|ArrayList
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
name|concurrent
operator|.
name|Executor
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
name|Response
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
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
name|PackageUtils
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
name|Configurable
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
name|interceptor
operator|.
name|AbstractAttributedInterceptorProvider
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
name|model
operator|.
name|OperationResourceInfo
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
name|Parameter
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
name|ParameterType
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
name|Service
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
name|model
operator|.
name|EndpointInfo
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
name|model
operator|.
name|InterfaceInfo
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
name|model
operator|.
name|MessageInfo
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
name|model
operator|.
name|MessagePartInfo
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
name|model
operator|.
name|OperationInfo
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
name|model
operator|.
name|ServiceInfo
import|;
end_import

begin_comment
comment|/**  * The CXF Service implementation which is used  * to register the JAX-RS endpoint with the runtime.  */
end_comment

begin_class
specifier|public
class|class
name|JAXRSServiceImpl
extends|extends
name|AbstractAttributedInterceptorProvider
implements|implements
name|Service
implements|,
name|Configurable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|6765400202555126993L
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|classResourceInfos
decl_stmt|;
specifier|private
name|DataBinding
name|dataBinding
decl_stmt|;
specifier|private
name|Executor
name|executor
decl_stmt|;
specifier|private
name|Invoker
name|invoker
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|QName
argument_list|,
name|Endpoint
argument_list|>
name|endpoints
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|address
decl_stmt|;
specifier|private
name|boolean
name|createServiceModel
decl_stmt|;
specifier|private
name|QName
name|serviceName
decl_stmt|;
specifier|public
name|JAXRSServiceImpl
parameter_list|(
name|String
name|address
parameter_list|,
name|QName
name|qname
parameter_list|)
block|{
name|this
operator|.
name|address
operator|=
name|address
expr_stmt|;
name|this
operator|.
name|serviceName
operator|=
name|qname
expr_stmt|;
block|}
specifier|public
name|JAXRSServiceImpl
parameter_list|(
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|cri
parameter_list|,
name|QName
name|qname
parameter_list|)
block|{
name|this
operator|.
name|classResourceInfos
operator|=
name|cri
expr_stmt|;
name|this
operator|.
name|serviceName
operator|=
name|qname
expr_stmt|;
block|}
specifier|public
name|JAXRSServiceImpl
parameter_list|(
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|cri
parameter_list|)
block|{
name|this
argument_list|(
name|cri
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JAXRSServiceImpl
parameter_list|(
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|cri
parameter_list|,
name|boolean
name|create
parameter_list|)
block|{
name|this
argument_list|(
name|cri
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|createServiceModel
operator|=
name|create
expr_stmt|;
block|}
specifier|public
name|void
name|setCreateServiceModel
parameter_list|(
name|boolean
name|create
parameter_list|)
block|{
name|createServiceModel
operator|=
name|create
expr_stmt|;
block|}
specifier|public
name|String
name|getBeanName
parameter_list|()
block|{
return|return
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|QName
name|getName
parameter_list|()
block|{
if|if
condition|(
name|serviceName
operator|!=
literal|null
condition|)
block|{
return|return
name|serviceName
return|;
block|}
if|if
condition|(
name|address
operator|==
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|primaryClass
init|=
name|classResourceInfos
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getServiceClass
argument_list|()
decl_stmt|;
name|String
name|ns
init|=
name|PackageUtils
operator|.
name|getNamespace
argument_list|(
name|PackageUtils
operator|.
name|getPackageName
argument_list|(
name|primaryClass
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|primaryClass
operator|.
name|getSimpleName
argument_list|()
argument_list|)
return|;
block|}
return|return
operator|new
name|QName
argument_list|(
name|address
argument_list|,
literal|"WebClient"
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|getClassResourceInfos
parameter_list|()
block|{
return|return
name|classResourceInfos
return|;
block|}
specifier|public
name|List
argument_list|<
name|ServiceInfo
argument_list|>
name|getServiceInfos
parameter_list|()
block|{
if|if
condition|(
operator|!
name|createServiceModel
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
comment|// try to convert to WSDL-centric model so that CXF DataBindings can get initialized
comment|// might become useful too if we support wsdl2
comment|// make databindings to use databinding-specific information
comment|// like @XmlRootElement for ex to select a namespace
name|this
operator|.
name|put
argument_list|(
literal|"org.apache.cxf.databinding.namespace"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ServiceInfo
argument_list|>
name|infos
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ClassResourceInfo
name|cri
range|:
name|classResourceInfos
control|)
block|{
name|ServiceInfo
name|si
init|=
operator|new
name|ServiceInfo
argument_list|()
decl_stmt|;
name|infos
operator|.
name|add
argument_list|(
name|si
argument_list|)
expr_stmt|;
name|QName
name|qname
init|=
name|JAXRSUtils
operator|.
name|getClassQName
argument_list|(
name|cri
operator|.
name|getServiceClass
argument_list|()
argument_list|)
decl_stmt|;
name|si
operator|.
name|setName
argument_list|(
name|qname
argument_list|)
expr_stmt|;
name|InterfaceInfo
name|inf
init|=
operator|new
name|InterfaceInfo
argument_list|(
name|si
argument_list|,
name|qname
argument_list|)
decl_stmt|;
name|si
operator|.
name|setInterface
argument_list|(
name|inf
argument_list|)
expr_stmt|;
for|for
control|(
name|OperationResourceInfo
name|ori
range|:
name|cri
operator|.
name|getMethodDispatcher
argument_list|()
operator|.
name|getOperationResourceInfos
argument_list|()
control|)
block|{
name|Method
name|m
init|=
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
decl_stmt|;
name|QName
name|oname
init|=
operator|new
name|QName
argument_list|(
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|m
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|OperationInfo
name|oi
init|=
name|inf
operator|.
name|addOperation
argument_list|(
name|oname
argument_list|)
decl_stmt|;
name|createMessagePartInfo
argument_list|(
name|oi
argument_list|,
name|m
operator|.
name|getReturnType
argument_list|()
argument_list|,
name|qname
argument_list|,
name|m
argument_list|,
literal|false
argument_list|)
expr_stmt|;
for|for
control|(
name|Parameter
name|pm
range|:
name|ori
operator|.
name|getParameters
argument_list|()
control|)
block|{
if|if
condition|(
name|pm
operator|.
name|getType
argument_list|()
operator|==
name|ParameterType
operator|.
name|REQUEST_BODY
condition|)
block|{
name|createMessagePartInfo
argument_list|(
name|oi
argument_list|,
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
operator|.
name|getParameterTypes
argument_list|()
index|[
name|pm
operator|.
name|getIndex
argument_list|()
index|]
argument_list|,
name|qname
argument_list|,
name|m
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|infos
return|;
block|}
specifier|private
name|void
name|createMessagePartInfo
parameter_list|(
name|OperationInfo
name|oi
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|QName
name|qname
parameter_list|,
name|Method
name|m
parameter_list|,
name|boolean
name|input
parameter_list|)
block|{
if|if
condition|(
name|type
operator|==
name|void
operator|.
name|class
operator|||
name|Source
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|InjectionUtils
operator|.
name|isPrimitive
argument_list|(
name|type
argument_list|)
operator|||
name|Response
operator|.
name|class
operator|==
name|type
condition|)
block|{
return|return;
block|}
name|QName
name|mName
init|=
operator|new
name|QName
argument_list|(
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
operator|(
name|input
condition|?
literal|"in"
else|:
literal|"out"
operator|)
operator|+
name|m
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|MessageInfo
name|ms
init|=
name|oi
operator|.
name|createMessage
argument_list|(
name|mName
argument_list|,
name|input
condition|?
name|MessageInfo
operator|.
name|Type
operator|.
name|INPUT
else|:
name|MessageInfo
operator|.
name|Type
operator|.
name|OUTPUT
argument_list|)
decl_stmt|;
if|if
condition|(
name|input
condition|)
block|{
name|oi
operator|.
name|setInput
argument_list|(
literal|"in"
argument_list|,
name|ms
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|oi
operator|.
name|setOutput
argument_list|(
literal|"out"
argument_list|,
name|ms
argument_list|)
expr_stmt|;
block|}
name|QName
name|mpQName
init|=
name|JAXRSUtils
operator|.
name|getClassQName
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|MessagePartInfo
name|mpi
init|=
name|ms
operator|.
name|addMessagePart
argument_list|(
name|mpQName
argument_list|)
decl_stmt|;
name|mpi
operator|.
name|setConcreteName
argument_list|(
name|mpQName
argument_list|)
expr_stmt|;
name|mpi
operator|.
name|setTypeQName
argument_list|(
name|mpQName
argument_list|)
expr_stmt|;
name|mpi
operator|.
name|setTypeClass
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EndpointInfo
name|getEndpointInfo
parameter_list|(
name|QName
name|endpoint
parameter_list|)
block|{
comment|// For WSDL-based services, this is to construct an EndpointInfo
comment|// (transport, binding, address etc) from WSDL's physical part.
comment|// not applicable to JAX-RS services.
return|return
literal|null
return|;
block|}
specifier|public
name|Executor
name|getExecutor
parameter_list|()
block|{
return|return
name|executor
return|;
block|}
specifier|public
name|void
name|setExecutor
parameter_list|(
name|Executor
name|executor
parameter_list|)
block|{
name|this
operator|.
name|executor
operator|=
name|executor
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
name|Map
argument_list|<
name|QName
argument_list|,
name|Endpoint
argument_list|>
name|getEndpoints
parameter_list|()
block|{
return|return
name|endpoints
return|;
block|}
specifier|public
name|void
name|setEndpoints
parameter_list|(
name|Map
argument_list|<
name|QName
argument_list|,
name|Endpoint
argument_list|>
name|endpoints
parameter_list|)
block|{
name|this
operator|.
name|endpoints
operator|=
name|endpoints
expr_stmt|;
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
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

