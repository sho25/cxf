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
name|AbstractJwsJsonReaderProvider
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
name|AbstractJwsJsonReaderProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSSEC_SIGNATURE_IN_LIST_PROPS
init|=
literal|"rs.security.signature.in.list.properties"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSSEC_SIGNATURE_LIST_PROPS
init|=
literal|"rs.security.signature.list.properties"
decl_stmt|;
specifier|private
name|List
argument_list|<
name|JwsSignatureVerifier
argument_list|>
name|sigVerifiers
decl_stmt|;
specifier|private
name|String
name|defaultMediaType
decl_stmt|;
specifier|private
name|boolean
name|strictVerification
decl_stmt|;
specifier|public
name|void
name|setSignatureVerifier
parameter_list|(
name|JwsSignatureVerifier
name|signatureVerifier
parameter_list|)
block|{
name|setSignatureVerifiers
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|signatureVerifier
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSignatureVerifiers
parameter_list|(
name|List
argument_list|<
name|JwsSignatureVerifier
argument_list|>
name|signatureVerifiers
parameter_list|)
block|{
name|this
operator|.
name|sigVerifiers
operator|=
name|signatureVerifiers
expr_stmt|;
block|}
specifier|protected
name|List
argument_list|<
name|JwsSignatureVerifier
argument_list|>
name|getInitializedSigVerifiers
parameter_list|()
block|{
if|if
condition|(
name|sigVerifiers
operator|!=
literal|null
condition|)
block|{
return|return
name|sigVerifiers
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
name|RSSEC_SIGNATURE_IN_LIST_PROPS
argument_list|,
name|RSSEC_SIGNATURE_LIST_PROPS
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
literal|"JWS JSON init properties resource is not identified"
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
name|JwsSignatureVerifier
argument_list|>
name|theSigVerifiers
init|=
operator|new
name|LinkedList
argument_list|<
name|JwsSignatureVerifier
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
name|theSigVerifiers
operator|.
name|addAll
argument_list|(
name|JwsUtils
operator|.
name|loadSignatureVerifiers
argument_list|(
name|propLoc
argument_list|,
name|m
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|theSigVerifiers
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
specifier|public
name|boolean
name|isStrictVerification
parameter_list|()
block|{
return|return
name|strictVerification
return|;
block|}
specifier|public
name|void
name|setStrictVerification
parameter_list|(
name|boolean
name|strictVerification
parameter_list|)
block|{
name|this
operator|.
name|strictVerification
operator|=
name|strictVerification
expr_stmt|;
block|}
block|}
end_class

end_unit
