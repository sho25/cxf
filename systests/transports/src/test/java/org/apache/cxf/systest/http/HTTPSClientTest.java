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
name|http
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
name|ws
operator|.
name|BindingProvider
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
name|configuration
operator|.
name|Configurer
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
name|AbstractBusClientServerTestBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world
operator|.
name|Greeter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world
operator|.
name|services
operator|.
name|SOAPService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * This test is meant to run against a spring-loaded  * HTTP/S service.  */
end_comment

begin_class
specifier|public
class|class
name|HTTPSClientTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
comment|//
comment|// data
comment|//
comment|/**      * the package path used to locate resources specific to this test      */
specifier|private
name|void
name|setTheConfiguration
parameter_list|(
name|String
name|config
parameter_list|)
block|{
comment|//System.setProperty("javax.net.debug", "all");
try|try
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|Configurer
operator|.
name|USER_CFG_FILE_PROPERTY_URL
argument_list|,
name|HTTPSClientTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
name|config
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|startServers
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
literal|"Server failed to launch"
argument_list|,
comment|// run the server in the same process
comment|// set this to false to fork a new process
name|launchServer
argument_list|(
name|BusServer
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|stopServers
parameter_list|()
throws|throws
name|Exception
block|{
name|stopAllServers
argument_list|()
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
name|Configurer
operator|.
name|USER_CFG_FILE_PROPERTY_URL
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
comment|//
comment|// tests
comment|//
specifier|public
specifier|final
name|void
name|testSuccessfulCall
parameter_list|(
name|String
name|configuration
parameter_list|,
name|String
name|address
parameter_list|)
throws|throws
name|Exception
block|{
name|testSuccessfulCall
argument_list|(
name|configuration
argument_list|,
name|address
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|final
name|void
name|testSuccessfulCall
parameter_list|(
name|String
name|configuration
parameter_list|,
name|String
name|address
parameter_list|,
name|URL
name|url
parameter_list|)
throws|throws
name|Exception
block|{
name|setTheConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|startServers
argument_list|()
expr_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|url
operator|=
name|SOAPService
operator|.
name|WSDL_LOCATION
expr_stmt|;
block|}
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|(
name|url
argument_list|,
name|SOAPService
operator|.
name|SERVICE
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Service is null"
argument_list|,
name|service
argument_list|)
expr_stmt|;
specifier|final
name|Greeter
name|port
init|=
name|service
operator|.
name|getHttpsPort
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Port is null"
argument_list|,
name|port
argument_list|)
expr_stmt|;
name|BindingProvider
name|provider
init|=
operator|(
name|BindingProvider
operator|)
name|port
decl_stmt|;
name|provider
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
name|address
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|port
operator|.
name|greetMe
argument_list|(
literal|"Kitty"
argument_list|)
argument_list|,
literal|"Hello Kitty"
argument_list|)
expr_stmt|;
name|stopServers
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
specifier|final
name|void
name|testJaxwsServer
parameter_list|()
throws|throws
name|Exception
block|{
name|testSuccessfulCall
argument_list|(
literal|"resources/jaxws-server.xml"
argument_list|,
literal|"https://localhost:9002/SoapContext/HttpsPort"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
specifier|final
name|void
name|testJaxwsServerChangeHttpsToHttp
parameter_list|()
throws|throws
name|Exception
block|{
name|testSuccessfulCall
argument_list|(
literal|"resources/jaxws-server.xml"
argument_list|,
literal|"http://localhost:9003/SoapContext/HttpPort"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
specifier|final
name|void
name|testJaxwsEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|testSuccessfulCall
argument_list|(
literal|"resources/jaxws-publish.xml"
argument_list|,
literal|"https://localhost:9001/SoapContext/HttpsPort"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
specifier|final
name|void
name|testPKCS12Endpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|testSuccessfulCall
argument_list|(
literal|"resources/pkcs12.xml"
argument_list|,
literal|"https://localhost:9006/SoapContext/HttpsPort"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
specifier|final
name|void
name|testResourceKeySpecEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|testSuccessfulCall
argument_list|(
literal|"resources/resource-key-spec.xml"
argument_list|,
literal|"https://localhost:9004/SoapContext/HttpsPort"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
specifier|final
name|void
name|testResourceKeySpecEndpointURL
parameter_list|()
throws|throws
name|Exception
block|{
name|testSuccessfulCall
argument_list|(
literal|"resources/resource-key-spec-url.xml"
argument_list|,
literal|"https://localhost:9005/SoapContext/HttpsPort"
argument_list|,
operator|new
name|URL
argument_list|(
literal|"https://localhost:9005/SoapContext/HttpsPort?wsdl"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

