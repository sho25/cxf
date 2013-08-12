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
name|jaxrs
operator|.
name|client
operator|.
name|spring
package|;
end_package

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
name|jaxrs
operator|.
name|client
operator|.
name|JAXRSClientFactoryBean
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
name|Assert
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
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
specifier|public
class|class
name|JAXRSClientFactoryBeanTest
extends|extends
name|Assert
block|{
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
name|BusFactory
operator|.
name|getDefaultBus
argument_list|(
literal|false
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|BusFactory
operator|.
name|getDefaultBus
argument_list|(
literal|false
argument_list|)
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClients
parameter_list|()
throws|throws
name|Exception
block|{
name|ClassPathXmlApplicationContext
name|ctx
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"/org/apache/cxf/jaxrs/client/spring/clients.xml"
block|}
argument_list|)
decl_stmt|;
name|Object
name|bean
init|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"client1.proxyFactory"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|bean
operator|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"setHeaderClient.proxyFactory"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|JAXRSClientFactoryBean
name|cfb
init|=
operator|(
name|JAXRSClientFactoryBean
operator|)
name|bean
decl_stmt|;
name|assertNotNull
argument_list|(
name|cfb
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong map size"
argument_list|,
name|cfb
operator|.
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong username"
argument_list|,
name|cfb
operator|.
name|getUsername
argument_list|()
argument_list|,
literal|"username"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong password"
argument_list|,
name|cfb
operator|.
name|getPassword
argument_list|()
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
name|QName
name|serviceQName
init|=
operator|new
name|QName
argument_list|(
literal|"http://books.com"
argument_list|,
literal|"BookService"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|serviceQName
argument_list|,
name|cfb
operator|.
name|getServiceName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|serviceQName
argument_list|,
name|cfb
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|getServiceName
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"ModelClient.proxyFactory"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|cfb
operator|=
operator|(
name|JAXRSClientFactoryBean
operator|)
name|bean
expr_stmt|;
name|assertNotNull
argument_list|(
name|cfb
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong map size"
argument_list|,
name|cfb
operator|.
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong username"
argument_list|,
name|cfb
operator|.
name|getUsername
argument_list|()
argument_list|,
literal|"username"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong password"
argument_list|,
name|cfb
operator|.
name|getPassword
argument_list|()
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

