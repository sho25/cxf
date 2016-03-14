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
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|LinkedList
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
name|logging
operator|.
name|Logger
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|message
operator|.
name|MessageUtils
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
name|JoseConstants
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
name|JweEncryptionProvider
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
name|JweUtils
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
name|JwsJsonProducer
import|;
end_import

begin_class
specifier|public
class|class
name|AbstractJweJsonWriterProvider
block|{
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|AbstractJweJsonWriterProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|List
argument_list|<
name|JweEncryptionProvider
argument_list|>
name|encProviders
decl_stmt|;
specifier|public
name|void
name|setEncryptionProvider
parameter_list|(
name|JweEncryptionProvider
name|provider
parameter_list|)
block|{
name|setEncryptionProviders
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|provider
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setEncryptionProviders
parameter_list|(
name|List
argument_list|<
name|JweEncryptionProvider
argument_list|>
name|providers
parameter_list|)
block|{
name|this
operator|.
name|encProviders
operator|=
name|providers
expr_stmt|;
block|}
specifier|protected
name|List
argument_list|<
name|JweEncryptionProvider
argument_list|>
name|getInitializedEncryptionProviders
parameter_list|()
block|{
if|if
condition|(
name|encProviders
operator|!=
literal|null
condition|)
block|{
return|return
name|encProviders
return|;
block|}
name|Message
name|m
init|=
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|Object
name|propLocsProp
init|=
name|MessageUtils
operator|.
name|getContextualProperty
argument_list|(
name|m
argument_list|,
name|JoseConstants
operator|.
name|RSSEC_ENCRYPTION_OUT_PROPS
argument_list|,
name|JoseConstants
operator|.
name|RSSEC_ENCRYPTION_PROPS
argument_list|)
decl_stmt|;
if|if
condition|(
name|propLocsProp
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"JWE JSON init properties resource is not identified"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JwsException
argument_list|(
name|JwsException
operator|.
name|Error
operator|.
name|NO_INIT_PROPERTIES
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|propLocs
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|propLocsProp
operator|instanceof
name|String
condition|)
block|{
name|String
index|[]
name|props
init|=
operator|(
operator|(
name|String
operator|)
name|propLocsProp
operator|)
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|propLocs
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|propLocs
operator|=
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
name|propLocsProp
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|JweEncryptionProvider
argument_list|>
name|theEncProviders
init|=
operator|new
name|LinkedList
argument_list|<
name|JweEncryptionProvider
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|propLoc
range|:
name|propLocs
control|)
block|{
name|theEncProviders
operator|.
name|addAll
argument_list|(
name|JweUtils
operator|.
name|loadJweEncryptionProviders
argument_list|(
name|propLoc
argument_list|,
name|m
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|theEncProviders
return|;
block|}
specifier|protected
name|void
name|writeJws
parameter_list|(
name|JwsJsonProducer
name|p
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|bytes
init|=
name|StringUtils
operator|.
name|toBytesUTF8
argument_list|(
name|p
operator|.
name|getJwsJsonSignedDocument
argument_list|()
argument_list|)
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|bytes
argument_list|)
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
