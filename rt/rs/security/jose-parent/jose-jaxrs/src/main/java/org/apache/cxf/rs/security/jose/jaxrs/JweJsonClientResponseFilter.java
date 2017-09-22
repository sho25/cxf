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
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Priority
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|ClientRequestContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|ClientResponseContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|ClientResponseFilter
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
name|common
operator|.
name|JoseUtils
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
name|jwe
operator|.
name|JweDecryptionOutput
import|;
end_import

begin_class
annotation|@
name|Priority
argument_list|(
name|Priorities
operator|.
name|JWE_CLIENT_READ_PRIORITY
argument_list|)
specifier|public
class|class
name|JweJsonClientResponseFilter
extends|extends
name|AbstractJweJsonDecryptingFilter
implements|implements
name|ClientResponseFilter
block|{
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ClientRequestContext
name|req
parameter_list|,
name|ClientResponseContext
name|res
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|isCheckEmptyStream
argument_list|()
operator|&&
operator|!
name|res
operator|.
name|hasEntity
argument_list|()
condition|)
block|{
return|return;
block|}
name|JweDecryptionOutput
name|out
init|=
name|decrypt
argument_list|(
name|res
operator|.
name|getEntityStream
argument_list|()
argument_list|)
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
name|out
operator|.
name|getContent
argument_list|()
decl_stmt|;
name|res
operator|.
name|setEntityStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|bytes
argument_list|)
argument_list|)
expr_stmt|;
name|res
operator|.
name|getHeaders
argument_list|()
operator|.
name|putSingle
argument_list|(
literal|"Content-Length"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|bytes
operator|.
name|length
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|ct
init|=
name|JoseUtils
operator|.
name|checkContentType
argument_list|(
name|out
operator|.
name|getHeaders
argument_list|()
operator|.
name|getContentType
argument_list|()
argument_list|,
name|getDefaultMediaType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ct
operator|!=
literal|null
condition|)
block|{
name|res
operator|.
name|getHeaders
argument_list|()
operator|.
name|putSingle
argument_list|(
literal|"Content-Type"
argument_list|,
name|ct
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|super
operator|.
name|isValidateHttpHeaders
argument_list|()
condition|)
block|{
name|super
operator|.
name|validateHttpHeadersIfNeeded
argument_list|(
name|res
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|out
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

