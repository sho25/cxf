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
name|oidc
operator|.
name|rp
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
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|core
operator|.
name|MediaType
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
name|core
operator|.
name|Response
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
name|impl
operator|.
name|MetadataMap
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
name|FormUtils
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
name|oauth2
operator|.
name|client
operator|.
name|Consumer
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
name|oidc
operator|.
name|common
operator|.
name|IdToken
import|;
end_import

begin_class
specifier|public
class|class
name|OidcIdTokenRequestFilter
implements|implements
name|ContainerRequestFilter
block|{
specifier|private
name|String
name|tokenFormParameter
init|=
literal|"id_token"
decl_stmt|;
specifier|private
name|IdTokenReader
name|idTokenReader
decl_stmt|;
specifier|private
name|Consumer
name|consumer
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|requestContext
parameter_list|)
throws|throws
name|IOException
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|form
init|=
name|toFormData
argument_list|(
name|requestContext
argument_list|)
decl_stmt|;
name|String
name|idTokenParamValue
init|=
name|form
operator|.
name|getFirst
argument_list|(
name|tokenFormParameter
argument_list|)
decl_stmt|;
if|if
condition|(
name|idTokenParamValue
operator|==
literal|null
condition|)
block|{
name|requestContext
operator|.
name|abortWith
argument_list|(
name|Response
operator|.
name|status
argument_list|(
literal|401
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|IdToken
name|idToken
init|=
name|idTokenReader
operator|.
name|getIdToken
argument_list|(
name|idTokenParamValue
argument_list|,
name|consumer
argument_list|)
decl_stmt|;
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|setContent
argument_list|(
name|IdToken
operator|.
name|class
argument_list|,
name|idToken
argument_list|)
expr_stmt|;
name|requestContext
operator|.
name|setSecurityContext
argument_list|(
operator|new
name|OidcSecurityContext
argument_list|(
name|idToken
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|toFormData
parameter_list|(
name|ContainerRequestContext
name|rc
parameter_list|)
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|requestState
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED_TYPE
operator|.
name|isCompatible
argument_list|(
name|rc
operator|.
name|getMediaType
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|body
init|=
name|FormUtils
operator|.
name|readBody
argument_list|(
name|rc
operator|.
name|getEntityStream
argument_list|()
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
name|FormUtils
operator|.
name|populateMapFromString
argument_list|(
name|requestState
argument_list|,
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
argument_list|,
name|body
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|requestState
return|;
block|}
specifier|public
name|void
name|setIdTokenReader
parameter_list|(
name|IdTokenReader
name|idTokenReader
parameter_list|)
block|{
name|this
operator|.
name|idTokenReader
operator|=
name|idTokenReader
expr_stmt|;
block|}
specifier|public
name|void
name|setTokenFormParameter
parameter_list|(
name|String
name|tokenFormParameter
parameter_list|)
block|{
name|this
operator|.
name|tokenFormParameter
operator|=
name|tokenFormParameter
expr_stmt|;
block|}
specifier|public
name|void
name|setConsumer
parameter_list|(
name|Consumer
name|consumer
parameter_list|)
block|{
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
block|}
block|}
end_class

end_unit

