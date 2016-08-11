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
name|service
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
name|endpoint
operator|.
name|Server
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
name|swagger
operator|.
name|Swagger2Feature
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
name|SpringApplication
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
name|context
operator|.
name|annotation
operator|.
name|Bean
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
name|hello1
operator|.
name|HelloServiceImpl1
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
name|hello2
operator|.
name|HelloServiceImpl2
import|;
end_import

begin_class
annotation|@
name|SpringBootApplication
specifier|public
class|class
name|SampleRestApplication
block|{
annotation|@
name|Autowired
specifier|private
name|Bus
name|bus
decl_stmt|;
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
name|SpringApplication
operator|.
name|run
argument_list|(
name|SampleRestApplication
operator|.
name|class
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Bean
specifier|public
name|Server
name|rsServer
parameter_list|()
block|{
name|JAXRSServerFactoryBean
name|endpoint
init|=
operator|new
name|JAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setServiceBeans
argument_list|(
name|Arrays
operator|.
expr|<
name|Object
operator|>
name|asList
argument_list|(
operator|new
name|HelloServiceImpl1
argument_list|()
argument_list|,
operator|new
name|HelloServiceImpl2
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setAddress
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setFeatures
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Swagger2Feature
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|endpoint
operator|.
name|create
argument_list|()
return|;
block|}
block|}
end_class

end_unit
