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
name|jca
operator|.
name|cxf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
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
name|javax
operator|.
name|resource
operator|.
name|ResourceException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|ConnectionEventListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|Subject
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
name|BusException
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
name|hello_world_soap_http
operator|.
name|Greeter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_class
specifier|public
specifier|abstract
class|class
name|ManagedConnectionTestBase
extends|extends
name|Assert
block|{
specifier|protected
name|Subject
name|subj
decl_stmt|;
specifier|protected
name|CXFConnectionRequestInfo
name|cri
decl_stmt|;
specifier|protected
name|CXFConnectionRequestInfo
name|cri2
decl_stmt|;
specifier|protected
name|ManagedConnectionImpl
name|mci
decl_stmt|;
specifier|protected
name|ManagedConnectionFactoryImpl
name|factory
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ManagedConnectionFactoryImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|Bus
name|bus
decl_stmt|;
specifier|protected
name|ConnectionEventListener
name|mockListener
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ConnectionEventListener
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|ManagedConnectionTestBase
parameter_list|()
block|{              }
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|ResourceException
throws|,
name|MalformedURLException
throws|,
name|BusException
block|{
name|subj
operator|=
operator|new
name|Subject
argument_list|()
expr_stmt|;
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/hello_world.wsdl"
argument_list|)
decl_stmt|;
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SOAPService"
argument_list|)
decl_stmt|;
name|QName
name|serviceName2
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SOAPService2"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SoapPort"
argument_list|)
decl_stmt|;
name|QName
name|portName2
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SoapPort2"
argument_list|)
decl_stmt|;
name|cri
operator|=
operator|new
name|CXFConnectionRequestInfo
argument_list|(
name|Greeter
operator|.
name|class
argument_list|,
name|wsdl
argument_list|,
name|serviceName
argument_list|,
name|portName
argument_list|)
expr_stmt|;
name|cri2
operator|=
operator|new
name|CXFConnectionRequestInfo
argument_list|(
name|Greeter
operator|.
name|class
argument_list|,
name|wsdl
argument_list|,
name|serviceName2
argument_list|,
name|portName2
argument_list|)
expr_stmt|;
name|BusFactory
name|bf
init|=
name|BusFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|bus
operator|=
name|bf
operator|.
name|createBus
argument_list|()
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|reset
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|factory
operator|.
name|getBus
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|bus
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|mci
operator|=
operator|new
name|ManagedConnectionImpl
argument_list|(
name|factory
argument_list|,
name|cri
argument_list|,
name|subj
argument_list|)
expr_stmt|;
name|mci
operator|.
name|addConnectionEventListener
argument_list|(
name|mockListener
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

