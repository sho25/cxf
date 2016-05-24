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
name|ws
operator|.
name|transfer
operator|.
name|resource
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

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Action
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
name|soap
operator|.
name|Addressing
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
name|ws
operator|.
name|transfer
operator|.
name|Delete
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
name|ws
operator|.
name|transfer
operator|.
name|DeleteResponse
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
name|ws
operator|.
name|transfer
operator|.
name|Get
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
name|ws
operator|.
name|transfer
operator|.
name|GetResponse
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
name|ws
operator|.
name|transfer
operator|.
name|Put
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
name|ws
operator|.
name|transfer
operator|.
name|PutResponse
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
name|ws
operator|.
name|transfer
operator|.
name|shared
operator|.
name|TransferConstants
import|;
end_import

begin_comment
comment|/**  * The interface definition of a Resource web service, according to the specification.  */
end_comment

begin_interface
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
name|TransferConstants
operator|.
name|TRANSFER_2011_03_NAMESPACE
argument_list|,
name|name
operator|=
name|TransferConstants
operator|.
name|NAME_RESOURCE
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
annotation|@
name|Addressing
argument_list|(
name|enabled
operator|=
literal|true
argument_list|,
name|required
operator|=
literal|true
argument_list|)
specifier|public
interface|interface
name|Resource
block|{
annotation|@
name|Action
argument_list|(
name|input
operator|=
name|TransferConstants
operator|.
name|ACTION_GET
argument_list|,
name|output
operator|=
name|TransferConstants
operator|.
name|ACTION_GET_RESPONSE
argument_list|)
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
name|TransferConstants
operator|.
name|NAME_OPERATION_GET
argument_list|)
annotation|@
name|WebResult
argument_list|(
name|name
operator|=
name|TransferConstants
operator|.
name|NAME_MESSAGE_GET_RESPONSE
argument_list|,
name|targetNamespace
operator|=
name|TransferConstants
operator|.
name|TRANSFER_2011_03_NAMESPACE
argument_list|,
name|partName
operator|=
literal|"Body"
argument_list|)
name|GetResponse
name|get
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
name|TransferConstants
operator|.
name|NAME_MESSAGE_GET
argument_list|,
name|targetNamespace
operator|=
name|TransferConstants
operator|.
name|TRANSFER_2011_03_NAMESPACE
argument_list|,
name|partName
operator|=
literal|"Body"
argument_list|)
name|Get
name|body
parameter_list|)
function_decl|;
annotation|@
name|Action
argument_list|(
name|input
operator|=
name|TransferConstants
operator|.
name|ACTION_DELETE
argument_list|,
name|output
operator|=
name|TransferConstants
operator|.
name|ACTION_DELETE_RESPONSE
argument_list|)
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
name|TransferConstants
operator|.
name|NAME_OPERATION_DELETE
argument_list|)
annotation|@
name|WebResult
argument_list|(
name|name
operator|=
name|TransferConstants
operator|.
name|NAME_MESSAGE_DELETE_RESPONSE
argument_list|,
name|targetNamespace
operator|=
name|TransferConstants
operator|.
name|TRANSFER_2011_03_NAMESPACE
argument_list|,
name|partName
operator|=
literal|"Body"
argument_list|)
name|DeleteResponse
name|delete
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
name|TransferConstants
operator|.
name|NAME_MESSAGE_DELETE
argument_list|,
name|targetNamespace
operator|=
name|TransferConstants
operator|.
name|TRANSFER_2011_03_NAMESPACE
argument_list|,
name|partName
operator|=
literal|"Body"
argument_list|)
name|Delete
name|body
parameter_list|)
function_decl|;
annotation|@
name|Action
argument_list|(
name|input
operator|=
name|TransferConstants
operator|.
name|ACTION_PUT
argument_list|,
name|output
operator|=
name|TransferConstants
operator|.
name|ACTION_PUT_RESPONSE
argument_list|)
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
name|TransferConstants
operator|.
name|NAME_OPERATION_PUT
argument_list|)
annotation|@
name|WebResult
argument_list|(
name|name
operator|=
name|TransferConstants
operator|.
name|NAME_MESSAGE_PUT_RESPONSE
argument_list|,
name|targetNamespace
operator|=
name|TransferConstants
operator|.
name|TRANSFER_2011_03_NAMESPACE
argument_list|,
name|partName
operator|=
literal|"Body"
argument_list|)
name|PutResponse
name|put
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
name|TransferConstants
operator|.
name|NAME_MESSAGE_PUT
argument_list|,
name|targetNamespace
operator|=
name|TransferConstants
operator|.
name|TRANSFER_2011_03_NAMESPACE
argument_list|,
name|partName
operator|=
literal|"Body"
argument_list|)
name|Put
name|body
parameter_list|)
function_decl|;
block|}
end_interface

end_unit
