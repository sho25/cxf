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
name|integration
package|;
end_package

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

begin_comment
comment|//import org.w3c.dom.Node;
end_comment

begin_comment
comment|//import org.w3c.dom.Text;
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
name|databinding
operator|.
name|AegisDatabinding
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
name|services
operator|.
name|BeanWithDOM
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
name|services
operator|.
name|DocumentService
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
name|services
operator|.
name|IDocumentService
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
name|Client
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
name|ClientImpl
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
name|frontend
operator|.
name|ClientProxy
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
name|frontend
operator|.
name|ClientProxyFactoryBean
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
name|factory
operator|.
name|ReflectionServiceFactoryBean
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

begin_comment
comment|/**  * Test mapping DOM.  * Commented out code for the case of an embedded Node object,  *  which doesn't work at all, and perhaps isn't supposed to.  */
end_comment

begin_class
specifier|public
class|class
name|DOMMappingTest
extends|extends
name|AbstractAegisTest
block|{
specifier|private
name|IDocumentService
name|docClient
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
name|createService
argument_list|(
name|DocumentService
operator|.
name|class
argument_list|,
literal|"DocService"
argument_list|)
expr_stmt|;
name|ClientProxyFactoryBean
name|proxyFac
init|=
operator|new
name|ClientProxyFactoryBean
argument_list|()
decl_stmt|;
name|ReflectionServiceFactoryBean
name|factory
init|=
operator|new
name|ReflectionServiceFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|getServiceConfigurations
argument_list|()
operator|.
name|add
argument_list|(
literal|0
argument_list|,
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|databinding
operator|.
name|XFireCompatibilityServiceConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|setServiceFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|setDataBinding
argument_list|(
operator|new
name|AegisDatabinding
argument_list|()
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|setAddress
argument_list|(
literal|"local://DocService"
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
name|Object
name|proxyObj
init|=
name|proxyFac
operator|.
name|create
argument_list|(
name|IDocumentService
operator|.
name|class
argument_list|)
decl_stmt|;
name|docClient
operator|=
operator|(
name|IDocumentService
operator|)
name|proxyObj
expr_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|proxyObj
argument_list|)
decl_stmt|;
name|ClientImpl
name|clientImpl
init|=
operator|(
name|ClientImpl
operator|)
name|client
decl_stmt|;
name|clientImpl
operator|.
name|setSynchronousTimeout
argument_list|(
literal|1000000000
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSimpleString
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|s
init|=
name|docClient
operator|.
name|simpleStringReturn
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"simple"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDocService
parameter_list|()
throws|throws
name|Exception
block|{
name|Document
name|doc
init|=
name|docClient
operator|.
name|returnDocument
argument_list|()
decl_stmt|;
name|Element
name|rootElement
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"carrot"
argument_list|,
name|rootElement
operator|.
name|getNodeName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBeanCases
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanWithDOM
name|bwd
init|=
name|docClient
operator|.
name|getBeanWithDOM
argument_list|()
decl_stmt|;
name|Element
name|rootElement
init|=
name|bwd
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"carrot"
argument_list|,
name|rootElement
operator|.
name|getNodeName
argument_list|()
argument_list|)
expr_stmt|;
comment|/*         Node shouldBeText = bwd.getNode();         assertTrue(shouldBeText instanceof Text);         Text text = (Text) shouldBeText;         assertEquals("Is a root vegetable.", text);         */
block|}
block|}
end_class

end_unit

