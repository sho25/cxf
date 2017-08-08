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
name|transport
operator|.
name|http
operator|.
name|auth
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|Base64Utility
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
name|PropertyUtils
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
name|configuration
operator|.
name|security
operator|.
name|AuthorizationPolicy
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

begin_class
specifier|public
specifier|final
class|class
name|DefaultBasicAuthSupplier
implements|implements
name|HttpAuthSupplier
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ENCODE_BASIC_AUTH_WITH_ISO8859
init|=
literal|"encode.basicauth.with.iso8859"
decl_stmt|;
specifier|public
name|DefaultBasicAuthSupplier
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|requiresRequestCaching
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
specifier|static
name|String
name|getBasicAuthHeader
parameter_list|(
name|String
name|userName
parameter_list|,
name|String
name|passwd
parameter_list|)
block|{
return|return
name|getBasicAuthHeader
argument_list|(
name|userName
argument_list|,
name|passwd
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getBasicAuthHeader
parameter_list|(
name|String
name|userName
parameter_list|,
name|String
name|passwd
parameter_list|,
name|boolean
name|useIso8859
parameter_list|)
block|{
name|String
name|userAndPass
init|=
name|userName
operator|+
literal|":"
operator|+
name|passwd
decl_stmt|;
name|byte
index|[]
name|authBytes
init|=
name|useIso8859
condition|?
name|userAndPass
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|ISO_8859_1
argument_list|)
else|:
name|userAndPass
operator|.
name|getBytes
argument_list|()
decl_stmt|;
return|return
literal|"Basic "
operator|+
name|Base64Utility
operator|.
name|encode
argument_list|(
name|authBytes
argument_list|)
return|;
block|}
specifier|public
name|String
name|getAuthorization
parameter_list|(
name|AuthorizationPolicy
name|authPolicy
parameter_list|,
name|URI
name|currentURI
parameter_list|,
name|Message
name|message
parameter_list|,
name|String
name|fullHeader
parameter_list|)
block|{
if|if
condition|(
name|authPolicy
operator|.
name|getUserName
argument_list|()
operator|!=
literal|null
operator|&&
name|authPolicy
operator|.
name|getPassword
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|boolean
name|encodeBasicAuthWithIso8859
init|=
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|ENCODE_BASIC_AUTH_WITH_ISO8859
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|getBasicAuthHeader
argument_list|(
name|authPolicy
operator|.
name|getUserName
argument_list|()
argument_list|,
name|authPolicy
operator|.
name|getPassword
argument_list|()
argument_list|,
name|encodeBasicAuthWithIso8859
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

