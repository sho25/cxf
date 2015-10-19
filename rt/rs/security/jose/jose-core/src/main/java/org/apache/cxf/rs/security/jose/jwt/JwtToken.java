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
name|jwt
package|;
end_package

begin_import
import|import
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
name|common
operator|.
name|JoseHeaders
import|;
end_import

begin_class
specifier|public
class|class
name|JwtToken
block|{
specifier|private
name|JoseHeaders
name|headers
decl_stmt|;
specifier|private
name|JwtClaims
name|claims
decl_stmt|;
specifier|public
name|JwtToken
parameter_list|(
name|JwtClaims
name|claims
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|JoseHeaders
argument_list|()
block|{ }
argument_list|,
name|claims
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JwtToken
parameter_list|(
name|JoseHeaders
name|headers
parameter_list|,
name|JwtClaims
name|claims
parameter_list|)
block|{
name|this
operator|.
name|headers
operator|=
name|headers
expr_stmt|;
name|this
operator|.
name|claims
operator|=
name|claims
expr_stmt|;
block|}
specifier|public
name|JoseHeaders
name|getHeaders
parameter_list|()
block|{
return|return
name|headers
return|;
block|}
specifier|public
name|JwtClaims
name|getClaims
parameter_list|()
block|{
return|return
name|claims
return|;
block|}
specifier|public
name|Object
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|headers
operator|.
name|getHeader
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|Object
name|getClaim
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|claims
operator|.
name|getClaim
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|headers
operator|.
name|hashCode
argument_list|()
operator|+
literal|37
operator|*
name|claims
operator|.
name|hashCode
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
return|return
name|obj
operator|instanceof
name|JwtToken
operator|&&
operator|(
operator|(
name|JwtToken
operator|)
name|obj
operator|)
operator|.
name|headers
operator|.
name|equals
argument_list|(
name|this
operator|.
name|headers
argument_list|)
operator|&&
operator|(
operator|(
name|JwtToken
operator|)
name|obj
operator|)
operator|.
name|claims
operator|.
name|equals
argument_list|(
name|this
operator|.
name|claims
argument_list|)
return|;
block|}
block|}
end_class

end_unit
