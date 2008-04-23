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
name|soap
operator|.
name|SOAPBody
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
name|SOAPMessage
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
name|Node
import|;
end_import

begin_comment
comment|//The following wsdl file is used.
end_comment

begin_comment
comment|//wsdlLocation = "/trunk/testutils/src/main/resources/wsdl/hello_world.wsdl"
end_comment

begin_class
annotation|@
name|WebServiceProvider
argument_list|(
name|portName
operator|=
literal|"SoapProviderPort"
argument_list|,
name|serviceName
operator|=
literal|"SOAPProviderService"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
name|wsdlLocation
operator|=
literal|"/wsdl/hello_world.wsdl"
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
name|MESSAGE
argument_list|)
specifier|public
class|class
name|HWSoapMessageDocProvider
implements|implements
name|Provider
argument_list|<
name|SOAPMessage
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
literal|"http://apache.org/hello_world_soap_http"
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
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"greetMe"
argument_list|)
decl_stmt|;
specifier|private
name|SOAPMessage
name|sayHiResponse
decl_stmt|;
specifier|private
name|SOAPMessage
name|greetMeResponse
decl_stmt|;
specifier|public
name|HWSoapMessageDocProvider
parameter_list|()
block|{
try|try
block|{
name|MessageFactory
name|factory
init|=
name|MessageFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"resources/sayHiDocLiteralResp.xml"
argument_list|)
decl_stmt|;
name|sayHiResponse
operator|=
name|factory
operator|.
name|createMessage
argument_list|(
literal|null
argument_list|,
name|is
argument_list|)
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"resources/GreetMeDocLiteralResp.xml"
argument_list|)
expr_stmt|;
name|greetMeResponse
operator|=
name|factory
operator|.
name|createMessage
argument_list|(
literal|null
argument_list|,
name|is
argument_list|)
expr_stmt|;
name|is
operator|.
name|close
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
block|}
block|}
specifier|public
name|SOAPMessage
name|invoke
parameter_list|(
name|SOAPMessage
name|request
parameter_list|)
block|{
name|SOAPMessage
name|response
init|=
literal|null
decl_stmt|;
try|try
block|{
name|SOAPBody
name|body
init|=
name|request
operator|.
name|getSOAPBody
argument_list|()
decl_stmt|;
name|Node
name|n
init|=
name|body
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
operator|=
name|sayHiResponse
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
operator|=
name|greetMeResponse
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
block|}
end_class

end_unit

