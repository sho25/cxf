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
name|ws
operator|.
name|policy
operator|.
name|handler
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
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
name|Policies
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
name|Policy
import|;
end_import

begin_interface
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"HelloPolicyService"
argument_list|)
annotation|@
name|Policy
argument_list|(
name|placement
operator|=
name|Policy
operator|.
name|Placement
operator|.
name|BINDING
argument_list|,
name|uri
operator|=
literal|"classpath:/handler_policies/x509SecurityPolicy.xml"
argument_list|)
specifier|public
interface|interface
name|HelloService
block|{
annotation|@
name|WebMethod
argument_list|(
name|action
operator|=
literal|"checkHello"
argument_list|)
annotation|@
name|WebResult
argument_list|(
name|name
operator|=
literal|"result"
argument_list|)
annotation|@
name|Policies
argument_list|(
block|{
annotation|@
name|Policy
argument_list|(
name|uri
operator|=
literal|"classpath:/handler_policies/inputPolicy.xml"
argument_list|,
name|placement
operator|=
name|Policy
operator|.
name|Placement
operator|.
name|PORT_TYPE_OPERATION_INPUT
argument_list|)
block|,
annotation|@
name|Policy
argument_list|(
name|uri
operator|=
literal|"classpath:/handler_policies/outputPolicy.xml"
argument_list|,
name|placement
operator|=
name|Policy
operator|.
name|Placement
operator|.
name|PORT_TYPE_OPERATION_OUTPUT
argument_list|)
block|}
argument_list|)
name|boolean
name|checkHello
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"input"
argument_list|)
name|String
name|input
parameter_list|)
throws|throws
name|MyFault
function_decl|;
block|}
end_interface

end_unit

