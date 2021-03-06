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
name|jms
operator|.
name|security
package|;
end_package

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
name|Map
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusTestServerBase
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
name|EmbeddedJMSBrokerLauncher
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
name|security
operator|.
name|wss4j
operator|.
name|WSS4JInInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|ConfigurationConstants
import|;
end_import

begin_class
specifier|public
class|class
name|Server
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|Server
operator|.
name|class
argument_list|)
decl_stmt|;
name|EmbeddedJMSBrokerLauncher
name|broker
decl_stmt|;
specifier|public
name|Server
parameter_list|(
name|EmbeddedJMSBrokerLauncher
name|b
parameter_list|)
block|{
name|broker
operator|=
name|b
expr_stmt|;
block|}
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
decl_stmt|;
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|inProperties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|inProperties
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|ACTION
argument_list|,
name|ConfigurationConstants
operator|.
name|SAML_TOKEN_SIGNED
argument_list|)
expr_stmt|;
name|inProperties
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|PW_CALLBACK_REF
argument_list|,
operator|new
name|KeystorePasswordCallback
argument_list|()
argument_list|)
expr_stmt|;
name|inProperties
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|SIG_VER_PROP_FILE
argument_list|,
literal|"bob.properties"
argument_list|)
expr_stmt|;
name|WSS4JInInterceptor
name|inInterceptor
init|=
operator|new
name|WSS4JInInterceptor
argument_list|(
name|inProperties
argument_list|)
decl_stmt|;
name|bus
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|inInterceptor
argument_list|)
expr_stmt|;
name|broker
operator|.
name|updateWsdl
argument_list|(
name|bus
argument_list|,
literal|"testutils/jms_test.wsdl"
argument_list|)
expr_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
literal|null
argument_list|,
operator|new
name|SecurityGreeterImplTwoWayJMS
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

