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
name|fortest
operator|.
name|cxf1450
package|;
end_package

begin_class
specifier|public
class|class
name|Name
block|{
specifier|private
name|String
name|firstName
init|=
literal|""
decl_stmt|;
specifier|private
name|String
name|lastName
init|=
literal|""
decl_stmt|;
name|Name
parameter_list|()
block|{     }
specifier|public
name|String
name|getFirstName
parameter_list|()
block|{
return|return
name|firstName
return|;
block|}
specifier|public
name|void
name|setFirstName
parameter_list|(
name|String
name|firstName
parameter_list|)
block|{
name|this
operator|.
name|firstName
operator|=
name|firstName
expr_stmt|;
block|}
specifier|public
name|String
name|getLastName
parameter_list|()
block|{
return|return
name|lastName
return|;
block|}
specifier|public
name|void
name|setLastName
parameter_list|(
name|String
name|lastName
parameter_list|)
block|{
name|this
operator|.
name|lastName
operator|=
name|lastName
expr_stmt|;
block|}
block|}
end_class

end_unit

