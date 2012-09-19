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
name|jaxrs
operator|.
name|client
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|UriBuilder
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
name|jaxrs
operator|.
name|impl
operator|.
name|MetadataMap
import|;
end_import

begin_comment
comment|/**  * Keeps the client state such as the baseURI, currentURI, requestHeaders, current response  *  */
end_comment

begin_class
specifier|public
class|class
name|LocalClientState
implements|implements
name|ClientState
block|{
specifier|private
specifier|static
specifier|final
name|String
name|HTTP_SCHEME
init|=
literal|"http"
decl_stmt|;
specifier|private
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|requestHeaders
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
specifier|private
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|templates
decl_stmt|;
specifier|private
name|Response
name|response
decl_stmt|;
specifier|private
name|URI
name|baseURI
decl_stmt|;
specifier|private
name|UriBuilder
name|currentBuilder
decl_stmt|;
specifier|public
name|LocalClientState
parameter_list|()
block|{              }
specifier|public
name|LocalClientState
parameter_list|(
name|URI
name|baseURI
parameter_list|)
block|{
name|this
operator|.
name|baseURI
operator|=
name|baseURI
expr_stmt|;
name|String
name|scheme
init|=
name|baseURI
operator|.
name|getScheme
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|scheme
argument_list|)
operator|&&
name|scheme
operator|.
name|startsWith
argument_list|(
name|HTTP_SCHEME
argument_list|)
condition|)
block|{
name|this
operator|.
name|currentBuilder
operator|=
name|UriBuilder
operator|.
name|fromUri
argument_list|(
name|baseURI
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|currentBuilder
operator|=
name|UriBuilder
operator|.
name|fromUri
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|LocalClientState
parameter_list|(
name|LocalClientState
name|cs
parameter_list|)
block|{
name|this
operator|.
name|requestHeaders
operator|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|cs
operator|.
name|requestHeaders
argument_list|)
expr_stmt|;
name|this
operator|.
name|templates
operator|=
name|cs
operator|.
name|templates
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|cs
operator|.
name|templates
argument_list|)
expr_stmt|;
name|this
operator|.
name|response
operator|=
name|cs
operator|.
name|response
expr_stmt|;
name|this
operator|.
name|baseURI
operator|=
name|cs
operator|.
name|baseURI
expr_stmt|;
name|this
operator|.
name|currentBuilder
operator|=
name|cs
operator|.
name|currentBuilder
operator|!=
literal|null
condition|?
name|cs
operator|.
name|currentBuilder
operator|.
name|clone
argument_list|()
else|:
literal|null
expr_stmt|;
block|}
specifier|public
name|void
name|setCurrentBuilder
parameter_list|(
name|UriBuilder
name|currentBuilder
parameter_list|)
block|{
name|this
operator|.
name|currentBuilder
operator|=
name|currentBuilder
expr_stmt|;
block|}
specifier|public
name|UriBuilder
name|getCurrentBuilder
parameter_list|()
block|{
return|return
name|currentBuilder
return|;
block|}
specifier|public
name|void
name|setBaseURI
parameter_list|(
name|URI
name|baseURI
parameter_list|)
block|{
name|this
operator|.
name|baseURI
operator|=
name|baseURI
expr_stmt|;
block|}
specifier|public
name|URI
name|getBaseURI
parameter_list|()
block|{
return|return
name|baseURI
return|;
block|}
specifier|public
name|void
name|setResponse
parameter_list|(
name|Response
name|r
parameter_list|)
block|{
name|this
operator|.
name|response
operator|=
name|r
expr_stmt|;
block|}
specifier|public
name|Response
name|getResponse
parameter_list|()
block|{
return|return
name|response
return|;
block|}
specifier|public
name|void
name|setRequestHeaders
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|requestHeaders
parameter_list|)
block|{
name|this
operator|.
name|requestHeaders
operator|=
name|requestHeaders
expr_stmt|;
block|}
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getRequestHeaders
parameter_list|()
block|{
return|return
name|requestHeaders
return|;
block|}
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getTemplates
parameter_list|()
block|{
return|return
name|templates
return|;
block|}
specifier|public
name|void
name|setTemplates
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|)
block|{
if|if
condition|(
name|templates
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|templates
operator|=
name|map
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
block|{
name|templates
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|templates
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|requestHeaders
operator|.
name|clear
argument_list|()
expr_stmt|;
name|response
operator|=
literal|null
expr_stmt|;
name|currentBuilder
operator|=
name|UriBuilder
operator|.
name|fromUri
argument_list|(
name|baseURI
argument_list|)
expr_stmt|;
name|templates
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|ClientState
name|newState
parameter_list|(
name|URI
name|newBaseURI
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|templatesMap
parameter_list|)
block|{
name|ClientState
name|state
init|=
operator|new
name|LocalClientState
argument_list|(
name|newBaseURI
argument_list|)
decl_stmt|;
if|if
condition|(
name|headers
operator|!=
literal|null
condition|)
block|{
name|state
operator|.
name|setRequestHeaders
argument_list|(
name|headers
argument_list|)
expr_stmt|;
block|}
comment|// we need to carry the template parameters forward
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|newTemplateParams
init|=
name|templates
decl_stmt|;
if|if
condition|(
name|newTemplateParams
operator|!=
literal|null
operator|&&
name|templatesMap
operator|!=
literal|null
condition|)
block|{
name|newTemplateParams
operator|.
name|putAll
argument_list|(
name|templatesMap
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|newTemplateParams
operator|=
name|templatesMap
expr_stmt|;
block|}
name|state
operator|.
name|setTemplates
argument_list|(
name|newTemplateParams
argument_list|)
expr_stmt|;
return|return
name|state
return|;
block|}
block|}
end_class

end_unit

