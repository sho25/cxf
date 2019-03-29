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
name|helpers
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|annotations
operator|.
name|SchemaValidation
operator|.
name|SchemaValidationType
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|message
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|IMocksControl
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
name|assertFalse
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|ServiceUtilsTest
block|{
specifier|private
name|IMocksControl
name|control
decl_stmt|;
specifier|private
name|Message
name|msg
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
name|msg
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testmakeNamespaceFromClassName
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|tns
init|=
name|ServiceUtils
operator|.
name|makeNamespaceFromClassName
argument_list|(
literal|"com.example.ws.Test"
argument_list|,
literal|"http"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://ws.example.com/"
argument_list|,
name|tns
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRequestResponseTypes
parameter_list|()
block|{
comment|// lets do server side first
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|REQUEST
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|IN
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|REQUEST
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|OUT
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|RESPONSE
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|OUT
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|RESPONSE
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|IN
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
comment|// now client side
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|REQUEST
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|IN
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|REQUEST
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|OUT
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|RESPONSE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|OUT
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|RESPONSE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|IN
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsSchemaValidationEnabled
parameter_list|()
block|{
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|NONE
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|NONE
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|NONE
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|BOTH
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|NONE
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|IN
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|NONE
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|OUT
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|IN
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|NONE
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|IN
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|BOTH
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|IN
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|IN
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|IN
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|OUT
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|OUT
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|NONE
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|OUT
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|BOTH
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|OUT
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|IN
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|OUT
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|OUT
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|BOTH
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|NONE
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|BOTH
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|BOTH
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|BOTH
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|IN
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|SchemaValidationType
operator|.
name|BOTH
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|OUT
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetSchemaValidationTypeBoolean
parameter_list|()
block|{
name|setupSchemaValidationValue
argument_list|(
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SchemaValidationType
operator|.
name|NONE
argument_list|,
name|ServiceUtils
operator|.
name|getSchemaValidationType
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
literal|""
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SchemaValidationType
operator|.
name|NONE
argument_list|,
name|ServiceUtils
operator|.
name|getSchemaValidationType
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SchemaValidationType
operator|.
name|NONE
argument_list|,
name|ServiceUtils
operator|.
name|getSchemaValidationType
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
literal|"false"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SchemaValidationType
operator|.
name|NONE
argument_list|,
name|ServiceUtils
operator|.
name|getSchemaValidationType
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
literal|"FALSE"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SchemaValidationType
operator|.
name|NONE
argument_list|,
name|ServiceUtils
operator|.
name|getSchemaValidationType
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
literal|"fAlse"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SchemaValidationType
operator|.
name|NONE
argument_list|,
name|ServiceUtils
operator|.
name|getSchemaValidationType
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SchemaValidationType
operator|.
name|BOTH
argument_list|,
name|ServiceUtils
operator|.
name|getSchemaValidationType
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
literal|"true"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SchemaValidationType
operator|.
name|BOTH
argument_list|,
name|ServiceUtils
operator|.
name|getSchemaValidationType
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
literal|"TRUE"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SchemaValidationType
operator|.
name|BOTH
argument_list|,
name|ServiceUtils
operator|.
name|getSchemaValidationType
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
literal|"tRue"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SchemaValidationType
operator|.
name|BOTH
argument_list|,
name|ServiceUtils
operator|.
name|getSchemaValidationType
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetSchemaValidationType
parameter_list|()
block|{
for|for
control|(
name|SchemaValidationType
name|type
range|:
name|SchemaValidationType
operator|.
name|values
argument_list|()
control|)
block|{
name|setupSchemaValidationValue
argument_list|(
name|type
operator|.
name|name
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|type
argument_list|,
name|ServiceUtils
operator|.
name|getSchemaValidationType
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|type
operator|.
name|name
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|type
argument_list|,
name|ServiceUtils
operator|.
name|getSchemaValidationType
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|setupSchemaValidationValue
argument_list|(
name|StringUtils
operator|.
name|capitalize
argument_list|(
name|type
operator|.
name|name
argument_list|()
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|type
argument_list|,
name|ServiceUtils
operator|.
name|getSchemaValidationType
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|setupSchemaValidationValue
parameter_list|(
name|Object
name|value
parameter_list|,
name|boolean
name|isRequestor
parameter_list|)
block|{
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|msg
operator|.
name|getContextualProperty
argument_list|(
name|Message
operator|.
name|SCHEMA_VALIDATION_ENABLED
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|msg
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|isRequestor
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

