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
name|httpsignature
operator|.
name|filters
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|Priorities
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
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
name|ext
operator|.
name|Provider
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|httpsignature
operator|.
name|MessageSigner
import|;
end_import

begin_comment
comment|/**  * RS CXF Filter which signs outgoing messages. It does not create a digest header  *  */
end_comment

begin_class
annotation|@
name|Provider
annotation|@
name|PreMatching
annotation|@
name|Priority
argument_list|(
name|Priorities
operator|.
name|AUTHENTICATION
argument_list|)
specifier|public
specifier|final
class|class
name|CreateSignatureFilter
implements|implements
name|ContainerRequestFilter
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|VerifySignatureFilter
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|MessageSigner
name|messageSigner
decl_stmt|;
specifier|private
name|boolean
name|enabled
decl_stmt|;
specifier|public
name|CreateSignatureFilter
parameter_list|()
block|{
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|requestCtx
parameter_list|)
block|{
if|if
condition|(
operator|!
name|enabled
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Verify signature filter is disabled"
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|messageSigner
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Message signer cannot be null"
argument_list|)
expr_stmt|;
return|return;
block|}
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|responseHeaders
init|=
name|requestCtx
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
if|if
condition|(
name|responseHeaders
operator|.
name|containsKey
argument_list|(
literal|"Signature"
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Message already contains a signature"
argument_list|)
expr_stmt|;
return|return;
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"Starting filter message signing process"
argument_list|)
expr_stmt|;
try|try
block|{
name|messageSigner
operator|.
name|sign
argument_list|(
name|responseHeaders
argument_list|,
name|requestCtx
operator|.
name|getUriInfo
argument_list|()
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|,
name|requestCtx
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"Finished filter message verification process"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MessageSigner
name|getMessageSigner
parameter_list|()
block|{
return|return
name|messageSigner
return|;
block|}
specifier|public
name|void
name|setMessageSigner
parameter_list|(
name|MessageSigner
name|messageSigner
parameter_list|)
block|{
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|messageSigner
argument_list|)
expr_stmt|;
name|this
operator|.
name|messageSigner
operator|=
name|messageSigner
expr_stmt|;
block|}
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
block|}
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
block|}
end_class

end_unit

