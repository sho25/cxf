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
name|aegis
operator|.
name|type
operator|.
name|java5
operator|.
name|map
package|;
end_package

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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|aegis
operator|.
name|AbstractAegisTest
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
name|interceptor
operator|.
name|LoggingInInterceptor
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
name|interceptor
operator|.
name|LoggingOutInterceptor
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
name|JaxWsProxyFactoryBean
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|StudentTest
extends|extends
name|AbstractAegisTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testWSDL
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsServerFactoryBean
name|sf
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setServiceClass
argument_list|(
name|StudentService
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setServiceBean
argument_list|(
operator|new
name|StudentServiceImpl
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"local://StudentService"
argument_list|)
expr_stmt|;
name|setupAegis
argument_list|(
name|sf
argument_list|)
expr_stmt|;
name|Server
name|server
init|=
name|sf
operator|.
name|create
argument_list|()
decl_stmt|;
name|Document
name|wsdl
init|=
name|getWSDLDocument
argument_list|(
name|server
argument_list|)
decl_stmt|;
name|assertValid
argument_list|(
literal|"//*[@name='string2stringMap']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReturnMap
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsServerFactoryBean
name|sf
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setServiceClass
argument_list|(
name|StudentService
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setServiceBean
argument_list|(
operator|new
name|StudentServiceImpl
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"local://StudentService"
argument_list|)
expr_stmt|;
name|setupAegis
argument_list|(
name|sf
argument_list|)
expr_stmt|;
name|Server
name|server
init|=
name|sf
operator|.
name|create
argument_list|()
decl_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
name|JaxWsProxyFactoryBean
name|proxyFac
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|proxyFac
operator|.
name|setAddress
argument_list|(
literal|"local://StudentService"
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|setServiceClass
argument_list|(
name|StudentService
operator|.
name|class
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|setupAegis
argument_list|(
name|proxyFac
operator|.
name|getClientFactoryBean
argument_list|()
argument_list|)
expr_stmt|;
name|StudentService
name|clientInterface
init|=
operator|(
name|StudentService
operator|)
name|proxyFac
operator|.
name|create
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Long
argument_list|,
name|Student
argument_list|>
name|fullMap
init|=
name|clientInterface
operator|.
name|getStudentsMap
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|fullMap
argument_list|)
expr_stmt|;
name|Student
name|one
init|=
name|fullMap
operator|.
name|get
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|one
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Student1"
argument_list|,
name|one
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|wildMap
init|=
name|clientInterface
operator|.
name|getWildcardMap
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"valuestring"
argument_list|,
name|wildMap
operator|.
name|get
argument_list|(
literal|"keystring"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReturnMapDocLiteral
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsServerFactoryBean
name|sf
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setServiceClass
argument_list|(
name|StudentServiceDocLiteral
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setServiceBean
argument_list|(
operator|new
name|StudentServiceDocLiteralImpl
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"local://StudentServiceDocLiteral"
argument_list|)
expr_stmt|;
name|setupAegis
argument_list|(
name|sf
argument_list|)
expr_stmt|;
name|Server
name|server
init|=
name|sf
operator|.
name|create
argument_list|()
decl_stmt|;
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
name|JaxWsProxyFactoryBean
name|proxyFac
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|proxyFac
operator|.
name|setAddress
argument_list|(
literal|"local://StudentServiceDocLiteral"
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|setServiceClass
argument_list|(
name|StudentServiceDocLiteral
operator|.
name|class
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|setupAegis
argument_list|(
name|proxyFac
operator|.
name|getClientFactoryBean
argument_list|()
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|StudentServiceDocLiteral
name|clientInterface
init|=
operator|(
name|StudentServiceDocLiteral
operator|)
name|proxyFac
operator|.
name|create
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Long
argument_list|,
name|Student
argument_list|>
name|fullMap
init|=
name|clientInterface
operator|.
name|getStudentsMap
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|fullMap
argument_list|)
expr_stmt|;
name|Student
name|one
init|=
name|fullMap
operator|.
name|get
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|one
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Student1"
argument_list|,
name|one
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

