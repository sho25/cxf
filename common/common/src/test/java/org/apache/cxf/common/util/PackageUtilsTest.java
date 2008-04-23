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
name|common
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
name|PackageUtilsTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testgetPackageName
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|packageName
init|=
name|PackageUtils
operator|.
name|getPackageName
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should get same packageName"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|packageName
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetPackageName
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|className
init|=
literal|"HelloWorld"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should return empty string"
argument_list|,
literal|""
argument_list|,
name|PackageUtils
operator|.
name|getPackageName
argument_list|(
name|className
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

