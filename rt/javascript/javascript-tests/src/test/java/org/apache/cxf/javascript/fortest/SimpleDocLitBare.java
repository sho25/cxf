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
name|Oneway
import|;
end_import

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
comment|/**  * Most of these violate WS-I(!)  */
end_comment

begin_interface
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"uri:org.apache.cxf.javascript.fortest"
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
name|SimpleDocLitBare
block|{
annotation|@
name|WebMethod
name|String
name|basicTypeFunctionReturnString
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"s"
argument_list|)
name|String
name|s
parameter_list|,
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"i"
argument_list|)
name|int
name|i
parameter_list|,
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"d"
argument_list|)
name|double
name|d
parameter_list|)
function_decl|;
comment|/*     @WebMethod     TestBean1 functionReturnTestBean1();     */
annotation|@
name|WebMethod
name|int
name|basicTypeFunctionReturnInt
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"s"
argument_list|)
name|String
name|s
parameter_list|,
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"d"
argument_list|)
name|double
name|d
parameter_list|)
function_decl|;
annotation|@
name|WebMethod
name|void
name|beanFunction
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"bean1"
argument_list|)
name|TestBean1
name|bean
parameter_list|,
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"beanArray"
argument_list|)
name|TestBean1
index|[]
name|beans
parameter_list|)
function_decl|;
annotation|@
name|WebMethod
name|String
name|compliant
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"beanParam"
argument_list|)
name|TestBean1
name|green
parameter_list|)
function_decl|;
annotation|@
name|WebMethod
argument_list|(
name|action
operator|=
literal|"lightsCamera"
argument_list|)
name|String
name|actionMethod
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"stringParam"
argument_list|)
name|String
name|param
parameter_list|)
function_decl|;
annotation|@
name|WebMethod
argument_list|(
name|action
operator|=
literal|"compliantNoArgs"
argument_list|)
name|TestBean2
name|compliantNoArgs
parameter_list|()
function_decl|;
annotation|@
name|Oneway
annotation|@
name|WebMethod
name|void
name|oneWay
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"corrigan"
argument_list|)
name|String
name|param
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

