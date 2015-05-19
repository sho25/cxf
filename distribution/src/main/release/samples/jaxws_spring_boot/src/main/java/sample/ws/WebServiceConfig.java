begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2012-2014 the original author or authors.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|sample
operator|.
name|ws
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|embedded
operator|.
name|ServletRegistrationBean
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
name|annotation
operator|.
name|Bean
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
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|ClassPathResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|config
operator|.
name|annotation
operator|.
name|EnableWs
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|config
operator|.
name|annotation
operator|.
name|WsConfigurerAdapter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|transport
operator|.
name|http
operator|.
name|MessageDispatcherServlet
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
name|bus
operator|.
name|spring
operator|.
name|SpringBus
import|;
end_import

begin_import
import|import
name|sample
operator|.
name|ws
operator|.
name|service
operator|.
name|Hello
import|;
end_import

begin_import
import|import
name|sample
operator|.
name|ws
operator|.
name|service
operator|.
name|HelloPortImpl
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
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
name|transport
operator|.
name|servlet
operator|.
name|CXFServlet
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
name|EndpointImpl
import|;
end_import

begin_class
annotation|@
name|EnableWs
annotation|@
name|Configuration
specifier|public
class|class
name|WebServiceConfig
extends|extends
name|WsConfigurerAdapter
block|{
annotation|@
name|Bean
specifier|public
name|ServletRegistrationBean
name|dispatcherServlet
parameter_list|()
block|{
name|CXFServlet
name|cxfServlet
init|=
operator|new
name|CXFServlet
argument_list|()
decl_stmt|;
return|return
operator|new
name|ServletRegistrationBean
argument_list|(
name|cxfServlet
argument_list|,
literal|"/Service/*"
argument_list|)
return|;
block|}
annotation|@
name|Bean
argument_list|(
name|name
operator|=
literal|"cxf"
argument_list|)
specifier|public
name|SpringBus
name|springBus
parameter_list|()
block|{
return|return
operator|new
name|SpringBus
argument_list|()
return|;
block|}
annotation|@
name|Bean
specifier|public
name|Hello
name|myService
parameter_list|()
block|{
return|return
operator|new
name|HelloPortImpl
argument_list|()
return|;
block|}
annotation|@
name|Bean
specifier|public
name|Endpoint
name|endpoint
parameter_list|()
block|{
name|EndpointImpl
name|endpoint
init|=
operator|new
name|EndpointImpl
argument_list|(
name|springBus
argument_list|()
argument_list|,
name|myService
argument_list|()
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|publish
argument_list|(
literal|"/Hello"
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

