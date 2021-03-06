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
name|ws
operator|.
name|policy
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Assertion
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|AssertionInfo
block|{
specifier|private
name|boolean
name|asserted
decl_stmt|;
specifier|private
specifier|final
name|Assertion
name|assertion
decl_stmt|;
specifier|private
name|String
name|errorMessage
decl_stmt|;
specifier|public
name|AssertionInfo
parameter_list|(
name|Assertion
name|a
parameter_list|)
block|{
name|assertion
operator|=
name|a
expr_stmt|;
block|}
specifier|public
name|boolean
name|isAsserted
parameter_list|()
block|{
return|return
name|asserted
return|;
block|}
specifier|public
name|void
name|setAsserted
parameter_list|(
name|boolean
name|a
parameter_list|)
block|{
name|asserted
operator|=
name|a
expr_stmt|;
block|}
specifier|public
name|void
name|setNotAsserted
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|asserted
operator|=
literal|false
expr_stmt|;
name|errorMessage
operator|=
name|message
expr_stmt|;
block|}
specifier|public
name|String
name|getErrorMessage
parameter_list|()
block|{
return|return
name|errorMessage
return|;
block|}
specifier|public
name|Assertion
name|getAssertion
parameter_list|()
block|{
return|return
name|assertion
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|assertion
operator|.
name|getName
argument_list|()
operator|+
literal|":"
operator|+
name|asserted
return|;
block|}
block|}
end_class

end_unit

