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
name|javascript
operator|.
name|types
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|Collection
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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLOutputFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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
name|databinding
operator|.
name|DataBinding
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
name|databinding
operator|.
name|DataWriter
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
name|javascript
operator|.
name|BasicNameManager
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
name|javascript
operator|.
name|JavascriptTestUtilities
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
name|javascript
operator|.
name|NameManager
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
name|javascript
operator|.
name|NamespacePrefixAccumulator
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
name|javascript
operator|.
name|fortest
operator|.
name|AttributeTestBean
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
name|JAXBDataBinding
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
name|service
operator|.
name|model
operator|.
name|SchemaInfo
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
name|test
operator|.
name|AbstractCXFSpringTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|GenericApplicationContext
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_class
specifier|public
class|class
name|AttributeTest
extends|extends
name|AbstractCXFSpringTest
block|{
specifier|private
name|JavascriptTestUtilities
name|testUtilities
decl_stmt|;
specifier|private
name|XMLOutputFactory
name|xmlOutputFactory
decl_stmt|;
specifier|private
name|Client
name|client
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ServiceInfo
argument_list|>
name|serviceInfos
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|SchemaInfo
argument_list|>
name|schemata
decl_stmt|;
specifier|private
name|NameManager
name|nameManager
decl_stmt|;
specifier|private
name|JaxWsProxyFactoryBean
name|clientProxyFactory
decl_stmt|;
specifier|public
name|AttributeTest
parameter_list|()
throws|throws
name|Exception
block|{
name|testUtilities
operator|=
operator|new
name|JavascriptTestUtilities
argument_list|(
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|addDefaultNamespaces
argument_list|()
expr_stmt|;
name|xmlOutputFactory
operator|=
name|XMLOutputFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|String
index|[]
name|getConfigLocations
parameter_list|()
block|{
return|return
operator|new
name|String
index|[]
block|{
literal|"classpath:attributeTestBeans.xml"
block|}
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeserialization
parameter_list|()
throws|throws
name|Exception
block|{
name|setupClientAndRhino
argument_list|(
literal|"attribute-test-proxy-factory"
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|readResourceIntoRhino
argument_list|(
literal|"/attributeTests.js"
argument_list|)
expr_stmt|;
name|DataBinding
name|dataBinding
init|=
operator|new
name|JAXBDataBinding
argument_list|(
name|AttributeTestBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dataBinding
argument_list|)
expr_stmt|;
name|AttributeTestBean
name|bean
init|=
operator|new
name|AttributeTestBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|element1
operator|=
literal|"e1"
expr_stmt|;
name|bean
operator|.
name|element2
operator|=
literal|"e2"
expr_stmt|;
name|bean
operator|.
name|attribute1
operator|=
literal|"a1"
expr_stmt|;
name|bean
operator|.
name|attribute2
operator|=
literal|"a2"
expr_stmt|;
name|String
name|serialized
init|=
name|serializeObject
argument_list|(
name|dataBinding
argument_list|,
name|bean
argument_list|)
decl_stmt|;
name|testUtilities
operator|.
name|rhinoCallInContext
argument_list|(
literal|"deserializeAttributeTestBean"
argument_list|,
name|serialized
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|serializeObject
parameter_list|(
name|DataBinding
name|dataBinding
parameter_list|,
name|AttributeTestBean
name|bean
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|DataWriter
argument_list|<
name|XMLStreamWriter
argument_list|>
name|writer
init|=
name|dataBinding
operator|.
name|createWriter
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
name|StringWriter
name|stringWriter
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|xmlStreamWriter
init|=
name|xmlOutputFactory
operator|.
name|createXMLStreamWriter
argument_list|(
name|stringWriter
argument_list|)
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|bean
argument_list|,
name|xmlStreamWriter
argument_list|)
expr_stmt|;
name|xmlStreamWriter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|xmlStreamWriter
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|stringWriter
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|void
name|setupClientAndRhino
parameter_list|(
name|String
name|clientProxyFactoryBeanId
parameter_list|)
throws|throws
name|IOException
block|{
name|testUtilities
operator|.
name|setBus
argument_list|(
name|getBean
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
literal|"cxf"
argument_list|)
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|initializeRhino
argument_list|()
expr_stmt|;
name|testUtilities
operator|.
name|readResourceIntoRhino
argument_list|(
literal|"/org/apache/cxf/javascript/cxf-utils.js"
argument_list|)
expr_stmt|;
name|clientProxyFactory
operator|=
name|getBean
argument_list|(
name|JaxWsProxyFactoryBean
operator|.
name|class
argument_list|,
name|clientProxyFactoryBeanId
argument_list|)
expr_stmt|;
name|client
operator|=
name|clientProxyFactory
operator|.
name|getClientFactoryBean
argument_list|()
operator|.
name|create
argument_list|()
expr_stmt|;
name|serviceInfos
operator|=
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getServiceInfos
argument_list|()
expr_stmt|;
comment|// there can only be one.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|serviceInfos
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ServiceInfo
name|serviceInfo
init|=
name|serviceInfos
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|schemata
operator|=
name|serviceInfo
operator|.
name|getSchemas
argument_list|()
expr_stmt|;
name|nameManager
operator|=
name|BasicNameManager
operator|.
name|newNameManager
argument_list|(
name|serviceInfo
argument_list|)
expr_stmt|;
name|NamespacePrefixAccumulator
name|prefixAccumulator
init|=
operator|new
name|NamespacePrefixAccumulator
argument_list|(
name|serviceInfo
operator|.
name|getXmlSchemaCollection
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|SchemaInfo
name|schema
range|:
name|schemata
control|)
block|{
name|SchemaJavascriptBuilder
name|builder
init|=
operator|new
name|SchemaJavascriptBuilder
argument_list|(
name|serviceInfo
operator|.
name|getXmlSchemaCollection
argument_list|()
argument_list|,
name|prefixAccumulator
argument_list|,
name|nameManager
argument_list|)
decl_stmt|;
name|String
name|allThatJavascript
init|=
name|builder
operator|.
name|generateCodeForSchema
argument_list|(
name|schema
operator|.
name|getSchema
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|allThatJavascript
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|readStringIntoRhino
argument_list|(
name|allThatJavascript
argument_list|,
name|schema
operator|.
name|toString
argument_list|()
operator|+
literal|".js"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|additionalSpringConfiguration
parameter_list|(
name|GenericApplicationContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{     }
block|}
end_class

end_unit

