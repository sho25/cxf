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
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

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
name|WebApplicationException
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|ReaderInterceptor
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
name|ReaderInterceptorContext
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
name|rs
operator|.
name|security
operator|.
name|httpsignature
operator|.
name|DigestVerifier
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
name|exception
operator|.
name|DigestFailureException
import|;
end_import

begin_comment
comment|/**  * RS CXF Reader Interceptor which extract the body of the message and verifies the digest  */
end_comment

begin_class
annotation|@
name|Provider
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
name|VerifyDigestInterceptor
implements|implements
name|ReaderInterceptor
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
name|VerifyDigestInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|DigestVerifier
name|digestVerifier
decl_stmt|;
specifier|private
name|boolean
name|enabled
decl_stmt|;
specifier|public
name|VerifyDigestInterceptor
parameter_list|()
block|{
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setDigestVerifier
argument_list|(
operator|new
name|DigestVerifier
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|aroundReadFrom
parameter_list|(
name|ReaderInterceptorContext
name|context
parameter_list|)
throws|throws
name|IOException
throws|,
name|WebApplicationException
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
literal|"Verify signature reader interceptor is disabled"
argument_list|)
expr_stmt|;
return|return
name|context
operator|.
name|proceed
argument_list|()
return|;
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"Starting interceptor message verification process"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|responseHeaders
init|=
name|context
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|byte
index|[]
name|messageBody
init|=
name|extractMessageBody
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|digestVerifier
operator|.
name|inspectDigest
argument_list|(
name|messageBody
argument_list|,
name|responseHeaders
argument_list|)
expr_stmt|;
name|context
operator|.
name|setInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|messageBody
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Finished interceptor message verification process"
argument_list|)
expr_stmt|;
return|return
name|context
operator|.
name|proceed
argument_list|()
return|;
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
name|DigestVerifier
name|getDigestVerifier
parameter_list|()
block|{
return|return
name|digestVerifier
return|;
block|}
specifier|public
name|void
name|setDigestVerifier
parameter_list|(
name|DigestVerifier
name|digestVerifier
parameter_list|)
block|{
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|digestVerifier
argument_list|)
expr_stmt|;
name|this
operator|.
name|digestVerifier
operator|=
name|digestVerifier
expr_stmt|;
block|}
specifier|private
name|byte
index|[]
name|extractMessageBody
parameter_list|(
name|ReaderInterceptorContext
name|context
parameter_list|)
block|{
try|try
init|(
name|InputStream
name|is
init|=
name|context
operator|.
name|getInputStream
argument_list|()
init|)
block|{
return|return
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|is
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DigestFailureException
argument_list|(
literal|"failed to validate the digest"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit
