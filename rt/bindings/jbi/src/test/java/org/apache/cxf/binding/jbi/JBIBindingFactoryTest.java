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
name|binding
operator|.
name|jbi
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
name|binding
operator|.
name|Binding
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
name|jbi
operator|.
name|interceptor
operator|.
name|JBIFaultOutInterceptor
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
name|jbi
operator|.
name|interceptor
operator|.
name|JBIOperationInInterceptor
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
name|jbi
operator|.
name|interceptor
operator|.
name|JBIWrapperInInterceptor
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
name|jbi
operator|.
name|interceptor
operator|.
name|JBIWrapperOutInterceptor
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
name|StaxOutInterceptor
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
name|ServiceInfo
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
name|JBIBindingFactoryTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testCreateBinding
parameter_list|()
block|{
name|JBIBindingInfo
name|info
init|=
operator|new
name|JBIBindingInfo
argument_list|(
operator|new
name|ServiceInfo
argument_list|()
argument_list|,
literal|"id"
argument_list|)
decl_stmt|;
name|Binding
name|binding
init|=
operator|new
name|JBIBindingFactory
argument_list|()
operator|.
name|createBinding
argument_list|(
name|info
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|binding
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|//assertEquals(?, binding.getInFaultInterceptors().size());
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|binding
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|binding
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|JBIOperationInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|binding
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|JBIWrapperInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|binding
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|StaxOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|binding
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|JBIWrapperOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|binding
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|StaxOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|binding
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|JBIFaultOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|binding
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

