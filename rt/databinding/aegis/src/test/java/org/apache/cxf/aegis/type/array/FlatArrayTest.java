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
name|array
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

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
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
name|frontend
operator|.
name|ServerFactoryBean
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
name|interceptor
operator|.
name|LoggingInInterceptor
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
name|FlatArrayTest
extends|extends
name|AbstractAegisTest
block|{
specifier|private
specifier|static
specifier|final
name|int
index|[]
name|INT_ARRAY
init|=
operator|new
name|int
index|[]
block|{
literal|4
block|,
literal|6
block|,
literal|12
block|}
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|STRING_ARRAY
init|=
block|{
literal|"fillo"
block|,
literal|"dough"
block|}
decl_stmt|;
specifier|private
name|Document
name|arrayWsdlDoc
decl_stmt|;
specifier|private
name|FlatArrayService
name|service
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
name|service
operator|=
operator|new
name|FlatArrayService
argument_list|()
expr_stmt|;
name|ServerFactoryBean
name|sf
init|=
operator|new
name|ServerFactoryBean
argument_list|()
decl_stmt|;
comment|// use full parameter names.
name|sf
operator|.
name|setServiceClass
argument_list|(
name|FlatArrayServiceInterface
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setServiceBean
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"local://FlatArray"
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setDataBinding
argument_list|(
operator|new
name|AegisDatabinding
argument_list|()
argument_list|)
expr_stmt|;
name|Server
name|s
init|=
name|sf
operator|.
name|create
argument_list|()
decl_stmt|;
name|s
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|arrayWsdlDoc
operator|=
name|getWSDLDocument
argument_list|(
literal|"FlatArrayServiceInterface"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testXmlConfigurationOfParameterTypeSchema
parameter_list|()
throws|throws
name|Exception
block|{
name|NodeList
name|typeList
init|=
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:types"
operator|+
literal|"/xsd:schema"
operator|+
literal|"[@targetNamespace='http://array.type.aegis.cxf.apache.org/']"
operator|+
literal|"/xsd:complexType[@name=\"submitStringArray\"]"
operator|+
literal|"/xsd:sequence/xsd:element"
operator|+
literal|"[@name='array']"
argument_list|,
name|arrayWsdlDoc
argument_list|)
decl_stmt|;
name|Element
name|typeElement
init|=
operator|(
name|Element
operator|)
name|typeList
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|nillableValue
init|=
name|typeElement
operator|.
name|getAttribute
argument_list|(
literal|"nillable"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|nillableValue
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|nillableValue
argument_list|)
operator|||
literal|"false"
operator|.
name|equals
argument_list|(
literal|"nillableValue"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|typeString
init|=
name|typeElement
operator|.
name|getAttribute
argument_list|(
literal|"type"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"xsd:string"
argument_list|,
name|typeString
argument_list|)
expr_stmt|;
comment|// no ArrayOf
name|typeList
operator|=
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:types"
operator|+
literal|"/xsd:schema[@targetNamespace='http://array.type.aegis.cxf.apache.org']"
operator|+
literal|"/xsd:complexType[@name='BeanWithFlatArray']"
operator|+
literal|"/xsd:sequence/xsd:element[@name='values']"
argument_list|,
name|arrayWsdlDoc
argument_list|)
expr_stmt|;
name|typeElement
operator|=
operator|(
name|Element
operator|)
name|typeList
operator|.
name|item
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertValidBoolean
argument_list|(
literal|"@type='xsd:int'"
argument_list|,
name|typeElement
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDataMovementPart
parameter_list|()
throws|throws
name|Exception
block|{
name|ClientProxyFactoryBean
name|proxyFac
init|=
operator|new
name|ClientProxyFactoryBean
argument_list|()
decl_stmt|;
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
literal|"local://FlatArray"
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|setServiceClass
argument_list|(
name|FlatArrayServiceInterface
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
name|Object
name|proxyObj
init|=
name|proxyFac
operator|.
name|create
argument_list|()
decl_stmt|;
name|FlatArrayServiceInterface
name|client
init|=
operator|(
name|FlatArrayServiceInterface
operator|)
name|proxyObj
decl_stmt|;
name|client
operator|.
name|submitStringArray
argument_list|(
name|STRING_ARRAY
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|STRING_ARRAY
argument_list|,
name|service
operator|.
name|stringArrayValue
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDataMovementBean
parameter_list|()
throws|throws
name|Exception
block|{
name|ClientProxyFactoryBean
name|proxyFac
init|=
operator|new
name|ClientProxyFactoryBean
argument_list|()
decl_stmt|;
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
literal|"local://FlatArray"
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|setServiceClass
argument_list|(
name|FlatArrayServiceInterface
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
name|Object
name|proxyObj
init|=
name|proxyFac
operator|.
name|create
argument_list|()
decl_stmt|;
name|FlatArrayServiceInterface
name|client
init|=
operator|(
name|FlatArrayServiceInterface
operator|)
name|proxyObj
decl_stmt|;
name|BeanWithFlatArray
name|bwfa
init|=
operator|new
name|BeanWithFlatArray
argument_list|()
decl_stmt|;
name|bwfa
operator|.
name|setValues
argument_list|(
name|INT_ARRAY
argument_list|)
expr_stmt|;
name|client
operator|.
name|takeBeanWithFlatArray
argument_list|(
name|bwfa
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|INT_ARRAY
argument_list|,
name|service
operator|.
name|beanWithFlatArrayValue
operator|.
name|getValues
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

