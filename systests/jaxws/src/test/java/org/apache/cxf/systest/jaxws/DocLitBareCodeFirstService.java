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
name|jaxws
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
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlList
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlSchemaType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|datatype
operator|.
name|XMLGregorianCalendar
import|;
end_import

begin_interface
annotation|@
name|WebService
argument_list|(
name|name
operator|=
literal|"DocLitBareCodeFirstService"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/systest/jaxws/DocLitBareCodeFirstService"
argument_list|)
annotation|@
name|SOAPBinding
argument_list|(
name|style
operator|=
name|SOAPBinding
operator|.
name|Style
operator|.
name|DOCUMENT
argument_list|,
name|use
operator|=
name|SOAPBinding
operator|.
name|Use
operator|.
name|LITERAL
argument_list|,
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
name|DocLitBareCodeFirstService
block|{
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
literal|"greetMe"
argument_list|)
annotation|@
name|WebResult
argument_list|(
name|targetNamespace
operator|=
literal|"http://namespace/result"
argument_list|,
name|name
operator|=
literal|"GreetMeBareResponse"
argument_list|)
name|GreetMeResponse
name|greetMe
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|targetNamespace
operator|=
literal|"http://namespace/request"
argument_list|)
name|GreetMeRequest
name|gmr
parameter_list|)
function_decl|;
annotation|@
name|XmlList
annotation|@
name|WebResult
argument_list|(
name|name
operator|=
literal|"Items"
argument_list|,
name|targetNamespace
operator|=
literal|"http://namespace/result"
argument_list|,
name|partName
operator|=
literal|"parameter"
argument_list|)
annotation|@
name|WebMethod
name|java
operator|.
name|math
operator|.
name|BigInteger
index|[]
name|sayTest
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|partName
operator|=
literal|"parameter"
argument_list|,
name|name
operator|=
literal|"SayTestRequest"
argument_list|,
name|targetNamespace
operator|=
literal|"http://www.tum.de/test"
argument_list|)
name|SayTestRequest
name|parameter
parameter_list|)
function_decl|;
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"SayTestRequest"
argument_list|,
name|namespace
operator|=
literal|"http://cxf.apache.org/test/request/bare"
argument_list|,
name|propOrder
operator|=
block|{
literal|"name"
block|}
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|namespace
operator|=
literal|"http://cxf.apache.org/test/request/bare"
argument_list|,
name|name
operator|=
literal|"SayTestObject"
argument_list|)
class|class
name|SayTestRequest
block|{
name|String
name|name
decl_stmt|;
specifier|public
name|SayTestRequest
parameter_list|()
block|{         }
specifier|public
name|SayTestRequest
parameter_list|(
name|String
name|n
parameter_list|)
block|{
name|name
operator|=
name|n
expr_stmt|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|n
parameter_list|)
block|{
name|name
operator|=
name|n
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
block|}
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"GreetMeRequest"
argument_list|,
name|namespace
operator|=
literal|"http://cxf.apache.org/test/request/bare"
argument_list|,
name|propOrder
operator|=
block|{
literal|"name"
block|}
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|namespace
operator|=
literal|"http://cxf.apache.org/test/request/bare"
argument_list|,
name|name
operator|=
literal|"GreetMeObject"
argument_list|)
class|class
name|GreetMeRequest
block|{
name|String
name|name
decl_stmt|;
specifier|public
name|GreetMeRequest
parameter_list|()
block|{         }
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|n
parameter_list|)
block|{
name|name
operator|=
name|n
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
block|}
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"GreetMeResponse"
argument_list|,
name|namespace
operator|=
literal|"http://cxf.apache.org/test/request/bare"
argument_list|,
name|propOrder
operator|=
block|{
literal|"name"
block|}
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|namespace
operator|=
literal|"http://cxf.apache.org/test/request/bare"
argument_list|,
name|name
operator|=
literal|"GreetMeResponseObject"
argument_list|)
class|class
name|GreetMeResponse
block|{
name|String
name|name
decl_stmt|;
specifier|public
name|GreetMeResponse
parameter_list|()
block|{         }
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|n
parameter_list|)
block|{
name|name
operator|=
name|n
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
block|}
annotation|@
name|WebResult
argument_list|(
name|name
operator|=
literal|"GMonthResult"
argument_list|,
name|targetNamespace
operator|=
literal|"http://namespace/result"
argument_list|,
name|partName
operator|=
literal|"parameter"
argument_list|)
annotation|@
name|WebMethod
name|GMonthTest
name|echoGMonthTest
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"GMonthRequest"
argument_list|,
name|targetNamespace
operator|=
literal|"http://namespace/result"
argument_list|,
name|partName
operator|=
literal|"parameter"
argument_list|)
name|GMonthTest
name|input
parameter_list|)
function_decl|;
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"GMonthTest"
argument_list|,
name|propOrder
operator|=
block|{
literal|"value"
block|}
argument_list|)
specifier|public
class|class
name|GMonthTest
block|{
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|,
name|nillable
operator|=
literal|true
argument_list|)
annotation|@
name|XmlSchemaType
argument_list|(
name|name
operator|=
literal|"gMonth"
argument_list|)
specifier|protected
name|XMLGregorianCalendar
name|value
decl_stmt|;
specifier|public
name|XMLGregorianCalendar
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
specifier|public
name|void
name|setValue
parameter_list|(
name|XMLGregorianCalendar
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
block|}
block|}
end_interface

end_unit

