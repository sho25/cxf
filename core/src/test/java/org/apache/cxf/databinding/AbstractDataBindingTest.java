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
name|databinding
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|AbstractDataBindingTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testNamespaceMapCheckDuplicates
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|testMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|testMap
operator|.
name|put
argument_list|(
literal|"urn:hello.there"
argument_list|,
literal|"ht"
argument_list|)
expr_stmt|;
name|testMap
operator|.
name|put
argument_list|(
literal|"urn:high.temperature"
argument_list|,
literal|"ht"
argument_list|)
expr_stmt|;
name|AbstractDataBinding
operator|.
name|checkNamespaceMap
argument_list|(
name|testMap
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNamespaceMapOK
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|testMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|testMap
operator|.
name|put
argument_list|(
literal|"urn:hello.there"
argument_list|,
literal|"ht"
argument_list|)
expr_stmt|;
name|testMap
operator|.
name|put
argument_list|(
literal|"urn:high.temperature"
argument_list|,
literal|"warm"
argument_list|)
expr_stmt|;
name|AbstractDataBinding
operator|.
name|checkNamespaceMap
argument_list|(
name|testMap
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

