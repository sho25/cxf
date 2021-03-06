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
name|binding
operator|.
name|soap
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_interface
specifier|public
interface|interface
name|SoapVersion
block|{
name|String
name|getBindingId
parameter_list|()
function_decl|;
name|double
name|getVersion
parameter_list|()
function_decl|;
name|String
name|getNamespace
parameter_list|()
function_decl|;
name|QName
name|getEnvelope
parameter_list|()
function_decl|;
name|QName
name|getHeader
parameter_list|()
function_decl|;
name|QName
name|getBody
parameter_list|()
function_decl|;
name|QName
name|getFault
parameter_list|()
function_decl|;
name|QName
name|getReceiver
parameter_list|()
function_decl|;
name|QName
name|getSender
parameter_list|()
function_decl|;
name|QName
name|getMustUnderstand
parameter_list|()
function_decl|;
name|QName
name|getVersionMismatch
parameter_list|()
function_decl|;
name|QName
name|getDateEncodingUnknown
parameter_list|()
function_decl|;
name|String
name|getSoapEncodingStyle
parameter_list|()
function_decl|;
name|String
name|getAttrNameMustUnderstand
parameter_list|()
function_decl|;
name|String
name|getAttrValueMustUnderstand
parameter_list|(
name|boolean
name|value
parameter_list|)
function_decl|;
comment|// Role related properties
comment|//-------------------------------------------------------------------------
name|String
name|getAttrNameRole
parameter_list|()
function_decl|;
name|String
name|getNoneRole
parameter_list|()
function_decl|;
name|String
name|getUltimateReceiverRole
parameter_list|()
function_decl|;
name|String
name|getNextRole
parameter_list|()
function_decl|;
name|String
name|getContentType
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

