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
name|Collection
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
name|binding
operator|.
name|AbstractWSDLBindingFactory
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
name|interceptors
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
name|wsdl
operator|.
name|interceptors
operator|.
name|BareOutInterceptor
import|;
end_import

begin_class
specifier|public
class|class
name|CorbaBindingFactory
extends|extends
name|AbstractWSDLBindingFactory
implements|implements
name|ConduitInitiator
implements|,
name|DestinationFactory
block|{
specifier|public
specifier|static
specifier|final
name|Collection
argument_list|<
name|String
argument_list|>
name|DEFAULT_NAMESPACES
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"http://cxf.apache.org/bindings/corba"
argument_list|,
literal|"http://schemas.apache.org/yoko/bindings/corba"
argument_list|)
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|transportIds
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|DEFAULT_NAMESPACES
argument_list|)
decl_stmt|;
specifier|protected
name|OrbConfig
name|orbConfig
init|=
operator|new
name|OrbConfig
argument_list|()
decl_stmt|;
specifier|public
name|CorbaBindingFactory
parameter_list|()
block|{
name|super
argument_list|(
name|DEFAULT_NAMESPACES
argument_list|)
expr_stmt|;
block|}
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
parameter_list|,
name|Bus
name|bus
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
argument_list|,
name|bus
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
parameter_list|,
name|Bus
name|bus
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
parameter_list|,
name|Bus
name|bus
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
argument_list|<>
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

