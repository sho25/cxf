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
name|common
operator|.
name|model
package|;
end_package

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

begin_class
specifier|public
class|class
name|JavaTypeTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetPredefinedDefaultTypeValue
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"0"
argument_list|,
operator|new
name|JavaType
argument_list|(
literal|"i"
argument_list|,
name|int
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
operator|.
name|getDefaultTypeValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"false"
argument_list|,
operator|new
name|JavaType
argument_list|(
literal|"i"
argument_list|,
name|boolean
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
operator|.
name|getDefaultTypeValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"new javax.xml.namespace.QName(\"\", \"\")"
argument_list|,
operator|new
name|JavaType
argument_list|(
literal|"i"
argument_list|,
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
operator|.
name|getDefaultTypeValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetArrayDefaultTypeValue
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"new int[0]"
argument_list|,
operator|new
name|JavaType
argument_list|(
literal|"i"
argument_list|,
literal|"int[]"
argument_list|,
literal|null
argument_list|)
operator|.
name|getDefaultTypeValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"new String[0]"
argument_list|,
operator|new
name|JavaType
argument_list|(
literal|"i"
argument_list|,
literal|"String[]"
argument_list|,
literal|null
argument_list|)
operator|.
name|getDefaultTypeValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetClassDefaultTypeValue
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"new org.apache.cxf.tools.common.model.JavaType()"
argument_list|,
operator|new
name|JavaType
argument_list|(
literal|"i"
argument_list|,
literal|"org.apache.cxf.tools.common.model.JavaType"
argument_list|,
literal|null
argument_list|)
operator|.
name|getDefaultTypeValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetClass
parameter_list|()
block|{
name|JavaType
name|type
init|=
operator|new
name|JavaType
argument_list|()
decl_stmt|;
name|type
operator|.
name|setClassName
argument_list|(
literal|"foo.bar.A"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo.bar"
argument_list|,
name|type
operator|.
name|getPackageName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|type
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

