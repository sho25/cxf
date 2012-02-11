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
name|xmlbeans
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
name|test
operator|.
name|AbstractCXFTest
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
name|cxf
operator|.
name|xmlbeans
operator|.
name|wsdltest
operator|.
name|GreeterMine
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
name|xmlbeans
operator|.
name|wsdltest
operator|.
name|SOAPMineService
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
name|xmlbeans
operator|.
name|wsdltest
operator|.
name|SayHi2MessageDocument
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
name|xmlbeans
operator|.
name|wsdltest
operator|.
name|StringListType
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
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|XmlBeansTest
extends|extends
name|AbstractCXFTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|XmlBeansTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CONFIG1
init|=
literal|"org/apache/cxf/xmlbeans/cxf.xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CONFIG2
init|=
literal|"org/apache/cxf/xmlbeans/cxf2.xml"
decl_stmt|;
specifier|private
name|SpringBusFactory
name|bf
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|bf
operator|=
operator|new
name|SpringBusFactory
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|false
argument_list|)
expr_stmt|;
name|bus
operator|=
literal|null
expr_stmt|;
block|}
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBusCreationFails
parameter_list|()
throws|throws
name|Exception
block|{
name|bf
operator|=
operator|new
name|SpringBusFactory
argument_list|()
expr_stmt|;
name|bus
operator|=
name|bf
operator|.
name|createBus
argument_list|(
name|CONFIG1
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBasicFails
parameter_list|()
throws|throws
name|Exception
block|{
name|bf
operator|=
operator|new
name|SpringBusFactory
argument_list|()
expr_stmt|;
name|bus
operator|=
name|bf
operator|.
name|createBus
argument_list|(
name|CONFIG2
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|URL
name|wsdlURL
init|=
name|XmlBeansTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/wsdl/xmlbeanstest.wsdl"
argument_list|)
decl_stmt|;
name|SOAPMineService
name|ss
init|=
operator|new
name|SOAPMineService
argument_list|(
name|wsdlURL
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/xmlbeans/wsdltest"
argument_list|,
literal|"SOAPMineService"
argument_list|)
argument_list|)
decl_stmt|;
name|GreeterMine
name|port
init|=
name|ss
operator|.
name|getSoapPort
argument_list|()
decl_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|port
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
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort"
argument_list|)
expr_stmt|;
name|SayHi2MessageDocument
name|document
init|=
name|SayHi2MessageDocument
operator|.
name|Factory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|StringListType
name|stringListType
init|=
name|document
operator|.
name|addNewSayHi2Message
argument_list|()
decl_stmt|;
name|stringListType
operator|.
name|setMyname
argument_list|(
literal|"sean"
argument_list|)
expr_stmt|;
name|stringListType
operator|.
name|setMyaddress
argument_list|(
literal|"home"
argument_list|)
expr_stmt|;
name|port
operator|.
name|sayHi2
argument_list|(
name|document
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

