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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|ClientRequestFilter
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
comment|/**  * RS CXF client Filter which signs outgoing messages. It does not create a digest header  *  */
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
name|CreateSignatureClientFilter
implements|implements
name|ClientRequestFilter
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
name|CreateSignatureClientFilter
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
name|ClientRequestContext
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
literal|"Create signature filter is disabled"
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
name|Object
argument_list|>
name|requestHeaders
init|=
name|requestCtx
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
if|if
condition|(
name|requestHeaders
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
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|convertedHeaders
init|=
name|convertHeaders
argument_list|(
name|requestHeaders
argument_list|)
decl_stmt|;
try|try
block|{
name|messageSigner
operator|.
name|sign
argument_list|(
name|convertedHeaders
argument_list|,
name|requestCtx
operator|.
name|getUri
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
name|requestHeaders
operator|.
name|put
argument_list|(
literal|"Signature"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|convertedHeaders
operator|.
name|get
argument_list|(
literal|"Signature"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Finished filter message verification process"
argument_list|)
expr_stmt|;
block|}
comment|// Convert the headers from List<Object> -> List<String>
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|convertHeaders
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestHeaders
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|convertedHeaders
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|requestHeaders
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|entry
range|:
name|requestHeaders
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|convertedHeaders
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|o
lambda|->
name|o
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|convertedHeaders
return|;
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
