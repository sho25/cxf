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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_comment
comment|/**  * Base Token representation  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AccessToken
block|{
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"access_token"
argument_list|)
specifier|private
name|String
name|tokenKey
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"token_type"
argument_list|)
specifier|private
name|AccessTokenType
name|tokenType
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
decl_stmt|;
specifier|protected
name|AccessToken
parameter_list|(
name|AccessTokenType
name|type
parameter_list|,
name|String
name|tokenKey
parameter_list|)
block|{
name|this
operator|.
name|tokenType
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|tokenKey
operator|=
name|tokenKey
expr_stmt|;
block|}
specifier|public
name|AccessTokenType
name|getTokenType
parameter_list|()
block|{
return|return
name|tokenType
return|;
block|}
comment|/**      * Returns the token key      * @return the key      */
specifier|public
name|String
name|getTokenKey
parameter_list|()
block|{
return|return
name|tokenKey
return|;
block|}
specifier|public
name|void
name|setParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
parameter_list|)
block|{
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getParameters
parameter_list|()
block|{
return|return
name|parameters
return|;
block|}
block|}
end_class

end_unit

