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
name|java5
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|AegisType
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
name|DefaultTypeCreator
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
name|DefaultTypeMapping
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
name|XFireXmlParamTypeTest
extends|extends
name|AbstractAegisTest
block|{
specifier|private
name|DefaultTypeMapping
name|tm
decl_stmt|;
specifier|private
name|Java5TypeCreator
name|creator
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
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
name|tm
operator|=
operator|new
name|DefaultTypeMapping
argument_list|(
literal|null
argument_list|,
name|DefaultTypeMapping
operator|.
name|createDefaultTypeMapping
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|creator
operator|=
operator|new
name|Java5TypeCreator
argument_list|()
expr_stmt|;
name|creator
operator|.
name|setNextCreator
argument_list|(
operator|new
name|DefaultTypeCreator
argument_list|()
argument_list|)
expr_stmt|;
name|creator
operator|.
name|setConfiguration
argument_list|(
operator|new
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
name|Configuration
argument_list|()
argument_list|)
expr_stmt|;
name|tm
operator|.
name|setTypeCreator
argument_list|(
name|creator
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testType
parameter_list|()
throws|throws
name|Exception
block|{
name|Method
name|m
init|=
name|CustomTypeService
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"doFoo"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|String
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|AegisType
name|type
init|=
name|creator
operator|.
name|createType
argument_list|(
name|m
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|tm
operator|.
name|register
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|type
operator|instanceof
name|CustomStringType
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:xfire:foo"
argument_list|,
literal|"custom"
argument_list|)
argument_list|,
name|type
operator|.
name|getSchemaType
argument_list|()
argument_list|)
expr_stmt|;
name|type
operator|=
name|creator
operator|.
name|createType
argument_list|(
name|m
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|tm
operator|.
name|register
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|type
operator|instanceof
name|CustomStringType
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:xfire:foo"
argument_list|,
literal|"custom"
argument_list|)
argument_list|,
name|type
operator|.
name|getSchemaType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMapServiceWSDL
parameter_list|()
throws|throws
name|Exception
block|{
name|createService
argument_list|(
name|CustomTypeService
operator|.
name|class
argument_list|,
operator|new
name|CustomTypeService
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Document
name|wsdl
init|=
name|getWSDLDocument
argument_list|(
literal|"CustomTypeService"
argument_list|)
decl_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:element[@name='s'][@type='ns0:custom']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
block|}
specifier|public
class|class
name|CustomTypeService
block|{
annotation|@
name|XmlReturnType
argument_list|(
name|type
operator|=
name|CustomStringType
operator|.
name|class
argument_list|,
name|namespace
operator|=
literal|"urn:xfire:foo"
argument_list|,
name|name
operator|=
literal|"custom"
argument_list|)
specifier|public
name|String
name|doFoo
parameter_list|(
annotation|@
name|XmlParamType
argument_list|(
name|type
operator|=
name|CustomStringType
operator|.
name|class
argument_list|,
name|namespace
operator|=
literal|"urn:xfire:foo"
argument_list|,
name|name
operator|=
literal|"custom"
argument_list|)
name|String
name|s
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

