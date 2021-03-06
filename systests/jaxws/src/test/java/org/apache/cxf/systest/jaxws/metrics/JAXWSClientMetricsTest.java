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
name|metrics
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|binding
operator|.
name|soap
operator|.
name|SoapFault
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
name|common
operator|.
name|i18n
operator|.
name|UncheckedException
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
name|endpoint
operator|.
name|Endpoint
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
name|message
operator|.
name|Exchange
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
name|metrics
operator|.
name|MetricsContext
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
name|metrics
operator|.
name|MetricsFeature
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
name|metrics
operator|.
name|MetricsProvider
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
name|BindingOperationInfo
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
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
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
name|junit
operator|.
name|rules
operator|.
name|ExpectedException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mockito
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|junit
operator|.
name|MockitoJUnitRunner
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
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|anyLong
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|times
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
specifier|public
class|class
name|JAXWSClientMetricsTest
extends|extends
name|AbstractCXFSpringTest
block|{
annotation|@
name|Rule
specifier|public
name|ExpectedException
name|expectedException
init|=
name|ExpectedException
operator|.
name|none
argument_list|()
decl_stmt|;
specifier|private
name|MetricsProvider
name|provider
decl_stmt|;
specifier|private
name|MetricsContext
name|operationContext
decl_stmt|;
specifier|private
name|MetricsContext
name|resourceContext
decl_stmt|;
specifier|private
name|MetricsContext
name|endpointContext
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|endpointContext
operator|=
name|Mockito
operator|.
name|mock
argument_list|(
name|MetricsContext
operator|.
name|class
argument_list|)
expr_stmt|;
name|operationContext
operator|=
name|Mockito
operator|.
name|mock
argument_list|(
name|MetricsContext
operator|.
name|class
argument_list|)
expr_stmt|;
name|resourceContext
operator|=
name|Mockito
operator|.
name|mock
argument_list|(
name|MetricsContext
operator|.
name|class
argument_list|)
expr_stmt|;
name|provider
operator|=
operator|new
name|MetricsProvider
argument_list|()
block|{
specifier|public
name|MetricsContext
name|createEndpointContext
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|boolean
name|asClient
parameter_list|,
name|String
name|cid
parameter_list|)
block|{
return|return
name|endpointContext
return|;
block|}
specifier|public
name|MetricsContext
name|createOperationContext
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|BindingOperationInfo
name|boi
parameter_list|,
name|boolean
name|asClient
parameter_list|,
name|String
name|cid
parameter_list|)
block|{
return|return
name|operationContext
return|;
block|}
specifier|public
name|MetricsContext
name|createResourceContext
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|String
name|resourceName
parameter_list|,
name|boolean
name|asClient
parameter_list|,
name|String
name|cid
parameter_list|)
block|{
return|return
name|resourceContext
return|;
block|}
block|}
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|usingClientProxyStopIsCalledWhenServerReturnsResponse
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|JaxWsClientFactoryBean
name|factory
init|=
operator|new
name|JaxWsClientFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
literal|"local://services/Book"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|IBookWebService
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setFeatures
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|MetricsFeature
argument_list|(
name|provider
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
specifier|final
name|Client
name|client
init|=
name|factory
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
literal|"getBook"
argument_list|,
literal|10
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
block|}
finally|finally
block|{
name|Mockito
operator|.
name|verify
argument_list|(
name|operationContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|start
argument_list|(
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|operationContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|stop
argument_list|(
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|endpointContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|start
argument_list|(
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|endpointContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|stop
argument_list|(
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verifyZeroInteractions
argument_list|(
name|resourceContext
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|usingClientProxyStopIsCalledWhenServerReturnsFault
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|JaxWsClientFactoryBean
name|factory
init|=
operator|new
name|JaxWsClientFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
literal|"local://services/Book"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|IBookWebService
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setFeatures
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|MetricsFeature
argument_list|(
name|provider
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
specifier|final
name|Client
name|client
init|=
name|factory
operator|.
name|create
argument_list|()
decl_stmt|;
name|expectedException
operator|.
name|expect
argument_list|(
name|SoapFault
operator|.
name|class
argument_list|)
expr_stmt|;
name|client
operator|.
name|invoke
argument_list|(
literal|"getBook"
argument_list|,
literal|11
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Mockito
operator|.
name|verify
argument_list|(
name|operationContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|start
argument_list|(
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|operationContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|stop
argument_list|(
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|endpointContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|start
argument_list|(
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|endpointContext
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|stop
argument_list|(
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verifyZeroInteractions
argument_list|(
name|resourceContext
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|usingClientProxyStopIsCalledForUnsupportedOperation
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|JaxWsClientFactoryBean
name|factory
init|=
operator|new
name|JaxWsClientFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
literal|"local://services/Book"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|IBookWebService
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setFeatures
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|MetricsFeature
argument_list|(
name|provider
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
specifier|final
name|Client
name|client
init|=
name|factory
operator|.
name|create
argument_list|()
decl_stmt|;
name|expectedException
operator|.
name|expect
argument_list|(
name|UncheckedException
operator|.
name|class
argument_list|)
expr_stmt|;
name|client
operator|.
name|invoke
argument_list|(
literal|"getBooks"
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Mockito
operator|.
name|verifyZeroInteractions
argument_list|(
name|endpointContext
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verifyZeroInteractions
argument_list|(
name|operationContext
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verifyZeroInteractions
argument_list|(
name|resourceContext
argument_list|)
expr_stmt|;
block|}
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
literal|"/org/apache/cxf/systest/jaxws/metrics/context.xml"
block|}
return|;
block|}
block|}
end_class

end_unit

