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
name|spec
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|Client
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|ClientBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|WebTarget
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
name|Fault
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
name|Interceptor
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
name|ClientConfiguration
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
name|WebClient
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
name|spec
operator|.
name|ClientImpl
operator|.
name|WebTargetImpl
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
name|Message
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
name|ClientImplTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|String
name|MY_INTERCEPTOR_NAME
init|=
literal|"MyInterceptor"
decl_stmt|;
specifier|private
specifier|static
class|class
name|MyInterceptor
implements|implements
name|Interceptor
argument_list|<
name|Message
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|MY_INTERCEPTOR_NAME
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
comment|// no-op
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleFault
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
comment|// no-op
block|}
block|}
comment|/**      * This test checks that we do not lose track of registered interceptors      * on the original client implementation after we create a new impl with      * the path(...) method - particularly when the path passed in to the      * path(...) method contains a template.      */
annotation|@
name|Test
specifier|public
name|void
name|testClientConfigCopiedOnPathCallWithTemplates
parameter_list|()
block|{
name|Client
name|client
init|=
name|ClientBuilder
operator|.
name|newClient
argument_list|()
decl_stmt|;
name|WebTarget
name|webTarget
init|=
name|client
operator|.
name|target
argument_list|(
literal|"http://localhost:8080/"
argument_list|)
decl_stmt|;
name|WebClient
name|webClient
init|=
name|getWebClient
argument_list|(
name|webTarget
argument_list|)
decl_stmt|;
name|ClientConfiguration
name|clientConfig
init|=
name|WebClient
operator|.
name|getConfig
argument_list|(
name|webClient
argument_list|)
decl_stmt|;
name|clientConfig
operator|.
name|setOutInterceptors
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|MyInterceptor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Precondition failed - original WebTarget is missing expected interceptor"
argument_list|,
name|doesClientConfigHaveMyInterceptor
argument_list|(
name|webClient
argument_list|)
argument_list|)
expr_stmt|;
name|WebTarget
name|webTargetAfterPath
init|=
name|webTarget
operator|.
name|path
argument_list|(
literal|"/rest/{key}/"
argument_list|)
operator|.
name|resolveTemplate
argument_list|(
literal|"key"
argument_list|,
literal|"myKey"
argument_list|)
decl_stmt|;
name|WebClient
name|webClientAfterPath
init|=
name|getWebClient
argument_list|(
name|webTargetAfterPath
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"New WebTarget is missing expected interceptor specified on 'parent' WebTarget's client impl"
argument_list|,
name|doesClientConfigHaveMyInterceptor
argument_list|(
name|webClientAfterPath
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|WebClient
name|getWebClient
parameter_list|(
name|WebTarget
name|webTarget
parameter_list|)
block|{
name|webTarget
operator|.
name|request
argument_list|()
expr_stmt|;
name|WebTargetImpl
name|webTargetImpl
init|=
operator|(
name|WebTargetImpl
operator|)
name|webTarget
decl_stmt|;
name|WebClient
name|webClient
init|=
name|webTargetImpl
operator|.
name|getWebClient
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No WebClient is associated with this WebTargetImpl"
argument_list|,
name|webClient
argument_list|)
expr_stmt|;
return|return
name|webClient
return|;
block|}
specifier|private
name|boolean
name|doesClientConfigHaveMyInterceptor
parameter_list|(
name|WebClient
name|webClient
parameter_list|)
block|{
name|ClientConfiguration
name|clientConfigAfterPath
init|=
name|WebClient
operator|.
name|getConfig
argument_list|(
name|webClient
argument_list|)
decl_stmt|;
name|boolean
name|foundMyInterceptor
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Interceptor
argument_list|<
name|?
argument_list|>
name|i
range|:
name|clientConfigAfterPath
operator|.
name|getOutInterceptors
argument_list|()
control|)
block|{
if|if
condition|(
name|MY_INTERCEPTOR_NAME
operator|.
name|equals
argument_list|(
name|i
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
name|foundMyInterceptor
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
return|return
name|foundMyInterceptor
return|;
block|}
comment|/**      * Similar to<code>testClientConfigCopiedOnPathCallWithTemplates</code>,      * this test uses a template, but in the initial call to target().  At this      * point, the WebTargetImpl's targetClient field will be null, so we need      * this test to ensure that there are no null pointers when creating and      * using a template on the first call to target().      */
annotation|@
name|Test
specifier|public
name|void
name|testTemplateInInitialTarget
parameter_list|()
block|{
name|String
name|address
init|=
literal|"http://localhost:8080/bookstore/{a}/simple"
decl_stmt|;
name|Client
name|client
init|=
name|ClientBuilder
operator|.
name|newClient
argument_list|()
decl_stmt|;
name|WebTarget
name|webTarget
init|=
name|client
operator|.
name|target
argument_list|(
name|address
argument_list|)
operator|.
name|resolveTemplate
argument_list|(
literal|"a"
argument_list|,
literal|"bookheaders"
argument_list|)
decl_stmt|;
name|webTarget
operator|.
name|request
argument_list|(
literal|"application/xml"
argument_list|)
operator|.
name|header
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|WebClient
name|webClient
init|=
name|getWebClient
argument_list|(
name|webTarget
argument_list|)
decl_stmt|;
name|ClientConfiguration
name|clientConfig
init|=
name|WebClient
operator|.
name|getConfig
argument_list|(
name|webClient
argument_list|)
decl_stmt|;
name|clientConfig
operator|.
name|setOutInterceptors
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|MyInterceptor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Precondition failed - original WebTarget is missing expected interceptor"
argument_list|,
name|doesClientConfigHaveMyInterceptor
argument_list|(
name|webClient
argument_list|)
argument_list|)
expr_stmt|;
name|WebTarget
name|webTargetAfterPath
init|=
name|webTarget
operator|.
name|path
argument_list|(
literal|"/rest/{key}/"
argument_list|)
operator|.
name|resolveTemplate
argument_list|(
literal|"key"
argument_list|,
literal|"myKey"
argument_list|)
decl_stmt|;
name|WebClient
name|webClientAfterPath
init|=
name|getWebClient
argument_list|(
name|webTargetAfterPath
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"New WebTarget is missing expected interceptor specified on 'parent' WebTarget's client impl"
argument_list|,
name|doesClientConfigHaveMyInterceptor
argument_list|(
name|webClientAfterPath
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
