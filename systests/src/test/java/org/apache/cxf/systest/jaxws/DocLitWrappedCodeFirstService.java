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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Vector
import|;
end_import

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
name|XmlType
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
name|Holder
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
name|FastInfoset
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
name|GZIP
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
name|WSDLDocumentation
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
name|WSDLDocumentationCollection
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
name|message
operator|.
name|Exchange
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
name|systest
operator|.
name|jaxws
operator|.
name|types
operator|.
name|Bar
import|;
end_import

begin_interface
annotation|@
name|WebService
argument_list|(
name|name
operator|=
literal|"DocLitWrappedCodeFirstService"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/systest/jaxws/DocLitWrappedCodeFirstService"
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
argument_list|)
annotation|@
name|WSDLDocumentationCollection
argument_list|(
block|{
annotation|@
name|WSDLDocumentation
argument_list|(
literal|"DocLitWrappedCodeFirstService interface"
argument_list|)
block|,
annotation|@
name|WSDLDocumentation
argument_list|(
name|value
operator|=
literal|"DocLitWrappedCodeFirstService top level doc"
argument_list|,
name|placement
operator|=
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|TOP
argument_list|)
block|,
annotation|@
name|WSDLDocumentation
argument_list|(
name|value
operator|=
literal|"DocLitWrappedCodeFirstService binding doc"
argument_list|,
name|placement
operator|=
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|BINDING
argument_list|)
block|,
annotation|@
name|WSDLDocumentation
argument_list|(
name|value
operator|=
literal|"DocLitWrappedCodeFirstService service/port doc"
argument_list|,
name|placement
operator|=
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|SERVICE_PORT
argument_list|)
block|}
argument_list|)
annotation|@
name|GZIP
annotation|@
name|FastInfoset
specifier|public
interface|interface
name|DocLitWrappedCodeFirstService
block|{
annotation|@
name|Oneway
annotation|@
name|WebMethod
name|void
name|doOneWay
parameter_list|()
function_decl|;
annotation|@
name|WebMethod
name|String
index|[]
name|arrayOutput
parameter_list|()
function_decl|;
annotation|@
name|WebMethod
name|String
name|arrayInput
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"input"
argument_list|)
name|String
index|[]
name|inputs
parameter_list|)
function_decl|;
annotation|@
name|WebMethod
name|Vector
argument_list|<
name|String
argument_list|>
name|listOutput
parameter_list|()
function_decl|;
annotation|@
name|WebMethod
name|String
name|echoStringNotReallyAsync
parameter_list|(
name|String
name|s
parameter_list|)
function_decl|;
annotation|@
name|WebMethod
name|int
index|[]
name|echoIntArray
parameter_list|(
name|int
index|[]
name|ar
parameter_list|,
name|Exchange
name|ex
parameter_list|)
function_decl|;
annotation|@
name|WebMethod
annotation|@
name|WebResult
argument_list|(
name|partName
operator|=
literal|"parameters"
argument_list|)
name|String
name|listInput
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|inputs
parameter_list|)
function_decl|;
annotation|@
name|WebMethod
name|String
name|multiListInput
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|inputs1
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|inputs2
parameter_list|,
name|String
name|x
parameter_list|,
name|int
name|y
parameter_list|)
function_decl|;
annotation|@
name|WebMethod
annotation|@
name|WSDLDocumentationCollection
argument_list|(
block|{
annotation|@
name|WSDLDocumentation
argument_list|(
literal|"multiInOut doc"
argument_list|)
block|,
annotation|@
name|WSDLDocumentation
argument_list|(
name|value
operator|=
literal|"multiInOut Input doc"
argument_list|,
name|placement
operator|=
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|PORT_TYPE_OPERATION_INPUT
argument_list|)
block|,
annotation|@
name|WSDLDocumentation
argument_list|(
name|value
operator|=
literal|"multiInOut Output doc"
argument_list|,
name|placement
operator|=
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|PORT_TYPE_OPERATION_OUTPUT
argument_list|)
block|,
annotation|@
name|WSDLDocumentation
argument_list|(
name|value
operator|=
literal|"multiInOut InputMessage doc"
argument_list|,
name|placement
operator|=
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|INPUT_MESSAGE
argument_list|)
block|,
annotation|@
name|WSDLDocumentation
argument_list|(
name|value
operator|=
literal|"multiInOut OutputMessage doc"
argument_list|,
name|placement
operator|=
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|OUTPUT_MESSAGE
argument_list|)
block|,
annotation|@
name|WSDLDocumentation
argument_list|(
name|value
operator|=
literal|"multiInOut binding doc"
argument_list|,
name|placement
operator|=
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|BINDING_OPERATION
argument_list|)
block|,
annotation|@
name|WSDLDocumentation
argument_list|(
name|value
operator|=
literal|"multiInOut binding Input doc"
argument_list|,
name|placement
operator|=
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|BINDING_OPERATION_INPUT
argument_list|)
block|,
annotation|@
name|WSDLDocumentation
argument_list|(
name|value
operator|=
literal|"multiInOut binding Output doc"
argument_list|,
name|placement
operator|=
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|BINDING_OPERATION_OUTPUT
argument_list|)
block|}
argument_list|)
name|String
name|multiInOut
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|mode
operator|=
name|WebParam
operator|.
name|Mode
operator|.
name|OUT
argument_list|)
name|Holder
argument_list|<
name|String
argument_list|>
name|a
parameter_list|,
annotation|@
name|WebParam
argument_list|(
name|mode
operator|=
name|WebParam
operator|.
name|Mode
operator|.
name|INOUT
argument_list|)
name|Holder
argument_list|<
name|String
argument_list|>
name|b
parameter_list|,
annotation|@
name|WebParam
argument_list|(
name|mode
operator|=
name|WebParam
operator|.
name|Mode
operator|.
name|OUT
argument_list|)
name|Holder
argument_list|<
name|String
argument_list|>
name|c
parameter_list|,
annotation|@
name|WebParam
argument_list|(
name|mode
operator|=
name|WebParam
operator|.
name|Mode
operator|.
name|INOUT
argument_list|)
name|Holder
argument_list|<
name|String
argument_list|>
name|d
parameter_list|,
annotation|@
name|WebParam
argument_list|(
name|mode
operator|=
name|WebParam
operator|.
name|Mode
operator|.
name|INOUT
argument_list|)
name|Holder
argument_list|<
name|String
argument_list|>
name|e
parameter_list|,
annotation|@
name|WebParam
argument_list|(
name|mode
operator|=
name|WebParam
operator|.
name|Mode
operator|.
name|OUT
argument_list|)
name|Holder
argument_list|<
name|String
argument_list|>
name|f
parameter_list|,
annotation|@
name|WebParam
argument_list|(
name|mode
operator|=
name|WebParam
operator|.
name|Mode
operator|.
name|OUT
argument_list|)
name|Holder
argument_list|<
name|String
argument_list|>
name|g
parameter_list|)
function_decl|;
annotation|@
name|WebMethod
name|List
argument_list|<
name|Foo
argument_list|>
name|listObjectOutput
parameter_list|()
function_decl|;
annotation|@
name|WebMethod
name|boolean
name|listObjectIn
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|mode
operator|=
name|WebParam
operator|.
name|Mode
operator|.
name|INOUT
argument_list|)
name|Holder
argument_list|<
name|List
argument_list|<
name|Foo
index|[]
argument_list|>
argument_list|>
name|foos
parameter_list|)
function_decl|;
annotation|@
name|WebMethod
name|List
argument_list|<
name|Foo
index|[]
argument_list|>
name|listObjectArrayOutput
parameter_list|()
function_decl|;
name|String
name|outOnly
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|mode
operator|=
name|WebParam
operator|.
name|Mode
operator|.
name|OUT
argument_list|)
name|Holder
argument_list|<
name|String
argument_list|>
name|out1
parameter_list|,
annotation|@
name|WebParam
argument_list|(
name|mode
operator|=
name|WebParam
operator|.
name|Mode
operator|.
name|OUT
argument_list|)
name|Holder
argument_list|<
name|String
argument_list|>
name|out2
parameter_list|)
function_decl|;
annotation|@
name|WebMethod
annotation|@
name|WSDLDocumentationCollection
argument_list|(
block|{
annotation|@
name|WSDLDocumentation
argument_list|(
name|value
operator|=
literal|"fault message doc"
argument_list|,
name|placement
operator|=
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|FAULT_MESSAGE
argument_list|,
name|faultClass
operator|=
name|CustomException
operator|.
name|class
argument_list|)
block|,
annotation|@
name|WSDLDocumentation
argument_list|(
name|value
operator|=
literal|"fault binding doc"
argument_list|,
name|placement
operator|=
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|BINDING_OPERATION_FAULT
argument_list|,
name|faultClass
operator|=
name|CustomException
operator|.
name|class
argument_list|)
block|,
annotation|@
name|WSDLDocumentation
argument_list|(
name|value
operator|=
literal|"fault porttype doc"
argument_list|,
name|placement
operator|=
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|PORT_TYPE_OPERATION_FAULT
argument_list|,
name|faultClass
operator|=
name|CustomException
operator|.
name|class
argument_list|)
block|}
argument_list|)
name|int
name|throwException
parameter_list|(
name|int
name|i
parameter_list|)
throws|throws
name|ServiceTestFault
throws|,
name|CustomException
throws|,
name|ComplexException
function_decl|;
annotation|@
name|RequestWrapper
argument_list|(
name|localName
operator|=
literal|"echoIntX"
argument_list|)
annotation|@
name|ResponseWrapper
argument_list|(
name|localName
operator|=
literal|"echoIntXResponse"
argument_list|)
name|int
name|echoIntDifferentWrapperName
parameter_list|(
name|int
name|i
parameter_list|)
function_decl|;
annotation|@
name|WebMethod
annotation|@
name|WebResult
argument_list|(
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/systest/jaxws/DocLitWrappedCodeFirstService"
argument_list|,
name|name
operator|=
literal|"result"
argument_list|)
annotation|@
name|RequestWrapper
argument_list|(
name|className
operator|=
literal|"org.apache.cxf.systest.jaxws.Echo"
argument_list|)
annotation|@
name|ResponseWrapper
argument_list|(
name|className
operator|=
literal|"org.apache.cxf.systest.jaxws.EchoResponse"
argument_list|)
name|String
name|echo
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/systest/jaxws/DocLitWrappedCodeFirstService2"
argument_list|,
name|name
operator|=
literal|"String_1"
argument_list|)
name|String
name|msg
parameter_list|)
function_decl|;
name|Bar
name|createBar
parameter_list|(
name|String
name|val
parameter_list|)
function_decl|;
specifier|static
class|class
name|Foo
block|{
name|String
name|name
decl_stmt|;
specifier|public
name|Foo
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
name|Set
argument_list|<
name|Foo
argument_list|>
name|getFooSet
parameter_list|()
function_decl|;
annotation|@
name|RequestWrapper
argument_list|(
name|className
operator|=
literal|"org.apache.cxf.systest.jaxws.DocLitWrappedCodeFirstService$DoFooListRequest"
argument_list|)
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
literal|"doFooList"
argument_list|)
name|String
name|doFooList
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"dbRef"
argument_list|)
name|List
argument_list|<
name|Foo
argument_list|>
name|fooList
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
literal|"doFooList"
argument_list|,
name|propOrder
operator|=
block|{
literal|"dbReves"
block|}
argument_list|)
specifier|public
specifier|static
class|class
name|DoFooListRequest
block|{
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"dbRef"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
specifier|protected
name|List
argument_list|<
name|Foo
argument_list|>
name|dbReves
init|=
operator|new
name|ArrayList
argument_list|<
name|Foo
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|List
argument_list|<
name|Foo
argument_list|>
name|getDbReves
parameter_list|()
block|{
return|return
name|dbReves
return|;
block|}
block|}
block|}
end_interface

end_unit

