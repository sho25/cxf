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
name|JAXRSServerFactoryBean
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
name|resources
operator|.
name|BookStore
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
name|resources
operator|.
name|BookStoreNoAnnotations
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
name|resources
operator|.
name|BookStoreSubresourcesOnly
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
name|JAXRSServerFactoryBeanTest
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
name|testServers
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
literal|"/org/apache/cxf/jaxrs/spring/servers.xml"
block|}
argument_list|)
decl_stmt|;
name|JAXRSServerFactoryBean
name|sfb
init|=
operator|(
name|JAXRSServerFactoryBean
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"simple"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong address"
argument_list|,
literal|"http://localhost:8080/rs"
argument_list|,
name|sfb
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"The resource classes should not be null"
argument_list|,
name|sfb
operator|.
name|getResourceClasses
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong resource class"
argument_list|,
name|BookStore
operator|.
name|class
argument_list|,
name|sfb
operator|.
name|getResourceClasses
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://books.com"
argument_list|,
literal|"BookService"
argument_list|)
argument_list|,
name|sfb
operator|.
name|getServiceName
argument_list|()
argument_list|)
expr_stmt|;
name|sfb
operator|=
operator|(
name|JAXRSServerFactoryBean
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"inlineServiceBeans"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"The resource classes should not be null"
argument_list|,
name|sfb
operator|.
name|getResourceClasses
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong resource class"
argument_list|,
name|BookStore
operator|.
name|class
argument_list|,
name|sfb
operator|.
name|getResourceClasses
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong resource class"
argument_list|,
name|BookStoreSubresourcesOnly
operator|.
name|class
argument_list|,
name|sfb
operator|.
name|getResourceClasses
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|sfb
operator|=
operator|(
name|JAXRSServerFactoryBean
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"inlineProvider"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"The provider should not be null"
argument_list|,
name|sfb
operator|.
name|getProviders
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong provider size"
argument_list|,
name|sfb
operator|.
name|getProviders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|sfb
operator|=
operator|(
name|JAXRSServerFactoryBean
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"moduleServer"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"The resource classes should not be null"
argument_list|,
name|sfb
operator|.
name|getResourceClasses
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong ResourceClasses size"
argument_list|,
literal|1
argument_list|,
name|sfb
operator|.
name|getResourceClasses
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong resource class"
argument_list|,
name|BookStoreNoAnnotations
operator|.
name|class
argument_list|,
name|sfb
operator|.
name|getResourceClasses
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

