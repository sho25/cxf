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
name|oauth2
operator|.
name|auth
operator|.
name|saml
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
name|InputStream
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
name|core
operator|.
name|Form
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
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|Base64Exception
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
name|Base64UrlUtility
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
name|provider
operator|.
name|FormEncodingProvider
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
name|ExceptionUtils
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
name|HttpUtils
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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|saml
operator|.
name|Constants
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
name|saml
operator|.
name|SamlOAuthValidator
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
name|utils
operator|.
name|OAuthConstants
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
name|saml
operator|.
name|AbstractSamlInHandler
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
name|saml
operator|.
name|SAMLUtils
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
name|saml
operator|.
name|assertion
operator|.
name|Subject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|SamlAssertionWrapper
import|;
end_import

begin_class
specifier|public
class|class
name|Saml2BearerAuthHandler
extends|extends
name|AbstractSamlInHandler
block|{
specifier|private
name|FormEncodingProvider
argument_list|<
name|Form
argument_list|>
name|provider
init|=
operator|new
name|FormEncodingProvider
argument_list|<>
argument_list|(
literal|true
argument_list|)
decl_stmt|;
specifier|private
name|SamlOAuthValidator
name|samlOAuthValidator
init|=
operator|new
name|SamlOAuthValidator
argument_list|()
decl_stmt|;
specifier|public
name|Saml2BearerAuthHandler
parameter_list|()
block|{     }
specifier|public
name|void
name|setSamlOAuthValidator
parameter_list|(
name|SamlOAuthValidator
name|validator
parameter_list|)
block|{
name|samlOAuthValidator
operator|=
name|validator
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|context
parameter_list|)
block|{
name|Message
name|message
init|=
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|Form
name|form
init|=
name|readFormData
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|formData
init|=
name|form
operator|.
name|asMap
argument_list|()
decl_stmt|;
name|String
name|assertionType
init|=
name|formData
operator|.
name|getFirst
argument_list|(
name|Constants
operator|.
name|CLIENT_AUTH_ASSERTION_TYPE
argument_list|)
decl_stmt|;
name|String
name|decodedAssertionType
init|=
name|assertionType
operator|!=
literal|null
condition|?
name|HttpUtils
operator|.
name|urlDecode
argument_list|(
name|assertionType
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|decodedAssertionType
operator|==
literal|null
operator|||
operator|!
name|Constants
operator|.
name|CLIENT_AUTH_SAML2_BEARER
operator|.
name|equals
argument_list|(
name|decodedAssertionType
argument_list|)
condition|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
name|String
name|assertion
init|=
name|formData
operator|.
name|getFirst
argument_list|(
name|Constants
operator|.
name|CLIENT_AUTH_ASSERTION_PARAM
argument_list|)
decl_stmt|;
name|Element
name|token
init|=
name|readToken
argument_list|(
name|message
argument_list|,
name|assertion
argument_list|)
decl_stmt|;
name|String
name|clientId
init|=
name|formData
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_ID
argument_list|)
decl_stmt|;
name|validateToken
argument_list|(
name|message
argument_list|,
name|token
argument_list|,
name|clientId
argument_list|)
expr_stmt|;
name|formData
operator|.
name|remove
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_ID
argument_list|)
expr_stmt|;
name|formData
operator|.
name|remove
argument_list|(
name|Constants
operator|.
name|CLIENT_AUTH_ASSERTION_PARAM
argument_list|)
expr_stmt|;
name|formData
operator|.
name|remove
argument_list|(
name|Constants
operator|.
name|CLIENT_AUTH_ASSERTION_TYPE
argument_list|)
expr_stmt|;
comment|// restore input stream
try|try
block|{
name|FormUtils
operator|.
name|restoreForm
argument_list|(
name|provider
argument_list|,
name|form
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
specifier|private
name|Form
name|readFormData
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
return|return
name|FormUtils
operator|.
name|readForm
argument_list|(
name|provider
argument_list|,
name|message
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|Element
name|readToken
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|assertion
parameter_list|)
block|{
if|if
condition|(
name|assertion
operator|==
literal|null
condition|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
try|try
block|{
name|byte
index|[]
name|deflatedToken
init|=
name|Base64UrlUtility
operator|.
name|decode
argument_list|(
name|assertion
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|deflatedToken
argument_list|)
decl_stmt|;
return|return
name|readToken
argument_list|(
name|message
argument_list|,
name|is
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Base64Exception
name|ex
parameter_list|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|validateToken
parameter_list|(
name|Message
name|message
parameter_list|,
name|Element
name|element
parameter_list|,
name|String
name|clientId
parameter_list|)
block|{
name|SamlAssertionWrapper
name|wrapper
init|=
name|toWrapper
argument_list|(
name|element
argument_list|)
decl_stmt|;
comment|// The common SAML assertion validation:
comment|// signature, subject confirmation, etc
name|super
operator|.
name|validateToken
argument_list|(
name|message
argument_list|,
name|wrapper
argument_list|)
expr_stmt|;
comment|// This is specific to OAuth2 path
comment|// Introduce SAMLOAuth2Validator to be reused between auth and grant handlers
name|Subject
name|subject
init|=
name|SAMLUtils
operator|.
name|getSubject
argument_list|(
name|message
argument_list|,
name|wrapper
argument_list|)
decl_stmt|;
if|if
condition|(
name|subject
operator|.
name|getName
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
if|if
condition|(
name|clientId
operator|!=
literal|null
operator|&&
operator|!
name|clientId
operator|.
name|equals
argument_list|(
name|subject
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
comment|//TODO:  Attempt to map client_id to subject.getName()
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
name|samlOAuthValidator
operator|.
name|validate
argument_list|(
name|message
argument_list|,
name|wrapper
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_ID
argument_list|,
name|subject
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

