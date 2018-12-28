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
name|exception
package|;
end_package

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
name|aegis
operator|.
name|databinding
operator|.
name|AegisDatabinding
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
name|frontend
operator|.
name|ClientProxyFactoryBean
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
name|invoker
operator|.
name|BeanInvoker
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

begin_class
specifier|public
class|class
name|ExceptionTest
extends|extends
name|AbstractAegisTest
block|{
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|Server
name|s
init|=
name|createService
argument_list|(
name|ExceptionService
operator|.
name|class
argument_list|,
operator|new
name|ExceptionServiceImpl
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|s
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|setInvoker
argument_list|(
operator|new
name|BeanInvoker
argument_list|(
operator|new
name|ExceptionServiceImpl
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|ClientProxyFactoryBean
name|proxyFac
init|=
operator|new
name|ClientProxyFactoryBean
argument_list|()
decl_stmt|;
name|proxyFac
operator|.
name|setAddress
argument_list|(
literal|"local://ExceptionService"
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
name|ExceptionService
name|client
init|=
name|proxyFac
operator|.
name|create
argument_list|(
name|ExceptionService
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|client
operator|.
name|sayHiWithException
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Must throw exception!"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|HelloException
name|e
parameter_list|)
block|{
comment|// nothing
block|}
comment|//check to make sure the fault is an element
name|Document
name|wsdl
init|=
name|getWSDLDocument
argument_list|(
literal|"ExceptionService"
argument_list|)
decl_stmt|;
name|addNamespace
argument_list|(
literal|"tns"
argument_list|,
literal|"http://exception.aegis.cxf.apache.org"
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//wsdl:message[@name='HelloException']/wsdl:part[@name='HelloException']"
operator|+
literal|"[@element='tns:String']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|HelloException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testJaxwsServerSimpleClient
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsServerFactoryBean
name|sfbean
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|sfbean
operator|.
name|setServiceClass
argument_list|(
name|ExceptionService
operator|.
name|class
argument_list|)
expr_stmt|;
name|sfbean
operator|.
name|setDataBinding
argument_list|(
operator|new
name|AegisDatabinding
argument_list|()
argument_list|)
expr_stmt|;
name|sfbean
operator|.
name|setAddress
argument_list|(
literal|"local://ExceptionServiceJaxWs1"
argument_list|)
expr_stmt|;
name|Server
name|server
init|=
name|sfbean
operator|.
name|create
argument_list|()
decl_stmt|;
name|Service
name|service
init|=
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|service
operator|.
name|setInvoker
argument_list|(
operator|new
name|BeanInvoker
argument_list|(
operator|new
name|ExceptionServiceImpl
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ClientProxyFactoryBean
name|proxyFac
init|=
operator|new
name|ClientProxyFactoryBean
argument_list|()
decl_stmt|;
name|proxyFac
operator|.
name|setAddress
argument_list|(
literal|"local://ExceptionServiceJaxWs1"
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
name|ExceptionService
name|clientInterface
init|=
name|proxyFac
operator|.
name|create
argument_list|(
name|ExceptionService
operator|.
name|class
argument_list|)
decl_stmt|;
name|clientInterface
operator|.
name|sayHiWithException
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|HelloException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testJaxwsNoXfireCompat
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsServerFactoryBean
name|sfbean
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|sfbean
operator|.
name|setServiceClass
argument_list|(
name|ExceptionService
operator|.
name|class
argument_list|)
expr_stmt|;
name|sfbean
operator|.
name|setDataBinding
argument_list|(
operator|new
name|AegisDatabinding
argument_list|()
argument_list|)
expr_stmt|;
name|sfbean
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setDataBinding
argument_list|(
name|sfbean
operator|.
name|getDataBinding
argument_list|()
argument_list|)
expr_stmt|;
name|sfbean
operator|.
name|setAddress
argument_list|(
literal|"local://ExceptionServiceJaxWs"
argument_list|)
expr_stmt|;
name|Server
name|server
init|=
name|sfbean
operator|.
name|create
argument_list|()
decl_stmt|;
name|Service
name|service
init|=
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|service
operator|.
name|setInvoker
argument_list|(
operator|new
name|BeanInvoker
argument_list|(
operator|new
name|ExceptionServiceImpl
argument_list|()
argument_list|)
argument_list|)
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
literal|"local://ExceptionServiceJaxWs"
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|setServiceClass
argument_list|(
name|ExceptionService
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
name|proxyFac
operator|.
name|getClientFactoryBean
argument_list|()
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setDataBinding
argument_list|(
operator|new
name|AegisDatabinding
argument_list|()
argument_list|)
expr_stmt|;
name|ExceptionService
name|clientInterface
init|=
operator|(
name|ExceptionService
operator|)
name|proxyFac
operator|.
name|create
argument_list|()
decl_stmt|;
name|clientInterface
operator|.
name|sayHiWithException
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|HelloException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testJaxws
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsServerFactoryBean
name|sfbean
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|sfbean
operator|.
name|setServiceClass
argument_list|(
name|ExceptionService
operator|.
name|class
argument_list|)
expr_stmt|;
name|setupAegis
argument_list|(
name|sfbean
argument_list|)
expr_stmt|;
name|sfbean
operator|.
name|setAddress
argument_list|(
literal|"local://ExceptionService4"
argument_list|)
expr_stmt|;
name|Server
name|server
init|=
name|sfbean
operator|.
name|create
argument_list|()
decl_stmt|;
name|Service
name|service
init|=
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|service
operator|.
name|setInvoker
argument_list|(
operator|new
name|BeanInvoker
argument_list|(
operator|new
name|ExceptionServiceImpl
argument_list|()
argument_list|)
argument_list|)
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
literal|"local://ExceptionService4"
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
name|ExceptionService
name|clientInterface
init|=
name|proxyFac
operator|.
name|create
argument_list|(
name|ExceptionService
operator|.
name|class
argument_list|)
decl_stmt|;
name|clientInterface
operator|.
name|sayHiWithException
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|ExceptionServiceImpl
implements|implements
name|ExceptionService
block|{
specifier|public
name|String
name|sayHiWithException
parameter_list|()
throws|throws
name|HelloException
block|{
name|HelloException
name|ex
init|=
operator|new
name|HelloException
argument_list|()
decl_stmt|;
name|ex
operator|.
name|setFaultInfo
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
block|}
end_class

end_unit

