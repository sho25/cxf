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
name|transport
operator|.
name|jms
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
name|HashSet
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
name|Resource
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
name|injection
operator|.
name|NoJSR250Annotations
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
name|AbstractTransportFactory
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

begin_class
annotation|@
name|NoJSR250Annotations
argument_list|(
name|unlessNull
operator|=
block|{
literal|"bus"
block|}
argument_list|)
specifier|public
class|class
name|JMSTransportFactory
extends|extends
name|AbstractTransportFactory
implements|implements
name|ConduitInitiator
implements|,
name|DestinationFactory
block|{
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|URI_PREFIXES
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
name|URI_PREFIXES
operator|.
name|add
argument_list|(
literal|"jms://"
argument_list|)
expr_stmt|;
name|URI_PREFIXES
operator|.
name|add
argument_list|(
literal|"jms:"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|public
name|JMSTransportFactory
parameter_list|()
block|{              }
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"cxf"
argument_list|)
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|bus
operator|=
name|b
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
name|endpointInfo
operator|.
name|getTarget
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
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
name|JMSOldConfigHolder
name|old
init|=
operator|new
name|JMSOldConfigHolder
argument_list|()
decl_stmt|;
name|JMSConfiguration
name|jmsConf
init|=
name|old
operator|.
name|createJMSConfigurationFromEndpointInfo
argument_list|(
name|bus
argument_list|,
name|endpointInfo
argument_list|,
literal|true
argument_list|)
decl_stmt|;
return|return
operator|new
name|JMSConduit
argument_list|(
name|endpointInfo
argument_list|,
name|target
argument_list|,
name|jmsConf
argument_list|,
name|bus
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
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
name|JMSOldConfigHolder
name|old
init|=
operator|new
name|JMSOldConfigHolder
argument_list|()
decl_stmt|;
name|JMSConfiguration
name|jmsConf
init|=
name|old
operator|.
name|createJMSConfigurationFromEndpointInfo
argument_list|(
name|bus
argument_list|,
name|endpointInfo
argument_list|,
literal|false
argument_list|)
decl_stmt|;
return|return
operator|new
name|JMSDestination
argument_list|(
name|bus
argument_list|,
name|endpointInfo
argument_list|,
name|jmsConf
argument_list|)
return|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getUriPrefixes
parameter_list|()
block|{
return|return
name|URI_PREFIXES
return|;
block|}
block|}
end_class

end_unit

