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
operator|.
name|multipart
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
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
name|jaxrs
operator|.
name|ext
operator|.
name|multipart
operator|.
name|MultipartInputFilter
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
name|jaxrs
operator|.
name|utils
operator|.
name|JAXRSUtils
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
name|jaxrs
operator|.
name|utils
operator|.
name|multipart
operator|.
name|AttachmentUtils
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
name|message
operator|.
name|Message
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

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractJwsMultipartVerificationFilter
block|{
specifier|private
name|JwsSignatureVerifier
name|sigVerifier
decl_stmt|;
specifier|private
name|boolean
name|useJwsJsonSignatureFormat
decl_stmt|;
specifier|private
name|boolean
name|bufferPayload
decl_stmt|;
specifier|public
name|void
name|setSigVerifier
parameter_list|(
name|JwsSignatureVerifier
name|sigVerifier
parameter_list|)
block|{
name|this
operator|.
name|sigVerifier
operator|=
name|sigVerifier
expr_stmt|;
block|}
specifier|protected
name|void
name|addMultipartFilterIfNeeded
parameter_list|(
name|MediaType
name|contentType
parameter_list|)
block|{
if|if
condition|(
name|contentType
operator|!=
literal|null
operator|&&
name|contentType
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
literal|"multipart"
argument_list|)
condition|)
block|{
name|Message
name|m
init|=
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|MultipartInputFilter
name|jwsFilter
init|=
operator|new
name|JwsMultipartSignatureInFilter
argument_list|(
name|m
argument_list|,
name|sigVerifier
argument_list|,
name|bufferPayload
argument_list|,
name|useJwsJsonSignatureFormat
argument_list|)
decl_stmt|;
name|AttachmentUtils
operator|.
name|addMultipartInFilter
argument_list|(
name|jwsFilter
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setUseJwsJsonSignatureFormat
parameter_list|(
name|boolean
name|useJwsJsonSignatureFormat
parameter_list|)
block|{
name|this
operator|.
name|useJwsJsonSignatureFormat
operator|=
name|useJwsJsonSignatureFormat
expr_stmt|;
block|}
specifier|public
name|void
name|setBufferPayload
parameter_list|(
name|boolean
name|bufferPayload
parameter_list|)
block|{
name|this
operator|.
name|bufferPayload
operator|=
name|bufferPayload
expr_stmt|;
block|}
block|}
end_class

end_unit

