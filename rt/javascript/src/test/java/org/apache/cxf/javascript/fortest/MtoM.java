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
name|javascript
operator|.
name|fortest
package|;
end_package

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

begin_comment
comment|/**  *   */
end_comment

begin_interface
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
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"uri:org.apache.cxf.javascript.fortest"
argument_list|)
specifier|public
interface|interface
name|MtoM
block|{
name|void
name|receiveNonXmlDH
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"theArg"
argument_list|)
name|MtoMParameterBeanWithDataHandler
name|param
parameter_list|)
function_decl|;
name|void
name|receiveNonXmlNoDH
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"theArgNoDH"
argument_list|)
name|MtoMParameterBeanNoDataHandler
name|param
parameter_list|)
function_decl|;
name|MtoMParameterBeanWithDataHandler
name|sendNonXmlDH
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

