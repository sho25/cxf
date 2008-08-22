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
name|xml
operator|.
name|ws
operator|.
name|RequestWrapper
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
name|ResponseWrapper
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_interface
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"uri:org.apache.cxf.javascript.fortest"
argument_list|)
specifier|public
interface|interface
name|SimpleDocLitWrapped
block|{
annotation|@
name|RequestWrapper
argument_list|(
name|className
operator|=
literal|"org.apache.cxf.javascript.fortest.BasicTypeFunctionReturnStringWrapper"
argument_list|)
annotation|@
name|ResponseWrapper
argument_list|(
name|className
operator|=
literal|"org.apache.cxf.javascript.fortest.StringWrapper"
argument_list|)
annotation|@
name|WebResult
argument_list|(
name|name
operator|=
literal|"returnValue"
argument_list|,
name|targetNamespace
operator|=
literal|"uri:org.apache.cxf.javascript.testns"
argument_list|)
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
literal|"l"
argument_list|)
name|long
name|l
parameter_list|,
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"f"
argument_list|)
name|float
name|f
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
name|String
name|basicTypeFunctionReturnStringNoWrappers
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
literal|"l"
argument_list|)
name|long
name|l
parameter_list|,
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"f"
argument_list|)
name|float
name|f
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
name|TestBean1
name|functionReturnTestBean1
parameter_list|()
function_decl|;
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
literal|"l"
argument_list|)
name|long
name|l
parameter_list|,
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"f"
argument_list|)
name|float
name|f
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
name|RequestWrapper
argument_list|(
name|className
operator|=
literal|"org.apache.cxf.javascript.fortest.BeanRequestWrapper"
argument_list|)
annotation|@
name|WebMethod
name|void
name|beanFunctionWithWrapper
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
name|void
name|genericTestFunction
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"g1"
argument_list|)
name|SpecificGenericClass
name|sgc
parameter_list|,
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"g2"
argument_list|)
name|GenericGenericClass
argument_list|<
name|Double
argument_list|>
name|ggc
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

