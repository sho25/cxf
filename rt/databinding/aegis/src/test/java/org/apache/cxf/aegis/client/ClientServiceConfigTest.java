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
name|client
package|;
end_package

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
name|service
operator|.
name|factory
operator|.
name|ReflectionServiceFactoryBean
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

begin_class
specifier|public
class|class
name|ClientServiceConfigTest
extends|extends
name|AbstractAegisTest
block|{
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|ReflectionServiceFactoryBean
name|factory
init|=
operator|new
name|ReflectionServiceFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setInvoker
argument_list|(
operator|new
name|BeanInvoker
argument_list|(
operator|new
name|EchoImpl
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setDataBinding
argument_list|(
operator|new
name|AegisDatabinding
argument_list|()
argument_list|)
expr_stmt|;
name|ServerFactoryBean
name|svrFac
init|=
operator|new
name|ServerFactoryBean
argument_list|()
decl_stmt|;
name|svrFac
operator|.
name|setAddress
argument_list|(
literal|"local://Echo"
argument_list|)
expr_stmt|;
name|svrFac
operator|.
name|setServiceFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|svrFac
operator|.
name|setServiceClass
argument_list|(
name|Echo
operator|.
name|class
argument_list|)
expr_stmt|;
name|svrFac
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|svrFac
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|ordinaryParamNameTest
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
name|ReflectionServiceFactoryBean
name|factory
init|=
operator|new
name|ReflectionServiceFactoryBean
argument_list|()
decl_stmt|;
name|proxyFac
operator|.
name|setServiceFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|setDataBinding
argument_list|(
operator|new
name|AegisDatabinding
argument_list|()
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|setAddress
argument_list|(
literal|"local://Echo"
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|setServiceClass
argument_list|(
name|Echo
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
name|Echo
name|echo
init|=
operator|(
name|Echo
operator|)
name|proxyFac
operator|.
name|create
argument_list|()
decl_stmt|;
name|String
name|boing
init|=
name|echo
operator|.
name|simpleEcho
argument_list|(
literal|"reflection"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"reflection"
argument_list|,
name|boing
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

