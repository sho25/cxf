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
name|io
operator|.
name|FileInputStream
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
name|FileUtils
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
name|IOUtils
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
name|junit
operator|.
name|After
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
name|JAXWSBindingTest
extends|extends
name|ProcessorTestBase
block|{
specifier|private
name|JAXWSContainer
name|processor
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
name|env
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
name|env
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
name|processor
operator|=
operator|new
name|JAXWSContainer
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|processor
operator|=
literal|null
expr_stmt|;
name|env
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDateTypeAdapter
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"/wsdl2java_wsdl/echo_date.wsdl"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_BINDING
argument_list|,
name|getLocation
argument_list|(
literal|"/wsdl2java_wsdl/echo_date.xjb"
argument_list|)
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|processor
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|String
name|path
init|=
literal|"/org/apache/cxf/tools/fortest/date"
decl_stmt|;
name|File
name|sei
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
name|path
operator|+
literal|"/EchoDate.java"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|sei
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|seiContent
init|=
name|FileUtils
operator|.
name|getStringFromFile
argument_list|(
name|sei
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|seiContent
operator|.
name|indexOf
argument_list|(
literal|"java.util.Date"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

