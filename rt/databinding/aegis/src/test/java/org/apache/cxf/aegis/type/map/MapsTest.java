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
name|aegis
operator|.
name|type
operator|.
name|map
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|wsdl
operator|.
name|Definition
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|WSDLException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|factory
operator|.
name|WSDLFactory
import|;
end_import

begin_comment
comment|//import org.w3c.dom.Document;
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|AbstractAegisTest
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
name|aegis
operator|.
name|type
operator|.
name|map
operator|.
name|fortest
operator|.
name|MapTest
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
name|aegis
operator|.
name|type
operator|.
name|map
operator|.
name|fortest
operator|.
name|MapTestImpl
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
name|aegis
operator|.
name|type
operator|.
name|map
operator|.
name|fortest
operator|.
name|ObjectWithAMap
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
name|aegis
operator|.
name|type
operator|.
name|map
operator|.
name|ns2
operator|.
name|ObjectWithAMapNs2
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
name|endpoint
operator|.
name|Server
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
name|JaxWsProxyFactoryBean
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
name|JaxWsServerFactoryBean
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
name|Ignore
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|MapsTest
extends|extends
name|AbstractAegisTest
block|{
specifier|private
specifier|static
name|MapTest
name|clientInterface
decl_stmt|;
specifier|private
specifier|static
name|Server
name|server
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|JaxWsServerFactoryBean
name|sf
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setServiceClass
argument_list|(
name|MapTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setServiceBean
argument_list|(
operator|new
name|MapTestImpl
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"local://MapTest"
argument_list|)
expr_stmt|;
name|setupAegis
argument_list|(
name|sf
argument_list|)
expr_stmt|;
name|server
operator|=
name|sf
operator|.
name|create
argument_list|()
expr_stmt|;
comment|//        server.getEndpoint().getInInterceptors().add(new LoggingInInterceptor());
comment|//        server.getEndpoint().getOutInterceptors().add(new LoggingOutInterceptor());
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
name|JaxWsProxyFactoryBean
name|proxyFac
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|proxyFac
operator|.
name|setAddress
argument_list|(
literal|"local://MapTest"
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|setServiceClass
argument_list|(
name|MapTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|setupAegis
argument_list|(
name|proxyFac
operator|.
name|getClientFactoryBean
argument_list|()
argument_list|)
expr_stmt|;
name|clientInterface
operator|=
operator|(
name|MapTest
operator|)
name|proxyFac
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Ignore
annotation|@
name|Test
comment|/**      * Until some issues in CXF-1051 are resolved, it's not clear what to test in here.       *       */
specifier|public
name|void
name|testMapWsdl
parameter_list|()
throws|throws
name|WSDLException
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
comment|//Document wsdl = getWSDLDocument("MapTestService");
name|Definition
name|wsdlDef
init|=
name|getWSDLDefinition
argument_list|(
literal|"MapTestService"
argument_list|)
decl_stmt|;
name|StringWriter
name|sink
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|WSDLFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newWSDLWriter
argument_list|()
operator|.
name|writeWSDL
argument_list|(
name|wsdlDef
argument_list|,
name|sink
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|sink
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvocations
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|Long
argument_list|,
name|String
argument_list|>
name|lts
init|=
name|clientInterface
operator|.
name|getMapLongToString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"twenty-seven"
argument_list|,
name|lts
operator|.
name|get
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|27
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testObjectsWithMaps
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectWithAMap
name|obj1
init|=
name|clientInterface
operator|.
name|returnObjectWithAMap
argument_list|()
decl_stmt|;
name|ObjectWithAMapNs2
name|obj2
init|=
name|clientInterface
operator|.
name|returnObjectWithAMapNs2
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|obj1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|obj2
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|obj1
operator|.
name|getTheMap
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|obj2
operator|.
name|getTheMap
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|obj1
operator|.
name|getTheMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|obj2
operator|.
name|getTheMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|obj1
operator|.
name|getTheMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"rainy"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|obj2
operator|.
name|getTheMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"rainy"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|obj1
operator|.
name|getTheMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"sunny"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|obj2
operator|.
name|getTheMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"sunny"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|obj2
operator|.
name|getTheMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"cloudy"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

