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
name|frontend
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
name|Map
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
name|Processor
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
name|plugin
operator|.
name|FrontEnd
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
name|plugin
operator|.
name|Generator
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
name|plugin
operator|.
name|Plugin
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
name|AbstractWSDLBuilder
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
name|processor
operator|.
name|WSDLToJavaProcessor
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
name|wsdl11
operator|.
name|JAXWSDefinitionBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|JAXWSProfileTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testLoadPlugins
parameter_list|()
block|{
name|PluginLoader
name|loader
init|=
name|PluginLoader
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|loader
argument_list|)
expr_stmt|;
name|loader
operator|.
name|loadPlugin
argument_list|(
literal|"/org/apache/cxf/tools/wsdlto/frontend/jaxws/jaxws-plugin.xml"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|loader
operator|.
name|getPlugins
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Plugin
name|plugin
init|=
name|getPlugin
argument_list|(
name|loader
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"tools-jaxws-frontend"
argument_list|,
name|plugin
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2.0"
argument_list|,
name|plugin
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"apache cxf"
argument_list|,
name|plugin
operator|.
name|getProvider
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|FrontEnd
argument_list|>
name|frontends
init|=
name|loader
operator|.
name|getFrontEnds
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|frontends
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|frontends
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|FrontEnd
name|frontend
init|=
name|getFrontEnd
argument_list|(
name|frontends
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"jaxws"
argument_list|,
name|frontend
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.cxf.tools.wsdlto.frontend.jaxws"
argument_list|,
name|frontend
operator|.
name|getPackage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"JAXWSProfile"
argument_list|,
name|frontend
operator|.
name|getProfile
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|frontend
operator|.
name|getGenerators
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|frontend
operator|.
name|getGenerators
argument_list|()
operator|.
name|getGenerator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|frontend
operator|.
name|getGenerators
argument_list|()
operator|.
name|getGenerator
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AntGenerator"
argument_list|,
name|getGenerator
argument_list|(
name|frontend
argument_list|,
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ImplGenerator"
argument_list|,
name|getGenerator
argument_list|(
name|frontend
argument_list|,
literal|1
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|FrontEndProfile
name|profile
init|=
name|loader
operator|.
name|getFrontEndProfile
argument_list|(
literal|"jaxws"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|profile
argument_list|)
expr_stmt|;
comment|//TODO: After generator completed ,umcomment these linses
comment|/*List<FrontEndGenerator> generators = profile.getGenerators();         assertNotNull(generators);         assertEquals(2, generators.size());         assertTrue(generators.get(0) instanceof AntGenerator);         assertTrue(generators.get(1) instanceof ImplGenerator);         */
name|Processor
name|processor
init|=
name|profile
operator|.
name|getProcessor
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|processor
operator|instanceof
name|WSDLToJavaProcessor
argument_list|)
expr_stmt|;
name|AbstractWSDLBuilder
name|builder
init|=
name|profile
operator|.
name|getWSDLBuilder
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|builder
operator|instanceof
name|JAXWSDefinitionBuilder
argument_list|)
expr_stmt|;
name|Class
name|container
init|=
name|profile
operator|.
name|getContainerClass
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|container
argument_list|,
name|JAXWSContainer
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/org/apache/cxf/tools/wsdlto/frontend/jaxws/jaxws-toolspec.xml"
argument_list|,
name|profile
operator|.
name|getToolspec
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Generator
name|getGenerator
parameter_list|(
name|FrontEnd
name|frontend
parameter_list|,
name|int
name|index
parameter_list|)
block|{
return|return
name|frontend
operator|.
name|getGenerators
argument_list|()
operator|.
name|getGenerator
argument_list|()
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
specifier|protected
name|FrontEnd
name|getFrontEnd
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|FrontEnd
argument_list|>
name|frontends
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|int
name|size
init|=
name|frontends
operator|.
name|size
argument_list|()
decl_stmt|;
return|return
name|frontends
operator|.
name|values
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|FrontEnd
index|[
name|size
index|]
argument_list|)
index|[
name|index
index|]
return|;
block|}
specifier|protected
name|Plugin
name|getPlugin
parameter_list|(
name|PluginLoader
name|loader
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|int
name|size
init|=
name|loader
operator|.
name|getPlugins
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
return|return
name|loader
operator|.
name|getPlugins
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|Plugin
index|[
name|size
index|]
argument_list|)
index|[
name|index
index|]
return|;
block|}
block|}
end_class

end_unit

