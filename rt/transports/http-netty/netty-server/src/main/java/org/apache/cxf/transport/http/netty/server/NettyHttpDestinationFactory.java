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
name|http
operator|.
name|netty
operator|.
name|server
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
name|http
operator|.
name|AbstractHTTPDestination
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
name|http
operator|.
name|DestinationRegistry
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
name|http
operator|.
name|HttpDestinationFactory
import|;
end_import

begin_class
annotation|@
name|NoJSR250Annotations
argument_list|()
specifier|public
class|class
name|NettyHttpDestinationFactory
implements|implements
name|HttpDestinationFactory
block|{
specifier|public
name|AbstractHTTPDestination
name|createDestination
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|DestinationRegistry
name|registry
parameter_list|)
throws|throws
name|IOException
block|{
name|NettyHttpServerEngineFactory
name|serverEngineFactory
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|NettyHttpServerEngineFactory
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
operator|new
name|NettyHttpDestination
argument_list|(
name|bus
argument_list|,
name|registry
argument_list|,
name|endpointInfo
argument_list|,
name|serverEngineFactory
argument_list|)
return|;
block|}
block|}
end_class

end_unit

