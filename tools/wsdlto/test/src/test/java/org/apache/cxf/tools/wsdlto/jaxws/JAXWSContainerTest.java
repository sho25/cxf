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
name|wsdlto
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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|Map
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
name|helpers
operator|.
name|CastUtils
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
name|FrontEndGenerator
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
name|ToolContext
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
name|model
operator|.
name|JavaInterface
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
name|model
operator|.
name|JavaMethod
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
name|model
operator|.
name|JavaModel
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
name|model
operator|.
name|JavaPort
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
name|model
operator|.
name|JavaServiceClass
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
name|validator
operator|.
name|ServiceValidator
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
name|wsdlto
operator|.
name|core
operator|.
name|DataBindingProfile
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
name|wsdlto
operator|.
name|core
operator|.
name|FrontEndProfile
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
name|wsdlto
operator|.
name|core
operator|.
name|PluginLoader
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
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|JAXWSContainer
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
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|processor
operator|.
name|WSDLToJavaProcessor
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
name|JAXWSContainerTest
extends|extends
name|ProcessorTestBase
block|{
annotation|@
name|Test
specifier|public
name|void
name|testCodeGen
parameter_list|()
block|{
try|try
block|{
name|JAXWSContainer
name|container
init|=
operator|new
name|JAXWSContainer
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|ToolContext
name|context
init|=
operator|new
name|ToolContext
argument_list|()
decl_stmt|;
comment|// By default we only generate the SEI/Types/Exception classes/Service Class(client stub)
comment|// Uncomment to generate the impl class
comment|// context.put(ToolConstants.CFG_IMPL, "impl");
comment|// Uncomment to compile the generated classes
comment|// context.put(ToolConstants.CFG_COMPILE, ToolConstants.CFG_COMPILE);
comment|// Where to put the compiled classes
comment|// context.put(ToolConstants.CFG_CLASSDIR, output.getCanonicalPath() + "/classes");
comment|// Where to put the generated source code
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|,
name|output
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|,
name|getLocation
argument_list|(
literal|"/wsdl2java_wsdl/hello_world.wsdl"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Delegate jaxb to generate the type classes
name|context
operator|.
name|put
argument_list|(
name|DataBindingProfile
operator|.
name|class
argument_list|,
name|PluginLoader
operator|.
name|getInstance
argument_list|()
operator|.
name|getDataBindingProfile
argument_list|(
literal|"jaxb"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|FrontEndProfile
operator|.
name|class
argument_list|,
name|PluginLoader
operator|.
name|getInstance
argument_list|()
operator|.
name|getFrontEndProfile
argument_list|(
literal|"jaxws"
argument_list|)
argument_list|)
expr_stmt|;
comment|// In case you want to remove some generators
name|List
argument_list|<
name|String
argument_list|>
name|generatorNames
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
name|ToolConstants
operator|.
name|CLT_GENERATOR
block|,
name|ToolConstants
operator|.
name|SVR_GENERATOR
block|,
name|ToolConstants
operator|.
name|IMPL_GENERATOR
block|,
name|ToolConstants
operator|.
name|ANT_GENERATOR
block|,
name|ToolConstants
operator|.
name|SERVICE_GENERATOR
block|,
name|ToolConstants
operator|.
name|FAULT_GENERATOR
block|,
name|ToolConstants
operator|.
name|SEI_GENERATOR
block|}
argument_list|)
decl_stmt|;
name|FrontEndProfile
name|frontend
init|=
name|context
operator|.
name|get
argument_list|(
name|FrontEndProfile
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|FrontEndGenerator
argument_list|>
name|generators
init|=
name|frontend
operator|.
name|getGenerators
argument_list|()
decl_stmt|;
for|for
control|(
name|FrontEndGenerator
name|generator
range|:
name|generators
control|)
block|{
name|assertTrue
argument_list|(
name|generatorNames
operator|.
name|contains
argument_list|(
name|generator
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|container
operator|.
name|setContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
comment|// Now shoot
name|container
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// At this point you should be able to get the
comment|// SEI/Service(Client stub)/Exception classes/Types classes
name|assertNotNull
argument_list|(
name|output
operator|.
name|list
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|output
operator|.
name|list
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"org/apache/cxf/w2j/hello_world_soap_http/Greeter.java"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"org/apache/cxf/w2j/hello_world_soap_http/SOAPService.java"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"org/apache/cxf/w2j/hello_world_soap_http/NoSuchCodeLitFault.java"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"org/apache/cxf/w2j/hello_world_soap_http/types/SayHi.java"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"org/apache/cxf/w2j/hello_world_soap_http/types/GreetMe.java"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// Now you can get the JavaModel from the context.
name|JavaModel
name|javaModel
init|=
name|context
operator|.
name|get
argument_list|(
name|JavaModel
operator|.
name|class
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|JavaInterface
argument_list|>
name|interfaces
init|=
name|javaModel
operator|.
name|getInterfaces
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|interfaces
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JavaInterface
name|intf
init|=
name|interfaces
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://cxf.apache.org/w2j/hello_world_soap_http"
argument_list|,
name|intf
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Greeter"
argument_list|,
name|intf
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.cxf.w2j.hello_world_soap_http"
argument_list|,
name|intf
operator|.
name|getPackageName
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|JavaMethod
argument_list|>
name|methods
init|=
name|intf
operator|.
name|getMethods
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|methods
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Boolean
name|methodSame
init|=
literal|false
decl_stmt|;
for|for
control|(
name|JavaMethod
name|m1
range|:
name|methods
control|)
block|{
if|if
condition|(
name|m1
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"testDocLitFault"
argument_list|)
condition|)
block|{
name|methodSame
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
name|assertTrue
argument_list|(
name|methodSame
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSuppressCodeGen
parameter_list|()
block|{
try|try
block|{
name|JAXWSContainer
name|container
init|=
operator|new
name|JAXWSContainer
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|ToolContext
name|context
init|=
operator|new
name|ToolContext
argument_list|()
decl_stmt|;
comment|// Do not generate any artifacts, we just want the code model.
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_SUPPRESS_GEN
argument_list|,
literal|"suppress"
argument_list|)
expr_stmt|;
comment|// Where to put the generated source code
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|,
name|output
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|,
name|getLocation
argument_list|(
literal|"/wsdl2java_wsdl/hello_world.wsdl"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Delegate jaxb to generate the type classes
name|context
operator|.
name|put
argument_list|(
name|DataBindingProfile
operator|.
name|class
argument_list|,
name|PluginLoader
operator|.
name|getInstance
argument_list|()
operator|.
name|getDataBindingProfile
argument_list|(
literal|"jaxb"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|FrontEndProfile
operator|.
name|class
argument_list|,
name|PluginLoader
operator|.
name|getInstance
argument_list|()
operator|.
name|getFrontEndProfile
argument_list|(
literal|"jaxws"
argument_list|)
argument_list|)
expr_stmt|;
name|container
operator|.
name|setContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
comment|// Now shoot
name|container
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// At this point you should be able to get the
comment|// SEI/Service(Client stub)/Exception classes/Types classes
name|assertNotNull
argument_list|(
name|output
operator|.
name|list
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|output
operator|.
name|list
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// Now you can get the JavaModel from the context.
name|Map
argument_list|<
name|QName
argument_list|,
name|JavaModel
argument_list|>
name|map
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|context
operator|.
name|get
argument_list|(
name|WSDLToJavaProcessor
operator|.
name|MODEL_MAP
argument_list|)
argument_list|)
decl_stmt|;
name|JavaModel
name|javaModel
init|=
name|map
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/w2j/hello_world_soap_http"
argument_list|,
literal|"SOAPService"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|javaModel
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|JavaInterface
argument_list|>
name|interfaces
init|=
name|javaModel
operator|.
name|getInterfaces
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|interfaces
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JavaInterface
name|intf
init|=
name|interfaces
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|interfaceName
init|=
name|intf
operator|.
name|getName
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Greeter"
argument_list|,
name|interfaceName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://cxf.apache.org/w2j/hello_world_soap_http"
argument_list|,
name|intf
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.cxf.w2j.hello_world_soap_http"
argument_list|,
name|intf
operator|.
name|getPackageName
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|JavaMethod
argument_list|>
name|methods
init|=
name|intf
operator|.
name|getMethods
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|methods
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Boolean
name|methodSame
init|=
literal|false
decl_stmt|;
name|JavaMethod
name|m1
init|=
literal|null
decl_stmt|;
for|for
control|(
name|JavaMethod
name|m2
range|:
name|methods
control|)
block|{
if|if
condition|(
name|m2
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"testDocLitFault"
argument_list|)
condition|)
block|{
name|methodSame
operator|=
literal|true
expr_stmt|;
name|m1
operator|=
name|m2
expr_stmt|;
break|break;
block|}
block|}
name|assertTrue
argument_list|(
name|methodSame
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|m1
operator|.
name|getExceptions
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"BadRecordLitFault"
argument_list|,
name|m1
operator|.
name|getExceptions
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"NoSuchCodeLitFault"
argument_list|,
name|m1
operator|.
name|getExceptions
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|null
decl_stmt|;
for|for
control|(
name|JavaServiceClass
name|service
range|:
name|javaModel
operator|.
name|getServiceClasses
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
literal|"SOAPService_Test1"
operator|.
name|equals
argument_list|(
name|service
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|List
argument_list|<
name|JavaPort
argument_list|>
name|ports
init|=
name|service
operator|.
name|getPorts
argument_list|()
decl_stmt|;
for|for
control|(
name|JavaPort
name|port
range|:
name|ports
control|)
block|{
if|if
condition|(
name|interfaceName
operator|.
name|equals
argument_list|(
name|port
operator|.
name|getPortType
argument_list|()
argument_list|)
condition|)
block|{
name|address
operator|=
name|port
operator|.
name|getBindingAdress
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|address
argument_list|)
condition|)
block|{
break|break;
block|}
block|}
name|assertEquals
argument_list|(
literal|"http://localhost:9000/SoapContext/SoapPort"
argument_list|,
name|address
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
block|}
specifier|public
name|void
name|testGetServceValidator
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXWSContainer
name|container
init|=
operator|new
name|JAXWSContainer
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ServiceValidator
argument_list|>
name|validators
init|=
name|container
operator|.
name|getServiceValidators
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|validators
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|validators
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|getLocation
parameter_list|(
name|String
name|wsdlFile
parameter_list|)
throws|throws
name|URISyntaxException
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|wsdlFile
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

