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
name|systest
operator|.
name|aegis
package|;
end_package

begin_class
specifier|public
class|class
name|BeanWithCharacter
block|{
specifier|private
name|Character
name|character
decl_stmt|;
specifier|private
name|char
name|primitiveCharacter
decl_stmt|;
specifier|public
name|Character
name|getCharacter
parameter_list|()
block|{
return|return
name|character
return|;
block|}
specifier|public
name|void
name|setCharacter
parameter_list|(
name|Character
name|character
parameter_list|)
block|{
name|this
operator|.
name|character
operator|=
name|character
expr_stmt|;
block|}
specifier|public
name|char
name|getPrimitiveChar
parameter_list|()
block|{
return|return
name|primitiveCharacter
return|;
block|}
specifier|public
name|void
name|setPrimitiveChar
parameter_list|(
name|char
name|pchar
parameter_list|)
block|{
name|this
operator|.
name|primitiveCharacter
operator|=
name|pchar
expr_stmt|;
block|}
block|}
end_class

end_unit

