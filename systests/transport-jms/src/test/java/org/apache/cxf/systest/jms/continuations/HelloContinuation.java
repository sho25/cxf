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
name|jms
operator|.
name|continuations
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
name|WebService
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|soap
operator|.
name|SOAPBinding
import|;
end_import

begin_interface
annotation|@
name|SOAPBinding
argument_list|(
name|style
operator|=
name|SOAPBinding
operator|.
name|Style
operator|.
name|RPC
argument_list|,
name|use
operator|=
name|SOAPBinding
operator|.
name|Use
operator|.
name|LITERAL
argument_list|)
annotation|@
name|WebService
argument_list|(
name|name
operator|=
literal|"HelloContinuation"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/systest/jaxws"
argument_list|)
specifier|public
interface|interface
name|HelloContinuation
block|{
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
literal|"sayHi"
argument_list|,
name|exclude
operator|=
literal|false
argument_list|)
name|String
name|sayHi
parameter_list|(
name|String
name|firstName
parameter_list|,
name|String
name|secondName
parameter_list|)
function_decl|;
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
literal|"isRequestSuspended"
argument_list|,
name|exclude
operator|=
literal|false
argument_list|)
name|boolean
name|isRequestSuspended
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
literal|"resumeRequest"
argument_list|,
name|exclude
operator|=
literal|false
argument_list|)
name|void
name|resumeRequest
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

