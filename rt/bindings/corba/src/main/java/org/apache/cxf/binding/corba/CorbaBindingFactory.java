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
name|binding
operator|.
name|corba
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
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
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
name|AbstractBindingFactory
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
name|Binding
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
name|corba
operator|.
name|interceptors
operator|.
name|CorbaStreamFaultInInterceptor
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
name|corba
operator|.
name|interceptors
operator|.
name|CorbaStreamFaultOutInterceptor
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
name|corba
operator|.
name|interceptors
operator|.
name|CorbaStreamInInterceptor
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
name|corba
operator|.
name|interceptors
operator|.
name|CorbaStreamOutInterceptor
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
name|corba
operator|.
name|utils
operator|.
name|OrbConfig
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
name|BareInInterceptor
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
name|BareOutInterceptor
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
name|BindingInfo
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
name|transport
operator|.
name|Conduit
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
name|transport
operator|.
name|ConduitInitiator
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
name|transport
operator|.
name|Destination
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
name|transport
operator|.
name|DestinationFactory
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
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
name|wsdl
operator|.
name|JAXBExtensionHelper
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
name|wsdl
operator|.
name|TExtensibilityElementImpl
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
name|wsdl
operator|.
name|WSDLManager
import|;
end_import

begin_class
specifier|public
class|class
name|CorbaBindingFactory
extends|extends
name|AbstractBindingFactory
implements|implements
name|ConduitInitiator
implements|,
name|DestinationFactory
block|{
specifier|private
specifier|static
specifier|final
name|String
name|YOKO_NAMESPACE
init|=
literal|"http://schemas.apache.org/yoko/bindings/corba"
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|transportIds
decl_stmt|;
specifier|private
name|OrbConfig
name|orbConfig
init|=
operator|new
name|OrbConfig
argument_list|()
decl_stmt|;
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"orbClass"
argument_list|)
specifier|public
name|void
name|setOrbClass
parameter_list|(
name|String
name|cls
parameter_list|)
block|{
name|orbConfig
operator|.
name|setOrbClass
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"orbSingletonClass"
argument_list|)
specifier|public
name|void
name|setOrbSingletonClass
parameter_list|(
name|String
name|cls
parameter_list|)
block|{
name|orbConfig
operator|.
name|setOrbSingletonClass
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
annotation|@
name|PostConstruct
name|void
name|registerYokoCompatibleExtensors
parameter_list|()
block|{
name|WSDLManager
name|manager
init|=
name|this
operator|.
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|createCompatExtensor
argument_list|(
name|manager
argument_list|,
name|javax
operator|.
name|wsdl
operator|.
name|Binding
operator|.
name|class
argument_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|BindingType
operator|.
name|class
argument_list|)
expr_stmt|;
name|createCompatExtensor
argument_list|(
name|manager
argument_list|,
name|javax
operator|.
name|wsdl
operator|.
name|BindingOperation
operator|.
name|class
argument_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|OperationType
operator|.
name|class
argument_list|)
expr_stmt|;
name|createCompatExtensor
argument_list|(
name|manager
argument_list|,
name|javax
operator|.
name|wsdl
operator|.
name|Definition
operator|.
name|class
argument_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|TypeMappingType
operator|.
name|class
argument_list|)
expr_stmt|;
name|createCompatExtensor
argument_list|(
name|manager
argument_list|,
name|javax
operator|.
name|wsdl
operator|.
name|Port
operator|.
name|class
argument_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|AddressType
operator|.
name|class
argument_list|)
expr_stmt|;
name|createCompatExtensor
argument_list|(
name|manager
argument_list|,
name|javax
operator|.
name|wsdl
operator|.
name|Port
operator|.
name|class
argument_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|PolicyType
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createCompatExtensor
parameter_list|(
name|WSDLManager
name|manager
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|parentType
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|TExtensibilityElementImpl
argument_list|>
name|elementType
parameter_list|)
block|{
try|try
block|{
name|JAXBExtensionHelper
operator|.
name|addExtensions
argument_list|(
name|manager
operator|.
name|getExtensionRegistry
argument_list|()
argument_list|,
name|parentType
argument_list|,
name|elementType
argument_list|,
name|YOKO_NAMESPACE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
comment|//ignore, just won't support the yoko extensors
block|}
block|}
specifier|public
name|Binding
name|createBinding
parameter_list|(
name|BindingInfo
name|bindingInfo
parameter_list|)
block|{
name|CorbaBinding
name|binding
init|=
operator|new
name|CorbaBinding
argument_list|()
decl_stmt|;
name|binding
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|CorbaStreamFaultInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|CorbaStreamFaultOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|BareOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|CorbaStreamOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|BareInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|CorbaStreamInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setBindingInfo
argument_list|(
name|bindingInfo
argument_list|)
expr_stmt|;
return|return
name|binding
return|;
block|}
specifier|public
name|Conduit
name|getConduit
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getConduit
argument_list|(
name|endpointInfo
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|Conduit
name|getConduit
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|,
name|EndpointReferenceType
name|target
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|CorbaConduit
argument_list|(
name|endpointInfo
argument_list|,
name|target
argument_list|,
name|orbConfig
argument_list|)
return|;
block|}
specifier|public
name|Destination
name|getDestination
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|CorbaDestination
argument_list|(
name|endpointInfo
argument_list|,
name|orbConfig
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getTransportIds
parameter_list|()
block|{
return|return
name|transportIds
return|;
block|}
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"transportIds"
argument_list|)
specifier|public
name|void
name|setTransportIds
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|ids
parameter_list|)
block|{
name|transportIds
operator|=
name|ids
expr_stmt|;
block|}
annotation|@
name|Resource
specifier|public
name|void
name|setOrbArgs
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|args
parameter_list|)
block|{
name|orbConfig
operator|.
name|setOrbArgs
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getUriPrefixes
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|uriPrefixes
init|=
operator|new
name|java
operator|.
name|util
operator|.
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|uriPrefixes
operator|.
name|add
argument_list|(
literal|"IOR"
argument_list|)
expr_stmt|;
name|uriPrefixes
operator|.
name|add
argument_list|(
literal|"ior"
argument_list|)
expr_stmt|;
name|uriPrefixes
operator|.
name|add
argument_list|(
literal|"file"
argument_list|)
expr_stmt|;
name|uriPrefixes
operator|.
name|add
argument_list|(
literal|"relfile"
argument_list|)
expr_stmt|;
name|uriPrefixes
operator|.
name|add
argument_list|(
literal|"corba"
argument_list|)
expr_stmt|;
return|return
name|uriPrefixes
return|;
block|}
specifier|public
name|OrbConfig
name|getOrbConfig
parameter_list|()
block|{
return|return
name|orbConfig
return|;
block|}
specifier|public
name|void
name|setOrbConfig
parameter_list|(
name|OrbConfig
name|config
parameter_list|)
block|{
name|orbConfig
operator|=
name|config
expr_stmt|;
block|}
block|}
end_class

end_unit

