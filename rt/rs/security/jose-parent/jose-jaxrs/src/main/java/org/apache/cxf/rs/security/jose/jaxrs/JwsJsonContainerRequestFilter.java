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
name|HttpMethod
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
name|container
operator|.
name|ContainerRequestContext
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
name|container
operator|.
name|ContainerRequestFilter
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
name|container
operator|.
name|PreMatching
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
name|helpers
operator|.
name|IOUtils
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
name|jws
operator|.
name|JwsException
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
name|JwsJsonConsumer
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
name|JwsJsonSignatureEntry
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
annotation|@
name|PreMatching
annotation|@
name|Priority
argument_list|(
name|Priorities
operator|.
name|JWS_SERVER_READ_PRIORITY
argument_list|)
specifier|public
class|class
name|JwsJsonContainerRequestFilter
extends|extends
name|AbstractJwsJsonReaderProvider
implements|implements
name|ContainerRequestFilter
block|{
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|context
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|HttpMethod
operator|.
name|GET
operator|.
name|equals
argument_list|(
name|context
operator|.
name|getMethod
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
name|JwsSignatureVerifier
name|theSigVerifier
init|=
name|getInitializedSigVerifier
argument_list|()
decl_stmt|;
name|JwsJsonConsumer
name|c
init|=
operator|new
name|JwsJsonConsumer
argument_list|(
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|context
operator|.
name|getEntityStream
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|validate
argument_list|(
name|c
argument_list|,
name|theSigVerifier
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JwsException
name|ex
parameter_list|)
block|{
name|context
operator|.
name|abortWith
argument_list|(
name|JAXRSUtils
operator|.
name|toResponse
argument_list|(
literal|400
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|byte
index|[]
name|bytes
init|=
name|c
operator|.
name|getDecodedJwsPayloadBytes
argument_list|()
decl_stmt|;
name|context
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
name|context
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
comment|// the list is guaranteed to be non-empty
name|JwsJsonSignatureEntry
name|sigEntry
init|=
name|c
operator|.
name|getSignatureEntries
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|ct
init|=
name|JoseUtils
operator|.
name|checkContentType
argument_list|(
name|sigEntry
operator|.
name|getUnionHeader
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
name|context
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
block|}
block|}
end_class

end_unit

