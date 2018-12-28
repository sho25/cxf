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
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Constructor
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
name|model
operator|.
name|ClassResourceInfo
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
name|BookStoreConstructor
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
name|ApplicationContext
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertSame
import|;
end_import

begin_class
specifier|public
class|class
name|SpringResourceFactoryTest
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
name|testFactory
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
literal|"/org/apache/cxf/jaxrs/spring/servers2.xml"
block|}
argument_list|)
decl_stmt|;
name|verifyFactory
argument_list|(
name|ctx
argument_list|,
literal|"sfactory1"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|verifyFactory
argument_list|(
name|ctx
argument_list|,
literal|"sfactory2"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|Object
name|serverBean
init|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"server1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|serverBean
argument_list|)
expr_stmt|;
name|JAXRSServerFactoryBean
name|factoryBean
init|=
operator|(
name|JAXRSServerFactoryBean
operator|)
name|serverBean
decl_stmt|;
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|list
init|=
name|factoryBean
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|getClassResourceInfo
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|BookStoreConstructor
operator|.
name|class
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getServiceClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|BookStoreConstructor
operator|.
name|class
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getResourceClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|BookStore
operator|.
name|class
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getServiceClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|BookStore
operator|.
name|class
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getResourceClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifyFactory
parameter_list|(
name|ApplicationContext
name|ctx
parameter_list|,
name|String
name|factoryName
parameter_list|,
name|boolean
name|isSingleton
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|bean
init|=
name|ctx
operator|.
name|getBean
argument_list|(
name|factoryName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|SpringResourceFactory
name|sf
init|=
operator|(
name|SpringResourceFactory
operator|)
name|bean
decl_stmt|;
name|assertNotNull
argument_list|(
name|sf
operator|.
name|getApplicationContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|isSingleton
argument_list|,
name|sf
operator|.
name|isSingleton
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isSingleton
condition|)
block|{
name|Constructor
argument_list|<
name|?
argument_list|>
name|c
init|=
name|sf
operator|.
name|getBeanConstructor
argument_list|()
decl_stmt|;
name|Constructor
argument_list|<
name|BookStore
argument_list|>
name|c2
init|=
name|BookStore
operator|.
name|class
operator|.
name|getConstructor
argument_list|(
operator|new
name|Class
index|[]
block|{}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|c
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
argument_list|,
name|c2
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

