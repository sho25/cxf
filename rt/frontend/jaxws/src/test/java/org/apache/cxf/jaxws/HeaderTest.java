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
name|jaxws
package|;
end_package

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
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|frontend
operator|.
name|ServerFactoryBean
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
name|binding
operator|.
name|soap
operator|.
name|JaxWsSoapBindingConfiguration
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
name|support
operator|.
name|JaxWsServiceFactoryBean
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
name|Service
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
name|MessagePartInfo
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
name|OperationInfo
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
name|local
operator|.
name|LocalTransportFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|header_test
operator|.
name|TestHeaderImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|header_test
operator|.
name|types
operator|.
name|TestHeader5
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|header_test
operator|.
name|types
operator|.
name|TestHeader5ResponseBody
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
name|HeaderTest
extends|extends
name|AbstractJaxWsTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testInvocation
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsServiceFactoryBean
name|bean
init|=
operator|new
name|JaxWsServiceFactoryBean
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|getBus
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setServiceClass
argument_list|(
name|TestHeaderImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|Service
name|service
init|=
name|bean
operator|.
name|create
argument_list|()
decl_stmt|;
name|OperationInfo
name|op
init|=
name|service
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getInterface
argument_list|()
operator|.
name|getOperation
argument_list|(
operator|new
name|QName
argument_list|(
name|service
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|"testHeader5"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|op
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|parts
init|=
name|op
operator|.
name|getInput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|parts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|MessagePartInfo
name|part
init|=
name|parts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|part
operator|.
name|getTypeClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TestHeader5
operator|.
name|class
argument_list|,
name|part
operator|.
name|getTypeClass
argument_list|()
argument_list|)
expr_stmt|;
name|parts
operator|=
name|op
operator|.
name|getOutput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|parts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|part
operator|=
name|parts
operator|.
name|get
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|part
operator|.
name|getTypeClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TestHeader5ResponseBody
operator|.
name|class
argument_list|,
name|part
operator|.
name|getTypeClass
argument_list|()
argument_list|)
expr_stmt|;
name|part
operator|=
name|parts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|part
operator|.
name|getTypeClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TestHeader5
operator|.
name|class
argument_list|,
name|part
operator|.
name|getTypeClass
argument_list|()
argument_list|)
expr_stmt|;
comment|//        part = parts.get(1);
comment|//        assertNotNull(part.getTypeClass());
name|ServerFactoryBean
name|svr
init|=
operator|new
name|ServerFactoryBean
argument_list|()
decl_stmt|;
name|svr
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|svr
operator|.
name|setServiceFactory
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|svr
operator|.
name|setServiceBean
argument_list|(
operator|new
name|TestHeaderImpl
argument_list|()
argument_list|)
expr_stmt|;
name|svr
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:9104/SoapHeaderContext/SoapHeaderPort"
argument_list|)
expr_stmt|;
name|svr
operator|.
name|setBindingConfig
argument_list|(
operator|new
name|JaxWsSoapBindingConfiguration
argument_list|(
name|bean
argument_list|)
argument_list|)
expr_stmt|;
name|svr
operator|.
name|create
argument_list|()
expr_stmt|;
name|Node
name|response
init|=
name|invoke
argument_list|(
literal|"http://localhost:9104/SoapHeaderContext/SoapHeaderPort"
argument_list|,
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|,
literal|"testHeader5.xml"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertNoFault
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"t"
argument_list|,
literal|"http://apache.org/header_test/types"
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//s:Header/t:testHeader5"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

