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
name|jaxrs
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
name|jws
operator|.
name|JwsHeaders
import|;
end_import

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
name|jws
operator|.
name|JwsSignatureVerifier
import|;
end_import

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
name|jws
operator|.
name|JwsUtils
import|;
end_import

begin_class
specifier|public
class|class
name|AbstractJwsReaderProvider
block|{
specifier|private
name|JwsSignatureVerifier
name|sigVerifier
decl_stmt|;
specifier|private
name|String
name|defaultMediaType
decl_stmt|;
specifier|public
name|void
name|setSignatureVerifier
parameter_list|(
name|JwsSignatureVerifier
name|signatureVerifier
parameter_list|)
block|{
name|this
operator|.
name|sigVerifier
operator|=
name|signatureVerifier
expr_stmt|;
block|}
specifier|protected
name|JwsSignatureVerifier
name|getInitializedSigVerifier
parameter_list|(
name|JwsHeaders
name|headers
parameter_list|)
block|{
if|if
condition|(
name|sigVerifier
operator|!=
literal|null
condition|)
block|{
return|return
name|sigVerifier
return|;
block|}
return|return
name|JwsUtils
operator|.
name|loadSignatureVerifier
argument_list|(
name|headers
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|public
name|String
name|getDefaultMediaType
parameter_list|()
block|{
return|return
name|defaultMediaType
return|;
block|}
specifier|public
name|void
name|setDefaultMediaType
parameter_list|(
name|String
name|defaultMediaType
parameter_list|)
block|{
name|this
operator|.
name|defaultMediaType
operator|=
name|defaultMediaType
expr_stmt|;
block|}
block|}
end_class

end_unit

