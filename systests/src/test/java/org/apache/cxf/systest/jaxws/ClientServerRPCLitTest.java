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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|UndeclaredThrowableException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|HttpURLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|soap
operator|.
name|SOAPBinding
operator|.
name|Style
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
operator|.
name|Use
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
name|XmlType
import|;
end_import

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

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|Dispatch
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
name|Endpoint
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
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
name|binding
operator|.
name|soap
operator|.
name|Soap11
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
name|helpers
operator|.
name|XMLUtils
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
name|helpers
operator|.
name|XPathUtils
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
name|staxutils
operator|.
name|W3CNamespaceContext
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusClientServerTestBase
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusTestServerBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_rpclit
operator|.
name|GreeterRPCLit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_rpclit
operator|.
name|SOAPServiceRPCLit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_rpclit
operator|.
name|types
operator|.
name|MyComplexStruct
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|RPCLitGreeterImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|ClientServerRPCLitTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|private
specifier|final
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"SoapPortRPCLit"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
class|class
name|Server
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|String
name|address
decl_stmt|;
name|Object
name|implementor
init|=
operator|new
name|RPCLitGreeterImpl
argument_list|()
decl_stmt|;
name|address
operator|=
literal|"http://localhost:9002/SOAPServiceRPCLit/SoapPort"
expr_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
name|address
operator|=
literal|"http://localhost:9002/TestRPCWsdl"
expr_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
operator|new
name|MyService
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
try|try
block|{
name|Server
name|s
init|=
operator|new
name|Server
argument_list|()
decl_stmt|;
name|s
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"done!"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startServers
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|Server
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBasicConnection
parameter_list|()
throws|throws
name|Exception
block|{
name|SOAPServiceRPCLit
name|service
init|=
operator|new
name|SOAPServiceRPCLit
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|String
name|response1
init|=
operator|new
name|String
argument_list|(
literal|"Hello Milestone-"
argument_list|)
decl_stmt|;
name|String
name|response2
init|=
operator|new
name|String
argument_list|(
literal|"Bonjour"
argument_list|)
decl_stmt|;
try|try
block|{
name|GreeterRPCLit
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|GreeterRPCLit
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|idx
init|=
literal|0
init|;
name|idx
operator|<
literal|1
condition|;
name|idx
operator|++
control|)
block|{
name|String
name|greeting
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"Milestone-"
operator|+
name|idx
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|String
name|exResponse
init|=
name|response1
operator|+
name|idx
decl_stmt|;
name|assertEquals
argument_list|(
name|exResponse
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|String
name|reply
init|=
name|greeter
operator|.
name|sayHi
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|response2
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|UndeclaredThrowableException
name|ex
parameter_list|)
block|{
throw|throw
operator|(
name|Exception
operator|)
name|ex
operator|.
name|getCause
argument_list|()
throw|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDispatchClient
parameter_list|()
throws|throws
name|Exception
block|{
name|SOAPServiceRPCLit
name|service
init|=
operator|new
name|SOAPServiceRPCLit
argument_list|()
decl_stmt|;
name|Dispatch
argument_list|<
name|Source
argument_list|>
name|disp
init|=
name|service
operator|.
name|createDispatch
argument_list|(
name|portName
argument_list|,
name|Source
operator|.
name|class
argument_list|,
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
argument_list|)
decl_stmt|;
name|String
name|req
init|=
literal|"<ns1:sendReceiveData xmlns:ns1=\"http://apache.org/hello_world_rpclit\">"
operator|+
literal|"<in xmlns:ns2=\"http://apache.org/hello_world_rpclit/types\">"
operator|+
literal|"<ns2:elem1>elem1</ns2:elem1><ns2:elem2>elem2</ns2:elem2><ns2:elem3>45</ns2:elem3>"
operator|+
literal|"</in></ns1:sendReceiveData>"
decl_stmt|;
name|Source
name|source
init|=
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|req
argument_list|)
argument_list|)
decl_stmt|;
name|Source
name|resp
init|=
name|disp
operator|.
name|invoke
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|resp
argument_list|)
expr_stmt|;
name|Node
name|nd
init|=
name|XMLUtils
operator|.
name|fromSource
argument_list|(
name|resp
argument_list|)
decl_stmt|;
if|if
condition|(
name|nd
operator|instanceof
name|Document
condition|)
block|{
name|nd
operator|=
operator|(
operator|(
name|Document
operator|)
name|nd
operator|)
operator|.
name|getDocumentElement
argument_list|()
expr_stmt|;
block|}
name|XPathUtils
name|xpu
init|=
operator|new
name|XPathUtils
argument_list|(
operator|new
name|W3CNamespaceContext
argument_list|(
operator|(
name|Element
operator|)
name|nd
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|xpu
operator|.
name|isExist
argument_list|(
literal|"/ns1:sendReceiveDataResponse/out"
argument_list|,
name|nd
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testComplexType
parameter_list|()
throws|throws
name|Exception
block|{
name|SOAPServiceRPCLit
name|service
init|=
operator|new
name|SOAPServiceRPCLit
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|GreeterRPCLit
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|GreeterRPCLit
operator|.
name|class
argument_list|)
decl_stmt|;
name|MyComplexStruct
name|in
init|=
operator|new
name|MyComplexStruct
argument_list|()
decl_stmt|;
name|in
operator|.
name|setElem1
argument_list|(
literal|"elem1"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setElem2
argument_list|(
literal|"elem2"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setElem3
argument_list|(
literal|45
argument_list|)
expr_stmt|;
try|try
block|{
name|MyComplexStruct
name|out
init|=
name|greeter
operator|.
name|sendReceiveData
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|in
operator|.
name|getElem1
argument_list|()
argument_list|,
name|out
operator|.
name|getElem1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|in
operator|.
name|getElem2
argument_list|()
argument_list|,
name|out
operator|.
name|getElem2
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|in
operator|.
name|getElem3
argument_list|()
argument_list|,
name|out
operator|.
name|getElem3
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UndeclaredThrowableException
name|ex
parameter_list|)
block|{
throw|throw
operator|(
name|Exception
operator|)
name|ex
operator|.
name|getCause
argument_list|()
throw|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoElementParts
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpURLConnection
name|httpConnection
init|=
name|getHttpConnection
argument_list|(
literal|"http://localhost:9002/TestRPCWsdl?wsdl"
argument_list|)
decl_stmt|;
name|httpConnection
operator|.
name|connect
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|httpConnection
operator|.
name|getResponseCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"OK"
argument_list|,
name|httpConnection
operator|.
name|getResponseMessage
argument_list|()
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
name|httpConnection
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|XMLUtils
operator|.
name|parse
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ns
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|ns
operator|.
name|put
argument_list|(
literal|"soap"
argument_list|,
name|Soap11
operator|.
name|SOAP_NAMESPACE
argument_list|)
expr_stmt|;
name|ns
operator|.
name|put
argument_list|(
literal|"wsdl"
argument_list|,
literal|"http://schemas.xmlsoap.org/wsdl/"
argument_list|)
expr_stmt|;
name|ns
operator|.
name|put
argument_list|(
literal|"xs"
argument_list|,
literal|"http://www.w3.org/2001/XMLSchema"
argument_list|)
expr_stmt|;
name|XPathUtils
name|xu
init|=
operator|new
name|XPathUtils
argument_list|(
name|ns
argument_list|)
decl_stmt|;
comment|//make sure the wrapper types are anonymous types
name|NodeList
name|ct
init|=
operator|(
name|NodeList
operator|)
name|xu
operator|.
name|getValue
argument_list|(
literal|"//wsdl:definitions/wsdl:message/wsdl:part[@element != '']"
argument_list|,
name|doc
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ct
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ct
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|ct
operator|=
operator|(
name|NodeList
operator|)
name|xu
operator|.
name|getValue
argument_list|(
literal|"//wsdl:definitions/wsdl:message/wsdl:part[@type != '']"
argument_list|,
name|doc
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|ct
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"MyObjectService"
argument_list|,
name|portName
operator|=
literal|"MyObjectServicePort"
argument_list|)
annotation|@
name|SOAPBinding
argument_list|(
name|use
operator|=
name|Use
operator|.
name|LITERAL
argument_list|,
name|style
operator|=
name|Style
operator|.
name|RPC
argument_list|)
specifier|public
specifier|static
class|class
name|MyService
block|{
annotation|@
name|WebMethod
specifier|public
name|MyObject
name|getMyObject
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"longField1"
argument_list|)
name|long
name|longField1
parameter_list|)
block|{
return|return
name|generateMyObject
argument_list|()
return|;
block|}
annotation|@
name|WebMethod
specifier|public
name|int
name|updateMyObject
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"myObject"
argument_list|)
name|MyObject
name|myObject
parameter_list|)
block|{
return|return
literal|3
return|;
block|}
specifier|private
specifier|static
name|MyObject
name|generateMyObject
parameter_list|()
block|{
name|MyObject
name|myObject
init|=
operator|new
name|MyObject
argument_list|()
decl_stmt|;
name|long
name|tempLong
init|=
literal|1
decl_stmt|;
name|myObject
operator|.
name|setStringField1
argument_list|(
literal|"S:"
operator|+
name|tempLong
operator|++
argument_list|)
expr_stmt|;
name|myObject
operator|.
name|setStringField2
argument_list|(
literal|"S:"
operator|+
name|tempLong
operator|++
argument_list|)
expr_stmt|;
name|myObject
operator|.
name|setLongField1
argument_list|(
name|tempLong
operator|++
argument_list|)
expr_stmt|;
name|myObject
operator|.
name|setLongField2
argument_list|(
name|tempLong
operator|++
argument_list|)
expr_stmt|;
return|return
name|myObject
return|;
block|}
block|}
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"MyObject"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|PROPERTY
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"MyObject"
argument_list|)
specifier|public
specifier|static
class|class
name|MyObject
block|{
specifier|private
name|String
name|stringField1
decl_stmt|;
specifier|private
name|String
name|stringField2
decl_stmt|;
specifier|private
name|long
name|longField1
decl_stmt|;
specifier|private
name|long
name|longField2
decl_stmt|;
specifier|public
name|String
name|getStringField1
parameter_list|()
block|{
return|return
name|stringField1
return|;
block|}
specifier|public
name|void
name|setStringField1
parameter_list|(
name|String
name|stringField1
parameter_list|)
block|{
name|this
operator|.
name|stringField1
operator|=
name|stringField1
expr_stmt|;
block|}
specifier|public
name|String
name|getStringField2
parameter_list|()
block|{
return|return
name|stringField2
return|;
block|}
specifier|public
name|void
name|setStringField2
parameter_list|(
name|String
name|stringField2
parameter_list|)
block|{
name|this
operator|.
name|stringField2
operator|=
name|stringField2
expr_stmt|;
block|}
specifier|public
name|long
name|getLongField1
parameter_list|()
block|{
return|return
name|longField1
return|;
block|}
specifier|public
name|void
name|setLongField1
parameter_list|(
name|long
name|longField1
parameter_list|)
block|{
name|this
operator|.
name|longField1
operator|=
name|longField1
expr_stmt|;
block|}
specifier|public
name|long
name|getLongField2
parameter_list|()
block|{
return|return
name|longField2
return|;
block|}
specifier|public
name|void
name|setLongField2
parameter_list|(
name|long
name|longField2
parameter_list|)
block|{
name|this
operator|.
name|longField2
operator|=
name|longField2
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

