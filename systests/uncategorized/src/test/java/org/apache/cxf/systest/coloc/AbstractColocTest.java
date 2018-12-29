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
name|systest
operator|.
name|coloc
package|;
end_package

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
name|ws
operator|.
name|BindingProvider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Endpoint
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
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
name|commons
operator|.
name|logging
operator|.
name|Log
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
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
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
name|testutil
operator|.
name|common
operator|.
name|TestUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractColocTest
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|AbstractColocTest
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Cxf Bus      */
specifier|protected
name|Bus
name|bus
decl_stmt|;
comment|/**      * WS endpoint object      */
specifier|protected
name|Endpoint
name|endpoint
decl_stmt|;
comment|/**      * Setup this test case      */
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
comment|// initialize Mule Manager
name|URL
name|cxfConfig
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|getCxfConfig
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|cxfConfig
operator|=
name|ClassLoaderUtils
operator|.
name|getResource
argument_list|(
name|getCxfConfig
argument_list|()
argument_list|,
name|AbstractColocTest
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|cxfConfig
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Make sure "
operator|+
name|getCxfConfig
argument_list|()
operator|+
literal|" is in the CLASSPATH"
argument_list|)
throw|;
block|}
name|assertNotNull
argument_list|(
name|cxfConfig
operator|.
name|toExternalForm
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//Bus is shared by client, router and server.
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|bus
operator|=
name|bf
operator|.
name|createBus
argument_list|(
name|cxfConfig
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
comment|//Start the Remote Server
comment|// the endpoint of the "real" cxf server
name|endpoint
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|getTransportURI
argument_list|()
argument_list|,
name|getServiceImpl
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tear down this test case      */
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|getLogger
argument_list|()
operator|.
name|debug
argument_list|(
literal|"tearDown ..."
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @return cxf configuration file name      */
specifier|protected
name|String
name|getCxfConfig
parameter_list|()
block|{
return|return
literal|"org/apache/cxf/systest/coloc/cxf.xml"
return|;
block|}
comment|/**      * Create client and return the port      * @return port for a interface represented by cls.      */
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getPort
parameter_list|(
name|QName
name|serviceName
parameter_list|,
name|QName
name|portName
parameter_list|,
name|String
name|wsdlLocation
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
name|Service
name|srv
init|=
name|Service
operator|.
name|create
argument_list|(
name|AbstractColocTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
name|wsdlLocation
argument_list|)
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|T
name|t
init|=
name|srv
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|cls
argument_list|)
decl_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|t
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
name|getTransportURI
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|t
return|;
block|}
comment|/**      * @return the greeter impl object      */
specifier|protected
specifier|abstract
name|Object
name|getServiceImpl
parameter_list|()
function_decl|;
comment|/**      * @return logger object      */
specifier|protected
specifier|abstract
name|Log
name|getLogger
parameter_list|()
function_decl|;
comment|/**      * @return transport URI for the WS Endpoint      */
specifier|protected
specifier|abstract
name|String
name|getTransportURI
parameter_list|()
function_decl|;
block|}
end_class

end_unit

