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
name|jaxrs
operator|.
name|cdi
operator|.
name|unwrapper
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
name|common
operator|.
name|util
operator|.
name|ClassUnwrapper
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
name|systests
operator|.
name|cdi
operator|.
name|base
operator|.
name|BookStorePreMatchingRequestFilter
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
name|systests
operator|.
name|cdi
operator|.
name|base
operator|.
name|BookStoreRequestFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|weld
operator|.
name|environment
operator|.
name|se
operator|.
name|Weld
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|weld
operator|.
name|environment
operator|.
name|se
operator|.
name|WeldContainer
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
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|equalTo
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|not
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|notNullValue
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
name|assertThat
import|;
end_import

begin_class
specifier|public
class|class
name|ClassUnwrapperTest
block|{
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|WeldContainer
name|container
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
specifier|final
name|Weld
name|weld
init|=
operator|new
name|Weld
argument_list|()
decl_stmt|;
name|container
operator|=
name|weld
operator|.
name|initialize
argument_list|()
expr_stmt|;
name|bus
operator|=
name|getBeanReference
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|getBeanReference
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
name|container
operator|.
name|select
argument_list|(
name|clazz
argument_list|)
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|container
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProxyClassIsProperlyUnwrapped
parameter_list|()
block|{
specifier|final
name|BookStorePreMatchingRequestFilter
name|filter
init|=
name|getBeanReference
argument_list|(
name|BookStorePreMatchingRequestFilter
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|ClassUnwrapper
name|unwrapper
init|=
operator|(
name|ClassUnwrapper
operator|)
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassUnwrapper
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|unwrapper
argument_list|,
name|notNullValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|filter
operator|.
name|getClass
argument_list|()
argument_list|,
name|not
argument_list|(
name|equalTo
argument_list|(
name|BookStorePreMatchingRequestFilter
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|unwrapper
operator|.
name|getRealClass
argument_list|(
name|filter
argument_list|)
argument_list|,
name|equalTo
argument_list|(
name|BookStorePreMatchingRequestFilter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRealClassIsProperlyUnwrapped
parameter_list|()
block|{
specifier|final
name|BookStoreRequestFilter
name|filter
init|=
name|getBeanReference
argument_list|(
name|BookStoreRequestFilter
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|ClassUnwrapper
name|unwrapper
init|=
operator|(
name|ClassUnwrapper
operator|)
name|bus
operator|.
name|getProperty
argument_list|(
name|ClassUnwrapper
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|unwrapper
argument_list|,
name|notNullValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|filter
operator|.
name|getClass
argument_list|()
argument_list|,
name|equalTo
argument_list|(
name|BookStoreRequestFilter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|unwrapper
operator|.
name|getRealClass
argument_list|(
name|filter
argument_list|)
argument_list|,
name|equalTo
argument_list|(
name|BookStoreRequestFilter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

