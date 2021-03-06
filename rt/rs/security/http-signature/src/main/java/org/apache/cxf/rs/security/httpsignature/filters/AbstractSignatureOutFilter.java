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
name|Properties
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
name|CastUtils
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
name|phase
operator|.
name|PhaseInterceptorChain
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
name|HTTPSignatureConstants
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
name|SignatureException
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
name|provider
operator|.
name|KeyProvider
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
name|utils
operator|.
name|DefaultSignatureConstants
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
name|utils
operator|.
name|KeyManagementUtils
import|;
end_import

begin_comment
comment|/**  * RS CXF abstract Filter which signs outgoing messages. It does not create a digest header  */
end_comment

begin_class
specifier|abstract
class|class
name|AbstractSignatureOutFilter
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
name|AbstractSignatureOutFilter
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
name|AbstractSignatureOutFilter
parameter_list|()
block|{
name|this
operator|.
name|enabled
operator|=
literal|true
expr_stmt|;
block|}
specifier|protected
name|void
name|performSignature
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|String
name|uriPath
parameter_list|,
name|String
name|httpMethod
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
name|messageSigner
operator|=
name|createMessageSigner
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|headers
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
name|headers
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
name|uriPath
argument_list|,
name|httpMethod
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|SignatureException
argument_list|(
literal|"Error creating signature"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
name|headers
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
operator|.
name|trim
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
specifier|private
name|MessageSigner
name|createMessageSigner
parameter_list|()
block|{
name|Properties
name|props
init|=
name|KeyManagementUtils
operator|.
name|loadSignatureOutProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|props
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SignatureException
argument_list|(
literal|"Signature properties are not configured correctly"
argument_list|)
throw|;
block|}
name|Message
name|m
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|KeyProvider
name|keyProvider
init|=
name|keyId
lambda|->
name|KeyManagementUtils
operator|.
name|loadPrivateKey
argument_list|(
name|m
argument_list|,
name|props
argument_list|)
decl_stmt|;
name|String
name|signatureAlgorithm
init|=
operator|(
name|String
operator|)
name|m
operator|.
name|getContextualProperty
argument_list|(
name|HTTPSignatureConstants
operator|.
name|RSSEC_SIGNATURE_ALGORITHM
argument_list|)
decl_stmt|;
if|if
condition|(
name|signatureAlgorithm
operator|==
literal|null
condition|)
block|{
name|signatureAlgorithm
operator|=
name|DefaultSignatureConstants
operator|.
name|SIGNING_ALGORITHM
expr_stmt|;
block|}
name|String
name|keyId
init|=
operator|(
name|String
operator|)
name|m
operator|.
name|getContextualProperty
argument_list|(
name|HTTPSignatureConstants
operator|.
name|RSSEC_HTTP_SIGNATURE_KEY_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyId
operator|==
literal|null
condition|)
block|{
name|keyId
operator|=
name|props
operator|.
name|getProperty
argument_list|(
name|HTTPSignatureConstants
operator|.
name|RSSEC_HTTP_SIGNATURE_KEY_ID
argument_list|)
expr_stmt|;
if|if
condition|(
name|keyId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SignatureException
argument_list|(
literal|"The signature key id is a required configuration property"
argument_list|)
throw|;
block|}
block|}
name|List
argument_list|<
name|String
argument_list|>
name|signedHeaders
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|m
operator|.
name|getContextualProperty
argument_list|(
name|HTTPSignatureConstants
operator|.
name|RSSEC_HTTP_SIGNATURE_OUT_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|signedHeaders
operator|==
literal|null
condition|)
block|{
name|signedHeaders
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|MessageSigner
argument_list|(
name|signatureAlgorithm
argument_list|,
name|keyProvider
argument_list|,
name|keyId
argument_list|,
name|signedHeaders
argument_list|)
return|;
block|}
block|}
end_class

end_unit

