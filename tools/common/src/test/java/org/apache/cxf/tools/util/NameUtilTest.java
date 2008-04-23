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
name|util
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

begin_class
specifier|public
class|class
name|NameUtilTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testMangleToClassName
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"Abc100Xyz"
argument_list|,
name|NameUtil
operator|.
name|mangleNameToClassName
argument_list|(
literal|"abc100xyz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"NameUtilTest"
argument_list|,
name|NameUtil
operator|.
name|mangleNameToClassName
argument_list|(
literal|"name_util_test"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"NameUtil"
argument_list|,
name|NameUtil
operator|.
name|mangleNameToClassName
argument_list|(
literal|"nameUtil"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Int"
argument_list|,
name|NameUtil
operator|.
name|mangleNameToClassName
argument_list|(
literal|"int"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMangleToVariableName
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"abc100Xyz"
argument_list|,
name|NameUtil
operator|.
name|mangleNameToVariableName
argument_list|(
literal|"abc100xyz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"nameUtilTest"
argument_list|,
name|NameUtil
operator|.
name|mangleNameToVariableName
argument_list|(
literal|"name_util_test"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"nameUtil"
argument_list|,
name|NameUtil
operator|.
name|mangleNameToVariableName
argument_list|(
literal|"NameUtil"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"int"
argument_list|,
name|NameUtil
operator|.
name|mangleNameToVariableName
argument_list|(
literal|"int"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsJavaIdentifier
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|NameUtil
operator|.
name|isJavaIdentifier
argument_list|(
literal|"int"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

