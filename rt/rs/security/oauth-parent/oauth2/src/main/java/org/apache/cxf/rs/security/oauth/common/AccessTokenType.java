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
name|rs
operator|.
name|security
operator|.
name|oauth
operator|.
name|common
package|;
end_package

begin_enum
specifier|public
enum|enum
name|AccessTokenType
block|{
name|BEARER
argument_list|(
literal|"bearer"
argument_list|)
block|,
name|MAC
argument_list|(
literal|"mac"
argument_list|)
block|;
specifier|private
name|String
name|type
decl_stmt|;
specifier|private
name|AccessTokenType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
specifier|public
specifier|static
name|AccessTokenType
name|fromString
parameter_list|(
name|String
name|str
parameter_list|)
block|{
if|if
condition|(
literal|"bearer"
operator|.
name|equals
argument_list|(
name|str
argument_list|)
condition|)
block|{
return|return
name|BEARER
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|str
argument_list|)
throw|;
block|}
block|}
specifier|public
name|String
name|getTokenType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|type
return|;
block|}
block|}
end_enum

end_unit

