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
name|wsdl
operator|.
name|service
operator|.
name|factory
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|WSDLException
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
name|service
operator|.
name|factory
operator|.
name|FactoryBeanListenerManager
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
name|factory
operator|.
name|ServiceConstructionException
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
name|wsdl
operator|.
name|WSDLManager
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
name|easymock
operator|.
name|IMocksControl
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|ReflectionServiceFactorBeanTest
block|{
specifier|protected
name|IMocksControl
name|control
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
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
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
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEmptyWsdlAndNoServiceClass
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|dummyWsdl
init|=
literal|"target/dummy.wsdl"
decl_stmt|;
name|ReflectionServiceFactoryBean
name|bean
init|=
operator|new
name|ReflectionServiceFactoryBean
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
decl_stmt|;
name|WSDLManager
name|wsdlmanager
init|=
name|control
operator|.
name|createMock
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|wsdlmanager
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|wsdlmanager
operator|.
name|getDefinition
argument_list|(
name|dummyWsdl
argument_list|)
argument_list|)
operator|.
name|andThrow
argument_list|(
operator|new
name|WSDLException
argument_list|(
literal|"PARSER_ERROR"
argument_list|,
literal|"Problem parsing '"
operator|+
name|dummyWsdl
operator|+
literal|"'."
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|FactoryBeanListenerManager
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|bean
operator|.
name|setWsdlURL
argument_list|(
name|dummyWsdl
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setServiceName
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_soap_http"
argument_list|,
literal|"GreeterService"
argument_list|)
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
try|try
block|{
name|bean
operator|.
name|create
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"no valid wsdl nor service class specified"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ServiceConstructionException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
end_class

end_unit

