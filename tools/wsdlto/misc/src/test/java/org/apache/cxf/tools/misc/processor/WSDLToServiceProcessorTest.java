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
name|tools
operator|.
name|misc
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|soap
operator|.
name|SOAPAddress
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|soap12
operator|.
name|SOAP12Address
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
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
name|BusFactory
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
name|tools
operator|.
name|common
operator|.
name|ProcessorTestBase
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
name|tools
operator|.
name|common
operator|.
name|ToolConstants
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
name|tools
operator|.
name|common
operator|.
name|ToolException
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
name|tools
operator|.
name|misc
operator|.
name|WSDLToService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|WSDLToServiceProcessorTest
extends|extends
name|ProcessorTestBase
block|{
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|env
operator|.
name|put
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNewService
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-transport"
block|,
literal|"soap"
block|,
literal|"-e"
block|,
literal|"serviceins"
block|,
literal|"-p"
block|,
literal|"portins"
block|,
literal|"-n"
block|,
literal|"Greeter_SOAPBinding"
block|,
literal|"-a"
block|,
literal|"http://localhost:9000/newservice/newport"
block|,
literal|"-d"
block|,
name|output
operator|.
name|getCanonicalPath
argument_list|()
block|,
name|getLocation
argument_list|(
literal|"/misctools_wsdl/hello_world.wsdl"
argument_list|)
block|}
decl_stmt|;
name|WSDLToService
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|File
name|outputFile
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"hello_world-service.wsdl"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"New wsdl file is not generated"
argument_list|,
name|outputFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|WSDLToServiceProcessor
name|processor
init|=
operator|new
name|WSDLToServiceProcessor
argument_list|()
decl_stmt|;
name|processor
operator|.
name|setEnvironment
argument_list|(
name|env
argument_list|)
expr_stmt|;
try|try
block|{
name|processor
operator|.
name|parseWSDL
argument_list|(
name|outputFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|Service
name|service
init|=
name|processor
operator|.
name|getWSDLDefinition
argument_list|()
operator|.
name|getService
argument_list|(
operator|new
name|QName
argument_list|(
name|processor
operator|.
name|getWSDLDefinition
argument_list|()
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
literal|"serviceins"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
name|fail
argument_list|(
literal|"Element wsdl:service serviceins Missed!"
argument_list|)
expr_stmt|;
block|}
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|service
operator|.
name|getPort
argument_list|(
literal|"portins"
argument_list|)
operator|.
name|getExtensibilityElements
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
name|it
operator|==
literal|null
operator|||
operator|!
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|fail
argument_list|(
literal|"Element wsdl:port portins Missed!"
argument_list|)
expr_stmt|;
block|}
name|boolean
name|found
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|obj
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|SOAPAddress
condition|)
block|{
name|SOAPAddress
name|soapAddress
init|=
operator|(
name|SOAPAddress
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|soapAddress
operator|.
name|getLocationURI
argument_list|()
operator|!=
literal|null
operator|&&
name|soapAddress
operator|.
name|getLocationURI
argument_list|()
operator|.
name|equals
argument_list|(
literal|"http://localhost:9000/newservice/newport"
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|fail
argument_list|(
literal|"Element soap:address of service port Missed!"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ToolException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Exception Encountered when parsing wsdl, error: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNewServiceSoap12
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-soap12"
block|,
literal|"-transport"
block|,
literal|"soap"
block|,
literal|"-e"
block|,
literal|"SOAPService"
block|,
literal|"-p"
block|,
literal|"SoapPort"
block|,
literal|"-n"
block|,
literal|"Greeter_SOAPBinding"
block|,
literal|"-a"
block|,
literal|"http://localhost:9000/SOAPService/SoapPort"
block|,
literal|"-d"
block|,
name|output
operator|.
name|getCanonicalPath
argument_list|()
block|,
name|getLocation
argument_list|(
literal|"/misctools_wsdl/hello_world_soap12.wsdl"
argument_list|)
block|}
decl_stmt|;
name|WSDLToService
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|File
name|outputFile
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"hello_world_soap12-service.wsdl"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"New wsdl file is not generated"
argument_list|,
name|outputFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|WSDLToServiceProcessor
name|processor
init|=
operator|new
name|WSDLToServiceProcessor
argument_list|()
decl_stmt|;
name|processor
operator|.
name|setEnvironment
argument_list|(
name|env
argument_list|)
expr_stmt|;
try|try
block|{
name|processor
operator|.
name|parseWSDL
argument_list|(
name|outputFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|Service
name|service
init|=
name|processor
operator|.
name|getWSDLDefinition
argument_list|()
operator|.
name|getService
argument_list|(
operator|new
name|QName
argument_list|(
name|processor
operator|.
name|getWSDLDefinition
argument_list|()
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
literal|"SOAPService"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
name|fail
argument_list|(
literal|"Element wsdl:service serviceins Missed!"
argument_list|)
expr_stmt|;
block|}
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|service
operator|.
name|getPort
argument_list|(
literal|"SoapPort"
argument_list|)
operator|.
name|getExtensibilityElements
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
name|it
operator|==
literal|null
operator|||
operator|!
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|fail
argument_list|(
literal|"Element wsdl:port portins Missed!"
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|obj
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|SOAP12Address
condition|)
block|{
name|SOAP12Address
name|soapAddress
init|=
operator|(
name|SOAP12Address
operator|)
name|obj
decl_stmt|;
name|assertNotNull
argument_list|(
name|soapAddress
operator|.
name|getLocationURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost:9000/SOAPService/SoapPort"
argument_list|,
name|soapAddress
operator|.
name|getLocationURI
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|ToolException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Exception Encountered when parsing wsdl, error: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDefaultLocation
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-transport"
block|,
literal|"soap"
block|,
literal|"-e"
block|,
literal|"serviceins"
block|,
literal|"-p"
block|,
literal|"portins"
block|,
literal|"-n"
block|,
literal|"Greeter_SOAPBinding"
block|,
literal|"-d"
block|,
name|output
operator|.
name|getCanonicalPath
argument_list|()
block|,
name|getLocation
argument_list|(
literal|"/misctools_wsdl/hello_world.wsdl"
argument_list|)
block|}
decl_stmt|;
name|WSDLToService
operator|.
name|main
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|File
name|outputFile
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"hello_world-service.wsdl"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"New wsdl file is not generated"
argument_list|,
name|outputFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|WSDLToServiceProcessor
name|processor
init|=
operator|new
name|WSDLToServiceProcessor
argument_list|()
decl_stmt|;
name|processor
operator|.
name|setEnvironment
argument_list|(
name|env
argument_list|)
expr_stmt|;
try|try
block|{
name|processor
operator|.
name|parseWSDL
argument_list|(
name|outputFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|Service
name|service
init|=
name|processor
operator|.
name|getWSDLDefinition
argument_list|()
operator|.
name|getService
argument_list|(
operator|new
name|QName
argument_list|(
name|processor
operator|.
name|getWSDLDefinition
argument_list|()
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
literal|"serviceins"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
name|fail
argument_list|(
literal|"Element wsdl:service serviceins Missed!"
argument_list|)
expr_stmt|;
block|}
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|service
operator|.
name|getPort
argument_list|(
literal|"portins"
argument_list|)
operator|.
name|getExtensibilityElements
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
name|it
operator|==
literal|null
operator|||
operator|!
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|fail
argument_list|(
literal|"Element wsdl:port portins Missed!"
argument_list|)
expr_stmt|;
block|}
name|boolean
name|found
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|obj
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|SOAPAddress
condition|)
block|{
name|SOAPAddress
name|soapAddress
init|=
operator|(
name|SOAPAddress
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|soapAddress
operator|.
name|getLocationURI
argument_list|()
operator|!=
literal|null
operator|&&
name|soapAddress
operator|.
name|getLocationURI
argument_list|()
operator|.
name|equals
argument_list|(
literal|"http://localhost:9000/serviceins/portins"
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|fail
argument_list|(
literal|"Element soap:address of service port Missed!"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ToolException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Exception Encountered when parsing wsdl, error: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testServiceExist
parameter_list|()
throws|throws
name|Exception
block|{
name|WSDLToServiceProcessor
name|processor
init|=
operator|new
name|WSDLToServiceProcessor
argument_list|()
decl_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|,
name|getLocation
argument_list|(
literal|"/misctools_wsdl/hello_world.wsdl"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_TRANSPORT
argument_list|,
operator|new
name|String
argument_list|(
literal|"http"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_SERVICE
argument_list|,
operator|new
name|String
argument_list|(
literal|"SOAPService_Test1"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_PORT
argument_list|,
operator|new
name|String
argument_list|(
literal|"SoapPort_Test1"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_BINDING_ATTR
argument_list|,
operator|new
name|String
argument_list|(
literal|"Greeter_SOAPBinding"
argument_list|)
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setEnvironment
argument_list|(
name|env
argument_list|)
expr_stmt|;
try|try
block|{
name|processor
operator|.
name|process
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Do not catch expected tool exception for service and port exist"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|e
operator|instanceof
name|ToolException
operator|&&
name|e
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"Input service and port already exist in imported contract"
argument_list|)
operator|>=
literal|0
operator|)
condition|)
block|{
name|fail
argument_list|(
literal|"Do not catch tool exception for service and port exist, "
operator|+
literal|"catch other unexpected exception!"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBindingNotExist
parameter_list|()
throws|throws
name|Exception
block|{
name|WSDLToServiceProcessor
name|processor
init|=
operator|new
name|WSDLToServiceProcessor
argument_list|()
decl_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|,
name|getLocation
argument_list|(
literal|"/misctools_wsdl/hello_world.wsdl"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_TRANSPORT
argument_list|,
operator|new
name|String
argument_list|(
literal|"http"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_BINDING_ATTR
argument_list|,
operator|new
name|String
argument_list|(
literal|"BindingNotExist"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_SERVICE
argument_list|,
operator|new
name|String
argument_list|(
literal|"serviceins"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_PORT
argument_list|,
operator|new
name|String
argument_list|(
literal|"portins"
argument_list|)
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setEnvironment
argument_list|(
name|env
argument_list|)
expr_stmt|;
try|try
block|{
name|processor
operator|.
name|process
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Do not catch expected tool exception for  binding not exist!"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|e
operator|instanceof
name|ToolException
operator|&&
name|e
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"Input binding does not exist in imported contract"
argument_list|)
operator|>=
literal|0
operator|)
condition|)
block|{
name|fail
argument_list|(
literal|"Do not catch tool exception for binding not exist, "
operator|+
literal|"catch other unexpected exception!"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWSDLValidation
parameter_list|()
throws|throws
name|Exception
block|{
comment|//intend to use a wsdl which will break WSIBPValidator
name|WSDLToServiceProcessor
name|processor
init|=
operator|new
name|WSDLToServiceProcessor
argument_list|()
decl_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|,
name|getLocation
argument_list|(
literal|"/misctools_wsdl/hello_world_mixed_style.wsdl"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_TRANSPORT
argument_list|,
operator|new
name|String
argument_list|(
literal|"soap"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_BINDING_ATTR
argument_list|,
operator|new
name|String
argument_list|(
literal|"Greeter_SOAPBinding"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_SERVICE
argument_list|,
operator|new
name|String
argument_list|(
literal|"serviceins"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_PORT
argument_list|,
operator|new
name|String
argument_list|(
literal|"portins"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_VALIDATE_WSDL
argument_list|,
name|ToolConstants
operator|.
name|CFG_VALIDATE_WSDL
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setEnvironment
argument_list|(
name|env
argument_list|)
expr_stmt|;
try|try
block|{
name|processor
operator|.
name|process
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Do not catch expected tool exception for breaking WSIBPValidator!"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|e
operator|instanceof
name|ToolException
operator|&&
name|e
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"Mixed style, invalid WSDL"
argument_list|)
operator|>=
literal|0
operator|)
condition|)
block|{
name|fail
argument_list|(
literal|"Do not catch tool exception for breaking WSIBPValidator!, "
operator|+
literal|"catch other unexpected exception!"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

