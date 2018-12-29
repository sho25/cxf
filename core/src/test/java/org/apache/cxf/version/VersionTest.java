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
name|version
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
name|VersionTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testLoadProperties
parameter_list|()
block|{
name|String
name|version
init|=
name|Version
operator|.
name|getCurrentVersion
argument_list|()
decl_stmt|;
name|String
name|token
init|=
literal|"${product.version}"
decl_stmt|;
name|assertFalse
argument_list|(
name|token
operator|.
name|equals
argument_list|(
name|version
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetVersion
parameter_list|()
block|{
name|String
name|completeVersion
init|=
name|Version
operator|.
name|getCompleteVersionString
argument_list|()
decl_stmt|;
name|String
name|currentVersion
init|=
name|Version
operator|.
name|getCurrentVersion
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|completeVersion
operator|.
name|contains
argument_list|(
name|currentVersion
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

