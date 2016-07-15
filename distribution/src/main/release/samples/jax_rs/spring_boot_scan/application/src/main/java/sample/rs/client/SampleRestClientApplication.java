begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|sample
operator|.
name|rs
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|UriBuilder
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
name|annotations
operator|.
name|Provider
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
name|annotations
operator|.
name|Provider
operator|.
name|Type
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
name|clustering
operator|.
name|FailoverFeature
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
name|clustering
operator|.
name|FailoverStrategy
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
name|clustering
operator|.
name|RandomStrategy
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
name|spring
operator|.
name|EnableJaxRsProxyClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|CommandLineRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|SpringBootApplication
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|builder
operator|.
name|SpringApplicationBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|ServiceInstance
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|discovery
operator|.
name|DiscoveryClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|netflix
operator|.
name|eureka
operator|.
name|EnableEurekaClient
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
name|stereotype
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|sample
operator|.
name|rs
operator|.
name|service
operator|.
name|HelloService
import|;
end_import

begin_class
annotation|@
name|SpringBootApplication
annotation|@
name|EnableEurekaClient
annotation|@
name|EnableJaxRsProxyClient
specifier|public
class|class
name|SampleRestClientApplication
block|{
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
operator|new
name|SpringApplicationBuilder
argument_list|(
name|SampleRestClientApplication
operator|.
name|class
argument_list|)
operator|.
name|web
argument_list|(
literal|false
argument_list|)
operator|.
name|run
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Bean
name|CommandLineRunner
name|initProxyClient
parameter_list|(
specifier|final
name|HelloService
name|service
parameter_list|)
block|{
return|return
operator|new
name|CommandLineRunner
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|(
name|String
modifier|...
name|runArgs
parameter_list|)
throws|throws
name|Exception
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|service
operator|.
name|sayHello
argument_list|(
literal|"ApacheCxfProxyUser1"
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|service
operator|.
name|sayHello
argument_list|(
literal|"ApacheCxfProxyUser2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|/**      * Basic Random selection of statically prepared addresses.      * More advanced strategies will periodically pull DiscoveryClient      * to update the addresses list      */
annotation|@
name|Component
annotation|@
name|Provider
argument_list|(
name|Type
operator|.
name|Feature
argument_list|)
specifier|static
class|class
name|EurekaFailoverFeature
extends|extends
name|FailoverFeature
block|{
annotation|@
name|Autowired
name|DiscoveryClient
name|discoveryClient
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|addresses
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|EurekaFailoverFeature
parameter_list|()
block|{
name|super
argument_list|(
literal|"eureka://registry"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|FailoverStrategy
name|getStrategy
parameter_list|()
block|{
for|for
control|(
name|ServiceInstance
name|s
range|:
name|discoveryClient
operator|.
name|getInstances
argument_list|(
literal|"jaxrs-hello-world-service"
argument_list|)
control|)
block|{
name|UriBuilder
name|ub
init|=
name|UriBuilder
operator|.
name|fromUri
argument_list|(
name|s
operator|.
name|getUri
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|getMetadata
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"servletPath"
argument_list|)
condition|)
block|{
name|ub
operator|.
name|path
argument_list|(
name|s
operator|.
name|getMetadata
argument_list|()
operator|.
name|get
argument_list|(
literal|"servletPath"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|addresses
operator|.
name|add
argument_list|(
name|ub
operator|.
name|build
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|RandomStrategy
name|rs
init|=
operator|new
name|RandomStrategy
argument_list|()
decl_stmt|;
name|rs
operator|.
name|setAlternateAddresses
argument_list|(
name|addresses
argument_list|)
expr_stmt|;
return|return
name|rs
return|;
block|}
block|}
block|}
end_class

end_unit

