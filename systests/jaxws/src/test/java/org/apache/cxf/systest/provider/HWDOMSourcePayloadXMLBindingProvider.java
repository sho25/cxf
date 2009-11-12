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
name|annotation
operator|.
name|Resource
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
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
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
name|DOMSource
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
name|WebServiceContext
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|MessageContext
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
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|DOMUtils
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
literal|"XMLProviderPort"
argument_list|,
name|serviceName
operator|=
literal|"XMLService"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/hello_world_xml_http/wrapped"
argument_list|,
name|wsdlLocation
operator|=
literal|"/wsdl/hello_world_xml_wrapped.wsdl"
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
annotation|@
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingType
argument_list|(
name|value
operator|=
literal|"http://cxf.apache.org/bindings/xformat"
argument_list|)
specifier|public
class|class
name|HWDOMSourcePayloadXMLBindingProvider
implements|implements
name|Provider
argument_list|<
name|DOMSource
argument_list|>
block|{
annotation|@
name|Resource
name|WebServiceContext
name|ctx
decl_stmt|;
specifier|public
name|HWDOMSourcePayloadXMLBindingProvider
parameter_list|()
block|{     }
specifier|public
name|DOMSource
name|invoke
parameter_list|(
name|DOMSource
name|request
parameter_list|)
block|{
if|if
condition|(
name|request
operator|!=
literal|null
condition|)
block|{
name|QName
name|qn
init|=
operator|(
name|QName
operator|)
name|ctx
operator|.
name|getMessageContext
argument_list|()
operator|.
name|get
argument_list|(
name|MessageContext
operator|.
name|WSDL_OPERATION
argument_list|)
decl_stmt|;
if|if
condition|(
name|qn
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"No Operation Name"
argument_list|)
throw|;
block|}
block|}
name|DocumentBuilderFactory
name|factory
decl_stmt|;
name|DocumentBuilder
name|builder
decl_stmt|;
name|Document
name|document
init|=
literal|null
decl_stmt|;
name|DOMSource
name|response
init|=
literal|null
decl_stmt|;
try|try
block|{
name|factory
operator|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|factory
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|builder
operator|=
name|factory
operator|.
name|newDocumentBuilder
argument_list|()
expr_stmt|;
name|InputStream
name|greetMeResponse
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"resources/XML_GreetMeDocLiteralResp.xml"
argument_list|)
decl_stmt|;
name|document
operator|=
name|builder
operator|.
name|parse
argument_list|(
name|greetMeResponse
argument_list|)
expr_stmt|;
name|DOMUtils
operator|.
name|writeXml
argument_list|(
name|document
argument_list|,
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
name|response
operator|=
operator|new
name|DOMSource
argument_list|(
name|document
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
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

