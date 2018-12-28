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
name|systest
operator|.
name|jaxws
operator|.
name|beanpostprocessor
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
name|endpoint
operator|.
name|Client
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
name|jaxb
operator|.
name|JAXBDataBinding
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
name|JaxWsClientFactoryBean
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
name|test
operator|.
name|AbstractCXFSpringTest
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

begin_comment
comment|/**  * The majority of this test happens when the context is loaded.  */
end_comment

begin_class
specifier|public
class|class
name|BeanPostProcessorTest
extends|extends
name|AbstractCXFSpringTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|verifyServices
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsClientFactoryBean
name|cf
init|=
operator|new
name|JaxWsClientFactoryBean
argument_list|()
decl_stmt|;
name|cf
operator|.
name|setAddress
argument_list|(
literal|"local://services/Alger"
argument_list|)
expr_stmt|;
name|cf
operator|.
name|setServiceClass
argument_list|(
name|IWebServiceRUs
operator|.
name|class
argument_list|)
expr_stmt|;
name|Client
name|client
init|=
name|cf
operator|.
name|create
argument_list|()
decl_stmt|;
name|String
name|response
init|=
operator|(
name|String
operator|)
name|client
operator|.
name|invoke
argument_list|(
literal|"consultTheOracle"
argument_list|)
index|[
literal|0
index|]
decl_stmt|;
name|assertEquals
argument_list|(
literal|"All your bases belong to us."
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|Service
name|service
init|=
name|WebServiceRUs
operator|.
name|getService
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|JAXBDataBinding
operator|.
name|class
argument_list|,
name|service
operator|.
name|getDataBinding
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|String
index|[]
name|getConfigLocations
parameter_list|()
block|{
return|return
operator|new
name|String
index|[]
block|{
literal|"/org/apache/cxf/systest/jaxws/beanpostprocessor/context.xml"
block|}
return|;
block|}
block|}
end_class

end_unit

