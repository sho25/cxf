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
name|javascript
operator|.
name|fortest
operator|.
name|aegis
package|;
end_package

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|BeanWithAnyTypeArray
block|{
specifier|private
name|String
name|string
decl_stmt|;
specifier|private
name|Object
index|[]
name|objects
decl_stmt|;
specifier|public
name|String
name|getString
parameter_list|()
block|{
return|return
name|string
return|;
block|}
specifier|public
name|void
name|setString
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|this
operator|.
name|string
operator|=
name|string
expr_stmt|;
block|}
specifier|public
name|Object
index|[]
name|getObjects
parameter_list|()
block|{
return|return
name|objects
return|;
block|}
specifier|public
name|void
name|setObjects
parameter_list|(
name|Object
index|[]
name|objects
parameter_list|)
block|{
name|this
operator|.
name|objects
operator|=
name|objects
expr_stmt|;
block|}
block|}
end_class

end_unit

