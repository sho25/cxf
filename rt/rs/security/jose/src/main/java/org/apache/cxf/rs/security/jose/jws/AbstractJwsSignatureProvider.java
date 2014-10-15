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
name|jws
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
name|JoseHeaders
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractJwsSignatureProvider
implements|implements
name|JwsSignatureProvider
block|{
specifier|private
name|String
name|algorithm
decl_stmt|;
specifier|protected
name|AbstractJwsSignatureProvider
parameter_list|(
name|String
name|algo
parameter_list|)
block|{
name|this
operator|.
name|algorithm
operator|=
name|algo
expr_stmt|;
block|}
specifier|protected
name|JoseHeaders
name|prepareHeaders
parameter_list|(
name|JoseHeaders
name|headers
parameter_list|)
block|{
if|if
condition|(
name|headers
operator|==
literal|null
condition|)
block|{
name|headers
operator|=
operator|new
name|JoseHeaders
argument_list|()
expr_stmt|;
block|}
name|String
name|algo
init|=
name|headers
operator|.
name|getAlgorithm
argument_list|()
decl_stmt|;
if|if
condition|(
name|algo
operator|!=
literal|null
condition|)
block|{
name|checkAlgorithm
argument_list|(
name|algo
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|checkAlgorithm
argument_list|(
name|algorithm
argument_list|)
expr_stmt|;
name|headers
operator|.
name|setAlgorithm
argument_list|(
name|algorithm
argument_list|)
expr_stmt|;
block|}
return|return
name|headers
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getAlgorithm
parameter_list|()
block|{
return|return
name|algorithm
return|;
block|}
annotation|@
name|Override
specifier|public
name|JwsSignature
name|createJwsSignature
parameter_list|(
name|JoseHeaders
name|headers
parameter_list|)
block|{
return|return
name|doCreateJwsSignature
argument_list|(
name|prepareHeaders
argument_list|(
name|headers
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
specifier|abstract
name|JwsSignature
name|doCreateJwsSignature
parameter_list|(
name|JoseHeaders
name|headers
parameter_list|)
function_decl|;
specifier|protected
name|void
name|checkAlgorithm
parameter_list|(
name|String
name|algo
parameter_list|)
block|{
if|if
condition|(
name|algo
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

