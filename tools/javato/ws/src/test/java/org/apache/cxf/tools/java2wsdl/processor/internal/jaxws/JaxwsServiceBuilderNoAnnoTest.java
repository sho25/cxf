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
name|java2wsdl
operator|.
name|processor
operator|.
name|internal
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
name|URI
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
name|jaxb
operator|.
name|JAXBContextCache
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
name|jaxws
operator|.
name|JaxwsServiceBuilder
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
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
name|java2wsdl
operator|.
name|generator
operator|.
name|wsdl11
operator|.
name|WSDL11Generator
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
name|JaxwsServiceBuilderNoAnnoTest
extends|extends
name|ProcessorTestBase
block|{
name|JaxwsServiceBuilder
name|builder
init|=
operator|new
name|JaxwsServiceBuilder
argument_list|()
decl_stmt|;
name|WSDL11Generator
name|generator
init|=
operator|new
name|WSDL11Generator
argument_list|()
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXBContextCache
operator|.
name|clearCaches
argument_list|()
expr_stmt|;
name|builder
operator|.
name|setBus
argument_list|(
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setBus
argument_list|(
name|builder
operator|.
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGeneratedWithElementryClass
parameter_list|()
throws|throws
name|Exception
block|{
name|builder
operator|.
name|setServiceClass
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|fortest
operator|.
name|classnoanno
operator|.
name|docbare
operator|.
name|Stock
operator|.
name|class
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setAddress
argument_list|(
literal|"http://localhost"
argument_list|)
expr_stmt|;
name|ServiceInfo
name|service
init|=
name|builder
operator|.
name|createService
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setServiceModel
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|File
name|output
init|=
name|getOutputFile
argument_list|(
literal|"stock_noanno_bare.wsdl"
argument_list|)
decl_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|URI
name|expectedFile
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"expected/stock_noanno_bare.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
decl_stmt|;
name|assertWsdlEquals
argument_list|(
operator|new
name|File
argument_list|(
name|expectedFile
argument_list|)
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGeneratedWithDocWrappedClass
parameter_list|()
throws|throws
name|Exception
block|{
name|builder
operator|.
name|setServiceClass
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|fortest
operator|.
name|classnoanno
operator|.
name|docwrapped
operator|.
name|Stock
operator|.
name|class
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setAddress
argument_list|(
literal|"http://localhost"
argument_list|)
expr_stmt|;
name|ServiceInfo
name|service
init|=
name|builder
operator|.
name|createService
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setServiceModel
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|File
name|output
init|=
name|getOutputFile
argument_list|(
literal|"stock_noanno_wrapped.wsdl"
argument_list|)
decl_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|URI
name|expectedFile
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"expected/stock_noanno_wrapped.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
decl_stmt|;
name|assertWsdlEquals
argument_list|(
operator|new
name|File
argument_list|(
name|expectedFile
argument_list|)
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
comment|// * Missing wsdl:types
comment|// * input message of binding operation "getPrice" MUST specify a value for the "namespace" attribute
comment|// * output message of binding operation "getPrice" MUST specify a value for the "namespace" attribute
comment|// CXF-527
annotation|@
name|Test
specifier|public
name|void
name|testGeneratedWithRPCClass
parameter_list|()
throws|throws
name|Exception
block|{
name|builder
operator|.
name|setServiceClass
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|fortest
operator|.
name|classnoanno
operator|.
name|rpc
operator|.
name|Stock
operator|.
name|class
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setAddress
argument_list|(
literal|"http://localhost"
argument_list|)
expr_stmt|;
name|ServiceInfo
name|service
init|=
name|builder
operator|.
name|createService
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setServiceModel
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|File
name|output
init|=
name|getOutputFile
argument_list|(
literal|"stock_noanno_rpc.wsdl"
argument_list|)
decl_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|URI
name|expectedFile
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"expected/stock_noanno_rpc.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
decl_stmt|;
name|assertWsdlEquals
argument_list|(
operator|new
name|File
argument_list|(
name|expectedFile
argument_list|)
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
specifier|private
name|File
name|getOutputFile
parameter_list|(
name|String
name|fileName
parameter_list|)
block|{
return|return
operator|new
name|File
argument_list|(
name|output
argument_list|,
name|fileName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

