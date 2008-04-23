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
name|provider
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|soap
operator|.
name|MessageFactory
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
name|Transformer
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
name|TransformerFactory
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
name|dom
operator|.
name|DOMResult
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
name|sax
operator|.
name|SAXSource
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
name|Provider
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
name|Service
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
name|ServiceMode
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
name|WebServiceProvider
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
name|DOMImplementation
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
name|bootstrap
operator|.
name|DOMImplementationRegistry
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
name|ls
operator|.
name|DOMImplementationLS
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
name|ls
operator|.
name|LSOutput
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
name|ls
operator|.
name|LSSerializer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
import|;
end_import

begin_comment
comment|//The following wsdl file is used.
end_comment

begin_comment
comment|//wsdlLocation = "/trunk/testutils/src/main/resources/wsdl/hello_world_rpc_lit.wsdl"
end_comment

begin_class
annotation|@
name|WebServiceProvider
argument_list|(
name|portName
operator|=
literal|"SoapPortProviderRPCLit6"
argument_list|,
name|serviceName
operator|=
literal|"SOAPServiceProviderRPCLit"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
name|wsdlLocation
operator|=
literal|"/wsdl/hello_world_rpc_lit.wsdl"
argument_list|)
annotation|@
name|ServiceMode
argument_list|(
name|value
operator|=
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
argument_list|)
specifier|public
class|class
name|HWSAXSourcePayloadProvider
implements|implements
name|Provider
argument_list|<
name|SAXSource
argument_list|>
block|{
specifier|private
specifier|static
name|QName
name|sayHi
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"sayHi"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|QName
name|greetMe
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"greetMe"
argument_list|)
decl_stmt|;
specifier|private
name|MessageFactory
name|factory
decl_stmt|;
specifier|private
name|InputSource
name|sayHiInputSource
decl_stmt|;
specifier|private
name|InputSource
name|greetMeInputSource
decl_stmt|;
specifier|public
name|HWSAXSourcePayloadProvider
parameter_list|()
block|{
try|try
block|{
name|factory
operator|=
name|MessageFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|InputStream
name|is
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"resources/sayHiRpcLiteralResp.xml"
argument_list|)
decl_stmt|;
name|Document
name|sayHiDocument
init|=
name|factory
operator|.
name|createMessage
argument_list|(
literal|null
argument_list|,
name|is
argument_list|)
operator|.
name|getSOAPBody
argument_list|()
operator|.
name|extractContentAsDocument
argument_list|()
decl_stmt|;
name|sayHiInputSource
operator|=
operator|new
name|InputSource
argument_list|(
name|getSOAPBodyStream
argument_list|(
name|sayHiDocument
argument_list|)
argument_list|)
expr_stmt|;
name|InputStream
name|is2
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"resources/GreetMeRpcLiteralResp.xml"
argument_list|)
decl_stmt|;
name|Document
name|greetMeDocument
init|=
name|factory
operator|.
name|createMessage
argument_list|(
literal|null
argument_list|,
name|is2
argument_list|)
operator|.
name|getSOAPBody
argument_list|()
operator|.
name|extractContentAsDocument
argument_list|()
decl_stmt|;
name|greetMeInputSource
operator|=
operator|new
name|InputSource
argument_list|(
name|getSOAPBodyStream
argument_list|(
name|greetMeDocument
argument_list|)
argument_list|)
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
block|}
block|}
specifier|public
name|SAXSource
name|invoke
parameter_list|(
name|SAXSource
name|request
parameter_list|)
block|{
name|SAXSource
name|response
init|=
operator|new
name|SAXSource
argument_list|()
decl_stmt|;
try|try
block|{
name|DOMResult
name|domResult
init|=
operator|new
name|DOMResult
argument_list|()
decl_stmt|;
name|Transformer
name|transformer
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
name|transformer
operator|.
name|transform
argument_list|(
name|request
argument_list|,
name|domResult
argument_list|)
expr_stmt|;
name|Node
name|n
init|=
name|domResult
operator|.
name|getNode
argument_list|()
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
while|while
condition|(
name|n
operator|.
name|getNodeType
argument_list|()
operator|!=
name|Node
operator|.
name|ELEMENT_NODE
condition|)
block|{
name|n
operator|=
name|n
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|n
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
name|sayHi
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
name|response
operator|.
name|setInputSource
argument_list|(
name|sayHiInputSource
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|n
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
name|greetMe
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
name|response
operator|.
name|setInputSource
argument_list|(
name|greetMeInputSource
argument_list|)
expr_stmt|;
block|}
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
block|}
return|return
name|response
return|;
block|}
specifier|private
name|InputStream
name|getSOAPBodyStream
parameter_list|(
name|Document
name|doc
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Try to get the DOMImplementation from the doc before
comment|// defaulting to the sun implementation class (which uses
comment|// sun internal xerces classes not found in the ibm jdk).
name|DOMImplementationLS
name|impl
init|=
literal|null
decl_stmt|;
name|DOMImplementation
name|docImpl
init|=
name|doc
operator|.
name|getImplementation
argument_list|()
decl_stmt|;
if|if
condition|(
name|docImpl
operator|!=
literal|null
operator|&&
name|docImpl
operator|.
name|hasFeature
argument_list|(
literal|"LS"
argument_list|,
literal|"3.0"
argument_list|)
condition|)
block|{
name|impl
operator|=
operator|(
name|DOMImplementationLS
operator|)
name|docImpl
operator|.
name|getFeature
argument_list|(
literal|"LS"
argument_list|,
literal|"3.0"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|DOMImplementationRegistry
operator|.
name|PROPERTY
argument_list|,
literal|"com.sun.org.apache.xerces.internal.dom.DOMImplementationSourceImpl"
argument_list|)
expr_stmt|;
name|DOMImplementationRegistry
name|registry
init|=
name|DOMImplementationRegistry
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|impl
operator|=
operator|(
name|DOMImplementationLS
operator|)
name|registry
operator|.
name|getDOMImplementation
argument_list|(
literal|"LS"
argument_list|)
expr_stmt|;
block|}
name|LSOutput
name|output
init|=
name|impl
operator|.
name|createLSOutput
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|byteArrayOutputStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|output
operator|.
name|setByteStream
argument_list|(
name|byteArrayOutputStream
argument_list|)
expr_stmt|;
name|LSSerializer
name|writer
init|=
name|impl
operator|.
name|createLSSerializer
argument_list|()
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|doc
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|byte
index|[]
name|buf
init|=
name|byteArrayOutputStream
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|buf
argument_list|)
return|;
block|}
block|}
end_class

end_unit

