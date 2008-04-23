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

begin_class
specifier|public
class|class
name|JavaInterfaceTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSetFullClassName
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|fullName
init|=
literal|"org.apache.cxf.tools.common.model.JavaInterface"
decl_stmt|;
name|JavaInterface
name|intf
init|=
operator|new
name|JavaInterface
argument_list|()
decl_stmt|;
name|intf
operator|.
name|setFullClassName
argument_list|(
name|fullName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.cxf.tools.common.model"
argument_list|,
name|intf
operator|.
name|getPackageName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"JavaInterface"
argument_list|,
name|intf
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

