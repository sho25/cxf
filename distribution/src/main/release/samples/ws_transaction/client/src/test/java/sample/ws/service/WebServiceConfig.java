begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|sample
operator|.
name|ws
operator|.
name|service
package|;
end_package

begin_import
import|import
name|com
operator|.
name|arjuna
operator|.
name|webservices11
operator|.
name|wsat
operator|.
name|sei
operator|.
name|CoordinatorPortTypeImpl
import|;
end_import

begin_import
import|import
name|com
operator|.
name|arjuna
operator|.
name|webservices11
operator|.
name|wscoor
operator|.
name|sei
operator|.
name|RegistrationPortTypeImpl
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
name|jaxws
operator|.
name|EndpointImpl
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Endpoint
import|;
end_import

begin_class
annotation|@
name|Configuration
specifier|public
class|class
name|WebServiceConfig
block|{
annotation|@
name|Autowired
specifier|private
name|Bus
name|bus
decl_stmt|;
annotation|@
name|Bean
specifier|public
name|Endpoint
name|registration
parameter_list|()
block|{
name|EndpointImpl
name|endpoint
init|=
operator|new
name|EndpointImpl
argument_list|(
name|bus
argument_list|,
operator|new
name|RegistrationPortTypeImpl
argument_list|()
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|publish
argument_list|(
literal|"/ws-c11/RegistrationService"
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
annotation|@
name|Bean
specifier|public
name|Endpoint
name|coordinator
parameter_list|()
block|{
name|EndpointImpl
name|endpoint
init|=
operator|new
name|EndpointImpl
argument_list|(
name|bus
argument_list|,
operator|new
name|CoordinatorPortTypeImpl
argument_list|()
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|publish
argument_list|(
literal|"/ws-t11-coordinator/CoordinatorService"
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

