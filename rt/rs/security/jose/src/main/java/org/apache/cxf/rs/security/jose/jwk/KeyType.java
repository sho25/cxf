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
name|jose
operator|.
name|jwk
package|;
end_package

begin_enum
specifier|public
enum|enum
name|KeyType
block|{
name|RSA
parameter_list|(
name|JsonWebKey
operator|.
name|KEY_TYPE_RSA
parameter_list|)
operator|,
constructor|EC(JsonWebKey.KEY_TYPE_ELLIPTIC
block|)
enum|,
name|OCTET
argument_list|(
name|JsonWebKey
operator|.
name|KEY_TYPE_OCTET
argument_list|)
enum|;
end_enum

begin_decl_stmt
specifier|private
specifier|final
name|String
name|type
decl_stmt|;
end_decl_stmt

begin_expr_stmt
name|KeyType
argument_list|(
name|String
name|type
argument_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
block|;     }
specifier|public
specifier|static
name|KeyType
name|getKeyType
argument_list|(
name|String
name|type
argument_list|)
block|{
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
end_expr_stmt

begin_elseif
elseif|else
if|if
condition|(
name|JsonWebKey
operator|.
name|KEY_TYPE_OCTET
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|OCTET
return|;
block|}
end_elseif

begin_else
else|else
block|{
return|return
name|valueOf
argument_list|(
name|type
argument_list|)
return|;
block|}
end_else

begin_function
unit|}     public
name|String
name|toString
parameter_list|()
block|{
return|return
name|type
return|;
block|}
end_function

unit|}
end_unit

