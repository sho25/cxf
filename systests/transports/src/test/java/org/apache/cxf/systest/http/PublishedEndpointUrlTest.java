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
name|HttpURLConnection
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
name|javax
operator|.
name|wsdl
operator|.
name|Definition
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Port
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|soap
operator|.
name|SOAPAddress
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|soap12
operator|.
name|SOAP12Address
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|factory
operator|.
name|WSDLFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|xml
operator|.
name|WSDLReader
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
name|helpers
operator|.
name|CastUtils
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
name|jaxws
operator|.
name|JaxWsServerFactoryBean
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
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|Greeter
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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
class|class
name|PublishedEndpointUrlTest
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
name|PublishedEndpointUrlTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testPublishedEndpointUrl
parameter_list|()
throws|throws
name|Exception
block|{
name|Greeter
name|implementor
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|GreeterImpl
argument_list|()
decl_stmt|;
name|String
name|publishedEndpointUrl
init|=
literal|"http://cxf.apache.org/publishedEndpointUrl"
decl_stmt|;
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
decl_stmt|;
name|JaxWsServerFactoryBean
name|svrFactory
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|svrFactory
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setServiceClass
argument_list|(
name|Greeter
operator|.
name|class
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/publishedEndpointUrl"
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setPublishedEndpointUrl
argument_list|(
name|publishedEndpointUrl
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setServiceBean
argument_list|(
name|implementor
argument_list|)
expr_stmt|;
name|Server
name|server
init|=
name|svrFactory
operator|.
name|create
argument_list|()
decl_stmt|;
name|WSDLReader
name|wsdlReader
init|=
name|WSDLFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newWSDLReader
argument_list|()
decl_stmt|;
name|wsdlReader
operator|.
name|setFeature
argument_list|(
literal|"javax.wsdl.verbose"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|svrFactory
operator|.
name|getAddress
argument_list|()
operator|+
literal|"?wsdl=1"
argument_list|)
decl_stmt|;
name|HttpURLConnection
name|connect
init|=
operator|(
name|HttpURLConnection
operator|)
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|connect
operator|.
name|getResponseCode
argument_list|()
argument_list|)
expr_stmt|;
name|Definition
name|wsdl
init|=
name|wsdlReader
operator|.
name|readWSDL
argument_list|(
name|svrFactory
operator|.
name|getAddress
argument_list|()
operator|+
literal|"?wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Service
argument_list|>
name|services
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|wsdl
operator|.
name|getAllServices
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|String
name|failMesg
init|=
literal|"WSDL provided incorrect soap:address location"
decl_stmt|;
for|for
control|(
name|Service
name|service
range|:
name|services
control|)
block|{
name|Collection
argument_list|<
name|Port
argument_list|>
name|ports
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|service
operator|.
name|getPorts
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Port
name|port
range|:
name|ports
control|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|extensions
init|=
name|port
operator|.
name|getExtensibilityElements
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|extension
range|:
name|extensions
control|)
block|{
name|String
name|actualUrl
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|extension
operator|instanceof
name|SOAP12Address
condition|)
block|{
name|actualUrl
operator|=
operator|(
operator|(
name|SOAP12Address
operator|)
name|extension
operator|)
operator|.
name|getLocationURI
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|extension
operator|instanceof
name|SOAPAddress
condition|)
block|{
name|actualUrl
operator|=
operator|(
operator|(
name|SOAPAddress
operator|)
name|extension
operator|)
operator|.
name|getLocationURI
argument_list|()
expr_stmt|;
block|}
comment|//System.out.println("Checking url: " + actualUrl + " against " + publishedEndpointUrl);
name|assertEquals
argument_list|(
name|failMesg
argument_list|,
name|publishedEndpointUrl
argument_list|,
name|actualUrl
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
name|server
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

