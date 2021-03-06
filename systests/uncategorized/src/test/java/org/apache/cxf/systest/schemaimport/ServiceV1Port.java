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
name|schemaimport
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
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/xsd/test/"
argument_list|,
name|name
operator|=
literal|"service_v1_port"
argument_list|)
annotation|@
name|SOAPBinding
argument_list|(
name|parameterStyle
operator|=
name|SOAPBinding
operator|.
name|ParameterStyle
operator|.
name|BARE
argument_list|)
specifier|public
interface|interface
name|ServiceV1Port
block|{
annotation|@
name|WebResult
argument_list|(
name|name
operator|=
literal|"responseMessage"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/xsd/test/messages"
argument_list|,
name|partName
operator|=
literal|"response"
argument_list|)
annotation|@
name|WebMethod
argument_list|(
name|action
operator|=
literal|"http://cxf.apache.org/xsd/test/requestMessage"
argument_list|)
name|ResponseType
name|testRequest
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|partName
operator|=
literal|"request"
argument_list|,
name|name
operator|=
literal|"requestMessage"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/xsd/test/messages"
argument_list|)
name|RequestType
name|request
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

