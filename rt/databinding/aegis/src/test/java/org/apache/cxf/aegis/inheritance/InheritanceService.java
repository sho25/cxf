begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  *   */
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
name|inheritance
package|;
end_package

begin_class
specifier|public
class|class
name|InheritanceService
block|{
specifier|public
name|AbstractUser
name|getEmployee
parameter_list|()
block|{
name|Employee
name|e
init|=
operator|new
name|Employee
argument_list|()
decl_stmt|;
name|e
operator|.
name|setDivision
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|e
operator|.
name|setName
argument_list|(
literal|"Dan D. Man"
argument_list|)
expr_stmt|;
return|return
name|e
return|;
block|}
specifier|public
name|void
name|receiveUser
parameter_list|(
name|AbstractUser
name|user
parameter_list|)
block|{
name|InheritancePOJOTest
operator|.
name|assertTrue
argument_list|(
name|user
operator|instanceof
name|Employee
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

