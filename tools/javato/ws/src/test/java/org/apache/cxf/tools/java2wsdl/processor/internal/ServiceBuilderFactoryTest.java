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
name|tools
operator|.
name|java2wsdl
operator|.
name|processor
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|JaxwsServiceBuilder
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
name|ServiceBuilder
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
name|simple
operator|.
name|SimpleServiceBuilder
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
name|tools
operator|.
name|fortest
operator|.
name|classnoanno
operator|.
name|docbare
operator|.
name|Stock
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
name|tools
operator|.
name|fortest
operator|.
name|simple
operator|.
name|Hello
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

begin_class
specifier|public
class|class
name|ServiceBuilderFactoryTest
extends|extends
name|Assert
block|{
name|ServiceBuilderFactory
name|factory
init|=
name|ServiceBuilderFactory
operator|.
name|getInstance
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
argument_list|,
literal|"jaxb"
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testGetJaxwsBuilder
parameter_list|()
block|{
name|factory
operator|.
name|setServiceClass
argument_list|(
name|Stock
operator|.
name|class
argument_list|)
expr_stmt|;
name|ServiceBuilder
name|builder
init|=
name|factory
operator|.
name|newBuilder
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|builder
operator|instanceof
name|JaxwsServiceBuilder
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetSimpleBuilder
parameter_list|()
block|{
name|factory
operator|.
name|setServiceClass
argument_list|(
name|Hello
operator|.
name|class
argument_list|)
expr_stmt|;
name|ServiceBuilder
name|builder
init|=
name|factory
operator|.
name|newBuilder
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|builder
operator|instanceof
name|SimpleServiceBuilder
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

